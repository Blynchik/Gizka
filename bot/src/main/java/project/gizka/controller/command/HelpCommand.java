package project.gizka.controller.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import project.gizka.controller.AbstractCommand;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class HelpCommand extends AbstractCommand {

    private final List<String> commands;

    public HelpCommand(List<String> commands) {
        super(0, "/help");
        this.commands = commands;
    }


    private String getCommandList() {

        StringBuilder text = new StringBuilder();
        text.append("Все команды бота:").append("\n");

        for (String command : commands) {
            text.append(command).append("\n");
        }

        return text.toString();
    }

    @Override
    protected Queue<?> getMessages(Update update) throws Exception {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        Queue<SendMessage> messages = new LinkedList<>();
        String text = this.getCommandList();
        messages.add(new SendMessage(chatId, text));
        improveReadiness();
        return messages;
    }
}
