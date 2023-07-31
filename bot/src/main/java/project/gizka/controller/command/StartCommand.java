package project.gizka.controller.command;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import project.gizka.client.RestClient;
import project.gizka.controller.AbstractCommand;

import java.util.LinkedList;
import java.util.Queue;

@Getter
@Setter
public class StartCommand extends AbstractCommand {

    private final String command;
    private final String startMessage;

    public StartCommand(RestClient restClient, int numOfArgs){
        super(restClient, numOfArgs);
        this.command = "/start";
        this.startMessage = "Rererer";
    }

    @Override
    public Queue<SendPhoto> getMessages(Update update) {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        Queue<SendPhoto> messages = new LinkedList<>();

        SendPhoto photo = new SendPhoto();
        photo.setChatId(chatId);
        InputFile inputFile = new InputFile();
        inputFile.setMedia("https://s1.1zoom.ru/big0/697/Love_Night_Moon_Trees_Silhouette_Two_Dating_576752_1280x853.jpg");
        photo.setPhoto(inputFile);
        photo.setCaption(startMessage);
        messages.add(photo);
        improveReadiness();
        return messages;
    }
}
