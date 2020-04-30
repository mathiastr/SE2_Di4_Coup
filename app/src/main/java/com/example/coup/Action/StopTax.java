package com.example.coup.Action;

import com.example.coup.CardType;
import com.example.coup.Player;

public class StopTax extends Action {
    Action preAction;

    public StopTax(Player player, Action preAction){
        super(player);
        this.playerDoingAction = player;
        this.preAction = preAction;
    }

    public boolean playReaction(){
        Challenge c = new Challenge((playerDoingAction),preAction);
        return c.playReaction(CardType.DUKE);

    }
}
