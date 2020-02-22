package com.example.eventscheduling.eventorg.ui;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;

import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.eventscheduling.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;


public class evntOrg_home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evnt_org_home);
        Toolbar toolbar = findViewById(R.id.nav_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        NavigationView navigationView = findViewById(R.id.nav_view_id);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_test_id, new evntOrg_profile())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
            selectTitle("Profile");
        }
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
           drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_test_id, new evntOrg_profile())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                selectTitle("Profile");
                break;
            case R.id.nav_packages:
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_test_id, new evntOrg_Packages())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                selectTitle("Packages");
                break;
            case R.id.nav_orders:
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_test_id, new evntOrg_Orders())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                selectTitle("Orders");
                break;
            case R.id.nav_messages:
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_test_id, new evntOrg_Messages())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                selectTitle("Messages");
                break;
            case R.id.nav_analysis:
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_test_id, new evntOrg_Analysis())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                selectTitle("Analysis");
                break;
            case R.id.nav_portfolio:
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_test_id, new evntOrg_Portfolio())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                selectTitle("Portfolio");
                break;
            case R.id.nav_calender:
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_test_id, new evntOrg_Calender())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                selectTitle("Calender");
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_send:
                Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show();
                break;
            default:
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_test_id, new evntOrg_profile())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                selectTitle("Profile");
                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    public void selectTitle(String title){
        getSupportActionBar().setTitle(title);
    }
}
