package com.example.coup;

import org.apache.tools.ant.Main;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.LooperMode;

@RunWith(RobolectricTestRunner.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class TestStartMenu {

    private MainActivity mainActivity;

    @Before
    public void TestCreate(){

        mainActivity = Robolectric.buildActivity(MainActivity.class).create().get();


    }

    @Test
    public void testOpenLobby(){

        mainActivity.openLobby();
    }

    @Test
    public void testOpenAbout(){

        mainActivity.openAbout();
    }

    @Test
    public void testQuitWarning(){

        mainActivity.quitWarning();
    }
    @Test
    public void testGoToWarning(){

        mainActivity.goToURL("https://github.com/mathiastr/SE2_Di4_Coup");
    }

    @After
    public void tearDown(){

        mainActivity=null;
    }
}
