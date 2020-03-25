package com.example.drugmaster.popups;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.drugmaster.Model.Drug;
import com.example.drugmaster.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class PopupAdddrugs extends AppCompatActivity {
    EditText name, maker, price;
    Spinner unit;
    Button save,cancel;
    Drug newdrug;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_adddrugs);

        creation();
        connection();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDrug();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addDrug() {
        if(!isEmpty()){
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("drugs");

            newdrug = new Drug(
                    db.push().getKey(),
                    name.getText().toString(),
                    unit.getSelectedItem().toString(),
                    maker.getText().toString(),
                    price.getText().toString());

            db.child(Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName())).child(newdrug.getId()).setValue(newdrug);
            finish();
            showMessage("лекарство добавлено!");
        }else
            showMessage("Есть пустые поля!!!");
    }
    private void showMessage(String str){
        Toast.makeText(this,str,Toast.LENGTH_LONG).show();
    }

    private boolean isEmpty() {
        if(TextUtils.isEmpty(name.getText().toString()))
            return true;
        if(TextUtils.isEmpty(maker.getText().toString()))
            return true;
        return TextUtils.isEmpty(price.getText().toString());
    }

    private void connection() {
        cancel = findViewById(R.id.cancel);
        save = findViewById(R.id.save);
        name = findViewById(R.id.name);
        unit = findViewById(R.id.unit);
        maker = findViewById(R.id.maker);
        price = findViewById(R.id.price);
    }

    private void creation() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.7));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

    }
}
