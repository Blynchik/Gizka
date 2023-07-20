package project.gizka.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project.gizka.command.impl.adventurer.CreateAdventurerCommand;
import project.gizka.command.impl.adventurer.FightAdventurerCommand;
import project.gizka.command.impl.adventurer.GetAdventurerCommand;
import project.gizka.command.impl.user.CreateUserCommand;
import project.gizka.command.impl.user.EditUserCommand;
import project.gizka.command.impl.user.GetUserCommand;
import project.gizka.command.impl.HelpCommand;
import project.gizka.client.RestClient;

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
                        "/edit - изменить пользователя",
                        "/hero - создать героя",
                        "/show - получить героя по id",
                        "/fight - начать битву")
                ),
                "/get", new GetUserCommand("/get", restClient),
                "/create", new CreateUserCommand("/create", restClient),
                "/edit", new EditUserCommand("/edit", restClient),
                "/hero", new CreateAdventurerCommand("/hero", restClient),
                "/show", new GetAdventurerCommand("/show", restClient),
                "/fight", new FightAdventurerCommand("/fight", restClient)
        );
    }
}
