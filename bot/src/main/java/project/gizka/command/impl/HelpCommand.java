package project.gizka.command.impl;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import project.gizka.command.AbstractCommand;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Getter
@Setter
public class HelpCommand extends AbstractCommand {

    private final List<String> commands;
    private final int requiredArgs = 0;

    public HelpCommand(List<String> commands) {
        super.setNumOfResponses(requiredArgs + 1);
        this.commands = commands;
    }

    @Override
    public Queue<SendMessage> getMessages(Update update) {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        Queue<SendMessage> messages = new LinkedList<>();
        String text = this.getCommandList();
        messages.add(new SendMessage(chatId, text));
        improveReadiness();
        return messages;
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
