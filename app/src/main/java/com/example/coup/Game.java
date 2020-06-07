package com.example.coup;


import android.drm.DrmStore;

import java.util.*;

public class Game {
    List<Player> players;
    List<Card> playingCards;
    //InGame InGame = new InGame();
    Stack<Card> cards;
    Action lastAction;
    Player playerDoingLastAction;

    public Game(){}


    public Game(List<Player> players){
        Player isWinner = null;
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


        //for each Player set 2 random Cards and remove these cards from cards (deck)

        /**
        shuffleCards();
        for (Player p : players){
            playingCards.add(dealCard());
            playingCards.add(dealCard());
            p.setCards(playingCards);
        }**/

//        play();
    }

    public Player updatePlayerCoins(String name, int coins){

        for(Player p: this.players){
            if(p.getName().equals(name)){
                p.setCoins(p.getCoins()+coins);
                return p;
            }
        }

        return null;

    }

    public Player getPlayerByName(String name){

        for(Player p: this.players){
            if(p.getName().equals(name)){
                return p;
            }
        }

        return null;

    }

    public void removePlayer(String name){

        this.players.remove(getPlayerByName(name));

    }



   /* private void play(){

        Game g = new Game(players);
        AllActions playAction = new AllActions(g);

        //while 2 Players inGame = true
        //get next player whos turn it is
        while (twoinGame()){
            for (Player p : players){

                boolean Actionisblocked;
                boolean playerbluffed = false;
                Object [] chooseAction = InGame.next(p);
                Action choosenAction = (Action) chooseAction[0];
                Player attackedPlayer = (Player) chooseAction[1];

                //check if player bluffed (if someone challenged him)
                if (InGame.waitForChallenge()!=null) {
                    Object[] challengeAction = InGame.waitForChallenge();
                    Player challengingPlayer1 = (Player) challengeAction[0];
                    CardType neededCardType1 = (CardType) challengeAction[1];
                    playerbluffed = playAction.challenge(neededCardType1, challengingPlayer1, p);
                }

                // Action steal or assassinate can only be blocked by the attacked player
                if (choosenAction == Action.STEAL || choosenAction == Action.ASSASSINATE){
                    List<Player> Playerscanblock = new ArrayList<>();
                    Playerscanblock.add(attackedPlayer);
                    Actionisblocked = InGame.waitForBlock(Playerscanblock);
                }else {
                    Actionisblocked = InGame.waitForBlock(players);
                }


                //if action is blocked and block is challenged, check if blocking player bluffed
                if (Actionisblocked && InGame.waitForChallenge()!=null){
                    Object[] challengeBlock = InGame.waitForChallenge();
                    Player challengingPlayer2 = (Player)challengeBlock[0];
                    CardType neededCardType2 = (CardType)challengeBlock[1];
                    boolean blockingplayerbluffed = playAction.challenge(neededCardType2,challengingPlayer2,p);
                    if (blockingplayerbluffed){
                        Actionisblocked = false;
                    }

                }
                // if player is not challenged or is challenged but didn't bluff and action is not blocked -> play choosen action
                if (!playerbluffed && !Actionisblocked) {

                    switch (choosenAction) {
                        case ASSASSINATE:
                            playAction.assassinate(p, attackedPlayer);
                        case FOREIGNAID:
                            playAction.foreignAid(p);
                        case INCOME:
                            playAction.income(p);
                        case EXCHANGE:
                            playAction.exchange(p);
                        case STEAL:
                            playAction.steal(p, attackedPlayer);
                            break;
                        case TAX:
                            playAction.tax(p);
                            break;
                        case COUP:
                            playAction.coup(p, attackedPlayer);
                            break;

                    }
                }
            }
        }

        for (Player p : players){
            boolean inGame = p.getInGame();
            if (inGame= true){

                //p is winner

            }

        }

    }
    */

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
    public void pushCard(Card card) { cards.push(card); }

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
    public Player getWinnerOfGame(){
        Player isWinner = null;
        for(Player p: players) {
            if (p.getInGame()) {
                if (isWinner != null) {
                    return null;
                } else {
                    isWinner = p;
                }
            }
        }
        return isWinner;
    }
    
    //check if >1 Players are inGame
    public boolean twoinGame(){
        int inGame = 0;

        for (Player p : players) {
            boolean x = p.getInGame();
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
    public void setLastAction(Action action){
        this.lastAction=action;
    }
    public Action getLastAction(){
        return this.lastAction;
    }
    public void setPlayerDoingLastAction(Player player){
        this.playerDoingLastAction=player;
    }
    public Player getPlayerDoingLastAction(){
        return this.playerDoingLastAction;
    }
}
