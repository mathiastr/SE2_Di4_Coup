package com.example.coup.Reaction;

import com.example.coup.Action.Action;
import com.example.coup.Player;

public class StopSteal implements Reaction{
    Player player;
    Action preAction;
    boolean bluffing;

    public StopSteal(Player player, Action preAction){
        this.player = player;
        this.preAction = preAction;
    }

    @Override
    public void playReaction(){
        //preaction = Steal
        //player can choose between Ambassador or Captain to block the steal
        //bluffing = false if player has either Ambassador or Captain
        //every Player can challange
    }
}
