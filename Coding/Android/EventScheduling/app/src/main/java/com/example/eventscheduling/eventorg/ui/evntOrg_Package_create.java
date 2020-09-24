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


    }

}