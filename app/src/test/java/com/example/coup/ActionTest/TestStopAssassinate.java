package com.example.coup.ActionTest;

import com.example.coup.Card;
import com.example.coup.CardType;
import com.example.coup.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class TestStopAssassinate {
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
    //Player that is challenged doesnt has the card he is claiming to have
    @Test
    public void testStopAssassinateSuccessful(){
        p1.setCards(handABASSADORandCAPTIAN);
        p2.setCards(handABASSADORandCAPTIAN);

        StopAssassinate s1 = new StopAssassinate(p2,a1);

        Assert.assertTrue(s1.playReaction());
    }
    //Player has a card that he is claiming to have
    @Test
    public void testStopAssasinateFailed(){

        p1.setCards(handDUKEandASSASSIAN);
        p2.setCards(handABASSADORandCAPTIAN);

        StopAssassinate s2 = new StopAssassinate(p2,a1);

        Assert.assertFalse(s2.playReaction());
    }

}
