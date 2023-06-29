package project.gizka.exception.validation;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Stream;

@Getter
@Setter
public class AppUserValidationException extends RuntimeException{
    private List<String> errorMessages;
    public AppUserValidationException(List<String> errorMessages){
        this.errorMessages = errorMessages;
    }
}
