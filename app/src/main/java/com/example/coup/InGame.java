package com.example.coup;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Image;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class InGame extends Activity {


    private Button next;
    private TextView timer; //Change to TextView Timer
    private TextView textView;
    private Button challenge;

    Player player;
    Game game;
    //private Button next, surrender, challenge;
    //private TextView textView; //Change to TextView Timer
    ArrayList<Card> cardsToChoose,choosenCard,cardsToReturn;
    ArrayList<String>  cardNamesToReturn;
    Card c1,c2,c3,c4;
    ImageView ivImageC1,ivImageC2,ivImageC3,ivImageC4;
    int count;

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


    Action ChoosenAktion;
    Player attackedPlayer;
    private SensorManager s;
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


    private CountDownTimer countDown;


    /*// should return choosen Action and attacked Player
    public Object[] next(Player CurrentPlayer){

            Object[] choosenAktion = new Object[2];
            choosenAktion[0]= ChoosenAktion;
            choosenAktion[1]=attackedPlayer;

        return  choosenAktion;
    }



    //should return Player who clicked challenge and needed CardType
    public Object[] waitForChallenge(){
        return null;
    }


    public boolean waitForBlock(List<Player> Playerscanblock){

        return false;
    }*/



    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ingame);


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

        //Time methods - optimise time after playing game. Either speed up or slow down.
        countDown = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                timer.setText("Your turn: " + millisUntilFinished / 1000);
            }

            public void onFinish() {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        connection.sendMessage("next");
                    }
                });

                thread.start();
                try {
                    thread.join();
                    timer.setText("Turn over.");

                    next.setEnabled(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        };


        connection = new ServerConnection();

        handler=new Handler();


        Assasinate = (Button)findViewById(R.id.button_assassinate);
        Tax = (Button)findViewById(R.id.button_tax);
        Steal = (Button)findViewById(R.id.button_steal);
        Exchange = (Button)findViewById(R.id.button_exchange);
        Income = (Button)findViewById(R.id.button_income);
        Foreign_Aid = (Button)findViewById(R.id.button_foreign_aid);
        Coup = (Button)findViewById(R.id.button_coup);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == Assasinate){
                    ChoosenAktion = Action.ASSASSINATE;
                    choosePlayer();
                }else if(v == Tax){
                    ChoosenAktion = Action.TAX;
                }else if(v == Steal){
                    ChoosenAktion = Action.STEAL;
                    choosePlayer();
                }else if(v == Exchange){
                    ChoosenAktion = Action.EXCHANGE;
                }else if(v == Income){
                    ChoosenAktion = Action.INCOME;
                }else if(v == Foreign_Aid){
                    ChoosenAktion = Action.FOREIGNAID;
                }else if(v == Coup){
                    ChoosenAktion = Action.COUP;
                    choosePlayer();
                }
            }
        };

        Assasinate.setOnClickListener(clickListener);
        Tax.setOnClickListener(clickListener);
        Steal.setOnClickListener(clickListener);
        Exchange.setOnClickListener(clickListener);
        Income.setOnClickListener(clickListener);
        Foreign_Aid.setOnClickListener(clickListener);
        Coup.setOnClickListener(clickListener);

        current = SensorManager.GRAVITY_EARTH;
        last = SensorManager.GRAVITY_EARTH;
        shake = 0.00f;




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



        // cheatfunktion: wenn man das Smartphone schüttelt erhält man 3 Coins, nur 1 mal einsetzbar
        /**
        SensorEventListener sel = new SensorEventListener() {
            @Override
            public void onSensorChanged(final SensorEvent sensorEvent) {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        connection.sendMessage("cheat"+" "+name);

                        float x = sensorEvent.values[0];
                        float y = sensorEvent.values[1];
                        float z = sensorEvent.values[2];

                        last = current;
                        current = (float) Math.sqrt((double) (x*x + y*y + z*z));
                        float delta = current - last;
                        shake = shake * 0.9f + delta;

                        //look for me in player list
                        for(Player me:game.getPlayers())
                            if(me.getName().equals(name)){
                                player=me;
                            }



                        if (shake > 12 && !player.getCheated()) {
                            //look for me in player list
                            for(Player me:game.getPlayers())
                                if(me.getName().equals(name)){
                                    me.setCoins(me.getCoins()+3);
                                    me.setCheated(true);
                                    player=me;

                                }


                        }

                    }
                });
                thread.start();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText("You cheated");
                        coins.setText("Your coins: "+player.getCoins());
                    }
                });

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        s =(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        s.registerListener(sel, s.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);*/




        Income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        connection.sendMessage("income"+" "+name);
                        //look for me in player list
                        for(Player me:game.getPlayers())
                            if(me.getName().equals(name)){
                                me.setCoins(me.getCoins()+1);
                                player=me;

                            }

                    }
                });
                thread.start();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Income.setEnabled(false);
                        Foreign_Aid.setEnabled(false);
                        Exchange.setEnabled(false);
                        Tax.setEnabled(false);
                        Steal.setEnabled(false);
                        textView.setText("You did income");
                        coins.setText("Your coins: "+player.getCoins());
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
                        connection.sendMessage("foreignaid"+" "+name);
                        //look for me in player list
                        for(Player me:game.getPlayers())
                            if(me.getName().equals(name)){
                                me.setCoins(me.getCoins()+2);
                                player=me;

                            }

                    }
                });

                thread.start();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Income.setEnabled(false);
                        Foreign_Aid.setEnabled(false);
                        Exchange.setEnabled(false);
                        Tax.setEnabled(false);
                        Steal.setEnabled(false);
                        textView.setText("You did foreign aid");
                        coins.setText("Your coins: "+player.getCoins());
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
                        connection.sendMessage("exchange"+" "+name);
                        //look for me in player list
                        for(Player me:game.getPlayers())
                            if(me.getName().equals(name)){
                                player=me;
                            }
                        showCardsToExchange();
                    }
                });

                thread.start();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Income.setEnabled(false);
                        Foreign_Aid.setEnabled(false);
                        Tax.setEnabled(false);
                        Exchange.setEnabled(false);
                        Steal.setEnabled(false);
                        textView.setText("You did exchange");
                    }
                });


            }
        });
        Tax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        connection.sendMessage("tax"+" "+name);
                        //look for me in player list
                        for(Player me:game.getPlayers())
                            if(me.getName().equals(name)) {
                                me.setCoins(me.getCoins() + 3);
                                player = me;
                            }

                    }
                });

                thread.start();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Income.setEnabled(false);
                        Foreign_Aid.setEnabled(false);
                        Exchange.setEnabled(false);
                        Tax.setEnabled(false);
                        Steal.setEnabled(false);
                        textView.setText("You did tax");
                        coins.setText("Your coins: "+player.getCoins());
                    }
                });


            }
        });
        Steal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                choosePlayer();


            }
        });

        Coup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCoup();
            }
        });

