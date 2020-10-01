package com.example.eventscheduling.client.ui;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventscheduling.R;
import com.example.eventscheduling.client.model.Client_RecyclerAdptr_Msg;
import com.example.eventscheduling.client.util.Client_Msg_Values;
import com.example.eventscheduling.eventorg.model.RecyclerView_Adapter_Message;
import com.example.eventscheduling.eventorg.ui.evntOrg_MessageDetail;
import com.example.eventscheduling.eventorg.ui.evntOrg_friend_list;
import com.example.eventscheduling.eventorg.util.MessageValues;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;


public class client_messages extends Fragment {
    private static final String TAG = "client_messages";
    private FloatingActionButton floatingActionBtn;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String currentUserID;
    private FirebaseFirestore firestore;
    private CollectionReference dbReference;
private RecyclerView recyclerView;
private Client_RecyclerAdptr_Msg adapter_message;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_messages, container, false);
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserID = currentUser.getUid();
        dbReference = FirebaseFirestore.getInstance().collection("Users").document(currentUserID).collection("conversation");
       initRecyclerView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        floatingActionBtn = view.findViewById(R.id.client_floatBtn_msg_evnt);
        floatingActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), client_friendList.class));
            }
        });
    }

    public void initRecyclerView(View view) {
        Log.d(TAG, "initRecyclerView: is called");
        recyclerView = view.findViewById(R.id.client_recyclerview_msg);

        Query query = dbReference.orderBy("timeStamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Client_Msg_Values> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Client_Msg_Values>().setQuery(query, Client_Msg_Values.class).build();
        adapter_message = new Client_RecyclerAdptr_Msg(getContext(), firestoreRecyclerOptions);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

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
        super.onStart();
        adapter_message.startListening();
    }

    @Override
    public void onStop() {
            super.onStop();
            adapter_message.stopListening();
    }
}
