package project.gizka.controller.command;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import project.gizka.client.RestClient;
import project.gizka.controller.AbstractCommand;
import project.gizka.dto.commonDto.AppUserCommonDto;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

@Getter
@Setter
public class StartCommand extends AbstractCommand {

    private static final String startMessage = "RERerrr";
    private static final String IMAGE_PATH = "C:\\Users\\Blynchik\\Desktop\\own\\class\\bot\\src\\main\\resources\\3901.750x0.jpg";

    public StartCommand(RestClient restClient, int numOfArgs){
        super(restClient, numOfArgs, "/start");
    }

    @Override
    public Queue<SendPhoto> getMessages(Update update) {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        String userName = message.getFrom().getUserName();
        Queue<SendPhoto> messages = new LinkedList<>();

        AppUserCommonDto userDto = getResponseFromDB(userName, chatId);
        SendPhoto photo = createPhoto(chatId, userDto);
        messages.add(photo);

        improveReadiness();
        return messages;
    }

    private AppUserCommonDto getResponseFromDB(String userName, String chatId) {
        return restClient.createAppUser(userName, chatId);
    }

    private SendPhoto createPhoto(String chatId, AppUserCommonDto userDto) {
        SendPhoto photo = new SendPhoto();
        photo.setChatId(chatId);
        File imageFile = new File(IMAGE_PATH);
        InputFile inputFile = new InputFile(imageFile);
        photo.setPhoto(inputFile);
        photo.setCaption(userDto.toString());
        return photo;
    }
}
