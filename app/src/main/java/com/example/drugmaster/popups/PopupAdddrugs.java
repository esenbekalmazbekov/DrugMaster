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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.drugmaster.Activities.ManagerActivity;
import com.example.drugmaster.Model.drugmodel.Drug;
import com.example.drugmaster.Model.ordermodel.Order;
import com.example.drugmaster.Model.ordermodel.OrderStatus;
import com.example.drugmaster.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class PopupAdddrugs extends AppCompatActivity {
    private EditText name, maker, price;
    private Spinner unit;
    private Button save,cancel;
    private Drug newdrug;
    private String userID;
    private String drugID;
    private Double drugOldPrice;
    private ArrayList<OrderStatus> activated;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_adddrugs);

        creation();
        connection();

        if(getIntent().getIntExtra("type",1) == ManagerActivity.CHANGE){
            newdrug = getIntent().getParcelableExtra("drug");
            if (newdrug != null) {
                drugID  = newdrug.getId();
                drugOldPrice = Double.parseDouble(newdrug.getPrice());
                name.setText(newdrug.getName());
                for (int i = 0;i < 4;i++){
                    if(unit.getItemAtPosition(i).toString().equals(newdrug.getUnit()))
                        unit.setSelection(i);
                }
                maker.setText(newdrug.getMaker());
                price.setText(newdrug.getPrice());
            }
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isChanged()){
                        changeDrug();
                        showMessage("Измененено!");
                    }else
                        showMessage("Имените данные или нажмите отмену!");
                }
            });

        }
        else {
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addDrug();
                }
            });
        }
        cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
    }

    private void changeDrug() {

        if(isEmpties()){
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("drugs");

            newdrug.setName(name.getText().toString());
            newdrug.setUnit(unit.getSelectedItem().toString());
            newdrug.setMaker(maker.getText().toString());
            newdrug.setPrice(price.getText().toString());
            userID = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            db.child(userID).child(newdrug.getId()).setValue(newdrug);
            showMessage("лекарство добавлено!");

            try{
                check();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }else
            showMessage("Есть пустые поля!!!");
    }

    private boolean isChanged() {
        if(!name.getText().toString().equals(newdrug.getName()))
            return true;
        if(!unit.getSelectedItem().toString().equals(newdrug.getUnit()))
            return true;
        if(!maker.getText().toString().equals(newdrug.getMaker()))
            return true;

        return !price.getText().toString().equals(newdrug.getPrice());
    }

    private void addDrug() {
        if(isEmpties()){
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("drugs");

            newdrug = new Drug(
                    db.push().getKey(),
                    name.getText().toString(),
                    unit.getSelectedItem().toString(),
                    maker.getText().toString(),
                    price.getText().toString());
            userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
            db.child(userID).child(newdrug.getId()).setValue(newdrug);
            showMessage("лекарство добавлено!");
            finish();
        }else
            showMessage("Есть пустые поля!!!");
    }

    private void check() throws NullPointerException {
        activated = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("orders").child("managers")
                .child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                activated.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    OrderStatus orderStatus = snapshot.getValue(OrderStatus.class);
                    if(orderStatus.getStatus().equals("Заказ Создан"))
                        activated.add(orderStatus);
                }
                if(activated.size() == 0){
                    finish();
                }
                else
                    deletion();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void deletion() throws NullPointerException{
        for (final OrderStatus orderStatus : activated) {
            FirebaseDatabase.getInstance().getReference().child("orders").child("clients").child(orderStatus.getClientID()).child(userID)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Order order = dataSnapshot.getValue(Order.class);
                            if (order.getDrugs().containsKey(drugID)) {
                                double minus = drugOldPrice * order.getDrugs().get(drugID);
                                int i = (int) (minus * 100);
                                minus = (double) i / 100;
                                minus = order.getCost() - minus;
                                i = (int) (minus * 100);
                                minus = (double) i / 100;
                                order.setCost(minus);
                                order.getDrugs().remove(drugID);
                                FirebaseDatabase.getInstance().getReference().child("orders").child("clients").child(orderStatus.getClientID()).child(userID).setValue(order);
                            }
                            if(activated.indexOf(orderStatus) == activated.size() - 1)
                                finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }

    private void showMessage(String str){
        Toast.makeText(this,str,Toast.LENGTH_LONG).show();
    }

    private boolean isEmpties() {
        if(TextUtils.isEmpty(name.getText().toString()))
            return false;
        if(TextUtils.isEmpty(maker.getText().toString()))
            return false;
        return !TextUtils.isEmpty(price.getText().toString());
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
