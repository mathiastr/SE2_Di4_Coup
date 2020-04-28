package com.example.coup.Action;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.coup.CardType;
import com.example.coup.Player;

public class StopSteal extends Action{
    Action preAction;
    Action thisAction;
    Button btnChooseAmbassador;
    Button btnChooseCaptain;
    Button challenge;
    TextView timer;

    boolean clicked = false;
    int waitingTimeinSec = 30;
    boolean challengedHasCorrectCard;

    CardType choosenCardType;

    public StopSteal(Player player, Action preAction){
        super(player);
        this.playerDoingAction = player;
        this.preAction = preAction;
    }

    public boolean playReaction(){
        //preaction = Steal
        //TODO player can choose between Ambassador or Captain to block the steal
        btnChooseAmbassador.setVisibility(View.VISIBLE);
        btnChooseCaptain.setVisibility(View.VISIBLE);
        btnChooseAmbassador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosenCardType = CardType.AMBASSADOR;
            }
        });
        btnChooseCaptain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosenCardType = CardType.CAPTAIN;
            }
        });

        //every Player can challange
        //TODO make Button Challenge Visible for every Player
        challenge.setVisibility(View.VISIBLE);
        challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO get Player who clicked
                Challenge c = new Challenge(new Player("der geklickt hat"),thisAction);
                challengedHasCorrectCard = c.playReaction(choosenCardType);
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
        //return true wenn der Steal gestoppt wird
        return challengedHasCorrectCard;
    }
}
