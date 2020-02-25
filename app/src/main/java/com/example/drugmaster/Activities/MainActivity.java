package com.example.drugmaster.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.example.drugmaster.R;
import com.example.drugmaster.View.GradientTextView;

public class MainActivity extends AppCompatActivity {
    GradientTextView welcome;
    Button registrBtn,signIn;
    EditText login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preAnimation();
        registration();
        signingIn();
    }

    private void signingIn() {
        signIn = findViewById(R.id.signIn);
        login = findViewById(R.id.userLogin);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(login.getText().toString().equals("manager"))
                    intent = new Intent(MainActivity.this, ManagerActivity.class);
                else
                    intent = new Intent(MainActivity.this, ClientActivity.class);
                startActivity(intent);
            }
        });
    }

    private void registration() {
        registrBtn = findViewById(R.id.registration);
        registrBtn.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Intent intent = new Intent(MainActivity.this, Registration.class);
                                              startActivity(intent);
                                          }
                                      }
        );
    }

    private void preAnimation(){
        welcome = findViewById(R.id.welcome_id);
        Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha_anim);
        welcome.startAnimation(anim);
    }
}