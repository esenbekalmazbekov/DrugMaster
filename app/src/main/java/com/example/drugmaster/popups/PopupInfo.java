package com.example.drugmaster.popups;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.drugmaster.Model.User;
import com.example.drugmaster.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PopupInfo extends AppCompatActivity {
    private User user;
    private TextView orgName, address, phone;
    private DatabaseReference userdatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);

        creation();
        putData();
        activebtns();
    }

    private void activebtns() {
        Button save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isChanged()){
                    user.setAddress(address.getText().toString());
                    user.setOrgname(orgName.getText().toString());
                    user.setPhone(phone.getText().toString());

                    userdatabase = FirebaseDatabase.getInstance().getReference();
                    userdatabase.child("users").child(user.getStatus() + "s").child(user.getId()).setValue(user);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("userdata",user);
                    setResult(RESULT_OK,resultIntent);
                    finish();
                }
                else
                    showMessage();
            }
        });
        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("userdata",user);
                setResult(RESULT_CANCELED,resultIntent);
                finish();
            }
        });
    }

    private void showMessage() {
        Toast.makeText(getApplicationContext(), "измените поля!!!",Toast.LENGTH_LONG).show();
    }

    private boolean isChanged() {

        if (!orgName.getText().toString().equals(user.getOrgname()) || !address.getText().toString().equals(user.getAddress()))
            return true;

        return !phone.getText().toString().equals(user.getPhone());
    }


    private void creation() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.5));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);
    }


    private void putData() {
        user = getIntent().getParcelableExtra("userdata");
        orgName = findViewById(R.id.orgName);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone);

        orgName.setText(user.getOrgname());
        address.setText(user.getAddress());
        phone.setText(user.getPhone());
    }
}
