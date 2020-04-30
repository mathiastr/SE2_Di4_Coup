package com.example.coup.ActionTest;

import com.example.coup.Action.Assassinate;
import com.example.coup.Action.Exchange;
import com.example.coup.Action.StopAssassinate;
import com.example.coup.Action.StopExchange;
import com.example.coup.Card;
import com.example.coup.CardType;
import com.example.coup.Game;
import com.example.coup.Player;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class TestStopExchange {
    Player p1,p2,p3,p4;
    Card c1,c2,c3,c4,c5;
    Exchange e1;
    List<Card> handDUKEandASSASSIAN;
    List<Card> handABASSADORandCAPTIAN;
    List<Card>handCONTESSAandDUKE;
    List<Card>handCAPTAINandCONTESSA;
    Game game;
    @Before
    public void before(){

        p1 = new Player("P1");
        p2 = new Player("P2");
        p3 = new Player("P3");
        p4 = new Player("P4");
        List<Player> players = new LinkedList<>();
        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);

        game = new Game(players);
        e1 = new Exchange(p1,game);

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

        handCAPTAINandCONTESSA= new LinkedList<Card>();
        handCONTESSAandDUKE.add(c3);
        handCONTESSAandDUKE.add(c5);

    }
    //Player that is challenged doesnt has the card he is claiming to have
    @Test
    public void testStopExchangeSuccessful() {

        p1.setCards(handDUKEandASSASSIAN);
        p2.setCards(handCONTESSAandDUKE);

        StopExchange se2 = new StopExchange(p2,e1);
        Assert.assertTrue(se2.playReaction());
    }
    //Player that is challenged has the card he is claiming to have
    @Test
    public void testStopExchangeFailed() {
        p1.setCards(handABASSADORandCAPTIAN);
        p2.setCards(handDUKEandASSASSIAN);

        StopExchange se2 = new StopExchange(p2,e1);
        Assert.assertFalse(se2.playReaction());
    }
    @After
    public void after(){
        e1=null;
        game=null;
        p1=null;
        p2=null;
        p3=null;
        p4=null;
    }
    }
