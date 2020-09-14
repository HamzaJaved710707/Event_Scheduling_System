package com.example.eventscheduling.eventorg.ui;


import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.eventscheduling.R;
import com.example.eventscheduling.eventorg.model.Portfolio_Pictures_Adapter;
import com.example.eventscheduling.eventorg.model.Portfolio_Videos_Adapter;
import com.example.eventscheduling.eventorg.model.VideoPlayerRecyclerView;
import com.example.eventscheduling.eventorg.util.portfolio_video_values;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.widget.LinearLayout.VERTICAL;

/**
 * A simple {@link Fragment} subclass.
 */
public class evntOrg_Portfolio_Videos extends Fragment implements View.OnClickListener {

    private static final String TAG = "evntOrg_Portfolio_Vid";
    private final static int NUM_COLUMNS = 1;
    private static int Vid_CAMERA_REQ = 1;
    private static int GALLERY_REQ = 2;
    // Floating button to add new media files
    FloatingActionButton flt_btn;
    ArrayList<portfolio_video_values> arrayList = new ArrayList<>();
    // Variables for Firebase
    private FirebaseFirestore firestore;
    private CollectionReference user_video_collection;
    private DocumentReference userRef;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String userID;
    private FirebaseStorage frStorage;
    private StorageReference usersVideoStorageRef;
    private Uri imageUri;
    private boolean firstTime = true;

    //

    private VideoPlayerRecyclerView mRecyclerView;

    public evntOrg_Portfolio_Videos() {
        // Required empty public constructor
    }

    //// Get the type of extension of Video
    public static String getMimeType(Uri uri) {
        String type = null;
        String url = uri.toString();
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        Log.d(TAG, "getMimeType: " + type);
        return type;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evnt_org__portfolio__videos,container, false);
        flt_btn = view.findViewById(R.id.evnt_portfolio_videos_flt_Btn);
        flt_btn.setOnClickListener(this);
        mRecyclerView = view.findViewById(R.id.video_recyler_view);
        Log.d(TAG, "onCreateView: " + "event organizor portfolio video" );
        prepareVideoList();
      /*  currentUser = mAuth.getCurrentUser();
        // checking whether user is null or not
        if (currentUser != null) {
            userID = currentUser.getUid();
        }
        firestore = FirebaseFirestore.getInstance();
        frStorage = FirebaseStorage.getInstance();
        usersVideoStorageRef = frStorage.getReference("Event_Org/" + userID + "/Uploads/Videos/");
        // In this Document Reference 1 means Profile Picture
        // Where 2 means Uploads or Portfolio data
        user_video_collection = firestore.collection("Users").document(userID).collection("Photos").document("2").collection("Videos");
        userRef = firestore.collection("Users").document(userID);*/
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView(view);
        if (firstTime) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mRecyclerView.playVideo(false);
                }
            });
            firstTime = false;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {

    }


    private void initRecyclerView(View view){

        mRecyclerView = view.findViewById(R.id.video_recyler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setMediaObjects(arrayList);


        Log.d(TAG, "initRecyclerView: "+ "event organizor portfolio video");
        Portfolio_Videos_Adapter adapter = new Portfolio_Videos_Adapter(arrayList, initGlide());
        mRecyclerView.setAdapter(adapter);
    }

    private RequestManager initGlide(){
        Log.d(TAG, "initGlide: " + "event organizor portfolio video");
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.white_background)
                .error(R.drawable.white_background);

        return Glide.with(this)
                .setDefaultRequestOptions(options);
    }

   /* private void initVideo(final View view){
        user_video_collection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Log.d(TAG, "onSuccess: " + documentSnapshot.get("Link"));
                    arrayList.add(documentSnapshot.getString("Link"));

                }
                initAdapter(view);
            }
        });

    }*/

    private void initAdapter(View view) {
        Log.d(TAG, "initAdapter: ");
       // RecyclerView recyclerView = view.findViewById(R.id.portfolio_video_recyclerView);
   //     Portfolio_Videos_Adapter video_adapter = new Portfolio_Videos_Adapter(view.getContext(), arrayList);
    //    recyclerView.setAdapter(video_adapter);
      //  StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, StaggeredGridLayoutManager.VERTICAL);
      //  recyclerView.setLayoutManager(layoutManager);
      //  Log.d(TAG, "initAdapter:  set every thing");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mRecyclerView!=null)
            mRecyclerView.releasePlayer();
    }

    // demo video list to check wether player is working or not
    private void prepareVideoList() {
        portfolio_video_values mediaObject = new portfolio_video_values();
        mediaObject.setId(1);
        mediaObject.setUserHandle("@h.pandya");
        mediaObject.setTitle(
                "Do you think the concept of marriage will no longer exist in the future?");
        mediaObject.setCoverUrl(
                "https://androidwave.com/media/images/exo-player-in-recyclerview-in-android-1.png");
        mediaObject.setUrl("https://www.youtube.com/watch?v=tlz91ktjPOs");
        portfolio_video_values mediaObject2 = new portfolio_video_values();
        mediaObject2.setId(2);
        mediaObject2.setUserHandle("@hardik.patel");
        mediaObject2.setTitle(
                "If my future husband doesn't cook food as good as my mother should I scold him?");
        mediaObject2.setCoverUrl(
                "https://androidwave.com/media/images/exo-player-in-recyclerview-in-android-2.png");
        mediaObject2.setUrl("https://www.youtube.com/watch?v=tlz91ktjPOs");
        portfolio_video_values mediaObject3 = new portfolio_video_values();
        mediaObject3.setId(3);
        mediaObject3.setUserHandle("@arun.gandhi");
        mediaObject3.setTitle("Give your opinion about the Ayodhya temple controversy.");
        mediaObject3.setCoverUrl(
                "https://androidwave.com/media/images/exo-player-in-recyclerview-in-android-3.png");
        mediaObject3.setUrl("https://androidwave.com/media/androidwave-video-3.mp4");
        portfolio_video_values mediaObject4 = new portfolio_video_values();
        mediaObject4.setId(4);
        mediaObject4.setUserHandle("@sachin.patel");
        mediaObject4.setTitle("When did kama founders find sex offensive to Indian traditions");
        mediaObject4.setCoverUrl(
                "https://androidwave.com/media/images/exo-player-in-recyclerview-in-android-4.png");
        mediaObject4.setUrl("https://www.youtube.com/watch?v=tlz91ktjPOs");
        portfolio_video_values mediaObject5 = new portfolio_video_values();
        mediaObject5.setId(5);
        mediaObject5.setUserHandle("@monika.sharma");
        mediaObject5.setTitle("When did you last cry in front of someone?");
        mediaObject5.setCoverUrl(
                "https://androidwave.com/media/images/exo-player-in-recyclerview-in-android-5.png");
        mediaObject5.setUrl("https://www.youtube.com/watch?v=tlz91ktjPOs");
        arrayList.add(mediaObject);
        arrayList.add(mediaObject2);
        arrayList.add(mediaObject3);
        arrayList.add(mediaObject4);
        arrayList.add(mediaObject5);
        arrayList.add(mediaObject);
        arrayList.add(mediaObject2);
        arrayList.add(mediaObject3);
        arrayList.add(mediaObject4);
        arrayList.add(mediaObject5);
    }
}
