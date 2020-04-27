package com.example.coup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //Die Startseite anzeigen wo man ein spiel starten kann, warten bis alle spieler da sind
    //und dann das game starten mit den Spielern
    // new Game
    //und jedem Spieler  bescheid sagen seine Ansicht anzeigen


    //Amar
    //Dies ist das Hauptmenü und führt zu weiteren Aktivitäten.
    // Wir werden einen separaten Bildschirm für die Lobby und den Spielbildschirm haben.
    // Dies ist die erste Aktivität, die gestartet wird.

    private Button ButtonToLobby;
    private Button QuitButton;

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
                quitApp();
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
}
