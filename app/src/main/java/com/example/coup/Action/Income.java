package com.example.coup.Action;

import com.example.coup.Player;

public class Income extends Action{

        //+1 coin

    public Income(Player playerDoingAction){
            super(playerDoingAction);
            this.playerDoingAction=playerDoingAction;
    }
    public void playAction(){
    }
}
