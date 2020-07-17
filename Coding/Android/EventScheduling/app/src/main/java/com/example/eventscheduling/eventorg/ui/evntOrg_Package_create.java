package com.example.eventscheduling.eventorg.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.eventscheduling.R;

public class evntOrg_Package_create extends FragmentActivity {
    private AutoCompleteTextView autoCompleteTextView;
    private int category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evnt_org__package_create);
        // Add adapter to drop down menu of textEditLayout
    /*    autoCompleteTextView = findViewById(R.id.evnt_package_service_dropdown);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                evntOrg_Package_create.this,
                R.layout.evntorg_package_service_list_item,
                getResources().getStringArray(R.array.service_list)
        );

        autoCompleteTextView.setAdapter(arrayAdapter);*/
        // Show to respective fragment according to event organizer category
        SharedPreferences sharedPreferences = getSharedPreferences("Category", Context.MODE_PRIVATE);
        category = sharedPreferences.getInt("category", 0);
        switch (category) {
            case 0:
                Toast.makeText(this, "Please Restart your application to work properly", Toast.LENGTH_LONG).show();
                break;
            case 1:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_create_package_evnt, new evnt_package_create_org())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                break;
            case 2:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_create_package_evnt, new evnt_package_create_catering())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                break;
            case 3:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_create_package_evnt, new evnt_package_create_venue())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                break;
            case 4:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_create_package_evnt, new evnt_package_create_decoration())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                break;
            case 5:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_create_package_evnt, new evnt_package_create_car())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                break;
            case 6:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_create_package_evnt, new evnt_package_create_card()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
            default:
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
                break;

        }
    }

}