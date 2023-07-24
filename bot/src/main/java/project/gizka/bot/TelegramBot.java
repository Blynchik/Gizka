package project.gizka.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import project.gizka.command.AbstractCommand;
import project.gizka.command.CommandMap;
import project.gizka.config.TelegramBotConfig;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final CommandMap validCommands;
    private final TelegramBotConfig botConfig;
    private final Map<String, AbstractCommand> notCompletedCommands;
    private final Map<String, ResponsePool> responsePools;

    @Autowired
    public TelegramBot(TelegramBotConfig botConfig,
                       CommandMap validCommands) {
        this.botConfig = botConfig;
        this.validCommands = validCommands;
        this.responsePools = new ConcurrentHashMap<>();
        this.notCompletedCommands = new ConcurrentHashMap<>();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message originalMessage = update.getMessage();
            String chatId = originalMessage.getChatId().toString();

            if (originalMessage.hasText()) {
                String commandKey = originalMessage.getText();
                AbstractCommand currentCommand = getCurrentCommand(chatId, commandKey);
                handleCommand(currentCommand, update, chatId);
            }
        }
    }

    private void handleCommand(AbstractCommand currentCommand, Update update, String chatId) {
        if (currentCommand != null && !currentCommand.isDone()) {
            ResponsePool currentResponsePool = getCurrentResponseBuffer(chatId);

            try {
                currentResponsePool.addMessage(currentCommand.handle(update));
            } catch (Exception e) {
                e.printStackTrace();
                String responseBody = e.getMessage();
                currentResponsePool.addMessage(new SendMessage(chatId, responseBody));
            }
            sendResponseMessage(currentResponsePool);

            if (currentCommand.isDone()) {
                removeCompletedCommand(chatId);
                if (!currentResponsePool.hasMessages()) {
                    removeMessageBuffer(chatId);
                }
            }
        }
    }

    private void sendResponseMessage(ResponsePool responsePool) {
        while (responsePool.hasMessages()) {
            SendMessage message = responsePool.getMessage();
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private ResponsePool getCurrentResponseBuffer(String chatId) {
        ResponsePool responsePool;
        if (responsePools.containsKey(chatId)) {
            responsePool = responsePools.get(chatId);
        } else {
            responsePool = new ResponsePool();
            responsePools.put(chatId, responsePool);
        }
        return responsePool;
    }

    private AbstractCommand getCurrentCommand(String chatId, String commandKey) {
        AbstractCommand abstractCommand;

        if (notCompletedCommands.containsKey(chatId)) {
            abstractCommand = notCompletedCommands.get(chatId);
        } else if (validCommands.getCommands().containsKey(commandKey)) {
            abstractCommand = validCommands.getCommands().get(commandKey);
            notCompletedCommands.put(chatId, abstractCommand);
        } else {
            abstractCommand = validCommands.getCommands().get("/help");
        }

        return abstractCommand;
    }


    private void removeCompletedCommand(String chatId) {
        notCompletedCommands.remove(chatId);
    }

    private void removeMessageBuffer(String chatId) {
        responsePools.remove(chatId);
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }
}