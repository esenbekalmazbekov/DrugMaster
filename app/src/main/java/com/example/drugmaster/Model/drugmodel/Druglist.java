package com.example.drugmaster.Model.drugmodel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.drugmaster.Activities.ManagerActivity;
import com.example.drugmaster.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class Druglist extends ArrayAdapter<Drug> {
    private Activity activity;
    private ArrayList<Drug> drugList;
    public static final int DRUGLIST_FRAGMENT_CHANGE = 3;

    Druglist(Activity activity, ArrayList<Drug> drugList) {
        super(activity, R.layout.drug_list,drugList);
        this.activity = activity;
        this.drugList = drugList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();

        @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(R.layout.drug_list,null,true);

        Drug drug = initialization(listViewItem,position);

        buttonFunctions(listViewItem,drug);

        return listViewItem;
    }

    private void buttonFunctions(View listViewItem,Drug drug) {
        Button change = listViewItem.findViewById(R.id.change);
        Button delete = listViewItem.findViewById(R.id.delet);
        try {
            change.setOnClickListener(new ChangeListener(drug.clone(),activity));
            delete.setOnClickListener(new DeleteListener(drug.clone()));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    private Drug initialization(View listViewItem, int position) {

        TextView name = listViewItem.findViewById(R.id.name);
        TextView unit = listViewItem.findViewById(R.id.unit);
        TextView maker = listViewItem.findViewById(R.id.maker);
        TextView price = listViewItem.findViewById(R.id.price);

        Drug drug = drugList.get(position);
        name.setText(drug.getName());
        unit.setText(drug.getUnit());
        maker.setText(drug.getMaker());
        price.setText(drug.getPrice() + " с");
        return drug;
    }

}

class DeleteListener implements View.OnClickListener{
    private Drug drug;
    DeleteListener(Drug deteteById) {
        this.drug = deteteById;
    }
    @Override
    public void onClick(View v) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().
                child("drugs").
                child(Objects.requireNonNull(
                        Objects.requireNonNull(FirebaseAuth.
                                getInstance().
                                getCurrentUser()
                        ).getDisplayName())
                ).child(drug.getId());

        db.removeValue();
    }
}

class ChangeListener implements View.OnClickListener{
    private Drug drug;
    private Activity activity;
    ChangeListener(Drug drug, Activity activity) {
        this.drug = drug;
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        ManagerActivity managerActivity = (ManagerActivity) activity;
        Objects.requireNonNull(managerActivity).createPopUpInfoChange(Druglist.DRUGLIST_FRAGMENT_CHANGE,drug);
    }
}