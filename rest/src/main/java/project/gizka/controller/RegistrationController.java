package project.gizka.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.gizka.dto.commonDto.AppUserCommonDto;
import project.gizka.dto.createDto.CreateAppUserDto;
import project.gizka.exception.validation.AppUserValidationException;
import project.gizka.model.AppUser;
import project.gizka.service.impl.AppUserService;
import project.gizka.util.Converter;
import project.gizka.validator.AppUserValidator;

import java.util.List;

import static project.gizka.util.Converter.getAppUserFrom;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {

    private final AppUserService appUserService;
    private final AppUserValidator appUserValidator;

    @Autowired
    public RegistrationController(AppUserService appUserService,
                                  AppUserValidator appUserValidator) {
        this.appUserService = appUserService;
        this.appUserValidator = appUserValidator;
    }

    @PostMapping()
    public ResponseEntity<AppUserCommonDto> create(@RequestBody @Valid CreateAppUserDto userDto,
                                                   BindingResult bindingResult) {
        checkForErrors(userDto, bindingResult);
        AppUser appUser = getAppUserFrom(userDto);
        AppUser createdUser = appUserService.create(appUser);
        AppUserCommonDto createdUserDto = Converter.getUserDtoFrom(createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
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
