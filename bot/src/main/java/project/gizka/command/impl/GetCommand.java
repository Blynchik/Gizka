package project.gizka.command.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import project.gizka.command.Command;
import project.gizka.service.RestClient;




public class GetCommand extends AbstractCommand {
    private final String command;
    private final RestClient restClient;

    public GetCommand(String command, RestClient restClient){
        super(1);
        this.command = command;
        this.restClient = restClient;
    }

    @Override
    public SendMessage handle(Update update) {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        return new SendMessage(chatId, askId());
    }

    @Override
    public SendMessage callback(Update update) {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        String text = restClient.getUserById(message.getText());
        return new SendMessage(chatId, text);
    }

    private String askId(){
        return "Введите id";
    }
}
