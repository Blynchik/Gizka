package project.gizka.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FightLog {
    private List<FightTurn> turns = new ArrayList<>();
    private String winner;

    public void addTurn(FightTurn turn)  {
        turns.add(turn);
    }
}
