package edu.url.salle.amir.azzam.sallefy.controller;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import edu.url.salle.amir.azzam.sallefy.R;
import edu.url.salle.amir.azzam.sallefy.model.User;
import edu.url.salle.amir.azzam.sallefy.model.UserToken;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.UserCallback;
import edu.url.salle.amir.azzam.sallefy.restapi.manager.UserManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity implements UserCallback {

    private Button mRegister;
    private Button mSignIn;
    private EditText username;
    private EditText first_name;
    private EditText last_name;
    private EditText email;
    private ImageButton seePassword;
    private boolean shown;
    private EditText password;
    private String usernameStr;
    private String passwordStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //Objects.requireNonNull(getSupportActionBar()).hide();

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
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v){
                usernameStr = username.getText().toString();
                String firstStr = first_name.getText().toString();
                String secondStr = last_name.getText().toString();
                String emailStr = email.getText().toString();
                passwordStr = password.getText().toString();
                boolean ok = true;

                if(emailStr.length()>=1){

                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


                    if (!emailStr.matches(emailPattern) || emailStr.length() == 0)
                    {
                        email.setError("Invalid email address");
                        ok = false;
                    }
                }
                else{
                    email.setError("Invalid email address");
                    ok = false;

                }
                if (passwordStr.length() < 1){
                    password.setError("You can't leave the password empty");
                    ok = false;

                }
                if (usernameStr.length() < 1){
                    username.setError("Invalid username ");
                    ok = false;

                }
                if (firstStr.length() < 1){
                    first_name.setError("Invalid name");
                    ok = false;

                }
                if(secondStr.length() < 1){
                    last_name.setError("Invalid name");
                    ok = false;

                }
                if(ok){
                    doLogin(emailStr, passwordStr, usernameStr, firstStr, secondStr);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void doLogin(String emailStr, String passwordStr, String usernameStr, String firstStr, String secondStr) {
        UserManager.getInstance(getApplicationContext()).registerAttempt(emailStr, usernameStr, passwordStr, secondStr, firstStr, SignUpActivity.this);

    }


    private void moveToSignIn() {
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
    }

    @Override
    public void onLoginSuccess(UserToken userToken) {
        Intent i = new Intent(getApplicationContext(), HomePageActivity.class);
        startActivity(i);
    }

    @Override
    public void onLoginFailure(Throwable throwable) {
        Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_SHORT).show();
        email.getText().clear();
        password.getText().clear();
    }

    @Override
    public void onRegisterSuccess() {
        UserManager.getInstance(getApplicationContext()).loginAttempt(usernameStr, passwordStr,SignUpActivity.this);


    }

    @Override
    public void onRegisterFailure(Throwable throwable) {
        Toast.makeText(getApplicationContext(),"no good luck next time", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUserInfoReceived(User userData) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
