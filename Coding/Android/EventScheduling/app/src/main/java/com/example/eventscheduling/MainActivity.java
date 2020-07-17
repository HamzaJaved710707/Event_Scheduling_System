package com.example.eventscheduling;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventscheduling.client.ui.client_signIn;
import com.example.eventscheduling.eventorg.ui.evntOrg_signIn;
import com.example.eventscheduling.eventorg.ui.evntOrg_signUp2;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);
        //Initialize views
        ImageView evntImage = findViewById(R.id.evnt_image);
        ImageView clientImage = findViewById(R.id.client_imageView);
        // Event Listeners
        evntImage.setOnClickListener(this);
        clientImage.setOnClickListener(this);

    }


    // Implementation of onClick listener
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.evnt_image:
                Intent intent = new Intent(this, evntOrg_signIn.class);
                startActivity(intent);
                break;
            case R.id.client_imageView:
                Intent intent1 = new Intent(this, client_signIn.class);
                startActivity(intent1);
                break;
            default:
                Intent intent2 = new Intent(this, client_signIn.class);
                startActivity(intent2);
                break;
        }
    }
}
