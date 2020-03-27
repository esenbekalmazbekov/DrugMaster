package com.example.drugmaster.Model.managermodel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.drugmaster.Model.User;
import com.example.drugmaster.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Managerlist extends ArrayAdapter<User> {
    private Activity activity;
    private ArrayList<User> managerList;

    Managerlist(Activity activity, ArrayList<User> managerList) {
        super(activity, R.layout.manager_list,managerList);
        this.activity = activity;
        this.managerList = managerList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();

        @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(R.layout.manager_list,null,true);

        User user = initialization(listViewItem,position);

        return listViewItem;
    }

    private User initialization(View listViewItem, int position){
        CircleImageView profileImage = listViewItem.findViewById(R.id.profile_image);
        TextView name = listViewItem.findViewById(R.id.firmname);
        TextView username = listViewItem.findViewById(R.id.username);



        User user = managerList.get(position);

        name.setText(user.getOrgname());
        username.setText(user.getEmail());

        return user;
    }
}
