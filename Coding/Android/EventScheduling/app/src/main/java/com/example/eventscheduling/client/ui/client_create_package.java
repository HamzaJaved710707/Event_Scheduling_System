package com.example.eventscheduling.client.ui;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventscheduling.R;
import com.example.eventscheduling.client.model.create_food_item_package_adapter;
import com.example.eventscheduling.client.model.create_services_package_adapter;
import com.example.eventscheduling.client.model.create_venue_package_adapter;
import com.example.eventscheduling.client.util.create_food_item_values;
import com.example.eventscheduling.client.util.create_service_package_values;
import com.example.eventscheduling.client.util.create_venue_package_values;
import com.example.eventscheduling.eventorg.model.DragData;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;


public class client_create_package extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    RecyclerView food_item_recyclerView;
    RecyclerView services_item_recyclerView;
    RecyclerView venue_item_recyclerView;
    String id;
    //Firebase Variables needed
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser;
    CollectionReference food_package_ref;
    CollectionReference service_package_ref;
    CollectionReference venue_package_ref;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat sdformat;
    private WeakReference<client_create_package> owner;
    // Firebase Collection item
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference food_items_resources = firebaseFirestore.collection("resources").document("1").collection("food_items");
    private CollectionReference service_item_resources = firebaseFirestore.collection("resources").document("1").collection("service_list");
    private CollectionReference venue_item_resources = firebaseFirestore.collection("resources").document("1").collection("venue_list");
    // Firebase Storage Reference
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference food_item_storage;
    private StorageReference service_item_storage;
    private StorageReference venue_item_storage;
    //Adapters for items
    private create_food_item_package_adapter food_item_adapter;
    private create_services_package_adapter services_item_adpater;
    private create_venue_package_adapter venue_item_adapter;
    private FloatingActionButton cartBtn;
    private FloatingActionButton done_btn;
    private List<String> food_list = new ArrayList<>();
    private List<String> service_list = new ArrayList<>();
    private List<String> venue_list = new ArrayList<>();
    private boolean venue_counter;
    private ProgressDialog progressDialog;
    private WriteBatch food_batch;
    private WriteBatch service_batch;
    private WriteBatch venue_batch;
    private String random;
    private MaterialButton data_picker_btn;
    private DocumentReference packageRef;
    private Calendar myCalendar ;
    private ProgressBar progressBar;
 // Date checker boolean
    private Boolean dateChecker = false;

    public client_create_package() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_create_package, container, false);
        dateChecker = false;
        currentUser = mAuth.getCurrentUser();
        //Clear write batch
        food_batch = firebaseFirestore.batch();
        service_batch = firebaseFirestore.batch();
        venue_batch = firebaseFirestore.batch();
        progressBar = view.findViewById(R.id.client_create_package_progressBar);
        progressBar.setVisibility(View.VISIBLE);
        // Progress Dialog
        progressDialog = new ProgressDialog(getContext());
        // counter to check user can only add one venue item
        venue_counter = false;
        // User authentication
        if (currentUser != null) {
            id = currentUser.getUid();
            random = String.valueOf(System.currentTimeMillis());
            packageRef = firebaseFirestore.collection("Users").document(id).collection("Packages").document(random);
            food_package_ref = firebaseFirestore.collection("Users").document(id).collection("Packages").document("0").collection("0").document(random).collection("food");
            service_package_ref = firebaseFirestore.collection("Users").document(id).collection("Packages").document("0").collection("0").document(random).collection("service");
            venue_package_ref = firebaseFirestore.collection("Users").document(id).collection("Packages").document("0").collection("0").document(random).collection("venue");
        }

        // Initialize recyclerview
        food_item_recyclerView = view.findViewById(R.id.client_package_recyc_food_items);
        services_item_recyclerView = view.findViewById(R.id.client_package_recyc_services);
        data_picker_btn = view.findViewById(R.id.client_create_package_dataPicker_btn);
        myCalendar = Calendar.getInstance();
        data_picker_btn.setOnClickListener(this);
        venue_item_recyclerView = view.findViewById(R.id.client_package_recyc_venue);
        food_item_storage = firebaseStorage.getReference();
        // Data picker dialog
        datePickerDialog = new DatePickerDialog(view.getContext(), client_create_package.this, 2020, 9, 20);
        // Done button
        done_btn = view.findViewById(R.id.flt_btn_package_done);
        done_btn.setOnClickListener(this);


