package project.gizka.controller.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import project.gizka.client.RestClient;
import project.gizka.controller.AbstractCommand;

import java.util.LinkedList;
import java.util.Queue;

public class CreateHeroCommand extends AbstractCommand {
    private final RestClient restClient;
    private String firstName;
    private String lastName;

    public CreateHeroCommand(RestClient restClient) {
        super(2, "/create");
        this.restClient = restClient;
    }

    @Override
    public Queue<SendMessage> getMessages(Update update) throws Exception {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        Queue<SendMessage> messages = new LinkedList<>();
        String text = "";

        if (this.getState() == this.getNumOfArgs()) {
            lastName = message.getText();
            text = restClient.createAdventurer(firstName, lastName, chatId);
            messages.add(new SendMessage(chatId,text));
            improveReadiness();
        } else if (this.getState() == 1) {
            firstName = message.getText();
            text = askSurname();
            messages.add(new SendMessage(chatId, text));
            improveReadiness();
        } else if (this.getState() == 0) {
            text = askName();
            messages.add(new SendMessage(chatId, text));
            improveReadiness();
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