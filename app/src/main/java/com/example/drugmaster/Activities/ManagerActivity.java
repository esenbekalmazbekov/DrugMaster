package com.example.drugmaster.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.drugmaster.Model.User;
import com.example.drugmaster.Model.drugmodel.Drug;
import com.example.drugmaster.Model.drugmodel.Druglist;
import com.example.drugmaster.R;
import com.example.drugmaster.fragments.ClientsFragment;
import com.example.drugmaster.fragments.InfoFragment;
import com.example.drugmaster.fragments.ListFragment;
import com.example.drugmaster.fragments.OrdersFragment;
import com.example.drugmaster.popups.PopupAdddrugs;
import com.example.drugmaster.popups.PopupInfo;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ManagerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    private DrawerLayout drawer;
    private User user;
    public static final int ADDITION = 1;
    public static final int CHANGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        user = getIntent().getParcelableExtra("userdata");

        setHeader();

        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,new InfoFragment())
                    .commit();

            navigationView.setCheckedItem(R.id.nav_info);
        }
    }

    private void setHeader() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navOrgname = headerView.findViewById(R.id.orgName);
        TextView navEmail = headerView.findViewById(R.id.mail);
        CircleImageView imageView = headerView.findViewById(R.id.profile_image);

        navOrgname.setText(user.getOrgname());
        navEmail.setText(user.getEmail());
        Glide.with(this).load(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhotoUrl()).into(imageView);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(!ClientActivity.own){
                ClientActivity.own = true;
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,new OrdersFragment())
                        .commit();
            }else
                super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == InfoFragment.INFO_FRAGMENT){
            if(resultCode == RESULT_OK){
                user = Objects.requireNonNull(data).getParcelableExtra("userdata");
                setHeader();
                finish();
            }
        }
    }

    public void createPopUpInfoChange(int whichone, Drug drug) {
        switch (whichone){
            case InfoFragment.INFO_FRAGMENT:{
                Intent intent = new Intent(ManagerActivity.this, PopupInfo.class);
                intent.putExtra("userdata",user);
                startActivityForResult(intent,InfoFragment.INFO_FRAGMENT);
            }break;
            case ListFragment.LIST_FRAGMENT:{
                Intent intent = new Intent(ManagerActivity.this, PopupAdddrugs.class);
                intent.putExtra("type",ADDITION);
                startActivity(intent);
            }break;
            case Druglist.DRUGLIST_FRAGMENT_CHANGE:{
                Intent intent = new Intent(ManagerActivity.this, PopupAdddrugs.class);
                intent.putExtra("type",CHANGE);
                intent.putExtra("drug",drug);
                startActivity(intent);
            }
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_info:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,new InfoFragment())
                        .commit();
                break;
            case R.id.nav_list:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,new ListFragment())
                        .commit();
                break;
            case R.id.nav_orders:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,new OrdersFragment())
                        .commit();
                break;
            case R.id.nav_clients:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,new ClientsFragment())
                        .commit();
                break;
            case R.id.nav_complaint:
                Toast.makeText(this,"complaint",Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_singout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ManagerActivity.this,MainActivity.class));
                ManagerActivity.this.finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public User getUser(){return user;}
}
