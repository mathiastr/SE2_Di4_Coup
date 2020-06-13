package com.example.coup;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.LinkedList;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class RoboElectricTest {



    private InGame inGame ;


    @Before
    public void setUp(){
        inGame= Robolectric.buildActivity(InGame.class).create().get();
    }



    @org.junit.Test
    public void test1(){

        List<String> enemies = new LinkedList<>();
        enemies.add("player2");
        enemies.add("player3");
        enemies.add("player4");

        inGame.initializeOpponents(enemies);

    }

    @org.junit.Test
    public void test2(){


        inGame.selectPlayer("steal");

    }
    @org.junit.Test
    public void test3(){

        inGame.challengePlayer("player2", "tax");

    }
    @org.junit.Test
    public void test4(){

        inGame.challengeConfirmation();



    }

}
