package com.example.coup.ActionTest;

import com.example.coup.Card;
import com.example.coup.CardType;
import com.example.coup.Game;
import com.example.coup.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class TestExchange {
    /*
    Player p1,p2,p3,p4;
    LinkedList<Player>listOfPlayers;
    Game game;
    Card c1,c2,c3,c4,c5;
    List<Card> handABASSADORandCAPTIAN,handDUKEandASSASSIAN,handASSASSIANandCAPTIAN,handCONTESSAandDUKE;


    @Before
    public void before(){
        p1 = new Player("P1");
        p2 = new Player("P2");
        p3 = new Player("P3");
        p4 = new Player("P4");
        listOfPlayers= new LinkedList<>();
        listOfPlayers.add(p1);
        listOfPlayers.add(p2);
        listOfPlayers.add(p3);
        listOfPlayers.add(p4);
        game = new Game(listOfPlayers);
    }
    @Test
    public void testExchange() {
        c1 = new Card(CardType.DUKE);
        c2 = new Card(CardType.ASSASSIN);
        c3 = new Card(CardType.CONTESSA);
        c4 = new Card(CardType.AMBASSADOR);
        c5 = new Card(CardType.CAPTAIN);

        handDUKEandASSASSIAN = new LinkedList<Card>();
        handDUKEandASSASSIAN.add(c1);
        handDUKEandASSASSIAN.add(c2);

        handABASSADORandCAPTIAN = new LinkedList<Card>();
        handABASSADORandCAPTIAN.add(c4);
        handABASSADORandCAPTIAN.add(c5);

        handCONTESSAandDUKE = new LinkedList<Card>();
        handCONTESSAandDUKE.add(c3);
        handCONTESSAandDUKE.add(c1);

        handASSASSIANandCAPTIAN = new LinkedList<Card>();
        handASSASSIANandCAPTIAN.add(c2);
        handASSASSIANandCAPTIAN.add(c5);

        p1.setCards(handABASSADORandCAPTIAN);
        p2.setCards(handASSASSIANandCAPTIAN);
        p3.setCards(handDUKEandASSASSIAN);
        p4.setCards(handCONTESSAandDUKE);


        LinkedList<Card> copyOfCards = new LinkedList<Card>();
        copyOfCards = (LinkedList<Card>) p1.getCards();
        Exchange exchange = new Exchange(p1, game);
        exchange.playAction();

        Assert.assertNotEquals(copyOfCards, p1.getCards());
    }

     */

}
