package com.example.eventscheduling.client.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.eventscheduling.R;
import com.example.eventscheduling.conn.VolleySingleton;

import java.util.HashMap;
import java.util.Map;


public class client_signUp extends AppCompatActivity {
    private static String url = "https://www.eventscheduling.000webapps.com";
    private EditText _name;
    private EditText _email;
    private EditText _mobileNumber;
    private EditText _password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize variables
        setContentView(R.layout.activity_client_signup);
        Button regBtn = findViewById(R.id.sign2_btn_next);
        _name = findViewById(R.id.signC_edTxt_name);
        _email = findViewById(R.id.signC_edTxt_email);
        _mobileNumber = findViewById(R.id.signC_edTxt_mNo);
        _password = findViewById(R.id.signC_edTxt_pass);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processing();
            }
        });
    }

    private void processing() {
        final String name = _name.getText().toString();
        final String email = _email.getText().toString();
        final String mobileNo = _mobileNumber.getText().toString();
        final String pass = _password.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("name", name);
                parameters.put("email", email);
                parameters.put("mobileNumber", mobileNo);
                parameters.put("password", pass);
                return parameters;
            }
        };
        VolleySingleton.getInstance().addReqToQueue(stringRequest);
    }
}
