package project.gizka.exception.validation;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdventurerValidationException extends ValidationException{
    private List<String> errorMessages;

    public AdventurerValidationException(List<String> errorMessages){
        super(errorMessages);
    }
}
