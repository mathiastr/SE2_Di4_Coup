package com.example.coup.Action;

import com.example.coup.CardType;
import com.example.coup.Player;

public class StopForeignAid extends Action{

    //// THIS CLASS SHOULD BE DELETED??? -> BlockForeignAid added.
    Action preAction;

    public StopForeignAid(Player player, Action preAction){
        super(player);
        this.playerDoingAction = player;
        this.preAction = preAction;
    }

    public boolean playReaction(){
        //anderer Spieler (preAction) muss ForeignAid gespielt haben

        //TODO every player has a chance to challenge
        //wenn ein Spieler Challenge klickt:
        Challenge c = new Challenge(playerDoingAction,this);
        //return true wenn der ForeignAid gestoppt wird
        return c.playReaction(CardType.DUKE);

    }
}
