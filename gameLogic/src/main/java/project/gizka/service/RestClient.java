package project.gizka.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import project.gizka.dto.commonDto.AdventurerCommonDto;

@Component
public class RestClient {

    private final RestTemplate restTemplate;
    @Value("${rest.api.base.url}")
    public String baseUrl;

    @Autowired
    public RestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Object getAdventurerById(String adventurerId) throws Exception {
        ResponseEntity<?> response = null;
        try {
            response = restTemplate.getForEntity(baseUrl + "/adventurer/" + adventurerId, AdventurerCommonDto.class);
        } catch (HttpClientErrorException.NotFound e) {
            return e.getMessage();
        } catch (HttpClientErrorException.BadRequest e) {
            return e.getMessage();
        }

        AdventurerCommonDto adventurerDto = null;
        if (response.getStatusCode() == HttpStatus.OK) {
            adventurerDto = (AdventurerCommonDto) response.getBody();
            if (adventurerDto != null) {

                int strength = adventurerDto.getStrength();
                int lowAttack = (int) (strength - Math.ceil(strength / 2.0));
                adventurerDto.setLowAttack(lowAttack);
                int highAttack = (int) (strength + Math.floor(strength / 2.0));
                adventurerDto.setHighAttack(highAttack);

                int dexterity = adventurerDto.getDexterity();
                int lowEvasion = (int) (dexterity - Math.ceil(dexterity / 2.0));
                adventurerDto.setLowEvasion(lowEvasion);
                int highEvasion = (int) (dexterity + Math.floor(dexterity / 2.0));
                adventurerDto.setHighEvasion(highEvasion);

                int constitution = adventurerDto.getConstitution();
                int healthPoint =constitution * 2;
                adventurerDto.setHealthPoint(healthPoint);
            }
        } else {
            throw new Exception("An error occurred while trying to get hero");
            //TODO
        }

        return adventurerDto;
    }
}

