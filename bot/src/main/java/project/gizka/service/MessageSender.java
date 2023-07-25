package project.gizka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import project.gizka.bot.TelegramBot;

import java.util.Map;
import java.util.Queue;

@Component
public class MessageSender implements Runnable {

    private final TelegramBot telegramBot;
    private final String MESSAGE_TO_DELETE = "Ожидание...";
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
        String chatId = sendMessage.getChatId();
        String text = sendMessage.getText();
        Map<String, Message> deletePool = telegramBot.getDeletePool();
        try {
            if(deletePool.containsKey(chatId)){
                Message message = deletePool.get(chatId);
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setMessageId(message.getMessageId());
                deleteMessage.setChatId(chatId);
                telegramBot.execute(deleteMessage);
                deletePool.remove(chatId);
            }

            Message message = telegramBot.execute(sendMessage);

            if(text.equals(MESSAGE_TO_DELETE)){
                deletePool.put(chatId,message);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
