package project.gizka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.gizka.model.AppUser;
import project.gizka.service.impl.AppUserService;

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
    public ResponseEntity<AppUser> create(AppUser appUser){
        AppUser createdUser = appUserService.create(appUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<HttpStatus> edit(@PathVariable Long id,
                                           AppUser updatedUser){
        appUserService.update(id, updatedUser);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id){
        appUserService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
