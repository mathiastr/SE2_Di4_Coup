package com.example.coup.ActionTest;

import com.example.coup.AllActions;
import com.example.coup.Card;
import com.example.coup.Game;
import com.example.coup.Player;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestActions {
    Game g;
    Player p1,p2,p3,p4;
    AllActions actions;
    List<Player> players;
    @Before
    public void before(){
        players=new ArrayList<>();
        p1= new Player();
        p2= new Player();
        p3= new Player();
        p4= new Player();

        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);

        g=new Game(players);
        g.dealStartOfGame();
        actions=new AllActions(g);
    }
    @Test
    public void testIncome(){
        actions.income(p1);
        Assert.assertEquals(3,p1.getCoins());
    }
    @Test
    public void testForeignAid(){
        actions.foreignAid(p1);
        Assert.assertEquals(4,p1.getCoins());
    }
    @Test
    public void testCoupPlayerLoses7Coins(){
        p1.setCoins(7);
        actions.coup(p1,p2);
        Assert.assertEquals(0,p1.getCoins());
    }
    @Test
    public void testCoupTargetPlayerLosesInfluence(){
        p1.setCoins(7);
        actions.coup(p1,p2);
        Assert.assertEquals(1,p2.getCards().size());
    }
    @Test
    public void testStealPlayerGets2Coins(){
        actions.steal(p1,p2);
        Assert.assertEquals(4,p1.getCoins());
    }
    @Test
    public void testStealTargetPlayerLoses2Coins(){
        actions.steal(p1,p2);
        Assert.assertEquals(0,p2.getCoins());
    }
    @Test
    public void testTax(){
        actions.tax(p1);
        Assert.assertEquals(5,p1.getCoins());
    }
    @Test
    public void testAssassinatePlayerLoses3Coins(){
        p1.setCoins(3);
        actions.assassinate(p1,p2);
        Assert.assertEquals(0,p1.getCoins());
    }
    @Test
    public void testAssassinateTargetLosesInfluence(){
        p1.setCoins(3);
        actions.assassinate(p1,p2);
        Assert.assertEquals(1,p2.getCards().size());
    }
    @Test
    public void testExchange(){
        ArrayList<Card> copyOfCards= (ArrayList<Card>) p1.getCards();
//        for(Card c: copyOfCards){
//            System.out.println(c.getTypeOfCard());
//        }
//        System.out.println("after");
        actions.exchange(p1);
//        for(Card c: p1.getCards()){
//            System.out.println(c.getTypeOfCard());
//        }

        Assert.assertNotEquals(copyOfCards,p1.getCards());

    }
    @After
    public void after(){
        p1=null;
        p2=null;
        p3=null;
        p4=null;
        g=null;
        players=null;
        actions=null;
    }
}
