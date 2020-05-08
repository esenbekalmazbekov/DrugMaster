package com.example.drugmaster.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.drugmaster.Model.achivemodel.ArchiveRequest;
import com.example.drugmaster.R;
import com.google.firebase.auth.FirebaseAuth;

public class ClientsFragment extends Fragment {
    private static TextView notFound;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clients,container,false);
        ListView archiveList = view.findViewById(R.id.archive);

        final EditText search = view.findViewById(R.id.searchtext);
        ImageButton btn = view.findViewById(R.id.search);
        notFound = view.findViewById(R.id.notFound);
        notFound.setVisibility(View.INVISIBLE);
        notFound.setText("Архив фирмы не найден");

        final String managerID = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        final ArchiveRequest request = new ArchiveRequest(archiveList,getActivity(),true);
        request.request(managerID);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(search.getText().toString().equals("")){
                    request.request(managerID);
                }else {
                    request.requestByFirm(managerID,search.getText().toString());
                }
            }
        });

        return view;
    }

    public static TextView getNotFound() {
        return notFound;
    }
}