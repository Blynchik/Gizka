package project.gizka.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import project.gizka.command.Command;
import project.gizka.service.RestClient;

public class CreateCommand extends AbstractCommand {

    private final String command;
    private final RestClient restClient;

    public CreateCommand(String command, RestClient restClient){
        super(1);
        this.command = command;
        this.restClient = restClient;
    }


    @Override
    public SendMessage handle(Update update) {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        return new SendMessage(chatId, askSlogan());
    }

    @Override
    public SendMessage callback(Update update) {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        String text = restClient.createUser(chatId, message.getText());
        return new SendMessage(chatId, text);
    }

    private String askSlogan(){
        return "Введите девиз";
    }
}
