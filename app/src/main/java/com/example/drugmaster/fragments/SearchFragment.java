package com.example.drugmaster.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
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

import com.example.drugmaster.Model.User;
import com.example.drugmaster.Model.searchmodel.SearchRequest;
import com.example.drugmaster.R;

public class SearchFragment extends Fragment {
    private SearchRequest searchRequest;
    @SuppressLint("StaticFieldLeak")
    private static ProgressBar progressBar;
    @SuppressLint("StaticFieldLeak")
    private static TextView notFound;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);

        final EditText search = view.findViewById(R.id.searchtext);
        final ImageButton srchbtn = view.findViewById(R.id.search);
        ListView searchlist = view.findViewById(R.id.searchList);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        notFound = view.findViewById(R.id.notFound);
        notFound.setVisibility(View.INVISIBLE);

        User client = getActivity().getIntent().getParcelableExtra("userdata");
        searchRequest = new SearchRequest(searchlist,getActivity(),client);

        srchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notFound.setVisibility(View.INVISIBLE);
                srchbtn.setBackgroundColor(Color.parseColor("#b00000"));
                progressBar.setVisibility(View.VISIBLE);
                searchRequest.getSearchByString(search.getText().toString());
                srchbtn.setBackgroundColor(Color.parseColor("#ff0055"));
            }
        });

        return view;
    }

    public static TextView getNotFound() {
        return notFound;
    }

    public static ProgressBar getProgressBar() {
        return progressBar;
    }
}
