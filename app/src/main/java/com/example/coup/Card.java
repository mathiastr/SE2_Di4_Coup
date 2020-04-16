package com.example.coup;

public class Card {
    private boolean isRevealed;
    private CardType typeOfCard;
    private static int cardID=1;

    public Card(CardType typeOfCard){
        this.typeOfCard=typeOfCard;
        cardID++;
    }

    public boolean isCardRevealed(){
        return isRevealed;
    }
    public CardType getTypeOfCard(){
        return typeOfCard;
    }
    public void revealCard(){
        isRevealed=true;
    }
    public void hideCard(){
        isRevealed=false;
    }

}
