package com.example.drugmaster.Model.drugmodel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

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

    public ShortDrugList(Activity activity, ArrayList<Drug> drugArrayList, Order order) {
        super(activity, R.layout.short_drug_list,drugArrayList);
        this.activity = activity;
        this.drugArrayList = drugArrayList;
        this.order = order;
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
    private void initialization(View listViewItem, int position) {
        TextView drugname = listViewItem.findViewById(R.id.name);
        TextView unit = listViewItem.findViewById(R.id.unit);
        TextView price = listViewItem.findViewById(R.id.price);
        EditText count = listViewItem.findViewById(R.id.count);
        TextView itog = listViewItem.findViewById(R.id.itog);
        final CheckBox box = listViewItem.findViewById(R.id.selected);

        boxlistener(box,position);

        drugname.setText(drugArrayList.get(position).getName());
        unit.setText(drugArrayList.get(position).getUnit());
        price.setText(drugArrayList.get(position).getPrice() + "с/ед");

        count.setText(Objects.requireNonNull(order.getDrugs().get(drugArrayList.get(position).getId())).toString());
        itog.setText(drugArrayList.get(position).getPrice() + "сом");
    }

    private void boxlistener(final CheckBox box, final int postion){
        final RefactorActivty refactorActivty = (RefactorActivty) activity;
        final ArrayList<String> drugs = refactorActivty.getDeletedrugs();
        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(box.isChecked()){
                    drugs.add(drugArrayList.get(postion).getId());
                }else{
                    drugs.remove(drugArrayList.get(postion).getId());
                }
                refactorActivty.deleteButtonShow();
            }
        });
    }
}
