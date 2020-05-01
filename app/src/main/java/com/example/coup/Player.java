package com.example.coup;

import java.util.ArrayList;
import java.util.List;

// Organisiert die Übersicht der einzelnen Spieler und deren Verfügbare Aktionen

public class Player {
    private String name;
    public  int coins=2;
    public  int influence=2;
    private List<Card> cards;
    private boolean inGame=true;
    private static int playerID= 1;

    private boolean canBlockAssassination=false;
    private boolean canBlockForeignAid=false;
    private boolean canBlockSteal=false;



    public Player(String name){
        this.name=name;
        playerID++;
        cards = new ArrayList<Card>();
    }

    public int hasCard(CardType type){
        //return -1 when Player doesnt have that Cardtype
        //return 0 when index of that Card is 0 (first card)
        //return 1 when index of that Card is 1 (second card)
        if(cards.get(0).getTypeOfCard().equals(type)) return 0;
        else if(cards.get(1).getTypeOfCard().equals(type)) return 1;
        else return -1;
    }

    public void revealCard(int cardIndex){
        //TODO show card for a view sec and then delete it
        //wait(1000); or show message what card
        cards.remove(cardIndex);
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public void addCard(Card c){
        this.cards.add(c);
    }

    public int getCoins(){
        return this.coins;
    }
    public String getName(){
        return this.name;
    }
    public void setCoins(int coins){
        this.coins=coins;
    }

    //TODO influence sind die Anzahl der Karten
    public int getInfluence() {
        return influence;
    }
    public void setInfluence(int influence) {
        this.influence = influence;
    }

    //lose Influence heißt der Spieler verliert eine Karte
    //public void loseInfluence(){
    //    this.influence--;
    //}

    public void loseCard(){
        int cardIndex = 0;
        if(cards.size()==2){
            //TODO let player choose which card to lose, cardIndex = 0 oder 1
            cards.remove(cardIndex);
        }
        else {
            cards.clear();
            inGame = false;
        }

    }

    //Umsetzung der gewählten Aktionen beim Spieler, durch Aufruf der Methoden in Action Klassen
    public void doIncomeAction(){
        coins++;
    }
    public void doForeignAidAction(){
        coins+=2;
    }
    public boolean ifCoupPossibleDoIt(){
        if(coins>=7){
            return true;
        }else {
            return false;
        }
    }
    public void doDukesAction(){
        coins+=3;
    }
    public boolean isAssassinsActionPossible(){
        if(coins>=3){
            coins-=3;
            return true;
        }else{
            return false;
        }
    }
    public void checkForReactions(){
        for(Card c:cards){
            if(c.getTypeOfCard()==CardType.CONTESSA){
                canBlockAssassination=true;
            }
            if(c.getTypeOfCard()==CardType.DUKE){
                canBlockForeignAid=true;
            }
            if(c.getTypeOfCard()==CardType.CAPTAIN||c.getTypeOfCard()==CardType.AMBASSADOR){
                canBlockSteal=true;
            }
        }
    }
    public boolean getCanBlockAssassination() {
        return canBlockAssassination;
    }
    public void setCanBlockAssassination(boolean canBlockAssassination) {
        this.canBlockAssassination = canBlockAssassination;
    }
    public boolean getCanBlockForeignAid() {
        return canBlockForeignAid;
    }
    public void setCanBlockForeignAid(boolean canBlockForeignAid) {
        this.canBlockForeignAid = canBlockForeignAid;
    }
    public boolean getCanBlockSteal() {
        return canBlockSteal;
    }
    public void setCanBlockSteal(boolean canBlockSteal) {
        this.canBlockSteal = canBlockSteal;
    }

    public boolean getInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }


    //TODO bitte alles folgende Löschen, wenn du es nicht brauchst @Mathias
/*
    //Regelt das Layout jedes Spielers für die einzelnen Aktionen
    TextView tvTimer;
    boolean timerOn;
    Button btnChallenge;
    boolean challengeClicked;

    Button btnChooseAmbassador;
    Button btnChooseCaptain;
    int choosenType;

    Button btnShow;
    Button btnLoose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_player_layout);

        tvTimer = findViewById(R.id.tvTimer);
        btnChallenge = findViewById(R.id.btnChallenge);
        btnChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                challengeClicked = true;
                timerOn = false;
            }
        });

        btnChooseAmbassador = findViewById(R.id.btnChooseAmb);
        btnChooseCaptain = findViewById(R.id.btnChooseCap);
        btnChooseAmbassador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosenType = 1;
            }
        });
        btnChooseCaptain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosenType = 2;
            }
        });

        btnShow = findViewById(R.id.btnShow);
        btnLoose = findViewById(R.id.btnLoose);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosenType = 1;
            }
        });
        btnLoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosenType = 2;
            }
        });
    }

    public void startTimer(int sec, Action a){
        timerOn = true;
        long currentTime;
        long endTime = System.currentTimeMillis() + (sec*1000);
        do{
            currentTime = System.currentTimeMillis();
            tvTimer.setText(((endTime-currentTime)/1000) + "sec");
        } while(currentTime < endTime && timerOn);
        if(currentTime>=endTime) a.isClickedChallenge = -1;
    }

    public void stopTimer(){
        timerOn = false;
    }

    public void challenge(Action a){
        btnChallenge.setVisibility(View.VISIBLE);
        challengeClicked = false;
        while(!challengeClicked && timerOn){

        }
        if(challengeClicked){
            a.whoClickedChallenge = this;
            a.isClickedChallenge = 1;
        }
        btnChallenge.setVisibility(View.INVISIBLE);
    }

    public CardType chooseAmOrCa(){
        btnChooseAmbassador.setVisibility(View.VISIBLE);
        btnChooseCaptain.setVisibility(View.VISIBLE);
        choosenType = 0;
        while(choosenType==0){

        }
        btnChooseAmbassador.setVisibility(View.INVISIBLE);
        btnChooseCaptain.setVisibility(View.INVISIBLE);
        if(choosenType==1){
            return CardType.AMBASSADOR;
        }
        else return CardType.CAPTAIN;
    }

    public boolean showOrLoose(int card, Player p){
        btnShow.setVisibility(View.VISIBLE);
        btnLoose.setVisibility(View.VISIBLE);
        choosenType = 0;
        while(choosenType==0){

        }
        btnShow.setVisibility(View.INVISIBLE);
        btnLoose.setVisibility(View.INVISIBLE);

        if(choosenType==1){
            getCards().get(card).revealCard();
            p.loseCard();
            return false;
        }
        else {
            loseCard();
            return true;
        }
    }

 */
}
