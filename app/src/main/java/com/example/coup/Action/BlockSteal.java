package com.example.coup.Action;

import com.example.coup.CardType;
import com.example.coup.Player;

public class BlockSteal extends Action{
    Steal stealToBlock;
    boolean isBlockStealChallanged=false;
    public BlockSteal(Player player, Steal steal){
        super(player);
        this.playerDoingAction=player;
        this.stealToBlock=steal;
    }
    //if block of Steal is successfull return true, else false
    public boolean playReaction(){
        //Cards Captain and Ambassador can block steal
        boolean isFirstCardCaptain = playerDoingAction.getCards().get(0).getTypeOfCard().equals(CardType.CAPTAIN);
        boolean isSecondCardCaptain = playerDoingAction.getCards().get(1).getTypeOfCard().equals(CardType.CAPTAIN);
        boolean isFirstCardAmbassador = playerDoingAction.getCards().get(0).getTypeOfCard().equals(CardType.AMBASSADOR);
        boolean isSecondCardAmbassador = playerDoingAction.getCards().get(1).getTypeOfCard().equals(CardType.AMBASSADOR);

        if(isBlockStealChallanged) {
            if ((isFirstCardCaptain || isFirstCardAmbassador)||(isSecondCardCaptain || isSecondCardAmbassador)) {

//                stealToBlock.playerDoingAction.loseCard();
                return true;
            } else {
//                playerDoingAction.loseCard();
                return false;
            }
        }else{
//            player that wanted to steal loses 2 coins while his steal action is blocked?.
//            stealToBlock.playerDoingAction.setCoins(stealToBlock.playerDoingAction.getCoins()-2);
            return true;
        }

    }

    public boolean isBlockStealChallanged() {
        return isBlockStealChallanged;
    }

    public void setBlockStealChallanged(boolean blockStealChallanged) {
        isBlockStealChallanged = blockStealChallanged;
    }
}
