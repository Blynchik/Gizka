package project.gizka.exception.validation;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BinaryContentValidationException extends ValidationException{
    private List<String> errorMessages;

    public BinaryContentValidationException(List<String> errorMessages){
        super(errorMessages);
    }
}
