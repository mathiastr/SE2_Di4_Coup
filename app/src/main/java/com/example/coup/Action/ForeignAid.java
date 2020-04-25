package com.example.coup.Action;

import com.example.coup.Player;

public class ForeignAid extends Action {

    boolean otherPlayerWantsToBlock =false;
    Player playerBlocking;

    //+2 coins
        //every player has chance to play StopForeignAid
        public ForeignAid(Player playerDoingAction){
            super(playerDoingAction);
            this.playerDoingAction=playerDoingAction;

        }
    public void playAction(){
            //TODO announce ForeignAid and give every Player chance to block. if blocked: new StopForeignAid
            //if(isActionBlocked==false){
              //  playerDoingAction.doForeignAidAction();
            //}

        if(otherPlayerWantsToBlock==true){
            StopForeignAid sfa1 = new StopForeignAid(playerBlocking,this);
            boolean result = sfa1.playReaction();

            if(result==true){
                playerBlocking.loseCard();
            }
            else{
                playerDoingAction.doForeignAidAction();
            }
        }else{
            playerDoingAction.doForeignAidAction();
        }

        }
}
