package com.example.coup.Action;

import com.example.coup.Player;

public class StopAssassinate extends Action {
    Action preAction;
    boolean bluffing;

    public StopAssassinate(Player player, Action preAction){
        super(player);
        this.playerDoingAction = player;
        this.preAction = preAction;
    }

    public void playReaction(){
        //preaction = Assassinate
        //bluffing = false if player has Contessa
        //every player has a chance to challenge
    }
}

