package com.example.eventscheduling.client.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.eventscheduling.MapLocation;
import com.example.eventscheduling.R;
import com.example.eventscheduling.conn.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class client_signUp extends AppCompatActivity {
    private static final String TAG = "client_signUp";
    private static String url = "https://eventscheduling.000webhostapp.com/android/client/client_reg.php";
    ProgressBar progressBar;
    AlertDialog.Builder alertDialog;
    SharedPreferences sharedPreferences;
    String mLatitude;
    String mLongitude;
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
        // Initialize Progress Bar
        progressBar = findViewById(R.id.progBar_Client_signUp);
        // Initialize Alert dialog
        alertDialog = new AlertDialog.Builder(this);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processing();
            }
        });
        // Initializing ImageView of map
        ImageView imageView = findViewById(R.id.client_mapView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_preference();
                Intent intent = new Intent(client_signUp.this, MapLocation.class);
                intent.putExtra("Value", "2");
                startActivity(intent);
            }
        });
        // Saving data before calling Map Activity
        Intent intent = getIntent();
        String value = intent.getStringExtra("value");
        if (value != null && value.matches("1")) {
            get_preference();
        }
        Log.d(TAG, "onCreate: value of coming intent " + value);
        mLatitude = getIntent().getStringExtra("latitude");
        mLongitude = getIntent().getStringExtra("longitude");
        Log.d(TAG, "onCreate: lat and long" + mLatitude + mLongitude);
    }

    private void processing() {
        final String name = _name.getText().toString();
        final String email = _email.getText().toString();
        final String mobileNo = _mobileNumber.getText().toString();
        final String pass = _password.getText().toString();
        if (name.matches("") && email.matches("") && mobileNo.matches("") && name.matches("")) {
            // If values are not set by the user
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Please enter your data");
        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "Response from server" + response);
                            if (progressBar.isShown()) {
                                progressBar.setVisibility(View.INVISIBLE);
                                // TO enable UI interaction back
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            }
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String result = jsonObject.getString("code");
                                Log.d(TAG, "onResponse: we get to array");
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
                                    startActivity(new Intent(client_signUp.this, client_home.class));
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
                                            " " + "Enter your data again");
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
                                Log.d(TAG, "onResponse: " + e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("name", name);
                    parameters.put("email", email);
                    parameters.put("mobileNumber", mobileNo);
                    parameters.put("password", pass);
                    parameters.put("latitude", mLatitude);
                    parameters.put("longitude", mLongitude);
                    return parameters;
                }
            };
            VolleySingleton.getInstance().addReqToQueue(stringRequest);
        }
    }

    private void set_preference() {
        Log.d(TAG, "set_preference: ");

        sharedPreferences = getSharedPreferences("Map_preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Name", _name.getText().toString());
        editor.putString("Email", _email.getText().toString());
        editor.putString("MobileNumber", _mobileNumber.getText().toString());
        editor.putString("Password", _password.getText().toString());
        editor.apply();
    }

    private void get_preference() {
        Log.d(TAG, "get_preference: ");
        SharedPreferences sharedPreferences1 = getSharedPreferences("Map_preference", Context.MODE_PRIVATE);
        _name.setText(sharedPreferences1.getString("Name", ""));
        _email.setText(sharedPreferences1.getString("Email", ""));
        _mobileNumber.setText(sharedPreferences1.getString("MobileNumber", ""));
        _password.setText(sharedPreferences1.getString("Password", ""));


    }

}
