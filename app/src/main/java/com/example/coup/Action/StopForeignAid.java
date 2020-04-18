package com.example.coup.Action;

import com.example.coup.Player;

public class StopForeignAid extends Action{
    Action preAction;
    boolean bluffing;
    //needed Card is Duke

    public StopForeignAid(Player player, Action preAction){
        super(player);
        this.playerDoingAction = player;
        this.preAction = preAction;
    }

    public void playReaction(){
        //anderer Spieler (preAction) muss ForeignAid gespielt haben
        //bluffing is false wenn player.cards.contains(Duke)
        //every player has a chance to challenge
        //
    }
}
