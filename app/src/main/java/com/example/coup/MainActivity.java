package com.example.coup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


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
        Button AboutButton = (Button) findViewById(R.id.button_about);

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

        AboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAbout();
            }
        });




    }


    public void openLobby() {
        Intent launchLobby = new Intent(this, LobbyScreen.class);
        startActivity(launchLobby);
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

    private void openAbout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("621.251 (20S) Software Engineering II\n" +
                "Lehrende/r\n" +
                "Dipl.-Ing.Mag. Karin Maria Hodnigg\n" +
                "Gruppe 4\n")
                .setCancelable(false)
                .setPositiveButton("Github", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        goToURL("https://github.com/mathiastr/SE2_Di4_Coup");
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog quitDialog = builder.create();
        quitDialog.show();
    }

    void goToURL(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    }



