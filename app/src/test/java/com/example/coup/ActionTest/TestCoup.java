package com.example.coup.ActionTest;

import com.example.coup.Card;
import com.example.coup.CardType;
import com.example.coup.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class TestCoup {
    Player p1, p2;
    Card c1, c2, c3, c4, c5;
    Coup coup1;
    List<Card> handDUKEandASSASSIAN;
    List<Card> handABASSADORandCAPTIAN;

    @Before
    public void before() {
        p1 = new Player("P1");
        p2 = new Player("P2");

        coup1 = new Coup(p1);
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
    public void testCoupCostSevenCoins(){
        p1.setCards(handABASSADORandCAPTIAN);
        p2.setCards(handDUKEandASSASSIAN);
        p1.setCoins(7);
        coup1.setTargetPlayer(p2);

        coup1.playAction();

        Assert.assertEquals(0,p1.getCoins());
    }
    @Test
    public void testCoupTargetPlayerLostInfluence(){
        p1.setCards(handABASSADORandCAPTIAN);
        p2.setCards(handDUKEandASSASSIAN);
        p1.setCoins(7);
        coup1.setTargetPlayer(p2);

        coup1.playAction();

        Assert.assertEquals(1,p2.getCards().size());
    }

}
