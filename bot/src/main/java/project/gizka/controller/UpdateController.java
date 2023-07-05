package project.gizka.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

@Component
public class UpdateController {

    private TelegramBot telegramBot;

    String url = "http://localhost:8080/api/user/";

    public void processUpdate(Update update) {
        if (update == null) {
            return;
        }

        if (update.hasMessage()) {
            Message originalMessage = update.getMessage();
            String text = originalMessage.getText();
            final RestTemplate restTemplate = new RestTemplate();
           try{
                var response = restTemplate.getForEntity(url + text, Object.class);
                String responseBody = Objects.requireNonNull(response.getBody()).toString();
                SendMessage message = new SendMessage();
                message.setChatId(originalMessage.getChatId().toString());
                message.setText(responseBody);
                telegramBot.sendAnswerMessage(message);
            } catch (HttpClientErrorException.NotFound e){
                String responseBody = e.getResponseBodyAsString();
                SendMessage message = new SendMessage();
                message.setChatId(originalMessage.getChatId().toString());
                message.setText(responseBody);
                telegramBot.sendAnswerMessage(message);
            } catch (Exception e){
               String responseBody = e.getMessage();
               SendMessage message = new SendMessage();
               message.setChatId(originalMessage.getChatId().toString());
               message.setText(responseBody);
               telegramBot.sendAnswerMessage(message);
           }
        }else {
            return;
        }
    }

    public void setBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }
}
