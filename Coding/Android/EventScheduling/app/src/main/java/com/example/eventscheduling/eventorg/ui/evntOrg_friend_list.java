package com.example.eventscheduling.eventorg.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventscheduling.R;
import com.example.eventscheduling.eventorg.model.friendList_Adapter;
import com.example.eventscheduling.eventorg.util.friendList_values;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class evntOrg_friend_list extends AppCompatActivity {
    friendList_Adapter friendList_adapter;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference dbReference = firebaseFirestore.collection("Users");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    private String current_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evnt_friend_list);
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            current_email =currentUser.getEmail();
        }
        initializeRecyclerView();
    }

    private void initializeRecyclerView() {
        Query query = dbReference.whereEqualTo("type", 0);
        FirestoreRecyclerOptions<friendList_values> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<friendList_values>()
                .setQuery(query, friendList_values.class).build();
        friendList_adapter = new friendList_Adapter(this, firestoreRecyclerOptions);
        RecyclerView recyclerView = findViewById(R.id.friend_list_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(friendList_adapter);
        friendList_adapter.setitemOnclickListener(new friendList_Adapter.onitemClickListener() {
            @Override
            public void itemClick(DocumentSnapshot documentSnapshot, int position) {
                String userId = documentSnapshot.getId();
                Intent intent = new Intent(evntOrg_friend_list.this, evntOrg_MessageDetail.class);
                intent.putExtra("chatId", userId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        friendList_adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        friendList_adapter.stopListening();
    }
}
