package com.example.eventscheduling;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    private final int splash_display_length = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.splashScreenStyle);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                   startActivity(intent);
            }
      },splash_display_length);
    }
}
