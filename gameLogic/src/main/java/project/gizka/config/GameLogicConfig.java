package project.gizka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GameLogicConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
