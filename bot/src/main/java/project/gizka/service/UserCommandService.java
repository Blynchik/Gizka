package project.gizka.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramBot;
import project.gizka.appUser.dto.CreateAppUserDto;
import project.gizka.appUser.model.AppUser;

import java.util.Objects;

@Service
public class UserCommandService {

    @Value("${rest.api.base.url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    public UserCommandService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public SendMessage returnSendMessage(String[] commandParts, Long chatId) {
        SendMessage message = new SendMessage();

        switch (commandParts[2]) {
            case "get":
                if (commandParts[3].matches("\\d+")) {
                    var response = restTemplate.getForEntity(baseUrl + "/user/" + commandParts[3], Object.class);
                    if (response.getStatusCode() == HttpStatus.OK) {
                        String responseBody = Objects.requireNonNull(response.getBody()).toString();
                        message.setText(responseBody);
                        message.setChatId(chatId);
                    } else {
                        message.setText("An error occurred while trying to get user");
                        message.setChatId(chatId);
                    }
                } else {
                    //TODO log exception not digit
                }
                break;

            case "delete":
                if (commandParts[3].matches("\\d+")) {
                    var response = restTemplate.exchange(baseUrl + "/user/" + commandParts[3] + "/delete", HttpMethod.DELETE, null, HttpStatus.class);
                    if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                        message.setText("User was successfully deleted");
                        message.setChatId(chatId);
                    } else {
                        message.setText("An error occurred while trying to delete user");
                        message.setChatId(chatId);
                    }
                } else {
                    //TODO log exception not digit
                }
                break;

            case "edit":
                if (commandParts[3].matches("\\d+")) {
                    CreateAppUserDto userDto = new CreateAppUserDto();
                    userDto.setChat(chatId.toString());
                    userDto.setLine("UPDATED LINE, CHECK TIME");
                    var response = restTemplate.exchange(baseUrl + "/user/" + commandParts[3] + "/edit", HttpMethod.PUT, new HttpEntity<>(userDto), AppUser.class);
                    if (response.getStatusCode() == HttpStatus.OK) {
                        String responseBody = Objects.requireNonNull(response.getBody()).toString();
                        message.setText(responseBody + "\nUser was successfully updated");
                        message.setChatId(chatId);
                    } else {
                        message.setText("An error occurred while trying to update user");
                        message.setChatId(chatId);
                    }
                } else {
                    //TODO log exception not digit
                }
                message.setText("Введите строку");
                message.setChatId(chatId);
                break;

            case "create":
                CreateAppUserDto userDto = new CreateAppUserDto();
                userDto.setChat(chatId.toString());
                userDto.setLine("UPDATED LINE, CHECK TIME");
                var response = restTemplate.postForEntity(baseUrl + "/user/create", userDto, CreateAppUserDto.class);
                if (response.getStatusCode() == HttpStatus.CREATED) {
                    String responseBody = Objects.requireNonNull(response.getBody()).toString();
                    message.setText(responseBody + "\nNew user created");
                    message.setChatId(chatId);
                } else {
                    message.setText("An error occurred while trying to create a new user");
                    message.setChatId(chatId);
                }
                break;
        }
        return message;
    }
}
