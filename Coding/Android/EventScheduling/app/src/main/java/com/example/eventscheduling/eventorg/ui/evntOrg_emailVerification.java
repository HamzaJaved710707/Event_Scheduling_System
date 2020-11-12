package com.example.eventscheduling.eventorg.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventscheduling.MainActivity;
import com.example.eventscheduling.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class evntOrg_emailVerification extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private MaterialButton sendRequest;
    private MaterialButton exitButton;
    private TextInputEditText emailTxt;
    private String emailString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evnt_org_email_verification);
        mAuth = FirebaseAuth.getInstance();
        sendRequest = findViewById(R.id.send_request_password);
        sendRequest.setOnClickListener(this);
        exitButton = findViewById(R.id.btn_request_exit);
        exitButton.setOnClickListener(this);
        emailTxt = findViewById(R.id.evnt_password_reset_editTxt);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Reset Password");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_request_password:
                sendRequestMethod();
                break;
            case R.id.btn_request_exit:

                startActivity(new Intent(evntOrg_emailVerification.this, MainActivity.class));
                break;
            default:
                break;
        }
    }

    private void sendRequestMethod() {

        emailString = emailTxt.getText().toString().trim();
        if (!emailString.equals("")) {
            mAuth.sendPasswordResetEmail(emailString).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(evntOrg_emailVerification.this, "Reset email send sucessfully", Toast.LENGTH_SHORT).show();
                    emailTxt.setText("");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(evntOrg_emailVerification.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(this, "Enter email address", Toast.LENGTH_SHORT).show();
        }


    }
}
