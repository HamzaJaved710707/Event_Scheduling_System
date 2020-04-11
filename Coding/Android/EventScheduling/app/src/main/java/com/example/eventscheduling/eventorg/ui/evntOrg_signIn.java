package com.example.eventscheduling.eventorg.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.eventscheduling.R;
import com.example.eventscheduling.conn.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class evntOrg_signIn extends AppCompatActivity {
    private static final String TAG = "evntOrg_signIn";
    Button signInBtn;
    Button registerBtn;
    EditText emailTxt;
    EditText passTxt;
    //Url to fetch data from server
    String url = "https://eventscheduling.000webhostapp.com/android/eventOrganizer/logIn_data.php";
    // This tag will be used to cancel the request
    private String tag_string_req = "string_req";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        signInBtn = findViewById(R.id.signIn);
        registerBtn = findViewById(R.id.registerBtn);
        emailTxt = findViewById(R.id.email_editText);
        passTxt = findViewById(R.id.password_editText);
        //ProgressBar
        final ProgressBar progressBar = findViewById(R.id.progBar_Evnt_signIn);
        //Alert Dialog
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(evntOrg_signIn.this);
        //To disable automatic popup of keyboard on screen...
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
                                             progressBar.setVisibility(View.VISIBLE);
                                             final String email = emailTxt.getText().toString();
                                             final String password = passTxt.getText().toString();
                                             Log.d(TAG, "onClick: email is " + email);
                                             Log.d(TAG, "onClick: password is " + password);
                                             if (email.matches("") && password.matches("")) {
                                                 Toast.makeText(evntOrg_signIn.this, "Enter your data first", Toast.LENGTH_SHORT).show();

                                             } else {
                                                 Log.d(TAG, "onClick: in string is called");
                                                 StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                                     @Override
                                                     public void onResponse(String response) {
                                                         try {
                                                             if(progressBar.isShown()){
                                                                progressBar.setVisibility(View.INVISIBLE);
                                                             }
                                                             Log.d(TAG, "onResponse: WE get some response" + response);
                                                             JSONArray jsonArray = new JSONArray(response);
                                                             JSONObject jsonObject = jsonArray.getJSONObject(0);
                                                             String value = jsonObject.getString("code");
                                                             Log.d(TAG, "onResponse: " + value);
                                                             if (value.matches("1")) {
                                                                 // IF everything is sucessfull then go to Home Activity
                                                              Intent intent = new Intent(getApplicationContext(),evntOrg_home.class);
                                                              startActivity(intent);
                                                             } else if(value.matches("0")){
                                                                 Toast.makeText(evntOrg_signIn.this, "Incorrect Email or Password", Toast.LENGTH_SHORT).show();
                                                             }
                                                             else{
                                                                 Toast.makeText(evntOrg_signIn.this, "Unexpected Error", Toast.LENGTH_SHORT).show();
                                                             }
                                                         } catch (JSONException e) {
                                                             e.printStackTrace();
                                                             Log.d(TAG, "JSON exception in String response: " + e.getMessage());
                                                         }


                                                     }
                                                 }, new Response.ErrorListener() {
                                                     @Override
                                                     public void onErrorResponse(VolleyError error) {
                                                         Toast.makeText(evntOrg_signIn.this, "Response Error", Toast.LENGTH_SHORT).show();
                                                         VolleyLog.d("Volley Log", error.getMessage());
                                                     }
                                                 }
                                                 ) {
                                                     @Override
                                                     protected Map<String, String> getParams() throws AuthFailureError {
                                                         Map<String, String> params = new HashMap<>();
                                                         params.put("email", email);
                                                         Log.d(TAG, "getParams: value of email" + email );
                                                         params.put("password", password);
                                                         return params;

                                                     }
                                                 };
                                                 VolleySingleton.getInstance().addReqToQueue(stringRequest);
                                             }
                                         }

                                     }
        );


    }
}
