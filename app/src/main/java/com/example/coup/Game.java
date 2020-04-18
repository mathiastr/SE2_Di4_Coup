package com.example.coup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {
    List<Player> players;
    List<Card> playingCards;
    LayoutManager manager = new LayoutManager();

    public Game(List<Player> players){
        this.players = players;
        for (CardType type : CardType.values()) {
            playingCards.add(new Card(type));
            playingCards.add(new Card(type));
            playingCards.add(new Card(type));
        }
        //for each Player set 2 random Cards
        play();
    }

    private void play(){
        //while 2 Players inGame = true
            //get next player whos turn it is
            //choosenAction = manager.next(players.get(x));
            //choosenAction.playAction();
    }
}
