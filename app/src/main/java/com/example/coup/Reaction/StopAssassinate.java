package com.example.coup.Reaction;

import com.example.coup.Action.Action;
import com.example.coup.Player;

public class StopAssassinate implements Reaction {
    Player player;
    Action preAction;
    boolean bluffing;

    public StopAssassinate(Player player, Action preAction){
        this.player = player;
        this.preAction = preAction;
    }

    @Override
    public void playReaction(){
        //preaction = Assassinate
        //bluffing = false if player has Contessa
        //every player has a chance to challenge
    }
}

