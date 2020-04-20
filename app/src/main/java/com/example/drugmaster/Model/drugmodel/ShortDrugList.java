package com.example.drugmaster.Model.drugmodel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.drugmaster.Activities.RefactorActivty;
import com.example.drugmaster.Model.ordermodel.Order;
import com.example.drugmaster.R;

import java.util.ArrayList;
import java.util.Objects;

public class ShortDrugList extends ArrayAdapter<Drug> {
    private Activity activity;
    private ArrayList<Drug> drugArrayList;
    private Order order;
    private boolean isManager;
    ShortDrugList(Activity activity, ArrayList<Drug> drugArrayList, Order order,boolean isManager) {
        super(activity, R.layout.short_drug_list,drugArrayList);
        this.activity = activity;
        this.drugArrayList = drugArrayList;
        this.order = order;
        this.isManager = isManager;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();

        @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(R.layout.short_drug_list,null,true);

        initialization(listViewItem,position);

        return listViewItem;
    }

    @SuppressLint("SetTextI18n")
    private void initialization(View listViewItem, final int position) {
        TextView drugname = listViewItem.findViewById(R.id.name);
        TextView unit = listViewItem.findViewById(R.id.unit);
        TextView price = listViewItem.findViewById(R.id.price);
        final EditText count = listViewItem.findViewById(R.id.count);
        final TextView itog = listViewItem.findViewById(R.id.itog);
        final CheckBox box = listViewItem.findViewById(R.id.selected);

        Button plus = listViewItem.findViewById(R.id.plus);
        final Button minus = listViewItem.findViewById(R.id.minus);

        if(isManager){
            if(order.getStatus().equals("Заказ Отправлен")){
                count.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(!count.getText().toString().equals("")){
                            int i = Integer.parseInt(count.getText().toString());
                            if(i <= 0){
                                Toast.makeText(activity, "Количество должно быть больше нуля!", Toast.LENGTH_LONG).show();
                                count.setText("" + 1);
                            }else{
                                double d;
                                order.getDrugs().put(drugArrayList.get(position).getId(),i);
                                d = i * Double.parseDouble(drugArrayList.get(position).getPrice());
                                d = d * 100;
                                i = (int)d;
                                d =  (double)i / 100;
                                itog.setText( d + "сом");
                                iflessThanOne(count,minus);
                            }
                        }
                    }
                });
                boxlistener(box,position);

                plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i = Integer.parseInt(count.getText().toString())+1;
                        newCost(i,count,position,itog,minus);
                    }
                });

                minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i = Integer.parseInt(count.getText().toString())-1;
                        newCost(i,count,position,itog,minus);
                    }
                });
            }
            else {
                count.setEnabled(false);
                plus.setVisibility(View.INVISIBLE);
                minus.setVisibility(View.INVISIBLE);
                box.setVisibility(View.INVISIBLE);
            }
        }else {
            if(order.getStatus().equals("Заказ Создан")){
                count.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(!count.getText().toString().equals("")){
                            int i = Integer.parseInt(count.getText().toString());
                            if(i <= 0){
                                Toast.makeText(activity, "Количество должно быть больше нуля!", Toast.LENGTH_LONG).show();
                                count.setText("" + 1);
                            }else{
                                double d;
                                order.getDrugs().put(drugArrayList.get(position).getId(),i);
                                d = i * Double.parseDouble(drugArrayList.get(position).getPrice());
                                d = d * 100;
                                i = (int)d;
                                d =  (double)i / 100;
                                itog.setText( d + "сом");
                                iflessThanOne(count,minus);
                            }
                        }
                    }
                });
                boxlistener(box,position);

                plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i = Integer.parseInt(count.getText().toString())+1;
                        newCost(i,count,position,itog,minus);
                    }
                });

                minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i = Integer.parseInt(count.getText().toString())-1;
                        newCost(i,count,position,itog,minus);
                    }
                });
            }
            else {
                count.setEnabled(false);
                plus.setVisibility(View.INVISIBLE);
                minus.setVisibility(View.INVISIBLE);
                box.setVisibility(View.INVISIBLE);
            }
        }


        drugname.setText(drugArrayList.get(position).getName());
        unit.setText(drugArrayList.get(position).getUnit());
        price.setText(drugArrayList.get(position).getPrice() + "с/ед");

        int i = Objects.requireNonNull(order.getDrugs().get(drugArrayList.get(position).getId()));
        newCost(i,count,position,itog,minus);
    }
    @SuppressLint("SetTextI18n")
    private void newCost(int i, EditText count, int position, TextView itog,Button minus){
        order.getDrugs().put(drugArrayList.get(position).getId(),i);
        count.setText("" + i);
        double d = i * Double.parseDouble(drugArrayList.get(position).getPrice());
        d = d * 100;
        i = (int)d;
        d =  (double)i / 100;
        itog.setText( d + "сом");

        iflessThanOne(count,minus);
    }
    private void boxlistener(final CheckBox box, final int postion){
        final RefactorActivty refactorActivty = (RefactorActivty) activity;
        final ArrayList<String> drugs = refactorActivty.getDeletedrugs();
        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(box.isChecked()){
                    drugs.add(drugArrayList.get(postion).getId());
                    order.setCost(order.getCost() - Double.parseDouble(drugArrayList.get(postion).getPrice()));
                }else{
                    drugs.remove(drugArrayList.get(postion).getId());
                    order.setCost(order.getCost() + Double.parseDouble(drugArrayList.get(postion).getPrice()));
                }
                refactorActivty.deleteButtonShow();
            }
        });
    }


    private void iflessThanOne(EditText count,Button minus){
        if(Integer.parseInt(count.getText().toString()) == 1)
            minus.setClickable(false);
        else
            minus.setClickable(true);
    }
}
