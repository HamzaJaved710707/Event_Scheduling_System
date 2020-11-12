package com.example.eventscheduling.client.ui;


import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.eventscheduling.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class client_profile extends Fragment implements View.OnClickListener {

    private static final String TAG = "client_profile";
    // Request code used to show popup menu when profile picture is clicked long
    private static int PIC_CAMERA_REQ = 1;
    private static int GALLERY_REQ = 2;
    private ImageView client_profile_photo;
    private ImageView client_cover_photo;
    private TextView userName;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference user_Profile_Ref;
    private DocumentReference userRef;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String userID;
    private FirebaseStorage frStorage;
    private StorageReference usersPicStorageRef;
    private Uri imageUri;
    private ProgressBar progressBar;
    private Dialog mOverlayDialog;
    private String addToStackString;
    private ImageView order_image;
    private ImageView friend_image;
    private ImageView message_image;

    public client_profile() {
        // Required empty public constructor
    }


    /// Implements ONClick listener
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.client_profile_order_icon:

                getParentFragmentManager().beginTransaction().replace(R.id.frameLayout_clientHome, new client_orders()).addToBackStack(addToStackString).commit();

                break;
            case R.id.client_profile_followers_icon:
                getParentFragmentManager().beginTransaction().replace(R.id.frameLayout_clientHome, new client_friendList()).addToBackStack(addToStackString).commit();

                break;
            case R.id.client_profile_msg_icon:
                getParentFragmentManager().beginTransaction().replace(R.id.frameLayout_clientHome, new client_messages()).addToBackStack(addToStackString).commit();

                break;
            default:
                return;

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_profile, container, false);
        client_profile_photo = view.findViewById(R.id.client_profile_pic);
        client_cover_photo = view.findViewById(R.id.client_cover_pic);
        progressBar = view.findViewById(R.id.client_profile_progressBar);

        mOverlayDialog = new Dialog(view.getContext(), android.R.style.Theme_Panel); //display an invisible overlay dialog to prevent user interaction and pressing back
        mOverlayDialog.setCancelable(false);
        mOverlayDialog.show();
        progressBar.setVisibility(View.VISIBLE);
        // Business Name Textview
        userName = view.findViewById(R.id.client_profile_business_name);
        registerForContextMenu(client_cover_photo);
        registerForContextMenu(client_profile_photo);
        friend_image = view.findViewById(R.id.client_profile_followers_icon);
        friend_image.setOnClickListener(this);
        order_image = view.findViewById(R.id.client_profile_order_icon);
        order_image.setOnClickListener(this);
        message_image = view.findViewById(R.id.client_profile_msg_icon);
        message_image.setOnClickListener(this);
        currentUser = mAuth.getCurrentUser();
        // checking whether user is null or not
        if (currentUser != null) {
            userID = currentUser.getUid();
        }
        frStorage = FirebaseStorage.getInstance();
        usersPicStorageRef = frStorage.getReference("Clients/" + userID + "/Profile_Picture/");
        // In this Document Reference 1 means Profile Picture
        // Where 2 means Uploads or Portfolio data
        user_Profile_Ref = firestore.collection("Users");
        userRef = firestore.collection("Users").document(userID);
        loadData(view);

        return view;

    }


// When user long press the profile image context menu will be shown
    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        switch (v.getId()) {
            case R.id.client_profile_pic:
                inflater.inflate(R.menu.evnt_profile_pic, menu);
                break;
            case R.id.client_cover_pic:
                inflater.inflate(R.menu.evnt_profile_pic, menu);
                break;
        }
    }

// Open camera or gallery to choose picture for profile of user
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.camera:
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, PIC_CAMERA_REQ);
                return true;
            case R.id.gallery:
                Intent intent1 = new Intent();
                intent1.setType("image/*");
                intent1.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent1, "Select Picture"), GALLERY_REQ);
                return true;
            default:
                return super.onContextItemSelected(item);

        }
    }
// When user select some file from gallery or camera result ... then this method will be called
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == PIC_CAMERA_REQ || requestCode == GALLERY_REQ) && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            // Creating the reference of the image
            usersPicStorageRef.child(String.valueOf(System.currentTimeMillis())).putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.d(TAG, "onSuccess: " + uri);
                                    String uri_download = uri.toString();
                                    Map data = new HashMap();
                                    data.put("imgUrl", uri_download);
                                    user_Profile_Ref.document(userID).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getContext(), "Upload Sucessfull", Toast.LENGTH_SHORT).show();
                                            Glide.with(client_profile.this).load(imageUri).into(client_profile_photo);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure:  " + e.getMessage());
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ((client_home)getActivity()).selectTitleOfActionBar("Profile");

    }



    // Automatically display data when this activity loads
    private void loadData(View view) {
        user_Profile_Ref.document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String link = documentSnapshot.getString("imgUrl");
                if(link != null){
                    Glide.with(client_profile.this).load(link).into(client_profile_photo);
                }
                else{
                    Glide.with(client_profile.this).load(R.mipmap.account_person).into(client_profile_photo);
                }

                userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String businessName = documentSnapshot.getString("Name");
                        userName.setText(businessName);
                        progressBar.setVisibility(View.INVISIBLE);
                        mOverlayDialog.dismiss();

                    }
                });
            }
        });

    }



}
