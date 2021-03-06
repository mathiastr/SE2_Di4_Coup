import com.example.coup.Card;
import com.example.coup.CardType;
import com.example.coup.Game;
import com.example.coup.Player;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestGame {

    Game g;
    Player p1,p2,p3,p4;
    List<Player> players;

    @Before
    public void before(){
        players=new ArrayList<>();
        p1= new Player();
        p2= new Player();
        p3= new Player();
        p4= new Player();

        p1.setName("P1");
        p2.setName("P2");
        p3.setName("P3");
        p4.setName("P4");

        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);

        g=new Game(players);
        g.dealStartOfGame();

    }

    @Test
    public void testsizeofDeck(){
        Assert.assertEquals(7,g.sizeOfDeck());
    }

    @Test
    public void testgetplayers(){
        Assert.assertEquals(players,g.getPlayers());
    }

    @Test
    public void testupdatePlayerCoins1(){
        g.updatePlayerCoins("P1", 5);
        Assert.assertEquals(7,p1.getCoins());
    }

    @Test
    public void testupdatePlayerCoins2(){
        Assert.assertEquals(p1,g.updatePlayerCoins("P1", 0));
    }

    @Test
    public void testupdatePlayerCoins3(){
        Assert.assertEquals(null,g.updatePlayerCoins("P5", 5));
    }

    @Test
    public void testgetPlayerByName1(){
        Assert.assertEquals(p2 ,g.getPlayerByName("P2"));
    }

    @Test
    public void testgetPlayerByName2(){
        Assert.assertEquals(null ,g.getPlayerByName("P7"));
    }

    @Test
    public void testremovePlayer(){
        g.removePlayer("P3");
        Assert.assertFalse(players.contains(p3));
    }

    @Test
    public void testpushCard(){
        Card c = new Card(CardType.ASSASSIN);
        g.pushCard(c);
        Assert.assertEquals(8,g.sizeOfDeck());
    }

    @Test
    public void testdealCard() {
        List<Card> TestCards = new ArrayList<>();
        TestCards.add(g.dealCard());
        TestCards.add(g.dealCard());
        Assert.assertEquals(2, TestCards.size());
        Assert.assertEquals(5, g.sizeOfDeck());

    }

    /**
     @Test
     public void testsetplayers(){
     Player p5,p6,p7,p8;
     List<Player> newplayers =new ArrayList<>();
     p5= new Player();
     p6= new Player();
     p7= new Player();
     p8= new Player();


     g.setPlayers(newplayers);
     Assert.assertEquals(newplayers,g.getPlayers());
     }

     @Test
     public void testtwoinGame1(){
     Assert.assertTrue(g.twoinGame());
     }

     **/

}
