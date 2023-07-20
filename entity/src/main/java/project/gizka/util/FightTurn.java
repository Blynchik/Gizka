package project.gizka.util;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FightTurn {
    private String attacker;
    private String defender;
    private int attack;
    private int evasion;
    private int damage;
    private int healthPoint;
}
