package com.example.coup.Action;

import com.example.coup.Player;

public class Coup extends Action {


        //-7 coins
        //read the input against what player
        //if that player only has one card left, player.loseCard(last card), player.inGame = false
        //sonst kann der Spieler wählen welche Karte verloren geht, input einlesen, player.loseCard(choosen card)
        //always successful

    public Coup(Player playerDoingAction){
    super(playerDoingAction);
        this.playerDoingAction=playerDoingAction;

}
    public void playAction(){
    }
}
