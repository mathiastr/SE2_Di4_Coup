package com.example.coup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    //Amar
    //Dies ist das Hauptmenü und führt zu weiteren Aktivitäten.
    // Wir werden einen separaten Bildschirm für die Lobby und den Spielbildschirm haben.
    // Dies ist die erste Aktivität, die gestartet wird.

    Game game;

//    private Button ButtonToLobby;
//    private Button QuitButton;
//    private AlertDialog QuitDialog;

    //These button are used just for presentation - remove after functionality for lobby to game implemented.
//    private Button testGameScreen;
//    private Button testEndGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button ButtonToLobby = (Button) findViewById(R.id.button_play);
        Button QuitButton= (Button) findViewById(R.id.button_quit);
        Button testGameScreen= (Button) findViewById(R.id.button_test_gamescreen);
        Button testEndGame=findViewById(R.id.button_test_endgamescreen);
        //Remove

        //Remove
        testGameScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGameScreen();
            }
        });

        //Remove
        testEndGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAfterGameScreen();
            }
        });




        ButtonToLobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLobby();
            }
        });

        QuitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quitWarning();
            }
        });


    }

    //Remove
    public void openGameScreen() {
        Intent launchGameScreen = new Intent(this, InGame.class);
        startActivity(launchGameScreen);
    }

    //Remove
    public void openAfterGameScreen() {
        Intent launchAfterScreen = new Intent(this, AfterGame.class);
        startActivity(launchAfterScreen);
    }

    public void openLobby() {
        Intent launchLobby = new Intent(this, LobbyScreen.class);
        startActivity(launchLobby);
    }

    public void quitApp() {
        MainActivity.this.finish();
        System.exit(0);
    }

    private void quitWarning() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure you want to quit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog quitDialog = builder.create();
        quitDialog.show();
    }


}
