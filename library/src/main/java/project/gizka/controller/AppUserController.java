package project.gizka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.gizka.dto.CreateAppUserDto;
import project.gizka.model.AppUser;
import project.gizka.service.impl.AppUserService;
import project.gizka.util.Converter;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class AppUserController {

    private final AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService){
        this.appUserService = appUserService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AppUser>> getAll(){
        List<AppUser> users = appUserService.getAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getById(@PathVariable Long id){
        AppUser appUser = appUserService.getById(id).get();
        return ResponseEntity.ok(appUser);
    }

    @PostMapping("/create")
    public ResponseEntity<AppUser> create(CreateAppUserDto userDto){
        AppUser appUser = Converter.getAppUser(userDto);
        AppUser createdUser = appUserService.create(appUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<AppUser> edit(@PathVariable Long id,
                                           CreateAppUserDto userDto){
        AppUser appUser = Converter.getAppUser(userDto);
        AppUser updatedUser = appUserService.update(id, appUser);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    @DeleteMapping("/{id}/delete/")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id){
        appUserService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
