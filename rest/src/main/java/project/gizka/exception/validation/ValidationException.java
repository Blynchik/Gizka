package project.gizka.exception.validation;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ValidationException extends RuntimeException {
    private List<String> errorMessages;
    public ValidationException(List<String> errorMessages){
        this.errorMessages = errorMessages;
    }
}
