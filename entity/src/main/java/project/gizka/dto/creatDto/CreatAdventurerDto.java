package project.gizka.dto.creatDto;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CreatAdventurerDto {
    @NotBlank(message = "Name should not be empty")
    @NotNull(message = "Name should not be empty")
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 1, max = 20, message = "Name should be less 20 than symbols")
    private String firstName;

    @NotBlank(message = "Surname should not be empty")
    @NotNull(message = "Surname should not be empty")
    @NotEmpty(message = "Surname should not be empty")
    @Size(min = 1, max = 50, message = "Surname should be less 50 than symbols")
    private String lastName;
}
