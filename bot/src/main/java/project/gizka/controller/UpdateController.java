//package project.gizka.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.RestTemplate;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.objects.Message;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import project.gizka.appUser.dto.CreateAppUserDto;
//
//import java.util.Objects;
//
//@Component
//public class UpdateController {
//
//    private TelegramBot telegramBot;
//    private final RestTemplate restTemplate;
//    @Value("${rest.api.base.url}")
//    private String baseUrl;
//
//    @Autowired
//    public UpdateController(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    public void processCommand(Update update) {
//        Message originalMessage = update.getMessage();
//        String command = originalMessage.getText();
//        SendMessage message = new SendMessage();
//        Long chatId = originalMessage.getChatId();
//        ResponseEntity<?> response;
//        String responseMessage;
//
//        try {
//            switch (command) {
//                case "/get" -> {
//                    SendMessage sendMessage = new SendMessage();
//                    sendMessage.setChatId(chatId);
//                    sendMessage.setText("Введи id пользователя");
//                    telegramBot.sendAnswerMessage(sendMessage);
//                    response = restTemplate.getForEntity(baseUrl + "/user/" + 1, Object.class);
//
//                    if (response.getStatusCode() == HttpStatus.OK) {
//                        responseMessage = Objects.requireNonNull(response.getBody()).toString();
//                    } else {
//                        responseMessage = "An error occurred while trying to get user";
//                    }
//
//                    message.setText(responseMessage);
//                    message.setChatId(chatId);
//
//                    telegramBot.sendAnswerMessage(message);
//                }
//                case "/create" -> {
//                    CreateAppUserDto userDto = new CreateAppUserDto();
//                    userDto.setChat(chatId.toString());
//                    userDto.setLine("UPDATED LINE, CHECK TIME");
//
//                    response = restTemplate.postForEntity(baseUrl + "/user/create", userDto, CreateAppUserDto.class);
//
//                    if (response.getStatusCode() == HttpStatus.CREATED) {
//                        responseMessage = Objects.requireNonNull(response.getBody()).toString() + "\nNew user created";
//                    } else {
//                        responseMessage = "An error occurred while trying to create a new user";
//                    }
//
//                    message.setText(responseMessage);
//                    message.setChatId(chatId);
//
//                    telegramBot.sendAnswerMessage(message);
//                }
//            }
//
//
//        } catch (HttpClientErrorException.NotFound e) {
//            String responseBody = e.getResponseBodyAsString();
//            message.setChatId(originalMessage.getChatId().toString());
//            message.setText(responseBody);
//            telegramBot.sendAnswerMessage(message);
//
//        } catch (Exception e) {
//            String responseBody = e.getMessage();
//            message.setChatId(originalMessage.getChatId().toString());
//            message.setText(responseBody);
//            telegramBot.sendAnswerMessage(message);
//        }
//    }
//
//    public void setBot(TelegramBot telegramBot) {
//        this.telegramBot = telegramBot;
//    }
//}
