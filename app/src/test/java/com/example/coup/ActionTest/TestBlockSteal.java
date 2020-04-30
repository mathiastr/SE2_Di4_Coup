package com.example.coup.ActionTest;

import com.example.coup.Action.BlockSteal;
import com.example.coup.Action.Steal;
import com.example.coup.Card;
import com.example.coup.CardType;
import com.example.coup.Player;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class TestBlockSteal {
    Player p1,p2;
    Card c1,c2,c3,c4,c5;
    Steal s1;
    List<Card> handDUKEandASSASSIAN;
    List<Card> handABASSADORandCAPTIAN;
    List<Card>handCAPTAINandDUKE;

    @Before
    public void before(){
        p1 = new Player("P1");
        p2 = new Player("P2");

        s1 = new Steal(p1);

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

        handCAPTAINandDUKE = new LinkedList<Card>();
        handCAPTAINandDUKE.add(c5);
        handCAPTAINandDUKE.add(c1);
    }
    @Test
    public void testBlockStealSuccessfulNotChallenged() {
        p1.setCards(handDUKEandASSASSIAN);
        p2.setCards(handCAPTAINandDUKE);
        BlockSteal blockSteal = new BlockSteal(p2,s1);
        Assert.assertTrue(blockSteal.playReaction());
    }
    @Test
    public void testBlockStealSuccessfulChallenged() {
        p1.setCards(handDUKEandASSASSIAN);
        p2.setCards(handCAPTAINandDUKE);
        BlockSteal blockSteal = new BlockSteal(p2,s1);
        blockSteal.setBlockStealChallanged(true);
        Assert.assertTrue(blockSteal.playReaction());
    }
    @Test
    public void testBlockStealFailed(){
        p1.setCards(handCAPTAINandDUKE);
        p2.setCards(handDUKEandASSASSIAN);
        BlockSteal blockSteal = new BlockSteal(p2,s1);
        blockSteal.setBlockStealChallanged(true);
        Assert.assertFalse(blockSteal.playReaction());
    }
}
