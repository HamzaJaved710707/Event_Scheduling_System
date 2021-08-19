package com.example.eventscheduling.eventorg.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventscheduling.R;
import com.example.eventscheduling.eventorg.model.msgDetail_adapter;
import com.example.eventscheduling.eventorg.util.msgDetail_values;
import com.example.eventscheduling.util.BaseActivity;
import com.example.eventscheduling.util.CallActivity;
import com.example.eventscheduling.util.SinchService;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class evntOrg_MessageDetail extends  BaseActivity implements View.OnClickListener, SinchService.StartFailedListener {
    private static final String TAG = "evntOrg_MessageDetail";
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentuser;
    private String mCurrentUserId;
    private DocumentReference chatUserDocRef;
    private DocumentReference currentUserDocRef;
    private String mChatUserId;
    private String mChatUserTokenId;
    private ImageView sendView;
    private EditText messageWriteField;
    private ImageView addMessageImgView;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference chat_Collection_Reference = firebaseFirestore.collection("chats");
    private CollectionReference conversation_Collection_Reference = firebaseFirestore.collection("conversation");
    private CollectionReference user_collection = firebaseFirestore.collection("Users");
    private msgDetail_adapter msg_adapter;
    private LinearLayoutManager linearLayoutManager;
    private int value = 1;
    private String chatUserName;
    private String currentUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evnt_org__message_detail);
        mAuth = FirebaseAuth.getInstance();
        mCurrentuser = mAuth.getCurrentUser();
        mCurrentUserId = Objects.requireNonNull(mCurrentuser).getUid();
        // Get intent from Message Activity from document snapshot
        mChatUserId = getIntent().getStringExtra("chatId");
        mChatUserTokenId = getIntent().getStringExtra("tokenId");
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Messages");

        }
        // Initialize Views
        sendView = findViewById(R.id.messageSendImg);
        messageWriteField = findViewById(R.id.messageWriteEdt);
        addMessageImgView = findViewById(R.id.addMsgDetailImg);
        sendView.setOnClickListener(this);
        addMessageImgView.setOnClickListener(this);
        linearLayoutManager = new LinearLayoutManager(this);
        initRecyclerView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.messageSendImg:
                sendMessage();
                break;
            case R.id.addMsgDetailImg:
                addDetails();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.message_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() ==R.id.video_btn_menu){
          /*  Intent intent = new Intent(evntOrg_MessageDetail.this, VideoCallingActivity.class);
            intent.putExtra("userId", mChatUserId);
            intent.putExtra("tokenId",mChatUserTokenId);
            startActivity(intent);*/
            if (!mCurrentUserId.equals(getSinchServiceInterface().getUserName())) {
                getSinchServiceInterface().stopClient();
            }

            if (!getSinchServiceInterface().isStarted()) {
                getSinchServiceInterface().startClient(mCurrentUserId);

            } else {
                openPlaceCallActivity();
            }

        }
        return true;
    }

    private void openPlaceCallActivity() {
        Call call = getSinchServiceInterface().callUserVideo(mChatUserId);
        String callId = call.getCallId();

        Intent callScreen = new Intent(this, CallActivity.class);
        callScreen.putExtra(SinchService.CALL_ID, callId);
        startActivity(callScreen);
    }

    private void stopButtonClicked() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().stopClient();
        }
        finish();
    }
    private void sendMessage() {

        Log.d(TAG, "Click on send button");
        String data = messageWriteField.getText().toString();
        messageWriteField.setText("");
        data = data.trim();
        if (!data.matches("")) {
            if(value == 1) {
                user_collection.document(mCurrentUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        currentUserName = documentSnapshot.getString("Name");
                        user_collection.document(mChatUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful() && task.getResult() != null) {

                                    chatUserName = task.getResult().getString("Name");
                                    Log.d(TAG, "onComplete: " + chatUserName);
                                    Map userData = new HashMap();
                                    userData.put("Name", chatUserName);
                                    userData.put("Id", mChatUserId);
                                    userData.put("timeStamp", System.currentTimeMillis());
                                    Map userData2 = new HashMap();
                                    userData2.put("Name", currentUserName);
                                    userData2.put("Id", mCurrentUserId);
                                    userData2.put("timeStamp", System.currentTimeMillis());
                                    user_collection.document(mCurrentUserId).collection("conversation").document(mChatUserId).set(userData);
                                    user_collection.document(mChatUserId).collection("conversation").document(mCurrentUserId).set(userData2);
                                    value++;
                                    messageWriteField.setText("");

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: " + e.toString());
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }




            Map chatData = new HashMap();
            chatData.put("message", data);
            chatData.put("seen", false);
            chatData.put("from", mChatUserId);
            chatData.put("timeStamp", System.currentTimeMillis());
            chat_Collection_Reference.document(mCurrentUserId).collection(mChatUserId).document().set(chatData);
            chat_Collection_Reference.document(mChatUserId).collection(mCurrentUserId).document().set(chatData);


        }
    }

    private void addDetails() {
    }

    // Initialize recycler view with firebase UI

    private void initRecyclerView() {
        Log.d(TAG, "initializing recycler view");
        Query query = chat_Collection_Reference.document(mCurrentUserId).collection(mChatUserId).orderBy("timeStamp", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<msgDetail_values> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<msgDetail_values>()
                .setQuery(query, msgDetail_values.class).build();
        msg_adapter = new msgDetail_adapter(this, firestoreRecyclerOptions);
        final RecyclerView recyclerView = findViewById(R.id.msgdetail_recyclerview);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(msg_adapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                msg_adapter.item_delete_swap(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
        // Scroll the window to bottom to show new msg added...
        msg_adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = msg_adapter.getItemCount();
                int lastVisiblePosition =
                        linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 || positionStart >= (friendlyMessageCount - 1)) {
                    recyclerView.scrollToPosition(positionStart);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        msg_adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        msg_adapter.stopListening();
    }
    @Override
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    public void onStartFailed(SinchError error) {

            Log.d(TAG, error.getMessage());
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onStarted() {
openPlaceCallActivity();
    }
}
