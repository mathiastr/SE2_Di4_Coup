package com.example.coup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class MainActivity extends Activity {

    //Amar
    //Dies ist das Hauptmenü und führt zu weiteren Aktivitäten.
    // Wir werden einen separaten Bildschirm für die Lobby und den Spielbildschirm haben.
    // Dies ist die erste Aktivität, die gestartet wird.

    Game game;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button ButtonToLobby = (Button) findViewById(R.id.button_play);
        Button QuitButton = (Button) findViewById(R.id.button_quit);

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
