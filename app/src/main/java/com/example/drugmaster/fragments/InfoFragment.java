package com.example.drugmaster.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.drugmaster.Activities.ClientActivity;
import com.example.drugmaster.Activities.ManagerActivity;
import com.example.drugmaster.Model.User;
import com.example.drugmaster.R;

import java.util.Objects;

public class InfoFragment extends Fragment {
    public static final int INFO_FRAGMENT = 1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_info,container,false);
        final User user = Objects.requireNonNull(getActivity()).getIntent().getParcelableExtra("userdata");

        TextView orgName = fragment.findViewById(R.id.orgName)
                ,email = fragment.findViewById(R.id.mail)
                ,phone = fragment.findViewById(R.id.phone)
                ,address = fragment.findViewById(R.id.address)
                ,status = fragment.findViewById(R.id.status);

        orgName.setText(Objects.requireNonNull(user).getOrgname());
        email.setText(user.getEmail());
        phone.setText(user.getPhone());
        address.setText(user.getAddress());
        status.setText(user.getStatus());
        Button change = fragment.findViewById(R.id.changebtn);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getStatus().equals("manager")){
                    ManagerActivity managerActivity = (ManagerActivity)getActivity();
                    Objects.requireNonNull(managerActivity).createPopUpInfoChange(INFO_FRAGMENT,null);
                }else {
                    ClientActivity clientActivity = (ClientActivity)getActivity();
                    Objects.requireNonNull(clientActivity).createPopUpInfoChange();
                }
            }
        });
        return fragment;
    }

}