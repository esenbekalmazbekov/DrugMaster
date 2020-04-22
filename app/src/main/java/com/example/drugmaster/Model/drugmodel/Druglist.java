package com.example.drugmaster.Model.drugmodel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.drugmaster.Activities.ClientActivity;
import com.example.drugmaster.Activities.ManagerActivity;
import com.example.drugmaster.Model.ordermodel.Order;
import com.example.drugmaster.Model.ordermodel.OrderStatus;
import com.example.drugmaster.R;
import com.example.drugmaster.popups.DialogBox;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class Druglist extends ArrayAdapter<Drug> {
    private Activity activity;
    private ArrayList<Drug> drugList;
    public static final int DRUGLIST_FRAGMENT_CHANGE = 3;
    private Button change;
    private Button delete;
    private Order order;

    Druglist(Activity activity, ArrayList<Drug> drugList,Order order) {
        super(activity, R.layout.drug_list,drugList);
        this.activity = activity;
        this.drugList = drugList;
        this.order = order;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();

        @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(R.layout.drug_list,null,true);

        initialization(listViewItem,position);

        return listViewItem;
    }

    private void buttonFunctions(Drug drug) {

        try {
            change.setOnClickListener(new ChangeListener(drug.clone(),activity));
            delete.setOnClickListener(new DeleteListener(drug.clone(),activity));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    private void initialization(View listViewItem, int position) {

        TextView name = listViewItem.findViewById(R.id.name);
        TextView unit = listViewItem.findViewById(R.id.unit);
        final TextView maker = listViewItem.findViewById(R.id.maker);
        TextView price = listViewItem.findViewById(R.id.price);

        change = listViewItem.findViewById(R.id.change);
        delete = listViewItem.findViewById(R.id.delet);

        final CheckBox box = listViewItem.findViewById(R.id.checkBox);

        final Drug drug = drugList.get(position);

        if(order!=null){
            if(order.getDrugs().containsKey(drug.getId()))
                box.setChecked(true);
        }

        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(box.isChecked()){
                    order.getDrugs().put(drug.getId(),1);
                    double plus = order.getCost() + Double.parseDouble(drug.getPrice());
                    int i = (int)(plus * 100);
                    plus = (double) i / 100;
                    order.setCost(plus);
                }else{
                    double minus = Double.parseDouble(drug.getPrice()) * order.getDrugs().get(drug.getId());
                    minus = order.getCost() - minus;
                    int i = (int)(minus * 100);
                    minus = (double) i / 100;
                    order.setCost(minus);
                    order.getDrugs().remove(drug.getId());
                }
            }
        });

        name.setText(drug.getName());
        unit.setText(drug.getUnit());
        maker.setText(drug.getMaker());
        price.setText(drug.getPrice() + " с");

        if(!ClientActivity.own){
            change.setVisibility(View.INVISIBLE);
            delete.setVisibility(View.INVISIBLE);
        }else{
            buttonFunctions(drug);
            box.setVisibility(View.INVISIBLE);
        }
    }
}


class DeleteListener implements View.OnClickListener {
    private Drug drug;
    private ManagerActivity activity;
    private ArrayList<OrderStatus> orderStatuses;
    private ArrayList<Order> orderArrayList;
    private ArrayList<OrderStatus> activated;
    private boolean drugInOrder = false;

    DeleteListener(Drug deteteById, Activity activity) {
        this.activity = (ManagerActivity) activity;
        this.drug = deteteById;
    }

