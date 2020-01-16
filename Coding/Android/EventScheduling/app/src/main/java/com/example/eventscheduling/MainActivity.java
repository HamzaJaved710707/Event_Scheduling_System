package com.example.eventscheduling;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.imageView2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventOrgIcon(v);
            }
        });
    }
    private void eventOrgIcon(View view){
        Toast.makeText(this, "This is toast activated while clicking on event org icon", Toast.LENGTH_SHORT).show();
        Intent  intent = new Intent(this, evntOrg_signIn.class);
        startActivity(intent);
    }
}
