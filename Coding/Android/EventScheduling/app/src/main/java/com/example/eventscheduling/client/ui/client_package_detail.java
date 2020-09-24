package com.example.eventscheduling.client.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventscheduling.R;
import com.example.eventscheduling.client.model.client_package_detail_adapter;
import com.example.eventscheduling.client.util.client_package_detail_values;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class client_package_detail extends Fragment {
    private static final String TAG = "package_detail";
    client_package_detail_adapter food_detail_adapter;
    client_package_detail_adapter service_detail_adapter;
    client_package_detail_adapter venue_detail_adapter;

    List<client_package_detail_values> foodList = new ArrayList<>();
    List<client_package_detail_values> serviceList = new ArrayList<>();
    List<client_package_detail_values> venueList = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference dbReference = firebaseFirestore.collection("Users");
    private CollectionReference foodCollection;
    private CollectionReference serviceCollection;
    private CollectionReference venueCollection;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    private String currentUserID;
    private client_package_detail_values package_value_object;
    private List<String> tempList = new ArrayList<>();
    private RecyclerView food_recyclerView;
    private RecyclerView service_recyclerView;
    private RecyclerView venue_recyclerview;
    private String packageId;
    private CollectionReference currentUser_order;
    private CollectionReference receivingUser_order;
    private MaterialButton sendButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_package_detail, container, false);

        if (getArguments() != null)
            packageId = getArguments().getString("packageId");


        food_recyclerView = view.findViewById(R.id.client_package_detail_food_recyc);
        service_recyclerView = view.findViewById(R.id.client_package_detail_service_recyc);
        venue_recyclerview = view.findViewById(R.id.client_package_detail_venue_recyc);

        setHasOptionsMenu(true);
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserID = currentUser.getUid();
            foodCollection = dbReference.document(currentUserID).collection("Packages").document("0").collection("0").document(packageId).collection("food");
            serviceCollection = dbReference.document(currentUserID).collection("Packages").document("0").collection("0").document(packageId).collection("service");
            venueCollection = dbReference.document(currentUserID).collection("Packages").document("0").collection("0").document(packageId).collection("venue");

            initialize_RecyclerView();
        }
        sendButton = view.findViewById(R.id.client_package_detail_send_btn);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("packageId", packageId);
                client_package_send_order frag = new client_package_send_order();
                frag.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.frameLayout_clientHome, frag)
                        .addToBackStack(null).commit();
            }
        });
        return view;
    }

    private void initialize_RecyclerView() {
        foodCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    foodList.add(new client_package_detail_values(documentSnapshot.getString("name")));
                }
                serviceCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            serviceList.add(new client_package_detail_values(documentSnapshot.getString("name")));
                        }
                        venueCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    venueList.add(new client_package_detail_values(documentSnapshot.getString("name")));
                                }
                                // Foood item recylcerview
                                food_detail_adapter = new client_package_detail_adapter(getContext(), foodList);
                                food_recyclerView.setHasFixedSize(true);
                                food_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                //food_detail_adapter.setOnClick(client_package_send_order.this);
                                food_recyclerView.setAdapter(food_detail_adapter);
                                // Service item recyclerveiw
                                service_detail_adapter = new client_package_detail_adapter(getContext(), serviceList);
                                service_recyclerView.setHasFixedSize(true);
                                service_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                //service_detail_adapter.setOnClick(client_package_send_order.this);
                                service_recyclerView.setAdapter(service_detail_adapter);

                                // Venue item recyclerveiw
                                venue_detail_adapter = new client_package_detail_adapter(getContext(), venueList);
                                venue_recyclerview.setHasFixedSize(true);
                                venue_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                                //service_detail_adapter.setOnClick(client_package_send_order.this);
                                venue_recyclerview.setAdapter(venue_detail_adapter);
                            }
                        });
                    }
                });
            }
        });


    }
}