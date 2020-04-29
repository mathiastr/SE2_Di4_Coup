package com.example.coup.Action;

import com.example.coup.Card;
import com.example.coup.Game;
import com.example.coup.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Exchange extends Action {

    boolean otherPlayerWantsToChallenge =false;
    Player playerChallenging;
    Game game;
    List<Card> cardsToChoose= new LinkedList<>();
    //user choose from cardsToChoose.
    //for example index 0,2
    int indexOfFirstChoosenCard=0;
    int indexOfSecondChoosenCard=2;

    //TODO
        //get 2 Random cards from the deck
        //player can choose one or no card (from the new cards)
        //if one card is choosen, choose a card from players own cards to exchange
        //every player can challange (needed Card is Ambassador)
        public Exchange(Player playerDoingAction, Game game){
            super(playerDoingAction);
            this.playerDoingAction=playerDoingAction;
            this.game=game;

        }
    public void playAction(){
            if(otherPlayerWantsToChallenge==true){
                // Needs Implementation

//                StopExchange se1= new StopExchange(playerChallenging,this);
//                boolean result = se1.playReaction();
//
//                if(result==true) {
//                    playerDoingAction.loseCard();
//                }else{
//                    playerChallenging.loseCard();
//                    //PlayerdoingAction draws another Card
//                }

            } else {
                //playerCards= playerDoingAction.getCards();
                List<Card> gameCards = game.getCards();
                List<Card> playerHand= playerDoingAction.getCards();
                if(playerDoingAction.getCards().size()==2){
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

                chooseCards(indexOfFirstChoosenCard,indexOfSecondChoosenCard);
            }
        }

        public List<Card> chooseCards(int index1, int index2){
            //needs User Input: which specific cards we want to choose and what to return tp the deck;

           List<Card> list = new ArrayList<>();

            if(playerDoingAction.getCards().size()==2) {
                list.add(cardsToChoose.get(index1));
                list.add(cardsToChoose.get(index2));

                cardsToChoose.remove(index1);
                cardsToChoose.remove(index2);

                game.returnCardtoDeck(cardsToChoose.get(0));
                game.returnCardtoDeck(cardsToChoose.get(1));
                game.shuffleCards();
            }


            playerDoingAction.setCards(list);

        return list;}

    public int getIndexOfFirstChoosenCard() {
        return indexOfFirstChoosenCard;
    }

    public void setIndexOfFirstChoosenCard(int indexOfFirstChoosenCard) {
        this.indexOfFirstChoosenCard = indexOfFirstChoosenCard;
    }

    public int getIndexOfSecondChoosenCard() {
        return indexOfSecondChoosenCard;
    }

    public void setIndexOfSecondChoosenCard(int indexOfSecondChoosenCard) {
        this.indexOfSecondChoosenCard = indexOfSecondChoosenCard;
    }

    public List<Card> getCardsToChoose() {
        return cardsToChoose;
    }
}
