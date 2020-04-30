package com.example.coup.Action;

import com.example.coup.Player;

public class Steal extends Action {
        Player targetPlayer;
        boolean targetPlayerWantsToChallenge =false;
        boolean targetPlayerWantsToBlock =false;

    //choosen player can counteract by playing StopSteal or they accept -2 coins
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
            //TODO get input who the Target Player is
        //TODO announce to target Player that this player wants to steal from him.
        //TODO give Target player option to block, if blocked: new StopSteal();
           
        if(targetPlayerWantsToChallenge==true){
            StopSteal ss1= new StopSteal(targetPlayer,this);
            boolean result = ss1.playReaction();

            if(result==true) {
                playerDoingAction.loseCard();
            }else{
                targetPlayer.loseCard();
                //PlayerdoingAction draws another Card
            }
        }else if(targetPlayerWantsToBlock==true){
            BlockSteal bs1 = new BlockSteal(targetPlayer,this);
            boolean result = bs1.playReaction();
            if(result&&bs1.isBlockStealChallanged()){
                playerDoingAction.loseCard();
            }if(!result){
                targetPlayer.loseCard();
            }else{
                //blocking player not challanged
                //Steal Action blocked!!!
            }
        }
        else{
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
