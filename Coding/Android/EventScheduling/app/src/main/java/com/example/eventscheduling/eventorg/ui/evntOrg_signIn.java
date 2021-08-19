package com.example.eventscheduling.eventorg.ui;

import android.app.AlertDialog;
import android.app.Dialog;
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

import com.example.eventscheduling.MainActivity;
import com.example.eventscheduling.R;
import com.example.eventscheduling.util.BaseActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import thebat.lib.validutil.ValidUtils;

public class evntOrg_signIn extends BaseActivity {
    private static final String TAG = "evntOrg_signIn";
   Button signInBtn;
    Button registerBtn;
    EditText emailTxt;
    EditText passTxt;
    TextView forgetPassTxt;

    //Url to fetch data from server
    //Firebase Authentication variable
    FirebaseAuth mAuth;
    // This tag will be used to cancel the request
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private Dialog mOverlayDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_org_sign_in);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        signInBtn = findViewById(R.id.evnt_signIn);
        registerBtn = findViewById(R.id.evnt_registerBtn);
        emailTxt = findViewById(R.id.evnt_email_editText);
        passTxt = findViewById(R.id.evnt_password_editText);
        forgetPassTxt = findViewById(R.id.evnt_forget_textView);
        forgetPassTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(evntOrg_signIn.this, evntOrg_emailVerification.class));
            }
        });
        //ProgressBar
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Processing");
        mOverlayDialog = new Dialog(evntOrg_signIn.this, android.R.style.Theme_Panel); //display an invisible overlay dialog to prevent user interaction and pressing back
        mOverlayDialog.setCancelable(false);
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
                mOverlayDialog.show();
                if (ValidUtils.isNetworkAvailable(evntOrg_signIn.this)) {
                    final String email = emailTxt.getText().toString();
                    final String password = passTxt.getText().toString();
                    Log.d(TAG, "onClick: email is " + email);
                    Log.d(TAG, "onClick: password is " + password);
                    if (email.matches("") && password.matches("")) {
                        dialog.dismiss();
                        mOverlayDialog.dismiss();
                        Toast.makeText(evntOrg_signIn.this, "Enter your data first", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onClick: enter data first ");

                    } else {
                        if (ValidUtils.validateEmail(emailTxt)) {


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
                                                ;
                                                firebaseFirestore.collection("Users").document(currentUserId).get()
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                long field = documentSnapshot.getLong("type");
                                                                boolean isActive = documentSnapshot.getBoolean("isActive");
                                                                if (field == 0) {
                                                                    if (isActive) {
                                                                        dialog.dismiss();
                                                                        mOverlayDialog.dismiss();
                                                                        Intent intent = new Intent(evntOrg_signIn.this, evntOrg_home.class);
                                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK & Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                        startActivity(intent);

                                                                        finish();

                                                                    } else {
                                                                        dialog.dismiss();
                                                                        mOverlayDialog.dismiss();
                                                                        Toast.makeText(evntOrg_signIn.this, "You are blocked by admin... Please connect support center", Toast.LENGTH_LONG).show();

                                                                    }

                                                                } else {
                                                                    dialog.dismiss();
                                                                    mOverlayDialog.dismiss();
                                                                    Toast.makeText(evntOrg_signIn.this, "You are not service provider... LogIn in other module", Toast.LENGTH_SHORT).show();
                                                                    startActivity(new Intent(evntOrg_signIn.this, MainActivity.class));

                                                                }

                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                dialog.dismiss();
mOverlayDialog.dismiss();
                                                                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                                                mAuth.signOut();
                                                                Toast.makeText(evntOrg_signIn.this, "Error", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(evntOrg_signIn.this, MainActivity.class);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK & Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        });
                                            }
                                        });


                                    }


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialog.dismiss();
                                    mOverlayDialog.dismiss();
                                    Toast.makeText(evntOrg_signIn.this, "Error... Try agian", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });

                        }
                        else{
                            Toast.makeText(evntOrg_signIn.this, "Email badly formated", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                else {
                    dialog.dismiss();
                    mOverlayDialog.dismiss();
                    Toast.makeText(evntOrg_signIn.this , "Please make sure you are connected to Internet", Toast.LENGTH_SHORT).show();
                }
            }



        });

    }
    private void openPlaceCallActivity() {
      /*  Call call = getSinchServiceInterface().callUserVideo(mChatUserId);
        String callId = call.getCallId();

        Intent callScreen = new Intent(this, CallActivity.class);
        callScreen.putExtra(SinchService.CALL_ID, callId);
        startActivity(callScreen);*/
    }





}
