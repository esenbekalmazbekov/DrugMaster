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
    private Activity activity;
    private User client;
    private ArrayList<OrderStatus> orderStatuses;

    public AllOrdersRequest(ListView orderView,Activity activity) {
        this.orderView = orderView;
        this.activity = activity;
        orderArrayList = new ArrayList<>();
        client = activity.getIntent().getParcelableExtra("userdata");
    }
    public AllOrdersRequest(ListView orderView,Activity activity,ArrayList<OrderStatus> orderStatuses,ArrayList<Order> orderArrayList){
        this.orderView = orderView;
        this.activity = activity;
        this.orderStatuses = orderStatuses;
        this.orderArrayList = orderArrayList;
    }

    public void requestforManagers(){
        OrderlistForManager orderlist;
        orderlist = new OrderlistForManager(activity,orderArrayList,orderStatuses);
        orderView.setAdapter(orderlist);
    }

    public void request(){
        final Orderlist[] orderlist = {new Orderlist(activity, orderArrayList)};
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
                        orderlist[0] = new Orderlist(activity,orderArrayList);
                        orderView.setAdapter(orderlist[0]);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }
}
