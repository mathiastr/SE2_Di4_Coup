package com.example.coup.Action;

import com.example.coup.Player;

public class Challenge extends Action{
    Action preAction;

    public Challenge(Player player, Action preAction){
        super(player);
        this.playerDoingAction = player;
        this.preAction = preAction;
    }

    public void playReaction(){
        //preAction.getNeededCardType
        //Player who is challenged:
        //if player has that Card, he can choose show card or loseCard
        //else the player doesn't have that card -> loseCard
        //when he showed the card -> the other player (played the challenge) loseCard
    }
}
