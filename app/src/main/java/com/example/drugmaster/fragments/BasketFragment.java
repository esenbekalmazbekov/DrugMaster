package com.example.drugmaster.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.drugmaster.Model.ordermodel.AllOrdersRequest;
import com.example.drugmaster.R;

import java.util.Objects;

public class BasketFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basket,container,false);

        ListView orderViews = view.findViewById(R.id.orders);
        AllOrdersRequest allOrdersRequest = new AllOrdersRequest(orderViews, Objects.requireNonNull(getActivity()));
        allOrdersRequest.request();

        return view;
    }
}
