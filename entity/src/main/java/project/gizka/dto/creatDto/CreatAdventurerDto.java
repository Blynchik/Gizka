package project.gizka.dto.creatDto;

import jakarta.validation.constraints.*;
import lombok.*;

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
    private String name;

    @NotBlank(message = "Surname should not be empty")
    @NotNull(message = "Surname should not be empty")
    @NotEmpty(message = "Surname should not be empty")
    @Size(min = 1, max = 50, message = "Surname should be less 50 than symbols")
    private String lastName;

    @Min(value = 1, message = "Strength should be more than 0")
    private int strength;

    @Min(value = 1, message = "Dexterity should be more than 0")
    private int dexterity;

    @Min(value = 1, message = "Dexterity should be more than 0")
    private int constitution;
}
