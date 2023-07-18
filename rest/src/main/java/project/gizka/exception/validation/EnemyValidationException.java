package project.gizka.exception.validation;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EnemyValidationException extends ValidationException{
    private List<String> errorMessages;

    public EnemyValidationException(List<String> errorMessages){
        super(errorMessages);
    }
}
