package com.example.drugmaster.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.drugmaster.Model.achivemodel.ArchiveRequest;
import com.example.drugmaster.R;

public class ClientsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clients,container,false);
        ListView archiveList = view.findViewById(R.id.archive);

        ArchiveRequest request = new ArchiveRequest(archiveList);
        request.request();

        return view;
    }
}