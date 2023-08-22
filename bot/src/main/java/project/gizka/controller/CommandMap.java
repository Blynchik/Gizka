package project.gizka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project.gizka.client.RestClient;
import project.gizka.controller.AbstractCommand;
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
                "/start", new StartCommand(restClient, 0),
                "/help", new HelpCommand(restClient, 0, List.of(
                        "/start - регистрация и начало игры",
                        "/help - все команды"
                )));
    }
}
