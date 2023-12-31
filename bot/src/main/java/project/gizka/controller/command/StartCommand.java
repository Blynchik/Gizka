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

    private final RestClient restClient;

    private static final String startMessage = "Добро пожаловать в мир фэнтези!\uD83C\uDF1F\uD83E\uDDDA\u200D♂\uFE0F" +
            "\n\nЗдесь ты станешь главным героем своей собственной истории, полной экшна и приключений.\n Вступай в схватку " +
            "со свирепыми монстрами, встречайся с опасностями и преодолевай испытания, чтобы завоевать славу и покорить" +
            " этот магический мир. \uD83D\uDDE1\uFE0F\uD83D\uDCA5" +
            "\n\nТы будешь прокладывать свой путь через загадочные земли, разыскивая сокровища и сражаясь за свою жизнь. " +
            "\nТак что готовь свое оружие, вступай в бой \uD83D\uDEE1\uFE0F\uD83E\uDD1C и докажи, что ты достоин быть " +
            "легендарным героем фэнтези! ✨\uD83D\uDCAA \uD83C\uDFC6" +
            "\n\nПриготовься к незабываемым приключениям и морю эмоций. \nПриятной игры! \uD83C\uDFAE\uD83D\uDD25";

    private static final String IMAGE_PATH = "D:\\нужное\\java\\own\\class\\bot\\src\\main\\resources\\magic-artifacts-travel-world-adventure.png";

    public StartCommand(RestClient restClient){
        super(0, "/start");
        this.restClient = restClient;
    }

    @Override
    public Queue<SendPhoto> getMessages(Update update) {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        String userName = message.getFrom().getUserName();

        if(userName == null){
            userName = message.getFrom().getFirstName() + " " + message.getFrom().getLastName();
        }

        if(userName == null){
            userName = chatId;
        }

        Queue<SendPhoto> messages = new LinkedList<>();

        String response = getResponseFromRest(userName, chatId);
        SendPhoto picture = getPicture(chatId, response);
        messages.add(picture);

        improveReadiness();
        return messages;
    }

    private String getResponseFromRest(String userName, String chatId) {
        return restClient.createAppUser(userName, chatId);
    }

    private SendPhoto getPicture(String chatId, String response) {
        SendPhoto photo = new SendPhoto();
        photo.setChatId(chatId);
        File imageFile = new File(IMAGE_PATH);
        InputFile inputFile = new InputFile(imageFile);
        photo.setPhoto(inputFile);


        photo.setCaption("\n"+startMessage+"\n" + response + "\n\n" +
                "Введите /create для создания своего уникального героя \uD83C\uDFAD✏\uFE0F");

        return photo;
    }
}
