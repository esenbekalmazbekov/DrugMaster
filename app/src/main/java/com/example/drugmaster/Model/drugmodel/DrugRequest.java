package com.example.drugmaster.Model.drugmodel;

import android.app.Activity;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class DrugRequest {
    private ListView drugView;
    private ArrayList<Drug> drugarray;
    private Activity activity;
    private String search;
    public DrugRequest(ListView drugView,Activity activity) {
        this.drugView = drugView;
        this.activity = activity;
        drugarray = new ArrayList<>();
    }

    public void request(){
        DatabaseReference db = getReference();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                drugarray.clear();
                for (DataSnapshot drugSnapshot: dataSnapshot.getChildren()){
                    Drug drug = drugSnapshot.getValue(Drug.class);

                    drugarray.add(drug);
                }

                Druglist adapter = new Druglist(activity,drugarray);
                drugView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void request(final String search){
        DatabaseReference db = getReference();
        this.search = search;
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                drugarray.clear();
                for (DataSnapshot drugSnapshot: dataSnapshot.getChildren()){
                    Drug drug = drugSnapshot.getValue(Drug.class);
                    if(isHave(Objects.requireNonNull(drug)))
                        drugarray.add(drug);
                }

                Druglist adapter = new Druglist(activity,drugarray);
                drugView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean isHave(Drug drug) {
        if (drug.getName().contains(search))
            return true;
        if (drug.getMaker().contains(search))
            return true;
        if (drug.getUnit().contains(search))
            return true;

        return drug.getPrice().contains(search);
    }

    private DatabaseReference getReference(){
        return FirebaseDatabase.
                getInstance().
                getReference("drugs").
                child(
                        Objects.requireNonNull(
                                Objects.requireNonNull(
                                        FirebaseAuth.
                                                getInstance().
                                                getCurrentUser()
                                ).getDisplayName()
                        )
                );
    }
}
