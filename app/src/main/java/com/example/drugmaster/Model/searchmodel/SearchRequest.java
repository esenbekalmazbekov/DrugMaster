package com.example.drugmaster.Model.searchmodel;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.drugmaster.Model.User;
import com.example.drugmaster.Model.drugmodel.Drug;
import com.example.drugmaster.fragments.SearchFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchRequest {
    private ListView listView;
    private ArrayList<SearchResult> searchResults;
    private Activity activity;
    private User client;

    public SearchRequest(ListView searchlist, FragmentActivity activity,User client) {
        listView = searchlist;
        this.activity = activity;
        this.client = client;
        searchResults = new ArrayList<>();
    }
    public void getSearchByString(final String str){
        FirebaseDatabase.getInstance().getReference("drugs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                searchResults.clear();
                String managerID;
                final User[] user = {null};
                for (DataSnapshot d : dataSnapshot.getChildren()){
                    managerID = d.getKey();
                    for(DataSnapshot d1 : d.getChildren()){
                        final Drug drug = d1.getValue(Drug.class);
                        if(drug.getName().contains(str)){
                            if(user[0] == null || user[0].getId().equals(managerID)){
                                FirebaseDatabase.getInstance().getReference("users").child("managers").child(managerID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        user[0] = dataSnapshot.getValue(User.class);
                                        searchResults.add(new SearchResult(user[0],drug));
                                        SearchFragment.getNotFound().setVisibility(View.VISIBLE);

                                        SearchView searchView = new SearchView(activity,searchResults,client);
                                        listView.setAdapter(searchView);
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }else {
                                searchResults.add(new SearchResult(user[0],drug));

                                SearchView searchView = new SearchView(activity,searchResults,client);
                                listView.setAdapter(searchView);
                            }
                        }
                    }
                }
                SearchFragment.getNotFound().setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
