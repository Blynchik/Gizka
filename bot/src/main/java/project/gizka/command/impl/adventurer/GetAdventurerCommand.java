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
public class GetAdventurerCommand extends AbstractCommand {
    private final String command;
    private final RestClient restClient;
    private final int numOfArgs = 1;
    private String adventurerId;

    public GetAdventurerCommand(String command, RestClient restClient){
        super.setNumOfArgs(numOfArgs);
        this.command = command;
        this.restClient = restClient;
    }

    @Override
    public SendMessage handle(Update update) throws Exception {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        String text = "";

        if(this.getState() == numOfArgs+1){
            adventurerId = message.getText();
            text = restClient.getAdventurerById(adventurerId);
        } else if (this.getState() == 1){
            text = askId();
        }
        return new SendMessage(chatId, text);
    }

    private String askId(){
        return "Введите id героя";
    }
}
