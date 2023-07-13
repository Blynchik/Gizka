package project.gizka.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class HelpCommand extends AbstractCommand {

    private final List<String> commands;

    public HelpCommand(List<String> commands) {
        super(1);
        this.commands = commands;
    }

    @Override
    public SendMessage handle(Update update) {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        return new SendMessage(chatId, getCommandList());
    }

    @Override
    public SendMessage callback(Update update) {
        return handle(update);
    }

    private String getCommandList() {
        StringBuilder text = new StringBuilder();
        text.append("Все команды бота:").append("\n");

        for (String command : commands) {
            text.append(command).append("\n");
        }

        return text.toString();
    }
}
