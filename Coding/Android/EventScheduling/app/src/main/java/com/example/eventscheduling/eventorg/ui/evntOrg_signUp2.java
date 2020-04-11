package com.example.eventscheduling.eventorg.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.eventscheduling.MapLocation;
import com.example.eventscheduling.R;
import com.example.eventscheduling.conn.VolleySingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class evntOrg_signUp2 extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "evntOrg_signUp2";
    private static String url = "https://eventscheduling.000webhostapp.com/android/eventOrganizer/evntOrg_reg_2.php";
    AlertDialog.Builder alertDialog;
    ProgressBar progressBar;
    // Variable to hold the map location of user
    String mLatitude;
    String mLongitude;
    String event_table_id1;
    private EditText businessName;
    private EditText province;
    private EditText city;
    private EditText area;
    private Spinner cat_spinner;
    private SharedPreferences sharedPreferences;
    private String email;
    private String password;
    // Firebase Authentication
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evnt_org_sign_up2);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        // Get id using intent from eventOrg signUp1
        event_table_id1 = getIntent().getStringExtra("id");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        Log.d(TAG, "onCreate: email " + email + "Password" + password);
        Log.d(TAG, "event Table value  " + event_table_id1);
        // Values from Map
        Log.d(TAG, "onCreate: value of coming intent " + event_table_id1);
        mLatitude = getIntent().getStringExtra("latitude");
        mLongitude = getIntent().getStringExtra("longitude");
        String value = getIntent().getStringExtra("value");
        Log.d(TAG, "onCreate: lat and long" + mLatitude + mLongitude);
        //Initialize views
        ImageView mapImage = findViewById(R.id.map_image_id);
        businessName = findViewById(R.id.sign2_edTxt_bussName);
        province = findViewById(R.id.sign2_edTxt_province);
        city = findViewById(R.id.sign2_edTxt_cityName);
        area = findViewById(R.id.sign2_edTxt_area);
        // ALertDialog
        alertDialog = new AlertDialog.Builder(evntOrg_signUp2.this);
        // ProgressBar
        progressBar = findViewById(R.id.progBar_Evnt_SignUp2);
        // Set click listener
        mapImage.setClickable(true);
        mapImage.setOnClickListener(this);
        Button nextButton = findViewById(R.id.sign2_btn_next);
        nextButton.setOnClickListener(this);
        // Initialize spinner
        cat_spinner = findViewById(R.id.sign2_spin_bussCat);
        ArrayList<String> list = new ArrayList<>();
        list.add("Event Organizer");
        list.add("Venuer");
        list.add("Decoration");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list);
        cat_spinner.setAdapter(arrayAdapter);
        cat_spinner.setSelection(0); // Select default value
        if (value != null && value.matches("1")) {
            get_preference();
        }

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
    }

    // Store current ACtivity data before calling another activity
    private void set_preference() {
        Log.d(TAG, "set_preference: ");

        sharedPreferences = getSharedPreferences("Map_preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("BusName", businessName.getText().toString());
        editor.putString("province", province.getText().toString());
        editor.putString("city", city.getText().toString());
        editor.putString("area", area.getText().toString());
        editor.putInt("cat_spinner", cat_spinner.getSelectedItemPosition());
        editor.putString("id", event_table_id1);
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
    }

    private void get_preference() {
        Log.d(TAG, "get_preference: ");
        SharedPreferences sharedPreferences1 = getSharedPreferences("Map_preference", Context.MODE_PRIVATE);
        businessName.setText(sharedPreferences1.getString("BusName", ""));
        province.setText(sharedPreferences1.getString("province", ""));
        city.setText(sharedPreferences1.getString("city", ""));
        area.setText(sharedPreferences1.getString("area", ""));
        event_table_id1 = sharedPreferences1.getString("id", null);
        cat_spinner.setSelection(sharedPreferences1.getInt("cat_spinner", 0));
        email = sharedPreferences1.getString("email","");
        password = sharedPreferences1.getString("password", "");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.map_image_id:
                map();
                break;
            case R.id.sign2_btn_next:
                processing();
                break;
        }
    }

    // Call map activity
    public void map() {
        set_preference();
        Intent intent = new Intent(this, MapLocation.class);
        intent.putExtra("Value", "1");
        startActivity(intent);
    }

    // Do processing
    // Send data to remote server and get response whether operation was successful or not
    private void processing() {
        Log.d(TAG, "processing: function is called");
        progressBar.setVisibility(View.VISIBLE);

        // Get the values from editTexts and spinner which user has provided
        final String _businessName = businessName.getText().toString();
        final String _province = province.getText().toString();
        final String _city = city.getText().toString();
        final String _area = area.getText().toString();
        final String category = cat_spinner.getSelectedItem().toString();
        // If user has not provided any information
        if (_businessName.matches("") && _province.matches("") && _city.matches("") && _area.matches("")) {
            if (mLongitude.matches("") && mLatitude.matches("")) {
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Please fill the form");
                alertDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
            }
        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "onResponse: we get some response from server" + response);
                            progressBar.setVisibility(View.INVISIBLE);

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
                                    progressBar.setVisibility(View.VISIBLE);

                                    registerWithFB();

                                } else if (result.matches("2")) {
                                    // Some values are not set by the user
                                    alertDialog.setTitle("Missing Values");
                                    alertDialog.setMessage("Please make sure to fill the data correctly");
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
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("Volley Log " + error.getMessage());
                    if (progressBar.isShown()) {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    alertDialog.setTitle("Undefined Error");
                    alertDialog.setMessage("SOme network error");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();
                }
            }) {
                @Override
                protected Map<String, String> getParams()  {
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("businessName", _businessName);
                    parameters.put("province", _province);
                    parameters.put("city", _city);
                    parameters.put("area", _area);
                    parameters.put("category", category);
                    parameters.put("latitude", mLatitude);
                    parameters.put("longitude", mLongitude);
                    parameters.put("event_id", event_table_id1);
                    return parameters;
                }
            };
            VolleySingleton.getInstance().addReqToQueue(stringRequest);
        }
    }

    // Registration with Firebase 
    private void registerWithFB() {
        Log.d(TAG, "registerWithFB: email " + email);
        Log.d(TAG, "registerWithFB:  password" + password);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.INVISIBLE);
                    // If firebase task is successful then call Home activity
                    startActivity(new Intent(evntOrg_signUp2.this, evntOrg_home.class));
                }else{
                    Toast.makeText(evntOrg_signUp2.this, "Network Error", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }


}
