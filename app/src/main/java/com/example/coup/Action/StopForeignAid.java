package com.example.coup.Action;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.coup.CardType;
import com.example.coup.Player;

import java.util.List;

public class StopForeignAid extends Action {
    Action preAction;
    Action thisAction;
    Player thisPlayer;
    public Challenge c;

    public Player whoClickedChallenge;
    public int isClickedChallenge;
    //All players of Game
    public List<Player> players = null;

    boolean challengedHasCorrectCard;


    public StopForeignAid(Player player, Action preAction) {
        super(player);
        thisPlayer = player;
        thisAction = this;
        this.playerDoingAction = player;
        this.preAction = preAction;
        whoClickedChallenge = null;
        isClickedChallenge = 0;
    }


    public boolean playReaction() {
        //anderer Spieler (preAction) muss ForeignAid gespielt haben

        //every player has a chance to challenge
        //TODO wie bekomme ich die Liste von allen Playern aus Game

        for (Player p : players) {
            if(!p.equals(thisPlayer)){
                p.startTimer(30, this);
                p.challenge(this);
            }

        }

        while (isClickedChallenge == 0) {
            //sollte nicht in eine Dauerschleife kommen, da notfalls immer der Timer ausläuft unf isClickedChallenge == -1 setzt
        }
        if (isClickedChallenge == -1) {
            //der Timer ist ausgelaufen
            //niemand hat StopForeignAid gechallenged
            //StopForeignAid geht problemlos durch
            return true;
            //TODO true oder false???
        } else if (isClickedChallenge == 1) {
            //jemand hat Challenge geklickt
            c = new Challenge(whoClickedChallenge, this);
            challengedHasCorrectCard = c.playReaction(CardType.DUKE);
            //For testing
            //challengedHasCorrectCard = true;
        }
        return !challengedHasCorrectCard;
    }
}
