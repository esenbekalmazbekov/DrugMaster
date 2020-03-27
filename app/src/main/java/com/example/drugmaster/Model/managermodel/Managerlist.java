package com.example.drugmaster.Model.managermodel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.drugmaster.Activities.ClientActivity;
import com.example.drugmaster.Model.User;
import com.example.drugmaster.R;

import java.io.InputStream;
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

        ImageButton info = listViewItem.findViewById(R.id.infobtn);
        ImageButton data = listViewItem.findViewById(R.id.databtn);

        info.setOnClickListener(new ImageBtnListerer(activity,"info",user));

        return listViewItem;
    }

    private User initialization(View listViewItem, int position){
        CircleImageView profileImage = listViewItem.findViewById(R.id.profile_image);
        TextView name = listViewItem.findViewById(R.id.firmname);
        TextView username = listViewItem.findViewById(R.id.username);

        User user = managerList.get(position);

        new GetProfileFromURL(profileImage).execute(user.getProfileUri());

        name.setText(user.getOrgname());
        username.setText(user.getEmail());

        return user;
    }

    @SuppressLint("StaticFieldLeak")
    public static class GetProfileFromURL extends AsyncTask<String,Void,Bitmap>{
        private CircleImageView imageView;

        GetProfileFromURL(CircleImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            String urldisplay = url[0];
            Bitmap bitmap = null;
            try {
                InputStream srt = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(srt);
            } catch (Exception e){
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }
}
class ImageBtnListerer implements View.OnClickListener {
    private Activity activity;
    private String fragment;
    private User user;
    ImageBtnListerer(Activity activity,String fragment,User user) {
        this.activity = activity;
        this.fragment = fragment;
        this.user = user;
    }

    @Override
    public void onClick(View v) {
        activity.getIntent().putExtra("userdata",user);
        ClientActivity act = (ClientActivity)activity;
        act.openNewFragments(fragment);
    }
}