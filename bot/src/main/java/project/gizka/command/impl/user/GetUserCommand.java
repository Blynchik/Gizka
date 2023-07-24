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
public class GetUserCommand extends AbstractCommand {
    private final String command;
    private final RestClient restClient;
    private final int numOfArgs = 1;
    private String userId;

    public GetUserCommand(String command, RestClient restClient) {
        super.setNumOfResponses(numOfArgs + 1);
        this.command = command;
        this.restClient = restClient;
    }

    @Override
    public Queue<SendMessage> getMessages(Update update) {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        Queue<SendMessage> messages = new LinkedList<>();
        String text = "";

        if (this.getReadiness() == numOfArgs) {
            userId = message.getText();
            text = restClient.getUserById(userId);
            messages.add(new SendMessage(chatId,text));
            improveReadiness();
        } else if (this.getReadiness() == 0) {
            text = askId();
            messages.add(new SendMessage(chatId,text));
            improveReadiness();
        }

        return messages;
    }

    private String askId() {
        return "Введите id пользователя";
    }
}
