package edu.url.salle.amir.azzam.sallefy.controller;

import androidx.appcompat.app.AppCompatActivity;
import edu.url.salle.amir.azzam.sallefy.R;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Button signIn ;
    private Button signUp;
    private Button forgotPassword ;
    private int backButtonCount;
    private ImageButton seePassword;
    private boolean shown;
    private EditText password;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        signIn = (Button) findViewById(R.id.login);
        signUp = (Button) findViewById(R.id.login2);
        forgotPassword = (Button) findViewById(R.id.button_send);
        seePassword = (ImageButton) findViewById(R.id.imageButton);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.username);
        shown = false ;

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToSignUp();
            }
        });

        seePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!shown) {
                    password.setTransformationMethod(new PasswordTransformationMethod());
                    shown = true;
                }
                else{
                    password.setTransformationMethod(null);
                    shown = false;
                }
            }

        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToForgotPassword();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = email.getText().toString().trim();
                String passwordStr = password.getText().toString();

                    if(emailStr.length()>=1){

                        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


                        if (!emailStr.matches(emailPattern))
                        {
                            email.setError("Invalid email address");
                         //   Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
                        }
                        else if (passwordStr.length() < 8){
                            password.setError("Invalid password");
                            //Toast.makeText(getApplicationContext(),"Invalid password", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        email.setError("You can't leave the email filed empty!");
                        //Toast.makeText(getApplicationContext(),"You can't leave the email filed empty!", Toast.LENGTH_SHORT).show();
                    }
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
