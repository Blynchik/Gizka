package project.gizka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import project.gizka.bot.MessageType;
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
                Queue<Object> responseQueue = telegramBot.getCommonResponsePool().getCommonPool();
                synchronized (responseQueue) {
                    while (!responseQueue.isEmpty()) {
                        Object message = responseQueue.poll();
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

    private void send(Object object) {

        try {
            MessageType messageType = MessageType.getMessageType(object);
            System.out.println(messageType);
            switch (messageType) {
                case TEXT -> {
                    SendMessage sendMessage = (SendMessage) object;
                    telegramBot.execute(sendMessage);
                }
                case PHOTO -> {
                    SendPhoto sendPhoto = (SendPhoto) object;
                    telegramBot.execute(sendPhoto);
                }
                case DELETE -> {
                    DeleteMessage deleteMessage = (DeleteMessage) object;
                    telegramBot.execute(deleteMessage);
                }
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
