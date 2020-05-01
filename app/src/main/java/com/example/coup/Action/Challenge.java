package com.example.coup.Action;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.coup.CardType;
import com.example.coup.Player;

public class Challenge extends Action{
    Action preAction;

    int waitingTimeinSec = 30;

    Button btnShowCard;
    Button btnLoseCard;
    TextView timer;

    boolean clicked = false;

    Player challenged;
    boolean firstCardIsNeeded;
    boolean secondCardIsNeeded;

    boolean returnValue = true;

    public Challenge(Player player, Action preAction){
        super(player);

        //TODO get btnShowCard and btnLoseCard and timer from the Player Layout

        this.preAction = preAction;
    }

    //returns true when challenged player does not have the correct card
    public boolean playReaction(CardType neededCardType){
        //preAction.getNeededCardType
        //Player who is challenged:
        challenged = preAction.playerDoingAction;
        //if player has that Card, he can choose show card or loseCard
        firstCardIsNeeded = challenged.getCards().get(0).getTypeOfCard().equals(neededCardType);
        secondCardIsNeeded = challenged.getCards().get(1).getTypeOfCard().equals(neededCardType);
        if(firstCardIsNeeded | secondCardIsNeeded){
            int i;
            if(firstCardIsNeeded) i=0;
            else i=1;
            return challenged.showOrLoose(i, playerDoingAction);
            //False if show, true if loose
        }
        else challenged.loseCard();
        return true;

        //else the player doesn't have that card -> loseCard
        //when he showed the card -> the other player (played the challenge) loseCard
    }
}
