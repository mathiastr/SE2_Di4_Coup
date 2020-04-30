package com.example.coup.Action;

import com.example.coup.CardType;
import com.example.coup.Player;

public class BlockForeignAid extends Action {
    ForeignAid foreignAidToBlock;
    boolean isBlockForeignAidChallanged=false;
    public BlockForeignAid(Player player, ForeignAid foreignAid){
        super(player);
        this.playerDoingAction=player;
        this.foreignAidToBlock=foreignAid;
    }
    //if block of ForeignAid is successfull return true, else false
    public boolean playReaction() {
        if(isBlockForeignAidChallanged) {
            boolean isFirsCardDuke = playerDoingAction.getCards().get(0).getTypeOfCard().equals(CardType.DUKE);
            boolean isSecondCardDuke = playerDoingAction.getCards().get(1).getTypeOfCard().equals(CardType.DUKE);
            if (isFirsCardDuke || isSecondCardDuke) {
//                foreignAidToBlock.playerDoingAction.loseCard();
                return true;
            } else {
//                playerDoingAction.loseCard();
                return false;
            }
        }else{
            return true;

        }

    }

    public boolean isBlockForeignAidChallanged() {
        return isBlockForeignAidChallanged;
    }

    public void setBlockForeignAidChallanged(boolean blockForeignAidChallanged) {
        isBlockForeignAidChallanged = blockForeignAidChallanged;
    }
}
