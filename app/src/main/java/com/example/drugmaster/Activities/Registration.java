package com.example.drugmaster.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.drugmaster.Model.User;
import com.example.drugmaster.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Registration extends AppCompatActivity{

    // profile change
    private CircleImageView imgUserPhoto;
    static int PReqCode = 1 ;
    static int REQUESCODE = 1 ;
    Uri pickedImgUri ;

    // registration
    private EditText orgName,password,repassword,email,phone,address,regcode;
    private RadioButton client,manager;
    private Button submit;
    private FirebaseAuth mAuth;
    private static final String ACTIVE_KEY = "activationKey";
    private DatabaseReference userdatabase;
    private String key;
    private ProgressBar progressBar;
    private User user;
    private String status = "manager";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getPhoto();
        activation();
    }

    private void activation() {
        getActivationKey();
        orgName = findViewById(R.id.orgName);
        password = findViewById(R.id.userPassoword);
        repassword = findViewById(R.id.userPassowordRewrite);
        email = findViewById(R.id.userEmail);
        phone = findViewById(R.id.phoneNumber);
        address = findViewById(R.id.location);
        regcode = findViewById(R.id.registrationID);
        client = findViewById(R.id.client);
        manager = findViewById(R.id.manager);
        mAuth = FirebaseAuth.getInstance();
        submit = findViewById(R.id.registration);
        progressBar = findViewById(R.id.progressBar);
        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = "client";
                client.setChecked(true);
            }
        });
        manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = "manager";
                manager.setChecked(true);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked();
            }
        });
    }

    private void clicked() {
        submit.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        if(fieldIsNotEmpty()){
            if(controlPasswordSameValue()){
                if(activationCodeControl()){
                    goToRegistration();
                }
                else {
                    showMessage("Неверный код регистра");
                    submit.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
            else{
                showMessage("Пароли не совподают");
                submit.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
        else{
            showMessage("Есть пустое поле");
            submit.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }

    }

    private void goToRegistration(){

        user = new User(
                this.regcode.getText().toString(),
                this.orgName.getText().toString(),
                this.email.getText().toString(),
                this.phone.getText().toString(),
                this.address.getText().toString(),
                status);



        mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
                            final StorageReference imageFilePath = mStorage.child(Objects.requireNonNull(pickedImgUri.getLastPathSegment()));
                            imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Registration.this.showMessage("onSuccess");
                                    imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(user.getId())
                                                    .setPhotoUri(uri)
                                                    .build();


                                            Objects.requireNonNull(mAuth.getCurrentUser()).updateProfile(profileUpdate)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                showMessage("Регистрация прошла успешна!!!");
                                                            }

                                                            updateUI();

                                                            userdatabase = FirebaseDatabase.getInstance().getReference();

                                                            if (user.getStatus().equals("manager"))
                                                                userdatabase.child("users").child("managers").child(user.getId()).setValue(user);
                                                            else
                                                                userdatabase.child("users").child("clients").child(user.getId()).setValue(user);

                                                            submit.setVisibility(View.VISIBLE);
                                                            progressBar.setVisibility(View.INVISIBLE);

                                                        }
                                                    });
                                        }
                                    });
                                }
                            });
                        } else {
                            Registration.this.showMessage("Ошибка!!!");
                            submit.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    private boolean activationCodeControl() {
        return regcode.getText().toString().equals(key);
    }
    private void updateUI() {
        Intent newPage;

        FirebaseDatabase.getInstance().getReference().child(ACTIVE_KEY).setValue(UUID.randomUUID().toString());
        if (user.getStatus().equals("manager"))
            newPage = new Intent(Registration.this,ManagerActivity.class);
        else
            newPage = new Intent(Registration.this,ClientActivity.class);

        newPage.putExtra("userdata",user);
        startActivity(newPage);
        finish();;
    }

    private boolean controlPasswordSameValue() {
        return password.getText().toString().equals(repassword.getText().toString());
    }

    private boolean fieldIsNotEmpty() {

        if(orgName.getText().toString().equals(""))
            return false;
        if(password.getText().toString().equals(""))
            return false;
        if(repassword.getText().toString().equals(""))
            return false;
        if(email.getText().toString().equals(""))
            return false;
        if(phone.getText().toString().equals(""))
            return false;
        if(address.getText().toString().equals(""))
            return false;
        return !regcode.getText().toString().equals("");
    }

    private void getPhoto() {
        imgUserPhoto = findViewById(R.id.profile_image) ;

        imgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 22)
                    checkAndRequestForPermission();
                else
                    openGallery();
            }
        });
    }

    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image !
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(Registration.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Registration.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(Registration.this,"Please accept for required permission",Toast.LENGTH_SHORT).show();
            }
            else
            {
                ActivityCompat.requestPermissions(Registration.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        }
        else
            openGallery();

    }

    private void showMessage(String str){
        Toast.makeText(Registration.this,str,Toast.LENGTH_LONG).show();
    }

    private void getActivationKey(){
        FirebaseDatabase.getInstance().getReference().child(ACTIVE_KEY)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Get user information
                        key = (String) Objects.requireNonNull(dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null ) {

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData() ;
            imgUserPhoto.setImageURI(pickedImgUri);
        }
    }
}