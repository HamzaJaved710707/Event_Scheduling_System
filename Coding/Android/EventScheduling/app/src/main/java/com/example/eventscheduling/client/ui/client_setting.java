package com.example.eventscheduling.client.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventscheduling.R;
import com.example.eventscheduling.eventorg.ui.evntOrg_home;
import com.example.eventscheduling.eventorg.ui.evntOrg_signIn;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class client_setting extends AppCompatActivity {
    private Button dlt_account_btn;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private TextInputEditText passwordEdit;
    private TextInputEditText confirmEdit;
    private MaterialButton changeBtn;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_setting);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        passwordEdit = findViewById(R.id.client_setting_change_pass_edit1);
        confirmEdit = findViewById(R.id.client_setting_change_pass_edit2);
        changeBtn = findViewById(R.id.client_change_pass_btn);
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordEdit.getText() != null  && confirmEdit.getText() != null){
                    String pass = passwordEdit.getText().toString();
                    String confirmPass = confirmEdit.getText().toString();
                    if( pass.equals(confirmPass)){
                        currentUser.updatePassword(pass).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(client_setting.this, "Password Updated...", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(client_setting.this, "Unable to change your password", Toast.LENGTH_SHORT).show();    
                            }
                        });
                    }
                }
              
            }
        });
        dlt_account_btn = findViewById(R.id.delete_account_btn_client);
        dlt_account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser != null) {
                    currentUser.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(client_setting.this, "Successfully deleted", Toast.LENGTH_SHORT).show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(client_setting.this, evntOrg_signIn.class));
                                }
                            }, 1000);
                        }
                    });
                }
            }

        });
    }
}