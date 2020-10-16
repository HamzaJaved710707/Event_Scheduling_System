package com.example.eventscheduling.eventorg.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.eventscheduling.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class evnt_profile_view extends Fragment implements View.OnClickListener {

    private static final String TAG = "evnt_profile_view";
    private ImageView friendImg;
    private ImageView msgImg;
    private ImageView portfolioImg;
    private MaterialTextView busniessTxt;
    private MaterialTextView businessCatTxt;
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
    private String id;
    private String backStackString;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evnt_profile_view, container, false);
        busniessTxt = view.findViewById(R.id.evnt_profile_view_about_businessNameTxt);
        businessCatTxt = view.findViewById(R.id.evnt_profile_view_about_businessCatTxt);
        emailTxt = view.findViewById(R.id.evnt_profile_view_about_business_emailTxt);
        numberTxt = view.findViewById(R.id.evnt_profile_view_about_business_numberTxt);
        portfolioImg = view.findViewById(R.id.evnt_profile_view_order_icon);
        portfolioImg.setOnClickListener(this);
        friendImg = view.findViewById(R.id.evnt_profile_view_followers_icon);
        friendImg.setOnClickListener(this);
        msgImg = view.findViewById(R.id.evnt_profile_view_msg_icon);
        msgImg.setOnClickListener(this);
        profileImg = view.findViewById(R.id.evnt_profile_view_pic);
        currentUser = mAuth.getCurrentUser();
        assert getArguments() != null;
        id = getArguments().getString("id");
        if (currentUser != null) {
            if(id == null){
                currentUserId = currentUser.getUid();
            }
            else{
                currentUserId = id;
            }

            userCollection = firestore.collection("Users");
            friendCollection = userCollection.document(currentUserId).collection("Friends");
            LoadData(view);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.evnt_profile_view_order_icon:
                // Portfolio
                evntOrg_Portfolio obj = new evntOrg_Portfolio();
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                obj.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_test_id, obj).addToBackStack(backStackString).commit();
                break;
            case R.id.evnt_profile_view_followers_icon:
                // Friends
                AddFriend();
                break;
            case R.id.evnt_profile_view_msg_icon:
                // Message
                break;
            default:
                break;

        }
    }

    // Load data from firebase
    private void LoadData(View view) {
        userCollection.document(currentUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                busniessTxt.setText(documentSnapshot.getString("businessName"));
                businessCatTxt.setText(documentSnapshot.getString("businessCat"));
                emailTxt.setText(documentSnapshot.getString("email"));
                numberTxt.setText(documentSnapshot.getString("mobileNumber"));
                nameTxt.setText(documentSnapshot.getString("Name"));
                Glide.with(view).load(documentSnapshot.getString("imgUrl")).into(profileImg);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void AddFriend(){

        friendCollection.document(currentUserId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Log.d(TAG, "onSuccess: document exist");
                            Toast.makeText(getContext(), "You are already friends", Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d(TAG, "onSuccess: does not exists");
                            Map value = new HashMap();
                            value.put("id", currentUser);
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
}