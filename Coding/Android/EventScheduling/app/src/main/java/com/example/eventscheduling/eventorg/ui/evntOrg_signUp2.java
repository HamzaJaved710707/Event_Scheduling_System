package com.example.eventscheduling.eventorg.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import com.example.eventscheduling.R;

import java.util.ArrayList;

public class evntOrg_signUp2 extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evntorg_sign_up2);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Button regBtn = findViewById(R.id.next_button);
        regBtn.setOnClickListener(this);
        Spinner spinner = findViewById(R.id.spinner);
        ArrayList<String> list = new ArrayList<>();
        list.add("Event Organizer");
        list.add("Venuer");
        list.add("Decoration");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list);
        spinner.setAdapter(arrayAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_button:
                Intent intent = new Intent(this, evntOrg_signUp3.class);
                startActivity(intent);
                break;
        }
    }

}
