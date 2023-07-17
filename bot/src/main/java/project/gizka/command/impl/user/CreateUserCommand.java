package project.gizka.command.impl.user;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import project.gizka.command.AbstractCommand;
import project.gizka.service.RestClient;

@Getter
@Setter
public class CreateUserCommand extends AbstractCommand {

    private final String command;
    private final RestClient restClient;
    private final int numOfArgs = 1;
    private String slogan;


    public CreateUserCommand(String command, RestClient restClient) {
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
            text = restClient.createUser(chatId, slogan);
        } else if (this.getState() == 1) {
            text = askSlogan();
        }

        return new SendMessage(chatId, text);
    }

    private String askSlogan() {
        return "Введите девиз";
    }
}
