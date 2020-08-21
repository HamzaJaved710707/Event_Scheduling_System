package com.example.eventscheduling.eventorg.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventscheduling.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableReference;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Map;


public class evntOrg_setting extends AppCompatActivity {
    private Button dlt_account_btn;
    private  FirebaseAuth mAuth;
    private  FirebaseUser currentUser;
    private FirebaseFirestore firestore;
    private CollectionReference userCollection;
    private  String currentUserId;
    private static final String TAG = "evntOrg_setting";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        userCollection = firestore.collection("Users");
        currentUserId = currentUser.getUid();
        setContentView(R.layout.activity_evnt_org_setting);
        dlt_account_btn = findViewById(R.id.delete_account_btn_evntOrg);
        dlt_account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentUser != null ) {
                    firebaseFunctionCall(userCollection.getPath());
                }
            }
            
        });
    }
// Function to invoke firebase Function to delete user account
    private void firebaseFunctionCall(String path){
         Map<String, Object> data = new HashMap<>();
            data.put("path", path);
        Log.d(TAG, "firebaseFunctionCall: " + path);
            HttpsCallableReference deleteFn =
                    FirebaseFunctions.getInstance().getHttpsCallable("recursiveDelete");
            deleteFn.call(data)
                    .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                        @Override
                        public void onSuccess(HttpsCallableResult httpsCallableResult) {
                            Toast.makeText(evntOrg_setting.this, "Sucessfully deleted", Toast.LENGTH_SHORT).show();
                            // Delete Success
                            // ...
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: " + e.getMessage());
                            Toast.makeText(evntOrg_setting.this, "Not Successful", Toast.LENGTH_SHORT).show();
                            // Delete failed
                            // ...
                        }
                    });
        }


}