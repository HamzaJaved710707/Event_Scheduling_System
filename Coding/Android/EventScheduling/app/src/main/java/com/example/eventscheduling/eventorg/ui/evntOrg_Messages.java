package com.example.eventscheduling.eventorg.ui;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventscheduling.R;
import com.example.eventscheduling.eventorg.model.RecyclerView_Adapter_Message;
import com.example.eventscheduling.eventorg.util.MessageValues;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

// Message Fragment to Show messages to Event organizer
public class evntOrg_Messages extends Fragment{
    private static final String TAG = "evntOrg_Messages";
    private FirebaseUser currentUser;
    private String currentUserID;
    private CollectionReference dbReference;
    private FirebaseAuth mAuth;
    private FloatingActionButton floatingActionBtn;
    private RecyclerView_Adapter_Message adapter_message;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_evnt_org__messages, container, false);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        floatingActionBtn = rootview.findViewById(R.id.floatBtn_msg_evnt);
        floatingActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), evntOrg_friend_list.class));
            }
        });
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserID = currentUser.getUid();
        dbReference = FirebaseFirestore.getInstance().collection("Users").document(currentUserID).collection("conversation");
        initRecyclerView(rootview);
        return rootview;
    }

    private void initRecyclerView(View rootview) {
        Log.d(TAG, "initRecyclerView: start");
        Query query = dbReference.orderBy("timeStamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<MessageValues> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<MessageValues>().setQuery(query, MessageValues.class).build();
        adapter_message = new RecyclerView_Adapter_Message(getContext(), firestoreRecyclerOptions);
        recyclerView = rootview.findViewById(R.id.message_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootview.getContext()));

        recyclerView.setAdapter(adapter_message);

        adapter_message.onItemClickListner(new RecyclerView_Adapter_Message.itemClickListenerMsgDetail() {
            @Override
            public void itemClickListener(DocumentSnapshot documentSnapshot, int position) {
                String chatUserId = documentSnapshot.getId();
                Intent intent = new Intent(getContext(), evntOrg_MessageDetail.class);
                intent.putExtra("chatId", chatUserId);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart: ");
        super.onStart();
        adapter_message.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter_message.stopListening();
    }

}
