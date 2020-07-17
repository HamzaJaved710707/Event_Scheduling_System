package com.example.eventscheduling.client.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eventscheduling.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class client_packages_frag extends Fragment {
    private static final String TAG = "client_packages_frag";
    // variable
    private AutoCompleteTextView typeEditText;
    private AutoCompleteTextView locationEditText;
    private ImageView filter_img;
    private RelativeLayout filter_rel_layout;
    private TextView filter_txt;
    // Variables of Firestore to fetch data from there
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String currentUserID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_packages_frag, container, false);
        // Initialize the variables of firestore
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserID = currentUser.getUid();
        }

        // Setting adapter for both autoComplete TextEdits
        typeEditText = view.findViewById(R.id.ediText_type_package_client);
        locationEditText = view.findViewById(R.id.location_editExt_package);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.Business_Category));
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.LocationType));
        typeEditText.setAdapter(typeAdapter);
        locationEditText.setAdapter(locationAdapter);
        filter_img = view.findViewById(R.id.filter_packages_client_img);
        filter_rel_layout = view.findViewById(R.id.relLayout_pckage_client);
        filter_txt = view.findViewById(R.id.filter_txt_client_package_frg);
        // Click listener to toggle the visibility of layout of filter image or text
        filter_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filter_rel_layout.getVisibility() == View.VISIBLE) {
                    filter_rel_layout.setVisibility(View.INVISIBLE);
                } else {
                    filter_rel_layout.setVisibility(View.VISIBLE);
                }
            }
        });
        filter_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filter_rel_layout.getVisibility() == View.VISIBLE) {
                    filter_rel_layout.setVisibility(View.INVISIBLE);
                } else {
                    filter_rel_layout.setVisibility(View.VISIBLE);
                }
            }
        });
        // REturning the view
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    // Initialize the recyclerView of this fragment
    private void initRecyclerVeiw() {
        Log.d(TAG, "initRecyclerVeiw: is started");


    }


}
