package com.example.eventscheduling.eventorg.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.eventscheduling.R;
import com.example.eventscheduling.util.YesNoDialog;
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

    // Firebase FireStore
    CollectionReference userRef = FirebaseFirestore.getInstance().collection("Users");
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String currentUserID;
    // Layout EditTexts
    private EditText nameEdit;
    private EditText email;
    private EditText mobileNumber;
    private EditText password;
    private EditText businessName;
    private EditText province;
    private EditText city;
    private AutoCompleteTextView busCat_AutoComp;
    // String to store values of EditText
    private String emailTxt;
    private String passwordTxt;
    private String nameTxt;
    private String mobileNumberTxt;
    private String businessNameTxt;
    private String cityTxt;
    private String businessCatTxt;
    // Alert Dialog
    private AlertDialog.Builder alertDialog;
    private ProgressBar progressBar;
    // Map to send data to firestore
    private Map userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evntorg_sign_up);

        //Initialize the views
        Button regBtn = findViewById(R.id.sign2_btn_next);
        email = findViewById(R.id.sign1_edTxt_email);
        mobileNumber = findViewById(R.id.sign1_edTxt_mobileNo);
        password = findViewById(R.id.sign1_edTxt_password);
        businessName = findViewById(R.id.business_edtXt_signUp2);
        province = findViewById(R.id.province_signUp2_edtXt);
        city = findViewById(R.id.city_signUp2_edtXt);
        nameEdit = findViewById(R.id.sign1_edTxt_Name);

        // Initialize Firebase Auth
        //ProgressBar
        progressBar = findViewById(R.id.progBar_evnt_signUp1);
        //Alert Dialog
        alertDialog = new AlertDialog.Builder(evntOrg_signUp.this);
        // Initialize business Category AutoComplete TextView
        busCat_AutoComp = findViewById(R.id.evnt_signUp2_busCat_txtView);
        ArrayAdapter<String> busCat_ArrayAdapter = new ArrayAdapter<String>(evntOrg_signUp.this,
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.Business_Category));

        busCat_AutoComp.setAdapter(busCat_ArrayAdapter);
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
        if (progressBar.isShown()) {
            progressBar.setVisibility(View.VISIBLE);
            // TO disable the UI interaction while progress bar is shown on the screen
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        nameTxt = nameEdit.getText().toString();
        emailTxt = email.getText().toString();
        mobileNumberTxt = mobileNumber.getText().toString();
        passwordTxt = password.getText().toString();
        businessNameTxt = businessName.getText().toString();
        businessCatTxt = busCat_AutoComp.getText().toString();
        cityTxt = city.getText().toString();
        if (nameTxt.matches("") && emailTxt.matches("") && mobileNumberTxt.matches("")
                && passwordTxt.matches("") && businessCatTxt.matches("") && cityTxt.matches("") && passwordTxt.matches("")) {
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
            userData = new HashMap();
            userData.put("Name", nameTxt);
            userData.put("email", emailTxt);
            userData.put("mobileNumber", mobileNumberTxt);
            userData.put("password", passwordTxt);
            userData.put("businessName", businessNameTxt);
            userData.put("businessCat", businessCatTxt);
            mAuth.createUserWithEmailAndPassword(emailTxt, passwordTxt).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                // If user is sucessfully created
                @Override
                public void onSuccess(final AuthResult authResult) {
                    // Get the user ID
                    currentUser = authResult.getUser();
                    // if user is not null
                    if (currentUser != null) {
                        currentUserID = currentUser.getUid();
                        // Add data about the user in firestore
                        userRef.document(currentUserID).set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // If all operations are successful then call signUp2 activity
                                // Also pass the value of userId through intent
                                Intent intent = new Intent(evntOrg_signUp.this, evntOrg_home.class);
                                intent.putExtra("ID", currentUserID);
                                startActivity(intent);
                            }
                            // IF
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                currentUser = authResult.getUser();
                                currentUser.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        startActivity(new Intent(evntOrg_signUp.this, evntOrg_signIn.class));
                                        Toast.makeText(evntOrg_signUp.this, "Try again...", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        startActivity(new Intent(evntOrg_signUp.this, evntOrg_signIn.class));
                                        Toast.makeText(evntOrg_signUp.this, "Try again", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }


                }
                // If user is not successfully created then show error msg on the screen
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    /// Show alert dialog to show error message to the user
                    new AlertDialog.Builder(getApplicationContext())
                            .setTitle("Firebase Error")
                            .setMessage("Something went wrong" + "/n" + "Please try again later").setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            return true;
                        }
                    })

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });
        }
    }

    private void addUserData(String id) {

    }
}
