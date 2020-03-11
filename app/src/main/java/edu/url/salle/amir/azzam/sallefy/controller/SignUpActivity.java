package edu.url.salle.amir.azzam.sallefy.controller;

import androidx.appcompat.app.AppCompatActivity;
import edu.url.salle.amir.azzam.sallefy.R;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private Button mRegister;
    private Button mSignIn;
    private EditText username;
    private EditText first_name;
    private EditText last_name;
    private EditText email;
    private ImageButton seePassword;
    private boolean shown;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Intent i = getIntent();

        mRegister = (Button) findViewById(R.id.register);
        mSignIn = (Button) findViewById(R.id.already_have_account);
        username = (EditText) findViewById(R.id.username);
        first_name = (EditText) findViewById(R.id.first_name);
        last_name = (EditText) findViewById(R.id.last_name);
        email = (EditText) findViewById(R.id.email);
        seePassword = (ImageButton) findViewById(R.id.imageButton2);
         password = (EditText) findViewById(R.id.password);

        mRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String usernameStr = username.getText().toString();
                String firstStr = first_name.getText().toString();
                String secondStr = last_name.getText().toString();
                String emailStr = email.getText().toString();
                String passwordStr = password.getText().toString();

                if(emailStr.length()>=1){

                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


                    if (!emailStr.matches(emailPattern) || emailStr.length() == 0)
                    {
                        email.setError("Invalid email address");
                        //   Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    email.setError("Invalid email address");
                }
                if (passwordStr.length() < 8){
                    password.setError("The password must be more than 8 characters");
                    //Toast.makeText(getApplicationContext(),"Invalid password", Toast.LENGTH_SHORT).show();
                }
                if (usernameStr.length() < 1){
                    username.setError("Invalid username ");
                }
                if (firstStr.length() < 1){
                    first_name.setError("Invalid name");
                }
                if(secondStr.length() < 1){
                    last_name.setError("Invalid name");
                }

            }
        });

        mSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                moveToSignIn();
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


    }



    private void moveToSignIn() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}
