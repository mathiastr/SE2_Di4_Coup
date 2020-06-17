package com.example.coup;


import android.content.Intent;
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
    public void TestOnWin(){

        Intent intent = new Intent();
        intent.putExtra("result", "win");
        afterGame = Robolectric.buildActivity(AfterGame.class,intent).create().get();

        afterGame.buttonMainMenu.performClick();

        afterGame=null;


    }

    @Test
    public void TestOnLose(){

        Intent intent = new Intent();
        intent.putExtra("result", "lose");
        afterGame = Robolectric.buildActivity(AfterGame.class,intent).create().get();

        afterGame.buttonMainMenu.performClick();

        afterGame=null;


    }


}
