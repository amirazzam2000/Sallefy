package edu.url.salle.amir.azzam.sallefy.controller;

import androidx.appcompat.app.AppCompatActivity;
import edu.url.salle.amir.azzam.sallefy.R;
import edu.url.salle.amir.azzam.sallefy.controller.ui.HomeFragment;
import edu.url.salle.amir.azzam.sallefy.model.User;
import edu.url.salle.amir.azzam.sallefy.model.UserToken;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.UserCallback;
import edu.url.salle.amir.azzam.sallefy.restapi.manager.UserManager;
import edu.url.salle.amir.azzam.sallefy.utils.PreferenceUtils;
import edu.url.salle.amir.azzam.sallefy.utils.Session;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.time.LocalDateTime;

public class InitializationActivity extends AppCompatActivity implements UserCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initilization);

        if (checkExistingPreferences()) {
           // doLogin(PreferenceUtils.getUser(this),PreferenceUtils.getPassword(this));
            Session.getInstance(this).setUserToken(new UserToken(PreferenceUtils.getToken(this)));
            Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
            startActivity(intent);

        }else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

    }

    private void doLogin(String username, String password) {
        UserManager.getInstance(getApplicationContext()).loginAttempt(username, password,InitializationActivity.this);
    }

    private boolean checkExistingPreferences () {
        //return PreferenceUtils.getUser(this) != null
        //        && PreferenceUtils.getPassword(this) != null;
        return PreferenceUtils.getToken(this)!=null;

    }


    @Override
    public void onLoginSuccess(UserToken userToken) {
        Session.getInstance(getApplicationContext())
                .setUserToken(userToken);

        //Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
        Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLoginFailure(Throwable throwable) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
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
    public void onUserFollowed(boolean value) {

    }

    @Override
    public void onUserSelected(User user) {

    }

    @Override
    public void onFollowersUserReceived() {

    }

    @Override
    public void onFollowingUsersReceived() {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
