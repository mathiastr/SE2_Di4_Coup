package com.example.coup;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class InGame extends AppCompatActivity {

    Player player;
    Game game;
    private Button next, surrender, challenge;
    private TextView textView; //Change to TextView Timer
    private String name;
    private ServerConnection connection;
    private AlertDialog.Builder builder;



    //Action buttons
    private Button Assasinate;
    private Button Tax;
    private Button Steal;
    private Button Exchange;
    private Button Income;
    private Button Foreign_Aid;
    private Button Coup;

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
        textView = findViewById(R.id.text_playercard1);
//        surrender = findViewById(R.id.button_surrender);

        connection = new ServerConnection();

        builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                try {
                    connection.disconnect();
                    Toast.makeText(InGame.this, "Disconnected", Toast.LENGTH_SHORT).show();
                    finish();
                    dialogInterface.dismiss();
                } catch (IOException e) {
                    Toast.makeText(InGame.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    finish();
                    dialogInterface.dismiss();
                    e.printStackTrace();
                }


            }
        });

        ConnectTask connectTask = new ConnectTask();
        connectTask.execute();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WriteTask write = new WriteTask();
                Boolean res = false;
                try {
                    res = write.execute("next").get();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if (res) {

                    ReadTask read = new ReadTask();
                    read.execute();

                }
            }
        });


        surrender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                WriteTask write = new WriteTask();
                Boolean res = false;
                try {
                    res = write.execute("exit").get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if (res) {

                    ReadTask read = new ReadTask();
                    read.execute();

                }

            }
        });
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

    //not finished
    public void settingOpponentNamesAtStartOfGame() {
        TextView tvOpp1name=(TextView) findViewById(R.id.textView_name_enemy_one);
        TextView tvOpp2name=(TextView) findViewById(R.id.textView_name_enemy_two);
        TextView tvOpp3name=(TextView) findViewById(R.id.textView_name_enemy_three);


    }

    //needs work
    public void updateCoins(){
       // TextView tvCoins= null;

        TextView tvPlayerName=(TextView) findViewById(R.id.textView_player_NAME);
        TextView tvOpp1name=(TextView) findViewById(R.id.textView_name_enemy_one);
        TextView tvOpp2name=(TextView) findViewById(R.id.textView_name_enemy_two);
        TextView tvOpp3name=(TextView) findViewById(R.id.textView_name_enemy_three);

        TextView tvPlayerCoins= (TextView) findViewById(R.id.textView_coins);
        TextView tvOpp1coins=(TextView) findViewById(R.id.textView1_enemy_one_coins_description);
        TextView tvOpp2coins=(TextView) findViewById(R.id.textView1_enemy_two_coins_description);
        TextView tvOpp3coins=(TextView) findViewById(R.id.textView1_enemy_three_coins_description);


        for(Player p: game.getPlayers()){
            if(p.getName().equals(tvOpp1name)){
                tvOpp1coins.setText(p.getCoins());
            }else if(p.getName().equals(tvOpp2name)){
                tvOpp2coins.setText(p.getCoins());
            }
            else if(p.getName().equals(tvOpp3name)){
                tvOpp3coins.setText(p.getCoins());
            }
            else if(p.getName().equals(tvPlayerName)){
                tvPlayerCoins.setText(p.getCoins());
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
            progressDialog = ProgressDialog.show(InGame.this, "ProgressDialog", "connecting");

        }



        @Override
        protected String doInBackground(Void... voids) {
            String res=null;

            try{

                connection.connect("se2-demo.aau.at",53214);
                String msg = connection.getMessage();

                if(msg.equals("noplayer")){
                    res=msg;
                }else res=msg;

            }
            catch (IOException e){
                e.printStackTrace();

            }




            return res;

        }
        @Override
        protected void onPostExecute(String res){

            //Enable/Disable functions for player on turn/wait

            if(res.equals("turn")||res.equals("wait")){
                Toast.makeText(InGame.this,"Connected",Toast.LENGTH_SHORT).show();

                next.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                surrender.setVisibility(View.VISIBLE);

                if(res.equals("turn"))
                    textView.setText("Your turn"); //Change to TextView Timer

                if(res.equals("wait")){
                    ReadTask read = new ReadTask();
                    read.execute();


                }





            }//no player found
            else if(res.equals("noplayer")){
                Toast.makeText(InGame.this,"No players found. Try again.",Toast.LENGTH_SHORT).show();

                finish();

            }// no connection to server
            else{
                Toast.makeText(InGame.this,"Cannot reach server! Try again.",Toast.LENGTH_SHORT).show();
                finish();

            }

            progressDialog.dismiss();


        }
    }

    //called when waiting for turn is required

    private class ReadTask extends AsyncTask<Void, Void, String>{

        @Override
        protected void onPreExecute(){
            next.setEnabled(false);
            surrender.setEnabled(false);
            textView.setText("Opponents turn"); //Change to TextView Timer

        }
        @Override
        protected String doInBackground(Void... voids) {
            String msg=null;

            try {
                while (true){
                    msg=connection.getMessage();
                    if(msg.equals("turn")||msg.equals("win")||msg.equals("lose"))
                        break;

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return msg;
        }

        protected void onPostExecute(String res){

            if(res.equals("turn")){
                textView.setText("Your turn");
                next.setEnabled(true);
                surrender.setEnabled(true);
            }
            if(res.equals("win")){
                builder.setTitle("You win");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            if(res.equals("lose")){
                builder.setTitle("You lose");
                AlertDialog dialog = builder.create();
                dialog.show();
            }


        }
    }

    //called every time an Action is done

    private class WriteTask extends AsyncTask<String,Void,Boolean>{


        @Override
        protected Boolean doInBackground(String... strings) {

            connection.sendMessage(strings[0]);

            return true;

        }


    }




}
