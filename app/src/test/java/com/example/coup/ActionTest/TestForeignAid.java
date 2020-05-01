package com.example.coup.ActionTest;

import com.example.coup.Player;
import org.junit.Assert;
import org.junit.Test;

public class TestForeignAid {

    @Test
    public void simpleTestForeignAid(){
        Player p1 = new Player("P1");
        ForeignAid fa1 = new ForeignAid(p1);

        fa1.playAction();
        Assert.assertEquals(4
                ,p1.getCoins());
    }
}
