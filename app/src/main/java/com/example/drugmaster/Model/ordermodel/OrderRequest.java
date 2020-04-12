package com.example.drugmaster.Model.ordermodel;

import android.app.Activity;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.example.drugmaster.Model.User;
import com.example.drugmaster.Model.drugmodel.DrugRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderRequest {
    private Order order;
    private DrugRequest drugRequest;
    private ListView drugView;
    private Activity activity;
    private User manager, client;

    public OrderRequest(Activity activity,DrugRequest drugRequest,ListView drugView){
        this.activity = activity;
        this.drugRequest = drugRequest;
        this.drugView = drugView;
        manager = activity.getIntent().getParcelableExtra("managerdata");
        client = activity.getIntent().getParcelableExtra("userdata");
    }

    public void requestSimpleOrder(){
        FirebaseDatabase.getInstance().getReference("orders").
                child("clients").
                child(client.getId()).
                child(manager.getId()).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        order = dataSnapshot.getValue(Order.class);

                        if(order == null)
                            order = new Order(manager.getId());

                        drugRequest = new DrugRequest(drugView,activity,order);
                        drugRequest.request();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }});
    }

    public void changeSimpleOrder(){
        if (order.getDrugs().size()!=0){
            FirebaseDatabase.getInstance().getReference().
                    child("orders").child("clients").
                    child(client.getId()).
                    child(manager.getId()).
                    setValue(order);

            OrderStatus status = new OrderStatus(client.getId(), order.getStatus());

            FirebaseDatabase.getInstance().getReference().
                    child("orders").child("managers").
                    child(manager.getId()).
                    child(client.getId()).
                    setValue(status);

        }else {
            FirebaseDatabase.getInstance().getReference().
                    child("orders").child("clients").
                    child(client.getId()).
                    child(manager.getId()).setValue(null);

            FirebaseDatabase.getInstance().getReference().
                    child("orders").child("managers").
                    child(manager.getId()).
                    child(client.getId()).setValue(null);
        }
    }
}
