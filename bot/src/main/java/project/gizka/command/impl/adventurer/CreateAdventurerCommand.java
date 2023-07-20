package project.gizka.command.impl.adventurer;

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
public class CreateAdventurerCommand extends AbstractCommand {

    private final String command;
    private final RestClient restClient;
    private final int numOfArgs = 2;
    private String firstName;
    private String lastName;

    public CreateAdventurerCommand(String command, RestClient restClient) {
        super.setNumOfArgs(numOfArgs);
        this.command = command;
        this.restClient = restClient;
    }

    @Override
    public Queue<SendMessage> handle(Update update) throws Exception {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        Queue<SendMessage> messages = new LinkedList<>();
        String text = "";

        if (this.getState() == numOfArgs + 1) {
            lastName = message.getText();
            text = restClient.createAdventurer(firstName, lastName);
            messages.add(new SendMessage(chatId,text));
        } else if (this.getState() == 2) {
            firstName = message.getText();
            text = askSurname();
            messages.add(new SendMessage(chatId, text));
        } else if (this.getState() == 1) {
            text = askName();
            messages.add(new SendMessage(chatId, text));
        }
        return messages;
    }

    private String askName() {
        return "Введите имя героя";
    }

    private String askSurname() {
        return "Введите фамилию героя";
    }
}
