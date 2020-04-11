package com.example.eventscheduling;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.eventscheduling.client.ui.client_signUp;
import com.example.eventscheduling.eventorg.ui.evntOrg_signUp2;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

public class MapLocation extends FragmentActivity implements OnMapReadyCallback{
    private static final String TAG = "MapLocation";
    // variable to store values
    public double latitude;
    public double longitude;
    private GoogleMap mMap;
    private boolean mServicePermission = false;
    // Permissions which are needed for map
    private String locationPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    private String coarsePermission = Manifest.permission.ACCESS_COARSE_LOCATION;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_location);
        // Get Intent to disable button

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //Button listener
        ((SupportMapFragment) Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.map_id))).getMapAsync(this);
        Button button = findViewById(R.id.select_map_button);

        Intent intent = getIntent();
        final String value = intent.getStringExtra("Value");
        if(!value.matches("[12]")){
            button.setVisibility(View.INVISIBLE);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(value.matches("1")){
                    Intent intent = new Intent(MapLocation.this, evntOrg_signUp2.class);
                    String mLatitude = String.valueOf(latitude);
                    String mLongitude = String.valueOf(longitude);
                    intent.putExtra("latitude", mLatitude);
                    intent.putExtra("longitude", mLongitude);
                    intent.putExtra("value","1");
                    startActivity(intent);
                }
                else if(value.matches("2")){
                    Intent intent = new Intent(MapLocation.this, client_signUp.class);
                    Log.d(TAG, "onClick: in maplocation " + latitude + longitude);
                    String mLatitude = String.valueOf(latitude);
                    String mLongitude = String.valueOf(longitude);
                    intent.putExtra("latitude", mLatitude);
                    intent.putExtra("longitude", mLongitude);
                    intent.putExtra("value","1");
                    startActivity(intent);
                }

            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        getPermission();
        mMap = googleMap;
        getLocation();
        mMap.setMyLocationEnabled(true);


    }

    // Function to get required permissions for google map
    public void getPermission() {
        mServicePermission = false;
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this, locationPermission)
                == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, coarsePermission)
                    == PackageManager.PERMISSION_GRANTED) {
                mServicePermission = true;
            }
        } else {
            // If permissions are not already given then ask from user
            ActivityCompat.requestPermissions(this, permissions, 1);
        }

    }

    // Function to get current location of user
    public void getLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mServicePermission) {
                final Task location = fusedLocationProviderClient.getLastLocation();

                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location location1 = (Location) task.getResult();
                            longitude = Objects.requireNonNull(location1).getLongitude();
                            latitude = Objects.requireNonNull(location1).getLatitude();
                            Log.d(TAG, "onComplete: in Maplocation " + latitude + longitude);
                            moveCamera(new LatLng(location1.getLatitude(), location1.getLongitude()), 15f);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.d(TAG, "getLocation: " + e.getMessage());
        }
    }

    // Move camera or screen to desire location in map
    public void moveCamera(LatLng latLng, float zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }



}
