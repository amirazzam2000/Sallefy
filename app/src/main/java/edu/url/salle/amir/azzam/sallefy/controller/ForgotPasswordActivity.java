package edu.url.salle.amir.azzam.sallefy.controller;

import androidx.appcompat.app.AppCompatActivity;
import edu.url.salle.amir.azzam.sallefy.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

public class ForgotPasswordActivity extends AppCompatActivity {

    private Button recoverPasswordButton;
    private Button logIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        //Objects.requireNonNull(getSupportActionBar()).hide();

        Intent i = getIntent();

        recoverPasswordButton = (Button) findViewById(R.id.reclaim_password);
        logIn = (Button) findViewById(R.id.logInBack);

        recoverPasswordButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                //Send recover password request to server
                //Switch to next screen
            }
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveBackToLogIn();
            }
        });
    }

    private void moveBackToLogIn() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

}
