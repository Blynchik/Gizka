package project.gizka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.gizka.service.RestClient;

@RestController
@RequestMapping("/api/game/adventurer")
public class AdventurerController {

    private final RestClient restClient;

    @Autowired
    public AdventurerController(RestClient restClient){
        this.restClient = restClient;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGameAdventurer(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(restClient.getAdventurerById(id));
    }
}
