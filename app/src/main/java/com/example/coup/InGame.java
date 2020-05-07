package com.example.coup;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class InGame extends AppCompatActivity {

    private Button next;
    private TextView timer; //Change to TextView Timer
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ingame);

        Bundle b = getIntent().getExtras();
        if(b!=null)
            name=b.getString("name");


        next = findViewById(R.id.button_next);
        timer = findViewById(R.id.textView_timer);


        connection=new ServerConnection();

        builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                try {
                    connection.disconnect();
                    Toast.makeText(InGame.this,"Disconnected",Toast.LENGTH_SHORT).show();
                    finish();
                    dialogInterface.dismiss();
                } catch (IOException e) {
                    Toast.makeText(InGame.this,"Connection Error",Toast.LENGTH_SHORT).show();
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

                if(res){

                    ReadTask read = new ReadTask();
                    read.execute();

                }
            }
        });


    }

    //Time methods - optimise time after playing game. Either speed up or slow down.
    public void turnTimer() {
        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                timer.setText("Your turn: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                timer.setText("Turn over.");
            }
        }.start();
    }

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


                if(res.equals("turn"))
                    turnTimer();

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
            timer.setText("Opponents turn"); //Change to TextView Timer

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
                //textView.setText("Your turn"); //Change to timer
                next.setEnabled(true);
                //Add challenge button
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
