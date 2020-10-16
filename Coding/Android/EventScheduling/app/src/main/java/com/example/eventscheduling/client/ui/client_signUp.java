package com.example.eventscheduling.client.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventscheduling.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Objects;


public class client_signUp extends AppCompatActivity {
    private static final String TAG = "client_signUp";
    ProgressBar progressBar;
    AlertDialog.Builder alertDialog;
    String mLatitude;
    String mLongitude;
    private EditText _name;
    private EditText _email;
    private EditText _mobileNumber;
    private EditText _password;
    private EditText _confirm_Pass;
    private String emailTxt;
    private String passTxt;
    private String name;
    private String mobileNumber;
    private String confirmPass;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestoreDatabase;
    private CollectionReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize variables
        setContentView(R.layout.activity_client_signup);
        Button regBtn = findViewById(R.id.client_signUp_btn);
        _name = findViewById(R.id.name_text_client_signUp);
        _email = findViewById(R.id.email_text_client_signUp);
        _mobileNumber = findViewById(R.id.client_mobileNo_signUp_text);
        _password = findViewById(R.id.pass_text_client_signUp);
        _confirm_Pass = findViewById(R.id.confirmPass_text_client_signUp);
        // Initialize Progress Bar
        progressBar = findViewById(R.id.progBar_Client_signUp);
        // Initialize Alert dialog
        alertDialog = new AlertDialog.Builder(this);
        // Registration Button click listener
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processing();
            }
        });

        // Firebase Authentication initialization
        mAuth = FirebaseAuth.getInstance();
    }

    private void processing() {

        name = _name.getText().toString().trim();
        emailTxt = _email.getText().toString().trim();
        mobileNumber = _mobileNumber.getText().toString().trim();
        passTxt = _password.getText().toString().trim();
        confirmPass = _confirm_Pass.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);
        if (name.trim().matches("") && emailTxt.trim().matches("") && mobileNumber.trim().matches("") && passTxt.trim().matches("") && confirmPass.trim().matches("")) {
            if (!passTxt.matches(confirmPass)) {
                // if password does not match with other password
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Unable to confirm your password");
            } else {
                // If values are not set by the user
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Please enter your data");
            }

        } else {
            // If user has entered some data then...
            // and also above conditions are satisfied
            registerWithFb();

        }


    }

    // Create account or register on Firestore
    // then if user successfully registered then add users data in firestore by calling function addValueToFirebase()
    private void registerWithFb() {
        mAuth.createUserWithEmailAndPassword(emailTxt, passTxt).addOnCompleteListener(client_signUp.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                addValueToFirebase(Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()));
            }
        });


    }

    // Adding data of user in firestore
    private void addValueToFirebase(FirebaseUser user) {
        String currentUser_id = user.getUid();
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String tokenId = instanceIdResult.getToken();
                firestoreDatabase = FirebaseFirestore.getInstance();
                HashMap userData = new HashMap<>();
                userData.put("Name", name);
                userData.put("type", 1);
                userData.put("email", emailTxt);
                userData.put("mobileNumber", mobileNumber);
                userData.put("password", passTxt);
                userData.put("isActive", false);
                userData.put("id", currentUser_id);
                userData.put("tokenId", tokenId);
                firestoreDatabase.collection("Users").document(currentUser_id).set(userData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                progressBar.setVisibility(View.INVISIBLE);
                                // If firebase task is successful then call Home activity
                                Intent intent = new Intent(client_signUp.this, client_home.class);
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Showing error message to the user
                        Log.d(TAG, "This is failure listener " + e.getMessage());
                        Toast.makeText(client_signUp.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


    }
}
