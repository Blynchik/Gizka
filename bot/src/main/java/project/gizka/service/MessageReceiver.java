package project.gizka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import project.gizka.bot.TelegramBot;
import project.gizka.command.AbstractCommand;

import java.util.Queue;

@Component
public class MessageReceiver implements Runnable {
    private final int SLEEP_TIME = 1000;
    private final TelegramBot telegramBot;

    @Autowired
    public MessageReceiver(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }


    @Override
    public void run() {
        try {
            while (true) {
                Queue<Update> updateQueue = telegramBot.getUpdatePool();
                while (!updateQueue.isEmpty()) {
                    Update update = updateQueue.poll();
                    if (update != null) {
                        Message originalMessage = update.getMessage();
                        String chatId = originalMessage.getChatId().toString();
                        String commandKey = originalMessage.getText();
                        AbstractCommand currentCommand = getCurrentCommand(chatId, commandKey);
                        handleCommand(currentCommand, update, chatId);
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

    private AbstractCommand getCurrentCommand(String chatId, String commandKey) {
        AbstractCommand abstractCommand;

        if (telegramBot.getNotCompletedCommands().containsKey(chatId)) {
            abstractCommand = telegramBot.getNotCompletedCommands().get(chatId);
        } else if (telegramBot.getValidCommands().getCommands().containsKey(commandKey)) {
            abstractCommand = telegramBot.getValidCommands().getCommands().get(commandKey);
            telegramBot.getNotCompletedCommands().put(chatId, abstractCommand);
        } else {
            abstractCommand = telegramBot.getValidCommands().getCommands().get("/help");
        }

        return abstractCommand;
    }

    private void handleCommand(AbstractCommand currentCommand, Update update, String chatId) {
        if (currentCommand != null && !currentCommand.isDone()) {
            try {
                currentCommand.handle(update);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (currentCommand.isDone()) {
                removeCompletedCommand(chatId);
            }
        }
    }

    private void removeCompletedCommand(String chatId) {
        telegramBot.getNotCompletedCommands().remove(chatId);
    }
}
