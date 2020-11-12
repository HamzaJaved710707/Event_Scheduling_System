package com.example.eventscheduling.client.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventscheduling.MainActivity;
import com.example.eventscheduling.R;
import com.example.eventscheduling.eventorg.ui.evntOrg_emailVerification;
import com.example.eventscheduling.util.BaseActivity;
import com.example.eventscheduling.util.SinchService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.sinch.android.rtc.SinchError;

public class client_signIn extends BaseActivity implements SinchService.StartFailedListener {
    private static final String TAG = "client_signIn";

    TextView emailTxt;
    TextView passwordTxt;
    FirebaseAuth mAuth;
    TextView forgetTxtView;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private ProgressDialog progressDialog;
    private Dialog mOverlayDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
    //    this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // Firebase Authentication initialization
        mAuth = FirebaseAuth.getInstance();
        //Initialize views
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Processing");
        mOverlayDialog = new Dialog(client_signIn.this, android.R.style.Theme_Panel); //display an invisible overlay dialog to prevent user interaction and pressing back
        mOverlayDialog.setCancelable(false);
     //   mOverlayDialog.show();
        Button signInBtn = findViewById(R.id.signIn);
        Button registerBtn = findViewById(R.id.registerBtn);
        emailTxt = findViewById(R.id.email_editText);
        passwordTxt = findViewById(R.id.password_editText);
        forgetTxtView = findViewById(R.id.forget_textView);
        forgetTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(client_signIn.this, evntOrg_emailVerification.class  );
                startActivity(intent);
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), client_signUp.class);
                startActivity(intent);
                finish();
            }
        });
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailTxt.getText().toString();
                final String password = passwordTxt.getText().toString();
                Log.d(TAG, "onClick: email is " + email);
                Log.d(TAG, "onClick: password is " + password);
                if (email.matches("") && password.matches("")) {
                    progressDialog.hide();
                    Toast.makeText(client_signIn.this, "Enter your data first", Toast.LENGTH_SHORT).show();

                } else {


                    progressDialog.show();
                    Log.d(TAG, "onClick: in string is called");
                    mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser currentUser = authResult.getUser();
                            if (currentUser != null) {

                                String currentUserId = currentUser.getUid();
                                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                    @Override
                                    public void onSuccess(InstanceIdResult instanceIdResult) {
                                         String tokenId = instanceIdResult.getToken();
                                         firebaseFirestore.collection("Users").document(currentUserId).update("tokenId", tokenId);
                                        firebaseFirestore.collection("Users").document(currentUserId).get()
                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        long field = documentSnapshot.getLong("type");
                                                        boolean isActive = documentSnapshot.getBoolean("isActive");
                                                        if (field == 1) {
                                                            if(isActive){
                                                                progressDialog.dismiss();
                                                                mOverlayDialog.dismiss();
                                                                startActivity(new Intent(client_signIn.this, client_home.class));
                                                                finish();

                                                            }
                                                            else{
                                                                progressDialog.dismiss();
                                                                mOverlayDialog.dismiss();
                                                                Toast.makeText(client_signIn.this, "You are blocked by admin... Please connect the support center", Toast.LENGTH_LONG).show();

                                                            }

                                                        } else {
                                                            progressDialog.dismiss();
                                                            mOverlayDialog.dismiss();
                                                            Toast.makeText(client_signIn.this, "You are not client... LogIn in other module", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(client_signIn.this, MainActivity.class));

                                                        }

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        progressDialog.dismiss();
mOverlayDialog.dismiss();
                                                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                                        mAuth.signOut();
                                                        Toast.makeText(client_signIn.this, "Error", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(client_signIn.this, MainActivity.class));
                                                    }
                                                });
                                    }
                                });


                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            mOverlayDialog.dismiss();
                            Log.d(TAG, "onFailure: " + e.getMessage());
                            Toast.makeText(client_signIn.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }

        });
    }

    private void openPlaceCallActivity() {
    }

    @Override
    public void onStartFailed(SinchError error) {

    }

    @Override
    public void onStarted() {

    }
}
