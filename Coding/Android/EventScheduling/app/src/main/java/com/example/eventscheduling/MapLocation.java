package com.example.eventscheduling;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapLocation extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "MapLocation";
    private GoogleMap mMap;
    private boolean mServicePermission = false;

    // Permissions which are needed for map
    private String locationPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    private String coarsePermission = Manifest.permission.ACCESS_COARSE_LOCATION;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mServicePermission) {
                final Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location location1 = (Location) task.getResult();
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
