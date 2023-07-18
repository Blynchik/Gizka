package project.gizka.dto.commonDto;

import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnemyCommonDto {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private int strength;
    private int dexterity;
    private int constitution;

    @Transient
    private int lowAttack;

    @Transient
    private int highAttack;

    @Transient
    private int lowEvasion;

    @Transient
    private int highEvasion;

    @Transient
    private int healthPoint;
}
