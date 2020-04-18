package com.example.coup;

import java.util.List;
import java.util.Random;

public class Player {
    private String name;
    private int coins;
    private List<Card> cards;
    private boolean inGame=true;
    private static int playerID= 1;

    public Player(String name){
        this.name=name;
        playerID++;
        coins = 2;
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
    public void addCoins(int coinsToAdd) {this.coins += coinsToAdd; }
    public void setCards(Card card1, Card card2){
        cards.add(card1);
        cards.add(card2);
    }
    public void loseCard(Card card){
        cards.remove(card);
        if(cards.isEmpty()){
            inGame = false;
        }
    }


}
