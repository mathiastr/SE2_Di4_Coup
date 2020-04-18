package com.example.coup.Action;

import com.example.coup.Player;

public class Assassinate extends Action  {

        //-3 coins
        //launch an assassination against a specific player
        //specific player can counteract with playing StopAssassinate or loseCard

    Player playerToAssassinate;

    public Assassinate(Player playerDoingAction){
        super(playerDoingAction);
        this.playerDoingAction=playerDoingAction;
    }

    public void playAction(){
        //needs to check if challanged also!!!

        if(playerDoingAction.isAssassinsActionPossible()==true&&!playerToAssassinate.getCanBlockAssassination()){
            playerToAssassinate.setInfluence(1);
        }
    }
    public void setPlayerToAssassinate(Player p){
        this.playerToAssassinate=p;
    }
    public Player getPlayerToAssassinate(){
        return this.playerToAssassinate;
    }


}
