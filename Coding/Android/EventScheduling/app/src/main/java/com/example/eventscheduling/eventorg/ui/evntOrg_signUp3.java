package com.example.eventscheduling.eventorg.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.eventscheduling.MapLocation;
import com.example.eventscheduling.R;

public class evntOrg_signUp3 extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evnt_org_sign_up3);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ImageView mapImage = findViewById(R.id.map_image_id);
        mapImage.setClickable(true);
        mapImage.setOnClickListener(this);
        Button nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.map_image_id:
                map();
                break;
            case R.id.next_button:
                Intent intent = new Intent(this, evntOrg_home.class);
                startActivity(intent);
                break;
        }
    }

    public void map() {
        Intent intent = new Intent(this, MapLocation.class);
        startActivity(intent);
    }
}
