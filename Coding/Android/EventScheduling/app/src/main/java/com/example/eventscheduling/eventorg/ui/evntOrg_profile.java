package com.example.eventscheduling.eventorg.ui;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eventscheduling.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;


public class evntOrg_profile extends Fragment {

    private static final String TAG = "evntOrg_profile";
    private static int PIC_CAMERA_REQ = 1;
    private static int GALLERY_REQ = 2;
    private ImageView profile_photo;
    private ImageView cover_photo;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private DocumentReference userRef;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String userID;
    private FirebaseStorage frStorage;
    private StorageReference usersPicStorageRef;
    private Uri imageUri;

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
        registerForContextMenu(cover_photo);
        registerForContextMenu(profile_photo);

        currentUser = mAuth.getCurrentUser();
        // checking whether user is null or not
        if (currentUser != null) {
            userID = currentUser.getUid();
        }
        frStorage = FirebaseStorage.getInstance();
        usersPicStorageRef = frStorage.getReference("usersPictures");
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

    private String getFileExtension(Uri uri) {
        ContentResolver cR = requireActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PIC_CAMERA_REQ) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    // Getting image data from ActivityResult class
                    imageUri = data.getData();

                    // Creating the reference of the image
                    usersPicStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

                    // Uploading image in the firestore storage
                    usersPicStorageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Adding reference and image Name in the database of the user
                            Map imageData = new HashMap();
                            imageData.put("image", taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                            firestore.collection("Users").document(userID).set(imageData);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: " + e.toString());

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });
                    Log.d(TAG, "onActivityResult: " + " Gallery option data has been received");
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == GALLERY_REQ) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    imageUri = data.getData();

                    // Creating the reference of the image
                    usersPicStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
                    if (imageUri != null) {
                        // Uploading image in the firestore storage
                        usersPicStorageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Adding reference and image Name in the database of the user
                                Map imageData = new HashMap();
                                imageData.put("image", taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                                firestore.collection("Users").document(userID).set(imageData);
                                Log.d(TAG, "onSuccess: " + "Inside on Sucess listener of profile activity");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: " + e.toString());

                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            }
                        });
                        Log.d(TAG, "onActivityResult: " + " Gallery option data has been received");
                    } else {
                        Log.d(TAG, "onActivityResult: imageURi is null");
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}