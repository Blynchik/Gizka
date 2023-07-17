package project.gizka.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "app_user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AppUser {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat", unique = true)
    @NotBlank(message = "Chat should not be empty")
    @NotNull(message = "Chat should not be empty")
    @NotEmpty(message = "Chat should not be empty")
    private String chat;

    @Column(name = "line")
    @Size(min = 0, max = 100, message = "Slogan should be less than 100 symbols")
    private String line;

    @Column(name = "registered_at", columnDefinition = "TIMESTAMP DEFAULT NOW()")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime registeredAt = LocalDateTime.now();

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT NOW()")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "appUser", fetch = FetchType.LAZY)
    private List<Adventurer> adventurers;
}
