package project.gizka.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "binary_content")
@Data
public class BinaryContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @NotBlank(message = "Name should not be empty")
    @NotNull(message = "Name should not be empty")
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 1, max = 50, message = "Name should be less than 50 symbols")
    private String name;

    @Column(name = "content_as_bytes")
    @Size(min = 1, max = 3_000_000, message = "Content should be less than 3MB")
    private byte[] contentAsBytes;


    @Column(name = "type")
    @NotBlank(message = "Type should not be empty")
    @NotNull(message = "Type should not be empty")
    @NotEmpty(message = "Type should not be empty")
    private  String type;
}
