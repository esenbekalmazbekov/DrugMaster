package com.example.drugmaster.Model.achivemodel;

import androidx.annotation.NonNull;

import com.example.drugmaster.Model.User;
import com.example.drugmaster.Model.drugmodel.Drug;
import com.example.drugmaster.Model.ordermodel.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ArchiveResponse {
    private User client,manager;
    private Order order;
    private ArrayList<ShortDrug> druglist;
    public ArchiveResponse(User client, User manager, Order order) {
        this.client = client;
        this.manager = manager;
        this.order = order;
        druglist = new ArrayList<>();
    }

    public void response(){
        db().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    Drug drug = d.getValue(Drug.class);
                    if(order.getDrugs().containsKey(drug.getId())){
                        druglist.add(new ShortDrug(drug.getName(),order.getDrugs().get(drug.getId())));
                    }
                }

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    LocalDateTime now;
                    now = LocalDateTime.now();

                    DatabaseReference newDB = FirebaseDatabase.getInstance().getReference("archive").child(manager.getId());
                    String date = now.toString().replace('/','-');
                    date = date.substring(0,10);
                    String id = newDB.push().getKey();

                    Archive archive = new Archive(
                            id,
                            client.getOrgname(),
                            client.getEmail(),
                            client.getId(),
                            manager.getOrgname(),
                            manager.getEmail(),
                            druglist,
                            date,
                            "" + order.getCost()
                    );
                    newDB.child(archive.getID()).setValue(archive);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private DatabaseReference db(){
        return FirebaseDatabase.getInstance().getReference("drugs").child(manager.getId());
    }
}
