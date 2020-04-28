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

            //give Player option to choose "show your Card" or "lose a card"
            btnShowCard.setVisibility(View.VISIBLE);
            btnLoseCard.setVisibility(View.VISIBLE);
            btnShowCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    returnValue = show();
                    clicked = true;
                    btnShowCard.setVisibility(View.INVISIBLE);
                    btnLoseCard.setVisibility(View.INVISIBLE);
                }
            });
            btnLoseCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    challenged.loseCard();
                    returnValue = true;
                    clicked = true;
                    btnShowCard.setVisibility(View.INVISIBLE);
                    btnLoseCard.setVisibility(View.INVISIBLE);
                }
            });
            //wait until clicked
            try {
                int time = waitingTimeinSec;
                timer.setVisibility(View.VISIBLE);
                while(time>=0 && !clicked) {
                    timer.setText(time + " sec");
                    wait(1000);
                    time--;
                }
                timer.setVisibility(View.INVISIBLE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return returnValue;
        }
        else challenged.loseCard();

        return true;

        //else the player doesn't have that card -> loseCard
        //when he showed the card -> the other player (played the challenge) loseCard
    }


    private boolean show(){
        if(firstCardIsNeeded) challenged.getCards().get(0).revealCard();
        else challenged.getCards().get(1).revealCard();
        this.playerDoingAction.loseCard();
        return false;
    }
}