/*        Coup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        connection.sendMessage("coup"+" "+name);
                        //look for me in player list
                        for(Player me:game.getPlayers())
                            if(me.getName().equals(name)){
                                me.setCoins(me.getCoins()-7);
                                player=me;

                            }
                        doCoup();

                    }
                });
                thread.start();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Income.setEnabled(false);
                        Foreign_Aid.setEnabled(false);
                        Exchange.setEnabled(false);
                        Tax.setEnabled(false);
                        Steal.setEnabled(false);
                        Coup.setEnabled(false);
                        textView.setText("You did coup on "+ attackedPlayer.getName());
                        coins.setText("Your coins: "+player.getCoins());
                    }
                });

            }
        });

 */







        challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                challengeConfirmation();
            }
        });




    }


    public void challengeConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //TODO: Add what happened last turn to text.
        builder.setMessage("Are you sure you want to challenge the last action?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog challengeDialog = builder.create();
        challengeDialog.show();
    }

    public void choosePlayer(){

        AlertDialog.Builder builder = new AlertDialog.Builder(InGame.this);
        builder.setTitle("Choose Player");

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
                        connection.sendMessage("steal"+" "+name+" "+attackedPlayer.getName());
                        //look for me in player list

                        for(Player p:game.getPlayers()) {
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
                        Income.setEnabled(false);
                        Foreign_Aid.setEnabled(false);
                        Exchange.setEnabled(false);
                        Tax.setEnabled(false);
                        Steal.setEnabled(false);
                        textView.setText("You stole from "+attackedPlayer.getName());
                        updateCoins(attackedPlayer.getName(), -2);
                        coins.setText("Your coins: "+player.getCoins());
                    }
                });

            }

        });

// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();


    }


    public void doCoup(){
        choosePlayer();


    }



/***Methods*********/

    public void challengeTimer() {
        new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                timer.setText("Challenge? " + millisUntilFinished);
            }

            public void onFinish() {
                timer.setText("Challenge over.");
            }
        }.start();
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

    //update opponent textview on income
    public void updateCoinsOnIncome(String onPlayer){

        //update coins for enemy 1
        if(tvOpp1name.getText().equals(onPlayer)){
            for(Player p: game.getPlayers()){
                if(p.getName().equals(onPlayer)){
                    p.setCoins(p.getCoins()+1);
                    Log.e("DEBUG INCOME", ""+p.getCoins());
                    tvOpp1coins.setText(Integer.toString(p.getCoins()));
                }
            }

        }
        //update coins for enemy 2
        if(tvOpp2name.getText().equals(onPlayer)){
            for(Player p: game.getPlayers()){
                if(p.getName().equals(onPlayer)){
                    p.setCoins(p.getCoins()+1);
                    tvOpp2coins.setText(Integer.toString(p.getCoins()));
                }
            }

        }
        //update coins for enemy 3
        if(tvOpp3name.getText().equals(onPlayer)){
            for(Player p: game.getPlayers()){
                if(p.getName().equals(onPlayer)){
                    p.setCoins(p.getCoins()+1);
                    tvOpp3coins.setText(Integer.toString(p.getCoins()));
                }
            }

        }
    }

    //update on foraign aid
    public void updateCoinsOnForeignAid(String onPlayer){

        //update coins for enemy 1
        if(tvOpp1name.getText().equals(onPlayer)){
            for(Player p: game.getPlayers()){
                if(p.getName().equals(onPlayer)){
                    p.setCoins(p.getCoins()+2);
                    Log.e("DEBUG INCOME", ""+p.getCoins());
                    tvOpp1coins.setText(Integer.toString(p.getCoins()));
                }
            }

        }
        //update coins for enemy 2
        if(tvOpp2name.getText().equals(onPlayer)){
            for(Player p: game.getPlayers()){
                if(p.getName().equals(onPlayer)){
                    p.setCoins(p.getCoins()+2);
                    tvOpp2coins.setText(Integer.toString(p.getCoins()));
                }
            }

        }
        //update coins for enemy 3
        if(tvOpp3name.getText().equals(onPlayer)){
            for(Player p: game.getPlayers()){
                if(p.getName().equals(onPlayer)){
                    p.setCoins(p.getCoins()+2);
                    tvOpp3coins.setText(Integer.toString(p.getCoins()));
                }
            }

        }
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

    public void mainPlayerChoosesCardToLose(){
        ivImageC1 = (ImageView) findViewById(R.id.card_playercard1);
        ivImageC2 = (ImageView) findViewById(R.id.card_playercard2);

        // Display: "Click on the Card you want to lose."
        count =0;

        if (ivImageC1.isShown() && ivImageC2.isShown()) {
            ivImageC1.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    if (count == 0) {
                        player.getCards().remove(0);
                        ivImageC1.setVisibility(View.INVISIBLE);
                        count++;
                    }
                }
            });


            ivImageC2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(count==0) {
                        player.getCards().remove(1);
                        ivImageC2.setVisibility(View.INVISIBLE);
                        count++;
                    }
                }
            });
        }
        else{
            player.getCards().remove(0);

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

        ivImageC1 = (ImageView) findViewById(R.id.card_playercard1);
        ivImageC2 = (ImageView) findViewById(R.id.card_playercard2);
        ivImageC3 = (ImageView) findViewById(R.id.card_playercard3);
        ivImageC4 = (ImageView) findViewById(R.id.card_playercard4);

        ivImageC3.setVisibility(View.VISIBLE);
        ivImageC4.setVisibility(View.VISIBLE);

        cardsToChoose = new ArrayList<>();
        choosenCard = new ArrayList<>();

        c1 = player.getCards().get(0);
        displayCards(c1.getTypeOfCard(), ivImageC1);
        cardsToChoose.add(c1);
        if (player.getCards().size() == 2) {
            c2 = player.getCards().get(1);
            displayCards(c2.getTypeOfCard(), ivImageC2);
            cardsToChoose.add(c2);
//        }
//        c3=game.dealCard();
////        cardsToChoose.add(c3);
//        player.addCard(c3);
//        displayCards(c3.getTypeOfCard(),ivImageC3);
//        c4= game.dealCard();
////        cardsToChoose.add(c3);
//        player.addCard(c4);
//        displayCards(c4.getTypeOfCard(),ivImageC4);

            Button chooseCards = (Button) findViewById(R.id.btnOK);



            ivImageC3.setVisibility(View.VISIBLE);
            ivImageC4.setVisibility(View.VISIBLE);
            if (player.getCards().size() == 4) {


                count = 0;
                ivImageC1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (count <= 2) {
                            choosenCard.add(c1);
                            cardsToChoose.remove(c1);
                            count++;
                        }
                    }
                });

                ivImageC2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (count <= 2) {
                            choosenCard.add(c2);
                            cardsToChoose.remove(c2);
                            count++;
                        }
                    }

                });
