package com.example.coup;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

        return  null;
    }
    */


    //should return Player who clicked challenge and needed CardType
    public Object[] waitForChallenge(){
        return null;
    }

    /*//should return boolean, if someone clicked block Action
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
                        textView.setText("You did foreign aid");
                        coins.setText("Your coins: "+player.getCoins());
                    }
                });




            }
        });


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

        TextView tvOpp1cards=(TextView) findViewById(R.id.opponentNumOfCards1);
        TextView tvOpp2cards=(TextView) findViewById(R.id.opponentNumOfCards2);
        TextView tvOpp3cards=(TextView) findViewById(R.id.opponentNumOfCards3);

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

        tvOpp1cards=(TextView) findViewById(R.id.opponentNumOfCards1);
        tvOpp2cards=(TextView) findViewById(R.id.opponentNumOfCards2);
        tvOpp3cards=(TextView) findViewById(R.id.opponentNumOfCards3);

        tvOpp1coins = findViewById(R.id.textView_enemy_one_coins);
        tvOpp2coins = findViewById(R.id.textView_enemy_two_coins);
        tvOpp3coins = findViewById(R.id.textView_enemy_three_coins);


        Log.e("DEBUG: ",opponents.get(0));

        tvOpp1name.setText(opponents.get(0));

        if(opponents.size()>2){
        tvOpp2name.setText(opponents.get(1));
        tvOpp3name.setText(opponents.get(2));
        }

        tvOpp1cards.setText("2");
        tvOpp2cards.setText("2");
        tvOpp3cards.setText("2");

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




    public void mainPlayerChoosesCardToLose(){
        final ImageView ivCard1 = (ImageView) findViewById(R.id.card_playercard1);
        final ImageView ivCard2 = (ImageView) findViewById(R.id.card_playercard2);

        // Display: "Click on the Card you want to lose."

        if(ivCard1.isShown()&&ivCard2.isShown()) {
            ivCard1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    player.getCards().remove(0);
                    ivCard1.setVisibility(View.INVISIBLE);
                }
            });

            ivCard2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    player.getCards().remove(1);
                    ivCard2.setVisibility(View.INVISIBLE);
                }
            });
        }else{
            player.getCards().remove(0);
        }
    }


    public void settingCardImagesAtStartOfGame() {
        ImageView ivCard1 = (ImageView) findViewById(R.id.card_playercard1);
        ImageView ivCard2 = (ImageView) findViewById(R.id.card_playercard2);

            if (player.getCards().get(0).getTypeOfCard().equals(CardType.CONTESSA)) {
                ivCard1.setImageResource(R.drawable.contessa);
            } else if (player.getCards().get(0).getTypeOfCard().equals(CardType.DUKE)) {
                ivCard1.setImageResource(R.drawable.duke);
            } else if (player.getCards().get(0).getTypeOfCard().equals(CardType.ASSASSIN)) {
                ivCard1.setImageResource(R.drawable.assassin);
            } else if (player.getCards().get(0).getTypeOfCard().equals(CardType.CAPTAIN)) {
                ivCard1.setImageResource(R.drawable.captain);
            } else if (player.getCards().get(0).getTypeOfCard().equals(CardType.AMBASSADOR)) {
                ivCard1.setImageResource(R.drawable.ambassador);
            }
            if (player.getCards().get(1).getTypeOfCard().equals(CardType.CONTESSA)) {
                ivCard2.setImageResource(R.drawable.contessa);
            } else if (player.getCards().get(1).getTypeOfCard().equals(CardType.DUKE)) {
                ivCard2.setImageResource(R.drawable.duke);
            } else if (player.getCards().get(1).getTypeOfCard().equals(CardType.ASSASSIN)) {
                ivCard2.setImageResource(R.drawable.assassin);
            } else if (player.getCards().get(1).getTypeOfCard().equals(CardType.CAPTAIN)) {
                ivCard2.setImageResource(R.drawable.captain);
            } else if (player.getCards().get(1).getTypeOfCard().equals(CardType.AMBASSADOR)) {
                ivCard2.setImageResource(R.drawable.ambassador);
            }
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

                    for(String playername: playernames)
                        players.add(new Player(playername));


                    Log.e("DEBUG CONNECTTAST", ""+playernames.size());
                    game = new Game(players);



                    for(String playername: playernames){
                        if(playername.equals(name))
                            continue;
                        opponents.add(playername);
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

                                textView.setText(split[1]+" used foreign aid");
                                updateCoinsOnForeignAid(split[1]);
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
