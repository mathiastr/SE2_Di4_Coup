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
import java.util.Timer;

public class InGame extends Activity implements SensorEventListener {


    Player player,attackedPlayer;
    Game game;
    ArrayList<Card> cardsToChoose, choosenCard;
    ImageView ivImageC1, ivImageC2, ivImageC3, ivImageC4,ivOpp1,ivOpp2,ivOpp3;
    int count;
    private boolean leftCardRemoved, rightCardRemoved,cardInHand,challengeAccepted, challengeDenied, foreignAidBlocked;
    private String name,cardNameToShow;
    private List<String> opponents,playernames;
    protected ServerConnection connection;
    private Handler handler;
    protected Button Assasinate,Tax,Steal,Exchange,Income,Foreign_Aid,Coup,chooseCards,next,challenge,Block_Foreign_Aid;
    private SensorManager s;
    private Sensor Accelerometer;
    private float current,last,shake;
    private TextView coins,tvOpp1name,tvOpp2name,tvOpp3name,tvOpp1coins,tvOpp2coins,tvOpp3coins,timer,textView;
    private CountDownTimer countDown,challengeTimer;
    private List<TextView> enemyTv, coinsTv;
    private List<ImageView> enemyIv;
    private String COINS = "Your coins: ";
    private boolean[] notInGame = new boolean[3];


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
        Block_Foreign_Aid = findViewById(R.id.button_blockforeignaid);

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

