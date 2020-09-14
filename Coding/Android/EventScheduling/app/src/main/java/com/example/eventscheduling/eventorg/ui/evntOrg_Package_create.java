package com.example.eventscheduling.eventorg.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.eventscheduling.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class evntOrg_Package_create extends FragmentActivity {
    private AutoCompleteTextView autoCompleteTextView;
    private int category;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;
    private String currentUserID;
    private DocumentReference documentReference;
    private ProgressBar progressBar;


    @Override
    protected void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);
        if (currentUser != null) {
            currentUserID = currentUser.getUid();
            firestore = FirebaseFirestore.getInstance();
            documentReference = firestore.collection("Users").document(currentUserID);
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String value = documentSnapshot.getString("businessCat");
                    if (value != null) {
                        getCat_Value(value);
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        mAuth.signOut();
                        startActivity(new Intent(evntOrg_Package_create.this, evntOrg_signIn.class));
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(evntOrg_Package_create.this, "Please log in again", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                    startActivity(new Intent(evntOrg_Package_create.this, evntOrg_signIn.class));

                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evnt_org__package_create);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        progressBar = findViewById(R.id.progressbar_evnt_create_package);


    }

    // Add adapter to drop down menu of textEditLayout
    /*    autoCompleteTextView = findViewById(R.id.evnt_package_service_dropdown);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                evntOrg_Package_create.this,
                R.layout.evntorg_package_service_list_item,
                getResources().getStringArray(R.array.service_list)
        );

        autoCompleteTextView.setAdapter(arrayAdapter);*/


    void getCat_Value(String value) {
        progressBar.setVisibility(View.INVISIBLE);
        switch (value) {

            case "Event_Organizer":
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_create_package_evnt, new evnt_package_create_org())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                break;
            case "Caterer":
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_create_package_evnt, new evnt_package_create_catering())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                break;
            case "Venue_Provider":
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_create_package_evnt, new evnt_package_create_venue())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                break;
            case "Decoration":
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_create_package_evnt, new evnt_package_create_decoration())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                break;
            case "Car_Rent":
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_create_package_evnt, new evnt_package_create_car())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                break;
            case "Invitation_Card":
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_create_package_evnt, new evnt_package_create_card()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
            default:
                break;


        }

    }

}