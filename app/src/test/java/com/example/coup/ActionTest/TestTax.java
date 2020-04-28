package com.example.coup.ActionTest;

import com.example.coup.Action.Assassinate;
import com.example.coup.Action.Tax;
import com.example.coup.Card;
import com.example.coup.CardType;
import com.example.coup.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class TestTax {
    Player p1,p2;
    Card c1,c2,c3,c4,c5;
    Tax t1;
    List<Card> handDUKEandASSASSIAN;
    List<Card> handABASSADORandCAPTIAN;

    @Before
    public void before(){
        p1 = new Player("P1");
        p2 = new Player("P2");

        t1 = new Tax(p1);

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

    }
    @Test
    public void testTakingThreeCoinsFromTreasury(){
        p1.setCards(handDUKEandASSASSIAN);
        t1.playAction();
        Assert.assertEquals(5,p1.getCoins());
    }

}
