package com.example.drugmaster.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.drugmaster.R;
import com.example.drugmaster.fragments.BasketFragment;
import com.example.drugmaster.fragments.InfoFragment;
import com.example.drugmaster.fragments.ManagerFragment;
import com.example.drugmaster.fragments.SearchFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ClientActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Toolbar toolbar;
    private DrawerLayout drawer;

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

        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_client,new InfoFragment())
                    .commit();

            navigationView.setCheckedItem(R.id.nav_info);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
