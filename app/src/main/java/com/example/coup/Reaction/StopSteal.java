package com.example.coup.Reaction;

import com.example.coup.Action.Action;
import com.example.coup.Player;

public class StopSteal implements Reaction{
    Player player;
    Action preAction;

    public StopSteal(Player player, Action preAction){
        this.player = player;
        this.preAction = preAction;
    }

    @Override
    public void playReaction(){
        //checken ob der Spieler diesen Zug ausführen darf
        //Konsequenzen des Zuges (zB. Anzahl coins einer Person ändern, den View ändern)
    }
}
