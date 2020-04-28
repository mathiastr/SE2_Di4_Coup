package com.example.coup.ActionTest;

import com.example.coup.Action.Assassinate;
import com.example.coup.Card;
import com.example.coup.CardType;
import com.example.coup.Player;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class TestAssassination {

    Player p1,p2;
    Card c1,c2,c3,c4,c5;
    Assassinate a1;
    List<Card>handDUKEandASSASSIAN;
    List<Card> handABASSADORandCAPTIAN;
    List<Card>handCONTESSAandDUKE;

    @Before
    public void before(){
        p1 = new Player("P1");
        p2 = new Player("P2");

        a1 = new Assassinate(p1);

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

    }

    //P2 has no Contessa
    @Test
    public void testAsssassinationKilled() {
        p1.setCards(handDUKEandASSASSIAN);
        p2.setCards(handABASSADORandCAPTIAN);
//
//        p1.checkForReactions();
//        p2.checkForReactions();

        p1.doIncomeAction();

        a1.setPlayerToAssassinate(p2);
        a1.playAction();

        Assert.assertEquals(1,p2.getCards().size());
    }

    //P2 has Contessa ***This should be in TestStopAssassination***
    @Test
    public void testAssassinationNotKilled(){
        p1.setCards(handDUKEandASSASSIAN);
        p2.setCards(handCONTESSAandDUKE);

        p1.checkForReactions();
        p2.checkForReactions();

        p1.doIncomeAction();

        a1.setPlayerToAssassinate(p2);
        a1.playAction();

        Assert.assertEquals(2,p2.getInfluence());
    }




    @After
    public void after(){
        p1=null;
        p2=null;
        a1=null;
        c1=null;
        c2=null;
        c3=null;
        c4=null;
        c5=null;
    }
}
