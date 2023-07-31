package project.gizka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project.gizka.client.RestClient;
import project.gizka.controller.AbstractCommand;
import project.gizka.controller.command.StartCommand;

import java.util.Map;

@Component
public class CommandMap {

    private final RestClient restClient;

    @Autowired
    public CommandMap(RestClient restClient){
        this.restClient = restClient;
    }

    public Map<String, AbstractCommand> getCommands() {
//        return Map.of(
//                "/help", new HelpCommand(List.of(
//                        "/help - все команды",
//                        "/get - получить пользователя по id",
//                        "/create - создать пользователя",
//                        "/edit - изменить пользователя",
//                        "/hero - создать героя",
//                        "/show - получить героя по id",
//                        "/fight - начать битву")
//                ),
//                "/get", new GetUserCommand("/get", restClient),
//                "/create", new CreateUserCommand("/create", restClient),
//                "/edit", new EditUserCommand("/edit", restClient),
//                "/hero", new CreateAdventurerCommand("/hero", restClient),
//                "/show", new GetAdventurerCommand("/show", restClient),
//                "/fight", new FightAdventurerCommand("/fight", restClient)
//        );
//    }

        return Map.of(
                "/start", new StartCommand(restClient, 0));
    }
}
