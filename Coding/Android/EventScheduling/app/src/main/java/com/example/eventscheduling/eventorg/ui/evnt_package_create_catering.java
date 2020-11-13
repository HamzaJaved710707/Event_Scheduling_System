package com.example.eventscheduling.eventorg.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.eventscheduling.R;
import com.example.eventscheduling.client.ui.client_packages_frag;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static android.media.tv.TvContract.AUTHORITY;


public class evnt_package_create_catering extends Fragment implements evntOrg_home.OnBackPressedListener {

    private static final int PIC_CAMERA_REQ = 1;
    private static final int GALLERY_REQ = 2;
    List<String> new_items_food = new ArrayList<>();
    List<String> new_items_service = new ArrayList<>();
    List<String> new_items_venue = new ArrayList<>();
    MaterialButton food_btn;
    private SharedPreferences sharedPreferences;
    private MaterialButton service_btn;
    private MaterialButton create_btn;
    private TextInputEditText price_edit;
    private TextInputEditText packageNameEdtXt;
    private String uri_download = "";
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
    private Uri imageUri = Uri.EMPTY;
    private SharedPreferences.Editor preferencesEditor;
    private Boolean create_bol = false;
    private String doc_ref = "";
    private StorageReference img_ref;
    private ProgressBar progressBar;
    private MaterialButton venueBtn;
    private Dialog mOverlayDialog;
    private DocumentReference foodCollection;
    private DocumentReference serviceCollection;
    private DocumentReference venueCollection;

