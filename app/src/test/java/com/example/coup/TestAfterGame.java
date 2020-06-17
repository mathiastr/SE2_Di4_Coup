package com.example.coup;


import android.os.Bundle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.LooperMode;

@RunWith(RobolectricTestRunner.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class TestAfterGame {

    private AfterGame afterGame;

    @Test
    public void TestCreate(){

        afterGame = Robolectric.buildActivity(AfterGame.class).create().get();


    }

    @Test
    public void TestGoToMenu(){

        afterGame = Robolectric.buildActivity(AfterGame.class).create().get();
        afterGame.goToMenu();


    }



}
