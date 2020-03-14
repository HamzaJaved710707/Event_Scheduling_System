package com.example.eventscheduling.eventorg.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class evntOrg_signUp2 extends AppCompatActivity implements View.OnClickListener {
    private static String url = "https://eventscheduling.000webhostapp.com/android/evntOrg_reg_2.php";
    AlertDialog.Builder alertDialog;
    ProgressBar progressBar;
    private TextView businessName;
    private TextView province;
    private TextView city;
    private TextView area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evnt_org_sign_up2);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //Initialize views
        ImageView mapImage = findViewById(R.id.map_image_id);
        businessName = findViewById(R.id.sign2_edTxt_bussName);
        province = findViewById(R.id.sign2_edTxt_province);
        city = findViewById(R.id.sign2_edTxt_cityName);
        area = findViewById(R.id.sign2_edTxt_area);
        // ALertDialog
        alertDialog = new AlertDialog.Builder(this);
        // ProgressBar
        progressBar = findViewById(R.id.progBar_Evnt_SignUp2);
        // Set click listener
        mapImage.setClickable(true);
        mapImage.setOnClickListener(this);
        Button nextButton = findViewById(R.id.sign2_btn_next);
        nextButton.setOnClickListener(this);
        // Initialize spinner
        Spinner Cat_spinner = findViewById(R.id.spinner);
        ArrayList<String> list = new ArrayList<>();
        list.add("Event Organizer");
        list.add("Venuer");
        list.add("Decoration");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        Cat_spinner.setAdapter(arrayAdapter);
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
        Intent intent = new Intent(this, MapLocation.class);
        startActivity(intent);
    }

    // Do processing
    // Send data to remote server and get response whether operation was successful or not
    private void processing() {
        progressBar.setVisibility(View.VISIBLE);
        final String _businessName = businessName.getText().toString();
        final String _province = province.getText().toString();
        final String _city = city.getText().toString();
        final String _area = area.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                                startActivity(new Intent(evntOrg_signUp2.this, evntOrg_home.class));
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

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("businessName", _businessName);
                parameters.put("province", _province);
                parameters.put("city", _city);
                parameters.put("area", _area);
                return parameters;
            }
        };
        VolleySingleton.getInstance().addReqToQueue(stringRequest);
    }
}
