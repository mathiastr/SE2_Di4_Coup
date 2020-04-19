package com.example.coup.Action;

import com.example.coup.Player;

public class Steal extends Action {
        Player targetPlayer;
        //choosen player can counteract by playing StopSteal or the accept -2 coins
        //+2 coins and -2 coins from choosen player
        //if choosen player coins = 1, take -1 coin
        //first let player counteract and then fulfill the actions, so it can be stopped before
    //stopSteal returns true wenn der Steal erfolgreich ohne challenge gestoppt wurde, also NICHT -2 coins
    //wenn false dann -2 coins
        public Steal(Player playerDoingAction){
            super(playerDoingAction);
            this.playerDoingAction=playerDoingAction;

        }
    public void playAction(){
            if(!targetPlayer.getCanBlockSteal()) {
                targetPlayer.setCoins(targetPlayer.getCoins() - 2);
                playerDoingAction.setCoins(playerDoingAction.getCoins() + 2);
            }
    }
    public void setTargetPlayer(Player player){
            this.targetPlayer=player;
    }
    public Player getTargetPlayer(){
            return this.targetPlayer;
    }
}
