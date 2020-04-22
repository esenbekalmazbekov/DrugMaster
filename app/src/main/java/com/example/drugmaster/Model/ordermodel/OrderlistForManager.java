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

import com.example.drugmaster.Activities.ClientActivity;
import com.example.drugmaster.Activities.ManagerActivity;
import com.example.drugmaster.Activities.RefactorActivty;
import com.example.drugmaster.Model.User;
import com.example.drugmaster.R;
import com.example.drugmaster.fragments.InfoFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class OrderlistForManager extends ArrayAdapter<Order> {
    private Activity activity;
    private ArrayList<Order> orders;
    private ArrayList<OrderStatus> statuses;
    OrderlistForManager(Activity activity, ArrayList<Order> orders,ArrayList<OrderStatus> statuses) throws NullPointerException{
        super(activity, R.layout.basket_list,orders);
        this.activity = activity;
        this.orders = orders;
        this.statuses = statuses;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();

        @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(R.layout.basket_list,null,true);
        getClients(listViewItem,position);
        return listViewItem;
    }

    private void getClients(final View view, final int position){
        FirebaseDatabase.getInstance().
                getReference("users").
                child("clients").
                child(statuses.get(position).getClientID()).
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
    private void initialization(final View view, final int position, final User client){
        TextView firmname = view.findViewById(R.id.firmname);
        final TextView username = view.findViewById(R.id.userEmail);
        final TextView status = view.findViewById(R.id.status);
        TextView cost = view.findViewById(R.id.cost);
        ImageButton list = view.findViewById(R.id.databtn);
        ImageButton info = view.findViewById(R.id.infobtn);

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientActivity.own = false;
                ManagerActivity managerActivity = (ManagerActivity)activity;
                managerActivity.getIntent().putExtra("managerdata",client);
                managerActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,new InfoFragment())
                        .commit();
            }
        });

        final User manager = activity.getIntent().getParcelableExtra("userdata");
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, RefactorActivty.class);
                intent.putExtra("managerdata",manager);
                intent.putExtra("userdata",client);
                intent.putExtra("manager",true);
                activity.startActivity(intent);
            }
        });

        switch (orders.get(position).getStatus()){
            case "Заказ Отправлен":{
                final Button request = view.findViewById(R.id.act);
                final Button deleteOrder = view.findViewById(R.id.delete);
                status.setText("Новый заказ");
                request.setText("Принять");
                request.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        orders.get(position).setStatus("Заказ Принят");
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

                deleteOrder.setText("Отклонить");
                deleteOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        orders.get(position).setStatus("Заказ Отменен");
                        FirebaseDatabase.getInstance().getReference().
                                child("orders").child("clients").
                                child(client.getId()).
                                child(manager.getId()).setValue(orders.get(position));

                        OrderStatus newStatus = new OrderStatus(client.getId(),orders.get(position).getStatus());

                        FirebaseDatabase.getInstance().getReference().
                                child("orders").child("managers").
                                child(manager.getId()).
                                child(client.getId()).setValue(newStatus);

                        request.setVisibility(View.INVISIBLE);
                        deleteOrder.setVisibility(View.INVISIBLE);
                        status.setTextColor(Color.RED);
                        status.setText("Заказ Отклонен");
                    }
                });
                status.setTextColor(Color.BLUE);
            }break;
            case "Заказ Изменен":{
                final Button request = view.findViewById(R.id.act);
                final Button deleteOrder = view.findViewById(R.id.delete);
                status.setText("Заказ изменен");
                request.setText("Отправить");
                request.setVisibility(View.INVISIBLE);
                deleteOrder.setVisibility(View.INVISIBLE);
                status.setTextColor(Color.MAGENTA);
            }break;
            case "Заказ Принят":{
                final Button request = view.findViewById(R.id.act);
                final Button deleteOrder = view.findViewById(R.id.delete);

                status.setText("Заказ принят");

                status.setTextColor(Color.GREEN);
                request.setVisibility(View.INVISIBLE);
                deleteOrder.setVisibility(View.INVISIBLE);
            }break;
            case "Заказ Получен":{
                final Button deleteOrder = view.findViewById(R.id.delete);
                deleteOrder.setVisibility(View.INVISIBLE);

                final Button requrest = view.findViewById(R.id.act);
                requrest.setText("Получена");
                requrest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        orders.get(position).setStatus("Заказ Оплачен");
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
                status.setTextColor(Color.YELLOW);
                status.setText("Ждите оплату");
            }break;
            case "Заказ Оплачен":{
                status.setTextColor(Color.DKGRAY);
                status.setText("Заказ Оплачен");
            }break;
            case "Заказ Отменен":{
                final Button deleteOrder = view.findViewById(R.id.delete);
                deleteOrder.setVisibility(View.INVISIBLE);
                final Button requrest = view.findViewById(R.id.act);
                requrest.setVisibility(View.INVISIBLE);

                status.setTextColor(Color.RED);
                status.setText("Заказ Отменен");
            }
        }

        firmname.setText(client.getOrgname());
        username.setText(client.getEmail());
        cost.setText(orders.get(position).getCost().toString() + "сом");
    }
}
