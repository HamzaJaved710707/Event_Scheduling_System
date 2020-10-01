package com.example.eventscheduling.client.ui;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventscheduling.R;
import com.example.eventscheduling.client.model.client_friendList_Adapter;
import com.example.eventscheduling.client.util.client_friendList_values;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.ContentValues.TAG;

// Client Search class
public class client_search extends Fragment implements  client_friendList_Adapter.OnItemClicked {

    SearchView searchView;

    // Firebase Variables
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String currentUserID;
    private CollectionReference dbReference;
    private FirebaseFirestore firestore;
    private List<String> tempList = new ArrayList<>();
    private List<client_friendList_values> friendLists = new ArrayList<>();
    private client_friendList_Adapter friendList_adapter;
    private String searchTXt;
    private RecyclerView recyclerView;
private RadioGroup radioGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_search, container, false);
        // Firebase Variable initialization
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserID = currentUser.getUid();
            firestore = FirebaseFirestore.getInstance();
            dbReference = firestore.collection("Users");
            radioGroup = view.findViewById(R.id.client_search_radio_group);
            recyclerView = view.findViewById(R.id.client_search_reyclerview);

        }
searchView = view.findViewById(R.id.searchview_client);
        friendList_adapter = new client_friendList_Adapter(getContext(), friendLists);

        // For changing the color of search icon in searchview
     /*   searchView = view.findViewById(R.id.searchview_client);
        ImageView searchIcon = view.findViewById(androidx.appcompat.R.id.search_mag_icon);
        searchIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_search_white));
*/
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                checkBtnId(radioGroup.getCheckedRadioButtonId(), query);

                Log.d(TAG, "onQueryTextSubmit: " + query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }
    private void checkBtnId(int id, String name){
        if(id == R.id.client_search_radioBtn_org){
            listOfOrganizers(name);
        }
        else if(id == R.id.client_search_radioBtn_client){
            listOfUsers(name);
        }
        else{
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

        // List of Organizers
   private void listOfOrganizers(String name){
        friendList_adapter.deleteData();
        Query query  = dbReference.whereIn("Name", Collections.singletonList(name)).whereEqualTo("type", 0);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    tempList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    tempList.add(documentSnapshot.getString("id"));
                }
                if(tempList.size() != 0){

                    dbReference.whereIn(FieldPath.documentId(), tempList).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                friendLists.clear();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    client_friendList_values object = new client_friendList_values(document.getString("Name"), document.getString("imgUrl"), document.getString("id"));
                                    friendLists.add(object);
                                }
                                friendList_adapter = new client_friendList_Adapter(getContext(), friendLists);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                friendList_adapter.setOnClick(client_search.this);
                                recyclerView.setAdapter(friendList_adapter);


                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
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
    // LIst of users
    private void listOfUsers(String user){
        friendList_adapter.deleteData();
        Query query  = dbReference.whereIn("Name", Collections.singletonList(user)).whereEqualTo("type", 1);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                tempList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    tempList.add(documentSnapshot.getString("id"));
                }
                if(tempList.size() != 0){
                    dbReference.whereIn(FieldPath.documentId(), tempList).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                friendLists.clear();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    client_friendList_values object = new client_friendList_values(document.getString("Name"), document.getString("imgUrl"), document.getString("id"));
                                    friendLists.add(object);
                                }
                                friendList_adapter = new client_friendList_Adapter(getContext(), friendLists);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                friendList_adapter.setOnClick(client_search.this);
                                recyclerView.setAdapter(friendList_adapter);


                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
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
    public void onMsgIconClick(int position, String id) {

    }

    @Override
    public void onFriendIconClick(int position, String id) {

    }
}
