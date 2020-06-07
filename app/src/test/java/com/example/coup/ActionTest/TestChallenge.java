package com.example.coup.ActionTest;

import com.example.coup.AllActions;
import com.example.coup.Card;
import com.example.coup.CardType;
import com.example.coup.Game;
import com.example.coup.Player;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.Arrays;

//@RunWith(MockitoJUnitRunner.class)
public class TestChallenge {
    //p1 clicked Challenge
    //p2 is challenged
    Player p1;
    Player p2;
    Game game;
    AllActions actions;


    /**
    @Before
    public void before(){
        p1 = new Player();
        p2 = new Player();
        game = new Game(Arrays.asList(p1,p2));
        p1.addCard(new Card(CardType.AMBASSADOR));
        p1.addCard(new Card(CardType.AMBASSADOR));
        p2.addCard(new Card(CardType.ASSASSIN));
        p2.addCard(new Card(CardType.CONTESSA));
        actions = new AllActions(game);
    }

    @Test
    public void testWrongCard(){
        Assert.assertEquals(2, p2.getCards().size());
        Assert.assertEquals(true, actions.challenge(CardType.DUKE, p1, p2));
        //p2 lost one card
        Assert.assertEquals(1, p2.getCards().size());

    }

    @Test
    public void testFirstCardRight(){
        Assert.assertEquals(2, p1.getCards().size());
        Assert.assertEquals(false,actions.challenge(CardType.ASSASSIN, p1, p2));
        //p1 lost one card
        Assert.assertEquals(1, p1.getCards().size());
    }

    /
    @Test
    public void testSecondCardRight(){
        Assert.assertEquals(2, p1.getCards().size());
        Assert.assertEquals(false,actions.challenge(CardType.CONTESSA, p1, p2));
        //p1 lost one card
        Assert.assertEquals(1, p1.getCards().size());
    }**/
}
