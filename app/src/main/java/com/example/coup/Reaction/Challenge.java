package com.example.coup.Reaction;

import com.example.coup.Action.Action;
import com.example.coup.Player;

public class Challenge implements Reaction{
    Player player;
    Action preAction;

    public Challenge(Player player, Action preAction){
        this.player = player;
        this.preAction = preAction;
    }

    @Override
    public void playReaction(){
        //preAction.getNeededCardType
        //Player who is challenged:
        //if player has that Card, he can choose show card or loseCard
        //else the player doesn't have that card -> loseCard
        //when he showed the card -> the other player (played the challenge) loseCard
    }
}
