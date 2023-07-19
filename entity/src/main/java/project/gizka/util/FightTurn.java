package project.gizka.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FightTurn {
    private String attacker;
    private String defender;
    private int attack;
    private int evasion;
    private int damage;
    private int healthPoint;
}
