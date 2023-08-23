package project.gizka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
