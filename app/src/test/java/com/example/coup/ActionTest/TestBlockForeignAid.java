package com.example.coup.ActionTest;

import com.example.coup.Action.Assassinate;
import com.example.coup.Action.BlockForeignAid;
import com.example.coup.Action.ForeignAid;
import com.example.coup.Card;
import com.example.coup.CardType;
import com.example.coup.Player;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class TestBlockForeignAid {
    Player p1,p2;
    Card c1,c2,c3,c4,c5;
    ForeignAid fa1;
    List<Card> handDUKEandASSASSIAN;
    List<Card> handABASSADORandCAPTIAN;
    List<Card>handCONTESSAandDUKE;

    @Before
    public void before(){
        p1 = new Player("P1");
        p2 = new Player("P2");

        fa1 = new ForeignAid(p1);

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
    public void testBlockForeignAidSuccessfullNotChallanged(){
        p1.setCards(handABASSADORandCAPTIAN);
        p2.setCards(handCONTESSAandDUKE);
        BlockForeignAid bfa1 = new BlockForeignAid(p2,fa1);
        Assert.assertTrue(bfa1.playReaction());
    }
    @Test
    public void testBlockForeignAidSuccessfullChallanged(){
        p1.setCards(handABASSADORandCAPTIAN);
        p2.setCards(handCONTESSAandDUKE);
        BlockForeignAid bfa1 = new BlockForeignAid(p2,fa1);
        bfa1.setBlockForeignAidChallanged(true);
        Assert.assertTrue(bfa1.playReaction());
    }
    @Test
    public void testBlockForeignAidFailed(){
        p1.setCards(handABASSADORandCAPTIAN);
        p2.setCards(handABASSADORandCAPTIAN);
        BlockForeignAid bfa1 = new BlockForeignAid(p2,fa1);
        bfa1.setBlockForeignAidChallanged(true);
        Assert.assertFalse(bfa1.playReaction());
    }
}
