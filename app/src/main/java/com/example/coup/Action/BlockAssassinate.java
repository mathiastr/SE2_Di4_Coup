package com.example.coup.Action;

import com.example.coup.CardType;
import com.example.coup.Player;

public class BlockAssassinate extends Action {
    Assassinate assassinateToBlock;
    boolean isBlockAssassinateChallanged=false;
    public BlockAssassinate(Player player, Assassinate assassinate){
        super(player);
        this.playerDoingAction=player;
        this.assassinateToBlock=assassinate;
    }
    //if block of Assassination is successfull return true, else false
    public boolean playReaction(){
        boolean isFirsCardContessa = playerDoingAction.getCards().get(0).getTypeOfCard().equals(CardType.CONTESSA);
        boolean isSecondCardContessa = playerDoingAction.getCards().get(1).getTypeOfCard().equals(CardType.CONTESSA);

            if(isBlockAssassinateChallanged) {
                if (isFirsCardContessa || isSecondCardContessa) {

                    // player1 that wanted to assassinate player2 loses card
                    //player2 blocked assassinate claiming to have contessa
                    //player1 challenged player2 to show Contessa
                    //player2 showed contessa
//                    assassinateToBlock.playerDoingAction.loseCard();
                    return true;
                } else {
//                    playerDoingAction.loseCard();
                    return false;
                }
            }else{
//                player that wanted to assassinate loses 3 coins while his assassian action is blocked???
//                assassinateToBlock.playerDoingAction.setCoins(assassinateToBlock.playerDoingAction.getCoins()-3);
                return true;
        }

    }

    public boolean isBlockAssassinateChallanged() {
        return isBlockAssassinateChallanged;
    }

    public void setBlockAssassinateChallanged(boolean blockAssassinateChallanged) {
        isBlockAssassinateChallanged = blockAssassinateChallanged;
    }
}
