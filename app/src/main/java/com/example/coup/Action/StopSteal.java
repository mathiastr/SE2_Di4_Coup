package com.example.coup.Action;

import com.example.coup.Player;

public class StopSteal extends Action{
    Action preAction;
    boolean bluffing;

    public StopSteal(Player player, Action preAction){
        super(player);
        this.playerDoingAction = player;
        this.preAction = preAction;
    }

    public void playReaction(){
        //preaction = Steal
        //player can choose between Ambassador or Captain to block the steal
        //bluffing = false if player has either Ambassador or Captain
        //every Player can challange
    }
}
