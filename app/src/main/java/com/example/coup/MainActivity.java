package com.example.coup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //Amar
    //Dies ist das Hauptmenü und führt zu weiteren Aktivitäten.
    // Wir werden einen separaten Bildschirm für die Lobby und den Spielbildschirm haben.
    // Dies ist die erste Aktivität, die gestartet wird.

    private Button ButtonToLobby;
    private Button QuitButton;
    private AlertDialog QuitDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButtonToLobby = (Button) findViewById(R.id.button_play);
        QuitButton = (Button) findViewById(R.id.button_quit);

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
