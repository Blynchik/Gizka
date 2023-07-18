package project.gizka.config;


import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

        @Bean
        public GroupedOpenApi api() {
            return GroupedOpenApi.builder()
                    .group("Game API")
                    .pathsToMatch("/api/**")
                    .build();
        }
}
