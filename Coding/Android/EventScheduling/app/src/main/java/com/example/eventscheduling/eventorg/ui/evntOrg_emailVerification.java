package com.example.eventscheduling.eventorg.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.eventscheduling.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class evntOrg_emailVerification extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evnt_org_email_verification);
       mAuth = FirebaseAuth.getInstance();
       user = mAuth.getCurrentUser();
       if(user != null){
           if(user.isEmailVerified()){
               Intent intent = new Intent(evntOrg_emailVerification.this,evntOrg_home.class);
              intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK & Intent.FLAG_ACTIVITY_CLEAR_TASK);
              startActivity(intent);
               finish();
           }
       }

        Button buttonExit   = findViewById(R.id.btn_email_verify);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(evntOrg_emailVerification.this, evntOrg_signIn.class));
            }
        });
        Button buttonSend = findViewById(R.id.send_email_veri_buttton);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(evntOrg_emailVerification.this, "Verification mail has been send", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(evntOrg_emailVerification.this, "Some error while sending... Try again", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}
