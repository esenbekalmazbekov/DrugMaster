package com.example.drugmaster.Model.ordermodel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.drugmaster.Activities.RefactorActivty;
import com.example.drugmaster.Model.User;
import com.example.drugmaster.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class Orderlist extends ArrayAdapter<Order> {
    private Activity activity;
    private ArrayList<Order> orders;

    Orderlist(Activity activity, ArrayList<Order> orders){
        super(activity,R.layout.basket_list,orders);
        this.activity = activity;
        this.orders = orders;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();

        @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(R.layout.basket_list,null,true);
        ImageButton info = listViewItem.findViewById(R.id.infobtn);
        info.setVisibility(View.INVISIBLE);
        getManager(listViewItem,position);
        return listViewItem;
    }

    private void getManager(final View view, final int position){
        FirebaseDatabase.getInstance().
                getReference("users").
                child("managers").
                child(orders.get(position).getManagerID()).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        initialization(view,position, Objects.requireNonNull(user));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void initialization(final View view, final int position, final User manager){
        TextView firmname = view.findViewById(R.id.firmname);
        final TextView username = view.findViewById(R.id.userEmail);
        TextView status = view.findViewById(R.id.status);
        TextView cost = view.findViewById(R.id.cost);
        ImageButton list = view.findViewById(R.id.databtn);
        final User client = activity.getIntent().getParcelableExtra("userdata");

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, RefactorActivty.class);
                intent.putExtra("managerdata",manager);
                intent.putExtra("userdata",client);
                intent.putExtra("manager",false);
                activity.startActivity(intent);
            }
        });


        switch (orders.get(position).getStatus()){
            case "Заказ Создан":{
                final Button deleteOrder = view.findViewById(R.id.delete);
                deleteOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase.getInstance().getReference().
                                child("orders").child("clients").
                                child(client.getId()).
                                child(manager.getId()).setValue(null);

                        FirebaseDatabase.getInstance().getReference().
                                child("orders").child("managers").
                                child(manager.getId()).
                                child(client.getId()).setValue(null);
                    }
                });
                final Button requrest = view.findViewById(R.id.act);
                requrest.setText("Запросить");
                requrest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        orders.get(position).setStatus("Заказ Отправлен");
                        deleteOrder.setVisibility(View.INVISIBLE);
                        requrest.setVisibility(View.INVISIBLE);
                        FirebaseDatabase.getInstance().getReference().
                                child("orders").child("clients").
                                child(client.getId()).
                                child(manager.getId()).setValue(orders.get(position));

                        OrderStatus newStatus = new OrderStatus(client.getId(),orders.get(position).getStatus());

                        FirebaseDatabase.getInstance().getReference().
                                child("orders").child("managers").
                                child(manager.getId()).
                                child(client.getId()).setValue(newStatus);
                    }
                });
                status.setTextColor(Color.BLUE);
                status.setText(orders.get(position).getStatus());
            }break;
            case "Заказ Отправлен":{
                final Button deleteOrder = view.findViewById(R.id.delete);
                deleteOrder.setVisibility(View.INVISIBLE);
                final Button requrest = view.findViewById(R.id.act);
                requrest.setVisibility(View.INVISIBLE);
                status.setTextColor(Color.GREEN);
                status.setText(orders.get(position).getStatus());
            }break;
            case "Заказ Изменен":{
                final Button request = view.findViewById(R.id.act);
                final Button deleteOrder = view.findViewById(R.id.delete);

                request.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        orders.get(position).setStatus("Заказ Принят");
                        deleteOrder.setVisibility(View.INVISIBLE);
                        request.setVisibility(View.INVISIBLE);
                        FirebaseDatabase.getInstance().getReference().
                                child("orders").child("clients").
                                child(client.getId()).
                                child(manager.getId()).setValue(orders.get(position));

                        OrderStatus newStatus = new OrderStatus(client.getId(),orders.get(position).getStatus());

                        FirebaseDatabase.getInstance().getReference().
                                child("orders").child("managers").
                                child(manager.getId()).
                                child(client.getId()).setValue(newStatus);
                    }
                });
                deleteOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase.getInstance().getReference().
                                child("orders").child("clients").
                                child(client.getId()).
                                child(manager.getId()).setValue(null);

                        FirebaseDatabase.getInstance().getReference().
                                child("orders").child("managers").
                                child(manager.getId()).
                                child(client.getId()).setValue(null);
                    }
                });

                request.setText("Принять");
                status.setText(orders.get(position).getStatus());
                status.setTextColor(Color.MAGENTA);
            }break;
            case "Заказ Принят":{
                final Button deleteOrder = view.findViewById(R.id.delete);
                deleteOrder.setVisibility(View.INVISIBLE);

                final Button requrest = view.findViewById(R.id.act);
                requrest.setText("Получен");
                requrest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        orders.get(position).setStatus("Заказ Получен");
                        FirebaseDatabase.getInstance().getReference().
                                child("orders").child("clients").
                                child(client.getId()).
                                child(manager.getId()).setValue(orders.get(position));

                        OrderStatus newStatus = new OrderStatus(client.getId(),orders.get(position).getStatus());

                        FirebaseDatabase.getInstance().getReference().
                                child("orders").child("managers").
                                child(manager.getId()).
                                child(client.getId()).setValue(newStatus);
                    }
                });
                status.setTextColor(Color.parseColor("#ff5100"));
                status.setText("Ждите поставку");
            }break;
            case "Заказ Получен":{
                final Button deleteOrder = view.findViewById(R.id.delete);
                deleteOrder.setVisibility(View.INVISIBLE);
                final Button requrest = view.findViewById(R.id.act);
                requrest.setVisibility(View.INVISIBLE);

                status.setTextColor(Color.LTGRAY);
                status.setText("Отправьте оплату");
            }break;
            case "Заказ Оплачен":{
                status.setTextColor(Color.DKGRAY);
                status.setText("Заказ Оплачен");
            }break;
            case "Заказ Отменен":{
                final Button deleteOrder = view.findViewById(R.id.delete);
                deleteOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase.getInstance().getReference().
                                child("orders").child("clients").
                                child(client.getId()).
                                child(manager.getId()).setValue(null);

                        FirebaseDatabase.getInstance().getReference().
                                child("orders").child("managers").
                                child(manager.getId()).
                                child(client.getId()).setValue(null);
                    }
                });
                final Button requrest = view.findViewById(R.id.act);
                requrest.setVisibility(View.INVISIBLE);
                status.setTextColor(Color.RED);
                status.setText(orders.get(position).getStatus());
            }
        }

        firmname.setText(manager.getOrgname());
        username.setText(manager.getEmail());
        cost.setText(orders.get(position).getCost().toString() + "сом");
    }
}
