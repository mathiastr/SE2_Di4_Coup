package com.example.coup.Action;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.coup.CardType;
import com.example.coup.Player;

public class StopForeignAid extends Action{
    Action preAction;
    Action thisAction;
    Button challenge;
    TextView timer;

    boolean clicked = false;
    int waitingTimeinSec = 30;

    boolean challengedHasCorrectCard;



    public StopForeignAid(Player player, Action preAction){
        super(player);
        thisAction = this;
        //TODO get Button Challenge
        this.playerDoingAction = player;
        this.preAction = preAction;
    }

    public boolean playReaction(){
        //anderer Spieler (preAction) muss ForeignAid gespielt haben

        //every player has a chance to challenge
        //TODO make Button Challenge Visible for every Player
        challenge.setVisibility(View.VISIBLE);
        challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO get Player who clicked
                Challenge c = new Challenge(new Player("der geklickt hat"),thisAction);
                challengedHasCorrectCard = c.playReaction(CardType.DUKE);
                clicked = true;
                challenge.setVisibility(View.INVISIBLE);
            }
        });

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
        //return true wenn der ForeignAid gestoppt wird
        return challengedHasCorrectCard;
    }
}
