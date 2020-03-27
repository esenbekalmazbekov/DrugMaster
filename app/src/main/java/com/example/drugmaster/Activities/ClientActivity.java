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
import com.example.drugmaster.R;
import com.example.drugmaster.fragments.BasketFragment;
import com.example.drugmaster.fragments.InfoFragment;
import com.example.drugmaster.fragments.ManagerFragment;
import com.example.drugmaster.fragments.SearchFragment;
import com.example.drugmaster.popups.PopupInfo;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClientActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Toolbar toolbar;
    private DrawerLayout drawer;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);


        toolbar = findViewById(R.id.toolbar_client);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawer = findViewById(R.id.clientdr_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        user = getIntent().getParcelableExtra("userdata");
        setHeader();
        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_client,new InfoFragment())
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
            if(!InfoFragment.own){
                InfoFragment.own = true;
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container_client,new ManagerFragment())
                        .commit();
            }else
                super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                user = Objects.requireNonNull(data).getParcelableExtra("userdata");
                setHeader();
                finish();
            }
        }
    }

    public void createPopUpInfoChange() {
        Intent intent = new Intent(ClientActivity.this, PopupInfo.class);
        intent.putExtra("userdata",user);
        startActivityForResult(intent,1);
    }

    public void openNewFragments(String fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_client,new InfoFragment())
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_info:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container_client,new InfoFragment())
                        .commit();
                break;
            case R.id.nav_manager:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container_client,new ManagerFragment())
                        .commit();
                break;
            case R.id.nav_search:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container_client,new SearchFragment())
                        .commit();
                break;
            case R.id.nav_basket:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container_client,new BasketFragment())
                        .commit();
                break;
            case R.id.nav_complaint:
                Toast.makeText(this,"complaint",Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_singout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ClientActivity.this,MainActivity.class));
                ClientActivity.this.finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}