package com.example.coup.ActionTest;

import com.example.coup.Action.Income;
import com.example.coup.Player;
import org.junit.Assert;
import org.junit.Test;

public class TestIncome {

    @Test
    public void simpleTestIncome(){
        Player p1 = new Player("P1");
        Income i1 = new Income(p1);

        i1.playAction();
        Assert.assertEquals(3,p1.getCoins());
    }
}
