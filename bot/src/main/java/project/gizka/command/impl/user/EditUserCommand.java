package project.gizka.command.impl.user;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import project.gizka.command.AbstractCommand;
import project.gizka.client.RestClient;

import java.util.LinkedList;
import java.util.Queue;

@Getter
@Setter
public class EditUserCommand extends AbstractCommand {

    private final String command;
    private final RestClient restClient;
    private final int numOfArgs = 2;
    private String userId;
    private String slogan;

    public EditUserCommand(String command, RestClient restClient) {
        super.setNumOfArgs(numOfArgs);
        this.command = command;
        this.restClient = restClient;
    }


    @Override
    public Queue<SendMessage> handle(Update update) {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        Queue<SendMessage> messages = new LinkedList<>();
        String text = "";

        if (this.getState() == numOfArgs + 1) {
            slogan = message.getText();
            text = restClient.updateUser(chatId, userId, slogan);
            messages.add(new SendMessage(chatId,text));
        } else if (this.getState() == 2) {
            userId = message.getText();
            text = askSlogan();
            messages.add(new SendMessage(chatId,text));
        } else if (this.getState() == 1) {
            text = askId();
            messages.add(new SendMessage(chatId,text));
        }

        return messages;
    }

    private String askSlogan() {
        return "Введите новый девиз";
    }

    private String askId() {
        return "Введите id";
    }
}
