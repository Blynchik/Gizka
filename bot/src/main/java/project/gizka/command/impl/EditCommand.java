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
public class EditCommand extends AbstractCommand {

    private final String command;
    private final RestClient restClient;
    private final int numOfArgs = 2;
    private String userId;
    private String slogan;

    public EditCommand(String command, RestClient restClient) {
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
            slogan = message.getText();
            text = restClient.updateUser(chatId, userId, slogan);
        } else if (this.getState() == 2) {
            userId = message.getText();
            text = askSlogan();
        } else if (this.getState() == 1) {
            text = askId();
        }

        return new SendMessage(chatId, text);
    }

    private String askSlogan() {
        return "Введите новый девиз";
    }

    private String askId() {
        return "Введите id";
    }
}
