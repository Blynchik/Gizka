package project.gizka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import project.gizka.bot.TelegramBot;
import project.gizka.controller.CommandController;

import java.util.Queue;

@Component
public class MessageReceiver implements Runnable {
    private final int SLEEP_TIME = 1000;
    private final TelegramBot telegramBot;
    private final CommandController commandController;

    @Autowired
    public MessageReceiver(TelegramBot telegramBot,
                           CommandController commandController) {
        this.telegramBot = telegramBot;
        this.commandController = commandController;
    }


    @Override
    public void run() {
        try {
            while (true) {
                Queue<Update> updateQueue = telegramBot.getUpdatePool();
                while (!updateQueue.isEmpty()) {
                    Update update = updateQueue.poll();
                    if (update != null) {
                        String chatId = "";
                        String commandKey = "";

                        if(update.hasMessage()) {
                            Message originalMessage = update.getMessage();
                            chatId = originalMessage.getChatId().toString();
                            commandKey = originalMessage.getText();
                        }

                        if(update.hasCallbackQuery()){
                            CallbackQuery callback = update.getCallbackQuery();
                            chatId = callback.getMessage().getChatId().toString();
                            commandKey = callback.getData();
                        }


                        commandController.handleCommand(update, chatId, commandKey);
                    }
                    try {
                        Thread.sleep(SLEEP_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
