package com.example.eventscheduling.client.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventscheduling.R;
import com.example.eventscheduling.client.model.Filter_Friend_Dialog;
import com.example.eventscheduling.client.model.client_friendList_Adapter;
import com.example.eventscheduling.client.util.client_friendList_values;
import com.example.eventscheduling.eventorg.util.friendList_values;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class client_friendList extends Fragment implements client_friendList_Adapter.OnItemClicked, Filter_Friend_Dialog.ExampleDialogListener {
    private static final String TAG = "client_friendList";
    client_friendList_Adapter friendList_adapter;
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
    private ProgressBar progressBar;
private String backStackString;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_client_friend_list, container, false);
        recyclerView = view.findViewById(R.id.client_friend_list_recyclerview);
        progressBar = view.findViewById(R.id.client_friendList_progressBar);
        progressBar.setVisibility(View.VISIBLE);
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserID = currentUser.getUid();
            friendCollection = dbReference.document(currentUserID).collection("Friends");
            initialize_RecyclerView(view);

        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((client_home)getActivity()).selectTitleOfActionBar("Friends");

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.filter_menu_actionbar, menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // This switch statement will work with Overflow menu...
        if (item.getItemId() == R.id.filter_menu) {
            openDialog();
        }
        return super.onOptionsItemSelected(item);

    }

    private void initialize_RecyclerView(View view) {
        // This will fetch all the ids of friends in Friend collection
        friendCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    tempList.add(documentSnapshot.getString("id"));
                }
                dbReference.whereIn(FieldPath.documentId(), tempList).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            friendLists.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                client_friendList_values object = new client_friendList_values(document.getString("Name"), document.getString("imgUrl"), document.getString("id"), document.getLong("type"));
                                friendLists.add(object);
                            }
                            friendList_adapter = new client_friendList_Adapter(getContext(), friendLists);

                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            friendList_adapter.setOnClick(client_friendList.this);
                            recyclerView.setAdapter(friendList_adapter);
                            progressBar.setVisibility(View.INVISIBLE);


                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
            }
        });

    }

    private void initialize_Friends_Recyclerview() {
        friendCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    tempList.add(documentSnapshot.getString("id"));
                }
                dbReference.whereIn(FieldPath.documentId(), tempList).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            friendLists.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                client_friendList_values object = new client_friendList_values(document.getString("Name"), document.getString("imgUrl"), document.getString("id"), document.getLong("type"));
                                friendLists.add(object);
                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            friendList_adapter.setOnClick(client_friendList.this);

                            recyclerView.swapAdapter(friendList_adapter, true);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
            }
        });
    }

    private void initialize_All_RecyclerView() {

        dbReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                friendLists.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    client_friendList_values object = new client_friendList_values(documentSnapshot.getString("Name"), documentSnapshot.getString("imgUrl"), documentSnapshot.getString("id"), documentSnapshot.getLong("type"));
                    friendLists.add(object);
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                friendList_adapter.setOnClick(client_friendList.this);

                recyclerView.swapAdapter(friendList_adapter, true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }


    @Override
    public void onMsgIconClick(int position, String id) {

        Intent intent = new Intent(getContext(), client_msgDetail.class);
        intent.putExtra("chatId", id);
        startActivity(intent);
    }

    @Override
    public void onFriendIconClick(int position, String id) {
        friendCollection.document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Log.d(TAG, "onSuccess: document exist");
                            Toast.makeText(getContext(), "You are alreading following", Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d(TAG, "onSuccess: does not exists");
                            Map value = new HashMap();
                            value.put("id", id);
                            friendCollection.document().set(value).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(), "Added to friend List", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.getMessage());
            }
        });

    }

    @Override
    public void onLayoutClick(String id, long type) {
        if(type == 1){
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            client_profile_view profile_view = new client_profile_view();
            profile_view.setArguments(bundle);
            getParentFragmentManager().beginTransaction().replace(R.id.frameLayout_clientHome, profile_view).addToBackStack(backStackString).commit();
        }
        else if(type == 0){
            // Show profile of client
        }

    }

    @Override
    public void applyTexts(CheckedTextView all, CheckedTextView friends) {
        Log.d(TAG, "applyTexts: ");
        if (all.isChecked()) {
            initialize_All_RecyclerView();
            Log.d(TAG, "applyTexts: all is checked");
        } else if (friends.isChecked()) {
            // initialize_Friend_RecyclerView();
            initialize_Friends_Recyclerview();
            Log.d(TAG, "applyTexts: friends is checked");
        } else {
            Log.d(TAG, "applyTexts: both");
            initialize_All_RecyclerView();
        }
    }

    public void openDialog() {
        Filter_Friend_Dialog filter_dialog = new Filter_Friend_Dialog(client_friendList.this);
        filter_dialog.show(getParentFragmentManager(), "example dialog");
        filter_dialog.setExampleDialog(client_friendList.this);
    }
}