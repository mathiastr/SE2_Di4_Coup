package com.example.coup.Action;

import com.example.coup.CardType;
import com.example.coup.Player;

public class StopSteal extends Action{
    Action preAction;

    public StopSteal(Player player, Action preAction){
        super(player);
        this.playerDoingAction = player;
        this.preAction = preAction;
    }

    public boolean playReaction(){
        //preaction = Steal
        //TODO player can choose between Ambassador or Captain to block the steal
        //every Player can challange
        //wenn ein Spieler Challenge klickt:

        //return true wenn der Steal gestoppt wird
//        Challenge c = new Challenge((playerDoingAction),this);
          Challenge c = new Challenge((playerDoingAction),preAction);

//        if(c.playReaction(CardType.AMBASSADOR)==false||c.playReaction(CardType.CAPTIAN)==false){
//            return false;
//        }
//        else return true;
       return c.playReaction(CardType.CAPTAIN);
    }
}
