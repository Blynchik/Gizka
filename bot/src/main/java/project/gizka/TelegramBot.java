package project.gizka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import project.gizka.command.CommandMap;
import project.gizka.command.AbstractCommand;

import java.util.Map;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final String botUsername;
    private final String botToken;
    private final CommandMap commands;
    private AbstractCommand currentCommand;

    @Autowired
    public TelegramBot(TelegramBotsApi telegramBotsApi,
                       @Value("${telegram-bot.name}") String botUsername,
                       @Value("${telegram-bot.token}") String botToken,
                       CommandMap commands) throws TelegramApiException {
        this.botUsername = botUsername;
        this.botToken = botToken;
        this.commands = commands;
        telegramBotsApi.registerBot(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message originalMessage = update.getMessage();
            String chatId = originalMessage.getChatId().toString();

            if (originalMessage.hasText()) {
                String commandKey = originalMessage.getText();
                Map<String, AbstractCommand> commandMap = commands.getCommands();
                SendMessage message;

                if (commandMap.containsKey(commandKey)) {
                    currentCommand = commandMap.get(commandKey);
                }

                if (currentCommand == null) {
                    message = new SendMessage(chatId, "Unknown command");
                    sendAnswerMessage(message);
                }

                if (!currentCommand.checkReadyForProcess()) {
                    try {
                        message = currentCommand.handle(update);
                    } catch (HttpClientErrorException.NotFound e) {
                        String responseBody = e.getResponseBodyAsString();
                        message = new SendMessage(chatId, responseBody);
                        sendAnswerMessage(message);
                    } catch (Exception e) {
                        String responseBody = e.getMessage();
                        message = new SendMessage(chatId, responseBody);
                        sendAnswerMessage(message);
                    }
                    sendAnswerMessage(message);
                }
            }
        }
    }

    public void sendAnswerMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            //TODO
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return this.botUsername;
    }

    @Override
    public String getBotToken() {
        return this.botToken;
    }
}
