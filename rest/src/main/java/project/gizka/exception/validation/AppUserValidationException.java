package project.gizka.exception.validation;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AppUserValidationException extends ValidationException{
    private List<String> errorMessages;
    public AppUserValidationException(List<String> errorMessages){
        super(errorMessages);
    }
}
