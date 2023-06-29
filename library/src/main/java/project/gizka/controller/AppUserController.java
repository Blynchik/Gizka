package project.gizka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.gizka.dto.CreateAppUserDto;
import project.gizka.exception.notFound.AppUserNotFoundException;
import project.gizka.model.AppUser;
import project.gizka.service.impl.AppUserService;

import java.util.List;

import static project.gizka.util.Converter.*;

@RestController
@RequestMapping("/api/user")
public class AppUserController {

    private final AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AppUser>> getAll() {
        List<AppUser> users = appUserService.getAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getById(@PathVariable Long id) {
        checkUserExistence(id);
        var optionalUser = appUserService.getById(id);
        return ResponseEntity.ok(optionalUser.get());
    }

    @PostMapping("/create")
    public ResponseEntity<AppUser> create(@RequestBody CreateAppUserDto userDto) {
        AppUser appUser = getAppUserFrom(userDto);
        AppUser createdUser = appUserService.create(appUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<AppUser> edit(@PathVariable Long id,
                                        @RequestBody CreateAppUserDto userDto) {
        checkUserExistence(id);
        AppUser appUser = getAppUserFrom(userDto);
        AppUser updatedUser = appUserService.update(id, appUser);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        checkUserExistence(id);
        appUserService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private void checkUserExistence(Long id){
        if (appUserService.checkExistence(id)) {
            throw new AppUserNotFoundException("ID " + id + "not found");
        }
    }
}
