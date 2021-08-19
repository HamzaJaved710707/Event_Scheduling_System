package com.example.eventscheduling.client.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.eventscheduling.MainActivity;
import com.example.eventscheduling.R;
import com.example.eventscheduling.util.BaseActivity;
import com.example.eventscheduling.util.SinchService;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.sinch.android.rtc.SinchError;
import com.stepstone.apprating.listener.RatingDialogListener;

public class client_home extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, RatingDialogListener, SinchService.StartFailedListener

 {
     private static final String TAG = "client_home";
     private static getRating getRatingInterface;
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
        client_profile client_profile = new client_profile();
        NavigationView navigationView = findViewById(R.id.nav_drawer_client);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = findViewById(R.id.drawerLayout_client);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().addToBackStack(null)
                    .replace(R.id.frameLayout_clientHome, new client_profile())
                    .commit();
            selectTitleOfActionBar("Profile");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.evnt_org_home, menu);
        // This change the text color of overflow menu
        for(int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(Color.BLACK), 0,     spanString.length(), 0);
            spanString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, spanString.length(), 0);
            item.setTitle(spanString);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else {
            // This switch statement will work with Overflow menu...
            switch (item.getItemId()) {
                case R.id.logOut:
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK & Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.setting_evntOrg:
                    startActivity(new Intent(client_home.this, client_setting.class));
                    break;
                default:
                    break;
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
        @Override
        public boolean onNavigationItemSelected (@NonNull MenuItem menuItem){
            switch (menuItem.getItemId()) {
                case R.id.nav_profile_client:
                    getSupportFragmentManager().beginTransaction().addToBackStack(null)
                            .replace(R.id.frameLayout_clientHome, new client_profile())
                            .commit();
                    selectTitleOfActionBar("Profile");
                    break;
                case R.id.nav_packages_client:
                    getSupportFragmentManager().beginTransaction().addToBackStack(null)
                            .replace(R.id.frameLayout_clientHome, new client_packages_frag())
                            .commit();
                    selectTitleOfActionBar("Packages");
                    break;
                case R.id.nav_orders_client:
                    getSupportFragmentManager().beginTransaction().addToBackStack(null)
                            .replace(R.id.frameLayout_clientHome, new client_orders())
                            .commit();
                    break;
                case R.id.nav_search_client:
                    getSupportFragmentManager().beginTransaction().addToBackStack(null)
                            .replace(R.id.frameLayout_clientHome, new client_search())
                            .commit();
                    selectTitleOfActionBar("Search");
                    break;
                case R.id.nav_messages_client:

                    getSupportFragmentManager().beginTransaction().addToBackStack(null)
                            .replace(R.id.frameLayout_clientHome, new client_messages())
                            .commit();
                    selectTitleOfActionBar("Messages");
                    break;
                case R.id.nav_history_client:
                    getSupportFragmentManager().beginTransaction().addToBackStack(null)
                            .replace(R.id.frameLayout_clientHome, new client_history())
                            .commit();
                    selectTitleOfActionBar("History");
                    break;
                case R.id.nav_share:
                    Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.nav_logout_client:
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    break;
                default:
                    getSupportFragmentManager().beginTransaction().addToBackStack(null)
                            .replace(R.id.frameLayout_clientHome, new client_profile())
                            .commit();
                    selectTitleOfActionBar("Profile");
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }


    public void selectTitleOfActionBar(String title) {
        getSupportActionBar().setTitle(title);
    }


     @Override
     public void onNegativeButtonClicked() {
         Log.d(TAG, "onNegativeButtonClicked: ");
     }

     @Override
     public void onNeutralButtonClicked() {
         Log.d(TAG, "onNeutralButtonClicked: ");
     }

     @Override
     public void onPositiveButtonClicked(int i, String s) {
         Log.d(TAG, "onPositiveButtonClicked: ");
         getRatingInterface.getRatingMethod(i, s);
     }

     @Override
     public void onStartFailed(SinchError error) {

     }

     @Override
     public void onStarted() {

     }

     public interface getRating{
        void getRatingMethod(int rate, String comment);
 }
 public static void ratingInterface(getRating rating){
        getRatingInterface = rating;
 }
 }
