package com.example.coup.Action;

import com.example.coup.Player;

public class ForeignAid extends Action {


        //+2 coins
        //every player has chance to play StopForeignAid
        public ForeignAid(Player playerDoingAction){
            super(playerDoingAction);
            this.playerDoingAction=playerDoingAction;

        }
    public void playAction(){
            //TODO announce ForeignAid and give every Player chance to block. if blocked: new StopForeignAid
            //if(isActionBlocked==false){
                playerDoingAction.doForeignAidAction();
            //}
        }
}
