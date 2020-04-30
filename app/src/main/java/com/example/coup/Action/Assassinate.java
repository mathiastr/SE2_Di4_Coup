package com.example.coup.Action;

import com.example.coup.Player;

public class Assassinate extends Action  {



        //-3 coins
        //launch an assassination against a specific player
        //specific player can counteract with playing StopAssassinate or loseCard
    //stopAssassinate hat einen Rückgabewert der angibt ob das Stoppen erfolgreich war und er nicht gechallenged wurde
    //wenn true dann dann verliert kein Spieler eine Karte
    //wenn false dann loseCard

    Player playerToAssassinate;
    boolean targetPlayerWantsToChallenge =false;

    public Assassinate(Player playerDoingAction){
        super(playerDoingAction);
        this.playerDoingAction=playerDoingAction;
    }

    public void playAction(){
        //needs to check if challanged also!!!

        if(playerDoingAction.isAssassinsActionPossible()==true&&!playerToAssassinate.getCanBlockAssassination()){
            //TODO let target player choose StopAssassinate or LoseCard

            //playerToAssassinate.loseInfluence();
            if(targetPlayerWantsToChallenge ==true){
            StopAssassinate sa1 =new StopAssassinate(playerToAssassinate,this);
            boolean result = sa1.playReaction();
            if(result==true){
                playerDoingAction.loseCard();
            }else{
                playerToAssassinate.loseCard();
                //playerDoingAction  replace Assassin Card with random card from the deck
            }
            }
            else {
                playerDoingAction.setCoins(playerDoingAction.getCoins()-3);
                playerToAssassinate.loseCard();
            }
        }
        else{
            //Print Action not possible
        }
    }
    public void setPlayerToAssassinate(Player p){
        this.playerToAssassinate=p;
    }
    public Player getPlayerToAssassinate(){
        return this.playerToAssassinate;
    }

    public boolean getOtherPlayerWantsToChallenge() {
        return targetPlayerWantsToChallenge;
    }

    public void setOtherPlayerWantsToChallenge(boolean otherPlayerWantsToChallenge) {
        this.targetPlayerWantsToChallenge = otherPlayerWantsToChallenge;
    }
}
