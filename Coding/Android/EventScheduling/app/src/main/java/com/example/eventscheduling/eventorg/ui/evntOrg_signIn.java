package com.example.eventscheduling.eventorg.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventscheduling.R;

public class evntOrg_signIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        Button signInBtn = findViewById(R.id.signIn);
        Button registerBtn = findViewById(R.id.registerBtn);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
                Intent intent = new Intent(getApplicationContext(), evntOrg_home.class);
                startActivity(intent);
            }
        });
    }
}
