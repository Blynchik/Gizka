package project.gizka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project.gizka.client.RestClient;
import project.gizka.controller.AbstractCommand;
import project.gizka.controller.command.CreateHeroCommand;
import project.gizka.controller.command.FightCommand;
import project.gizka.controller.command.HelpCommand;
import project.gizka.controller.command.StartCommand;

import java.util.List;
import java.util.Map;

@Component
public class CommandMap {

    private final RestClient restClient;

    @Autowired
    public CommandMap(RestClient restClient) {
        this.restClient = restClient;
    }

    public Map<String, AbstractCommand> getCommands() {
        return Map.of(
                "/start", new StartCommand(restClient),
                "/create", new CreateHeroCommand(restClient),
                "/fight", new FightCommand(restClient),
                "/help", new HelpCommand(List.of(
                        "/start - регистрация и начало игры",
                        "/help - все команды",
                        "/create - создать героя",
                        "/fight - начать сражение"
                )));
    }
}
