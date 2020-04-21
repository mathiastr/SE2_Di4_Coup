package com.example.coup.Action;

import com.example.coup.CardType;
import com.example.coup.Player;

public class Challenge extends Action{
    Action preAction;

    // true by default? *mesa*
    boolean showCard=true;

    public Challenge(Player player, Action preAction){
        //TODO machen die Folgenden Zeilen nicht genau das gleiche zwei mal?
        super(player);
        this.playerDoingAction = player;

        this.preAction = preAction;
    }

    //returns true when challenged player has the correct card
    public boolean playReaction(CardType neededCardType){
        //preAction.getNeededCardType
        //Player who is challenged:
        Player challenged = preAction.playerDoingAction;
        //if player has that Card, he can choose show card or loseCard
        boolean firstCardIsNeeded = challenged.getCards().get(0).getTypeOfCard().equals(neededCardType);
        boolean secondCardIsNeeded = challenged.getCards().get(1).getTypeOfCard().equals(neededCardType);
        if(firstCardIsNeeded | secondCardIsNeeded){
            //TODO give Player option to choose "show your Card" or "lose a card"
            //showCard = input vom Spieler
            if(showCard){
                if(firstCardIsNeeded) challenged.getCards().get(0).revealCard();
                else challenged.getCards().get(1).revealCard();
                this.playerDoingAction.loseCard();
                //true<->false *mesa*
                return false;
            }
            else challenged.loseCard();
        }
        else challenged.loseCard();

        // false <->true *mesa*
        return true;

        //else the player doesn't have that card -> loseCard
        //when he showed the card -> the other player (played the challenge) loseCard
    }
}
