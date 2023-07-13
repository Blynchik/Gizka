package project.gizka.command.impl;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import project.gizka.command.AbstractCommand;
import project.gizka.service.RestClient;


@Getter
@Setter
public class GetCommand extends AbstractCommand {
    private final String command;
    private final RestClient restClient;
    private final int numOfArgs = 1;
    private String userId;

    public GetCommand(String command, RestClient restClient) {
        super.setNumOfArgs(numOfArgs);
        this.command = command;
        this.restClient = restClient;
    }

    @Override
    public SendMessage handle(Update update) {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        String text = "";

        if (this.getState() == numOfArgs + 1) {
            userId = message.getText();
            text = restClient.getUserById(userId);
        } else if (this.getState() == 1) {
            text = askId();
        }

        return new SendMessage(chatId, text);
    }

    private String askId() {
        return "Введите id";
    }
}
