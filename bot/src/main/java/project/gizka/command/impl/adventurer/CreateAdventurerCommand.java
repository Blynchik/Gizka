package project.gizka.command.impl.adventurer;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import project.gizka.command.AbstractCommand;
import project.gizka.service.RestClient;

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
    public SendMessage handle(Update update) throws Exception {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        String text = "";

        if (this.getState() == numOfArgs + 1) {
            lastName = message.getText();
            text = restClient.createAdventurer(firstName, lastName);
        } else if (this.getState() == 2) {
            firstName = message.getText();
            text = askSurname();
        } else if (this.getState() == 1) {
            text = askName();
        }
        return new SendMessage(chatId, text);
    }

    private String askName() {
        return "Введите имя героя";
    }

    private String askSurname() {
        return "Введите фамилию героя";
    }
}
