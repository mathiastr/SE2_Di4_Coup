package com.example.coup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.LooperMode;

@RunWith(RobolectricTestRunner.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class TestSplashActivity {

    private SplashActivity splash;

    @Test
    public void testSplash(){

        splash= Robolectric.buildActivity(SplashActivity.class).create().get();
    }

}
