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


   






    @After
    public void tearDown(){

        inGame=null;
        game=null;
    }

}
