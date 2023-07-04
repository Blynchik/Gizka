package project.gizka.exception.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ExceptionResponse {
    private List<String> messages;
    private LocalDateTime dateTime;

    public ExceptionResponse(List<String> messages, LocalDateTime dateTime){
        this.messages = messages;
        this.dateTime = dateTime;
    }
}
