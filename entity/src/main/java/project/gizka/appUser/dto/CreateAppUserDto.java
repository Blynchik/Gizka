package project.gizka.appUser.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CreateAppUserDto {

    @NotBlank(message = "Chat should not be empty")
    @NotNull(message = "Chat should not be empty")
    @NotEmpty(message = "Chat should not be empty")
    private String chat;

    private String line;
}
