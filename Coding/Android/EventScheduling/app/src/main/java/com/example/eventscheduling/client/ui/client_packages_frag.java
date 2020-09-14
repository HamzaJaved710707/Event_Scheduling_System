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
import com.example.eventscheduling.client.model.Filter_Package_Dialog_client;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class client_packages_frag extends Fragment implements Filter_Package_Dialog_client.ExampleDialogListener{
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
            filter_img = view.findViewById(R.id.filter_packages_client_img);
        filter_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        // REturning the view
        return view;
    }
    public void openDialog() {
        Filter_Package_Dialog_client filter_dialog = new Filter_Package_Dialog_client(client_packages_frag.this);
        filter_dialog.show(getParentFragmentManager(), "example dialog");
        filter_dialog.setExampleDialog(client_packages_frag.this);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    // Initialize the recyclerView of this fragment
    private void initRecyclerVeiw() {
        Log.d(TAG, "initRecyclerVeiw: is started");


    }


    @Override
    public void applyTexts(String username, String password) {
        Log.d(TAG, "applyTexts: in fragment"+ username + password);
    }
}
