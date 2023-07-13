package project.gizka.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import project.gizka.command.AbstractCommand;
import project.gizka.service.RestClient;


public class GetCommand extends AbstractCommand {
    private final String command;
    private final RestClient restClient;
    private static final int NUM_OF_ARGS = 1;
    private String userId;

    public GetCommand(String command, RestClient restClient) {
        super(NUM_OF_ARGS);
        this.command = command;
        this.restClient = restClient;
    }

    @Override
    public SendMessage handle(Update update) {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        String text = "";

        if (this.getState() == NUM_OF_ARGS + 1) {
            userId = message.getText();
            text = restClient.getUserById(userId);
        }

        if (this.getState() == 1) {
            text = askId();
        }

        return new SendMessage(chatId, text);
    }

//    @Override
//    public SendMessage callback(Update update) {
//        Message message = update.getMessage();
//        String chatId = message.getChatId().toString();
//        String text = restClient.getUserById(message.getText());
//        return new SendMessage(chatId, text);
//    }

    private String askId() {
        return "Введите id";
    }
}
