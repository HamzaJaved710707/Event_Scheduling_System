package com.example.eventscheduling.eventorg.ui;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.eventscheduling.R;
import com.example.eventscheduling.eventorg.model.Portfolio_Pictures_Adapter;
import com.example.eventscheduling.eventorg.model.Portfolio_Videos_Adapter;
import com.example.eventscheduling.eventorg.util.portfolio_video_values;
import com.example.eventscheduling.util.ExoMediaPlayer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.widget.LinearLayout.VERTICAL;

/**
 * A simple {@link Fragment} subclass.
 */
public class evntOrg_Portfolio_Videos extends Fragment implements View.OnClickListener, Portfolio_Videos_Adapter.itemClickInterface {

    private static final String TAG = "evntOrg_Portfolio_Vid";
    private final static int NUM_COLUMNS = 1;
    private static int Vid_CAMERA_REQ = 1;
    private static int GALLERY_REQ = 2;
    // Floating button to add new media files
    FloatingActionButton flt_btn;
    ArrayList<portfolio_video_values> arrayList = new ArrayList<>();
    // Variables for Firebase
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference user_video_collection;
    private DocumentReference userRef;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String userID;
    private FirebaseStorage frStorage = FirebaseStorage.getInstance();
    private StorageReference usersVideoStorageRef;
    private Uri imageUri;
    private boolean firstTime = true;
    private static final int SELECT_VIDEO = 0;
    private RecyclerView recyclerView;
    private Portfolio_Videos_Adapter videos_adapter;
    private String id;
    //


    public evntOrg_Portfolio_Videos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evnt_org__portfolio__videos,container, false);
        flt_btn = view.findViewById(R.id.evnt_portfolio_videos_flt_Btn);
        flt_btn.setOnClickListener(this);
        id = getArguments().getString("id");
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            if(id == null){
                userID = currentUser.getUid();
            }
            else{
                userID = id;
            }

            usersVideoStorageRef = frStorage.getReference("Event_Org/" + userID + "/Uploads/Videos/");
            user_video_collection = firestore.collection("Users").document(userID).collection("Photos").document("2").collection("Videos");
            recyclerView = view.findViewById(R.id.evntOrg_portfolio_video_recyclerview);

            initRecyclerView(view);
        }


        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

    }

   public void setfltVisibilty(){
        flt_btn.setVisibility(View.VISIBLE);

   }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.evnt_portfolio_videos_flt_Btn){
            Intent videoIntent = new Intent(Intent.ACTION_PICK);
            videoIntent.setType("video/*");
            startActivityForResult(Intent.createChooser(videoIntent, "Select Video"),SELECT_VIDEO );

        }

    }


    private void initRecyclerView(View view){
        user_video_collection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                    arrayList.add(new portfolio_video_values(documentSnapshot.getString("Name"), documentSnapshot.getString("Link")));
                }
                videos_adapter = new Portfolio_Videos_Adapter(view.getContext(), arrayList);
                videos_adapter.setListener(evntOrg_Portfolio_Videos.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                recyclerView.setAdapter(videos_adapter);
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
    @ Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_VIDEO) {
              Uri  selectedVideoPath = data.getData();
                if(selectedVideoPath == null) {
                    Log.d(TAG, "onActivityResult: SELECTED DATA PATH IS NULL");

                } else {
                    usersVideoStorageRef.child(Long.toString(System.currentTimeMillis())).putFile(selectedVideoPath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uri_download = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            uri_download.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String uriString = uri.toString();
                                    Map data = new HashMap();
                                    data.put("Name", "Portfolio Video");
                                    data.put("Link", uriString);
                                    user_video_collection.add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getContext(), "Upload Sucessfull", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }
                            });


                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d(TAG, Long.toString((taskSnapshot.getBytesTransferred() /taskSnapshot.getTotalByteCount())* 100) );
                        }
                    });
                }
            }
        }

    }

    @Override
    public void itemClick(String url) {
        String tag = "exo";
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        ExoMediaPlayer obj = new ExoMediaPlayer();
        obj.setArguments(bundle);
        getChildFragmentManager().beginTransaction().
                replace(R.id.evntOrg_portfolio_videos_frameLayout, obj).addToBackStack(tag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }
}
