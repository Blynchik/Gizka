package project.gizka.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @Column(name = "first_name")
    @NotBlank(message = "Name should not be empty")
    @NotNull(message = "Name should not be empty")
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 1, max = 20, message = "Name should be less 20 than symbols")
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "Surname should not be empty")
    @NotNull(message = "Surname should not be empty")
    @NotEmpty(message = "Surname should not be empty")
    @Size(min = 1, max = 50, message = "Surname should be less 50 than symbols")
    private String lastName;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT NOW()")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt = LocalDateTime.now();

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
