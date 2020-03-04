package edu.url.salle.amir.azzam.sallefy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ForgotPasswordActivity extends AppCompatActivity {

    private Button recoverPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        recoverPasswordButton = (Button) findViewById(R.id.reclaim_password);
        recoverPasswordButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                //Send recover password request to server
                //Switch to next screen
            }
        });
    }
}
