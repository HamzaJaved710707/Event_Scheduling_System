package com.example.eventscheduling.eventorg.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventscheduling.R;
import com.example.eventscheduling.eventorg.mapService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class evntOrg_signUp2 extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "evntOrg_signUp2";
    AlertDialog.Builder alertDialog;
    ProgressBar progressBar;
    // Variable to hold the map location of user
    Double latitude;
    Double longitude;
    String event_table_id1;
    // Firebase Database Reference
    FirebaseFirestore databaseReference;
    com.example.eventscheduling.eventorg.mapService mapService;
    private EditText businessName;
    private EditText province;
    private EditText city;
    private EditText area;
    private Spinner cat_spinner;
    private SharedPreferences sharedPreferences;
    private String email;
    private String password;
    private String name;
    private FirebaseUser user;
    private String userID;
    // Firebase Authentication
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private AutoCompleteTextView busCat_AutoComp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evnt_org_sign_up2);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mapService = new mapService(this);
        userID = getIntent().getStringExtra("ID");
        latitude = mapService.getLatitude();
        longitude = mapService.getLongitude();
        //Initialize views
          businessName = findViewById(R.id.business_edtXt_signUp2);
        province = findViewById(R.id.province_signUp2_edtXt);
        city = findViewById(R.id.city_signUp2_edtXt);
        // ALertDialog
        alertDialog = new AlertDialog.Builder(evntOrg_signUp2.this);
        // ProgressBar
        progressBar = findViewById(R.id.progBar_Evnt_SignUp2);
        // Set click listener
        Button nextButton = findViewById(R.id.btn_evntOrg_signUp2);
        nextButton.setOnClickListener(this);
        // Initialize business Category AutoComplete TextView
        busCat_AutoComp = findViewById(R.id.evnt_signUp2_busCat_txtView);
        ArrayAdapter<String> busCat_ArrayAdapter = new ArrayAdapter<String>(evntOrg_signUp2.this,
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.Business_Category));

        busCat_AutoComp.setAdapter(busCat_ArrayAdapter);




    }

    // Store current ACtivity data before calling another activity


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_evntOrg_signUp2) {
            processing();
        }
    }


    // Do processing
    // Send data to remote server and get response whether operation was successful or not
    private void processing() {
        Log.d(TAG, "processing: function is called");
        progressBar.setVisibility(View.VISIBLE);

        // Get the values from editTexts and spinner which user has provided
        final String _businessName = businessName.getText().toString();
        final String _province = province.getText().toString();
        final String _city = city.getText().toString();
        final String category = busCat_AutoComp.getText().toString();
        // If user has not provided any information
        if (_businessName.matches("") && _province.matches("") && _city.matches("") ) {

            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Please fill the form");
            alertDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }
            );

        } else {

            Map parameters = new HashMap<>();
            parameters.put("businessName", _businessName);
            parameters.put("province", _province);
            parameters.put("city", _city);
            parameters.put("category", category);
            parameters.put("latitude", latitude);
            parameters.put("longitude", longitude);
            parameters.put("isActive", true);
            firebaseFirestore.collection("Users").document(userID).collection("businessData").document("1").set(parameters)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent intent = new Intent(evntOrg_signUp2.this, evntOrg_home.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK & Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(evntOrg_signUp2.this, "Error... Try again", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + e.toString());
                }
            });

        }
    }

}
