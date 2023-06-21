package project.gizka.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "app_user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class AppUser {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String chatId;

    @Column(name = "registered_at", columnDefinition = "timestamp default now()")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime registeredAt = LocalDateTime.now();

    @Column(name = "updated_at", columnDefinition = "timestamp default now()")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt = LocalDateTime.now();
}
