package com.example.coup;

public class AllActions {

    public void income(Player player){}
    public void foreignAid(Player player){}
    public void coup(Player player, Player againstPlayer){}

    public void assassinate(Player player, Player againstPlayer){}
    public void exchange(Player player){}
    public void steal(Player player, Player againstPlayer){}
    public void tax(Player player){}



    //Da Blocks nicht wirklich was tun, außer die vorherige Aktion verhinden, brauchen sie keine extra Methode

    //returns true when challenged player does not have the correct card
    public boolean challenge(CardType neededCardType, Player clickedChallenge, Player challenged){

        boolean firstCardIsNeeded = challenged.getCards().get(0).getTypeOfCard().equals(neededCardType);
        boolean secondCardIsNeeded = challenged.getCards().get(1).getTypeOfCard().equals(neededCardType);

        if(firstCardIsNeeded){
            challenged.getGame().pushCard(challenged.getCards().get(0));
            challenged.revealCard(0);
            challenged.addCard(challenged.getGame().dealCard());
            clickedChallenge.loseCard();
            return false;
        }
        else if(secondCardIsNeeded){
            challenged.getGame().pushCard(challenged.getCards().get(1));
            challenged.revealCard(1);
            challenged.addCard(challenged.getGame().dealCard());
            clickedChallenge.loseCard();
            return false;
        }
        else{
            challenged.loseCard();
            return true;
        }
    }
}
