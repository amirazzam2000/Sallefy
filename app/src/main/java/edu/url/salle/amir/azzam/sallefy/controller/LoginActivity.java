package edu.url.salle.amir.azzam.sallefy.controller;

import androidx.appcompat.app.AppCompatActivity;
import edu.url.salle.amir.azzam.sallefy.R;
import edu.url.salle.amir.azzam.sallefy.model.User;
import edu.url.salle.amir.azzam.sallefy.model.UserToken;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.UserCallback;

import android.content.Intent;
import android.os.Bundle;
import edu.url.salle.amir.azzam.sallefy.restapi.manager.UserManager;
import edu.url.salle.amir.azzam.sallefy.utils.Constants;
import edu.url.salle.amir.azzam.sallefy.utils.PreferenceUtils;
import edu.url.salle.amir.azzam.sallefy.utils.Session;

import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements UserCallback {

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
        //Objects.requireNonNull(getSupportActionBar()).hide();

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
                String emailStr = email.getText().toString();
                String passwordStr = password.getText().toString();
                boolean ok = true;

                    if(emailStr.length()<1){
                        ok = false;
                        email.setError("You can't leave the email filed empty!");

                    }
                    if (passwordStr.length() < 1){
                        password.setError("You can't leave the password filed empty!");
                        ok = false;
                        //Toast.makeText(getApplicationContext(),"Invalid password", Toast.LENGTH_SHORT).show();
                    }
                    if(ok){
                        doLogin(emailStr, passwordStr);
                    }

            }
        });

        checkForSavedData();

    }

    private void doLogin(String username, String password) {
        UserManager.getInstance(getApplicationContext()).loginAttempt(username, password,LoginActivity.this);
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

    private void checkForSavedData() {
        if (checkExistingPreferences()) {
            email.setText(PreferenceUtils.getUser(this));
            password.setText(PreferenceUtils.getPassword(this));
            doLogin(PreferenceUtils.getUser(this),PreferenceUtils.getPassword(this));
        }
    }
    private boolean checkExistingPreferences () {
        return PreferenceUtils.getUser(this) != null
                && PreferenceUtils.getPassword(this) != null;
    }

    private void moveToForgotPassword() {
        Intent i = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
        startActivity(i);
    }

    private void moveToSignUp(){

        Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(i);
    }

    @Override
    public void onLoginSuccess(UserToken userToken) {
        Session.getInstance(getApplicationContext())
                .setUserToken(userToken);
        PreferenceUtils.saveUser(this, email.getText().toString());
        PreferenceUtils.savePassword(this, password.getText().toString());

        UserManager.getInstance(this).getUserData(email.getText().toString(), this);

        //Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
        Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLoginFailure(Throwable throwable) {
        Toast.makeText(getApplicationContext(),"wrong email or password", Toast.LENGTH_SHORT).show();
        email.getText().clear();
        password.getText().clear();

    }

    @Override
    public void onRegisterSuccess() {

    }

    @Override
    public void onRegisterFailure(Throwable throwable) {

    }

    @Override
    public void onUserInfoReceived(User userData) {
        Session.getInstance(getApplicationContext())
                .setUser(userData);
        //Intent intent= new Intent();
        System.out.println("i'm here");
        //Intent intent = new Intent();
        //setResult(Constants.NETWORK.LOGIN_OK,intent);

        finish();

    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
