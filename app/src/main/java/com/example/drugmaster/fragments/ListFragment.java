package com.example.drugmaster.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.drugmaster.Activities.ManagerActivity;
import com.example.drugmaster.Model.Drug;
import com.example.drugmaster.Model.Druglist;
import com.example.drugmaster.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ListFragment extends Fragment {
    public static final int LIST_FRAGMENT = 2;
    private ListView drugView;
    private ImageButton addDrugs;
    private ArrayList<Drug> drugarray;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_list,container,false);

        initialize(fragment);

        addDrugs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ManagerActivity managerActivity = (ManagerActivity)getActivity();
                Objects.requireNonNull(managerActivity).createPopUpInfoChange(LIST_FRAGMENT,null);
            }
        });
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        DatabaseReference db = FirebaseDatabase.
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

        drugarray = new ArrayList<>();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                drugarray.clear();
                for (DataSnapshot drugSnapshot: dataSnapshot.getChildren()){
                    Drug drug = drugSnapshot.getValue(Drug.class);

                    drugarray.add(drug);
                }

                Druglist adapter = new Druglist(getActivity(),drugarray);
                drugView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initialize(View fragment) {
        addDrugs = fragment.findViewById(R.id.addbutton);
        drugView = fragment.findViewById(R.id.listOfDrugs);
    }


}
