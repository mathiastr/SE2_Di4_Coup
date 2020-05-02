package com.example.coup;

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
        for(Player p: players){
            p.setCoins(2);
        }
        shuffleCards();
        //for each Player set 2 random Cards and remove these cards from playingCards (deck)
        play();
    }

    private void play(){
        //TODO
        //while 2 Players inGame = true
            //get next player whos turn it is
            //if player has more than 10 coins, Action Coup is required
            //choosenAction = manager.next(players.get(x));
            //choosenAction.playAction();
            //depending on action blocks might be possible by certain players
            //chance to challenge after every Action
    }
    public void shuffleCards(){
        Collections.shuffle(cards);
    }
    public int sizeOfDeck(){
        return cards.size();
    }
    public Card dealCard(){
        shuffleCards();
        return cards.pop();
    }
    public void pushCard(Card card) { cards.push(card); }
    public List<Card> getCards(){
        return this.cards;
    }
    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
    public void returnCardtoDeck(Card c){
        cards.push(c);
    }
    public void dealStartOfGame() {
        for (Player p : getPlayers()) {
            List<Card> cards = new ArrayList<>();
            cards.add(dealCard());
            cards.add(dealCard());
            p.setCards(cards);
        }
    }
}
