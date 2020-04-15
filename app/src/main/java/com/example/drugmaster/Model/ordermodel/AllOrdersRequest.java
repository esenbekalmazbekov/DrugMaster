package com.example.drugmaster.Model.ordermodel;

import android.app.Activity;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.example.drugmaster.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllOrdersRequest{
    private ListView orderView;
    private ArrayList<Order> orderArrayList;
    private Orderlist orderlist;
    private Activity activity;
    private User client;
    public AllOrdersRequest(ListView orderView,Activity activity) {
        this.orderView = orderView;
        this.activity = activity;
        orderArrayList = new ArrayList<>();
        client = activity.getIntent().getParcelableExtra("userdata");
    }

    public void request(){
        FirebaseDatabase.getInstance().getReference("orders").
                child("clients").
                child(client.getId()).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        orderArrayList.clear();
                        for(DataSnapshot d : dataSnapshot.getChildren()){
                            orderArrayList.add(d.getValue(Order.class));
                        }
                        orderlist = new Orderlist(activity,orderArrayList);
                        orderView.setAdapter(orderlist);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }
}
