package project.gizka.dto.commonDto;

import jakarta.persistence.Transient;
import lombok.*;

@Data
public class FighterDto {

    private String name;
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
