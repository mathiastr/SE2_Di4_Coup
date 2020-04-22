package com.example.coup.Action;

import com.example.coup.Player;

public class Exchange extends Action {

//TODO
        //get 2 Random cards from the deck
        //player can choose one or no card (from the new cards)
        //if one card is choosen, choose a card from players own cards to exchange
        //every player can challange (needed Card is Ambassador)
        public Exchange(Player playerDoingAction){
            super(playerDoingAction);
            this.playerDoingAction=playerDoingAction;

        }
    public void playAction(){
    }
}
