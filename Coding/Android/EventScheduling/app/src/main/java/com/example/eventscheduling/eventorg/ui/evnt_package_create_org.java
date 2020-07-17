package com.example.eventscheduling.eventorg.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.eventscheduling.R;
import com.example.eventscheduling.util.SpaceTokenizer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class evnt_package_create_org extends Fragment implements View.OnClickListener {
    private MultiAutoCompleteTextView service_multiValue;
    private MultiAutoCompleteTextView food_multiValue;
    private EditText package_editText;
    private AutoCompleteTextView venue_textView;
    private TextInputEditText priceEditText;
    private Button create_btn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;
    private String currentuserID;
    private static final String TAG = "evnt_package_create_org";
    public evnt_package_create_org() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evnt_package_create_org, container, false);
        // Initialize the views in UI
        service_multiValue = view.findViewById(R.id.evnt_package_service_dropdown);
        food_multiValue = view.findViewById(R.id.evnt_package_food_dropdown_create_org);
        package_editText = view.findViewById(R.id.package_edtXt_evnt_create_org);
        venue_textView = view.findViewById(R.id.venue_edtXt_package_create_org);
        priceEditText = view.findViewById(R.id.price_edtXt_evnt_create_org);
        create_btn = view.findViewById(R.id.btn_create_package_org);
        create_btn.setOnClickListener(this);
        // Set Adapter for Service AutoCompleteTextView
        ArrayAdapter<String> serviceAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.service_list));
        service_multiValue.setAdapter(serviceAdapter);
        service_multiValue.setTokenizer(new SpaceTokenizer());
        service_multiValue.setThreshold(1);
        // Set Adapter for Food AutoCompleteTextView
        ArrayAdapter<String> foodAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.food_list));
        food_multiValue.setAdapter(foodAdapter);
        food_multiValue.setTokenizer(new SpaceTokenizer());
        food_multiValue.setThreshold(1);

        //set adapter for venue
        ArrayAdapter<String> venueAdapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.Venue_list));
        venue_textView.setAdapter(venueAdapter);
        // Firebase Authentication initialization
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentuserID = currentUser.getUid();
        }
        // Firestore initialization
        firestore = FirebaseFirestore.getInstance();

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_create_package_org) {
            create_btn_fn();
        }

    }

    // Create button functionality

    private void create_btn_fn() {

        if (isEmpty(priceEditText) && isEmpty(package_editText) && isEmpty(service_multiValue) && isEmpty(food_multiValue) && isEmpty(venue_textView)) {
            Toast.makeText(getContext(), "Please Fill all fields", Toast.LENGTH_SHORT).show();

        } else {

            String price = priceEditText.getText().toString();
            String packageName = package_editText.getText().toString();
            String services = service_multiValue.getText().toString();
            String food = food_multiValue.getText().toString();
            String venue = venue_textView.getText().toString();
            String[] foodArray = food.split(" ");
            List<String> foodList = Arrays.asList(foodArray);
            String[] serviceArray = services.split(" ");
            List<String> serviceList = Arrays.asList(serviceArray);
            Map packageData = new HashMap();
            packageData.put("PackageName", packageName);
            packageData.put("Services", serviceList);
            packageData.put("Food", foodList);
            packageData.put("Venue", venue);
            packageData.put("Price", price);
            firestore.collection("Users").document(currentuserID).collection("Packages").document().set(packageData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            FragmentManager fragmentManager = getParentFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.evntOrg_packages_frameLayout, new evntOrg_Packages())
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                            Log.d(TAG, "onSuccess: in evnt package create organization ");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Failed to create Package... Please Try again", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    // check weather EditText is empty or not
    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() <= 0;
    }
}