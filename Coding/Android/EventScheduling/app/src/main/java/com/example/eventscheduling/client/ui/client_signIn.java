package com.example.eventscheduling.client.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
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

public class client_signIn extends AppCompatActivity {
    private static final String TAG = "client_signIn";
    private static String url = "https://eventscheduling.000webhostapp.com/android/client/logIn_data.php";
    TextView emailTxt;
    TextView passwordTxt;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialize views
        progressDialog = new ProgressDialog(this);
        Button signInBtn = findViewById(R.id.signIn);
        Button registerBtn = findViewById(R.id.registerBtn);
        emailTxt = findViewById(R.id.email_editText);
        passwordTxt = findViewById(R.id.password_editText);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), client_signUp.class);
                startActivity(intent);
            }
        });
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailTxt.getText().toString();
                final String password = passwordTxt.getText().toString();
                Log.d(TAG, "onClick: email is " + email);
                Log.d(TAG, "onClick: password is " + password);
                if (email.matches("") && password.matches("")) {
                    Toast.makeText(client_signIn.this, "Enter your data first", Toast.LENGTH_SHORT).show();

                } else {
                    progressDialog.show();
                    Log.d(TAG, "onClick: in string is called");
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                Log.d(TAG, "onResponse: WE get some response" + response);
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String value = jsonObject.getString("code");
                                Log.d(TAG, "onResponse: " + value);
                                if (value.matches("1")) {
                                    Intent intent = new Intent(getApplicationContext(), client_home.class);
                                    startActivity(intent);
                                } else if (value.matches("0")) {
                                    Toast.makeText(client_signIn.this, "Failure", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(client_signIn.this, "Unexpected Error", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d(TAG, "JSON exception in String response: " + e.getMessage());
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(client_signIn.this, "Response Error", Toast.LENGTH_SHORT).show();
                            VolleyLog.d("Volley Log", error.getMessage());
                        }
                    }
                    ) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("email", email);
                            Log.d(TAG, "getParams: value of email" + email);
                            params.put("password", password);
                            return params;

                        }
                    };
                    VolleySingleton.getInstance().addReqToQueue(stringRequest);
                }
            }


        });
    }

}
