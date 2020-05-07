package com.example.drugmaster.Model.ordermodel;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.example.drugmaster.Activities.ClientActivity;
import com.example.drugmaster.Model.User;
import com.example.drugmaster.Model.drugmodel.DrugRequest;
import com.example.drugmaster.fragments.ListFragment;
import com.example.drugmaster.popups.DialogBox;
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

    public OrderRequest(Activity activity,ListView drugView){
        this.activity = activity;
        this.drugView = drugView;
        manager = activity.getIntent().getParcelableExtra("managerdata");
        client = activity.getIntent().getParcelableExtra("userdata");
    }

    public void requestShortOrder(final boolean isManager){
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
                        drugRequest.getshortlist(isManager);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }});
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

                        if (!order.getStatus().equals("Заказ Создан")){
                            DialogBox dialogBox = new DialogBox("Внимание!","У вас уже есть заказ с этого Менеджера");
                            ClientActivity clientActivity = (ClientActivity)activity;
                            dialogBox.show(clientActivity.getSupportFragmentManager(),"example dialog");
                            ListFragment.getProgressBar().setVisibility(View.INVISIBLE);
                        }else {
                            drugRequest = new DrugRequest(drugView,activity,order);
                            drugRequest.request();
                        }
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

    public Order getOrder() {
        return order;
    }

    public DrugRequest getDrugRequest(){return drugRequest;}
}
