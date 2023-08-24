package project.gizka.controller.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import project.gizka.client.RestClient;
import project.gizka.controller.AbstractCommand;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

public class CreateHeroCommand extends AbstractCommand {
    private final RestClient restClient;
    private String firstName;
    private String lastName;
    private static final String IMAGE_PATH = "D:\\нужное\\java\\own\\class\\bot\\src\\main\\resources\\logo.jpg";

    public CreateHeroCommand(RestClient restClient) {
        super(2, "/create");
        this.restClient = restClient;
    }

    @Override
    public Queue<Object> getMessages(Update update) throws Exception {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        Queue<Object> messages = new LinkedList<>();
        String text = "";

        if (this.getState() == this.getNumOfArgs()) {
            lastName = message.getText();
            text = restClient.createAdventurer(firstName, lastName, chatId);
            messages.add(getPicture(chatId, text));
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
        return "Введите имя героя ✏\uFE0F";
    }

    private String askSurname() {
        return "Введите фамилию героя ✏\uFE0F";
    }

    private SendPhoto getPicture(String chatId, String text) {
        SendPhoto photo = new SendPhoto();
        photo.setChatId(chatId);
        File imageFile = new File(IMAGE_PATH);
        InputFile inputFile = new InputFile(imageFile);
        photo.setPhoto(inputFile);


        photo.setCaption(text);

        return photo;
    }
}