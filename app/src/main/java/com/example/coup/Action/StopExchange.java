package com.example.coup.Action;

import com.example.coup.CardType;
import com.example.coup.Player;

public class StopExchange extends Action {
    Action preAction;

    public StopExchange(Player player, Action preAction){
        super(player);
        this.playerDoingAction = player;
        this.preAction = preAction;
    }

    public boolean playReaction(){
        //preaction = Exchange
        //wenn ein Spieler Challenge klickt:

        //return true wenn der Steal gestoppt wird
        Challenge c = new Challenge((playerDoingAction),preAction);
//
        return c.playReaction(CardType.AMBASSADOR);
    }
}
