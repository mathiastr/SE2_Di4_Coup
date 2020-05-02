package com.example.coup;

import java.util.ArrayList;
import java.util.List;

public class AllActions {

    Game game;
    Player p; /*Needed for Exchange Action*/
    List<Card> cardsToChoose; /*Needed for Exchange Action*/
    public AllActions(Game thisGame){
        game = thisGame;
    }

    public void income(Player player){
        player.setCoins(player.getCoins()+1);
    }
    public void foreignAid(Player player){
        player.setCoins(player.getCoins()+2);
    }
    public void coup(Player player, Player againstPlayer){
        player.setCoins(player.getCoins()-7);
        againstPlayer.loseCard();
    }

    public void assassinate(Player player, Player againstPlayer){
        player.setCoins(player.getCoins()-3);
        againstPlayer.loseCard();
    }
    public void exchange(Player player){
        this.p=player;
        cardsToChoose = new ArrayList<>();
        List<Card> gameCards = game.getCards();
        List<Card> playerHand= player.getCards();
        if(player.getCards().size()==2){
            cardsToChoose.add(playerHand.get(0));
            cardsToChoose.add(playerHand.get(1));
        }else{
            cardsToChoose.add(playerHand.get(0));
        }
        game.shuffleCards();
//                cardsToChoose.add(gameCards.get(gameCards.size()-1));
//                cardsToChoose.add(gameCards.get(gameCards.size()-2));
        cardsToChoose.add(game.dealCard());
        cardsToChoose.add(game.dealCard());

    }


    public List<Card> chooseCards(int index1, int index2){
        //needs User Input: which specific cards we want to choose and what to return tp the deck;

        List<Card> list = new ArrayList<>();

        if(p.getCards().size()==2) {
            list.add(cardsToChoose.get(index1));
            list.add(cardsToChoose.get(index2));

            cardsToChoose.remove(index1);
            cardsToChoose.remove(index2);

            game.returnCardtoDeck(cardsToChoose.get(0));
            game.returnCardtoDeck(cardsToChoose.get(1));
            game.shuffleCards();
        }

        p.setCards(list);
        return list;
    }

    public void steal(Player player, Player againstPlayer){
        againstPlayer.setCoins(againstPlayer.getCoins()-2);
        player.setCoins(player.getCoins()+2);
    }

    public void tax(Player player){
        player.setCoins(player.getCoins()+3);
    }


    //Da Blocks nicht wirklich was tun, außer die vorherige Aktion verhinden, brauchen sie keine extra Methode

    //returns true when challenged player does not have the correct card
    public boolean challenge(CardType neededCardType, Player clickedChallenge, Player challenged){

        boolean firstCardIsNeeded = challenged.getCards().get(0).getTypeOfCard().equals(neededCardType);
        boolean secondCardIsNeeded = challenged.getCards().get(1).getTypeOfCard().equals(neededCardType);

        if(firstCardIsNeeded){
            game.pushCard(challenged.getCards().get(0));
            challenged.revealCard(0);
            challenged.addCard(game.dealCard());
            clickedChallenge.loseCard();
            return false;
        }
        else if(secondCardIsNeeded){
            game.pushCard(challenged.getCards().get(1));
            challenged.revealCard(1);
            challenged.addCard(game.dealCard());
            clickedChallenge.loseCard();
            return false;
        }
        else{
            challenged.loseCard();
            return true;
        }
    }
}
