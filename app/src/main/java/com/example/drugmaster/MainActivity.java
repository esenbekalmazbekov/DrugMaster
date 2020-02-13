package com.example.drugmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.example.drugmaster.View.GradientTextView;

public class MainActivity extends AppCompatActivity {
    GradientTextView welcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preAnimation();
    }

    private void preAnimation(){
        welcome = findViewById(R.id.welcome_id);
        Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha_anim);
        welcome.startAnimation(anim);
    }
}
