package com.example.drugmaster.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.drugmaster.Model.User;
import com.example.drugmaster.Model.ordermodel.AllOrdersRequest;
import com.example.drugmaster.Model.ordermodel.Order;
import com.example.drugmaster.Model.ordermodel.OrderStatus;
import com.example.drugmaster.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrdersFragment extends Fragment {
    private ArrayList<OrderStatus> orderStatuses;
    private ArrayList<Order> orders;
    private User manager;

    // View
    private ListView orderView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders,container,false);
        orders = new ArrayList<>();
        orderStatuses = new ArrayList<>();
        orderView = view.findViewById(R.id.orders);
        readStatus();
        return view;
    }

    private void readStatus(){
        manager = getActivity().getIntent().getParcelableExtra("userdata");

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("orders").child("managers").child(manager.getId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                OrdersFragment.this.orderStatuses.clear();
                OrdersFragment.this.orders.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    OrderStatus orderStatus = snapshot.getValue(OrderStatus.class);
                    if(!orderStatus.getStatus().equals("Заказ Создан"))
                        OrdersFragment.this.orderStatuses.add(orderStatus);
                }
                readOrders();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readOrders() {
        for (final OrderStatus orderStatus : orderStatuses){
            FirebaseDatabase.getInstance().getReference().child("orders").child("clients").child(orderStatus.getClientID()).child(manager.getId())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Order order = dataSnapshot.getValue(Order.class);
                            orders.add(order);
                            if(orderStatuses.size() == orders.size()){
                                AllOrdersRequest ordersRequest = new AllOrdersRequest(orderView,getActivity(),orderStatuses,orders);
                                ordersRequest.requestforManagers();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }
}
