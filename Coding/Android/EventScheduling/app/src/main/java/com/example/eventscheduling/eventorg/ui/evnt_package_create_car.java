package com.example.eventscheduling.eventorg.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.eventscheduling.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class evnt_package_create_car extends Fragment {


    private static final int PIC_CAMERA_REQ = 1 ;
    private static final int GALLERY_REQ =2  ;
    List<String> new_items_service = new ArrayList<>();
    MaterialButton food_btn;
    private TextInputEditText price_edit;
    private TextInputEditText packageNameEdtXt;
    private  String uri_download;
    private CircleImageView imageView;
    private int SpannedLength = 0;
    private int chipLength = 4;
    private String m_Text = "";
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;
    private String currentUserID;
    private FirebaseStorage firebaseStorage;
    private CollectionReference packageReference;
    private StorageReference package_img_Ref;
    private Uri imageUri;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_evnt_package_create_car, container, false);
        // Firebase Initialization
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUserID = currentUser.getUid();
            firestore = FirebaseFirestore.getInstance();
            firebaseStorage = FirebaseStorage.getInstance();
            package_img_Ref = firebaseStorage.getReference("Event_Org/"+currentUserID +"/Packages_Photos");
            packageReference = firestore.collection("Users").document(currentUserID).collection("Packages");
            /*create_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveImage();
                }
            });*/
        }
        return view;
    }
}






























