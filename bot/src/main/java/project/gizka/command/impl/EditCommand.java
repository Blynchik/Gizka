package project.gizka.command.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import project.gizka.command.Command;
import project.gizka.service.RestClient;

public class EditCommand extends AbstractCommand {

    private final String command;
    private final RestClient restClient;
    private String id;
    private String slogan;

    public EditCommand(String command, RestClient restClient) {
        super(1);
        this.command = command;
        this.restClient = restClient;
    }


    @Override
    public SendMessage handle(Update update) {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();

        String ask = "";

        if (this.getState() == 2) {
            ask = askSlogan();
            slogan = message.getText();
        }

        if (this.getState() == 1) {
            ask = askId();
        }

        return new SendMessage(chatId, ask);
    }

    @Override
    public SendMessage callback(Update update) {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();

        String text = "";

        if (this.getState() == 2){
            slogan = message.getText();
            text = restClient.updateUser(id, chatId, slogan);
        }

        if (this.getState() == 1) {
            id = message.getText();
            this.setState(this.getState()+1);
            text = askSlogan();
        }

        return new SendMessage(chatId, text);
    }

    private String askSlogan() {
        return "Введите новый девиз";
    }

    private String askId() {
        return "Введите id";
    }
}
