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

import com.example.drugmaster.Model.managermodel.ManagerRequest;
import com.example.drugmaster.R;

public class ManagerFragment extends Fragment {
    private ListView managers;
    private EditText searchtext;
    private ImageButton search;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_manager,container,false);
        initialize(fragment);

        return fragment;
    }

    private void initialize(View fragment) {
        managers = fragment.findViewById(R.id.listOfManagers);
        searchtext = fragment.findViewById(R.id.searchtext);
        search = fragment.findViewById(R.id.search);
    }

    @Override
    public void onStart() {
        super.onStart();
        new ManagerRequest(managers,getActivity()).request();
    }
}
