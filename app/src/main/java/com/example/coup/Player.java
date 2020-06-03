package com.example.coup;

import java.util.ArrayList;
import java.util.List;

// Organisiert die Übersicht der einzelnen Spieler und deren Verfügbare Aktionen

public class Player {
    private String name;
    public  int coins=2;
    public  int influence=2;
    private List<Card> cards;
    private boolean inGame=true;
    private static int playerID= 1;
    public boolean cheated = false;

    private boolean canBlockAssassination=false;
    private boolean canBlockForeignAid=false;
    private boolean canBlockSteal=false;



    public Player(String name){
        this.name=name;
        playerID++;
        cards = new ArrayList<Card>();
    }


    public boolean hasCard(CardType type) {

        if (cards.size() == 2) {
            if (cards.get(0).getTypeOfCard().equals(type))
                return true;
            else if (cards.get(1).getTypeOfCard().equals(type))
                return true;
        } else {
            if (cards.get(0).getTypeOfCard().equals(type))
                return true;
        }
        return false;
    }

    public void revealCard(int cardIndex){
        //TODO show card for a view sec and then delete it
        //wait(1000); or show message what card
        cards.remove(cardIndex);
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public void addCard(Card c){
        this.cards.add(c);
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

    public boolean getCheated(){return this.cheated;}
    public void setCheated(boolean cheated){
        this.cheated = cheated;
    }


    //TODO influence sind die Anzahl der Karten
    public int getInfluence() {
        return this.getCards().size();
    }


    //lose Influence heißt der Spieler verliert eine Karte


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

    public boolean getInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }


   
}
