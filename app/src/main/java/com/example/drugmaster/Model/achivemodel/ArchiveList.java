package com.example.drugmaster.Model.achivemodel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.drugmaster.R;
import com.example.drugmaster.popups.ArchivePupups;

import java.util.ArrayList;

public class ArchiveList extends ArrayAdapter<Archive> {
    private final Activity activity;
    private final ArrayList<Archive> archives;
    private final boolean inManager;
    ArchiveList(Activity activity, ArrayList<Archive> archives,boolean inManager){
        super(activity, R.layout.archive_list,archives);
        this.activity = activity;
        this.archives = archives;
        this.inManager = inManager;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();

        @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(R.layout.archive_list,null,true);
        init(listViewItem,position);

        return listViewItem;
    }

    @SuppressLint("SetTextI18n")
    private void init(View view, final int pos){
        TextView firmname = view.findViewById(R.id.firmname);
        TextView userEmail = view.findViewById(R.id.userEmail);
        TextView date = view.findViewById(R.id.date);
        TextView price = view.findViewById(R.id.price);
        ImageButton dbn = view.findViewById(R.id.databtn);

        if(inManager){
            firmname.setText(archives.get(pos).getClOrgName());
            userEmail.setText(archives.get(pos).getClientEmail());
            date.setText(archives.get(pos).getDate());
            price.setText(archives.get(pos).getPrice()+"сом");
            dbn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, ArchivePupups.class);
                    intent.putParcelableArrayListExtra("drugs",archives.get(pos).getList());
                    activity.startActivity(intent);
                }
            });
        }
    }
}
