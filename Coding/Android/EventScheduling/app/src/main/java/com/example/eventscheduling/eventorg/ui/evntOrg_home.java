package com.example.eventscheduling.eventorg.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
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

import com.example.eventscheduling.MainActivity;
import com.example.eventscheduling.R;
import com.example.eventscheduling.client.ui.client_home;
import com.example.eventscheduling.util.BaseActivity;
import com.example.eventscheduling.util.CallActivity;
import com.example.eventscheduling.util.SinchService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.stepstone.apprating.listener.RatingDialogListener;


public class evntOrg_home extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, RatingDialogListener, SinchService.StartFailedListener {

    private static final String TAG = "evntOrg_home";
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    private String currentUserID;
    protected OnBackPressedListener onBackPressedListener;
    private static getRating_evntOrg getRatingInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evnt_org_home);
        // toolbar setting
        Toolbar toolbar = findViewById(R.id.nav_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setOnBackPressedListener(onBackPressedListener);
        // Navigation drawer setting
        NavigationView navigationView = findViewById(R.id.nav_view_id);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        // Firebase Authentication
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUserID = currentUser.getUid();

        }
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_test_id, new evntOrg_profile(), "Profile")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null)
                    .commit();
            setTitleActionBar("Profile");


        }

    }

    @Override
    protected void onStart() {
        super.onStart();
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
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        else{
            // This switch statement will work with Overflow menu...
            switch (item.getItemId()){
                case R.id.logOut:
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK & Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    break;

                case R.id.setting_evntOrg:
                    startActivity(new Intent(this, evntOrg_setting.class));
                    break;
                default:
                    break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_profile:
                sinchStarter();
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_test_id, new evntOrg_profile(), "Profile")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null)
                        .commit();
                setTitleActionBar("Profile");

                break;
            case R.id.nav_packages:
                sinchStarter();
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_test_id, new evntOrg_Packages())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null)
                        .commit();
                setTitleActionBar("Packages");
                break;
            case R.id.nav_orders:
                sinchStarter();
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_test_id, new evntOrg_Orders())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null)
                        .commit();
                setTitleActionBar("Orders");
                break;
            case R.id.nav_messages:
                sinchStarter();
                setTitleActionBar("Messages");
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_test_id, new evntOrg_Messages())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null)
                        .commit();

                break;
            case R.id.nav_analysis:
                sinchStarter();
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_test_id, new evntOrg_Analysis())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null)
                        .commit();
                setTitleActionBar("Analysis");
                break;
            case R.id.nav_portfolio:
                sinchStarter();
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_test_id, new evntOrg_Portfolio())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null)
                        .commit();
                setTitleActionBar("Portfolio");
                break;
            case R.id.nav_calender:
                sinchStarter();
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_test_id, new evntOrg_Calender())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null)
                        .commit();
                setTitleActionBar("Calender");
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout_eventOrg:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                sinchStarter();
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_test_id, new evntOrg_profile(),"Profile")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null)
                        .commit();
                setTitleActionBar("Profile");
                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void sinchStarter() {

        if (!currentUserID.equals(getSinchServiceInterface().getUserName())) {
            getSinchServiceInterface().stopClient();
        }

        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(currentUserID);

        } else {
            Log.d(TAG, "onNavigationItemSelected: ");
        }
    }

    // Function to change the title of ActionBar
    public void setTitleActionBar(String title){
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
        getRatingInterface.getRatingMethod_evntOrg(i, s);
    }

    @Override
    public void onStartFailed(SinchError error) {

    }

    @Override
    public void onStarted() {

    }


    public interface OnBackPressedListener {
        void doBack();
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

  /*  @Override
    public void onBackPressed() {
        if (onBackPressedListener != null)
            onBackPressedListener.doBack();
        else
            super.onBackPressed();
    }*/

    @Override
    protected void onDestroy() {
        onBackPressedListener = null;
        super.onDestroy();
    }
/// when Back button is clicked from activity
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();


        }
        return super.onKeyDown(keyCode, event);
    }
/// Logic to handle Back button trigger...
    protected void exitByBackKey() {
        evntOrg_profile myFragment = (evntOrg_profile)getSupportFragmentManager().findFragmentByTag("Profile");
        if (myFragment != null && myFragment.isVisible()) {
            // add your code here
            AlertDialog alertbox = new AlertDialog.Builder(this)
                    .setMessage("Do you want to exit application?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        // do something when the button is clicked
                        public void onClick(DialogInterface arg0, int arg1) {

                            finish();
                            //close();


                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {

                        // do something when the button is clicked
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    })
                    .show();

        }

    }
    public interface getRating_evntOrg{
        void getRatingMethod_evntOrg(int rate, String comment);
    }
    public static void ratingInterface_evntOrg(getRating_evntOrg rating){
        getRatingInterface = rating;
    }


}

