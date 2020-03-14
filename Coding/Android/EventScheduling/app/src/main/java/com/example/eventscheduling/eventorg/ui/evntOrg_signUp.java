package com.example.eventscheduling.eventorg.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

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


public class evntOrg_signUp extends AppCompatActivity {
    private static final String TAG = "evntOrg_signUp";
    private static String url = "https://eventscheduling.000webhostapp.com/android/evntOrg_reg_1.php";
    EditText nameText;
    EditText email;
    EditText mobileNumber;
    EditText password;
    AlertDialog.Builder alertDialog;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evntorg_sign_up);

        //Initialize the views
        Button regBtn = findViewById(R.id.sign2_btn_next);
        nameText = findViewById(R.id.sign2_edTxt_cityName);
        email = findViewById(R.id.sign1_edTxt_email);
        mobileNumber = findViewById(R.id.sign2_edTxt_area);
        password = findViewById(R.id.sign1_edTxt_pass);
        //ProgressBar
        progressBar = findViewById(R.id.progBar_evnt_signUp1);
        //Alert Dialog
        alertDialog = new AlertDialog.Builder(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processing();
            }
        });
    }

//Function to send registration data to server
    // Get the response from server whether successful or not

    private void processing() {
        progressBar.setVisibility(View.VISIBLE);
        final String name = nameText.getText().toString();
        final String _email = email.getText().toString();
        final String mobileNo = mobileNumber.getText().toString();
        final String pass = password.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response from server" + response);
                        if(progressBar.isShown()){
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String result = jsonObject.getString("code");
                            if (result.matches("0")) {
                                // Some error occurred while inserting data into database
                                alertDialog.setTitle("Undefined Values");
                                alertDialog.setMessage("Please make sure you entered correct data");

                                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                alertDialog.show();

                            } else if (result.matches("1")) {
                                // Sucessfully insert data
                                startActivity(new Intent(evntOrg_signUp.this,evntOrg_signUp2.class));
                            } else if (result.matches("2")) {
                                // User is already registered with this email
                                alertDialog.setTitle("Alert");
                                alertDialog.setMessage("You are already registered");
                                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                alertDialog.show();

                            } else if (result.matches("3")) {
                                // Some values are not set by user
                                alertDialog.setTitle("Missing Values");
                                alertDialog.setMessage("Please fill the missing values");
                                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                alertDialog.show();

                            } else {
                                // Undefined error occurred
                                alertDialog.setTitle("Undefined Error");
                                alertDialog.setMessage("Ooppsss... Sorry for inconvenience" +
                                        " " + "Enter your data again" );
                                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                alertDialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "JSON exception in String response: " + e.getMessage());
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.getMessage());
                VolleyLog.d(error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Get values from user and send them to server for registration
                Map<String, String> parameters = new HashMap<>();
                parameters.put("name", name);
                parameters.put("email", _email);
                parameters.put("mobileNumber", mobileNo);
                parameters.put("password", pass);
                return parameters;
            }
        };
        VolleySingleton.getInstance().addReqToQueue(stringRequest);
    }
}
