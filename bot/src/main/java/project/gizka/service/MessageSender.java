package project.gizka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import project.gizka.bot.TelegramBot;

import java.util.Queue;

@Component
public class MessageSender implements Runnable {

    private final TelegramBot telegramBot;

    private final int SLEEP_TIME = 1000;

    @Autowired
    public MessageSender(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }


    @Override
    public void run() {
        try {
            while (true) {
                telegramBot.getCommonResponsePool().addPrivatePoolToCommonPool();
                Queue<SendMessage> responseQueue = telegramBot.getCommonResponsePool().getCommonPool();
                synchronized(responseQueue) {
                    while (!responseQueue.isEmpty()) {
                        SendMessage message = responseQueue.poll();
                        send(message);
                    }
                }
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void send(SendMessage sendMessage) {
        try {
            telegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
