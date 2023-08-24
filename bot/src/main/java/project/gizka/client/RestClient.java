package project.gizka.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import project.gizka.dto.commonDto.AdventurerCommonDto;
import project.gizka.dto.commonDto.AppUserCommonDto;
import project.gizka.dto.createDto.CreateAdventurerDto;
import project.gizka.dto.createDto.CreateAppUserDto;
import project.gizka.exception.RestException;
import project.gizka.util.FightLog;

import java.util.ArrayList;
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

    public String createAppUser(String userName, String chatId) {
        CreateAppUserDto userDto = new CreateAppUserDto(userName, chatId);
        String url = baseUrl + "/registration";
        try {
            ResponseEntity<AppUserCommonDto> response = restTemplate.postForEntity(url, userDto, AppUserCommonDto.class);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            if (ex.getMessage().contains("[Chat already exists]")) {
                return "\nУ вас уже есть аккаунт ✅";
            } else {
                return ex.getMessage() + ". Press /start";
            }
        }
        return "\nВаш аккаунт создан ❗";
    }

    public String createAdventurer(String firstName, String lastName, String chatId) {
        CreateAdventurerDto adventurerDto = new CreateAdventurerDto();
        adventurerDto.setName(firstName);
        adventurerDto.setLastName(lastName);
        adventurerDto.setStrength(1);
        adventurerDto.setConstitution(1);
        adventurerDto.setDexterity(1);

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(chatId, chatId);

        HttpEntity<CreateAdventurerDto> request = new HttpEntity<>(adventurerDto, headers);

        try {
            var response = restTemplate.exchange(baseUrl + "/adventurer/create", HttpMethod.POST, request, AdventurerCommonDto.class);

            String responseText;
            if (response.getStatusCode() == HttpStatus.CREATED) {
                AdventurerCommonDto adventurerDtoResponse = (AdventurerCommonDto) response.getBody();
                String description = "Имя: \uD83D\uDCDC" + adventurerDtoResponse.getName() + " " + adventurerDtoResponse.getLastName() + "\n" +
                        "Сила: \uD83C\uDFCB\uFE0F\u200D♂\uFE0F" + adventurerDtoResponse.getStrength() + "\n" +
                        "Лоскость: \uD83E\uDD38\u200D♀\uFE0F" + adventurerDtoResponse.getDexterity() + "\n" +
                        "Выносливость: \uD83C\uDFC3" + adventurerDtoResponse.getConstitution();
                responseText = "Новый герой создан\uD83D\uDCCC:\n\n" + description +
                        "\n\nВведите /fight для начала сражения \uD83E\uDD3A";
            } else {
                responseText = "An error occurred while trying to create a new adventurer";
            }

            return responseText;
        } catch (Exception e) {
            return e.getMessage() + ". Press /start" ;
        }
    }

    public FightLog getFightLog(String chatId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(chatId, chatId);

        HttpEntity request = new HttpEntity<>(headers);
        String responseText;
        ResponseEntity<FightLog> response = null;
        try {
            var responseId = restTemplate.exchange(baseUrl + "/user/" + chatId + "/getAdventurers", HttpMethod.GET, request, ArrayList.class);
            ArrayList<Integer> ids = (ArrayList<Integer>) responseId.getBody();

            Integer id = ids.get(ids.size() - 1);

            response = restTemplate.exchange(gameLogicUrl + "/fight/" + id, HttpMethod.GET, request, FightLog.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                responseText = "Fight log:\n" + Objects.requireNonNull(response.getBody());
            } else {
                responseText = "An error occurred while trying to get fight log";
            }
        } catch (Exception e) {
            responseText = e.getMessage();
            FightLog fightLog =  new FightLog();
            fightLog.setWinner("An error occurred while trying to get fight log, try to authenticate. Press /start");
            return fightLog;
        }

        return response.getBody();
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
}
