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
        CardType choosenCardType = CardType.AMBASSADOR;
        //every Player can challange
        //wenn ein Spieler Challenge klickt:
        Challenge c = new Challenge(new Player("der geklickt hat"),this);
        boolean challengedHasCorrectCard = c.playReaction(choosenCardType);
        //return true wenn der Steal gestoppt wird
        return challengedHasCorrectCard;
    }
}
