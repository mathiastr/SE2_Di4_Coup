package com.example.coup.Action;

import com.example.coup.CardType;
import com.example.coup.Player;

public class StopAssassinate extends Action {
    Action preAction;
    boolean bluffing;

    public StopAssassinate(Player player, Action preAction){
        super(player);
        this.playerDoingAction = player;
        this.preAction = preAction;
    }

    //return true when stopAssassinate geht durch
    public boolean playReaction(){
        //preaction = Assassinate

        //every player has a chance to challenge
        //es gibt eine bestimmte Zeit um Challenge zu klicken
        //der erste der challenge klickt, new Challenge().playAction; und return den Rückgabetyp
        //keiner klickt auf challenge, return true;
        return true;

    }
}

