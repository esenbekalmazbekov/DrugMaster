package com.example.drugmaster.Model.achivemodel;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.example.drugmaster.fragments.ClientsFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ArchiveRequest {
    private ListView arcList;
    private ArrayList<Archive> archives;
    private Activity activity;
    private final boolean isManager;
    public ArchiveRequest(ListView archiveList,Activity activity,boolean isManager) {
        archives = new ArrayList<>();
        arcList = archiveList;
        this.activity = activity;
        this.isManager = isManager;
    }

    public void requestByFirm(String managerID, String firmname){
        archives.clear();
        Query query = db().child(managerID).orderByChild("clOrgName").equalTo(firmname);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    Archive archive = d.getValue(Archive.class);
                    archives.add(archive);
                    arcList.setAdapter(new ArchiveList(activity,archives,isManager));
                }
                if (archives.size() > 0)
                    ClientsFragment.getNotFound().setVisibility(View.INVISIBLE);
                ClientsFragment.getBar().setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ClientsFragment.getNotFound().setVisibility(View.VISIBLE);
        arcList.setAdapter(new ArchiveList(activity,archives,isManager));
    }

    public void request(String managerID){
        db().child(managerID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                archives.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()){
                    Archive archive = d.getValue(Archive.class);
                    archives.add(archive);
                    arcList.setAdapter(new ArchiveList(activity,archives,isManager));
                }
                ClientsFragment.getNotFound().setVisibility(View.INVISIBLE);
                ClientsFragment.getBar().setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private DatabaseReference db(){
        return FirebaseDatabase.getInstance().getReference("archive");
    }

    public void requestByID(String managerID,String clientID) {
        archives.clear();
        Query query = db().child(managerID).orderByChild("clientID").equalTo(clientID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    Archive archive = d.getValue(Archive.class);
                    archives.add(archive);
                    arcList.setAdapter(new ArchiveList(activity,archives,isManager));
                }
                if (archives.size() > 0)
                    ClientsFragment.getNotFound().setVisibility(View.INVISIBLE);

                ClientsFragment.getBar().setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ClientsFragment.getNotFound().setVisibility(View.VISIBLE);
        arcList.setAdapter(new ArchiveList(activity,archives,isManager));
    }
}
