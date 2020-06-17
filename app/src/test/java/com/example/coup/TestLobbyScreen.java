package com.example.coup;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.LooperMode;

@RunWith(RobolectricTestRunner.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class TestLobbyScreen {

    private LobbyScreen screen;

    @Before
    public void setUp(){

        screen = Robolectric.buildActivity(LobbyScreen.class).create().get();

    }

    @Test
    public void gotToURL(){

        screen.goToURL("https://github.com/mathiastr/SE2_Di4_Coup");
    }

    @After
    public void tearDown(){

        screen=null;
    }


}
