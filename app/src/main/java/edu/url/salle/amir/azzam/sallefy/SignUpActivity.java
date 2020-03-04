package edu.url.salle.amir.azzam.sallefy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    private Button mRegister;
    private Button mSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mRegister = (Button) findViewById(R.id.register);
        mSignin = (Button) findViewById(R.id.login2);

        mRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //do nothing
            }
        });

        mSignin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //do nothing
            }
        });







    }
}
