package project.gizka.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import project.gizka.dto.commonDto.AppUserCommonDto;
import project.gizka.dto.createDto.CreateAdventurerDto;
import project.gizka.dto.createDto.CreateAppUserDto;
import project.gizka.exception.RestException;
import project.gizka.util.FightLog;

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

    public AppUserCommonDto createAppUser(String userName, String chatId){
        CreateAppUserDto userDto = new CreateAppUserDto(userName, chatId);
        try{
            var response = restTemplate.postForEntity(baseUrl + "/registration", userDto, AppUserCommonDto.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                return response.getBody();
            }

        }catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RestException(ex.getResponseBodyAsString());
        } catch (Exception ex) {
            throw new RestException(ex.getMessage());
        }

        return null;
    }


//    public AppUserCommonDto getUserById(String userId) {
//        try {
//            ResponseEntity<AppUserCommonDto> response = restTemplate.getForEntity(baseUrl + "/user/" + userId, AppUserCommonDto.class);
//            if (response.getStatusCode() == HttpStatus.OK) {
//                return response.getBody();
//            }
//        } catch (HttpClientErrorException | HttpServerErrorException ex) {
//            throw new RestException(ex.getResponseBodyAsString());
//        } catch (Exception ex) {
//            throw new RestException(ex.getMessage());
//        }
//        return null;
//    }
//
//    public String createUser(String chatId, String slogan) { //сделать, чтобы возвращал ResponseEntity, в переработка в String происходила в другом месте
//
//        CreateAppUserDto userDto = new CreateAppUserDto();
//        userDto.setChat(chatId);
//        userDto.setLine(slogan);
//
//        var response = restTemplate.postForEntity(baseUrl + "/user/create", userDto, Object.class);
//        String responseText;
//
//        if (response.getStatusCode() == HttpStatus.CREATED) {
//            responseText = "New user successfully created:\n" + Objects.requireNonNull(response.getBody());
//        } else {
//            responseText = "An error occurred while trying to create a new user";
//        }
//
//        return responseText;
//    }
//
//    public String updateUser(String chatId, String userId, String slogan) {
//
//        CreateAppUserDto userDto = new CreateAppUserDto();
//        userDto.setChat(chatId);
//        userDto.setLine(slogan);
//
//        var response = restTemplate.exchange(baseUrl + "/user/" + userId + "/edit", HttpMethod.PUT, new HttpEntity<>(userDto), Object.class);
//        String responseText;
//
//        if (response.getStatusCode() == HttpStatus.OK) {
//            responseText = "User was successfully updated\n" + Objects.requireNonNull(response.getBody());
//        } else {
//            responseText = "An error occurred while trying to update user";
//        }
//        return responseText;
//    }
//
//    public String createAdventurer(String firstName, String lastName) {
//        CreateAdventurerDto adventurerDto = new CreateAdventurerDto();
//
//        adventurerDto.setName(firstName);
//        adventurerDto.setLastName(lastName);
//        adventurerDto.setStrength(1);
//        adventurerDto.setDexterity(1);
//        adventurerDto.setConstitution(1);
//
//        var response = restTemplate.postForEntity(baseUrl + "/adventurer/create", adventurerDto, Object.class);
//        String responseText;
//
//        if (response.getStatusCode() == HttpStatus.CREATED) {
//            responseText = "New adventurer successfully created:\n" + Objects.requireNonNull(response.getBody());
//        } else {
//            responseText = "An error occurred while trying to create a new adventurer";
//        }
//
//        return responseText;
//    }
//
//    public String getAdventurerById(String adventurerId) {
//        var response = restTemplate.getForEntity(gameLogicUrl + "/adventurer/" + adventurerId, Object.class);
//        String responseText;
//
//        if (response.getStatusCode() == HttpStatus.OK) {
//            responseText = "Found adventurer:\n" + Objects.requireNonNull(response.getBody());
//        } else {
//            responseText = "An error occurred while trying to get adventurer";
//        }
//
//        return responseText;
//    }
//
//    public FightLog getFightLog(String adventurerId) {
//        var response = restTemplate.getForEntity(gameLogicUrl + "/fight/" + adventurerId, FightLog.class);
//        String responseText;
//
//        if (response.getStatusCode() == HttpStatus.OK) {
//            responseText = "Fight log:\n" + Objects.requireNonNull(response.getBody());
//        } else {
//            responseText = "An error occurred while trying to get fight log";
//        }
//
//        return response.getBody();
//    }
}
