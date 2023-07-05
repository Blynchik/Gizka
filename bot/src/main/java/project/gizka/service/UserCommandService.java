package project.gizka.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Objects;

@Service
public class UserCommandService {

    @Value("${rest.api.base.url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    public UserCommandService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public SendMessage returnSendMessage(String[] commandParts) {
        SendMessage message = new SendMessage();

        if (commandParts[2].matches("\\d+")) {
            var response = restTemplate.getForEntity(baseUrl + "/user/" + commandParts[2], Object.class);
            String responseBody = Objects.requireNonNull(response.getBody()).toString();
            message.setText(responseBody);
        } else {
            //TODO log exception not digit
        }
        return message;
    }
}
