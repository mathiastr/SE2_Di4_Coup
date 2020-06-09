package com.example.coup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AfterGame extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       String result=" ";

        // set win or lose result
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
            result=bundle.getString("result");


        //

        setContentView(R.layout.activity_after_game);

        TextView resulttext = findViewById(R.id.textView_result_message);
        resulttext.setText(result);



        Button buttonQuit = (Button) findViewById(R.id.button_aftergame_quit);
        Button buttonMainMenu = (Button) findViewById(R.id.button_aftergame_mainmenu);



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
