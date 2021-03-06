package com.example.coup;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InGame extends Activity implements SensorEventListener {


    Player player;
    Player attackedPlayer;
    Game game;
    List<Card> cardsToChoose;
    List<Card> choosenCard;
    ImageView ivImageC1;
    ImageView ivImageC2;
    ImageView ivImageC3;
    ImageView ivImageC4;
    ImageView ivOpp1;
    ImageView ivOpp2;
    ImageView ivOpp3;


    private int count;
    protected boolean leftCardRemoved;
    protected boolean rightCardRemoved;
    protected boolean cardInHand;
    protected boolean challengeAccepted;
    protected boolean challengeDenied;
    protected boolean foreignAidBlocked;
    protected boolean turn;
    protected String name;
    protected String cardNameToShow;
    protected List<String> opponents;
    private List<String> playernames;
    protected ServerConnection connection;
    private Handler handler;
    protected Button assassinateButton;
    protected Button taxButton;
    protected Button stealButton;
    protected Button exchangeButton;
    protected Button incomeButton;
    protected Button foreignAidButton;
    protected Button coupButton;
    protected Button chooseCards;
    protected Button next;
    protected Button challenge;
    protected Button blockForeignAidButton;
    protected Button detectCheaterButton;
    private SensorManager s;
    private Sensor Accelerometer;
    private float current;
    private float last;
    private float shake;
    protected TextView coins;
    private TextView tvOpp1name;
    private TextView tvOpp2name;
    private TextView tvOpp3name;
    protected TextView tvOpp1coins;
    protected TextView tvOpp2coins;
    protected TextView tvOpp3coins;
    private TextView tvOpp1Inf;
    private TextView tvOpp2Inf;
    private TextView tvOpp3Inf;
    protected TextView timer;
    protected TextView textView;
    protected CountDownTimer countDown;
    protected CountDownTimer challengeTimer;
    private List<TextView> enemyTv;
    private List<TextView> coinsTv;
    private List<ImageView> enemyIv;
    private String coinsTxt = "Your coins: ";
    private String exchangeTxt="exchange";
    private String stealTxt="steal";
    private String assassinateTxt="assassinate";
    private String challengeTxt="challenge";
    private String looseCardTxt="loose card";
    private String assassinTxt="assassin";
    private String contessaTxt="contessa";
    private String captainTxt="captain";
    private String ambassadorTxt="ambassador";
    private String dukeTxt="duke";
    private String debugTXT="DEBUG";
    protected boolean[] notInGame = new boolean[3];
    private List<TextView> enemyInfluence;
    protected AlertDialog challengeDialog1;
    protected AlertDialog challengeDialog2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ingame);

        s = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Accelerometer = s.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        s.registerListener(this, Accelerometer , SensorManager.SENSOR_DELAY_NORMAL);

        current = SensorManager.GRAVITY_EARTH;
        last = SensorManager.GRAVITY_EARTH;
        shake = 0.00f;

        ivOpp1 = findViewById(R.id.imageView_enemy_one);
        ivOpp2 = findViewById(R.id.imageView_enemy_two);
        ivOpp3 = findViewById(R.id.imageView_enemy_three);

        ivOpp1.setImageResource(R.drawable.enemy_one_passive);
        ivOpp2.setImageResource(R.drawable.enemy_two_passive);
        ivOpp3.setImageResource(R.drawable.enemy_three_passive);

        tvOpp1Inf = findViewById(R.id.textView_enemy_one_influence);
        tvOpp2Inf = findViewById(R.id.textView_enemy_two_influence);
        tvOpp3Inf = findViewById(R.id.textView_enemy_three_influence);





        Bundle b = getIntent().getExtras();

        if(b!=null)
            name = b.getString("name");

        challenge = (Button) findViewById(R.id.button_challenge);
        next = findViewById(R.id.button_next);
        timer = findViewById(R.id.textView_timer);
        textView = findViewById(R.id.textView_action);
        incomeButton = findViewById(R.id.button_income);
        coins = findViewById(R.id.textView_coins);
        foreignAidButton = findViewById(R.id.button_foreign_aid);
        exchangeButton = findViewById(R.id.button_exchange);
        blockForeignAidButton = findViewById(R.id.button_blockforeignaid);

        chooseCards = (Button) findViewById(R.id.btnOK);

        //Time methods - optimise time after playing game. Either speed up or slow down.
        countDown = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                timer.setText("Your turn: " + millisUntilFinished / 1000);
            }

            public void onFinish() {

                timer.setText("Turn over");
                disableAll();

            }
        };


        connection = new ServerConnection();

        handler = new Handler();


        assassinateButton = (Button) findViewById(R.id.button_assassinate);
        taxButton = (Button) findViewById(R.id.button_tax);
        stealButton = (Button) findViewById(R.id.button_steal);
        exchangeButton = (Button) findViewById(R.id.button_exchange);
        incomeButton = (Button) findViewById(R.id.button_income);
        foreignAidButton = (Button) findViewById(R.id.button_foreign_aid);
        coupButton = (Button) findViewById(R.id.button_coup);
        detectCheaterButton = (Button) findViewById(R.id.button_suspect_cheat);




        ConnectTask connectTask = new ConnectTask();
        connectTask.execute();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendToServer("next");



                        disableNext();
                        foreignAidBlocked=false;
                        turn=false;
                        countDown.cancel();
                        timer.setText("");




            }
        });


        incomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToServer("income" + " " + name);


                        player=game.updatePlayerCoins(name, 1);
                        disableAll();
                        textView.setText("You did income");
                        coins.setText(coinsTxt + player.getCoins());



            }
        });

        foreignAidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToServer("foreignaid" + " " + name);


                        player=game.updatePlayerCoins(name, 2);
                        disableAll();
                        textView.setText("You did foreign aid");
                        coins.setText(coinsTxt + player.getCoins());



            }
        });
        exchangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToServer(exchangeTxt + " " + name);

                player= game.getPlayerByName(name);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        showCardsToExchange();
                    }
                }, 250);

            }
        });
        taxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToServer("tax" + " " + name);


                        player = game.updatePlayerCoins(name, 3);
                        disableAll();
                        textView.setText("You did tax");
                        coins.setText(coinsTxt + player.getCoins());




            }
        });
        stealButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectPlayer(stealTxt);


            }
        });

        assassinateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(player.getCoins()<3)
                    textView.setText("You need 3 coins to use assassinate");
                else
                    selectPlayer(assassinateTxt);
            }
        });

        coupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableNext();
                selectPlayer("coup");
            }
        });


        challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                disableNext();
                sendToServer(challengeTxt);
            }
        });

        blockForeignAidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPlayer("bfa");
            }
        });

        detectCheaterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPlayer("detectCheater");
                disableAll();
            }
        });



    }

    protected void selectPlayer(final String action){

        final AlertDialog.Builder builder = new AlertDialog.Builder(InGame.this);
        builder.setTitle("Choose Player");

        // add a list
        final String[] players = opponents.toArray(new String[opponents.size()]);
        builder.setItems(players, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                attackedPlayer = game.getPlayerByName(players[which]);

                doAction(action);


            }

        });
        builder.setNegativeButton("abort", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();



    }

    protected void doAction(String action) {
        if(action.equals("bfa"))
            blockForeignAid();
        if(action.equals(stealTxt))
            stealFromPlayer();
        if(action.equals("coup"))
            doCoup();
        if(action.equals(assassinateTxt))
            assasinateplayer();
        if (action.equals("detectCheater"))
            detectCheater();

    }

    protected void blockForeignAid(){

        sendToServer("bfa" + " " + name + " " + attackedPlayer.getName());

        disableAll();

    }


    // cheatfunktion: wenn man das Smartphone schüttelt erhält man 3 Coins, nur 1 mal einsetzbar
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            last = current;
            current = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = current - last;
            shake = shake * 0.9f + delta;


            if (shake > 15 && !player.getCheated()) {


                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            connection.sendMessage("cheat" + " " + name);

                            for (Player me : game.getPlayers())
                                if (me.getName().equals(name)) {
                                    me.setCoins(me.getCoins() +3);
                                    me.setCheated(true);
                                    player = me;
                                }

                        }
                    });

                    thread.start();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText("You cheated");
                            coins.setText(coinsTxt + player.getCoins());
                        }
                    });



            }

        }
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public void challengePlayer(final String lastPlayer, final String lastAction) {

        boolean challengable = lastAction.equals(assassinateTxt) || lastAction.equals("tax")
                || lastAction.equals(stealTxt) || lastAction.equals(exchangeTxt) || lastAction.equals("bfa");


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //TODO: Add w hat happened last turn to text.

        if (challengable) {

            builder.setMessage("Are you sure you want to challenge " + lastAction + " on " + lastPlayer + " ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                            sendToServer(challengeTxt + " " + name + " " + lastPlayer + " " + lastAction);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {

                                    textView.setText("You did challenge");
                                    disableAll();

                                }
                            });

                            dialogInterface.dismiss();

                        }
                    })

                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
        } else {

            builder.setMessage("You can not challenge " + lastAction + " on " + lastPlayer)
                    .setCancelable(false)
                    .setNegativeButton("abort", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sendToServer("stop");
                            enableNext();
                            dialog.cancel();
                        }
                    });

        }

        challengeDialog1 = builder.create();

        challengeDialog1.show();


    }

    public void challengeConfirmation() {

        challengeAccepted = false;
        challengeDenied = false;

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //TODO: Add what happened last turn to text.
        builder.setMessage("You have been challenged! Choose an option")
                .setCancelable(false)
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        acceptChallenge();
                        dialogInterface.dismiss();



                    }
                })

                .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        denyChallenge();


                        dialogInterface.dismiss();
                    }
                });


        challengeDialog2 = builder.create();


        Log.e("DEBUG CARDINHAND", "" + cardInHand);



        challengeDialog2.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {


                setChallengeTimer(challengeDialog2);

            }
        });

        challengeDialog2.show();

    }

    protected void setChallengeTimer(final AlertDialog challengeDialog) {
        if (!cardInHand)
            challengeDialog.getButton(challengeDialog.BUTTON_POSITIVE).setEnabled(false);

        challengeTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                challengeDialog.setMessage("Your last move has been challenged. Do you accept the challenge?" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {

                if(!challengeAccepted && !challengeDenied)
                    challengeDialog.getButton(DialogInterface.BUTTON_NEGATIVE).callOnClick();

            }
        };

        challengeTimer.start();
    }

    protected void denyChallenge() {
        sendToServer(looseCardTxt + " " + name);
        challengeDenied = true;

        handler.post(new Runnable() {
            @Override
            public void run() {
                mainPlayerChoosesCardToLose("challenge");

            }
        });


    }

    protected void acceptChallenge() {
        sendToServer("show card" + " "+ name+ " " + cardNameToShow);

        challengeAccepted = true;
        cardInHand=false;

        textView.setText("Challenge successful");



    }

    public void stealFromPlayer() {

        if(attackedPlayer.getCoins()<2)
            textView.setText(attackedPlayer.getName()+" has not enough coins to steal from" );
        else {

            sendToServer(stealTxt + " " + name + " " + attackedPlayer.getName());

        }

    }

    public void detectCheater(){
        sendToServer("sCheat"+" "+name+" "+attackedPlayer.getName());

    }

    public void disableAll(){
        assassinateButton.setEnabled(false);
        incomeButton.setEnabled(false);
        foreignAidButton.setEnabled(false);
        exchangeButton.setEnabled(false);
        stealButton.setEnabled(false);
        taxButton.setEnabled(false);
        coupButton.setEnabled(false);
        challenge.setEnabled(false);
        blockForeignAidButton.setEnabled(false);
        detectCheaterButton.setEnabled(false);

        assassinateButton.setBackgroundResource(R.drawable.assassinate_button_passive);
        incomeButton.setBackgroundResource(R.drawable.income_button_passive);
        foreignAidButton.setBackgroundResource(R.drawable.foreign_button_passive);
        exchangeButton.setBackgroundResource(R.drawable.exchange_button_passive);
        stealButton.setBackgroundResource(R.drawable.steal_button_passive);
        taxButton.setBackgroundResource(R.drawable.tax_button_passive);
        coupButton.setBackgroundResource(R.drawable.coup_button_passive);
        challenge.setBackgroundResource(R.drawable.challenge_button_passive);
        blockForeignAidButton.setBackgroundResource(R.drawable.blockfa_button_passive);
        detectCheaterButton.setBackgroundResource(R.drawable.suspect_button_passive);

    }

    public void enableAll() {

        if(player.getCoins()<7){
            coupButton.setEnabled(false);
            coupButton.setBackgroundResource(R.drawable.coup_button_passive);
        }
        else {
            coupButton.setEnabled(true);
            coupButton.setBackgroundResource(R.drawable.coup_button);
        }

        if(player.getCoins()>=10){
            disableAll();
            coupButton.setEnabled(true);
            coupButton.setBackgroundResource(R.drawable.coup_button);
        }
        else{
            assassinateButton.setEnabled(true);
            incomeButton.setEnabled(true);
            if(foreignAidBlocked){
                foreignAidButton.setEnabled(false);
                foreignAidButton.setBackgroundResource(R.drawable.foreign_button_passive);
            }
            else{
                foreignAidButton.setEnabled(true);
                foreignAidButton.setBackgroundResource(R.drawable.foreign_button);
            }
            exchangeButton.setEnabled(true);
            stealButton.setEnabled(true);
            taxButton.setEnabled(true);

            blockForeignAidButton.setEnabled(true);

            challenge.setEnabled(true);

            detectCheaterButton.setEnabled(true);


            assassinateButton.setBackgroundResource(R.drawable.assassinate_button);
            incomeButton.setBackgroundResource(R.drawable.income_button);
            exchangeButton.setBackgroundResource(R.drawable.exchange_button);
            stealButton.setBackgroundResource(R.drawable.steal_button);
            taxButton.setBackgroundResource(R.drawable.tax_button);
            challenge.setBackgroundResource(R.drawable.challenge_button);
            blockForeignAidButton.setBackgroundResource(R.drawable.blockfa_button);
            detectCheaterButton.setBackgroundResource(R.drawable.suspect_button);

        }
    }

    protected void enableNext(){
        next.setEnabled(true);
        next.setBackgroundResource(R.drawable.next_button);
    }
    protected void disableNext(){
        next.setEnabled(false);
        next.setBackgroundResource(R.drawable.next_button_passive);
    }

    protected void doCoup() {

        sendToServer("coup" + " " + name + " " + attackedPlayer.getName());


                disableAll();
                player = game.updatePlayerCoins(name, -7);
                textView.setText("You did coup on " + attackedPlayer.getName());
                coins.setText("Your coins: " + player.getCoins());


    }


    //initialize opponent textviews
    public void initializeOpponents(List<String> opponents) {
        tvOpp1name=(TextView) findViewById(R.id.textView_name_enemy_one);
        tvOpp2name=(TextView) findViewById(R.id.textView_name_enemy_two);
        tvOpp3name=(TextView) findViewById(R.id.textView_name_enemy_three);

        tvOpp1coins = findViewById(R.id.textView_enemy_one_coins);
        tvOpp2coins = findViewById(R.id.textView_enemy_two_coins);
        tvOpp3coins = findViewById(R.id.textView_enemy_three_coins);



        Log.e("DEBUG: ",opponents.get(0));

        tvOpp1name.setText(opponents.get(0));

        if(opponents.size()>2){
        tvOpp2name.setText(opponents.get(1));
        tvOpp3name.setText(opponents.get(2));
        }

        tvOpp1coins.setText("2");
        tvOpp2coins.setText("2");
        tvOpp3coins.setText("2");

        enemyTv = new LinkedList<>();
        enemyTv.add(tvOpp1name);
        enemyTv.add(tvOpp2name);
        enemyTv.add(tvOpp3name);

        coinsTv = new LinkedList<>();
        coinsTv.add(tvOpp1coins);
        coinsTv.add(tvOpp2coins);
        coinsTv.add(tvOpp3coins);

        enemyInfluence = new LinkedList<>();
        enemyInfluence.add(tvOpp1Inf);
        enemyInfluence.add(tvOpp2Inf);
        enemyInfluence.add(tvOpp3Inf);


    }

    public void updateCoins(String onPlayer, int coinsAdded){

        for(int i=0; i<enemyTv.size();i++){
            if(enemyTv.get(i).getText().equals(onPlayer)){

                if(coinsAdded==0){
                    Player p = game.updatePlayerCoins(onPlayer, 0);
                    coinsTv.get(i).setText(Integer.toString(p.getCoins()));

                }
                else{

                Player p = game.updatePlayerCoins(onPlayer, coinsAdded);
                coinsTv.get(i).setText(Integer.toString(p.getCoins()));
                }
            }
        }

    }


    public void removeOpponent(String onPlayer){

        for(int i=0; i<enemyTv.size();i++){
            if(enemyTv.get(i).getText().equals(onPlayer)){
                if(i==0){
                    ivOpp1.setImageResource(R.drawable.enemy_one_dead);
                    tvOpp1Inf.setText(Integer.toString(0));
                    tvOpp1Inf.setTextColor(Color.RED);
                }
                if(i==1){
                    ivOpp2.setImageResource(R.drawable.enemy_two_dead);
                    tvOpp2Inf.setText(Integer.toString(0));
                    tvOpp2Inf.setTextColor(Color.RED);
                }
                if(i==2){
                    ivOpp3.setImageResource(R.drawable.enemy_three_dead);
                    tvOpp3Inf.setText(Integer.toString(0));
                    tvOpp3Inf.setTextColor(Color.RED);
                }
                notInGame[i]=true;

            }
        }


    }
    public void updateOpponentOnTurn(String onPlayer){

        for(int i=0; i<enemyTv.size();i++){
            if(enemyTv.get(i).getText().equals(onPlayer)){

                if(i==0)
                    ivOpp1.setImageResource(R.drawable.enemy_one_active);

                if(i==1)
                    ivOpp2.setImageResource(R.drawable.enemy_two_active);

                if(i==2)
                    ivOpp3.setImageResource(R.drawable.enemy_three_active);
            }
            else
                if(!notInGame[i]){
                    if(i==0)
                        ivOpp1.setImageResource(R.drawable.enemy_one_passive);

                    if(i==1)
                        ivOpp2.setImageResource(R.drawable.enemy_two_passive);

                    if(i==2)
                        ivOpp3.setImageResource(R.drawable.enemy_three_passive);


                }

        }


    }

    protected void decreaseInfluence(String onPlayer){

        for(int i=0; i<enemyTv.size();i++){
            if(enemyTv.get(i).getText().equals(onPlayer)){
                enemyInfluence.get(i).setText(Integer.toString(1));
            }
        }

    }



    public void mainPlayerChoosesCardToLose(String cause){
        ivImageC1 = (ImageView) findViewById(R.id.card_playercard1);
        ivImageC2 = (ImageView) findViewById(R.id.card_playercard2);

        // Display: "Click on the Card you want to lose."

        String why=null;
        if(cause.equals("coup"))
            why="Couped: ";
        if(cause.equals("challenge"))
            why="Challenge failed: ";
        if(cause.equals("assassinate"))
            why="Assassinated: ";


        textView.setText(why+"Choose a card to lose");

        count =0;


        if (player.getCards().size()==2) {

            ivImageC1.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {

                    leftCardRemoved = true;
                    looseCard();
                }
            });

            ivImageC2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rightCardRemoved = true;
                    looseCard();

                }
            });
        }
        else
            returnLastCard();



    }

    protected void looseCard(){

        Card c;

        int cardToRemove = 0;

            if(leftCardRemoved){
                c=player.getCards().get(0);
                ivImageC1.setVisibility(View.INVISIBLE);
            }
            else {
                c=player.getCards().get(1);
                cardToRemove = 1;
                ivImageC2.setVisibility(View.INVISIBLE);
            }

            ivImageC1.setOnClickListener(null);
            ivImageC2.setOnClickListener(null);

            //update player
            for(Player me: game.getPlayers()){
                if(me.getName().equals(name)){
                    me.getCards().remove(cardToRemove);
                    player=me;
                }

            }

            textView.setText("You lost an influence");

            final Card cardToReturn = c;


            sendToServer(getCardNameAsString(cardToReturn));



    }

    protected void returnLastCard(){

        final Card cardToReturn = player.getCards().get(0);

        sendToServer("lastcard"+" "+getCardNameAsString(cardToReturn));


    }

    protected String getCardNameAsString(Card cardToReturn) {
        switch (cardToReturn.getTypeOfCard()){
            case DUKE: return dukeTxt;
            case CAPTAIN: return captainTxt;
            case ASSASSIN: return assassinTxt;
            case CONTESSA: return contessaTxt;
            case AMBASSADOR: return ambassadorTxt;
            default: return "";
        }
    }


    public void settingCardImagesAtStartOfGame() {
        ImageView ivCard1 = (ImageView) findViewById(R.id.card_playercard1);
        ImageView ivCard2 = (ImageView) findViewById(R.id.card_playercard2);

        CardType firstCardType= player.getCards().get(0).getTypeOfCard();
        CardType secondCardType= player.getCards().get(1).getTypeOfCard();
        displayCards(firstCardType,ivCard1);
        displayCards(secondCardType,ivCard2);
        ivCard1.setVisibility(View.VISIBLE);
        ivCard2.setVisibility(View.VISIBLE);
    }
    public void displayCards(CardType ct,ImageView iv){
        if (ct.equals(CardType.CONTESSA)) {
            iv.setImageResource(R.drawable.contessa);
        } else if (ct.equals(CardType.DUKE)) {
            iv.setImageResource(R.drawable.duke);
        } else if (ct.equals(CardType.ASSASSIN)) {
            iv.setImageResource(R.drawable.assassin);
        } else if (ct.equals(CardType.CAPTAIN)) {
            iv.setImageResource(R.drawable.captain);
        } else if (ct.equals(CardType.AMBASSADOR)) {
            iv.setImageResource(R.drawable.ambassador);
        }
    }
    public void showCardsToExchange() {

        disableAll();

        disableNext();

        ivImageC1 = (ImageView) findViewById(R.id.card_playercard1);
        ivImageC2 = (ImageView) findViewById(R.id.card_playercard2);
        ivImageC3 = (ImageView) findViewById(R.id.card_playercard3);
        ivImageC4 = (ImageView) findViewById(R.id.card_playercard4);

        choosenCard = cardsToChoose;

        displayCards(cardsToChoose.get(1).getTypeOfCard(), ivImageC3);
        displayCards(cardsToChoose.get(0).getTypeOfCard(), ivImageC4);

        ivImageC3.setVisibility(View.VISIBLE);
        ivImageC4.setVisibility(View.VISIBLE);



        chooseCards.setVisibility(View.VISIBLE);
        ivImageC4.setClickable(true);
        ivImageC3.setClickable(true);

        if(player.getCards().size()==2){
            ivImageC4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Card> hand = new ArrayList<>();
                    hand.add(cardsToChoose.get(0));
                    hand.add(player.getCards().get(1));
                    cardsToChoose.set(0, player.getCards().get(0));
                    player.setCards(hand);
                    displayCards(player.getCards().get(0).getTypeOfCard(), ivImageC1);
                    displayCards(cardsToChoose.get(0).getTypeOfCard(), ivImageC4);
                }
            });

            ivImageC3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    List<Card> hand = new ArrayList<>();
                    hand.add(player.getCards().get(0));
                    hand.add(cardsToChoose.get(1));

                    cardsToChoose.set(1, player.getCards().get(1));

                    player.setCards(hand);

                    displayCards(player.getCards().get(1).getTypeOfCard(), ivImageC2);
                    displayCards(cardsToChoose.get(1).getTypeOfCard(), ivImageC3);
                }
            });
        }
        else {

            if(leftCardRemoved){

                ivImageC4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        List<Card> hand = new ArrayList<>();
                        hand.add(cardsToChoose.get(0));

                        cardsToChoose.set(0, player.getCards().get(0));

                        player.setCards(hand);
                        displayCards(player.getCards().get(0).getTypeOfCard(), ivImageC2);
                        displayCards(cardsToChoose.get(0).getTypeOfCard(), ivImageC4);

                    }
                });

                ivImageC3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        List<Card> hand = new ArrayList<>();
                        hand.add(cardsToChoose.get(1));

                        cardsToChoose.set(1, player.getCards().get(0));

                        player.setCards(hand);
                        displayCards(player.getCards().get(0).getTypeOfCard(), ivImageC2);
                        displayCards(cardsToChoose.get(1).getTypeOfCard(), ivImageC3);

                    }
                });

            }

            if(rightCardRemoved){

                ivImageC4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        List<Card> hand = new ArrayList<>();
                        hand.add(cardsToChoose.get(0));

                        cardsToChoose.set(0, player.getCards().get(0));

                        player.setCards(hand);
                        displayCards(player.getCards().get(0).getTypeOfCard(), ivImageC1);
                        displayCards(cardsToChoose.get(0).getTypeOfCard(), ivImageC4);

                    }
                });

                ivImageC3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        List<Card> hand = new ArrayList<>();
                        hand.add(cardsToChoose.get(1));

                        cardsToChoose.set(1, player.getCards().get(0));

                        player.setCards(hand);
                        displayCards(player.getCards().get(0).getTypeOfCard(), ivImageC1);
                        displayCards(cardsToChoose.get(1).getTypeOfCard(), ivImageC3);

                    }
                });

            }
        }

        chooseCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final List<String> cardsToReturn = convertCardTypeToStringName(cardsToChoose);

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for(String cardname:cardsToReturn)
                            connection.sendMessage(cardname);

                    }
                });
                thread.start();


                ivImageC3.setVisibility(View.INVISIBLE);
                ivImageC4.setVisibility(View.INVISIBLE);

                ivImageC4.setClickable(false);
                ivImageC4.setOnClickListener(null);


                chooseCards.setVisibility(View.INVISIBLE);
                enableNext();

            }
        });


    }
    public ArrayList<String> convertCardTypeToStringName(List<Card> listOfCards){
        ArrayList<String> convertedList= new ArrayList<>();
        for(Card c :listOfCards) {
            if (c.getTypeOfCard().equals(CardType.ASSASSIN)) {
                convertedList.add(assassinTxt);
            }
            if (c.getTypeOfCard().equals(CardType.DUKE)) {
                convertedList.add(dukeTxt);
            }
            if (c.getTypeOfCard().equals(CardType.CONTESSA)) {
                convertedList.add(contessaTxt);
            }
            if (c.getTypeOfCard().equals(CardType.AMBASSADOR)) {
                convertedList.add(ambassadorTxt);
            }
            if (c.getTypeOfCard().equals(CardType.CAPTAIN)) {
                convertedList.add(captainTxt);
            }
        }
    return convertedList;}


    protected void assasinateplayer(){

        sendToServer(assassinateTxt+" "+name+" "+attackedPlayer.getName());


                disableAll();
                disableNext();
                textView.setText("Assassinating "+attackedPlayer.getName());


    }

    protected void blockAction(final String attacker, final String action){

        sendToServer("block");


                if(action.equals(stealTxt))
                    textView.setText("You blocked "+attacker+" stealing from you");
                else textView.setText("You blocked "+attacker+ " assassinating you");

    }

    protected void sendToServer(final String message){

        new Thread(new Runnable() {
            @Override
            public void run() {
                connection.sendMessage(message);
            }
        }).start();

    }

    protected void handleMessage(String msg, final String[] split){

        if(msg.startsWith("turn")){
            enableAll();
            enableNext();
            textView.setText("Your turn");
            timer.setVisibility(View.VISIBLE);
            countDown.start();
            updateOpponentOnTurn(name);
            turn=true;

        }
        if(msg.startsWith("income")){
            textView.setText(split[1]+" used income");
            updateCoins(split[1], 1);

        }
        if(msg.startsWith("tax")){

        }
        if(msg.startsWith("foreignaid")){
            textView.setText(split[1]+" used Foreign Aid");
            updateCoins(split[1], 2);

        }
        if(msg.startsWith("steal")){

        }
        if(msg.startsWith("nextplayer")){
            textView.setText(split[1]+"'s turn");
            updateOpponentOnTurn(split[1]);

        }

        if(msg.startsWith("tax")){

            textView.setText(split[1]+" used tax");
            updateCoins(split[1], 3);

        }

        if(msg.startsWith("block")){
            Log.e("DEBUG BLOCK",msg);
            if(split[2].equals(name)){
                textView.setText(split[1]+" blocked your "+split[3]);
                if(split[3].equals(assassinateTxt))
                    player=game.updatePlayerCoins(name, -3);
                coins.setText(coinsTxt +player.getCoins());
                enableNext();
                disableAll();
            }
            else{
                textView.setText(split[1]+" blocked "+split[2]+"'s "+split[3]);
                if(split[3].equals(assassinateTxt))
                    updateCoins(split[2], -3);

            }

        }

        if(msg.startsWith("bfa")){

                    if(split[2].equals(name)){
                        textView.setText(split[1]+" blocked foreign aid on you");
                        foreignAidBlocked = true;
                    }else textView.setText(split[1]+" blocked foreign aid on "+split[2]);

        }
        if(msg.startsWith(stealTxt)){

                    //stealing player
                    if(split[1].equals(name)){

                        player=game.updatePlayerCoins(name, 2);

                        disableAll();
                        textView.setText("You stole from " + attackedPlayer.getName());
                        updateCoins(attackedPlayer.getName(), -2);
                        coins.setText(coinsTxt + player.getCoins());

                    }
                    else //stolen player
                        if(split[2].equals(name)) {

                            if(player.hasCard(CardType.CAPTAIN)){
                                blockAction(split[1], stealTxt);
                            }
                            else{
                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        connection.sendMessage("fail");
                                    }
                                });
                                thread.start();

                                textView.setText(split[1]+" used steal on you");
                                updateCoins(split[1],2);

                                player= game.updatePlayerCoins(name, -2);

                                coins.setText(coinsTxt +player.getCoins());

                            }

                        }
                        else{
                            textView.setText(split[1]+" used steal on "+split[2]);
                            updateCoins(split[1],2);
                            updateCoins(split[2],-2);

                        }

        }

        if(msg.startsWith("cheat")){

//
                    updateCoins(split[1],3);



        }
        if(msg.startsWith(exchangeTxt)){


                    textView.setText(split[1]+" used exchange");


        }
        if(msg.startsWith(assassinateTxt)){


                    if(split[1].equals(name)){
                        disableAll();
                        enableNext();
                        textView.setText("You used assassinate on "+attackedPlayer.getName());

                        player=game.updatePlayerCoins(name, -3);

                        Log.e("DEBUG ASSASINATE", ""+player.getCoins());

                        coins.setText(coinsTxt +player.getCoins());

                        decreaseInfluence(split[2]);

                    }
                    else
                    if(split[2].equals(name)){

                        if(player.hasCard(CardType.CONTESSA))
                            blockAction(split[1], assassinateTxt);
                        else{

                            textView.setText(split[1]+" used assassinate on you");
                            mainPlayerChoosesCardToLose("assassinate");

                        }

                        updateCoins(split[1], -3);

                    }
                    else{
                        textView.setText(split[1]+" used assassinate on "+split[2]);
                        updateCoins(split[1], -3);
                    }

        }

        if (msg.startsWith("sCheat")){

            if(split[2].equals(name)){

                if(player.getCheated()){
                    sendToServer("sRight"+" "+split[1]+" "+split[2]);
                    player=game.updatePlayerCoins(name, -player.getCoins());
                    coins.setText(coinsTxt+player.getCoins());
                    textView.setText("You got caught cheating");

                }
                else{
                    sendToServer("sWrong"+" "+split[1]+" "+split[2]);
                    updateCoins(split[1],0);
                    textView.setText(split[1]+" suspected you cheating");

                }

            }

        }

        if(msg.startsWith("sRight")){

            if(split[1].equals(name)){

                textView.setText("You caught "+split[2]+" cheating");
                updateCoins(split[2], 0);

            }
            else {
                textView.setText(split[1]+" caught "+split[2]+" cheating");
                updateCoins(split[2], 0);

            }


        }

        if(msg.startsWith("sWrong")){

            if(split[1].equals(name)){

                textView.setText("You incorrectly suspected "+split[2]+" cheating");
                player=game.updatePlayerCoins(name,0);
                coins.setText(coinsTxt+player.getCoins());

            }
            else {
                textView.setText(split[1]+" incorrectly suspected "+split[2]+" cheating");
                updateCoins(split[2], 0);

            }


        }



        if (msg.startsWith("lastaction")) {
            Log.e("DEBUG LASTACTION", msg);

                    challengePlayer(split[1], split[2]);

        }

        if (msg.startsWith(challengeTxt)) {
            Log.e("DEBUG CHALLENGE", msg);


            if (split[2].equals(name)) {

                //check if player has card in hand

                Log.e("DEBUG ACTION", split[3]);
                if (split[3].equals("tax")) {
                    if (player.hasCard(CardType.DUKE)) {
                        cardInHand = true;
                        cardNameToShow = dukeTxt;
                    }
                }
                if (split[3].equals("bfa")) {
                    if (player.hasCard(CardType.DUKE)) {
                        cardInHand = true;
                        cardNameToShow = dukeTxt;
                    }
                }
                if (split[3].equals(stealTxt)) {
                    if (player.hasCard(CardType.CAPTAIN)) {
                        cardInHand = true;
                        cardNameToShow = captainTxt;
                    }
                }
                if (split[3].equals(assassinateTxt)) {
                    if (player.hasCard(CardType.ASSASSIN)) {
                        cardInHand = true;
                        cardNameToShow = assassinTxt;
                    }
                }
                if (split[3].equals(exchangeTxt)) {
                    if (player.hasCard(CardType.AMBASSADOR)) {
                        cardInHand = true;
                        cardNameToShow = ambassadorTxt;
                    }
                }


                challengeConfirmation();


            } else {

                textView.setText(split[1] + " challenged " + split[2]);

            }


        }

        if (msg.startsWith("show card")) {


                    if(turn)
                        enableNext();
                    textView.setText(split[2] + " has card " + split[3]);


        }

        if (msg.startsWith(looseCardTxt)) {


                    if(turn){
                        enableNext();
                        // next.setBackgroundColor(Color.GREEN);
                    }

                    textView.setText(split[2] + " lost an influence");

                    decreaseInfluence(split[2]);

        }


        if (msg.startsWith("lostgame")) {


                    if(turn)
                        enableNext();
                    textView.setText(split[1] + " lost the game");
                    removeOpponent(split[1]);
                    opponents.remove(split[1]);

                    game.removePlayer(split[1]);


        }

        if(msg.startsWith("coup")){


                    updateCoins(split[1],-7);
                    if(split[2].equals(name)){

                        textView.setText(split[1]+" used coup on you");
                        mainPlayerChoosesCardToLose("coup");


                    }
                    else
                        textView.setText(split[1]+" used coup on "+split[2]);
        }

    }

    /****************AsynTask classes********/

    //Connect to Server and finally enable Action-Buttons

    private class ConnectTask extends AsyncTask<Void,Void,String> {


        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute(){
            progressDialog = ProgressDialog.show(InGame.this, "Starting game", "searching for players");

            disableNext();
            textView.setVisibility(View.INVISIBLE);
            opponents = new LinkedList<>();


        }



        @Override
        protected String doInBackground(Void... voids) {

            String msg;

            try{

                connection.connect("se2-demo.aau.at",53214);
                msg = connection.getMessage();

                if(msg.equals("ok")){
                    connection.sendMessage(name);


                    //get player names
                    playernames = new LinkedList<>();
                    msg=connection.getMessage();

                    //message like: playername player95
                    String[] split = msg.split(" ");

                    while (msg.startsWith("playername")){
                        playernames.add(split[1]);
                        msg=connection.getMessage();
                        split=msg.split(" ");

                    }

                    List<Player> players = new LinkedList<>();

                    for(String playername: playernames){
                        Player p = new Player();
                        p.setName(playername);
                        players.add(p);
                    }


                    Log.e("DEBUG CONNECTTAST", ""+playernames.size());
                    game = new Game(players);


                    for(String playername: playernames){
                        if(playername.equals(name))
                            continue;
                        opponents.add(playername);
                    }


                    //get associated card names from server
                    List<String> cardnames = new LinkedList<>();

                    while(msg.startsWith("card")){
                        Log.e(debugTXT, msg);
                        split=msg.split(" ");
                        cardnames.add(split[1]);
                        msg=connection.getMessage();
                    }


                    //get starting cards
                    List<Card> cards = convertStringNameToCardType(cardnames);

                    //set starting cards
                    for(Player p : game.getPlayers())
                        if(p.getName().equals(name)){
                            p.setCards(cards);
                            player=p;
                        }

                    Log.e("DEBUG CONNECTTAST", ""+opponents.size());

                }

            }
            catch (IOException e){
                e.printStackTrace();
                msg=null;


            }

            return msg;

        }
        @Override
        protected void onPostExecute(String res){

            //no server connection
            if(res==null){
                Toast.makeText(InGame.this,"Cannot reach server! Try again.",Toast.LENGTH_SHORT).show();
                finish();
                progressDialog.dismiss();
                return;

            }

            //Enable/Disable functions for player on turn/wait

            if(res.equals("turn")||res.startsWith("wait")){
                Toast.makeText(InGame.this,"Connected",Toast.LENGTH_SHORT).show();

                setUp(res);


            }

            //no player found
            if(res.equals("noplayer")){
                Toast.makeText(InGame.this,"No players found. Try again.",Toast.LENGTH_SHORT).show();

                finish();

            }


            progressDialog.dismiss();


        }
    }

    protected void setUp(String res) {
        next.setVisibility(View.VISIBLE);

        textView.setVisibility(View.VISIBLE);


        initializeOpponents(opponents);

        String[] split = res.split(" ");

        if(res.equals("turn")){
            enableAll();
            enableNext();
            textView.setText("Your turn");
            turn = true;
            timer.setVisibility(View.VISIBLE);
            countDown.start();

        }
        else{
            disableAll();
            disableNext();
            updateOpponentOnTurn(split[1]);
            timer.setVisibility(View.INVISIBLE);
            textView.setText(split[1]+"'s turn");

        }

        settingCardImagesAtStartOfGame();


        ReadTask read = new ReadTask();
        read.execute();


        Log.e(debugTXT, "SUCCESS");
    }

    protected List<Card> convertStringNameToCardType(List<String> cardnames) {
        List<Card> cards = new LinkedList<>();

        for(String cardname: cardnames){

            if(cardname.equals(contessaTxt))
                cards.add(new Card(CardType.CONTESSA));
            if(cardname.equals(dukeTxt))
                cards.add(new Card(CardType.DUKE));
            if(cardname.equals(captainTxt))
                cards.add(new Card(CardType.CAPTAIN));
            if(cardname.equals(ambassadorTxt))
                cards.add(new Card(CardType.AMBASSADOR));
            if(cardname.equals(assassinTxt))
                cards.add(new Card(CardType.ASSASSIN));

        }
        return cards;
    }

    //listening in Background and interacting with UI
    private class ReadTask extends AsyncTask<Void, Void, String>{


        @Override
        protected String doInBackground(Void... voids) {
            String msg=null;

            try {
                while (true){
                    msg=connection.getMessage();

                    Log.e("MESSAGE", msg);

                    if(msg==null||msg.equals("win")||msg.equals("lose"))
                        break;

                    final String[] split = msg.split(" ");

                    final String message=msg;

                    if(msg.startsWith("card")){
                        cardsToChoose = new ArrayList<>();
                        ArrayList<String> cardnames = new ArrayList<>();
                        Log.e(debugTXT, msg);
                        cardnames.add(split[1]);
                        msg=connection.getMessage();

                        String[] second = msg.split(" ");
                        cardnames.add(second[1]);



                        cardsToChoose= convertStringNameToCardType(cardnames);

                    }else
                        runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            handleMessage(message,split);
                        }
                    });

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return msg;
        }

        protected void onPostExecute(String res){

            if(res==null){
                Toast.makeText(InGame.this,"Connection lost",Toast.LENGTH_SHORT).show();
                finish();
                return;

            }

            //switch to Aftergame on win
            if(res.equals("win")||res.equals("lose")){
                try {
                    connection.disconnect();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                finishGame(res);
            }



        }
    }

    protected void finishGame(String res) {

        Intent i = new Intent(InGame.this, AfterGame.class);
        if(res.equals("win"))
            i.putExtra("result", "win");
        else
            i.putExtra("result", "lose");
        startActivity(i);
    }

}








