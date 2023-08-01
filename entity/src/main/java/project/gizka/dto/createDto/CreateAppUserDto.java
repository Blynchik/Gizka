package project.gizka.dto.createDto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
public class CreateAppUserDto {

    @NotBlank(message = "Name id should not be empty")
    @NotNull(message = "Name id should not be empty")
    @NotEmpty(message = "Name id should not be empty")
    private String name;

    @NotBlank(message = "Chat should not be empty")
    @NotNull(message = "Chat should not be empty")
    @NotEmpty(message = "Chat should not be empty")
    private String chat;
}
