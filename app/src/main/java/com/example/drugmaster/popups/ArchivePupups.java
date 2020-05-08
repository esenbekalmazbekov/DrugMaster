package com.example.drugmaster.popups;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.drugmaster.Model.achivemodel.ShortDrug;
import com.example.drugmaster.R;

import java.util.ArrayList;

public class ArchivePupups extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive_pupups);
        creation();
        showData();
    }

    private void showData() {
        ArrayList<ShortDrug> drugs = getIntent().getParcelableArrayListExtra("drugs");
        ListView shortCut = findViewById(R.id.shortcut);
        shortCut.setAdapter(new ShortCut(this,drugs));
    }

    private void creation() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.5));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);
    }

}

class ShortCut extends ArrayAdapter<ShortDrug>{
    private ArchivePupups pupups;
    private ArrayList<ShortDrug> drugs;

    ShortCut(ArchivePupups activity, ArrayList<ShortDrug> drugs){
        super(activity, R.layout.drug_shortcut,drugs);
        pupups = activity;
        this.drugs = drugs;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = pupups.getLayoutInflater();

        @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(R.layout.drug_shortcut,null,true);
        init(listViewItem,position);

        return listViewItem;
    }

    @SuppressLint("SetTextI18n")
    private void init(View listViewItem, int position) {
        TextView name = listViewItem.findViewById(R.id.drugname);
        TextView count = listViewItem.findViewById(R.id.count);

        name.setText(drugs.get(position).getDrugName());
        count.setText("" + drugs.get(position).getCount());
    }


}