package com.example.drugmaster.Model.managermodel;

import android.app.Activity;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.example.drugmaster.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ManagerRequest {
    private ListView managerView;
    private ArrayList<User> managerArray;
    private Activity activity;
    private String search;

    public ManagerRequest(ListView managerView, Activity activity) {
        this.managerView = managerView;
        this.managerArray = new ArrayList<>();
        this.activity = activity;
    }

    public void request(){
        DatabaseReference db = getReference();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                managerArray.clear();
                for (DataSnapshot drugSnapshot: dataSnapshot.getChildren()){
                    User user = drugSnapshot.getValue(User.class);

                    managerArray.add(user);
                }

                Managerlist adapter = new Managerlist(activity,managerArray);
                managerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void request(final String search){
        DatabaseReference db = getReference();
        this.search = search;
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                managerArray.clear();
                for (DataSnapshot drugSnapshot: dataSnapshot.getChildren()){
                    User user = drugSnapshot.getValue(User.class);
                    if(isHave(Objects.requireNonNull(user)))
                        managerArray.add(user);
                }

                Managerlist adapter = new Managerlist(activity,managerArray);
                managerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean isHave(User manager) {
        if (manager.getOrgname().contains(search))
            return true;
        return manager.getEmail().contains(search);
    }

    private DatabaseReference getReference(){
        return FirebaseDatabase.
                getInstance().
                getReference().
                child("users").
                child("managers");
    }
}
