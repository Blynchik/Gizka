package project.gizka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import project.gizka.bot.TelegramBot;

import java.util.Map;
import java.util.Queue;

@Component
public class CommandController {

    private final TelegramBot telegramBot;

    @Autowired
    public CommandController(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void handleCommand(Update update, String chatId, String commandKey) {
        AbstractCommand currentCommand = getCurrentCommand(chatId, commandKey);
        if (currentCommand != null && !currentCommand.isDone()) {
            try {
                handle(update, currentCommand);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (currentCommand.isDone()) {
                telegramBot.getNotCompletedCommands().remove(chatId);
            }
        }
    }

    private void handle(Update update, AbstractCommand abstractCommand) throws Exception {
        String chatId = update.getMessage().getChatId().toString();

        Queue<?> messages = abstractCommand.getMessages(update);
        putResponseToPrivatePool(messages, chatId);
    }

    private void putResponseToPrivatePool(Queue<?> messages, String chatId) {
        telegramBot.getCommonResponsePool().getPrivateResponsePools().addResponseToPrivatePool(messages, chatId);
    }

    private AbstractCommand getCurrentCommand(String chatId, String commandKey) {
        Map<String, AbstractCommand> notCompletedCommands = telegramBot.getNotCompletedCommands();
        Map<String, AbstractCommand> validCommands = telegramBot.getValidCommands().getCommands();
        AbstractCommand currentCommand = validCommands.get("/help");

        if (validCommands.containsKey(commandKey)) {
            notCompletedCommands.remove(chatId);
            currentCommand = validCommands.get(commandKey);
            notCompletedCommands.put(chatId, currentCommand);
        } else {
            if (!validCommands.containsKey(commandKey) && notCompletedCommands.containsKey(chatId)) {
                currentCommand = notCompletedCommands.get(chatId);
            }
        }
        return currentCommand;
    }
}
