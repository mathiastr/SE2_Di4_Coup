package com.example.coup;


public class Assassinate  extends Action  {
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
