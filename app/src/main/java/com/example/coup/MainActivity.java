package com.example.coup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    //Die Startseite anzeigen wo man ein spiel starten kann, warten bis alle spieler da sind
    //und dann das game starten mit den Spielern
    // new Game
    //und jedem Spieler  bescheid sagen seine Ansicht anzeigen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

}
