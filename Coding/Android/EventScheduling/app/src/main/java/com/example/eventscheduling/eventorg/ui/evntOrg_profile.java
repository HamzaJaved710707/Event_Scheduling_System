package com.example.eventscheduling.eventorg.ui;


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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class evntOrg_profile extends Fragment {

    private static final String TAG = "evntOrg_profile";
    // Request code used to show popup menu when profile picture is clicked long
    private static int PIC_CAMERA_REQ = 1;
    private static int GALLERY_REQ = 2;
    private ImageView profile_photo;
    private ImageView cover_photo;
    private TextView business_name_txtView;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private DocumentReference user_Profile_Ref;
    private DocumentReference userRef;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String userID;
    private FirebaseStorage frStorage;
    private StorageReference usersPicStorageRef;
    private Uri imageUri;
    private ProgressBar progressBar;
    public evntOrg_profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evnt_org_profile, container, false);
        profile_photo = view.findViewById(R.id.evnt_profile_pic);
        cover_photo = view.findViewById(R.id.evnt_cover_pic);
        // Business Name Textview
        business_name_txtView = view.findViewById(R.id.evnt_profile_business_name);
        registerForContextMenu(cover_photo);
        registerForContextMenu(profile_photo);
        progressBar = view.findViewById(R.id.evntOrg_profile_progressBar);
        progressBar.setVisibility(View.VISIBLE);

        currentUser = mAuth.getCurrentUser();
        // checking whether user is null or not
        if (currentUser != null) {
            userID = currentUser.getUid();
        }
        frStorage = FirebaseStorage.getInstance();
        usersPicStorageRef = frStorage.getReference("Event_Org/" + userID + "/Profile_Picture/");
        // In this Document Reference 1 means Profile Picture
        // Where 2 means Uploads or Portfolio data
        user_Profile_Ref = firestore.collection("Users").document(userID);
        userRef = firestore.collection("Users").document(userID);


        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // If user is not null then proceed
       /* if(currentUser != null){
            userRef = firestore.collection("Users").document(userID).collection("businessData").document("1");

            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String category = (String) documentSnapshot.get("category");
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("Category", Context.MODE_PRIVATE);
                    Log.d(TAG, "onSuccess: user category is  " + category);
                    switch (category) {
                        case "Event Organizer":
                            sharedPreferences.edit().putInt("category", 1).apply();
                            break;
                        case "Catering":
                            sharedPreferences.edit().putInt("category", 2).apply();
                            break;
                        case "Venue Provider":
                            sharedPreferences.edit().putInt("category", 3).apply();
                            break;
                        case "Decoration":
                            sharedPreferences.edit().putInt("category", 4).apply();
                            break;
                        case "Car Service":
                            sharedPreferences.edit().putInt("category", 5).apply();
                            break;
                        case "Invitation Cards":
                            sharedPreferences.edit().putInt("category", 6).apply();
                            break;
                        default:
                            sharedPreferences.edit().putInt("category", 0).apply();
                            break;
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: " + e.toString());
                }
            });

        }
        else{
            Toast.makeText(getContext(), "Please restart the application", Toast.LENGTH_LONG).show();
        }*/


    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        switch (v.getId()) {
            case R.id.evnt_profile_pic:
                inflater.inflate(R.menu.evnt_profile_pic, menu);
                break;
            case R.id.evnt_cover_pic:
                inflater.inflate(R.menu.evnt_profile_pic, menu);
                break;
        }
    }

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
                                    user_Profile_Ref.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getContext(), "Upload Sucessfull", Toast.LENGTH_SHORT).show();
                                            Glide.with(evntOrg_profile.this).load(imageUri).into(profile_photo);
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

    private void loadData() {
        user_Profile_Ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String link = documentSnapshot.getString("imgUrl");
                Glide.with(evntOrg_profile.this).load(link).into(profile_photo);
                userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String businessName = documentSnapshot.getString("businessName");
                        business_name_txtView.setText(businessName);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });

    }

    // Portfolio icon click handler
    private void Portfolio_onClick(View view) {
        Toast.makeText(getContext(), "Portfolio icon is clicked", Toast.LENGTH_SHORT).show();
    }

    // Followers icon click handler
    private void Followers_onClick(View view) {
        Toast.makeText(getContext(), "Followers icon is clicked", Toast.LENGTH_SHORT).show();

    }

    // Message Icon click handler
    private void Msg_onClick(View view) {
        Toast.makeText(getContext(), "Message icon is clicked", Toast.LENGTH_SHORT).show();

    }

}