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


    private Button next;
    private TextView timer; //Change to TextView Timer
    private TextView textView;
    private Button challenge;

    Player player;
    Game game;
    //private Button next, surrender, challenge;
    //private TextView textView; //Change to TextView Timer
    ArrayList<Card> cardsToChoose, choosenCard, cardsToReturn;
    ArrayList<String> cardNamesToReturn;
    Card c1, c2, c3, c4;
    ImageView ivImageC1, ivImageC2, ivImageC3, ivImageC4;
    int count;
    boolean leftCardRemoved, rightCardRemoved;

    private String name;
    private List<String> opponents;
    private ServerConnection connection;
    private List<String> playernames;
    private Handler handler;


    //Action buttons
    private Button Assasinate;
    private Button Tax;
    private Button Steal;
    private Button Exchange;
    private Button Income;
    private Button Foreign_Aid;
    private Button Coup;
    private Button chooseCards;


    Action ChoosenAktion;
    Player attackedPlayer;
    private SensorManager s;
    private Sensor Accelerometer;
    private float current;
    private float last;
    private float shake;



    // should return choosen Action and attacked Player

    //textviews

    private TextView coins;

    private TextView tvOpp1name;
    private TextView tvOpp2name;
    private TextView tvOpp3name;

    private TextView tvOpp1cards;
    private TextView tvOpp2cards;
    private TextView tvOpp3cards;

    private TextView tvOpp1coins;
    private TextView tvOpp2coins;
    private TextView tvOpp3coins;

    private ImageView ivOpp1 ;
    private ImageView ivOpp2;
    private ImageView ivOpp3;


    private CountDownTimer countDown;

    //Challenge variables
    private boolean cardInHand;
    private String cardNameToShow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ingame);

        s = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Accelerometer = s.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        s.registerListener(this, Accelerometer , SensorManager.SENSOR_DELAY_NORMAL);

        ivOpp1 = findViewById(R.id.imageView_enemy_one);
        ivOpp2 = findViewById(R.id.imageView_enemy_two);
        ivOpp3 = findViewById(R.id.imageView_enemy_three);


        Bundle b = getIntent().getExtras();
        if (b != null)
            name = b.getString("name");

        challenge = (Button) findViewById(R.id.button_challenge);
        next = findViewById(R.id.button_next);
        timer = findViewById(R.id.textView_timer);
        textView = findViewById(R.id.textView_action);
        Income = findViewById(R.id.button_income);
        coins = findViewById(R.id.textView_coins);
        Foreign_Aid = findViewById(R.id.button_foreign_aid);
        Exchange = findViewById(R.id.button_exchange);

        chooseCards = (Button) findViewById(R.id.btnOK);

        //Time methods - optimise time after playing game. Either speed up or slow down.
        countDown = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                timer.setText("Your turn: " + millisUntilFinished / 1000);
            }

            public void onFinish() {

                disableAll();

            }
        };


        connection = new ServerConnection();

        handler = new Handler();


        Assasinate = (Button) findViewById(R.id.button_assassinate);
        Tax = (Button) findViewById(R.id.button_tax);
        Steal = (Button) findViewById(R.id.button_steal);
        Exchange = (Button) findViewById(R.id.button_exchange);
        Income = (Button) findViewById(R.id.button_income);
        Foreign_Aid = (Button) findViewById(R.id.button_foreign_aid);
        Coup = (Button) findViewById(R.id.button_coup);




        ConnectTask connectTask = new ConnectTask();
        connectTask.execute();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        connection.sendMessage("next");
                    }
                });

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        next.setEnabled(false);
                        countDown.cancel();
                        timer.setText("Turn over");
                    }
                });


                thread.start();
            }
        });


        Income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        connection.sendMessage("income" + " " + name);
                        //look for me in player list
                        for (Player me : game.getPlayers())
                            if (me.getName().equals(name)) {
                                me.setCoins(me.getCoins() + 1);
                                player = me;

                            }

                    }
                });
                thread.start();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        disableAll();
                        textView.setText("You did income");
                        coins.setText("Your coins: " + player.getCoins());
                    }
                });


            }
        });

        Foreign_Aid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        connection.sendMessage("foreignaid" + " " + name);
                        //look for me in player list
                        for (Player me : game.getPlayers())
                            if (me.getName().equals(name)) {
                                me.setCoins(me.getCoins() + 2);
                                player = me;

                            }

                    }
                });

                thread.start();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        disableAll();
                        textView.setText("You did foreign aid");
                        coins.setText("Your coins: " + player.getCoins());
                    }
                });


            }
        });
        Exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        connection.sendMessage("exchange" + " " + name);
                        //look for me in player list
                        for (Player me : game.getPlayers())
                            if (me.getName().equals(name)) {
                                player = me;
                            }

                    }
                });

                thread.start();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        showCardsToExchange();
                    }
                }, 250);

            }
        });
        Tax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        connection.sendMessage("tax" + " " + name);
                        //look for me in player list
                        for (Player me : game.getPlayers())
                            if (me.getName().equals(name)) {
                                me.setCoins(me.getCoins() + 3);
                                player = me;
                            }

                    }
                });

                thread.start();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        disableAll();
                        textView.setText("You did tax");
                        coins.setText("Your coins: " + player.getCoins());
                    }
                });


            }
        });
        Steal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stealFromPlayer();


            }
        });

        Assasinate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(player.getCoins()<2)
                    textView.setText("You need 2 coins to use assassinate");
                else
                    assasinateplayer();
            }
        });

        Coup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doCoup();
            }
        });


        challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                challengePlayer();
            }
        });


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
                            coins.setText("Your coins: " + player.getCoins());
                        }
                    });



            }

        }
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public void challengePlayer() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //TODO: Add what happened last turn to text.
        builder.setMessage("Are you sure you want to challenge the last action?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                connection.sendMessage("challenge" + " " + name);
                            }
                        });

                        thread.start();

                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                textView.setText("You did challenge");

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

        AlertDialog challengeDialog = builder.create();

        challengeDialog.show();


    }

    public void challengeConfirmation() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //TODO: Add what happened last turn to text.
        builder.setMessage("You have been challenged! Choose an option")
                .setCancelable(false)
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                connection.sendMessage("show card" + " " + name + " " + cardNameToShow);
                            }
                        });

                        thread.start();

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText("Challenge successful");
                            }
                        });

                        cardInHand=false;
                        dialogInterface.dismiss();


                    }
                })

                .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });

                        thread.start();

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //mainPlayerChoosesCardToLose();

                            }
                        });

                        dialogInterface.dismiss();
                    }
                });

        final AlertDialog challengeDialog = builder.create();

        //if player doesn't have the card in hand disable show card
        challengeDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {

                challengeDialog.getButton(challengeDialog.BUTTON_POSITIVE).setEnabled(false);

                CountDownTimer timer = new CountDownTimer(10000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        challengeDialog.setMessage("Hurry up!: " + millisUntilFinished / 1000);
                    }

                    @Override
                    public void onFinish() {

                        challengeDialog.getButton(challengeDialog.BUTTON_NEGATIVE).callOnClick();

                    }
                };
                timer.start();
            }
        });

        challengeDialog.show();


    }

    public void stealFromPlayer() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(InGame.this);
        builder.setTitle("Choose Player");

        // add a list
        final String[] players = opponents.toArray(new String[opponents.size()]);
        builder.setItems(players, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for (Player p : game.getPlayers()) {
                    if (p.getName().equals(players[which])) {
                        attackedPlayer = p;
                    }
                }

                if(attackedPlayer.getCoins()<2)
                    textView.setText(attackedPlayer.getName()+" has not enough coin to steal from" );
                else {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            connection.sendMessage("steal" + " " + name + " " + attackedPlayer.getName());
                            //look for me in player list

                            for (Player p : game.getPlayers()) {
                                if (p.getName().equals(name)) {
                                    p.setCoins(p.getCoins() + 2);
                                    player = p;
                                }

                            }


                        }
                    });

                    thread.start();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            disableAll();
                            textView.setText("You stole from " + attackedPlayer.getName());
                            updateCoins(attackedPlayer.getName(), -2);
                            coins.setText("Your coins: " + player.getCoins());
                        }
                    });
                }




            }

        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();


    }

    public void disableAll(){
        Assasinate.setEnabled(false);
        Income.setEnabled(false);
        Foreign_Aid.setEnabled(false);
        Exchange.setEnabled(false);
        Steal.setEnabled(false);
        Tax.setEnabled(false);
        Coup.setEnabled(false);
        challenge.setEnabled(false);


    }

    public void enableAll() {

        if(player.getCoins()<7)
            Coup.setEnabled(false);
        else Coup.setEnabled(true);

        if(player.getCoins()>=10){
            disableAll();
            Coup.setEnabled(true);
        }
        else{
            Assasinate.setEnabled(true);
            Income.setEnabled(true);
            Foreign_Aid.setEnabled(true);
            Exchange.setEnabled(true);
            Steal.setEnabled(true);
            Tax.setEnabled(true);

            challenge.setEnabled(true);
        }




    }


    private void doCoup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(InGame.this);
        builder.setTitle("Choose Player for Coup");

