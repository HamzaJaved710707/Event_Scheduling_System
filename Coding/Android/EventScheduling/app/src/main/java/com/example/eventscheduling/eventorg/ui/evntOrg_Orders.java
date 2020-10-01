package com.example.eventscheduling.eventorg.ui;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventscheduling.R;
import com.example.eventscheduling.client.model.client_orders_adapter;
import com.example.eventscheduling.client.ui.client_orders;
import com.example.eventscheduling.client.ui.client_package_detail_custom;
import com.example.eventscheduling.client.ui.client_package_detail_default;
import com.example.eventscheduling.client.util.client_orders_values;
import com.example.eventscheduling.eventorg.model.RecyclerView_Adapter_Order;
import com.example.eventscheduling.eventorg.util.OrderValues;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

// Fragment to display orders to event Organizer
public class evntOrg_Orders extends Fragment implements  RecyclerView_Adapter_Order.OnItemClicked {
    private static final String TAG = "evntOrg_Orders";
    // Array to initialize the values for order
   List<OrderValues> orderList = new ArrayList<>();
    private List<String> userList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference dbReference = firebaseFirestore.collection("Users");
    private CollectionReference orderCollection;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    private String currentUserID;
    private ArrayList<String > packageId = new ArrayList<>();
    private RecyclerView_Adapter_Order order_apapter;
    private DocumentReference packageDocument;
    private ProgressBar progressBar;
    public evntOrg_Orders() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evnt_org__orders, container, false);
        recyclerView = view.findViewById(R.id.order_recyclerView_event);
        progressBar = view.findViewById(R.id.evntOrg_orders_progressBar);
        progressBar.setVisibility(View.VISIBLE);
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUserID = currentUser.getUid();
            orderCollection = dbReference.document(currentUserID).collection("Orders");
            initOrders(view);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: is called");
        initOrders(view);


    }

    public void initOrders(View view) {
        Log.d(TAG, "initOrders: is called");

        // SeT adapter
        orderCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    userList.clear();
                    packageId.clear();;

                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    userList.add(documentSnapshot.getString("from"));
                    packageId.add(documentSnapshot.getString("packageId"));
                }

                for(int i= 1 ; i<= userList.size(); i++){
                    dbReference.whereIn(FieldPath.documentId(), userList).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            orderList.clear();

                            for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                orderList.add(new OrderValues(documentSnapshot.getString("Name"), documentSnapshot.getString("imgUrl"), documentSnapshot.getString("id")));
                            }

                            order_apapter = new RecyclerView_Adapter_Order(view.getContext(), orderList);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                            order_apapter.getValues(packageId);
                            order_apapter.setOnClick(evntOrg_Orders.this::itemClick);
                            recyclerView.setAdapter(order_apapter);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            }





    });
    }

    @Override
    public void itemClick(String id,String packageId) {
         packageDocument = dbReference.document(id).collection("Packages").document(packageId);
        packageDocument.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Boolean custom =  documentSnapshot.getBoolean("custom");
                if(custom){
                    getCustomPackage(packageId, id);
                }
                else{
                    getDefaultPackage(packageId, id);
                }
            }
        });
    }

    private void getCustomPackage(String packageId, String userId) {
        client_package_detail_custom frag = new client_package_detail_custom();
        Bundle bundle = new Bundle();
        bundle.putString("packageId", packageId);
        bundle.putString("userId", userId);
        bundle.putBoolean("orders", true);
        frag.setArguments(bundle);
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_test_id, frag).addToBackStack(null).commit();

    }
    private void getDefaultPackage(String packageId, String userId){

        client_package_detail_default frag = new client_package_detail_default();
        Bundle bundle = new Bundle();
        bundle.putString("packageId", packageId);
        bundle.putString("userId", userId);
        bundle.putBoolean("orders", true);
        frag.setArguments(bundle);
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_test_id, frag).addToBackStack(null).commit();

    }


}
