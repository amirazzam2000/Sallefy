package edu.url.salle.amir.azzam.sallefy.controller;

import androidx.appcompat.app.AppCompatActivity;
import edu.url.salle.amir.azzam.sallefy.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Button signIn ;
    private Button signUp;
    private Button forgotPassword ;
    private int backButtonCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        signIn = (Button) findViewById(R.id.login);
        signUp = (Button) findViewById(R.id.login2);
        forgotPassword = (Button) findViewById(R.id.button_send);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToSignUp();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToForgotPassword();
            }
        });




    }

    @Override
    public void onBackPressed()
    {
        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    private void moveToForgotPassword() {
        Intent i = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
        startActivity(i);
    }

    private void moveToSignUp(){

        Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(i);
    }
}
