package com.example.eventscheduling.eventorg.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventscheduling.R;
import com.example.eventscheduling.eventorg.model.RecyclerView_Adapter_Packages;
import com.example.eventscheduling.eventorg.util.PackagesValues;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class evntOrg_Packages extends Fragment {
    private static final String TAG = "evntOrg_Packages";
    private RecyclerView recyclerView;
    private RecyclerView_Adapter_Packages packages_adapter;
    private FloatingActionButton floatingActionButton;
    private FirebaseUser currentUser;
    private String currentUserID;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private CollectionReference dbReference;


    // Oncreate View constructor of packages
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evnt_org__packages, container, false);
        floatingActionButton = view.findViewById(R.id.floating_AtnBar_evnt_package);
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserID = currentUser.getUid();
        }
        dbReference = FirebaseFirestore.getInstance().collection("Users").document(currentUserID).collection("Packages");
        initRecyclerView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Floating Action button which can be used to open activity of create packages...
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), evntOrg_Package_create.class));
            }
        });

    }

    // to initialize the adapter and recyclerview of Packages of event organizer
    public void initRecyclerView(View view) {
        Log.d(TAG, "initRecyclerView: is called");
        recyclerView = view.findViewById(R.id.package_recycler);
        Query query = dbReference.orderBy("PackageName", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<PackagesValues> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<PackagesValues>().setQuery(query, PackagesValues.class).build();
        packages_adapter = new RecyclerView_Adapter_Packages(getContext(), firestoreRecyclerOptions);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(packages_adapter);
    }

    // Start listening values from server when this activity is started
    @Override
    public void onStart() {
        super.onStart();
        packages_adapter.startListening();
    }

    // stop listening values from server when this activity is stopped
    @Override
    public void onStop() {
        super.onStop();
        if (packages_adapter != null) {
            packages_adapter.stopListening();
        }

    }
}
