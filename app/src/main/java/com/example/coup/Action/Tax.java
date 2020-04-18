package com.example.coup.Action;

import com.example.coup.Player;

public class Tax extends Action {

    //needed Card is Duke

        //+3 coins
        //every player can challenge

    public Tax(Player playerDoingAction){
        super(playerDoingAction);
        this.playerDoingAction=playerDoingAction;

    }
    public void playAction(){
        playerDoingAction.doDukesAction();
    }
}
