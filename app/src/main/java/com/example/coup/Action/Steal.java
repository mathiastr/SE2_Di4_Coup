package com.example.coup.Action;

import com.example.coup.Player;

public class Steal extends Action {


        //+2 coins and -2 coins from choosen player
        //if choosen player coins = 1, take -1 coin
        //choosen player can counteract by playing StopSteal or the accept -2 coins
        public Steal(Player playerDoingAction){
            super(playerDoingAction);
            this.playerDoingAction=playerDoingAction;

        }
    public void playAction(){ }
}
