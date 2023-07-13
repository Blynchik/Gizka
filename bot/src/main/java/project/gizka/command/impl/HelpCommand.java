package project.gizka.command.impl;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import project.gizka.command.AbstractCommand;

import java.util.List;

@Getter
@Setter
public class HelpCommand extends AbstractCommand {

    private final List<String> commands;
    private final int numOfArgs = 0;

    public HelpCommand(List<String> commands) {
        super.setNumOfArgs(numOfArgs);
        this.commands = commands;
    }

    @Override
    public SendMessage handle(Update update) {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        String text = this.getCommandList();
        return new SendMessage(chatId, text);
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
