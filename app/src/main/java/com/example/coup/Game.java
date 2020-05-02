package com.example.coup;

import com.example.coup.Action.Action;

import java.util.*;

public class Game {
    List<Player> players;
    List<Card> playingCards;
    LayoutManager manager = new LayoutManager();
    Stack<Card> cards;

    //Should we use Stack instead of List for Cards?
    public Game(List<Player> players){
        this.players = players;
        cards = new Stack<Card>();
        for(int i = 0; i < 3; i++){
            cards.push(new Card(CardType.CONTESSA));
            cards.push(new Card(CardType.DUKE));
            cards.push(new Card(CardType.CAPTAIN));
            cards.push(new Card(CardType.AMBASSADOR));
            cards.push(new Card(CardType.ASSASSIN));
        }


        //for each Player set 2 random Cards and remove these cards from cards (deck)
        shuffleCards();
        for (int i  = 0; i < cards.size(); i++){

            playingCards.add(dealCard());
            playingCards.add(dealCard());
            players.get(i).setCards(playingCards);

        }

        play();
    }

    private void play(){


        //while 2 Players inGame = true
        //get next player whos turn it is
        while (twoinGame()){
            for (int i  = 0; i < players.size(); i++){

                //Action choosenAction = manager.next(players.get(i));
                //choosenAction.playAction();

            }

        }
        for (int i  = 0; i < players.size(); i++){
            boolean inGame = players.get(i).getInGame();
            if (inGame= true){
                //  players.get(i) ist Gewinner
            }

        }

    }
    public void shuffleCards(){
        Collections.shuffle(cards);
    }
    public int sizeOfDeck(){
        return cards.size();
    }
    public Card dealCard(){
        return cards.pop();
    }
    public List<Card> getCards(){
        return this.cards;
    }

    //check if >1 Players are inGame
    public boolean twoinGame(){

        int inGame = 0;

        for (int i  = 0; i < players.size(); i++) {
            boolean x = players.get(i).getInGame();
            if (x = true) {
                inGame++;
            }
        }

        if (inGame > 1) {
            return true;
        } else {
            return false;
        }
    }
}
