package project.gizka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import project.gizka.command.Command;
import project.gizka.command.CommandMap;
import project.gizka.command.impl.AbstractCommand;
import project.gizka.command.impl.EditCommand;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final String botUsername;
    private final String botToken;
    private final CommandMap commands;
    private final Map<String, String> bindingBy = new ConcurrentHashMap<>();

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
                    AbstractCommand command = commandMap.get(commandKey);

                    message = command.handle(update);
                    bindingBy.put(chatId, commandKey);
                    sendAnswerMessage(message);

                } else if (bindingBy.containsKey(chatId)) {
                    AbstractCommand command = commandMap.get(chatId);
                    message = commandMap.get(bindingBy.get(chatId)).callback(update);
                    bindingBy.remove(chatId);
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
