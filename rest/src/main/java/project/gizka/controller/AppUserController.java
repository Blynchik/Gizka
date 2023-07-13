package project.gizka.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.gizka.appUser.dto.CreateAppUserDto;
import project.gizka.appUser.model.AppUser;
import project.gizka.exception.notFound.AppUserNotFoundException;
import project.gizka.exception.validation.AppUserValidationException;
import project.gizka.service.impl.AppUserService;
import project.gizka.validator.AppUserValidator;

import java.util.List;

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
    @Operation(summary = """
            Метод возвращает список пользователей со всей информацией
            о них (id, chat, registeredAt, updatedAt) с кодом 200.
            Если пользователей нет, то возвращается пустой список
            с кодом 200.
            """)
    public ResponseEntity<List<AppUser>> getAll() {
        List<AppUser> users = appUserService.getAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = """
            Метод возвращает информацию о пользователе (id, chat, registeredAt, updatedAt)
            по его id с кодом 200.
            Если такого id не существует, то возвращается сообщение об ошибке и
            времени ошибки с кодом 404.
            """)
    public ResponseEntity<AppUser> getById(@PathVariable Long id) {
        checkUserExistence(id);
        var optionalUser = appUserService.getById(id);
        return ResponseEntity.ok(optionalUser.get());
    }

    @PostMapping("/create")
    @Operation(summary = """
            Метод создает, сохраняет и возвращает нового пользователя
            (id, chat, registeredAt, updatedAt)в БД с кодом 201.
            Для создания пользователя требуется ввести чат.
            Чат не может бысть пустым и доллжен быть уникальным.
            Если чат пустой или не уникальный, возвращается
            сообщение об ошибке с временем ошибки с кодом 400.
            """)
    public ResponseEntity<AppUser> create(@RequestBody @Valid CreateAppUserDto userDto,
                                          BindingResult bindingResult) {
        checkForErrors(userDto, bindingResult);
        AppUser appUser = getAppUserFrom(userDto);
        AppUser createdUser = appUserService.create(appUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}/edit")
    @Operation(summary = """
            Метод редактирует существующего пользователя по его id
            и возвращает отредактированного пользователя
            (id, chat, registeredAt, updatedAt) с кодом 200.
            Переданный новый чат не может бысть пустым и доллжен быть уникальным.
            Если чат пустой или не уникальный, возвращается
            сообщение об ошибке с временем ошибки с кодом 400.
            Если такого id не существует, то возвращается сообщение об ошибке и
            времени ошибки с кодом 404.
            """)
    public ResponseEntity<AppUser> edit(@PathVariable Long id,
                                        @RequestBody @Valid CreateAppUserDto userDto,
                                        BindingResult bindingResult) {
        checkUserExistence(id);

        if(bindingResult.hasErrors()){
            List<String> errorMessages = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            throw new AppUserValidationException(errorMessages);
        }

        AppUser appUser = getAppUserFrom(userDto);
        AppUser updatedUser = appUserService.update(id, appUser);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}/delete")
    @Operation(summary = """
            Метод удаляет пользователя по его id
            и возвращает код 204.
            Если такого id не существует, то возвращается сообщение об ошибке и
            времени ошибки с кодом 404.
            """)
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        checkUserExistence(id);
        appUserService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private void checkUserExistence(Long id) {
        if (!appUserService.checkExistence(id)) {
            throw new AppUserNotFoundException("ID " + id + " not found");
        }
    }

    private void checkForErrors(CreateAppUserDto userDto,
                                BindingResult bindingResult) {
        appUserValidator.validate(userDto, bindingResult);
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            throw new AppUserValidationException(errorMessages);
        }
    }
}
