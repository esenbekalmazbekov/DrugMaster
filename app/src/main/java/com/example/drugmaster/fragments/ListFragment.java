package com.example.drugmaster.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.drugmaster.Activities.ManagerActivity;
import com.example.drugmaster.R;

import java.util.Objects;

public class ListFragment extends Fragment {
    public static final int LIST_FRAGMENT = 2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_list,container,false);
        Button addDrugs = fragment.findViewById(R.id.addbutton);
        addDrugs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ManagerActivity managerActivity = (ManagerActivity)getActivity();
                Objects.requireNonNull(managerActivity).createPopUpInfoChange(LIST_FRAGMENT);
            }
        });
        return fragment;
    }

}
