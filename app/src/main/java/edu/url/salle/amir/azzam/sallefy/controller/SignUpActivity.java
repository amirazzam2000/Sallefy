package edu.url.salle.amir.azzam.sallefy.controller;

import androidx.appcompat.app.AppCompatActivity;
import edu.url.salle.amir.azzam.sallefy.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private Button mRegister;
    private Button mSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Intent i = getIntent();

        mRegister = (Button) findViewById(R.id.register);
        mSignIn = (Button) findViewById(R.id.already_have_account);

        mRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //do nothing
            }
        });

        mSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                moveToSignIn();
            }
        });


    }



    private void moveToSignIn() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}
