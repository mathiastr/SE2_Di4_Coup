package com.example.coup.Action;

import com.example.coup.CardType;
import com.example.coup.Player;

public class StopAssassinate extends Action {
    Action preAction;
    boolean bluffing;
    Player playerAttemptingAssassination;

    public StopAssassinate(Player player, Action preAction){
        super(player);
        this.playerDoingAction = player;
        this.preAction = preAction;
    }

    //return true when stopAssassinate geht durch
    public boolean playReaction(){
        //preaction = Assassinate

    // mesa:
        /*when player1 challenges player2 and Challenge is successful
        * successful means -> player2 not having the card he claimed.
        * than method playReaction returns true
        * and if challenge is unsuccessful that means player2 has the proper card
        * for action he called than playReaction should return false
        * */

        Challenge c1 = new Challenge(playerDoingAction,preAction);
        return c1.playReaction(CardType.ASSASSIN);


    }
}

