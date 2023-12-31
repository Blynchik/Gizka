package project.gizka.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.gizka.dto.commonDto.AdventurerCommonDto;
import project.gizka.dto.createDto.CreateAdventurerDto;
import project.gizka.model.Adventurer;
import project.gizka.exception.notFound.AdventurerNotFoundException;
import project.gizka.exception.validation.AdventurerValidationException;
import project.gizka.model.AppUser;
import project.gizka.model.AuthUser;
import project.gizka.service.impl.AdventurerService;
import project.gizka.service.impl.AppUserService;
import project.gizka.util.Converter;

import java.util.List;

@RestController
@RequestMapping("/api/adventurer")
public class AdventurerController {

    private final AdventurerService adventurerService;
    private final AppUserService appUserService;

    @Autowired
    public AdventurerController(AdventurerService adventurerService,
                                AppUserService appUserService) {
        this.adventurerService = adventurerService;
        this.appUserService = appUserService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AdventurerCommonDto>> getAll() {
        List<Adventurer> adventurers = adventurerService.getAll();
        List<AdventurerCommonDto> adventurersDto = adventurers.stream()
                .map(Converter::getAdventurerDtoFrom)
                .toList();
        return ResponseEntity.ok(adventurersDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdventurerCommonDto> getById(@PathVariable Long id) {
        checkAdventurerExistence(id);
        var optionalAdventurer = adventurerService.getById(id);
        AdventurerCommonDto adventurerDto = Converter.getAdventurerDtoFrom(optionalAdventurer.get());
        return ResponseEntity.ok(adventurerDto);
    }

    @PostMapping("/create")
    public ResponseEntity<AdventurerCommonDto> create(@RequestBody @Valid CreateAdventurerDto adventurerDto,
                                                      BindingResult bindingResult,
                                                      @AuthenticationPrincipal AuthUser authUser) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            throw new AdventurerValidationException(errorMessages);
        }

        Adventurer adventurer = Converter.getAdventurerFrom(adventurerDto);
        adventurer.setAppUser(appUserService.getById(authUser.id()).get());
        Adventurer createdAdventurer = adventurerService.create(adventurer);
        AdventurerCommonDto createdAdventurerDto = Converter.getAdventurerDtoFrom(createdAdventurer);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAdventurerDto);
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<AdventurerCommonDto> edit(@PathVariable Long id,
                                                    @RequestBody @Valid CreateAdventurerDto adventurerDto,
                                                    BindingResult bindingResult) {
        checkAdventurerExistence(id);

        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            throw new AdventurerValidationException(errorMessages);
        }

        Adventurer adventurer = Converter.getAdventurerFrom(adventurerDto);
        Adventurer updatedAdventurer = adventurerService.update(id, adventurer);
        AdventurerCommonDto updatedAdventurerDto = Converter.getAdventurerDtoFrom(updatedAdventurer);
        return ResponseEntity.ok(updatedAdventurerDto);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        checkAdventurerExistence(id);
        adventurerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private void checkAdventurerExistence(Long id) {
        if (!adventurerService.checkExistence(id)) {
            throw new AdventurerNotFoundException("Adventurer " + id + " not found");
        }
    }
}
