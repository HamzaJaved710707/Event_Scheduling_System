package com.example.eventscheduling.client.ui;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventscheduling.R;
import com.example.eventscheduling.client.model.Filter_Friend_Dialog;
import com.example.eventscheduling.client.model.client_send_package_adapter;
import com.example.eventscheduling.client.util.client_friendList_values;
import com.example.eventscheduling.eventorg.util.friendList_values;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class client_package_send_order extends Fragment implements client_send_package_adapter.OnItemClicked, Filter_Friend_Dialog.ExampleDialogListener {
    private static final String TAG = "package_send_order";
    private client_send_package_adapter friendList_adapter;
    List<client_friendList_values> friendLists = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference dbReference = firebaseFirestore.collection("Users");
    private CollectionReference friendCollection;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    private String currentUserID;
    private friendList_values class_values = new friendList_values();
    private List<String> tempList = new ArrayList<>();
    private RecyclerView recyclerView;
    private String packageId;
    private CollectionReference currentUser_order;
    private CollectionReference receivingUser_order;
    private String date;
    private Boolean isDefaultPackage = false;
    private String packageUser;
    private ProgressBar progressBar;

    public client_package_send_order() {
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
        View view = inflater.inflate(R.layout.fragment_client_package_send_order, container, false);
        // Get package id


            packageId = getArguments().getString("packageId");
            date = getArguments().getString("date");
            isDefaultPackage = getArguments().getBoolean("isDefault");
            packageUser = getArguments().getString("packageUser");
progressBar = view.findViewById(R.id.client_package_send_progBar);
        recyclerView = view.findViewById(R.id.client_package_send_order_recylerview);
        setHasOptionsMenu(true);
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserID = currentUser.getUid();
            friendCollection = dbReference.document(currentUserID).collection("Friends");
            currentUser_order = dbReference.document(currentUserID).collection("Orders");
            initialize_RecyclerView();
            progressBar.setVisibility(View.VISIBLE);
        }
        return view;
    }

    // Overridden method of interface used in adapter
    // Execute query to store and send order to the event organizer selected by the user
    @Override
    public void onItemClick(String id) {
        Map order_data = new HashMap();
        order_data.put("from", currentUserID);
        order_data.put("to", id);
        order_data.put("status", 0);
        order_data.put("packageId", packageId);
        if(packageUser == null){
            order_data.put("packageUser", currentUserID);
        }else{
            order_data.put("packageUser", packageUser);
        }

        if(isDefaultPackage){
            order_data.put("custom", false);
        }else {
            order_data.put("custom", true);
        }
        order_data.put("date", date);
        currentUser_order.add(order_data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                receivingUser_order = firebaseFirestore.collection("Users");
                receivingUser_order.document(id).collection("Orders").document().set(order_data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Your order send successfully", Toast.LENGTH_SHORT).show();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getParentFragmentManager().beginTransaction().replace(R.id.frameLayout_clientHome, new client_packages_frag()).commit();
                                    }
                                },1500);
                                getParentFragmentManager().beginTransaction().replace(R.id.frameLayout_clientHome, new client_packages_frag()).commit();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error!!!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear(); // clears all menu items..
        inflater.inflate(R.menu.filter_menu_actionbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
        // This change the text color of overflow menu
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // This switch statement will work with Overflow menu...
        if (item.getItemId() == R.id.filter_menu) {
            openDialog();
        }
        return super.onOptionsItemSelected(item);

    }

    // Open Dialog method is used to show dialog on the screen
    // From which user can apply filter to the recyclerview
    public void openDialog() {

        Filter_Friend_Dialog filter_dialog = new Filter_Friend_Dialog(client_package_send_order.this);
        filter_dialog.show(getParentFragmentManager(), "example dialog");
        filter_dialog.setExampleDialog(client_package_send_order.this);
    }

    // Recyclerview to show the list of friends of this user
    private void initialize_RecyclerView() {
        // This will fetch all the ids of friends in Friend collection
        friendCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    tempList.add(documentSnapshot.getId());
                }
                if ((tempList != null) && (tempList.size() != 0)) {
                    dbReference.whereIn(FieldPath.documentId(), tempList).whereEqualTo("type", 0).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                friendLists.clear();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    client_friendList_values object = new client_friendList_values(document.getString("Name"), document.getString("imgUrl"), document.getString("id"), document.getLong("type"));
                                    friendLists.add(object);
                                }
                                friendList_adapter = new client_send_package_adapter(getContext(), friendLists);

                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                friendList_adapter.setOnClick(client_package_send_order.this);
                                recyclerView.setAdapter(friendList_adapter);

progressBar.setVisibility(View.INVISIBLE);
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    public void applyTexts(CheckedTextView all, CheckedTextView friends) {
        if (all.isChecked()) {
            initialize_EventOrg_RecyclerView();
            Log.d(TAG, "applyTexts: all is checked");
        } else if (friends.isChecked()) {
            // initialize_Friend_RecyclerView();
            initialize_Friends_Recyclerview();
            Log.d(TAG, "applyTexts: friends is checked");
        } else {
            Log.d(TAG, "applyTexts: both");
            initialize_EventOrg_RecyclerView();
        }
    }

    private void initialize_Friends_Recyclerview() {

    }

    private void initialize_EventOrg_RecyclerView() {
        progressBar.setVisibility(View.VISIBLE);
        dbReference.whereEqualTo("type", 0).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                friendLists.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    client_friendList_values object = new client_friendList_values(documentSnapshot.getString("Name"), documentSnapshot.getString("imgUrl"), documentSnapshot.getString("id"), documentSnapshot.getLong("type"));
                    friendLists.add(object);
                }
                friendList_adapter = new client_send_package_adapter(getContext(), friendLists);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                friendList_adapter.setOnClick(client_package_send_order.this);

                recyclerView.swapAdapter(friendList_adapter, true);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.d(TAG, e.getMessage());

            }
        });
    }
}