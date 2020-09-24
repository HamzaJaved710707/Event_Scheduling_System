package com.example.eventscheduling.client.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventscheduling.R;
import com.example.eventscheduling.client.model.client_friendList_Adapter;
import com.example.eventscheduling.client.model.client_orders_adapter;
import com.example.eventscheduling.client.util.client_friendList_values;
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
    private List<String> order_user_list = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_orders, container, false);
        currentUser = mAuth.getCurrentUser();
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
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    order_user_list.add(documentSnapshot.getString("to"));
                }
                for(int i= 0 ; i<= order_user_list.size(); i++){
                    dbReference.whereIn(FieldPath.documentId(), order_user_list).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            order_list.clear();

                            for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                order_list.add(new client_orders_values(documentSnapshot.getString("Name"), documentSnapshot.getString("imgUrl"), documentSnapshot.getString("id")));
                            }

                            orders_adapter = new client_orders_adapter(view.getContext(), order_list);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                            orders_adapter.setOnClick(client_orders.this::itemClick);
                            recyclerView.setAdapter(orders_adapter);
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
    public void itemClick(String id) {
        Toast.makeText(getContext(), "Item is clicked", Toast.LENGTH_SHORT).show();
    }
}

