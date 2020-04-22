package com.example.coup;

import java.util.List;
import java.util.Observable;

// Organisiert die Übersicht der einzelnen Spieler und deren Verfügbare Aktionen

public class Player  {
    private String name;
    public  int coins=2;
    public  int influence=2;
    private List<Card> cards;
    private boolean inGame=true;
    private static int playerID= 1;

    private boolean canBlockAssassination=false;
    private boolean canBlockForeignAid=false;
    private boolean canBlockSteal=false;


    public Player(String name){
        this.name=name;
        playerID++;
    }

    public int hasCard(CardType type){
        //return -1 when Player doesnt have that Cardtype
        //return 0 when index of that Card is 0 (first card)
        //return 1 when index of that Card is 1 (second card)
        if(cards.get(0).getTypeOfCard().equals(type)) return 0;
        else if(cards.get(1).getTypeOfCard().equals(type)) return 1;
        else return -1;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
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

    //TODO influence sind die Anzahl der Karten
    public int getInfluence() {
        return influence;
    }
    public void setInfluence(int influence) {
        this.influence = influence;
    }

    //lose Influence heißt der Spieler verliert eine Karte
    //public void loseInfluence(){
    //    this.influence--;
    //}

    public void loseCard(){
        int cardIndex = 0;
        if(cards.size()==2){
            //TODO let player choose which card to lose, cardIndex = 0 oder 1
            cards.remove(cardIndex);
        }
        else {
            cards.clear();
            inGame = false;
        }

    }

    //Umsetzung der gewählten Aktionen beim Spieler, durch Aufruf der Methoden in Action Klassen
    public void doIncomeAction(){
        coins++;
    }
    public void doForeignAidAction(){
        coins+=2;
    }
    public boolean ifCoupPossibleDoIt(){
        if(coins>=7){
            coins-=7;
            return true;
        }else {
            return false;
        }
    }
    public void doDukesAction(){
        coins+=3;
    }
    public boolean isAssassinsActionPossible(){
        if(coins>=3){
            coins-=3;
            return true;
        }else{
            return false;
        }
    }
    public void checkForReactions(){
        for(Card c:cards){
            if(c.getTypeOfCard()==CardType.CONTESSA){
                canBlockAssassination=true;
            }
            if(c.getTypeOfCard()==CardType.DUKE){
                canBlockForeignAid=true;
            }
            if(c.getTypeOfCard()==CardType.CAPTAIN||c.getTypeOfCard()==CardType.AMBASSADOR){
                canBlockSteal=true;
            }
        }
    }
    public boolean getCanBlockAssassination() {
        return canBlockAssassination;
    }
    public void setCanBlockAssassination(boolean canBlockAssassination) {
        this.canBlockAssassination = canBlockAssassination;
    }
    public boolean getCanBlockForeignAid() {
        return canBlockForeignAid;
    }
    public void setCanBlockForeignAid(boolean canBlockForeignAid) {
        this.canBlockForeignAid = canBlockForeignAid;
    }
    public boolean getCanBlockSteal() {
        return canBlockSteal;
    }
    public void setCanBlockSteal(boolean canBlockSteal) {
        this.canBlockSteal = canBlockSteal;
    }
}
