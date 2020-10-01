package com.example.eventscheduling.client.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eventscheduling.R;
import com.example.eventscheduling.client.model.client_package_detail_adapter;
import com.example.eventscheduling.client.util.client_package_detail_values;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;


public class client_package_detail_default extends Fragment implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "package_detail";
    client_package_detail_adapter food_detail_adapter;
    client_package_detail_adapter service_detail_adapter;
    client_package_detail_adapter venue_detail_adapter;

    List<String> foodList = new ArrayList<>();
    List<String> serviceList = new ArrayList<>();
    List<String> venueList = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference dbReference = firebaseFirestore.collection("Users");
    private DocumentReference customCollection;


    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    private String currentUserID;
    private String packageUser;
    private RecyclerView food_recyclerView;
    private RecyclerView service_recyclerView;
    private RecyclerView venue_recyclerview;
    private String packageId;

    private MaterialButton sendButton;
    private MaterialButton datePickerBtn;

    private String date;
    private TextView dateTExtview;
    private CircleImageView packageImg;
    private TextView packageName;
    private TextView priceTxt;
    private SimpleDateFormat sdformat;
    private Calendar myCalendar;
private DatePickerDialog datePickerDialog;
    private boolean dateChecker = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_package_detail_default, container, false);


        packageId = getArguments().getString("packageId");
        packageUser = getArguments().getString("userId");
datePickerBtn = view.findViewById(R.id.datePicker_client_package_detail);
        myCalendar = Calendar.getInstance();

        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(view.getContext(), client_package_detail_default.this, 2020, 9, 20);
                datePickerDialog.show();

            }
        });
        food_recyclerView = view.findViewById(R.id.client_package_detail_default_food_recyc);
        service_recyclerView = view.findViewById(R.id.client_package_detail_default_service_recyc);
        venue_recyclerview = view.findViewById(R.id.client_package_detail_default_venue_recyc);
        dateTExtview = view.findViewById(R.id.date_txt_default_detail);
        packageImg = view.findViewById(R.id.client_packageDetail_default_img);
        packageName = view.findViewById(R.id.client_packagedetail_default_name);
        priceTxt = view.findViewById(R.id.price_txt_detail);
        setHasOptionsMenu(true);
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserID = currentUser.getUid();

            customCollection = dbReference.document(packageUser).collection("Packages").document(packageId);
            initialize_RecyclerView();


        }
        sendButton = view.findViewById(R.id.client_package_default_detail_send_btn);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dateChecker) {
                    Bundle bundle = new Bundle();
                    {
                        bundle.putString("packageId", packageId);
                        bundle.putString("date", date);
                        bundle.putBoolean("isDefault", true);
                        client_package_send_order frag = new client_package_send_order();
                        frag.setArguments(bundle);
                        getParentFragmentManager().beginTransaction().replace(R.id.frameLayout_clientHome, frag)
                                .addToBackStack(null).commit();
                    }

                } else {
                    Toast.makeText(getContext(), "Please select date for order", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }







    private void initialize_RecyclerView() {
        customCollection.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                foodList = (ArrayList<String>)documentSnapshot.get("Food");
                serviceList = (ArrayList<String>) documentSnapshot.get("Services");
                venueList = (ArrayList<String>) documentSnapshot.get("Venue");
                Glide.with(getContext()).load(documentSnapshot.getString("image")).into(packageImg);
                packageName.setText(documentSnapshot.getString("PackageName"));
                priceTxt.setText(documentSnapshot.getString("price"));
                food_detail_adapter = new client_package_detail_adapter(getContext(), foodList);
                service_detail_adapter = new client_package_detail_adapter(getContext(), serviceList);
              //  venue_detail_adapter = new client_package_detail_adapter(getContext(), venueList);
                food_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                food_recyclerView.setAdapter(food_detail_adapter);
                service_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                service_recyclerView.setAdapter(service_detail_adapter);
             //   venue_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
             //   venue_recyclerview.setAdapter(venue_detail_adapter);

            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String myFormat = "MMM dd, yyyy"; //In which you need put here
        sdformat = new SimpleDateFormat(myFormat, Locale.US);
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        Log.d(TAG, "onDateSet: " + sdformat.format(myCalendar.getTime()));
        date = sdformat.format(myCalendar.getTime());
        dateChecker = true;
    }
}