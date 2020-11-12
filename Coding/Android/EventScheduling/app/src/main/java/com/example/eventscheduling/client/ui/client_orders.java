package com.example.eventscheduling.client.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventscheduling.R;
import com.example.eventscheduling.client.model.client_friendList_Adapter;
import com.example.eventscheduling.client.model.client_orders_adapter;
import com.example.eventscheduling.client.util.client_friendList_values;
import com.example.eventscheduling.client.util.client_orders_data;
import com.example.eventscheduling.client.util.client_orders_values;
import com.example.eventscheduling.eventorg.util.friendList_values;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class client_orders extends Fragment implements client_orders_adapter.OnItemClicked  {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference dbReference = firebaseFirestore.collection("Users");
    private CollectionReference orderCollection;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    private String currentUserID;
    private client_orders_values order_value_class = new client_orders_values();
    private List<client_orders_values> order_list = new ArrayList<>();
    private RecyclerView recyclerView;
    private client_orders_adapter orders_adapter;
    private ArrayList<String> order_user_list = new ArrayList<>();
    private ArrayList<String> packageList = new ArrayList<>();
    private ProgressBar progressBar;
    private List<client_orders_data> client_orders_data = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onStart() {
        super.onStart();
        ((client_home)getActivity()).selectTitleOfActionBar("Orders");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_orders, container, false);
        currentUser = mAuth.getCurrentUser();
        progressBar = view.findViewById(R.id.client_order_progressBar);
        progressBar.setVisibility(View.VISIBLE);

        if(currentUser != null){
            currentUserID = currentUser.getUid();
            orderCollection = dbReference.document(currentUserID).collection("Orders");
            recyclerView = view.findViewById(R.id.client_orders_recycler_layout);
            initialize_RecyclerView(view);
        }
        return view;
    }

    private void initialize_RecyclerView( View view) {


        orderCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                order_user_list.clear();
                order_list.clear();
                client_orders_data.clear();
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    if(documentSnapshot.getLong("status") == 0){
                        order_user_list.add(documentSnapshot.getString("to"));
                        client_orders_data.add(new client_orders_data(documentSnapshot.getString("packageUser"), documentSnapshot.getString("packageId"), documentSnapshot.getId(), documentSnapshot.getString("from")));
                    }


                }
            
                if(order_user_list.size() == 0){
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "No Orders to display", Toast.LENGTH_SHORT).show();
                }
                for(int i= 0 ; i< order_user_list.size(); i++){
                    String value = order_user_list.get(i);
                    dbReference.whereIn(FieldPath.documentId(), Collections.singletonList(value)).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                order_list.add(new client_orders_values(documentSnapshot.getString("Name"), documentSnapshot.getString("imgUrl"),"", "", "", documentSnapshot.getId()));
                            }

                            orders_adapter = new client_orders_adapter(view.getContext(), order_list);
                            recyclerView.setHasFixedSize(true);
                            orders_adapter.setpackageList(client_orders_data);
                            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                            orders_adapter.setOnClick(client_orders.this::itemClick);
                            recyclerView.setAdapter(orders_adapter);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    @Override
    public void itemClick(String Packageid, String userId, String orderId) {
      Bundle bundle = new Bundle();
      bundle.putString("packageId", Packageid);
      bundle.putBoolean("orders", true);
      bundle.putString("userId",userId);
      bundle.putString("orderId", orderId);
        client_package_detail_custom frag = new client_package_detail_custom();
      frag.setArguments(bundle);
      getParentFragmentManager().beginTransaction().replace(R.id.frameLayout_clientHome, frag).addToBackStack(null).commit();


    }
}

