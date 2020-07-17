package com.example.eventscheduling.eventorg.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventscheduling.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class evntOrg_signUp extends AppCompatActivity {
    private static final String TAG = "evntOrg_signUp";
    private static String url = "https://eventscheduling.000webhostapp.com/android/eventOrganizer/evntOrg_reg_1.php";
    // Firebase FireStore
    CollectionReference userRef = FirebaseFirestore.getInstance().collection("Users");
    FirebaseAuth mAuth;

    private EditText nameText;
    private EditText email;
    private EditText mobileNumber;
    private EditText password;
    private AlertDialog.Builder alertDialog;
    private ProgressBar progressBar;
    private String emailTxt;
    private String passwordTxt;
    private String nameTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evntorg_sign_up);

        //Initialize the views
        Button regBtn = findViewById(R.id.sign2_btn_next);
        nameText = findViewById(R.id.sign2_edTxt_cityName);
        email = findViewById(R.id.sign1_edTxt_email);
        mobileNumber = findViewById(R.id.sign2_edTxt_area);
        password = findViewById(R.id.sign1_edTxt_pass);
        // Initialize Firebase Auth
        //ProgressBar
        progressBar = findViewById(R.id.progBar_evnt_signUp1);
        //Alert Dialog
        alertDialog = new AlertDialog.Builder(evntOrg_signUp.this);
        //To disable keyboard to automatically popup
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processing();
            }
        });
        // Initialize firestore variables
        mAuth = FirebaseAuth.getInstance();
    }

//Function to send registration data to server
    // Get the response from server whether successful or not

    private void processing() {
        Log.d(TAG, "processing:  is called ");
        if (!progressBar.isShown()) {
            progressBar.setVisibility(View.VISIBLE);
            // TO disable the UI interaction while progress bar is shown on the screen
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        nameTxt = nameText.getText().toString();
        emailTxt = email.getText().toString();
        final String mobileNo = mobileNumber.getText().toString();
        passwordTxt = password.getText().toString();
        if (nameTxt.matches("") && emailTxt.matches("") && mobileNo.matches("") && passwordTxt.matches("")) {
            // Undefined error occurred
            if (progressBar.isShown()) {
                progressBar.setVisibility(View.INVISIBLE);
            }
            alertDialog.setTitle("Undefined Error");
            alertDialog.setMessage("Enter your data first");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();

        } else {
            final Map userData = new HashMap();
            userData.put("Name", nameTxt);
            userData.put("email", emailTxt);
            userData.put("mobileNumber", mobileNo);
            userData.put("password", passwordTxt);
            mAuth.createUserWithEmailAndPassword(emailTxt, passwordTxt).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    FirebaseUser currentUser = authResult.getUser();
                    String currentUserId = currentUser.getUid();
                    userRef.document(currentUserId).set(userData);
                    startActivity(new Intent(evntOrg_signUp.this, evntOrg_signUp2.class));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(evntOrg_signUp.this, "Error while processing. Try again", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