                sendToServer("next");

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        next.setEnabled(false);
                        foreignAidBlocked=false;
                        countDown.cancel();
                        timer.setText("");
                    }
                });



            }
        });


        Income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToServer("income" + " " + name);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        player=game.updatePlayerCoins(name, 1);
                        disableAll();
                        textView.setText("You did income");
                        coins.setText(COINS + player.getCoins());
                    }
                });


            }
        });

        Foreign_Aid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToServer("foreignaid" + " " + name);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        player=game.updatePlayerCoins(name, 2);
                        disableAll();
                        textView.setText("You did foreign aid");
                        coins.setText(COINS + player.getCoins());
                    }
                });


            }
        });
        Exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToServer("exchange" + " " + name);

                player= game.getPlayerByName(name);

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
                sendToServer("tax" + " " + name);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        player = game.updatePlayerCoins(name, 3);
                        disableAll();
                        textView.setText("You did tax");
                        coins.setText(COINS + player.getCoins());
                    }
                });


            }
        });
        Steal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectPlayer("steal");


            }
        });

        Assasinate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(player.getCoins()<3)
                    textView.setText("You need 3 coins to use assassinate");
                else
                    selectPlayer("assassinate");
            }
        });

        Coup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectPlayer("coup");
            }
        });


        challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendToServer("challenge");
            }
        });

        Block_Foreign_Aid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPlayer("bfa");
            }
        });


    }

    private void selectPlayer(final String action){

        final AlertDialog.Builder builder = new AlertDialog.Builder(InGame.this);
        builder.setTitle("Choose Player");

        // add a list
        final String[] players = opponents.toArray(new String[opponents.size()]);
        builder.setItems(players, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                attackedPlayer = game.getPlayerByName(players[which]);

                if(action.equals("bfa"))
                    blockForeignAid();
                if(action.equals("steal"))
                    stealFromPlayer();
                if(action.equals("coup"))
                    doCoup();
                if(action.equals("assassinate"))
                    assasinateplayer();


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

    private void blockForeignAid(){

        sendToServer("bfa" + " " + name + " " + attackedPlayer.getName());

        handler.post(new Runnable() {
                    @Override
                    public void run() {
                        disableAll();
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
                            coins.setText(COINS + player.getCoins());
                        }
                    });



            }

        }
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public void challengePlayer(final String lastPlayer, final String lastAction) {

        boolean challengable = lastAction.equals("assassinate") || lastAction.equals("tax")
                || lastAction.equals("steal") || lastAction.equals("exchange") || lastAction.equals("bfa");


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //TODO: Add w hat happened last turn to text.

        if (challengable) {

            builder.setMessage("Are you sure you want to challenge " + lastAction + " on " + lastPlayer + " ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                            sendToServer("challenge" + " " + name + " " + lastPlayer + " " + lastAction);

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
                            dialog.cancel();
                        }
                    });

        }

        AlertDialog challengeDialog = builder.create();

        challengeDialog.show();


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


                        sendToServer("show card" + " "+ name+ " " + cardNameToShow);


                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText("Challenge successful");
                            }
                        });

                        challengeAccepted = true;
                        cardInHand=false;
                        dialogInterface.dismiss();



                    }
                })

                .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendToServer("loose card" + " " + name);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                mainPlayerChoosesCardToLose();

                            }
                        });

                        challengeDenied = true;



                        dialogInterface.dismiss();
                    }
                });


        final AlertDialog challengeDialog = builder.create();


        Log.e("DEBUG CARDINHAND", "" + cardInHand);



        challengeDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {


                if (!cardInHand)
                    challengeDialog.getButton(challengeDialog.BUTTON_POSITIVE).setEnabled(false);

                challengeTimer = new CountDownTimer(10000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        challengeDialog.setMessage("Hurry up :" + millisUntilFinished / 1000);
                    }

                    @Override
                    public void onFinish() {

                        if(!challengeAccepted && !challengeDenied)
                            challengeDialog.getButton(DialogInterface.BUTTON_NEGATIVE).callOnClick();

                    }
                };

                challengeTimer.start();

            }
        });

        challengeDialog.show();

    }

    public void stealFromPlayer() {

        if(attackedPlayer.getCoins()<2)
            textView.setText(attackedPlayer.getName()+" has not enough coin to steal from" );
        else {

            sendToServer("steal" + " " + name + " " + attackedPlayer.getName());

        }

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
        Block_Foreign_Aid.setEnabled(false);
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
            if(foreignAidBlocked)
                Foreign_Aid.setEnabled(false);
            else
                Foreign_Aid.setEnabled(true);
            Exchange.setEnabled(true);
            Steal.setEnabled(true);
            Tax.setEnabled(true);

            Block_Foreign_Aid.setEnabled(true);

            challenge.setEnabled(true);
        }
    }

    private void doCoup() {

        sendToServer("coup" + " " + name + " " + attackedPlayer.getName());

        handler.post(new Runnable() {
            @Override
            public void run() {
                disableAll();
                player = game.updatePlayerCoins(name, -7);
                next.setEnabled(true);
                textView.setText("You did coup on " + attackedPlayer.getName());
                coins.setText("Your coins: " + player.getCoins());
            }
        });


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

        enemyIv = new LinkedList<>();
        enemyIv.add(ivOpp1);
        enemyIv.add(ivOpp2);
        enemyIv.add(ivOpp3);


    }

    public void updateCoins(String onPlayer, int coinsAdded){

        for(int i=0; i<enemyTv.size();i++){
            if(enemyTv.get(i).getText().equals(onPlayer)){
                Player p = game.updatePlayerCoins(onPlayer, coinsAdded);
                coinsTv.get(i).setText(Integer.toString(p.getCoins()));
            }
        }

    }


    public void removeOpponent(String onPlayer){

        for(int i=0; i<enemyTv.size();i++){
            if(enemyTv.get(i).getText().equals(onPlayer)){
                if(i==0){
                    ivOpp1.setImageResource(R.drawable.enemy_one_dead);
                }
                if(i==1){
                    ivOpp2.setImageResource(R.drawable.enemy_two_dead);
                }
                if(i==2){
                    ivOpp3.setImageResource(R.drawable.enemy_three_passive);
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


            sendToServer(getCardNameAsString(cardToReturn));
        }
        else returnLastCard();
    }

    private void returnLastCard(){

        final Card cardToReturn = player.getCards().get(0);

        sendToServer("lastcard"+" "+getCardNameAsString(cardToReturn));


    }

    private String getCardNameAsString(Card cardToReturn) {
        switch (cardToReturn.getTypeOfCard()){
            case DUKE: return "duke";
            case CAPTAIN: return "captain";
            case ASSASSIN: return "assassin";
            case CONTESSA: return "contessa";
            case AMBASSADOR: return "ambassador";
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

        sendToServer("assassinate"+" "+name+" "+attackedPlayer.getName());

    }

    private void blockAction(final String attacker, final String action){

        sendToServer("block");

        handler.post(new Runnable() {
            @Override
            public void run() {
                if(action.equals("steal"))
                    textView.setText("You blocked "+attacker+" stealing from you");
                else textView.setText("You blocked "+attacker+ " assassinating you");
            }
        });
    }

    private void sendToServer(final String message){

        new Thread(new Runnable() {
            @Override
            public void run() {
                connection.sendMessage(message);
            }
        }).start();

    }

    /****************AsynTask classes********/

    //Connect to Server and finally enable Action-Buttons

    private class ConnectTask extends AsyncTask<Void,Void,String> {


        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute(){
            progressDialog = ProgressDialog.show(InGame.this, "Starting game", "searching for players");

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

            if(res.equals("turn")||res.startsWith("wait")){
                Toast.makeText(InGame.this,"Connected",Toast.LENGTH_SHORT).show();

                next.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);


                initializeOpponents(opponents);

                String[] split = res.split(" ");

                if(res.equals("turn")){
                    enableAll();
                    next.setEnabled(true);
                    textView.setText("Your turn");
                    timer.setVisibility(View.VISIBLE);
                    countDown.start();

                }
                else{
                    disableAll();
                    updateOpponentOnTurn(split[1]);
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

    //listening in Background and interacting with UI
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
                                updateOpponentOnTurn(name);

                            }
                        });
                    }

                    if(msg.startsWith("nextplayer")){

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(split[1]+"'s turn");
                                updateOpponentOnTurn(split[1]);
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

                    if(msg.startsWith("block")){
                        Log.e("DEBUG BLOCK",msg);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(split[2].equals(name)){
                                    textView.setText(split[1]+" blocked your "+split[3]);
                                    if(split[3].equals("assassinate"))
                                        player=game.updatePlayerCoins(name, -3);
                                    coins.setText(COINS+player.getCoins());
                                    disableAll();
                                }
                                else{
                                    textView.setText(split[1]+" blocked "+split[2]+"'s "+split[3]);
                                    if(split[3].equals("assassinate"))
                                        updateCoins(split[2], -3);

                                }
                            }
                        });
                    }

                    if(msg.startsWith("bfa")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(split[2].equals(name)){
                                    textView.setText(split[1]+" blocked foreign aid on you");
                                    foreignAidBlocked = true;
                                }else textView.setText(split[1]+" bocked foreign aid on "+split[2]);
                            }
                        });
                    }


                    if(msg.startsWith("steal")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //stealing player
                                if(split[1].equals(name)){

                                    player=game.updatePlayerCoins(name, 2);

                                    disableAll();
                                    textView.setText("You stole from " + attackedPlayer.getName());
                                    updateCoins(attackedPlayer.getName(), -2);
                                    coins.setText(COINS + player.getCoins());

                                }
                                else //stolen player
                                    if(split[2].equals(name)) {

                                        if(player.hasCard(CardType.CAPTAIN)){
                                            blockAction(split[1], "steal");
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

                                            coins.setText(COINS+player.getCoins());

                                        }

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

                                if(split[1].equals(name)){
                                    disableAll();
                                    next.setEnabled(true);
                                    textView.setText("You used assassinate on"+attackedPlayer.getName());

                                    player=game.updatePlayerCoins(name, -3);

                                    Log.e("DEBUG ASSASINATE", ""+player.getCoins());

                                    coins.setText(COINS+player.getCoins());

                                }
                                else
                                if(split[2].equals(name)){

                                    if(player.hasCard(CardType.CONTESSA))
                                        blockAction(split[1], "assassinate");
                                    else{

                                        textView.setText(split[1]+" used assassinate on you");
                                        mainPlayerChoosesCardToLose();

                                    }

                                    updateCoins(split[1], -3);

                                }
                                else{
                                    textView.setText(split[1]+" used assassinate on "+split[2]);
                                    updateCoins(split[1], -3);
                                }

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

                    if (msg.startsWith("lastaction")) {
                        Log.e("DEBUG LASTACTION", msg);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                challengePlayer(split[1], split[2]);
                            }
                        });
                    }

                    if (msg.startsWith("challenge")) {
                        Log.e("DEBUG CHALLENGE", msg);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                if (split[2].equals(name)) {

                                    //check if player has card in hand

                                    Log.e("DEBUG ACTION", split[3]);
                                    if (split[3].equals("tax")) {
                                        if (player.hasCard(CardType.DUKE)) {
                                            cardInHand = true;
                                            cardNameToShow = "duke";
                                        }
                                    }
                                    if (split[3].equals("bfa")) {
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
                                    if (split[3].equals("exchange")) {
                                        if (player.hasCard(CardType.AMBASSADOR)) {
                                            cardInHand = true;
                                            cardNameToShow = "ambassador";
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
                                textView.setText(split[2] + " showed card " + split[3]);
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

                                game.removePlayer(split[1]);
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








