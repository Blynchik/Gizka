package project.gizka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.gizka.client.RestClient;
import project.gizka.service.LogicService;

@RestController
@RequestMapping("/api/game")
public class LogicController {

    private final RestClient restClient;
    private final LogicService logicService;

    @Autowired
    public LogicController(RestClient restClient,
                           LogicService logicService){
        this.restClient = restClient;
        this.logicService = logicService;
    }

    @GetMapping("/adventurer/{id}")
    public ResponseEntity<?> getGameAdventurer(@PathVariable String id) throws Exception {
        return ResponseEntity.ok(restClient.getAdventurerById(id));
    }

    @GetMapping("/randomEnemy")
    public ResponseEntity<?> getRandomEnemy() throws Exception{
        return ResponseEntity.ok(restClient.getRandomEnemy());
    }

    @GetMapping("/fight/{adventurerId}")
    public ResponseEntity<?> getFight(@PathVariable Long adventurerId) throws Exception {
        return ResponseEntity.ok(logicService.fight(adventurerId));
    }
}
