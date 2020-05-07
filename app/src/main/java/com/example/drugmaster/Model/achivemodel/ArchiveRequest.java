package com.example.drugmaster.Model.achivemodel;

import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ArchiveRequest {
    private ListView arcList;
    public ArchiveRequest(ListView archiveList) {
        arcList = archiveList;
    }

    public void request(){

    }


    private DatabaseReference db(){
        return FirebaseDatabase.getInstance().getReference("archive");
    }
}
