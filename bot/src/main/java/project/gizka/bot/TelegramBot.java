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
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final CommandMap validCommands;
    private final TelegramBotConfig botConfig;
    private final Map<String, AbstractCommand> notCompletedCommands;
    private final Map<String, ResponseBuffer> responseBuffers;

    @Autowired
    public TelegramBot(TelegramBotConfig botConfig,
                       CommandMap validCommands) {
        this.botConfig = botConfig;
        this.validCommands = validCommands;
        this.responseBuffers = new ConcurrentHashMap<>();
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
            ResponseBuffer currentResponseBuffer = getCurrentResponseBuffer(chatId);

            try {
                currentResponseBuffer.addMessage(currentCommand.handle(update));
            } catch (Exception e) {
                e.printStackTrace();
                String responseBody = e.getMessage();
                currentResponseBuffer.addMessage(new SendMessage(chatId, responseBody));
            }
            sendResponseMessage(currentResponseBuffer);

            if (currentCommand.isDone()) {
                removeCompletedCommand(chatId);
                if (!currentResponseBuffer.hasMessages()) {
                    removeMessageBuffer(chatId);
                }
            }
        }
    }

    private void sendResponseMessage(ResponseBuffer responseBuffer) {
        while (responseBuffer.hasMessages()) {
            SendMessage message = responseBuffer.getMessage();
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private ResponseBuffer getCurrentResponseBuffer(String chatId) {
        ResponseBuffer responseBuffer;
        if (responseBuffers.containsKey(chatId)) {
            responseBuffer = responseBuffers.get(chatId);
        } else {
            responseBuffer = new ResponseBuffer();
            responseBuffers.put(chatId, responseBuffer);
        }
        return responseBuffer;
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
        responseBuffers.remove(chatId);
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