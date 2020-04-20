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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.drugmaster.Activities.ClientActivity;
import com.example.drugmaster.Activities.ManagerActivity;
import com.example.drugmaster.Model.User;
import com.example.drugmaster.Model.drugmodel.DrugRequest;
import com.example.drugmaster.Model.ordermodel.Order;
import com.example.drugmaster.Model.ordermodel.OrderRequest;
import com.example.drugmaster.R;

import java.util.Objects;

public class ListFragment extends Fragment {
    public static final int LIST_FRAGMENT = 2;
    private ListView drugView;
    private ImageButton addDrugs, searchbtn;
    private EditText searchtext;
    private DrugRequest drugRequest;

    @SuppressLint("StaticFieldLeak")
    private static ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_list,container,false);
        initialize(fragment);
        final User manager;

        final Order order;

        if(ClientActivity.own){
            manager = Objects.requireNonNull(getActivity()).getIntent().getParcelableExtra("userdata");
            order = new Order(Objects.requireNonNull(manager).getId());
            drugRequest = new DrugRequest(drugView, Objects.requireNonNull(getActivity()), order);

            addDrugs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ManagerActivity managerActivity = (ManagerActivity)getActivity();
                    Objects.requireNonNull(managerActivity).createPopUpInfoChange(LIST_FRAGMENT,null);
                }
            });
        }
        else{
            final OrderRequest orderRequest = new OrderRequest(Objects.requireNonNull(getActivity()),drugRequest,drugView);
            orderRequest.requestSimpleOrder();

            addDrugs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderRequest.changeSimpleOrder();
                }
            });
        }

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drugRequest.request(searchtext.getText().toString());
            }
        });

        return fragment;
    }

    public static ProgressBar getProgressBar() {
        return progressBar;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (ClientActivity.own)
            drugRequest.request();
    }

    private void initialize(View fragment) {
        addDrugs = fragment.findViewById(R.id.addbutton);
        drugView = fragment.findViewById(R.id.listOfDrugs);
        searchbtn = fragment.findViewById(R.id.search);
        searchtext = fragment.findViewById(R.id.searchtext);
        progressBar = fragment.findViewById(R.id.progress);
    }
}
