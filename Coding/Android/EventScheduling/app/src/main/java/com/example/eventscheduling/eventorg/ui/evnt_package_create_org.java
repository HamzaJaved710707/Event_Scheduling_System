package com.example.eventscheduling.eventorg.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.eventscheduling.R;
import com.example.eventscheduling.util.SpaceTokenizer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.UploadTask;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


public class evnt_package_create_org extends Fragment implements View.OnClickListener {
    private static final int PIC_CAMERA_REQ = 1;
    private static final int GALLERY_REQ = 2;
    private MaterialButton service_btn;
    private MaterialButton food_btn;
    private EditText package_editText;
    private AutoCompleteTextView venue_textView;
    private TextInputEditText priceEditText;
    private ImageView pic_imageView;
    private Button create_btn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;
    private String currentuserID;
    private static final String TAG = "evnt_package_create_org";
    private Uri imageUri = Uri.EMPTY;

    public evnt_package_create_org() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evnt_package_create_org, container, false);
// save the state of instance
        if (savedInstanceState != null) {
            imageUri = Uri.parse(savedInstanceState.getString("URI"));
        }
        // Initialize the views in UI
        service_btn = view.findViewById(R.id.service_btn_package_evnt_create_org);

        package_editText = view.findViewById(R.id.evnt_package_Name_org_inputField);
        priceEditText = view.findViewById(R.id.price_org_package_create);
        create_btn = view.findViewById(R.id.evnt_package_creat_btn_org);

        String[] colors = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Pick a color");
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


      //  registerForContextMenu(pic_imageView);
        create_btn.setOnClickListener(this);
        if(!imageUri.equals(Uri.EMPTY)){
            pic_imageView.setImageURI(imageUri);
        }

        //set adapter for venue
        ArrayAdapter<String> venueAdapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.Venue_list));
//        venue_textView.setAdapter(venueAdapter);
        // Firebase Authentication initialization
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentuserID = currentUser.getUid();

            // Firestore initialization
            firestore = FirebaseFirestore.getInstance();
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("URI", imageUri.toString());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.evnt_package_creat_btn_org) {
            create_btn_fn();
        }

    }

// To create overlay menu
    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
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
 /*       if ((requestCode == PIC_CAMERA_REQ || requestCode == GALLERY_REQ) && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            // Creating the reference of the image

        }*/

        if ((requestCode == PIC_CAMERA_REQ || requestCode == GALLERY_REQ) && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            pic_imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
    }

    private void create_btn_fn() {
/*
        if (isEmpty(priceEditText) && isEmpty(package_editText) && isEmpty(service_multiValue) && isEmpty(food_multiValue) && isEmpty(venue_textView)) {
            Toast.makeText(getContext(), "Please Fill all fields", Toast.LENGTH_SHORT).show();

        } else {

            String price = priceEditText.getText().toString();
            String packageName = package_editText.getText().toString();
            String services = service_multiValue.getText().toString();
            String food = food_multiValue.getText().toString();
            String venue = venue_textView.getText().toString();
            String[] foodArray = food.split(" ");
            List<String> foodList = Arrays.asList(foodArray);
            String[] serviceArray = services.split(" ");
            List<String> serviceList = Arrays.asList(serviceArray);
            Map packageData = new HashMap();
            packageData.put("PackageName", packageName);
            packageData.put("Services", serviceList);
            packageData.put("Food", foodList);
            packageData.put("Venue", venue);
            packageData.put("Price", price);
            firestore.collection("Users").document(currentuserID).collection("Packages").document().set(packageData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            FragmentManager fragmentManager = getParentFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.evntOrg_packages_frameLayout, new evntOrg_Packages())
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                            Log.d(TAG, "onSuccess: in evnt package create organization ");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Failed to create Package... Please Try again", Toast.LENGTH_SHORT).show();
                }
            });
        }*/


    }

    // check weather EditText is empty or not
    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() <= 0;
    }
}