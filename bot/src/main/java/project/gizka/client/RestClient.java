package project.gizka.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import project.gizka.dto.creatDto.CreatAdventurerDto;
import project.gizka.dto.creatDto.CreatAppUserDto;

import java.util.Objects;

@Component
public class RestClient {

    private final RestTemplate restTemplate;
    @Value("${rest.api.base.url}")
    public String baseUrl;

    @Value("${game.api.logic.url}")
    public String gameLogicUrl;

    @Autowired
    public RestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getUserById(String userId) {
        var response = restTemplate.getForEntity(baseUrl + "/user/" + userId, Object.class);
        String responseText;

        if (response.getStatusCode() == HttpStatus.OK) {
            responseText = "Found user:\n" + Objects.requireNonNull(response.getBody());
        } else {
            responseText = "An error occurred while trying to get user";
        }

        return responseText;
    }

    public String createUser(String chatId, String slogan) { //сделать, чтобы возвращал ResponseEntity, в переработка в String происходила в другом месте

        CreatAppUserDto userDto = new CreatAppUserDto();
        userDto.setChat(chatId);
        userDto.setLine(slogan);

        var response = restTemplate.postForEntity(baseUrl + "/user/create", userDto, Object.class);
        String responseText;

        if (response.getStatusCode() == HttpStatus.CREATED) {
            responseText = "New user successfully created:\n" + Objects.requireNonNull(response.getBody());
        } else {
            responseText = "An error occurred while trying to create a new user";
        }

        return responseText;
    }

    public String updateUser(String chatId, String userId, String slogan) {

        CreatAppUserDto userDto = new CreatAppUserDto();
        userDto.setChat(chatId);
        userDto.setLine(slogan);

        var response = restTemplate.exchange(baseUrl + "/user/" + userId + "/edit", HttpMethod.PUT, new HttpEntity<>(userDto), Object.class);
        String responseText;

        if (response.getStatusCode() == HttpStatus.OK) {
            responseText = "User was successfully updated\n" + Objects.requireNonNull(response.getBody());
        } else {
            responseText = "An error occurred while trying to update user";
        }
        return responseText;
    }

    public String createAdventurer(String firstName, String lastName){
        CreatAdventurerDto adventurerDto = new CreatAdventurerDto();

        adventurerDto.setName(firstName);
        adventurerDto.setLastName(lastName);
        adventurerDto.setStrength(1);
        adventurerDto.setDexterity(1);
        adventurerDto.setConstitution(1);

        var response = restTemplate.postForEntity(baseUrl + "/adventurer/create", adventurerDto, Object.class);
        String responseText;

        if (response.getStatusCode() == HttpStatus.CREATED) {
            responseText = "New adventurer successfully created:\n" + Objects.requireNonNull(response.getBody());
        } else {
            responseText = "An error occurred while trying to create a new adventurer";
        }

        return responseText;
    }

    public String getAdventurerById(String adventurerId) {
        var response = restTemplate.getForEntity(gameLogicUrl + "/adventurer/" + adventurerId, Object.class);
        String responseText;

        if (response.getStatusCode() == HttpStatus.OK) {
            responseText = "Found adventurer:\n" + Objects.requireNonNull(response.getBody());
        } else {
            responseText = "An error occurred while trying to get adventurer";
        }

        return responseText;
    }
}
