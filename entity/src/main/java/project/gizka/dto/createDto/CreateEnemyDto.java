package project.gizka.dto.createDto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class CreateEnemyDto {

    @NotBlank(message = "Name should not be empty")
    @NotNull(message = "Name should not be empty")
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 1, max = 50, message = "Name should be less 50 than symbols")
    private String name;

    @NotBlank(message = "Description should not be empty")
    @NotNull(message = "Description should not be empty")
    @NotEmpty(message = "Description should not be empty")
    @Size(min = 1, max = 500, message = "Description should be less 50 than symbols")
    private String description;

    @Column(name = "strength")
    @Min(value = 1, message = "Strength should be more than 0")
    private int strength;

    @Column(name = "dexterity")
    @Min(value = 1, message = "Dexterity should be more than 0")
    private int dexterity;

    @Column(name = "constitution")
    @Min(value = 1, message = "Dexterity should be more than 0")
    private int constitution;
}
