package com.example.coup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AfterGame extends AppCompatActivity {

    private Button buttonQuit;
    private Button buttonMainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_game);

        buttonQuit = (Button) findViewById(R.id.button_aftergame_quit);
        buttonMainMenu = (Button) findViewById(R.id.button_aftergame_mainmenu);

        buttonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AfterGame.this.finish();
                System.exit(0);
            }
        });

        buttonMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMenu();
            }
        });
    }

    public void goToMenu() {
        Intent goMenu = new Intent(this, MainActivity.class);
        startActivity(goMenu);
    }
}
