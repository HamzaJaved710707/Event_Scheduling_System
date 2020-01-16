package com.example.eventscheduling;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class evntOrg_signUp2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evntorg_sign_up2);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Button regBtn = findViewById(R.id.button);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), evntOrg_home.class);
                startActivity(intent);
            }
        });

        Spinner spinner = findViewById(R.id.spinner);
        ArrayList<String> list = new ArrayList<>();
        list.add("Event Organizer");
        list.add("Venuer");
        list.add("Decoration");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,list);
        spinner.setAdapter(arrayAdapter);
    }

}
