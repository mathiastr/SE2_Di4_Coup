package com.example.coup.ActionTest;

import com.example.coup.Player;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

@RunWith(MockitoJUnitRunner.class)
public class TestStopForeignAid {

    StopForeignAid stopForeignAid;
    Player player;

    @Before
    public void before(){
        stopForeignAid = new StopForeignAid(player, null);
        player = new Player("Sarah");
    }

    //Test Player.startTimer
    @Test
    public void startTimer(){
        Assert.assertEquals(0, stopForeignAid.isClickedChallenge);
        player.startTimer(5,stopForeignAid);
        Assert.assertEquals(-1, stopForeignAid.isClickedChallenge);
    }


}
