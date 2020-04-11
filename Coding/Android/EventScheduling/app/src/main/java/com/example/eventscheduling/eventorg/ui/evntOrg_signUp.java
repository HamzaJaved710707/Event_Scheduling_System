package com.example.eventscheduling.eventorg.ui;

import android.app.AlertDialog;
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
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class evntOrg_signUp extends AppCompatActivity {
    private static final String TAG = "evntOrg_signUp";
    private static String url = "https://eventscheduling.000webhostapp.com/android/eventOrganizer/evntOrg_reg_1.php";
    private  EditText nameText;
   private EditText email;
   private EditText mobileNumber;
  private   EditText password;
   private AlertDialog.Builder alertDialog;
   private ProgressBar progressBar;
   private String emailTxt;
   private String passwordTxt;
   // Firebase Authentication

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
        // Initialize Firebase Auth
        //ProgressBar
        progressBar = findViewById(R.id.progBar_evnt_signUp1);
        //Alert Dialog
        alertDialog = new AlertDialog.Builder(evntOrg_signUp.this);
        //To disable keyboard to automatically popup
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
        Log.d(TAG, "processing:  is called ");
        if (!progressBar.isShown()) {
            progressBar.setVisibility(View.VISIBLE);
            // TO disable the UI interaction while progress bar is shown on the screen
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        final String name = nameText.getText().toString();
       emailTxt = email.getText().toString();
        final String mobileNo = mobileNumber.getText().toString();
        passwordTxt = password.getText().toString();
        if (name.matches("") && emailTxt.matches("") && mobileNo.matches("") && passwordTxt.matches("")) {
            // Undefined error occurred
            if(progressBar.isShown()){ progressBar.setVisibility(View.INVISIBLE);}
            alertDialog.setTitle("Undefined Error");
            alertDialog.setMessage("Enter your data first");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();

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
                                String table_id = jsonObject.getString("id");
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
                                    Intent intent = new Intent(evntOrg_signUp.this, evntOrg_signUp2.class);
                                    intent.putExtra("id",table_id);
                                    intent.putExtra("email", emailTxt);
                                    intent.putExtra("password", passwordTxt);
                                    Log.d(TAG, "onResponse: "+ table_id + emailTxt + passwordTxt+ " is going to send");
                                    startActivity(intent);
                                } else if (result.matches("2")) {
                                    // User is already registered with this email
                                    Log.d(TAG, "onResponse: is 2");
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
                                Log.d(TAG, "JSON exception in String response: " + e.getMessage());
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "onErrorResponse: " + error.getMessage());
                    VolleyLog.d(error.toString());
                    if(progressBar.isShown()){
                        progressBar.setVisibility(View.INVISIBLE);
                        // Undefined error occurred
                        alertDialog.setTitle("Undefined Error");
                        alertDialog.setMessage("Some network error");
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        alertDialog.show();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    // Get values from user and send them to server for registration
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("name", name);
                    parameters.put("email", emailTxt);
                    parameters.put("mobileNumber", mobileNo);
                    parameters.put("password", passwordTxt);
                    return parameters;
                }
            };
            VolleySingleton.getInstance().addReqToQueue(stringRequest);
        }
    }
}
