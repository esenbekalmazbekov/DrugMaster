package com.example.drugmaster.Model.ordermodel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, RefactorActivty.class);
                intent.putExtra("managerdata",manager);
                User client = activity.getIntent().getParcelableExtra("userdata");
                intent.putExtra("userdata",client);
                activity.startActivity(intent);
            }
        });

        firmname.setText(manager.getOrgname());
        username.setText(manager.getEmail());
        status.setText(orders.get(position).getStatus());
        cost.setText(orders.get(position).getCost().toString() + "сом");
    }

}
