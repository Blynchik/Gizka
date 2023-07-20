package project.gizka.client;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import project.gizka.dto.commonDto.AdventurerCommonDto;
import project.gizka.dto.commonDto.EnemyCommonDto;

@Component
public class RestClient {

    private final RestTemplate restTemplate;
    @Value("${rest.api.base.url}")
    public String baseUrl;

    @Autowired
    public RestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Object getAdventurerById(String adventurerId) throws Exception { //сделать, чтобы возвращалось ResponseEntity, а переработка происходила в другом месте
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
                int healthPoint =constitution;
                adventurerDto.setHealthPoint(healthPoint);
            }
        } else {
            throw new Exception("An error occurred while trying to get hero");
            //TODO
        }

        return adventurerDto;
    }

    public Object getRandomEnemy() throws Exception {
        ResponseEntity<?> response = null;
        try {
            response = restTemplate.getForEntity(baseUrl + "/enemy/random", EnemyCommonDto.class);
        } catch (HttpClientErrorException.NotFound e) {
            return e.getMessage();
        } catch (HttpClientErrorException.BadRequest e) {
            return e.getMessage();
        }

        EnemyCommonDto enemyDto = null;

        if (response.getStatusCode() == HttpStatus.OK) {
            enemyDto = (EnemyCommonDto) response.getBody();
            if (enemyDto != null) {
                int strength = enemyDto.getStrength();
                int lowAttack = (int) (strength - Math.ceil(strength / 2.0));
                enemyDto.setLowAttack(lowAttack);
                int highAttack = (int) (strength + Math.floor(strength / 2.0));
                enemyDto.setHighAttack(highAttack);

                int dexterity = enemyDto.getDexterity();
                int lowEvasion = (int) (dexterity - Math.ceil(dexterity / 2.0));
                enemyDto.setLowEvasion(lowEvasion);
                int highEvasion = (int) (dexterity + Math.floor(dexterity / 2.0));
                enemyDto.setHighEvasion(highEvasion);

                int constitution = enemyDto.getConstitution();
                int healthPoint = constitution;
                enemyDto.setHealthPoint(healthPoint);
            }
        } else {
            throw new Exception("An error occurred while trying to get hero");
            //TODO
        }

        return enemyDto;
    }
}

