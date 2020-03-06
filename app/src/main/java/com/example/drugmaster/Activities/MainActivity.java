package com.example.drugmaster.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.drugmaster.Model.User;
import com.example.drugmaster.R;
import com.example.drugmaster.View.GradientTextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private Button registrBtn,signIn;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private Intent newPage;
    private User user;

    private EditText login,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);
        preAnimation();
        registration();
        signingIn();
    }

    private void signingIn() {

        signIn = findViewById(R.id.signIn);
        login = findViewById(R.id.userLogin);
        password = findViewById(R.id.userPassoword);
        mAuth = FirebaseAuth.getInstance();
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fieldIsFree()){
                    showMessage("есть пустые поля");
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    signIn.setVisibility(View.INVISIBLE);
                    registrBtn.setVisibility(View.INVISIBLE);
                    loginProsses(login.getText().toString(),password.getText().toString());
                }
            }
        });
    }

    private boolean fieldIsFree() {
        boolean con = false;
        if (login.getText().toString().equals("") || password.getText().toString().equals(""))
            con = true;
        return con;
    }

    private void loginProsses(final String login, String password) {
        mAuth.signInWithEmailAndPassword(login,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                    updateUI(login);
                else {
                    showMessage(Objects.requireNonNull(task.getException()).getMessage());
                    progressBar.setVisibility(View.INVISIBLE);
                    signIn.setVisibility(View.VISIBLE);
                    registrBtn.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void updateUI(final String login) {
        FirebaseDatabase.getInstance().getReference().child("users").child("managers").child(login).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                progressBar.setVisibility(View.INVISIBLE);
                signIn.setVisibility(View.VISIBLE);
                registrBtn.setVisibility(View.VISIBLE);
                setLogin(user);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                FirebaseDatabase.getInstance().getReference().child("users").child("clients").child(login).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        user = dataSnapshot.getValue(User.class);
                        progressBar.setVisibility(View.INVISIBLE);
                        signIn.setVisibility(View.VISIBLE);
                        registrBtn.setVisibility(View.VISIBLE);
                        setLogin(user);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        showMessage("ваши данные не сохранились,обратитесь к админу!!!");
                        progressBar.setVisibility(View.INVISIBLE);
                        signIn.setVisibility(View.VISIBLE);
                        registrBtn.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    private void setLogin(User user) {
        if (user.getStatus().equals("manager"))
            newPage = new Intent(MainActivity.this,ManagerActivity.class);
        else
            newPage = new Intent(MainActivity.this,ClientActivity.class);

        startActivity(newPage);
        finish();
    }

    private void registration() {
        registrBtn = findViewById(R.id.registration);
        registrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPage = new Intent(MainActivity.this, Registration.class);
                startActivity(newPage);
            }
        });
    }

    private void preAnimation(){
        GradientTextView welcome = findViewById(R.id.welcome_id);
        Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha_anim);
        welcome.startAnimation(anim);
    }
    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        try {
            FirebaseUser user = mAuth.getCurrentUser();

            if(user != null) {
                //user is already connected  so we need to redirect him to home page
                updateUI(user.getEmail());

            }
        }catch (NullPointerException e){
            showMessage("нет сохр пользователя");
        }

    }
}