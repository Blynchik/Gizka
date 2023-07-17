package project.gizka.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import project.gizka.dto.creatDto.CreatAppUserDto;
import project.gizka.service.impl.AppUserService;

@Component
public class AppUserValidator implements Validator {
    private final AppUserService appUserService;

    @Autowired
    public AppUserValidator(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CreatAppUserDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreatAppUserDto userDto = (CreatAppUserDto) target;

        if (appUserService.getByChat(userDto.getChat()).isPresent()) {
            errors.rejectValue("chat", "", "Chat already exists");
        }
    }
}
