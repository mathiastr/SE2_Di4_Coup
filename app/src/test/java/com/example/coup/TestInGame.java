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
    public void testInitializeOpponents(){

        List<String> enemies = new LinkedList<>();
        enemies.add("player2");
        enemies.add("player3");
        enemies.add("player4");

        inGame.initializeOpponents(enemies);

    }

    @Test
    public void testSelectPlayer(){


        inGame.selectPlayer("steal");

    }
    @Test
    public void testChallengeplayer(){

        inGame.challengePlayer("player2", "tax");

    }
    @Test
    public void testChallengeconfirmation(){

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



    @Test
    public void testExchangeTwoCards(){
        List<Card> cards = new LinkedList<>();

        cards.add(new Card(CardType.CONTESSA));
        cards.add(new Card(CardType.AMBASSADOR));

        inGame.player.setCards(cards);

        ArrayList<Card> cardstochoose = new ArrayList<>();

        cardstochoose.add(new Card(CardType.CAPTAIN));
        cardstochoose.add(new Card(CardType.DUKE));

        inGame.cardsToChoose=cardstochoose;

        inGame.showCardsToExchange();

        inGame.ivImageC3.performClick();
        inGame.ivImageC4.performClick();

        inGame.chooseCards.performClick();

        Assert.assertEquals(CardType.CAPTAIN, inGame.player.getCards().get(0).getTypeOfCard());
        Assert.assertEquals(CardType.DUKE, inGame.player.getCards().get(1).getTypeOfCard());


    }

    @Test
    public void testExchangeLeftCardRemoved(){

        inGame.leftCardRemoved = true;

        List<Card> cards = new LinkedList<>();

        cards.add(new Card(CardType.CONTESSA));

        inGame.player.setCards(cards);

        ArrayList<Card> cardstochoose = new ArrayList<>();

        cardstochoose.add(new Card(CardType.CAPTAIN));
        cardstochoose.add(new Card(CardType.DUKE));

        inGame.cardsToChoose=cardstochoose;

        inGame.showCardsToExchange();

        inGame.ivImageC3.performClick();
        inGame.ivImageC4.performClick();

        Assert.assertEquals(CardType.CAPTAIN, inGame.player.getCards().get(0).getTypeOfCard());



    }

    @Test
    public void testExchangeRightCardRemoved(){

        inGame.rightCardRemoved = true;

        List<Card> cards = new LinkedList<>();

        cards.add(new Card(CardType.CONTESSA));

        inGame.player.setCards(cards);

        ArrayList<Card> cardstochoose = new ArrayList<>();

        cardstochoose.add(new Card(CardType.CAPTAIN));
        cardstochoose.add(new Card(CardType.DUKE));

        inGame.cardsToChoose=cardstochoose;

        inGame.showCardsToExchange();

        inGame.ivImageC3.performClick();
        inGame.ivImageC4.performClick();

        Assert.assertEquals(CardType.CAPTAIN, inGame.player.getCards().get(0).getTypeOfCard());



    }


    @Test
    public void testCardNameToString(){
        Card c1 = new Card(CardType.AMBASSADOR);
        Assert.assertEquals("ambassador", inGame.getCardNameAsString(c1));
        Card c2 = new Card(CardType.DUKE);
        Assert.assertEquals("duke", inGame.getCardNameAsString(c2));
        Card c3 = new Card(CardType.CAPTAIN);
        Assert.assertEquals("captain", inGame.getCardNameAsString(c3));
        Card c4 = new Card(CardType.ASSASSIN);
        Assert.assertEquals("assassin", inGame.getCardNameAsString(c4));
        Card c5 = new Card(CardType.CONTESSA);
        Assert.assertEquals("contessa", inGame.getCardNameAsString(c5));

    }


    @Test
    public void testCardToString(){

        List<Card> cards = new LinkedList<>();

        cards.add(new Card(CardType.AMBASSADOR));
        cards.add(new Card(CardType.DUKE));
        cards.add(new Card(CardType.ASSASSIN));
        cards.add(new Card(CardType.CAPTAIN));
        cards.add(new Card(CardType.CONTESSA));

        List<String> cardnames = inGame.convertCardTypeToStringName(cards);

        Assert.assertEquals("ambassador", cardnames.get(0));
        Assert.assertEquals("duke", cardnames.get(1));
        Assert.assertEquals("assassin", cardnames.get(2));
        Assert.assertEquals("captain", cardnames.get(3));
        Assert.assertEquals("contessa", cardnames.get(4));



    }

    @Test
    public void testStringToCard(){

        List<String> cardnames = new LinkedList<>();

        cardnames.add("ambassador");
        cardnames.add("duke");
        cardnames.add("assassin");
        cardnames.add("captain");
        cardnames.add("contessa");

        List<Card> cards = inGame.convertStringNameToCardType(cardnames);

        Assert.assertEquals(CardType.AMBASSADOR, cards.get(0).getTypeOfCard());
        Assert.assertEquals(CardType.DUKE, cards.get(1).getTypeOfCard());
        Assert.assertEquals(CardType.ASSASSIN, cards.get(2).getTypeOfCard());
        Assert.assertEquals(CardType.CAPTAIN, cards.get(3).getTypeOfCard());
        Assert.assertEquals(CardType.CONTESSA, cards.get(4).getTypeOfCard());

    }


    @Test
    public void testNext(){


        inGame.next.callOnClick();

        Assert.assertEquals(false, inGame.next.isEnabled());

    }

    @Test
    public void testIncome(){



        inGame.incomeButton.callOnClick();


        Assert.assertEquals("Your coins: 3", inGame.coins.getText().toString());

    }

    @Test
    public void testForeignAid(){



        inGame.foreignAidButton.callOnClick();


        Assert.assertEquals("Your coins: 4" , inGame.coins.getText().toString());

    }

    @Test
    public void testTax(){



        inGame.taxButton.callOnClick();


        Assert.assertEquals("Your coins: 5" , inGame.coins.getText().toString());

    }

    @Test
    public void testImageAtStart1(){

        List<Card> cards = new LinkedList<>();

        cards.add(new Card(CardType.AMBASSADOR));
        cards.add(new Card(CardType.DUKE));

        inGame.player.setCards(cards);

        inGame.settingCardImagesAtStartOfGame();

        Assert.assertEquals(CardType.AMBASSADOR, inGame.player.getCards().get(0).getTypeOfCard());
        Assert.assertEquals(CardType.DUKE, inGame.player.getCards().get(1).getTypeOfCard());


    }

    @Test
    public void testImageAtStart2(){

        List<Card> cards = new LinkedList<>();

        cards.add(new Card(CardType.ASSASSIN));
        cards.add(new Card(CardType.CAPTAIN));

        inGame.player.setCards(cards);

        inGame.settingCardImagesAtStartOfGame();

        Assert.assertEquals(CardType.ASSASSIN, inGame.player.getCards().get(0).getTypeOfCard());
        Assert.assertEquals(CardType.CAPTAIN, inGame.player.getCards().get(1).getTypeOfCard());


    }

    @Test
    public void testImageAtStart3(){

        List<Card> cards = new LinkedList<>();

        cards.add(new Card(CardType.CONTESSA));
        cards.add(new Card(CardType.AMBASSADOR));

        inGame.player.setCards(cards);

        inGame.settingCardImagesAtStartOfGame();

        Assert.assertEquals(CardType.CONTESSA, inGame.player.getCards().get(0).getTypeOfCard());
        Assert.assertEquals(CardType.AMBASSADOR, inGame.player.getCards().get(1).getTypeOfCard());


    }


    @Test
    public void testBlockAction(){
        inGame.blockAction("player2","steal");

        Assert.assertEquals("You blocked player2 stealing from you", inGame.textView.getText().toString());

        inGame.blockAction("player2","assassinate");

        Assert.assertEquals("You blocked player2 assassinating you", inGame.textView.getText().toString());
    }

    @Test
    public void testTurn(){
        List<String> enemies = new LinkedList<>();
        enemies.add("player2");
        enemies.add("player3");
        enemies.add("player4");

        inGame.initializeOpponents(enemies);

        inGame.name="player1";

        String msg = "turn";
        String[] split = msg.split(" ");



        inGame.handleMessage(msg,  split);

        Assert.assertEquals("Your turn", inGame.textView.getText().toString());

    }

    @Test
    public void testEnemyIncome(){
        List<String> enemies = new LinkedList<>();
        enemies.add("player2");
        enemies.add("player3");
        enemies.add("player4");

        inGame.initializeOpponents(enemies);

        inGame.name="player1";

        String msg = "income player2";
        String[] split = msg.split(" ");

        inGame.handleMessage(msg,  split);

        Assert.assertEquals("3", inGame.tvOpp1coins.getText().toString());


    }

    @Test
    public void testEnemyFA(){
        List<String> enemies = new LinkedList<>();
        enemies.add("player2");
        enemies.add("player3");
        enemies.add("player4");

        inGame.initializeOpponents(enemies);

        inGame.name="player1";

        String msg = "foreignaid player2";
        String[] split = msg.split(" ");

        inGame.handleMessage(msg,  split);

        Assert.assertEquals("4", inGame.tvOpp1coins.getText().toString());


    }

    @Test
    public void testEnemyTax(){
        List<String> enemies = new LinkedList<>();
        enemies.add("player2");
        enemies.add("player3");
        enemies.add("player4");

        inGame.initializeOpponents(enemies);

        inGame.name="player1";

        String msg = "tax player2";
        String[] split = msg.split(" ");

        inGame.handleMessage(msg,  split);

        Assert.assertEquals("5", inGame.tvOpp1coins.getText().toString());


    }

    @Test
    public void testEnemyBlock1(){
        List<String> enemies = new LinkedList<>();
        enemies.add("player2");
        enemies.add("player3");
        enemies.add("player4");

        inGame.initializeOpponents(enemies);

        inGame.name="player1";

        String msg = "block player2 player1 steal";
        String[] split = msg.split(" ");

        inGame.handleMessage(msg,  split);

        Assert.assertEquals("player2 blocked your steal", inGame.textView.getText().toString());


    }

    @Test
    public void testEnemyBlock2(){
        List<String> enemies = new LinkedList<>();
        enemies.add("player2");
        enemies.add("player3");
        enemies.add("player4");

        inGame.initializeOpponents(enemies);

        inGame.name="player1";

        String msg = "block player2 player3 steal";
        String[] split = msg.split(" ");

        inGame.handleMessage(msg,  split);

        Assert.assertEquals("player2 blocked player3's steal", inGame.textView.getText().toString());


    }

    @Test
    public void testFAButton(){
        inGame.foreignAidButton.callOnClick();
    }

    @Test
    public void faBlocked1(){

        inGame.name="player1";

        String msg = "bfa player2 player1";
        String[] split = msg.split(" ");

        inGame.handleMessage(msg,  split);

        Assert.assertEquals("player2 blocked foreign aid on you", inGame.textView.getText().toString());

    }

    @Test
    public void faBlocked2(){

        inGame.name="player1";

        String msg = "bfa player2 player3";
        String[] split = msg.split(" ");

        inGame.handleMessage(msg,  split);

        Assert.assertEquals("player2 blocked foreign aid on player3", inGame.textView.getText().toString());

    }



    @Test
    public void testNextPlayer(){
        List<String> enemies = new LinkedList<>();
        enemies.add("player2");
        enemies.add("player3");
        enemies.add("player4");

        inGame.initializeOpponents(enemies);

        inGame.name="player1";

        String msg = "nextplayer player2";
        String[] split = msg.split(" ");

        inGame.handleMessage(msg,  split);

        Assert.assertEquals("player2's turn", inGame.textView.getText().toString());
    }


    @Test
    public void testLostGame1(){

        List<String> enemies = new LinkedList<>();
        enemies.add("player2");
        enemies.add("player3");
        enemies.add("player4");


        inGame.initializeOpponents(enemies);

        String msg = "lostgame player2";
        String[] split = msg.split(" ");

        inGame.handleMessage(msg,  split);

        Assert.assertEquals("player2 lost the game", inGame.textView.getText().toString());



    }

    @Test
    public void testLostGame2(){

        List<String> enemies = new LinkedList<>();
        enemies.add("player2");
        enemies.add("player3");
        enemies.add("player4");


        inGame.initializeOpponents(enemies);

        String msg = "lostgame player3";
        String[] split = msg.split(" ");

        inGame.handleMessage(msg,  split);

        Assert.assertEquals("player3 lost the game", inGame.textView.getText().toString());



    }

    @Test
    public void testLostGame3(){

        List<String> enemies = new LinkedList<>();
        enemies.add("player2");
        enemies.add("player3");
        enemies.add("player4");


        inGame.initializeOpponents(enemies);

        String msg = "lostgame player4";
        String[] split = msg.split(" ");

        inGame.handleMessage(msg,  split);

        Assert.assertEquals("player4 lost the game", inGame.textView.getText().toString());



    }




    @Test
    public void testChallengedOnTax(){

        List<Card> cards =new LinkedList<>();
        cards.add(new Card(CardType.DUKE));
        cards.add(new Card(CardType.AMBASSADOR));

        Player p = new Player();
        p.setName("player1");
        p.setCards(cards);

        inGame.player=p;

        String msg = "challenge player2 player1 tax";
        String[] split = msg.split(" ");

        inGame.handleMessage(msg,  split);

        Assert.assertEquals("duke", inGame.cardNameToShow);


    }

    @Test
    public void testChallengedOnBFA(){

        List<Card> cards =new LinkedList<>();
        cards.add(new Card(CardType.DUKE));
        cards.add(new Card(CardType.AMBASSADOR));

        Player p = new Player();
        p.setName("player1");
        p.setCards(cards);

        inGame.player=p;

        String msg = "challenge player2 player1 bfa";
        String[] split = msg.split(" ");

        inGame.handleMessage(msg,  split);

        Assert.assertEquals("duke", inGame.cardNameToShow);


    }

    @Test
    public void testChallengedOnSteal(){

        List<Card> cards =new LinkedList<>();
        cards.add(new Card(CardType.DUKE));
        cards.add(new Card(CardType.CAPTAIN));

        Player p = new Player();
        p.setName("player1");
        p.setCards(cards);

        inGame.player=p;

        String msg = "challenge player2 player1 steal";
        String[] split = msg.split(" ");

        inGame.handleMessage(msg,  split);

        Assert.assertEquals("captain", inGame.cardNameToShow);


    }

    @Test
    public void testChallengedOnAssassinate(){

        List<Card> cards =new LinkedList<>();
        cards.add(new Card(CardType.ASSASSIN));
        cards.add(new Card(CardType.CAPTAIN));

        Player p = new Player();
        p.setName("player1");
        p.setCards(cards);

        inGame.player=p;

        String msg = "challenge player2 player1 assassinate";
        String[] split = msg.split(" ");

        inGame.handleMessage(msg,  split);

        Assert.assertEquals("assassin", inGame.cardNameToShow);


    }

    @Test
    public void testChallengedOnExchange(){

        List<Card> cards =new LinkedList<>();
        cards.add(new Card(CardType.AMBASSADOR));
        cards.add(new Card(CardType.CAPTAIN));

        Player p = new Player();
        p.setName("player1");
        p.setCards(cards);

        inGame.player=p;

        String msg = "challenge player2 player1 exchange";
        String[] split = msg.split(" ");

        inGame.handleMessage(msg,  split);

        Assert.assertEquals("ambassador", inGame.cardNameToShow);


    }

    @Test
    public void testChallengedOtherPlayer(){

        List<Card> cards =new LinkedList<>();
        cards.add(new Card(CardType.ASSASSIN));
        cards.add(new Card(CardType.CAPTAIN));

        Player p = new Player();
        p.setName("player1");
        p.setCards(cards);

        inGame.player=p;

        String msg = "challenge player2 player3 assassinate";
        String[] split = msg.split(" ");

        inGame.handleMessage(msg,  split);

        Assert.assertEquals("player2 challenged player3", inGame.textView.getText().toString());


    }

    @Test
    public void testChallengeNotPossible(){

        List<Card> cards =new LinkedList<>();
        cards.add(new Card(CardType.ASSASSIN));
        cards.add(new Card(CardType.CAPTAIN));

        Player p = new Player();
        p.setName("player1");
        p.setCards(cards);

        inGame.player=p;

        String msg = "challenge player2 player3 income";
        String[] split = msg.split(" ");

        inGame.handleMessage(msg,  split);


    }


    @Test
    public void testStealFromPlayer(){
        List<String> enemies = new LinkedList<>();
        enemies.add("player2");
        enemies.add("player3");
        enemies.add("player4");

        inGame.initializeOpponents(enemies);

        inGame.name="player1";

        Player p = new Player();
        p.setName("player2");

        inGame.attackedPlayer=p;

        String msg = "steal player1 player2";
        String[] split = msg.split(" ");

        inGame.handleMessage(msg,  split);

        Assert.assertEquals("You stole from player2", inGame.textView.getText().toString());


    }

    @Test
    public void testGetStolenByPlayer(){
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

        String msg = "steal player2 player1";
        String[] split = msg.split(" ");

        inGame.handleMessage(msg,  split);

        Assert.assertEquals("player2 used steal on you", inGame.textView.getText().toString());


    }

    @Test
    public void testOtherPlayerSteal(){
        List<String> enemies = new LinkedList<>();
        enemies.add("player2");
        enemies.add("player3");
        enemies.add("player4");

        inGame.initializeOpponents(enemies);

        inGame.name="player1";

        Player p = new Player();
        p.setName("player2");

        inGame.attackedPlayer=p;

        String msg = "steal player2 player3";
        String[] split = msg.split(" ");

        inGame.handleMessage(msg,  split);

        Assert.assertEquals("player2 used steal on player3", inGame.textView.getText().toString());


    }

    @Test
    public void testChallengeButton(){
        inGame.challenge.callOnClick();
    }

    @Test
    public void testsuspectCheaterright(){
        List<String> enemies = new LinkedList<>();
        enemies.add("player2");
        enemies.add("player3");
        enemies.add("player4");

        inGame.initializeOpponents(enemies);

        inGame.name="player1";

        Player p = new Player();
        p.setName("player2");
        p.setCheated(false);

        inGame.attackedPlayer=p;

        String msg = "sRight player1 player2";
        String[] split = msg.split(" ");

        inGame.handleMessage(msg,  split);

        Assert.assertEquals("You caught player2 cheating", inGame.textView.getText().toString());

    }

    @Test
    public void testsuspectCheaterwrong(){
        List<String> enemies = new LinkedList<>();
        enemies.add("player2");
        enemies.add("player3");
        enemies.add("player4");

        inGame.initializeOpponents(enemies);

        inGame.name="player1";

        Player p = new Player();
        p.setName("player2");
        p.setCheated(false);

        inGame.attackedPlayer=p;

        String msg = "sWrong player1 player2";
        String[] split = msg.split(" ");

        inGame.handleMessage(msg,  split);

        Assert.assertEquals("You incorrectly suspected player2 cheating", inGame.textView.getText().toString());

    }

    @Test
    public void testsuspectCheaterrightOtherPlayers(){
        List<String> enemies = new LinkedList<>();
        enemies.add("player2");
        enemies.add("player3");
        enemies.add("player4");

        inGame.initializeOpponents(enemies);

        inGame.name="player1";

        Player p = new Player();
        p.setName("player2");
        p.setCheated(false);

        inGame.attackedPlayer=p;

        String msg = "sRight player3 player2";
        String[] split = msg.split(" ");

        inGame.handleMessage(msg,  split);

        Assert.assertEquals("player3 caught player2 cheating", inGame.textView.getText().toString());

    }

    @Test
    public void testsuspectCheaterwrongOtherPlayers(){
        List<String> enemies = new LinkedList<>();
        enemies.add("player2");
        enemies.add("player3");
        enemies.add("player4");

        inGame.initializeOpponents(enemies);

        inGame.name="player1";

        Player p = new Player();
        p.setName("player2");
        p.setCheated(false);

        inGame.attackedPlayer=p;

        String msg = "sWrong player3 player2";
        String[] split = msg.split(" ");

        inGame.handleMessage(msg,  split);

        Assert.assertEquals("player3 incorrectly suspected player2 cheating", inGame.textView.getText().toString());

    }

    @Test
    public void testgetSuspectedRight(){
        List<String> enemies = new LinkedList<>();
        enemies.add("player2");
        enemies.add("player3");
        enemies.add("player4");

        inGame.initializeOpponents(enemies);

        inGame.name="player1";


        Player p1 = new Player();
        p1.setName("player1");
        p1.setCheated(true);


        inGame.player=p1;

        Player p2 = new Player();
        p2.setName("player2");

        inGame.attackedPlayer=p1;

        String msg = "sCheat player2 player1";
        String[] split = msg.split(" ");

        inGame.handleMessage(msg,  split);

        Assert.assertEquals("You got caught cheating", inGame.textView.getText().toString());


    }

    @Test
    public void testgetSuspectedWrong(){
        List<String> enemies = new LinkedList<>();
        enemies.add("player2");
        enemies.add("player3");
        enemies.add("player4");

        inGame.initializeOpponents(enemies);

        inGame.name="player1";


        Player p1 = new Player();
        p1.setName("player1");

        inGame.player=p1;

        Player p2 = new Player();
        p2.setName("player2");

        inGame.attackedPlayer=p1;

        String msg = "sCheat player2 player1";
        String[] split = msg.split(" ");

        inGame.handleMessage(msg,  split);

        Assert.assertEquals("player2 suspected you cheating", inGame.textView.getText().toString());


    }

    @Test
    public void testExchangeButton(){

        inGame.exchangeButton.performClick();
    }

    @Test
    public void testSetUpTurn(){

        List<String> enemies = new LinkedList<>();
        enemies.add("player2");
        enemies.add("player3");
        enemies.add("player4");

        inGame.opponents=enemies;

        inGame.initializeOpponents(enemies);

        Player p = new Player();
        p.setName("player1");

        List<Card> cards = new LinkedList<>();
        cards.add(new Card(CardType.AMBASSADOR));
        cards.add(new Card(CardType.AMBASSADOR));

        p.setCards(cards);

        inGame.player=p;
        inGame.name=p.getName();


        inGame.setUp("turn");
    }

    @Test
    public void testSetUpWait(){


    }






    @After
    public void tearDown(){

        inGame=null;
        game=null;
    }

}
