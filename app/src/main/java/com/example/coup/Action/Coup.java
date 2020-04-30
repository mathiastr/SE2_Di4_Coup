package com.example.coup.Action;

import com.example.coup.Player;

public class Coup extends Action {

    Player targetPlayer;

        //-7 coins
        //read the input against what player
        //if that player only has one card left, player.loseCard(last card), player.inGame = false
        //sonst kann der Spieler wählen welche Karte verloren geht, input einlesen, player.loseCard(choosen card)
        //always successful

    public Coup(Player playerDoingAction){
    super(playerDoingAction);
        this.playerDoingAction=playerDoingAction;

}
    public void playAction(){
        if(playerDoingAction.ifCoupPossibleDoIt()==true){
            if(targetPlayer.getCards().size()==1){
                playerDoingAction.setCoins(playerDoingAction.getCoins()-7);
                targetPlayer.loseCard();
                targetPlayer.setInGame(false);
            }
            else if(targetPlayer.getCards().size()==2){
                playerDoingAction.setCoins(playerDoingAction.getCoins()-7);
                targetPlayer.loseCard();
            }
        }
    }
    public void setTargetPlayer(Player player){
        this.targetPlayer=player;
    }
    public Player getTargetPlayer(){
        return this.targetPlayer;
    }



}
