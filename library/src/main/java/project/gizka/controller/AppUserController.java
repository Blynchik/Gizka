package project.gizka.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.gizka.dto.CreateAppUserDto;
import project.gizka.exception.notFound.AppUserNotFoundException;
import project.gizka.exception.validation.AppUserValidationException;
import project.gizka.model.AppUser;
import project.gizka.service.impl.AppUserService;
import project.gizka.validator.AppUserValidator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static project.gizka.util.Converter.getAppUserFrom;

@RestController
@RequestMapping("/api/user")
public class AppUserController {

    private final AppUserService appUserService;
    private final AppUserValidator appUserValidator;

    @Autowired
    public AppUserController(AppUserService appUserService, AppUserValidator appUserValidator) {
        this.appUserService = appUserService;
        this.appUserValidator = appUserValidator;
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
    public ResponseEntity<AppUser> create(@RequestBody @Valid CreateAppUserDto userDto,
                                          BindingResult bindingResult) {
        checkForErrors(userDto,bindingResult);
        AppUser appUser = getAppUserFrom(userDto);
        AppUser createdUser = appUserService.create(appUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<AppUser> edit(@PathVariable Long id,
                                        @RequestBody @Valid CreateAppUserDto userDto,
                                        BindingResult bindingResult) {
        checkUserExistence(id);
        checkForErrors(userDto,bindingResult);
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
        if (!appUserService.checkExistence(id)) {
            throw new AppUserNotFoundException("ID " + id + " not found");
        }
    }

    private void checkForErrors(CreateAppUserDto userDto,
                                BindingResult bindingResult){
        appUserValidator.validate(userDto,bindingResult);
        if(bindingResult.hasErrors()){
            List<String> errorMessages = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            throw new AppUserValidationException(errorMessages);
        }
    }
}
