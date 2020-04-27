package com.example.coup;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LobbyScreen extends AppCompatActivity {

    private Button buttonReady;
    private Button buttonRules;
    public EditText nameInput;
    //private String nameInputCheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_screen);

        buttonReady = (Button) findViewById(R.id.buttonReady);
        buttonRules = (Button) findViewById(R.id.buttonRules);
        nameInput = (EditText) findViewById(R.id.nameInput);
        //nameInputCheck = nameInput.getText().toString();

        buttonReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEmpty(nameInput)) {
                    //TODO Implement a Toast notification if the nameInput is empty
                    //Toast.makeText(getApplicationContext(), "You did not enter a name", Toast.LENGTH_SHORT).show();
                } else {
                    //TODO Implement the READY functionality and a notification
                }
            }
        });

        buttonRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private boolean isEmpty(EditText nameInput) {
        return nameInput.getText().toString().trim().length() == 0;
    }
}
