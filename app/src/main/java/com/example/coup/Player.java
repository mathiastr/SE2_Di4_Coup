package com.example.coup;

import java.util.List;

public class Player {
    private String name;
    private int coins;
    private List<Card> cards;
    private boolean inGame=true;
    private static int playerID= 1;

    public Player(String name){
        this.name=name;
        playerID++;
    }
    public int getCoins(){
        return this.coins;
    }
    public String getName(){
        return this.name;
    }
    public void setCoins(int coins){
        this.coins=coins;
    }



}
