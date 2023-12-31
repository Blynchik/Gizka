package project.gizka.util;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FightTurn {
    private String attacker;
    private String defender;
    private int attack;
    private int evasion;
    private int damage;
    private int healthPoint;
}
