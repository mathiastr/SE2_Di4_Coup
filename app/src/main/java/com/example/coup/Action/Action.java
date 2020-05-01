package com.example.coup.Action;

import com.example.coup.Game;
import com.example.coup.Player;

public class Action {

        public Player whoClickedChallenge;
        public int isClickedChallenge;
        Player playerDoingAction;
        boolean isActionBlocked=false;


        public Action(Player playerDoingAction){
            this.playerDoingAction=playerDoingAction;
        }


        public  void playAction(Player playerDoingAction,Player target){
        }

        public boolean isActionBlocked() {
                return isActionBlocked;
        }

        public void setActionBlocked(boolean actionBlocked) {
                isActionBlocked = actionBlocked;
        }

        public void stop(){

        }
}