   private ArrayList<String> service = new ArrayList<>();
   private ArrayList<String> venue = new ArrayList<>();
   private ArrayList<String> food = new ArrayList<>();

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

// Select food item from list
  /*      food_multiValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == SpannedLength - chipLength)
                {
                    SpannedLength = s.length();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                chip.setText(s.subSequence(SpannedLength, s.length()));
                Log.d(TAG, "afterTextChanged: " + s.subSequence(SpannedLength, s.length()));
                chip.setBounds(0, 0, chip.getIntrinsicWidth(), chip.getIntrinsicHeight());
                ImageSpan span = new ImageSpan(chip);
                s.setSpan(span, SpannedLength, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannedLength = s.length();
                chip.setCloseIconResource(R.drawable.ic_baseline_close_24);
                Log.d(TAG, "afterTextChanged: " + s.length());
            }
        });*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (sharedPreferences != null) {
            if (imageUri.equals(Uri.EMPTY)) {
                sharedPreferences.getString("URI", imageUri.toString());
                Log.d(TAG, "onCreate: " + imageUri);

            }
        }


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String imageStringUri = imageUri.toString();
        Log.d(TAG, "onSaveInstanceState: " + imageUri);

        outState.putString("imageUri", imageStringUri);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            String imageStringUri = savedInstanceState.getString("imageUri");
            imageUri = Uri.parse(imageStringUri);
            Log.d(TAG, "onViewStateRestored: " + imageUri);
            Glide.with(evnt_package_create_catering.this).load(imageUri).into(imageView);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            String imageStringUri = savedInstanceState.getString("imageUri");
            imageUri = Uri.parse(imageStringUri);
            Glide.with(evnt_package_create_catering.this).load(imageUri).into(imageView);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = view.getContext().getSharedPreferences("MY_SHARE", MODE_PRIVATE);
        preferencesEditor = sharedPreferences.edit();
        progressBar.setVisibility(View.VISIBLE);
        mOverlayDialog = new Dialog(view.getContext(), android.R.style.Theme_Panel); //display an invisible overlay dialog to prevent user interaction and pressing back
        mOverlayDialog.setCancelable(false);
        mOverlayDialog.show();

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        if (imageUri.equals(Uri.EMPTY)) {

            preferencesEditor.putString("URI", imageUri.toString());
            Log.d(TAG, "onpause value: " + imageUri.toString());
            preferencesEditor.commit();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        if (sharedPreferences != null) {
            String temp = "";
            String temp2 = "";
            temp = sharedPreferences.getString("URI", "");
            temp2 = sharedPreferences.getString("img_ref", "");

            Log.d(TAG, "onstart value: " + temp);
            imageUri = Uri.parse(temp);


        }
        if (!uri_download.equals("")) {
            Glide.with(evnt_package_create_catering.this).load(uri_download).into(imageView);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evnt_package_create_catering, container, false);
        food_btn = view.findViewById(R.id.food_btn_package_evnt_create_catering);
        service_btn = view.findViewById(R.id.service_btn_package_evnt_create_caterer);
        create_btn = view.findViewById(R.id.evnt_package_creat_btn_caterer);
        imageView = view.findViewById(R.id.profilePic_create_catering);
        packageNameEdtXt = view.findViewById(R.id.evnt_package_Name_caterer_inputField);
        price_edit = view.findViewById(R.id.evntOrg_create_package_price);
        progressBar = view.findViewById(R.id.evnt_create_package_progressBar);
        venueBtn = view.findViewById(R.id.venue_btn_package_evnt_create_catering);

        registerForContextMenu(imageView);

        ///Restore data
        if (savedInstanceState != null) {
            String imageStringUri = savedInstanceState.getString("imageUri");
            imageUri = Uri.parse(imageStringUri);
        }
        // Firebase Initialization
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserID = currentUser.getUid();
            firestore = FirebaseFirestore.getInstance();
            firebaseStorage = FirebaseStorage.getInstance();
            package_img_Ref = firebaseStorage.getReference("Event_Org/" + currentUserID + "/Packages_Photos");
            packageReference = firestore.collection("Users").document(currentUserID).collection("Packages");
            checkCategory(view);
            foodCollection = firestore.collection("Users").document(currentUserID).collection("Resources").document("1");
            serviceCollection = firestore.collection("Users").document(currentUserID).collection("Resources").document("2");
            venueCollection = firestore.collection("Users").document(currentUserID).collection("Resources").document("3");
            create_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    create_bol = true;
                    savePackage();
                }
            });
        }
        food_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new_items_food.clear();
                foodCollection.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ArrayList<String> food = (ArrayList<String>) documentSnapshot.get("food");

                        if(food != null){
                            String[] arr = new String[food.size()];
                            for(int i=0 ; i< food.size();i++){
                                arr[i] = food.get(i);
                                //getProductName or any suitable method
                            }
                            AlertDialog.Builder builder =
                                    new AlertDialog.Builder(getActivity());


                            ArrayList<String> finalFood1 = food;
                            builder.setTitle("Select")
                                    .setMultiChoiceItems(arr, null,
                                            new DialogInterface.OnMultiChoiceClickListener() {
                                                public void onClick(DialogInterface dialog, int item, boolean isChecked) {

                                                    if (isChecked) {
                                                        new_items_food.add(arr[item]);
                                                    }


                                                }
                                            }).setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Log.d("EVent Packages", "onClick: " + new_items_food);
                                }
                            }).setNegativeButton("Add new Item", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setTitle("Add Food Item");

                                    // Set up the input
                                    final EditText input = new EditText(getContext());
                                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                                    builder.setView(input);

                                    // Set up the buttons
                                    builder.setPositiveButton("OK", (dialog1, which1) -> {

                                        m_Text = input.getText().toString();
                                        finalFood1.add(m_Text);

                                        foodCollection.update("food", finalFood1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "Sucessfully added");
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d(TAG,e.getMessage());
                                            }
                                        });
                                    });
                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                                    builder.show();

                                }
                            });


                            builder.create();
                            builder.show();
                        }else{
                            food = new ArrayList<>();
                            String[] arr = {""};
                            AlertDialog.Builder builder =
                                    new AlertDialog.Builder(getActivity());


                            ArrayList<String> finalFood = food;
                            builder.setTitle("Select")
                                    .setMultiChoiceItems(arr, null,
                                            new DialogInterface.OnMultiChoiceClickListener() {
                                                public void onClick(DialogInterface dialog, int item, boolean isChecked) {

                                                    if (isChecked) {
                                                        new_items_food.add(arr[item]);
                                                    }


                                                }
                                            }).setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Log.d("EVent Packages", "onClick: " + new_items_food);
                                }
                            }).setNegativeButton("Add new Item", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setTitle("Add Food Item");

                                    // Set up the input
                                    final EditText input = new EditText(getContext());
                                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                                    input.setInputType(InputType.TYPE_CLASS_TEXT );
                                    builder.setView(input);

                                    // Set up the buttons
                                    builder.setPositiveButton("OK", (dialog1, which1) -> {

                                        m_Text = input.getText().toString();
                                        finalFood.add(m_Text);
                                        Map foodData = new HashMap();
                                        foodData.put("food", finalFood);
                                        foodCollection.set(foodData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "onSuccess: ");
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d(TAG, e.getMessage());
                                            }
                                        });
                                    });
                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                                    builder.show();

                                }
                            });


                            builder.create();
                            builder.show();
                        }
                    }
                });

            }


        });
        venueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                venueCollection.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ArrayList<String> venue = (ArrayList<String>) documentSnapshot.get("venue");
                        if (venue != null) {
                        String[] arr = new String[venue.size()];
                        for (int i = 0; i < venue.size(); i++) {
                            arr[i] = venue.get(i);
                            //getProductName or any suitable method
                        }


                            AlertDialog.Builder builder =
                                    new AlertDialog.Builder(getActivity());


                            ArrayList<String> finalVenue1 = venue;
                            builder.setTitle("Select Venue")
                                    .setMultiChoiceItems(arr, null,
                                            new DialogInterface.OnMultiChoiceClickListener() {
                                                public void onClick(DialogInterface dialog, int item, boolean isChecked) {
                                                    Log.i("Dialogos", "Opción elegida: " + arr[item]);
                                                    if (isChecked) {
                                                        new_items_venue.add(arr[item]);
                                                    }


                                                }
                                            }).setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Log.d("EVent Packages", "onClick: " + new_items_service);
                                }
                            }).setNegativeButton("Add new Venue", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setTitle("Select Venue");

                                    // Set up the input
                                    final EditText input = new EditText(getContext());
                                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                                    input.setInputType(InputType.TYPE_CLASS_TEXT );
                                    builder.setView(input);

                                    // Set up the buttons
                                    builder.setPositiveButton("OK", (dialog12, which12) -> {

                                        m_Text = input.getText().toString();
                                       new_items_venue.add(m_Text);

                                       venueCollection.update("venue",new_items_venue);
                                    });
                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                                    builder.show();

                                }
                            });


                            builder.create();
                            builder.show();
                        }else{
                            venue = new ArrayList<>();
                            String[] arr = {""};
                            AlertDialog.Builder builder =
                                    new AlertDialog.Builder(getActivity());


                            ArrayList<String> finalVenue = venue;
                            builder.setTitle("Select Venue")
                                    .setMultiChoiceItems(arr, null,
                                            new DialogInterface.OnMultiChoiceClickListener() {
                                                public void onClick(DialogInterface dialog, int item, boolean isChecked) {
                                                    Log.i("Dialogos", "Opción elegida: " + arr[item]);
                                                    if (isChecked) {
                                                        new_items_venue.add(arr[item]);
                                                    }


                                                }
                                            }).setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Log.d("EVent Packages", "onClick: " + new_items_service);
                                }
                            }).setNegativeButton("Add new Venue", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setTitle("Select Venue");

                                    // Set up the input
                                    final EditText input = new EditText(getContext());
                                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                                    input.setInputType(InputType.TYPE_CLASS_TEXT );
                                    builder.setView(input);

                                    // Set up the buttons
                                    builder.setPositiveButton("OK", (dialog12, which12) -> {

                                        m_Text = input.getText().toString();
                                        finalVenue.add(m_Text);
                                        Map foodData = new HashMap();
                                        foodData.put("venue", finalVenue);
                                       venueCollection.set(foodData);
                                    });
                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                                    builder.show();

                                }
                            });


                            builder.create();
                            builder.show();
                        }


                    }    });
            }
        });

        /// set click listener for service button
        service_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               serviceCollection.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        service = (ArrayList<String>) documentSnapshot.get("service");
                        if ( service!= null && service.size() != 0){
                            String[] arr = new String[service.size()];
                        for (int i = 0; i < service.size(); i++) {
                            arr[i] = service.get(i);
                            //getProductName or any suitable method
                        }
                        if (service != null) {

                            AlertDialog.Builder builder =
                                    new AlertDialog.Builder(getActivity());


                            builder.setTitle("Select Service")
                                    .setMultiChoiceItems(arr, null,
                                            new DialogInterface.OnMultiChoiceClickListener() {
                                                public void onClick(DialogInterface dialog, int item, boolean isChecked) {
                                                    Log.i("Dialogos", "Opción elegida: " + arr[item]);
                                                    if (isChecked) {
                                                        new_items_service.add(arr[item]);
                                                    }


                                                }
                                            }).setPositiveButton("Done", (dialog, which) -> {
                                                dialog.dismiss();
                                                Log.d("EVent Packages", "onClick: " + new_items_service);
                                            }).setNegativeButton("Add new Service", (dialog, which) -> {
                                                dialog.dismiss();
                                                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                                                builder1.setTitle("Select Service");

                                                // Set up the input
                                                final EditText input = new EditText(getContext());
                                                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                                                input.setInputType(InputType.TYPE_CLASS_TEXT );
                                                builder1.setView(input);

                                                // Set up the buttons
                                                builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        m_Text = input.getText().toString();
                                                    service.add(m_Text);

                                                   serviceCollection.update("service", service);
                                                    }
                                                });
                                                builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.cancel();
                                                    }
                                                });

                                                builder1.show();

                                            });


                            builder.create();
                            builder.show();
                        }


                    }else{
service = new ArrayList<>();
 String[] arr = {""};
                            AlertDialog.Builder builder =
                                    new AlertDialog.Builder(getActivity());


                            builder.setTitle("Select Service")
                                    .setMultiChoiceItems(arr, null,
                                            new DialogInterface.OnMultiChoiceClickListener() {
                                                public void onClick(DialogInterface dialog, int item, boolean isChecked) {
                                                    Log.i("Dialogos", "Opción elegida: " + arr[item]);
                                                    if (isChecked) {
                                                        new_items_service.add(arr[item]);
                                                    }


                                                }
                                            }).setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Log.d("EVent Packages", "onClick: " + new_items_service);
                                }
                            }).setNegativeButton("Add new Service", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setTitle("Select Service");

                                    // Set up the input
                                    final EditText input = new EditText(getContext());
                                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                                    input.setInputType(InputType.TYPE_CLASS_TEXT );
                                    builder.setView(input);

                                    // Set up the buttons
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            m_Text = input.getText().toString();
service.add(m_Text);   Map foodData = new HashMap();
                                            foodData.put("service", service);
serviceCollection.set(foodData);
                                        }
                                    });
                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                                    builder.show();

                                }
                            });


                            builder.create();
                            builder.show();
                        } }    });

            }


        });

        /// return view of the layout
        return view;
    }

    private void checkCategory(View view) {
        String value;
        firestore.collection("Users").document(currentUserID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String value = documentSnapshot.getString("businessCat");
                if (value != null) {
                    switch (value) {

                        case "Event_Organizer":
                            progressBar.setVisibility(View.INVISIBLE);
mOverlayDialog.dismiss();
                            break;
                        case "Caterer":
                            progressBar.setVisibility(View.INVISIBLE);
                            mOverlayDialog.dismiss();

                            venueBtn.setVisibility(View.GONE);
                            break;
                        case "Venue_Provider":
                            food_btn.setVisibility(View.GONE);
                            progressBar.setVisibility(View.INVISIBLE);
                            mOverlayDialog.dismiss();

                            break;
                        case "Decoration":
                            food_btn.setVisibility(View.GONE);
                            progressBar.setVisibility(View.INVISIBLE);
                            mOverlayDialog.dismiss();

                            venueBtn.setVisibility(View.GONE);
                            break;
                        case "Car_Rent":
                            venueBtn.setVisibility(View.GONE);
                            progressBar.setVisibility(View.INVISIBLE);
                            mOverlayDialog.dismiss();

                            food_btn.setVisibility(View.GONE);

                            break;
                        case "Invitation_Card":
                            venueBtn.setVisibility(View.GONE);
                            progressBar.setVisibility(View.INVISIBLE);
                            mOverlayDialog.dismiss();

                            food_btn.setVisibility(View.GONE);
                        default:
                            break;


                    }
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    mOverlayDialog.dismiss();

                    mAuth.signOut();
                    startActivity(new Intent(view.getContext(), evntOrg_signIn.class));
                }
            }
        });


    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        if (v.getId() == R.id.profilePic_create_catering) {
            inflater.inflate(R.menu.evnt_profile_pic, menu);
        }

    }

    /// Logic to handle Back button trigger...
    protected void exitByBackKey() {

        android.app.AlertDialog alertbox = new android.app.AlertDialog.Builder(getContext())
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {

                        //finish();
                        //close();


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (imageUri.equals(Uri.EMPTY)) {

            preferencesEditor.putString("URI", imageUri.toString());
            Log.d(TAG, "ondestroy value: " + imageUri.toString());
            preferencesEditor.commit();
        }
        if (img_ref != null) {

            img_ref.delete();
            Log.d(TAG, "OnDestroy: ");
        }
        /*if(!create_bol){
            img_ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    packageReference.document(doc_ref).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if ((create_bol == false) && (img_ref != null)) {

            img_ref.delete();
            Log.d(TAG, "onDetach: ");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == PIC_CAMERA_REQ || requestCode == GALLERY_REQ) && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            // Creating the reference of the image
            saveImage();
            Log.d(TAG, "onActivityResult: " + imageUri);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

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

    // Save the values of the package
    private void savePackage() {
        if (!packageNameEdtXt.getText().toString().trim().equals("") && !price_edit.getText().toString().trim().equals("")) {
            progressBar.setVisibility(View.VISIBLE);
            mOverlayDialog.show();
            firestore.collection("Users").document(currentUserID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String PackageName = packageNameEdtXt.getText().toString().trim();
                    String price = price_edit.getText().toString().trim();
                    uri_download = sharedPreferences.getString("uri_download", uri_download);
                    String businessName = documentSnapshot.getString("businessName");
                    Map data = new HashMap();
                    data.clear();

                    data.put("PackageName", PackageName);

                    packageReference.add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getContext(), "Package Successfully Created", Toast.LENGTH_SHORT).show();
                            data.clear();
                            doc_ref = documentReference.getId();
                            data.put("image", uri_download);
                            data.put("Services", new_items_service);
                            data.put("Food", new_items_food);
                            data.put("price", price);
                            data.put("businessName", businessName);
                            data.put("id", doc_ref);
                            data.put("custom", false);
                            data.put("userId", currentUserID);
                            data.put("venue",new_items_venue);
                            progressBar.setVisibility(View.INVISIBLE);
                            mOverlayDialog.dismiss();
                            packageReference.document(documentReference.getId()).update(data).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    Toast.makeText(getContext(), "Package Created Successfully", Toast.LENGTH_SHORT).show();
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            getParentFragmentManager().beginTransaction().replace(R.id.fragment_test_id, new client_packages_frag()).addToBackStack(null).commit();
                                        }
                                    },2000);
                                }
                            });

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

                }
            });
        }

    }

    private void saveImage() {
        Log.d(TAG, "saveImage: " + imageUri);
        progressBar.setVisibility(View.VISIBLE);
        mOverlayDialog.show();
        package_img_Ref.child((System.currentTimeMillis() + "." + getMimeType(imageUri))).putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d(TAG, "onSuccess: " + uri);
                                uri_download = uri.toString();

                                //     Glide.with(getContext()).load(uri).into(imageView);
                                img_ref = taskSnapshot.getStorage();
                                preferencesEditor.putString("uri_download", uri_download);

                                preferencesEditor.apply();
                                Log.d(TAG, "Image Storage Reference" + img_ref);
                                progressBar.setVisibility(View.INVISIBLE);
                                mOverlayDialog.dismiss();

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

    @Override
    public void doBack() {
        exitByBackKey();
    }
}