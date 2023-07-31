package project.gizka.controller.command;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import project.gizka.client.RestClient;
import project.gizka.controller.AbstractCommand;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

@Getter
@Setter
public class StartCommand extends AbstractCommand {
    private final String startMessage;

    public StartCommand(RestClient restClient, int numOfArgs){
        super(restClient, numOfArgs, "/start");
        this.startMessage = "Rererer";
    }

    @Override
    public Queue<SendPhoto> getMessages(Update update) {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        Queue<SendPhoto> messages = new LinkedList<>();

        SendPhoto photo = new SendPhoto();
        photo.setChatId(chatId);
        File imageFile = new File("C:\\Users\\Blynchik\\Desktop\\own\\class\\bot\\src\\main\\resources\\3901.750x0.jpg");
        InputFile inputFile = new InputFile(imageFile);
        photo.setPhoto(inputFile);
        photo.setCaption(startMessage);
        messages.add(photo);
        improveReadiness();
        return messages;
    }
}
