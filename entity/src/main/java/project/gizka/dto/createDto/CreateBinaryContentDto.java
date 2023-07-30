package project.gizka.dto.createDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateBinaryContentDto {

    @NotBlank(message = "Path should not be empty")
    @NotNull(message = "Path should not be empty")
    @NotEmpty(message = "Path should not be empty")
    private String pathToImage;

    @Size(min = 1, max = 50, message = "Name should be less than 50 symbols")
    @NotBlank(message = "Name should not be empty")
    @NotNull(message = "Name should not be empty")
    @NotEmpty(message = "Name should not be empty")
    private String name;
}
