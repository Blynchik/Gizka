package project.gizka.util;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
public class FightLog {
    private List<FightTurn> turns = new ArrayList<>();
    private String winner;

    public void addTurn(FightTurn turn)  {
        turns.add(turn);
    }
}
