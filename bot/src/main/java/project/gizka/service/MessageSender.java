package project.gizka.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import project.gizka.bot.MessageType;
import project.gizka.bot.TelegramBot;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Component
public class MessageSender implements Runnable {

    private final TelegramBot telegramBot;
    private final int SLEEP_TIME = 1000;
    private Queue<Integer> idMessageToDelete = new LinkedList<>();

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
            switch (messageType) {
                case TEXT -> {
                    SendMessage sendMessage = (SendMessage) object;
                    Message messageToDelete = telegramBot.execute(sendMessage);
                    if (sendMessage.getText().contains("Ожидание"))
                        idMessageToDelete.add(messageToDelete.getMessageId());
                }
                case PHOTO -> {
                    SendPhoto sendPhoto = (SendPhoto) object;
                    telegramBot.execute(sendPhoto);
                }
                case DELETE -> {
                    DeleteMessage deleteMessage = (DeleteMessage) object;
                    deleteMessage.setMessageId(idMessageToDelete.poll());
                    telegramBot.execute(deleteMessage);
                }
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