//                    ivImageC3.setClickable(true);
                ivImageC3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (count <= 2) {
                            choosenCard.add(c3);
                            cardsToChoose.remove(c3);
                            count++;
                        }
                    }

                });
//                    ivImageC4.setClickable(true);
                ivImageC4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (count <= 2) {
                            choosenCard.add(c4);
                            cardsToChoose.remove(c4);
                            count++;
                        }
                    }

                });
                chooseCards.setVisibility(View.VISIBLE);
                chooseCards.setClickable(true);

                chooseCards.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cardsToReturn=cardsToChoose;
                        cardNamesToReturn = new ArrayList<>();

                        cardNamesToReturn=convertCardTypeToStringName(cardsToReturn);

                        for(String cardname: cardNamesToReturn){
                            connection.sendMessage(cardname);
                        }
                        if (count == 2) {
                        player.setCards(choosenCard);
//                        for(Card c:cardsToChoose){
//                            game.returnCardtoDeck(c);
//                        }


                            ivImageC1.setVisibility(View.INVISIBLE);
                            ivImageC2.setVisibility(View.INVISIBLE);
                            ivImageC3.setVisibility(View.INVISIBLE);
                            ivImageC4.setVisibility(View.INVISIBLE);
                            settingCardImagesAtStartOfGame();
//                        game.shuffleCards();
                        }
                    }
                });
            } else if (player.getCards().size() == 1) {
                ivImageC3.setVisibility(View.VISIBLE);
                ivImageC4.setVisibility(View.VISIBLE);
                count = 0;
                if (count <= 1 && ivImageC1.getVisibility() == View.VISIBLE) {
                    ivImageC1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            choosenCard.add(c1);
                            cardsToChoose.remove(c1);
                            count++;
                        }
                    });
                }
                if (count <= 1 && ivImageC2.getVisibility() == View.VISIBLE) {
                    ivImageC2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            choosenCard.add(c2);
                            cardsToChoose.remove(c2);
                            count++;
                        }
                    });
                }
                if (count <= 1) {
                    ivImageC3.setClickable(true);
                    ivImageC3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            choosenCard.add(c3);
                            cardsToChoose.remove(c3);
                            count++;
                        }
                    });
                }
                if (count <= 1) {
                    ivImageC4.setClickable(true);
                    ivImageC4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            choosenCard.add(c4);
                            cardsToChoose.remove(c4);
                            count++;
                        }
                    });
                }
            }
            if (count == 1) {
                chooseCards.setVisibility(View.VISIBLE);
                chooseCards.setClickable(true);
                chooseCards.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cardsToReturn=cardsToChoose;
                        cardNamesToReturn = new ArrayList<>();

                        cardNamesToReturn=convertCardTypeToStringName(cardsToReturn);

                        for(String cardname: cardNamesToReturn){
                            connection.sendMessage(cardname);
                        }
                    player.setCards(choosenCard);
//                    for(Card c:cardsToChoose){
//                        game.returnCardtoDeck(c);
//                    }
                        ivImageC1.setVisibility(View.INVISIBLE);
                        ivImageC2.setVisibility(View.INVISIBLE);
                        ivImageC3.setVisibility(View.INVISIBLE);
                        ivImageC4.setVisibility(View.INVISIBLE);
                        settingCardImagesAtStartOfGame();
//                    game.shuffleCards();

                    }
                });

            }


        }