    @Override
    public void onClick(View v) {
        try {
            check();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void check() throws NullPointerException {
        orderStatuses = new ArrayList<>();
        orderArrayList = new ArrayList<>();
        activated = new ArrayList<>();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("orders").child("managers").child(activity.getUser().getId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderStatuses.clear();
                orderArrayList.clear();
                activated.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    OrderStatus orderStatus = snapshot.getValue(OrderStatus.class);
                    if (!orderStatus.getStatus().equals("Заказ Создан"))
                        orderStatuses.add(orderStatus);
                    else
                        activated.add(orderStatus);
                }
                if (orderStatuses.size() == 0)
                    deletion();
                else
                    readOrders();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readOrders() {
        for (final OrderStatus orderStatus : orderStatuses) {
            FirebaseDatabase.getInstance().getReference().child("orders").child("clients").child(orderStatus.getClientID()).child(activity.getUser().getId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Order order = dataSnapshot.getValue(Order.class);
                            orderArrayList.add(order);
                            if (order.getDrugs().containsKey(drug.getId())) {
                                drugInOrder = true;
                            }
                            if (orderStatuses.size() == orderArrayList.size() && !drugInOrder) {
                                deletion();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
            if (drugInOrder) {
                DialogBox dialogBox = new DialogBox(
                        "Внимание!",
                        "Это лекарство находится в принятом(ых) заказе(ах), если нужно удалить это лекаство отменяйте в последующих заказах и ждите завершения нынешних!!!"
                );
                dialogBox.setMustDestroy(false);
                dialogBox.show(activity.getSupportFragmentManager(), "example dialog");
                break;
            }

        }
    }

    private void deletion() {
        final String drugID = drug.getId();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().
                child("drugs").
                child(Objects.requireNonNull(
                        Objects.requireNonNull(FirebaseAuth.
                                getInstance().
                                getCurrentUser()
                        ).getDisplayName())
                ).child(drug.getId());
        db.removeValue();
        if (activated.size() != 0) {
            for (final OrderStatus orderStatus : activated) {
                FirebaseDatabase.getInstance().getReference().child("orders").child("clients").child(orderStatus.getClientID()).child(activity.getUser().getId())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Order order = dataSnapshot.getValue(Order.class);
                                if (order.getDrugs().containsKey(drugID)) {
                                    double minus = Double.parseDouble(drug.getPrice()) * order.getDrugs().get(drugID);
                                    int i = (int) (minus * 100);
                                    minus = (double) i / 100;
                                    minus = order.getCost() - minus;
                                    i = (int) (minus * 100);
                                    minus = (double) i / 100;
                                    order.setCost(minus);
                                    order.getDrugs().remove(drugID);
                                    FirebaseDatabase.getInstance().getReference().child("orders").child("clients").child(orderStatus.getClientID()).child(activity.getUser().getId()).setValue(order);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        }
    }
}


class ChangeListener implements View.OnClickListener{
    private Drug drug;
    private ManagerActivity activity;
    private ArrayList<OrderStatus> orderStatuses;
    private ArrayList<Order> orderArrayList;
    private boolean drugInOrder = false;

    ChangeListener(Drug drug, Activity activity) {
        this.drug = drug;
        this.activity = (ManagerActivity) activity;
    }

    @Override
    public void onClick(View v) {
        try {
            check();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    private void check() throws NullPointerException{
        orderStatuses = new ArrayList<>();
        orderArrayList = new ArrayList<>();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("orders").child("managers").child(activity.getUser().getId());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderStatuses.clear();
                orderArrayList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    OrderStatus orderStatus = snapshot.getValue(OrderStatus.class);
                    if(!orderStatus.getStatus().equals("Заказ Создан"))
                        orderStatuses.add(orderStatus);
                }
                if(orderStatuses.size() == 0)
                    changing();
                else
                    readOrders();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void readOrders() {
        for (final OrderStatus orderStatus : orderStatuses){
            FirebaseDatabase.getInstance().getReference().child("orders").child("clients").child(orderStatus.getClientID()).child(activity.getUser().getId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Order order = dataSnapshot.getValue(Order.class);
                            orderArrayList.add(order);
                            if(order.getDrugs().containsKey(drug.getId())){
                                drugInOrder = true;
                            }
                            if(orderStatuses.size() == orderArrayList.size() && !drugInOrder){
                                changing();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
            if (drugInOrder){
                DialogBox dialogBox = new DialogBox(
                        "Внимание!",
                        "Это лекарство находится в принятом(ых) заказе(ах), если нужно изменить это лекаство отменяйте в последующих заказах и ждите завершения нынешних!!!"
                );
                dialogBox.setMustDestroy(false);
                dialogBox.show(activity.getSupportFragmentManager(),"example dialog");
                break;
            }

        }
    }

    private void changing() {
        Objects.requireNonNull(activity).createPopUpInfoChange(Druglist.DRUGLIST_FRAGMENT_CHANGE,drug);
    }
}