// add a list
        final String[] players = opponents.toArray(new String[opponents.size()]);
        builder.setItems(players, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for (Player p : game.getPlayers()) {
                    if (p.getName().equals(players[which])) {
                        attackedPlayer = p;
                    }
                }

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        connection.sendMessage("coup" + " " + name + " " + attackedPlayer.getName());

                        for (Player me : game.getPlayers())
                            if (me.getName().equals(name)) {
                                me.setCoins(me.getCoins() -7);
                                player = me;
                            }

                    }
                });

                thread.start();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        disableAll();
                        next.setEnabled(true);
                        textView.setText("You did coup on " + attackedPlayer.getName());
                        coins.setText("Your coins: " + player.getCoins());
                    }
                });

            }

        });

// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();


    }



    public void updateOpponentInfluence(Player player){
        TextView numOfCards=null;

        TextView tvOpp1name=(TextView) findViewById(R.id.textView_name_enemy_one);
        TextView tvOpp2name=(TextView) findViewById(R.id.textView_name_enemy_two);
        TextView tvOpp3name=(TextView) findViewById(R.id.textView_name_enemy_three);

        TextView tvOpp1cards=(TextView) findViewById(R.id.textView_enemy_one_influence);
        TextView tvOpp2cards=(TextView) findViewById(R.id.textView_name_enemy_two);
        TextView tvOpp3cards=(TextView) findViewById(R.id.textView_name_enemy_three);

        if(player.getName().equals(tvOpp1name)){
            numOfCards=tvOpp1cards;
        }
        else if(player.getName().equals(tvOpp2name)){
            numOfCards=tvOpp2cards;
        }
        else if(player.getName().equals(tvOpp3name)){
            numOfCards=tvOpp3cards;
        }
        numOfCards.setText(player.getCards().size()-1);
    }
    //initialize opponent textviews
    public void initializeOpponents(List<String> opponents) {
        tvOpp1name=(TextView) findViewById(R.id.textView_name_enemy_one);
        tvOpp2name=(TextView) findViewById(R.id.textView_name_enemy_two);
        tvOpp3name=(TextView) findViewById(R.id.textView_name_enemy_three);

//        tvOpp1cards=(TextView) findViewById(R.id.opponentNumOfCards1);
//        tvOpp2cards=(TextView) findViewById(R.id.opponentNumOfCards2);
//        tvOpp3cards=(TextView) findViewById(R.id.opponentNumOfCards3);

        tvOpp1coins = findViewById(R.id.textView_enemy_one_coins);
        tvOpp2coins = findViewById(R.id.textView_enemy_two_coins);
        tvOpp3coins = findViewById(R.id.textView_enemy_three_coins);


        Log.e("DEBUG: ",opponents.get(0));

        tvOpp1name.setText(opponents.get(0));

        if(opponents.size()>2){
        tvOpp2name.setText(opponents.get(1));
        tvOpp3name.setText(opponents.get(2));
        }

        //tvOpp1cards.setText("2");
        //tvOpp2cards.setText("2");
        //tvOpp3cards.setText("2");

        tvOpp1coins.setText("2");
        tvOpp2coins.setText("2");
        tvOpp3coins.setText("2");


    }

    public void updateCoins(String onPlayer, int coinsAdded){
        //update coins for enemy 1
        if(tvOpp1name.getText().equals(onPlayer)){
            for(Player p: game.getPlayers()){
                if(p.getName().equals(onPlayer)){
                    p.setCoins(p.getCoins()+coinsAdded);
                    tvOpp1coins.setText(Integer.toString(p.getCoins()));
                }
            }

        }
        //update coins for enemy 2
        if(tvOpp2name.getText().equals(onPlayer)){
            for(Player p: game.getPlayers()){
                if(p.getName().equals(onPlayer)){
                    p.setCoins(p.getCoins()+coinsAdded);
                    tvOpp2coins.setText(Integer.toString(p.getCoins()));
                }
            }

        }
        //update coins for enemy 3
        if(tvOpp3name.getText().equals(onPlayer)){
            for(Player p: game.getPlayers()){
                if(p.getName().equals(onPlayer)){
                    p.setCoins(p.getCoins()+coinsAdded);
                    tvOpp3coins.setText(Integer.toString(p.getCoins()));
                }
            }

        }
    }


    public void removeOpponent(String onPlayer){



        //remove enemy 1
        if(tvOpp1name.getText().equals(onPlayer)){
            for(Player p: game.getPlayers()){
                if(p.getName().equals(onPlayer)){
                    ivOpp1.setBackgroundColor(Color.RED);
                    tvOpp1coins.setVisibility(View.INVISIBLE);
                }
            }

        }
        //remove enemy 2
        if(tvOpp2name.getText().equals(onPlayer)){
            for(Player p: game.getPlayers()){
                if(p.getName().equals(onPlayer)){
                    ivOpp2.setBackgroundColor(Color.RED);
                    tvOpp2coins.setVisibility(View.INVISIBLE);
                }
            }

        }
        //remove enemy 3
        if(tvOpp3name.getText().equals(onPlayer)){
            for(Player p: game.getPlayers()){
                if(p.getName().equals(onPlayer)){
                    ivOpp3.setBackgroundColor(Color.RED);
                    tvOpp3coins.setVisibility(View.INVISIBLE);
                }
            }

        }
    }

    public void mainPlayerChoosesCardToLose(){
        ivImageC1 = (ImageView) findViewById(R.id.card_playercard1);
        ivImageC2 = (ImageView) findViewById(R.id.card_playercard2);

        // Display: "Click on the Card you want to lose."

        textView.setText("Click on the Card you want to lose.");

        count =0;


        if (ivImageC1.isShown() && ivImageC2.isShown()) {

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

    private void looseCard(){

        Card c;

        int cardToRemove = 0;

        if(player.getCards().size()==2){
            if(leftCardRemoved){
                c=player.getCards().get(0);
                ivImageC1.setVisibility(View.INVISIBLE);
            }
            else {
                c=player.getCards().get(1);
                cardToRemove = 1;
                ivImageC2.setVisibility(View.INVISIBLE);
            }

            //update player
            for(Player me: game.getPlayers()){
                if(me.getName().equals(name)){
                    me.getCards().remove(cardToRemove);
                    player=me;
                }

            }

            textView.setText("You lost an influence");

            final Card cardToReturn = c;

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    switch (cardToReturn.getTypeOfCard()){
                        case DUKE: connection.sendMessage("duke");
                        case CAPTAIN: connection.sendMessage("captain");
                        case ASSASSIN: connection.sendMessage("assassin");
                        case CONTESSA: connection.sendMessage("contessa");
                        case AMBASSADOR: connection.sendMessage("ambassador");
                    }

                }
            });

            thread.start();
        }
        else returnLastCard();



    }

    private void returnLastCard(){

        final Card cardToReturn = player.getCards().get(0);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                switch (cardToReturn.getTypeOfCard()){
                    case DUKE: connection.sendMessage("lastcard duke");
                    case CAPTAIN: connection.sendMessage("lastcard captain");
                    case ASSASSIN: connection.sendMessage("lastcard assassin");
                    case CONTESSA: connection.sendMessage("lastcard contessa");
                    case AMBASSADOR: connection.sendMessage("lastcard ambassador");
                }

            }
        });

        thread.start();


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

        next.setEnabled(false);

        ivImageC1 = (ImageView) findViewById(R.id.card_playercard1);
        ivImageC2 = (ImageView) findViewById(R.id.card_playercard2);
        ivImageC3 = (ImageView) findViewById(R.id.card_playercard3);
        ivImageC4 = (ImageView) findViewById(R.id.card_playercard4);


        choosenCard = cardsToChoose;

        displayCards(cardsToChoose.get(1).getTypeOfCard(), ivImageC3);
        displayCards(cardsToChoose.get(0).getTypeOfCard(), ivImageC4);

        ivImageC3.setVisibility(View.VISIBLE);
        ivImageC4.setVisibility(View.VISIBLE);

        if(cardsToChoose==null)
            Log.e("DEBUG", "CARDS NULL");


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
                next.setEnabled(true);
                disableAll();
            }
        });


    }
    public ArrayList<String> convertCardTypeToStringName(List<Card> listOfCards){
        ArrayList<String> convertedList= new ArrayList<>();
        for(Card c :listOfCards) {
            if (c.getTypeOfCard().equals(CardType.ASSASSIN)) {
                convertedList.add("assassin");
            }
            if (c.getTypeOfCard().equals(CardType.DUKE)) {
                convertedList.add("duke");
            }
            if (c.getTypeOfCard().equals(CardType.CONTESSA)) {
                convertedList.add("contessa");
            }
            if (c.getTypeOfCard().equals(CardType.AMBASSADOR)) {
                convertedList.add("ambassador");
            }
            if (c.getTypeOfCard().equals(CardType.CAPTAIN)) {
                convertedList.add("captain");
            }
        }
    return convertedList;}


    private void assasinateplayer(){

        AlertDialog.Builder builder = new AlertDialog.Builder(InGame.this);
        builder.setTitle("Choose Player to assassinate");

// add a list
        final String[] players = opponents.toArray(new String[opponents.size()]);
        builder.setItems(players, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for(Player p: game.getPlayers()) {
                    if (p.getName().equals(players[which])) {
                        attackedPlayer = p;
                    }
                }

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        connection.sendMessage("assassinate"+" "+name+" "+attackedPlayer.getName());
                    }
                });

                thread.start();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        disableAll();
                        next.setEnabled(true);
                        textView.setText("You tried to assassinate "+attackedPlayer.getName());

                        for(Player me : game.getPlayers()){
                            if(me.getName().equals(name)){
                                me.setCoins(me.getCoins()-2);
                                player = me;
                            }
                        }

                    }
                });

            }

        });

// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();



    }

    /****************AsynTask classes********/

    //Connect to Server and finally enable Action-Buttons

    private class ConnectTask extends AsyncTask<Void,Void,String> {


        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute(){
            progressDialog = ProgressDialog.show(InGame.this, "Starting game", "connecting");

            next.setEnabled(false);
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
                        players.add(new Player(playername));
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
                        Log.e("DEBUG", msg);
                        split=msg.split(" ");
                        cardnames.add(split[1]);
                        msg=connection.getMessage();
                    }


                    //get starting cards
                    List<Card> cards = new LinkedList<>();

                    for(String cardname: cardnames){

                        if(cardname.equals("contessa"))
                            cards.add(new Card(CardType.CONTESSA));
                        if(cardname.equals("duke"))
                            cards.add(new Card(CardType.DUKE));
                        if(cardname.equals("captain"))
                            cards.add(new Card(CardType.CAPTAIN));
                        if(cardname.equals("ambassador"))
                            cards.add(new Card(CardType.AMBASSADOR));
                        if(cardname.equals("assassin"))
                            cards.add(new Card(CardType.ASSASSIN));

                    }

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

            if(res.equals("turn")||res.equals("wait")){
                Toast.makeText(InGame.this,"Connected",Toast.LENGTH_SHORT).show();

                next.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);


                initializeOpponents(opponents);


                if(res.equals("turn")){
                    enableAll();
                    next.setEnabled(true);
                    textView.setText("Your turn");
                    timer.setVisibility(View.VISIBLE);
                    countDown.start();

                }
                else{
                    disableAll();
                    timer.setVisibility(View.INVISIBLE);

                }

                settingCardImagesAtStartOfGame();


                ReadTask read = new ReadTask();
                read.execute();


                Log.e("DEBUG", "SUCCESS");


            }

            //no player found
            if(res.equals("noplayer")){
                Toast.makeText(InGame.this,"No players found. Try again.",Toast.LENGTH_SHORT).show();

                finish();

            }


            progressDialog.dismiss();


        }
    }

    //always listening in background and interacting with ui

    private class ReadTask extends AsyncTask<Void, Void, String>{


        @Override
        protected String doInBackground(Void... voids) {
            String msg=null;

            try {
                while (true){
                    msg=connection.getMessage();

                    if(msg==null||msg.equals("win")||msg.equals("lose"))
                        break;

                    final String[] split = msg.split(" ");

                    if(msg.equals("turn")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                enableAll();
                                next.setEnabled(true);
                                textView.setText("Your turn");
                                timer.setVisibility(View.VISIBLE);
                                countDown.start();

                            }
                        });
                    }

                    if(msg.startsWith("income")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                textView.setText(split[1]+" used income");
                                updateCoins(split[1], 1);
                            }
                        });
                    }
                    if(msg.startsWith("foreignaid")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                textView.setText(split[1]+" used Foreign Aid");
                                updateCoins(split[1], 2);
                            }
                        });
                    }

                    if(msg.startsWith("tax")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                textView.setText(split[1]+" used tax");
                                updateCoins(split[1], 3);
                            }
                        });
                    }

                    if(msg.startsWith("steal")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //attacked player
                                if(split[2].equals(name)) {
                                    textView.setText(split[1]+" used steal on you");
                                    updateCoins(split[1],2);
                                    for(Player me:game.getPlayers())
                                        if(me.getName().equals(name)){
                                            me.setCoins(me.getCoins()-2);
                                            player=me;
                                        }

                                    coins.setText("Your coins: "+player.getCoins());

                                }
                                else{
                                    textView.setText(split[1]+" used steal on "+split[2]);
                                    updateCoins(split[1],2);
                                    updateCoins(split[2],-2);

                                }

                            }
                        });


                    }

                    if(msg.startsWith("cheat")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                updateCoins();
                                updateCoins(split[1],3);
                            }
                        });


                    }
                    if(msg.startsWith("exchange")){

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(split[1]+" used Exchange");
                            }
                        });

                    }
                    if(msg.startsWith("assassinate")){

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(split[2].equals(name)){

                                    textView.setText(split[1]+" used assassinate on you");
                                    mainPlayerChoosesCardToLose();


                                }
                                else
                                    textView.setText(split[1]+" used assassinate on "+split[2]);

                                updateCoins(split[1], -2);

                            }
                        });
                    }


                    if(msg.startsWith("card")){
                        cardsToChoose = new ArrayList<>();
                        ArrayList<String> cardnames = new ArrayList<>();
                        Log.e("DEBUG", msg);
                        cardnames.add(split[1]);
                        msg=connection.getMessage();

                        String[] second = msg.split(" ");
                        cardnames.add(second[1]);


                        ArrayList<Card> list = new ArrayList<>();

                        for(String cardname: cardnames){

                            if(cardname.equals("contessa"))
                                cardsToChoose.add(new Card(CardType.CONTESSA));
                            if(cardname.equals("duke"))
                                cardsToChoose.add(new Card(CardType.DUKE));
                            if(cardname.equals("captain"))
                                cardsToChoose.add(new Card(CardType.CAPTAIN));
                            if(cardname.equals("ambassador"))
                                cardsToChoose.add(new Card(CardType.AMBASSADOR));
                            if(cardname.equals("assassin"))
                                cardsToChoose.add(new Card(CardType.ASSASSIN));

                        }



                    }

                    if (msg.startsWith("challenge")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                if (split[2].equals(name)) {

                                    //check if player has card in hand

                                    if (split[3].equals("tax")) {
                                        if (player.hasCard(CardType.DUKE)) {
                                            cardInHand = true;
                                            cardNameToShow = "duke";
                                        }
                                    }
                                    if (split[3].equals("steal")) {
                                        if (player.hasCard(CardType.CAPTAIN)) {
                                            cardInHand = true;
                                            cardNameToShow = "captain";
                                        }
                                    }
                                    if (split[3].equals("assassinate")) {
                                        if (player.hasCard(CardType.ASSASSIN)) {
                                            cardInHand = true;
                                            cardNameToShow = "assassin";
                                        }
                                    }

                                    challengeConfirmation();


                                } else {

                                    textView.setText(split[1] + " challenged " + split[2]);

                                }
                            }
                        });


                    }

                    if (msg.startsWith("show card")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(split[2] + " showed card");
                            }
                        });

                    }

                    if (msg.startsWith("loose card")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(split[2] + " lost an influence");
                            }
                        });

                    }


                    if (msg.startsWith("lostgame")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(split[1] + " lost the game");
                                removeOpponent(split[1]);

                                for(Player enemy:game.getPlayers())
                                    if(enemy.getName().equals(split[1]))
                                        game.getPlayers().remove(enemy);
                            }
                        });

                    }

                     if(msg.startsWith("coup")){

                         runOnUiThread(new Runnable() {
                             @Override
                             public void run() {
                                 updateCoins(split[1],-7);
                                 if(split[2].equals(name)){

                                     textView.setText(split[1]+" used coup on you");
                                     mainPlayerChoosesCardToLose();


                                 }
                                 else
                                     textView.setText(split[1]+" used coup on "+split[2]);

                             }
                         });

                     }



                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return msg;
        }

        protected void onPostExecute(String res){

            //switch to Aftergame on win
            if(res.equals("win")){

                try {
                    connection.disconnect();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent i = new Intent(InGame.this, AfterGame.class);
                i.putExtra("result", "win");
                startActivity(i);
            }

            //switch on lose
            if(res.equals("lose")){
                try {
                    connection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent i = new Intent(InGame.this, AfterGame.class);
                i.putExtra("result", "lose");
                startActivity(i);
            }




        }
    }






}
