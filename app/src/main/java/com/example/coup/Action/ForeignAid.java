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
            BlockForeignAid ba1= new BlockForeignAid(playerBlocking,this);
            boolean result = ba1.playReaction();
            //blocking player has Duke and he has been challenged
            if(result&&ba1.isBlockForeignAidChallanged){
                playerDoingAction.loseCard();
            }
            //blocking player has no Duke and he has benn challenged
            if(!result){
                playerBlocking.loseCard();
            }
            //blocking player not challanged
            else{
            //Action blocked!!!
            }
        //foreign aid successfull... nobody wants to block
        }else{
            playerDoingAction.doForeignAidAction();
        }

        }

    public boolean isOtherPlayerWantsToBlock() {
        return otherPlayerWantsToBlock;
    }

    public void setOtherPlayerWantsToBlock(boolean otherPlayerWantsToBlock) {
        this.otherPlayerWantsToBlock = otherPlayerWantsToBlock;
    }
}
