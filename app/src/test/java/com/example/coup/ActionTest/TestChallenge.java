package com.example.coup.ActionTest;

import com.example.coup.Card;
import com.example.coup.CardType;
import com.example.coup.Player;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class TestChallenge {
    Challenge challenge;
    Player stopStealer;
    Player challenger;
    Action preAction;

    @Before
    public void before(){
        stopStealer = new Player("Stop the Steal");
        stopStealer.setCards(new ArrayList<Card>());
        challenger = new Player("challenge him");
        challenger.addCard(new Card(CardType.AMBASSADOR));
        challenger.addCard(new Card(CardType.CONTESSA));
        preAction = new StopSteal(stopStealer, null);
        challenge = new Challenge(challenger,preAction);
    }

    @Test
    public void playReactionWrongCard(){
        Assert.assertEquals(2, stopStealer.getCards().size());
        stopStealer.addCard(new Card(CardType.AMBASSADOR));
        stopStealer.addCard(new Card(CardType.CONTESSA));
        boolean value = challenge.playReaction(CardType.CAPTAIN);
        Assert.assertEquals(true, value);
        Assert.assertEquals(1, stopStealer.getCards().size());
    }

    @Test
    public void playReactionRightCardTimeOut(){
        stopStealer.addCard(new Card(CardType.CAPTAIN));
        stopStealer.addCard(new Card(CardType.CONTESSA));
        Assert.assertEquals(2, stopStealer.getCards().size());
        boolean value = challenge.playReaction(CardType.CAPTAIN);


        Assert.assertEquals(true, value);
        Assert.assertEquals(1, stopStealer.getCards().size());
    }

}
