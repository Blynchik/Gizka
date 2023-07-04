package project.gizka.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final String botUsername;
    private final String botToken;
    private final UpdateController updateController;

    @Autowired
    public TelegramBot(TelegramBotsApi telegramBotsApi,
                       @Value("${telegram-bot.name}") String botUsername,
                       @Value("${telegram-bot.token}") String botToken,
                       UpdateController updateController) throws TelegramApiException {
        this.botUsername = botUsername;
        this.botToken = botToken;
        this.updateController = updateController;
        telegramBotsApi.registerBot(this);
    }

    @PostConstruct
    public void init(){
        updateController.setBot(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
            updateController.processUpdate(update);
    }

    public void sendAnswerMessage(SendMessage message) {
        if (message != null) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return this.botUsername;
    }

    @Override
    public String getBotToken(){
        return this.botToken;
    }
}
