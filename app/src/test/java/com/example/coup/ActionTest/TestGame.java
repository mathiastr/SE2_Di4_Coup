package com.example.coup.ActionTest;

import com.example.coup.Action.Exchange;
import com.example.coup.Card;
import com.example.coup.CardType;
import com.example.coup.Game;
import com.example.coup.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class TestGame {
    Player p1, p2, p3, p4;
    LinkedList<Player> listOfPlayers;
    Game game;
    Card c1, c2, c3, c4, c5;


    @Before
    public void before() {
        p1 = new Player("P1");
        p2 = new Player("P2");
        p3 = new Player("P3");
        p4 = new Player("P4");
        listOfPlayers = new LinkedList<>();
        listOfPlayers.add(p1);
        listOfPlayers.add(p2);
        listOfPlayers.add(p3);
        listOfPlayers.add(p4);
        c1 = new Card(CardType.DUKE);
        c2 = new Card(CardType.ASSASSIN);
        c3 = new Card(CardType.CONTESSA);
        c4 = new Card(CardType.AMBASSADOR);
        c5 = new Card(CardType.CAPTAIN);

        game = new Game(listOfPlayers);
        game.dealStartOfGame();

    }

    @Test
    //Cards total = 15
    //4 Players * 2 Cards = 8
    //Cards left = 7
    public void testDealStartOfGame(){
        Assert.assertEquals(7,game.getCards().size());
    }
    //Cards total = 15
    //4 Players * 2 Cards = 8
    //Cards left = 7
    //Exchange Action= Player draws 2 cards.
    // Than player has 4 cards in total.
    //He chooses 2 cards from 4 and returns 2 to the deck.
    @Test
    public void testExchangeAction(){
        Exchange exchange = new Exchange(p1,game);
        exchange.playAction();
        Assert.assertEquals(7,game.getCards().size());
    }
}
