package com.example.eventscheduling.client.ui;

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

import com.example.eventscheduling.MainActivity;
import com.example.eventscheduling.R;
import com.google.android.material.navigation.NavigationView;

public class client_home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);
        Toolbar toolbar = findViewById(R.id.toolbar_client_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        NavigationView navigationView = findViewById(R.id.nav_drawer_client);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = findViewById(R.id.drawerLayout_client);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().addToBackStack(null)
                    .replace(R.id.frameLayout_clientHome, new client_home_frag())
                    .commit();
            selectTitle("Home");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) { return true; }
        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home_client:
                getSupportFragmentManager().beginTransaction().addToBackStack(null)
                        .replace(R.id.frameLayout_clientHome, new client_home_frag())
                        .commit();
                selectTitle("Home");
                break;
            case R.id.nav_profile_client:
                getSupportFragmentManager().beginTransaction().addToBackStack(null)
                        .replace(R.id.frameLayout_clientHome, new client_profile())
                        .commit();
                selectTitle("Profile");
                break;
            case R.id.nav_search_client:
                getSupportFragmentManager().beginTransaction().addToBackStack(null)
                        .replace(R.id.frameLayout_clientHome, new client_search())
                        .commit();
                selectTitle("Search");
                break;
            case R.id.nav_messages_client:
                getSupportFragmentManager().beginTransaction().addToBackStack(null)
                        .replace(R.id.frameLayout_clientHome, new client_messages())
                        .commit();
                selectTitle("Messages");
                break;
            case R.id.nav_history_client:
                getSupportFragmentManager().beginTransaction().addToBackStack(null)
                        .replace(R.id.frameLayout_clientHome, new client_history())
                        .commit();
                selectTitle("History");
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_logout_client:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

            default:
                getSupportFragmentManager().beginTransaction().addToBackStack(null)
                        .replace(R.id.frameLayout_clientHome, new client_home_frag())
                        .commit();
                selectTitle("Home");
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void selectTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

}
