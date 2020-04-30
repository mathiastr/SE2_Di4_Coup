package com.example.coup.ActionTest;

import com.example.coup.Action.Assassinate;
import com.example.coup.Action.BlockAssassinate;
import com.example.coup.Card;
import com.example.coup.CardType;
import com.example.coup.Player;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class TestBlockAssassinate {
    Player p1,p2;
    Card c1,c2,c3,c4,c5;
    Assassinate a1;
    List<Card> handDUKEandASSASSIAN;
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
    @Test
    public void testBlockAssassinateSuccessfulNotChallenged() {
        p1.setCards(handDUKEandASSASSIAN);
        p2.setCards(handCONTESSAandDUKE);
        BlockAssassinate blockAssassinate=new BlockAssassinate(p2,a1);
        Assert.assertTrue(blockAssassinate.playReaction());
    }
    @Test
    public void testBlockAssassinateSuccessfullChallanged() {
        p1.setCards(handDUKEandASSASSIAN);
        p2.setCards(handCONTESSAandDUKE);

        BlockAssassinate blockAssassinate=new BlockAssassinate(p2,a1);
        blockAssassinate.setBlockAssassinateChallanged(true);
        Assert.assertTrue(blockAssassinate.playReaction());
    }
    @Test
    public void testBlockAssassinateFailed(){
        p1.setCards(handDUKEandASSASSIAN);
        p2.setCards(handABASSADORandCAPTIAN);
        BlockAssassinate blockAssassinate=new BlockAssassinate(p2,a1);
        blockAssassinate.setBlockAssassinateChallanged(true);
        Assert.assertFalse(blockAssassinate.playReaction());

    }
    }
