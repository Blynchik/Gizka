package project.gizka.exception.validation;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ValidationException extends RuntimeException {
    public ValidationException(List<String> errorMessages){
        super(errorMessages.toString());
    }
}