//        cardNamesToReturn = new ArrayList<>();
//        cardNamesToReturn=convertCardTypeToStringName(cardsToReturn);
//
//            for(String cardname: cardNamesToReturn){
//                connection.sendMessage(cardname);
//            }

    }
    public ArrayList<String> convertCardTypeToStringName(ArrayList listOfCards){
        ArrayList<String> convertedList= new ArrayList<>();
        for(Card c :cardsToReturn) {
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

    //will be removed later
    private void disableNotImplemented(){

        Assasinate.setEnabled(false);
        //Tax.setEnabled(false);
        Exchange.setEnabled(false);
        //Steal.setEnabled(false);
        challenge.setEnabled(false);
        Coup.setEnabled(false);

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
            disableNotImplemented();

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

                    for(String playername: playernames)
                        players.add(new Player(playername));


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
                    next.setEnabled(true);
                    textView.setText("Your turn");
                    timer.setVisibility(View.VISIBLE);
                    countDown.start();

                }
                else{
                    next.setEnabled(false);
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
                    final String[] split = msg.split(" ");
                    if(msg==null||msg.equals("win")||msg.equals("lose"))
                        break;

                    if(msg.equals("turn")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                next.setEnabled(true);
                                Income.setEnabled(true);
                                Foreign_Aid.setEnabled(true);
                                Exchange.setEnabled(true);
                                Tax.setEnabled(true);
                                Steal.setEnabled(true);
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
                                updateCoinsOnIncome(split[1]);
                            }
                        });
                    }
                    if(msg.startsWith("foreignaid")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                textView.setText(split[1]+" used Foreign Aid");
                                updateCoinsOnForeignAid(split[1]);
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
					
					/**/

                    /**
                     * player receives cards from server on exchange
                     if(msg.startsWith("card")){

                     TODO:
                     get two card from server, add them to cardtochose




                     }

                     */

                    /**
                     */
                     if(msg.startsWith("coup")){

                         runOnUiThread(new Runnable() {
                             @Override
                             public void run() {
                                 
                                 textView.setText(split[1]+" used coup on "+attackedPlayer.getName());
                                 updateCoins(split[1],-7);
                             }
                         });


                    /* TODO:
                     split msg to get playername

                     if playername is equals to this player, the player loses a card
                     else display playername with message: playername lost an influence*/

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
