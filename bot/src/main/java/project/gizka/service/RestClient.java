package project.gizka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import project.gizka.appUser.dto.CreateAppUserDto;
import project.gizka.appUser.model.AppUser;

import java.util.Objects;

@Component
public class RestClient {

    private final RestTemplate restTemplate;
    @Value("${rest.api.base.url}")
    public String baseUrl;

    @Autowired
    public RestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getUserById(String userId) {
        ResponseEntity<?> response = restTemplate.getForEntity(baseUrl + "/user/" + userId, Object.class);
        String responseText;

        if (response.getStatusCode() == HttpStatus.OK) {
            responseText = "Found user:\n" + Objects.requireNonNull(response.getBody());
        } else {
            responseText = "An error occurred while trying to get user";
        }

        return responseText;
    }

    public String createUser(String chatId, String slogan) {

        CreateAppUserDto userDto = new CreateAppUserDto();
        userDto.setChat(chatId);
        userDto.setLine(slogan);

        var response = restTemplate.postForEntity(baseUrl + "/user/create", userDto, AppUser.class);
        String responseText;

        if (response.getStatusCode() == HttpStatus.CREATED) {
            responseText = "New user successfully created:\n" + Objects.requireNonNull(response.getBody());
        } else {
            responseText = "An error occurred while trying to create a new user";
        }

        return responseText;
    }

    public String updateUser(String chatId, String userId, String slogan) {

        CreateAppUserDto userDto = new CreateAppUserDto();
        userDto.setChat(chatId);
        userDto.setLine(slogan);

        var response = restTemplate.exchange(baseUrl + "/user/" + userId + "/edit", HttpMethod.PUT, new HttpEntity<>(userDto), AppUser.class);
        String responseText;

        if (response.getStatusCode() == HttpStatus.OK) {
            responseText = "User was successfully updated\n" + Objects.requireNonNull(response.getBody());
        } else {
            responseText = "An error occurred while trying to update user";
        }
        return responseText;
    }
}