// CArt button in which dragged items are dropped
        cartBtn = view.findViewById(R.id.flt_btn_package_cart);
        cartBtn.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.d(TAG, "onDrag: entered");
                        cartBtn.setBackgroundTintList(ColorStateList.valueOf(GREEN));
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        Log.d(TAG, "onDrag: exited");
                        cartBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#03DAC6")));

                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        Log.d(TAG, "onDrag: ended");
                        cartBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#03DAC6")));

                        break;
                    case DragEvent.ACTION_DROP:
                        cartBtn.setBackgroundTintList(ColorStateList.valueOf(GREEN));
                        done_btn.setVisibility(View.VISIBLE);
                        done_btn.setBackgroundTintList(ColorStateList.valueOf(RED));
                        Log.d(TAG, "onDrag: drop");
                        final float dropX = event.getX();
                        final float dropY = event.getY();
                        final DragData state = (DragData) event.getLocalState();

                        Log.d(TAG, "onDrag: " + v);
                        Log.d(TAG, "onDrag: " + state.getName());
                        Log.d(TAG, "onDrag: " + state.getUrl());
                        Log.d(TAG, "onDrag: " + v.getParent());
                        switch (state.getVal()) {

                            case 0:
                                food_list.add(state.getName());
                                break;
                            case 1:
                                service_list.add(state.getName());

                                break;
                            case 2:
                                if (!venue_counter) {
                                    venue_list.add(state.getName());
                                    venue_counter = true;
                                } else {
                                    Toast.makeText(getContext(), "You can only add One Venue in Package", Toast.LENGTH_SHORT).show();
                                }

                                break;
                            default:
                                break;

                        }
