package project.gizka.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import project.gizka.command.AbstractCommand;
import project.gizka.service.RestClient;

public class CreateCommand extends AbstractCommand {

    private final String command;
    private final RestClient restClient;
    private static final int NUM_OF_ARGS = 1;
    private String slogan;



    public CreateCommand(String command, RestClient restClient){
        super(NUM_OF_ARGS);
        this.command = command;
        this.restClient = restClient;
    }


    @Override
    public SendMessage handle(Update update) {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        String text = "";

        if (this.getState() == NUM_OF_ARGS + 1){
            slogan = message.getText();
            text = restClient.createUser(chatId, slogan);
        }

        if(this.getState() == 1){
            text = askSlogan();
        }

        return new SendMessage(chatId, text);
    }

    private String askSlogan(){
        return "Введите девиз";
    }
}
