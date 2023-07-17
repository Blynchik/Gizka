package project.gizka.dto.creatDto;

import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CreatAppUserDto {

    @NotBlank(message = "Chat should not be empty")
    @NotNull(message = "Chat should not be empty")
    @NotEmpty(message = "Chat should not be empty")
    private String chat;

    @Size(min = 0, max = 100, message = "Slogan should be less than 100 symbols")
    private String line;
}