/*
                        final ImageView shape = (ImageView) LayoutInflater.from(this).inflate(
                                R.layout.view_shape, dropContainer, false);*/
                   /*     shape.setImageResource(state.item.getImageDrawable());
                        shape.setX(dropX - (float) state.width / 2);
                        shape.setY(dropY - (float) state.height / 2);
                        shape.getLayoutParams().width = state.width;
                        shape.getLayoutParams().height = state.height;
                        dropContainer.addView(shape);*/
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        init_food_RecyclerView();
        init_service_RecyclerView();
        init_venue_RecyclerView();
        return view;
    }

    private void init_food_RecyclerView() {
        Log.d(TAG, "initializing food recycler view");
        Query query = food_items_resources;
        FirestoreRecyclerOptions<create_food_item_values> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<create_food_item_values>()
                .setQuery(query, create_food_item_values.class).build();
        food_item_adapter = new create_food_item_package_adapter(getContext(), firestoreRecyclerOptions);
        food_item_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        food_item_recyclerView.setAdapter(food_item_adapter);


    }

    private void init_service_RecyclerView() {
        Log.d(TAG, "initializing service recycler view");
        Query query = service_item_resources;
        FirestoreRecyclerOptions<create_service_package_values> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<create_service_package_values>()
                .setQuery(query, create_service_package_values.class).build();
        services_item_adpater = new create_services_package_adapter(getContext(), firestoreRecyclerOptions);
        services_item_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        services_item_recyclerView.setAdapter(services_item_adpater);
    }

    private void init_venue_RecyclerView() {
        Log.d(TAG, "initializing venue recycler view");
        Query query = venue_item_resources;
        FirestoreRecyclerOptions<create_venue_package_values> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<create_venue_package_values>()
                .setQuery(query, create_venue_package_values.class).build();
        venue_item_adapter = new create_venue_package_adapter(getContext(), firestoreRecyclerOptions);
        venue_item_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        venue_item_recyclerView.setAdapter(venue_item_adapter);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStart() {

        super.onStart();
        food_item_adapter.startListening();
        services_item_adpater.startListening();
        venue_item_adapter.startListening();
        dateChecker = false;

    }

    @Override
    public void onStop() {
        super.onStop();
        food_item_adapter.stopListening();
        services_item_adpater.stopListening();
        venue_item_adapter.stopListening();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            // Add Done button Functionality
            case R.id.flt_btn_package_done:
                if(dateChecker){
                    doneButtonFunctionality();
                }
                else{
                    Toast.makeText(getContext(), "Please select date of event", Toast.LENGTH_SHORT).show();
                }

                break;
            // Add data picker button functionality
            case R.id.client_create_package_dataPicker_btn:
                datePickerDialog.show();
                break;
            default:
                break;
        }
    }

    private void doneButtonFunctionality() {
        Map data = new HashMap();
        progressDialog.show();
        if (food_list.isEmpty() && service_list.isEmpty() && venue_list.isEmpty()) {
            Toast.makeText(getContext(), "No item is added to cart yet", Toast.LENGTH_SHORT).show();
        } else if (!food_list.isEmpty() && !service_list.isEmpty() && venue_list.isEmpty()) {
            data.clear();
            data.put("date", sdformat.format(myCalendar.getTime()));
            data.put("packageId", random);
            data.put("custom", true);
            data.put("Food", food_list);
            packageRef.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressDialog.dismiss();
                    Bundle bundle = new Bundle();
                    bundle.putString("packageId", random);
                    bundle.putString("date",sdformat.format(myCalendar.getTime()));
                    bundle.putBoolean("custom", true);
                    //set Fragmentclass Arguments
                    client_package_detail_custom frag = new client_package_detail_custom();
                    frag.setArguments(bundle);
                    getParentFragmentManager().beginTransaction().replace(R.id.frameLayout_clientHome, frag)
                            .addToBackStack(null).commit();
                }
            });

        } else if (food_list.isEmpty() && !service_list.isEmpty() && venue_list.isEmpty()) {
            // Here implement service list
                data.clear();
            data.put("date", sdformat.format(myCalendar.getTime()));
            data.put("packageId", random);
            data.put("custom", true);
                data.put("Services", service_list);
                packageRef.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Bundle bundle = new Bundle();
                        bundle.putString("packageId", random);
                        bundle.putString("date",sdformat.format(myCalendar.getTime()));
                        bundle.putBoolean("custom", true);
                        //set Fragmentclass Arguments
                        client_package_detail_custom frag = new client_package_detail_custom();
                        frag.setArguments(bundle);
                        getParentFragmentManager().beginTransaction().replace(R.id.frameLayout_clientHome, frag)
                                .addToBackStack(null).commit();
                    }
                });


        } else if (food_list.isEmpty() && service_list.isEmpty() && !venue_list.isEmpty()) {
            data.clear();
            data.put("Venue", venue_list);
            data.put("date", sdformat.format(myCalendar.getTime()));
            data.put("packageId", random);
            data.put("custom", true);
            packageRef.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressDialog.dismiss();
                    Bundle bundle = new Bundle();
                    bundle.putString("packageId", random);
                    bundle.putString("date",sdformat.format(myCalendar.getTime()));
                    bundle.putBoolean("custom", true);
                    //set Fragmentclass Arguments
                    client_package_detail_custom frag = new client_package_detail_custom();
                    frag.setArguments(bundle);
                    getParentFragmentManager().beginTransaction().replace(R.id.frameLayout_clientHome, frag)
                            .addToBackStack(null).commit();
                }
            });

            // implement venue list
        } else {
            data.clear();
            data.put("date", sdformat.format(myCalendar.getTime()));
            data.put("packageId", random);
            data.put("custom", true);

        data.put("Food", food_list);
        data.put("Services", service_list);
            data.put("Venue", venue_list);

        packageRef.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();
                Bundle bundle = new Bundle();
                bundle.putString("packageId", random);
                bundle.putString("date",sdformat.format(myCalendar.getTime()));
                bundle.putBoolean("custom", true);                //set Fragmentclass Arguments
                client_package_detail_custom frag = new client_package_detail_custom();
                frag.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.frameLayout_clientHome, frag)
                        .addToBackStack(null).commit();
            }
        });
        }



    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        String myFormat = "MMM dd, yyyy"; //In which you need put here
        sdformat = new SimpleDateFormat(myFormat, Locale.US);
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        Log.d(TAG, "onDateSet: " + sdformat.format(myCalendar.getTime()));
        dateChecker = true;
    }
}
