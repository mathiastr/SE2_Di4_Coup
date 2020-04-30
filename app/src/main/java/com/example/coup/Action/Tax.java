package com.example.coup.Action;

import com.example.coup.Player;

public class Tax extends Action {

    boolean otherPlayerWantsToChallenge =false;
    Player playerChallenging;
    //needed Card is Duke

        //+3 coins
        //every player can challenge

    public Tax(Player playerDoingAction){
        super(playerDoingAction);
        this.playerDoingAction=playerDoingAction;

    }
    public void playAction(){
        //TODO announce Tax and give every Player chance to challenge, if challenged: new Challenge();
        if(otherPlayerWantsToChallenge==true){
            StopTax st1 = new StopTax(playerChallenging,this);
            boolean result = st1.playReaction();
            if(result==true){
                playerDoingAction.loseCard();
            }else{
                playerChallenging.loseCard();
            }
        }else{
            playerDoingAction.setCoins(playerDoingAction.getCoins()+3);
        }


    }
}
