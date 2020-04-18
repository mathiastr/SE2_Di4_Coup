package com.example.coup.Reaction;

import com.example.coup.Action.Action;
import com.example.coup.Action.ForeignAid;
import com.example.coup.Player;

public class StopForeignAid implements Reaction{
    Player player;
    Action preAction;
    boolean bluffing;
    //needed Card is Duke

    public StopForeignAid(Player player, Action preAction){
        this.player = player;
        this.preAction = preAction;
    }

    @Override
    public void playReaction(){
        //anderer Spieler (preAction) muss ForeignAid gespielt haben
        //bluffing is false wenn player.cards.contains(Duke)
        //every player has a chance to challenge
        //
    }
}
