package com.example.eventscheduling.client.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventscheduling.R;
import com.example.eventscheduling.client.model.client_package_detail_adapter;
import com.example.eventscheduling.client.util.client_package_detail_values;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class client_package_detail_custom extends Fragment {
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
    private client_package_detail_values package_value_object;
    private List<String> tempList = new ArrayList<>();
    private RecyclerView food_recyclerView;
    private RecyclerView service_recyclerView;
    private RecyclerView venue_recyclerview;
    private String packageId;
    private CollectionReference currentUser_order;
    private CollectionReference receivingUser_order;
    private MaterialButton sendButton;
    private Boolean custom;
    private DocumentReference defaultPackage;
    private String date;
    private TextView dateTExtview;
    private Boolean checkFrag= false;
    private MaterialButton cancelBtn;
    private String packageUser;
    private MaterialTextView empty_foodTxtview;
private MaterialTextView empty_serviceTxtView;
private MaterialTextView empty_venueTxtView;
private String orderId;
private boolean evnt = false;
private String from_id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_package_detail_custom, container, false);


            packageId = getArguments().getString("packageId");
            custom = getArguments().getBoolean("custom");
            checkFrag = getArguments().getBoolean("orders");
            evnt =getArguments().getBoolean("envt");
            packageUser = getArguments().getString("userId");
            date =getArguments().getString("date");
            orderId = getArguments().getString("orderId");
            food_recyclerView = view.findViewById(R.id.client_package_detail_food_recyc);
            service_recyclerView = view.findViewById(R.id.client_package_detail_service_recyc);
            venue_recyclerview = view.findViewById(R.id.client_package_detail_venue_recyc);
            cancelBtn = view.findViewById(R.id.client_package_detail_cancel_btn);
            dateTExtview = view.findViewById(R.id.date_txt_detail);
            empty_foodTxtview = view.findViewById(R.id.client_package_detail_custom_food_empty);
            empty_serviceTxtView = view.findViewById(R.id.client_package_detail_custom_service_empty);
            empty_venueTxtView = view.findViewById(R.id.client_package_detail_custom_venue_empty);

            setHasOptionsMenu(true);
            currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {


                    currentUserID = currentUser.getUid();
                    if(packageUser == null){
                        customCollection = dbReference.document(currentUserID).collection("Packages").document(packageId);
                    }
                    else{
                        customCollection  = dbReference.document(packageUser).collection("Packages").document(packageId);
                    }
                    initialize_RecyclerView();






            }
            sendButton = view.findViewById(R.id.client_package_detail_send_btn);


        if(checkFrag && evnt) {
            sendButton.setText("Accept");
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebaseFirestore.collection("Users").document(packageUser).collection("Orders").document(orderId).update("status", 1)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(), "Order Accepted", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Error while accepting order", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            cancelBtn.setVisibility(View.GONE);
        }
        else if(checkFrag){
    sendButton.setVisibility(View.GONE);
    cancelBtn.setVisibility(View.VISIBLE);
cancelBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        dbReference.document(currentUserID).collection("Orders").document(orderId).delete();
   //     dbReference.document(packageUser).collection("Orders").document(packageId).delete();
        getParentFragmentManager().beginTransaction().replace(R.id.frameLayout_clientHome, new client_orders()).addToBackStack(null).commit();
    }
});
        }
        else{
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("packageId", packageId);
                    bundle.putString("date", date);
                    client_package_send_order frag = new client_package_send_order();
                    frag.setArguments(bundle);
                    getParentFragmentManager().beginTransaction().replace(R.id.frameLayout_clientHome, frag)
                            .addToBackStack(null).commit();
                }
            });
        }
        return view;
    }


    private void initialize_RecyclerView() {
        customCollection.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                foodList = (ArrayList<String>)documentSnapshot.get("Food");
                serviceList = (ArrayList<String>) documentSnapshot.get("Services");
                venueList = (ArrayList<String>) documentSnapshot.get("venue");
                date = documentSnapshot.getString("date");
             if(foodList == null){
                 empty_foodTxtview.setVisibility(View.VISIBLE);
             }
              if(serviceList == null){
                  empty_serviceTxtView.setVisibility(View.VISIBLE);
              }
              if(venueList == null){
                  empty_venueTxtView.setVisibility(View.VISIBLE);
              }
                food_detail_adapter = new client_package_detail_adapter(getContext(), foodList);
                service_detail_adapter = new client_package_detail_adapter(getContext(), serviceList);
                venue_detail_adapter = new client_package_detail_adapter(getContext(), venueList);
                food_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                food_recyclerView.setAdapter(food_detail_adapter);
                service_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                service_recyclerView.setAdapter(service_detail_adapter);
                venue_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                venue_recyclerview.setAdapter(venue_detail_adapter);
                dateTExtview.setText(date);
            }
        });

    }
}