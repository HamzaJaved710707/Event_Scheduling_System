package com.example.eventscheduling.eventorg.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventscheduling.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class evntOrg_signIn extends AppCompatActivity {
    private static final String TAG = "evntOrg_signIn";
    Button signInBtn;
    Button registerBtn;
    EditText emailTxt;
    EditText passTxt;
    TextView forgetPassTxt;
    //Url to fetch data from server
    String url = "https://eventscheduling.000webhostapp.com/android/eventOrganizer/logIn_data.php";
    //Firebase Authentication variable
    FirebaseAuth mAuth;
    FirebaseUser user;
    // This tag will be used to cancel the request
    private String tag_string_req = "string_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        signInBtn = findViewById(R.id.signIn);
        registerBtn = findViewById(R.id.registerBtn);
        emailTxt = findViewById(R.id.email_editText);
        passTxt = findViewById(R.id.password_editText);
        forgetPassTxt = findViewById(R.id.forget_textView);
        forgetPassTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(evntOrg_signIn.this, evntOrg_signUp2.class));
            }
        });
        //ProgressBar
        final ProgressDialog dialog = new ProgressDialog(this);
        //Alert Dialog
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(evntOrg_signIn.this);
        //To disable automatic popup of keyboard on screen...
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mAuth = FirebaseAuth.getInstance();
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), evntOrg_signUp.class);
                startActivity(intent);
            }
        });
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                final String email = emailTxt.getText().toString();
                final String password = passTxt.getText().toString();
                Log.d(TAG, "onClick: email is " + email);
                Log.d(TAG, "onClick: password is " + password);
                if (email.matches("") && password.matches("")) {
                    Toast.makeText(evntOrg_signIn.this, "Enter your data first", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onClick: enter data first ");

                } else {
                    Log.d(TAG, "onClick: in string is called");
                    mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            dialog.dismiss();
                            Intent intent = new Intent(evntOrg_signIn.this, evntOrg_home.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK & Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(evntOrg_signIn.this, "Error... Try agian", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onFailure: " + e.toString());
                        }
                    });

                }


            }
        });

    }


}
