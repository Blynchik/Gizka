package project.gizka.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project.gizka.command.impl.*;
import project.gizka.service.RestClient;

import java.util.List;
import java.util.Map;

@Component
public class CommandMap {

    private final RestClient restClient;

    @Autowired
    public CommandMap(RestClient restClient){
        this.restClient = restClient;
    }

    public Map<String, AbstractCommand> getCommands() {
        return Map.of(
                "/help", new HelpCommand(List.of(
                        "/help - все команды",
                        "/get - получить пользователя по id",
                        "/create - создать пользователя",
                        "/edit - изменить пользователя")
                ),
                "/get", new GetCommand("/get", restClient),
                "/create", new CreateCommand("/create", restClient),
                "/edit", new EditCommand("/edit", restClient)
        );
    }
}
