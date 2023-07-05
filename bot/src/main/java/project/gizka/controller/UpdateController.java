package project.gizka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import project.gizka.service.UserCommandService;

@Component
public class UpdateController {

    private TelegramBot telegramBot;

    private final UserCommandService userCommandService;

    @Autowired
    public UpdateController(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    public void processUpdate(Update update) {
        if (update == null) {
            //TODO log exception
            return;
        }

        if (update.hasMessage()) {
            processCommand(update);
        } else {
            //TODO log exception
            return;
        }
    }

    private void processCommand(Update update) {
        Message originalMessage = update.getMessage();

        if (originalMessage != null && originalMessage.hasText()) {
            String command = originalMessage.getText();
            String[] commandParts = command.split("/");

            try {
                if (commandParts[1].equals("user")) {
                  SendMessage response = userCommandService.returnSendMessage(commandParts);
                  response.setChatId(originalMessage.getChatId());
                  telegramBot.sendAnswerMessage(response);
                } else {
                    //TODO log not user directory
                }


            } catch (HttpClientErrorException.NotFound e) {
                String responseBody = e.getResponseBodyAsString();
                SendMessage message = new SendMessage();
                message.setChatId(originalMessage.getChatId().toString());
                message.setText(responseBody);
                telegramBot.sendAnswerMessage(message);

            } catch (Exception e) {
                String responseBody = e.getMessage();
                SendMessage message = new SendMessage();
                message.setChatId(originalMessage.getChatId().toString());
                message.setText(responseBody);
                telegramBot.sendAnswerMessage(message);
            }
        } else {
            //TODO log exception if no text
            return;
        }

    }

    public void setBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }
}
