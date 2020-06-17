package com.example.coup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LobbyScreen extends Activity {

    public EditText nameInput;
    //private String nameInputCheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_screen);

        Button buttonReady = (Button) findViewById(R.id.buttonReady);
        Button buttonRules = (Button) findViewById(R.id.buttonRules);
        nameInput = (EditText) findViewById(R.id.nameInput);
        //nameInputCheck = nameInput.getText().toString();

        buttonReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEmpty(nameInput)) {
                    //TODO Implement a Toast notification if the nameInput is empty
                    Toast.makeText(getApplicationContext(), "You did not enter a name", Toast.LENGTH_SHORT).show();
                } else {


                    Intent intent = new Intent(LobbyScreen.this, InGame.class);
                    intent.putExtra("name", nameInput.getText().toString());
                    startActivity(intent);
                }
            }
        });

        buttonRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRules();
            }
        });
    }

    private boolean isEmpty(EditText nameInput) {
        return nameInput.getText().toString().trim().length() == 0;
    }

    private void showRules() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Do you want to visit an external website to learn about the rules? WARNING: It will take you out of the app.")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        goToURL("https://boardgamegeek.com/boardgame/131357/coup");
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

    protected void goToURL(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
