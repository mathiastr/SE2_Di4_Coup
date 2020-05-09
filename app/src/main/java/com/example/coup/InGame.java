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
import java.util.ArrayList;
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
    int count;
    ArrayList<Card> cardsToChoose,choosenCard;
    Card c1,c2,c3,c4;
    ImageView ivImageC1,ivImageC2,ivImageC3,ivImageC4;

    AllActions action;
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
//    public Object[] waitForChallenge(){
//        //                Object[] arr;
////
////                CardType neededCard;
////                neededCard = null;
////                AllActions allActions = new AllActions(game);
////                if (game.getLastAction().equals(Action.ASSASSINATE)) {
////                    neededCard = CardType.ASSASSIN;
////                } else if (game.getLastAction().equals(Action.TAX)) {
////                    neededCard = CardType.DUKE;
////                } else if (game.getLastAction().equals(Action.STEAL)) {
////                    neededCard = CardType.CAPTAIN;
////                } else if (game.getLastAction().equals(Action.EXCHANGE)) {
////                    neededCard = CardType.AMBASSADOR;
////                }
////                Player p = new Player("");
////                arr= new Object[]{
////                        neededCard,p};
////
////        return arr;
//        return null;
//    }

    /*//should return boolean, if someone clicked block Action
    public boolean waitForBlock(List<Player> Playerscanblock){

        return false;
    }*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ingame);
        Player p2,p3,p4;
        player= new Player("P1");
        ArrayList<Player> players=new ArrayList<>();
        player = new Player("P1");
        p2= new Player("P2");
        p3= new Player("P3");
        p4= new Player("P4");

        players.add(player);
        players.add(p2);
        players.add(p3);
        players.add(p4);

//        Card c1 =new Card(CardType.CAPTAIN);
//        Card c2 = new Card(CardType.ASSASSIN);
//        List<Card> cards= new ArrayList<>();
//        cards.add(c1);
//        cards.add(c2);

//        player.setCards(cards);
        game=new Game(players);
        game.dealStartOfGame();
        action=new AllActions(game);
        settingCardImagesAtStartOfGame();



//        settingCardImagesAtStartOfGame();
//        showCardsToExchange();
        Income= (Button) findViewById(R.id.button_income);
        Income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action.income(player);
                updateCoins();
            }});

        Foreign_Aid= (Button) findViewById(R.id.button_foreign_aid);
        Foreign_Aid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action.foreignAid(player);
                updateCoins();
            }});

        Button exchange= (Button) findViewById(R.id.button_exchange);
        exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCardsToExchange();
            }});

        Button chooseCardtoLose= (Button) findViewById(R.id.button_assassinate);
        chooseCardtoLose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPlayerChoosesCardToLose();
            }});

//        Bundle b = getIntent().getExtras();
//        if (b != null)
//            name = b.getString("name");

//        challenge = (Button) findViewById(R.id.button_challenge);
//        next = findViewById(R.id.button_next);
//        textView = findViewById(R.id.text_playercard1);
////        surrender = findViewById(R.id.button_surrender);
//
//        connection = new ServerConnection();
//
//        builder = new AlertDialog.Builder(this);
//        builder.setCancelable(false);
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//                try {
//                    connection.disconnect();
//                    Toast.makeText(InGame.this, "Disconnected", Toast.LENGTH_SHORT).show();
//                    finish();
//                    dialogInterface.dismiss();
//                } catch (IOException e) {
//                    Toast.makeText(InGame.this, "Connection Error", Toast.LENGTH_SHORT).show();
//                    finish();
//                    dialogInterface.dismiss();
//                    e.printStackTrace();
//                }
//
//
//            }
//        });
//
//        ConnectTask connectTask = new ConnectTask();
//        connectTask.execute();
//
//        next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                WriteTask write = new WriteTask();
//                Boolean res = false;
//                try {
//                    res = write.execute("next").get();
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                }
//
//                if (res) {
//
//                    ReadTask read = new ReadTask();
//                    read.execute();
//
//                }
//            }
//        });
//
//
//        surrender.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                WriteTask write = new WriteTask();
//                Boolean res = false;
//                try {
//                    res = write.execute("exit").get();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                }
//
//                if (res) {
//
//                    ReadTask read = new ReadTask();
//                    read.execute();
//
//                }
//
//            }
//        });
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

    //not finished
////    public void settingOpponentNamesAtStartOfGame() {
////        TextView tvOpp1name=(TextView) findViewById(R.id.textView_name_enemy_one);
////        TextView tvOpp2name=(TextView) findViewById(R.id.textView_name_enemy_two);
////        TextView tvOpp3name=(TextView) findViewById(R.id.textView_name_enemy_three);
//
//
//    }
    public void updateCoins(){
        TextView tvPlayerCoins= (TextView) findViewById(R.id.textView_coins);
        tvPlayerCoins.setText("Your Coins: "+player.getCoins());
    }






//    needs work
    public void updateCoinsAll(){
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

    public void showCardsToExchange(){
        TextView tvTextC1 = (TextView) findViewById(R.id.text_playercard1);
        TextView tvTextC2 = (TextView) findViewById(R.id.text_playercard2);
//        TextView tvTextC3 = (TextView) findViewById(R.id.text_playercard3);
//        TextView tvTextC4 = (TextView) findViewById(R.id.text_playercard4);

        ivImageC1= (ImageView) findViewById(R.id.card_playercard1);
        ivImageC2= (ImageView) findViewById(R.id.card_playercard2);
        ivImageC3= (ImageView) findViewById(R.id.card_playercard3);
        ivImageC4= (ImageView) findViewById(R.id.card_playercard4);

        ivImageC3.setVisibility(View.VISIBLE);
        ivImageC4.setVisibility(View.VISIBLE);

        cardsToChoose = new ArrayList<>();
        choosenCard = new ArrayList<>();

        c1 = player.getCards().get(0);
        displayCards(c1.getTypeOfCard(),ivImageC1);
        cardsToChoose.add(c1);
        if(player.getCards().size()==2) {
            c2 = player.getCards().get(1);
            displayCards(c2.getTypeOfCard(),ivImageC2);
            cardsToChoose.add(c2);
        }
        c3=game.dealCard();
//        cardsToChoose.add(c3);
        player.addCard(c3);
        displayCards(c3.getTypeOfCard(),ivImageC3);
        c4= game.dealCard();
//        cardsToChoose.add(c3);
        player.addCard(c4);
        displayCards(c4.getTypeOfCard(),ivImageC4);

        Button chooseCards= (Button) findViewById(R.id.btnOK);
        TextView yourName= (TextView) findViewById(R.id.textView_player_NAME);
        TextView action= (TextView) findViewById(R.id.textView_action);

//        yourName.setVisibility(View.INVISIBLE);
//        action.setVisibility(View.INVISIBLE);
//        chooseCards.setVisibility(View.VISIBLE);
        ivImageC3.setVisibility(View.VISIBLE);
        ivImageC4.setVisibility(View.VISIBLE);
        if(player.getCards().size()==4) {


            count = 0;
                        ivImageC1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(count<=2) {
                                    choosenCard.add(c1);
                                cardsToChoose.remove(c1);
                                count++;
                            }
                            }
                        });

                        ivImageC2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(count<=2) {
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
                            if(count<=2) {
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
                            if(count<=2) {
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
                            if(count==2){
                                player.setCards(choosenCard);
                            for(Card c:cardsToChoose){
                                game.returnCardtoDeck(c);
                            }
                            ivImageC1.setVisibility(View.INVISIBLE);
                            ivImageC2.setVisibility(View.INVISIBLE);
                            ivImageC3.setVisibility(View.INVISIBLE);
                            ivImageC4.setVisibility(View.INVISIBLE);
                            settingCardImagesAtStartOfGame();
                            game.shuffleCards();
                            }
                        }
                    });
       }
         else if(player.getCards().size()==1){
            ivImageC3.setVisibility(View.VISIBLE);
            ivImageC4.setVisibility(View.VISIBLE);
            count=0;
            if(count<=1&&ivImageC1.getVisibility()==View.VISIBLE){
                ivImageC1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        choosenCard.add(c1);
                        cardsToChoose.remove(c1);
                        count++;
                    }
                });
            } if(count<=1&&ivImageC2.getVisibility()==View.VISIBLE){
                ivImageC2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        choosenCard.add(c2);
                        cardsToChoose.remove(c2);
                        count++;
                    }
                });
            }
            if(count<=1) {
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
            if(count<=1) {
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
        if(count==1){
            chooseCards.setVisibility(View.VISIBLE);
            chooseCards.setClickable(true);
            chooseCards.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    player.setCards(choosenCard);
                    for(Card c:cardsToChoose){
                        game.returnCardtoDeck(c);
                    }
                    ivImageC1.setVisibility(View.INVISIBLE);
                    ivImageC2.setVisibility(View.INVISIBLE);
                    ivImageC3.setVisibility(View.INVISIBLE);
                    ivImageC4.setVisibility(View.INVISIBLE);
                    settingCardImagesAtStartOfGame();
                    game.shuffleCards();

                }
            });
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
