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
    private final CommandMap commands;
    private final TelegramBotConfig botConfig;
    private final Map<String, AbstractCommand> userCommands;
    private final MessageBuffer messageBuffer;

    @Autowired
    public TelegramBot(TelegramBotConfig botConfig, CommandMap commands) {
        this.botConfig = botConfig;
        this.commands = commands;
        this.messageBuffer = new MessageBuffer();
        this.userCommands = new ConcurrentHashMap<>();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message originalMessage = update.getMessage();
            String chatId = originalMessage.getChatId().toString();

            if (originalMessage.hasText()) {
                String commandKey = originalMessage.getText();
                AbstractCommand currentCommand;

                if (userCommands.containsKey(chatId)) { //если пользователем уже была введена команда
                    currentCommand = userCommands.get(chatId);
                    handleCommand(currentCommand, update, chatId);

                } else { //если команда еще не была введена

                    if (commands.getCommands().containsKey(commandKey)) { //если команда валидна
                        currentCommand = commands.getCommands().get(commandKey);
                        userCommands.put(chatId, currentCommand);
                        handleCommand(currentCommand,update,chatId);
                    }
                }
            }
        }
    }

    private void handleCommand(AbstractCommand currentCommand, Update update, String chatId) {
        if (currentCommand != null && !currentCommand.isDone()) {
            try {
                messageBuffer.addMessage(currentCommand.handle(update));
            } catch (Exception e) {
                e.printStackTrace();
                String responseBody = e.getMessage();
                messageBuffer.addMessage(new SendMessage(chatId, responseBody));
            }
            sendResponseMessage();

            if (currentCommand.isDone()) {
                removeCompletedCommand(chatId);
            }
        }
    }

    private void removeCompletedCommand(String chatId) {
        userCommands.remove(chatId);
    }

    private void sendResponseMessage() {
        while (messageBuffer.hasMessages()) {
            SendMessage message = messageBuffer.getMessage();
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
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