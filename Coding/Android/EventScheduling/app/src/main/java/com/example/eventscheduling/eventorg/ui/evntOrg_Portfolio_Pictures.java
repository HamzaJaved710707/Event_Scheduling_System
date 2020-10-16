package com.example.eventscheduling.eventorg.ui;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.eventscheduling.R;
import com.example.eventscheduling.eventorg.model.Portfolio_Pictures_Adapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class evntOrg_Portfolio_Pictures extends Fragment implements View.OnClickListener {
    private static final String TAG = "evntOrg_Portfolio_Pic";
    private final static int NUM_COLUMNS = 2;
    private static int PIC_CAMERA_REQ = 1;
    private static int GALLERY_REQ = 2;
    // Floating button to add new media files
    FloatingActionButton flt_btn;
    List<String> arrayList = new ArrayList<>();
    // Variables for Firebase
    private FirebaseFirestore firestore;
    private CollectionReference user_picture_collection;
    private DocumentReference userRef;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String userID;
    private FirebaseStorage frStorage;
    private StorageReference usersPicStorageRef;
    private Uri imageUri;
private String id;
    public evntOrg_Portfolio_Pictures() {
        // Required empty public constructor
    }
//// Get the type of extension of photo

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evnt_org__portfolio__pictures, container, false);
        flt_btn = view.findViewById(R.id.evnt_portfolio_pictures_flt_Btn);
        flt_btn.setOnClickListener(this);
        currentUser = mAuth.getCurrentUser();
        assert getArguments() != null;
        id = getArguments().getString("id");
        // checking whether user is null or not
        if (currentUser != null) {
            if(id == null){
                userID = currentUser.getUid();
            }
            else{
                userID = id;
            }

        }
        firestore = FirebaseFirestore.getInstance();
        frStorage = FirebaseStorage.getInstance();
        usersPicStorageRef = frStorage.getReference("Event_Org/" + userID + "/Uploads/Pictures/");
        // In this Document Reference 1 means Profile Picture
        // Where 2 means Uploads or Portfolio data
        user_picture_collection = firestore.collection("Users").document(userID).collection("Photos").document("2").collection("Pictures");
        userRef = firestore.collection("Users").document(userID);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onStart() {
        super.onStart();
        initPictures(getView());
    }

    public void initPictures(final View view) {
        Log.d(TAG, "initPictures: ");
        arrayList.clear();
        user_picture_collection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Log.d(TAG, "onSuccess: " + documentSnapshot.get("Link"));
                    arrayList.add(documentSnapshot.getString("Link"));

                }
                initAdapter(view);
            }
        });

    }

    public void initAdapter(View view) {
        Log.d(TAG, "initAdapter: ");
        RecyclerView recyclerView = view.findViewById(R.id.portfolio_pictures_recyclerView);
        Portfolio_Pictures_Adapter pic_adapter = new Portfolio_Pictures_Adapter(view.getContext(), arrayList);
        recyclerView.setAdapter(pic_adapter);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        Log.d(TAG, "initAdapter:  set every thing");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.evnt_portfolio_pictures_flt_Btn:
                String[] colors = {"Camera", "Gallery"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Select Picture");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        if (which == 0) {
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            startActivityForResult(intent, PIC_CAMERA_REQ);

                        } else if (which == 1) {
                            Intent intent1 = new Intent();
                            intent1.setType("image/*");
                            intent1.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent1, "Select Picture"), GALLERY_REQ);
                        }
                    }
                });
                builder.show();
                break;
            default:
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == PIC_CAMERA_REQ || requestCode == GALLERY_REQ) && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            // Creating the reference of the image
            usersPicStorageRef.child(Long.toString(System.currentTimeMillis())).putFile(imageUri)
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
                                    data.put("Name", "Portfolio Picture");
                                    data.put("Link", uri_download);
                                    user_picture_collection.add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getContext(), "Upload Sucessfull", Toast.LENGTH_SHORT).show();

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
}
