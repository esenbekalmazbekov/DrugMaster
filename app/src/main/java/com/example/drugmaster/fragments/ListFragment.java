package com.example.drugmaster.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.drugmaster.Activities.ManagerActivity;
import com.example.drugmaster.Model.drugmodel.DrugRequest;
import com.example.drugmaster.R;

import java.util.Objects;

public class ListFragment extends Fragment {
    public static final int LIST_FRAGMENT = 2;
    private ListView drugView;
    private ImageButton addDrugs, searchbtn;
    private EditText searchtext;

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

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DrugRequest(drugView,getActivity()).request(searchtext.getText().toString());
            }
        });

        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        new DrugRequest(drugView,getActivity()).request();
    }

    private void initialize(View fragment) {
        addDrugs = fragment.findViewById(R.id.addbutton);
        drugView = fragment.findViewById(R.id.listOfDrugs);
        searchbtn = fragment.findViewById(R.id.search);
        searchtext = fragment.findViewById(R.id.searchtext);
    }
}
