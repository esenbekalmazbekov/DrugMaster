package com.example.drugmaster.Model.searchmodel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.drugmaster.Model.User;
import com.example.drugmaster.Model.ordermodel.Order;
import com.example.drugmaster.Model.ordermodel.OrderStatus;
import com.example.drugmaster.R;
import com.example.drugmaster.fragments.SearchFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchView extends ArrayAdapter<SearchResult> {
    private Activity activity;
    private ArrayList<SearchResult> searchResults;
    private User client;

    SearchView(Activity activity,ArrayList<SearchResult> searchResults,User client) {
        super(activity, R.layout.search_druglist,searchResults);
        this.activity = activity;
        this.searchResults = searchResults;
        this.client = client;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();

        @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(R.layout.search_druglist,null,true);
        initialization(listViewItem,position);

        return listViewItem;
    }
    @SuppressLint("SetTextI18n")
    private void initialization(View listViewItem, final int position){
        TextView name = listViewItem.findViewById(R.id.name);
        TextView unit = listViewItem.findViewById(R.id.unit);
        TextView maker = listViewItem.findViewById(R.id.maker);
        TextView price = listViewItem.findViewById(R.id.price);
        TextView firmname = listViewItem.findViewById(R.id.firmname);
        TextView userEmail = listViewItem.findViewById(R.id.userEmail);
        final Button button = listViewItem.findViewById(R.id.addbutton);

        name.setText(searchResults.get(position).getDrug().getName());
        unit.setText(searchResults.get(position).getDrug().getUnit());
        maker.setText(searchResults.get(position).getDrug().getMaker());
        price.setText(searchResults.get(position).getDrug().getPrice() + "сом");

        SearchFragment.getProgressBar().setVisibility(View.INVISIBLE);

        userEmail.setText(searchResults.get(position).getManagerID().getEmail());
        firmname.setText(searchResults.get(position).getManagerID().getOrgname());

        final DatabaseReference db = FirebaseDatabase.getInstance().getReference()
                .child("orders").child("clients").child(client.getId())
                .child(searchResults.get(position).getManagerID().getId());


        db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                        Order order = dataSnapshot.getValue(Order.class);
                        OrderStatus status = null;
                        if (order == null){
                            order = new Order(searchResults.get(position).getManagerID().getId());
                            status = new OrderStatus(client.getId(),"Заказ Создан");
                        }

                        if (!order.getDrugs().containsKey(searchResults.get(position).getDrug().getId())){
                            if(order.getStatus().equals("Заказ Создан")){
                                final Order finalOrder = order;
                                final OrderStatus finalStatus = status;
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        double plus = finalOrder.getCost() + Double.parseDouble(searchResults.get(position).getDrug().getPrice());
                                        int i = (int)(plus * 100);
                                        plus = (double) i / 100;
                                        finalOrder.setCost(plus);
                                        finalOrder.getDrugs().put(searchResults.get(position).getDrug().getId(),1);

                                        db.setValue(finalOrder);
                                        if(finalStatus != null){
                                            FirebaseDatabase.getInstance().getReference("orders").child("managers")
                                                    .child(searchResults.get(position).getManagerID().getId())
                                                    .child(client.getId()).setValue(finalStatus);

                                        }
                                        button.setClickable(false);
                                        button.setText("Добавлен");
                                        button.setBackgroundColor(Color.parseColor("#00db33"));
                                    }
                                });
                            }else {
                                button.setClickable(false);
                                button.setText("Недоступен");
                                button.setBackgroundColor(Color.parseColor("#b00000"));
                            }
                        }else {
                            button.setClickable(false);
                            button.setText("Добавлен");
                            button.setBackgroundColor(Color.parseColor("#00db33"));
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
