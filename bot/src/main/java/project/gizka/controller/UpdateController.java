package project.gizka.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;

@Component
public class UpdateController {

    private TelegramBot telegramBot;

    String url = "http://localhost:8080/api/user/";

    public void processUpdate(Update update){
        if(update == null){
            return;
        }

        if(update.hasMessage()){
            Message originalMessage = update.getMessage();
            String text = originalMessage.getText();
            final RestTemplate restTemplate = new RestTemplate();
            HashMap response = restTemplate.getForObject(url + text, HashMap.class);
            System.out.println(response);
            SendMessage message = new SendMessage();
            message.setChatId(originalMessage.getChatId().toString());
            message.setText(response.toString());
            telegramBot.sendAnswerMessage(message);
        } else {
            return;
        }
    }

    public void setBot(TelegramBot telegramBot){
        this.telegramBot = telegramBot;
    }
}
