package com.example.eventscheduling.client.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventscheduling.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class client_profile_view extends Fragment implements View.OnClickListener {
private ImageView callImg;
private ImageView msgImg;
private ImageView friendImg;
private MaterialTextView emailTxt;
private MaterialTextView numberTxt;
private CircleImageView profileImg;
private TextView nameTxt;
private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
private FirebaseAuth mAuth = FirebaseAuth.getInstance();
private FirebaseUser currentUser;
private String currentUserId;
private CollectionReference userCollection;
private String remoteUserId;
private CollectionReference friendCollection;
    private static final String TAG = "client_profile_view";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_client_profile_view, container, false);
        callImg = view.findViewById(R.id.client_profile_view_order_icon);
        callImg.setOnClickListener(this);
        msgImg = view.findViewById(R.id.client_profile_view_msg_icon);
        msgImg.setOnClickListener(this);
        friendImg = view.findViewById(R.id.client_profile_view_followers_icon);
       friendImg.setOnClickListener(this);
        emailTxt = view.findViewById(R.id.client_profile_view_about_emailTxt);
        numberTxt = view.findViewById(R.id.client_profile_view_about_numberTxt);
        profileImg = view.findViewById(R.id.client_profile_view_pic);
        nameTxt = view.findViewById(R.id.client_profile_view_business_name);
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUserId = currentUser.getUid();
            userCollection = firestore.collection("Users");
            friendCollection = userCollection.document(currentUserId).collection("Friends");
            assert getArguments() != null;
            if(getArguments().getString("id") != null){
                remoteUserId = getArguments().getString("id");
                LoadData();
            }

        }
        return view;
    }

    private void LoadData(){
        userCollection.document(remoteUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                nameTxt.setText( documentSnapshot.getString("Name"));
                emailTxt.setText(documentSnapshot.getString("email"));
                numberTxt.setText(documentSnapshot.getString("mobileNumber"));


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.client_profile_view_order_icon:
                //Video Call
                Toast.makeText(getContext(), "User is not online", Toast.LENGTH_SHORT).show();
                break;
            case R.id.client_profile_view_msg_icon:
                Intent intent = new Intent(getContext(), client_msgDetail.class);
                intent.putExtra("chatId", remoteUserId);
                startActivity(intent);
                //Message
                break;
            case R.id.client_profile_view_followers_icon:
                //Add Friend
                addFriend();

                break;
            default:
                break;
        }
    }


    private void addFriend(){
        friendCollection.document(remoteUserId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Log.d(TAG, "onSuccess: document exist");
                            Toast.makeText(getContext(), "You are already friends", Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d(TAG, "onSuccess: does not exists");
                            Map value = new HashMap();
                            value.put("friends", true);
                            friendCollection.document(remoteUserId).set(value).addOnSuccessListener(new OnSuccessListener<Void>() {
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
}