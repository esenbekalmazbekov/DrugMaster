package com.example.drugmaster.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.drugmaster.Activities.ClientActivity;
import com.example.drugmaster.Model.User;
import com.example.drugmaster.Model.achivemodel.ArchiveRequest;
import com.example.drugmaster.R;
import com.google.firebase.auth.FirebaseAuth;

public class ClientsFragment extends Fragment {

    @SuppressLint("StaticFieldLeak")
    private static TextView notFound;
    @SuppressLint("StaticFieldLeak")
    private static ProgressBar bar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clients,container,false);
        ListView archiveList = view.findViewById(R.id.archive);

        final EditText search = view.findViewById(R.id.searchtext);
        ImageButton btn = view.findViewById(R.id.search);
        notFound = view.findViewById(R.id.notFound);
        notFound.setVisibility(View.INVISIBLE);
        bar = view.findViewById(R.id.bar);

        final String managerID;
        final ArchiveRequest request = new ArchiveRequest(archiveList,getActivity(), ClientActivity.own);
        if(ClientActivity.own){
            managerID = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            request.request(managerID);

            notFound.setText("Архив фирмы не найден");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bar.setVisibility(View.VISIBLE);
                    if(search.getText().toString().equals("")){
                        request.request(managerID);
                    }else {
                        request.requestByFirm(managerID,search.getText().toString());
                    }
                }
            });

        }else {
            search.setVisibility(View.INVISIBLE);
            btn.setVisibility(View.INVISIBLE);
            User manager = getActivity().getIntent().getParcelableExtra("managerdata");
            managerID = manager.getId();
            notFound.setText("У вас нет архивов");

            request.requestByID(managerID,ClientActivity.getUser().getId());
        }

        return view;
    }

    public static ProgressBar getBar() {
        return bar;
    }

    public static TextView getNotFound() {
        return notFound;
    }
}