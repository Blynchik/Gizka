package project.gizka.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "adventurer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Adventurer {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private AppUser appUser;

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

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT NOW()")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt = LocalDateTime.now();
}
