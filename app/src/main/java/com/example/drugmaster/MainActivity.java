package com.example.drugmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.drugmaster.View.GradientTextView;

public class MainActivity extends AppCompatActivity {
    GradientTextView welcome;
    Button registrBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preAnimation();
        registration();
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