package com.example.coup;

import android.os.Looper;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.LooperMode;
import org.robolectric.internal.IShadow;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowLooper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


@RunWith(RobolectricTestRunner.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class TestInGame {


    private InGame inGame ;

    private Game game;




    @Before
    public void setUp(){


        inGame= Robolectric.buildActivity(InGame.class).create().get();

        List<Player> players = new LinkedList<>();

        for(int i=0;i<3;i++){
            Player p = new Player();
            p.setName("player"+(i+1));
            players.add(p);
        }

        game=new Game(players);

        inGame.game=game;

        inGame.player=players.get(0);

        inGame.name="player1";



    }



    @Test
    public void test1(){

        List<String> enemies = new LinkedList<>();
        enemies.add("player2");
        enemies.add("player3");
        enemies.add("player4");

        inGame.initializeOpponents(enemies);

    }

    @Test
    public void test2(){


        inGame.selectPlayer("steal");

    }
    @Test
    public void test3(){

        inGame.challengePlayer("player2", "tax");

    }
    @Test
    public void test4(){

        inGame.challengeConfirmation();

    }

    @Test
    public void testCoupOnMainPlayer(){

        List<String> enemies = new LinkedList<>();
        enemies.add("player2");
        enemies.add("player3");
        enemies.add("player4");


        List<Card> cards =new LinkedList<>();
        cards.add(new Card(CardType.AMBASSADOR));
        cards.add(new Card(CardType.CAPTAIN));

        inGame.initializeOpponents(enemies);

        Player p = new Player();
        p.setName("player1");
        p.setCards(cards);


        inGame.player=p;

        String msg = "coup player2 player1";
        String[] split = msg.split(" ");

        inGame.handleMessage(msg,  split);

        Assert.assertEquals("Couped: Choose a card to lose", inGame.textView.getText().toString());


    }

    @Test
    public void testCoupOnOtherPlayer(){

        List<String> enemies = new LinkedList<>();
        enemies.add("player2");
        enemies.add("player3");
        enemies.add("player4");


        List<Card> cards =new LinkedList<>();
        cards.add(new Card(CardType.AMBASSADOR));
        cards.add(new Card(CardType.CAPTAIN));

        inGame.initializeOpponents(enemies);

        Player p = new Player();
        p.setName("player1");
        p.setCards(cards);


        inGame.player=p;

        String msg = "coup player2 player3";
        String[] split = msg.split(" ");

        inGame.handleMessage(msg,  split);

        Assert.assertEquals("player2 used coup on player3", inGame.textView.getText().toString());


    }

    @Test
    public void testCoupButton(){

        Player p = new Player();
        p.setName("player2");

        inGame.attackedPlayer=p;


        inGame.coupButton.performClick();

        inGame.doAction("coup");

        Assert.assertEquals("You did coup on player2", inGame.textView.getText().toString());

    }


    @Test
    public void looseLeftCard(){

        List<Card> cards = new LinkedList<>();

        cards.add(new Card(CardType.AMBASSADOR));
        cards.add(new Card(CardType.DUKE));

        inGame.player.setCards(cards);

        inGame.settingCardImagesAtStartOfGame();

        inGame.mainPlayerChoosesCardToLose("coup");

        inGame.ivImageC1.callOnClick();

        Assert.assertEquals(true, inGame.leftCardRemoved);

    }

    @Test
    public void looseRightCard(){

        List<Card> cards = new LinkedList<>();

        cards.add(new Card(CardType.AMBASSADOR));
        cards.add(new Card(CardType.DUKE));

        inGame.player.setCards(cards);

        inGame.settingCardImagesAtStartOfGame();

        inGame.mainPlayerChoosesCardToLose("assassinate");

        inGame.ivImageC2.callOnClick();

        Assert.assertEquals(true, inGame.rightCardRemoved);



    }

    @Test
    public void looseLastCard(){

        List<Card> cards = new LinkedList<>();

        cards.add(new Card(CardType.AMBASSADOR));
        cards.add(new Card(CardType.DUKE));

        inGame.player.setCards(cards);

        inGame.settingCardImagesAtStartOfGame();

        inGame.returnLastCard();

        Assert.assertEquals(CardType.AMBASSADOR, inGame.player.getCards().get(0).getTypeOfCard());



    }
    @Test
    public void testEnemyAssassinated(){
        List<String> enemies = new LinkedList<>();
        enemies.add("player2");
        enemies.add("player3");
        enemies.add("player4");

        inGame.initializeOpponents(enemies);

        inGame.name="player1";

        Player p = new Player();
        p.setName("player2");

        inGame.attackedPlayer=p;

        String msg = "assassinate player1 player2";
        String[] split = msg.split(" ");

        inGame.handleMessage(msg,  split);

        Assert.assertEquals("You used assassinate on player2", inGame.textView.getText().toString());


    }

    @Test
    public void testAssassinatedByEnemy(){

        List<String> enemies = new LinkedList<>();
        enemies.add("player2");
        enemies.add("player3");
        enemies.add("player4");

        inGame.initializeOpponents(enemies);

        inGame.name="player1";

        List<Card> cards =new LinkedList<>();
        cards.add(new Card(CardType.AMBASSADOR));
        cards.add(new Card(CardType.AMBASSADOR));

        Player p1 = new Player();
        p1.setName("player1");
        p1.setCards(cards);


        inGame.player=p1;

        Player p2 = new Player();
        p2.setName("player2");

        inGame.attackedPlayer=p1;

        String msg = "assassinate player2 player1";
        String[] split = msg.split(" ");

        inGame.handleMessage(msg,  split);

        Assert.assertEquals("Assassinated: Choose a card to lose", inGame.textView.getText().toString());



    }


    @Test
    public void testOtherPlayerAssassinated(){
        List<String> enemies = new LinkedList<>();
        enemies.add("player2");
        enemies.add("player3");
        enemies.add("player4");

        inGame.initializeOpponents(enemies);

        inGame.name="player1";

        Player p = new Player();
        p.setName("player2");

        inGame.attackedPlayer=p;

        String msg = "assassinate player3 player2";
        String[] split = msg.split(" ");

        inGame.handleMessage(msg,  split);

        Assert.assertEquals("player3 used assassinate on player2", inGame.textView.getText().toString());


    }


    @Test
    public void testAsssassinateNoCoins(){
        inGame.assassinateButton.performClick();

        Assert.assertEquals("You need 3 coins to use assassinate", inGame.textView.getText().toString());
    }

    @Test
    public void testAssassinate(){

        inGame.player.setCoins(3);

        Player attacked = new Player();
        attacked.setName("player2");

        inGame.attackedPlayer=attacked;

        inGame.assassinateButton.performClick();

        inGame.doAction("assassinate");

        Assert.assertEquals("Assassinating "+inGame.attackedPlayer.getName(), inGame.textView.getText().toString());

    }











    @After
    public void tearDown(){

        inGame=null;
        game=null;
    }

}
