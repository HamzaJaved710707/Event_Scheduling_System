package com.example.eventscheduling.client.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventscheduling.R;
import com.example.eventscheduling.client.model.client_package_detail_adapter;
import com.example.eventscheduling.eventorg.ui.evntOrg_home;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stepstone.apprating.AppRatingDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class client_package_detail_default extends Fragment implements DatePickerDialog.OnDateSetListener, client_home.getRating, evntOrg_home.getRating_evntOrg
        , View.OnClickListener {
    private static final String TAG = "package_detail";
    client_package_detail_adapter food_detail_adapter;
    client_package_detail_adapter service_detail_adapter;
    client_package_detail_adapter venue_detail_adapter;

    List<String> foodList = new ArrayList<>();
    List<String> serviceList = new ArrayList<>();
    List<String> venueList = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference dbReference = firebaseFirestore.collection("Users");
    private DocumentReference customCollection;


    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    private String currentUserID;
    private String packageUser;
    private RecyclerView food_recyclerView;
    private RecyclerView service_recyclerView;
    private RecyclerView venue_recyclerview;
    private String packageId;

    private MaterialButton sendButton;
    private MaterialButton datePickerBtn;
    private MaterialButton cancelBtn;

    private String date;
    private CircleImageView packageImg;
    private TextView packageName;
    private TextView priceTxt;
    private SimpleDateFormat sdformat;
    private Calendar myCalendar;
    private DatePickerDialog datePickerDialog;
    private boolean dateChecker = false;
    private MaterialTextView empty_foodTxtView;
    private MaterialTextView empty_service_TextView;
    private MaterialTextView empty_venueTxtView;
    private boolean evnt = false;
    private int new_ratingStar;
    private long fiveStars;
    private long fourStars;
    private long threeStars;
    private long twoStars;
    private long oneStar;
    private ImageView foodArrow;
    private ImageView serviceArrow;
    private ImageView venueArrow;
    private RatingBar ratingBar;
    private String orderId;
    private String from_id;
    private boolean orders = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_package_detail_default, container, false);


        packageId = getArguments().getString("packageId");
        packageUser = getArguments().getString("userId");
        evnt = getArguments().getBoolean("evnt");
        orders = getArguments().getBoolean("orders");
        orderId = getArguments().getString("orderId");
        datePickerBtn = view.findViewById(R.id.datePicker_client_package_detail);
        cancelBtn = view.findViewById(R.id.client_package_default_detail_cancel_btn);
        myCalendar = Calendar.getInstance();
        setHasOptionsMenu(true);
        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(view.getContext(), client_package_detail_default.this, 2020, 9, 20);
                datePickerDialog.show();

            }
        });
        ratingBar = view.findViewById(R.id.client_package_default_ratingbar);
        food_recyclerView = view.findViewById(R.id.client_package_detail_default_food_recyc);
        service_recyclerView = view.findViewById(R.id.client_package_detail_default_service_recyc);
        venue_recyclerview = view.findViewById(R.id.client_package_detail_default_venue_recyc);
        packageImg = view.findViewById(R.id.client_packageDetail_default_img);
        packageName = view.findViewById(R.id.client_packagedetail_default_name);
        priceTxt = view.findViewById(R.id.price_txt_detail);
        empty_foodTxtView = view.findViewById(R.id.client_package_detail_custom_food_empty_default);
        empty_service_TextView = view.findViewById(R.id.client_package_detail_custom_service_empty_default);
        empty_venueTxtView = view.findViewById(R.id.client_package_detail_custom_venue_empty_default);
        foodArrow = view.findViewById(R.id.package_default_food_arrow);
        serviceArrow = view.findViewById(R.id.package_default_service_arrow);
        venueArrow = view.findViewById(R.id.package_default_venue_arrow);
        foodArrow.setOnClickListener(this);
        serviceArrow.setOnClickListener(this);
        venueArrow.setOnClickListener(this);

        setHasOptionsMenu(true);
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserID = currentUser.getUid();

            customCollection = dbReference.document(packageUser).collection("Packages").document(packageId);

            initialize_RecyclerView();


        }
        sendButton = view.findViewById(R.id.client_package_default_detail_send_btn);

        if (evnt) {
            evntOrg_home.ratingInterface_evntOrg(client_package_detail_default.this);

        } else {
            client_home.ratingInterface(client_package_detail_default.this);
        }
        if (evnt) {
            sendButton.setVisibility(View.GONE);
            datePickerBtn.setVisibility(View.GONE);
            cancelBtn.setVisibility(View.GONE);
        }
        if (orders && evnt) {
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  firebaseFirestore.collection("Users").document(packageUser).collection("Orders").document(orderId).update("status", 1)
                          .addOnSuccessListener(new OnSuccessListener<Void>() {
                              @Override
                              public void onSuccess(Void aVoid) {
                                 firebaseFirestore.collection("Users").document(from_id).collection("Orders").document(orderId).update("status", 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                     @Override
                                     public void onSuccess(Void aVoid) {
                                         Log.d(TAG, "onSuccess: ");
                                     }
                                 }).addOnFailureListener(new OnFailureListener() {
                                     @Override
                                     public void onFailure(@NonNull Exception e) {
                                         Log.d(TAG, "onFailure: ");
                                     }
                                 });
                              }
                          }).addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                          Toast.makeText(getContext(), "Error while accepting order", Toast.LENGTH_SHORT).show();
                      }
                  });
                }
            });
            sendButton.setVisibility(View.VISIBLE);
            sendButton.setText("Accept Order");
            datePickerBtn.setVisibility(View.GONE);
            cancelBtn.setVisibility(View.GONE);
        }
        else if(orders){
            sendButton.setVisibility(View.GONE);
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        else {
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dateChecker) {
                        Bundle bundle = new Bundle();

                            bundle.putString("packageId", packageId);
                            bundle.putString("date", date);
                            bundle.putBoolean("isDefault", true);
                            bundle.putString("packageUser", packageUser);
                            client_package_send_order frag = new client_package_send_order();
                            frag.setArguments(bundle);
                            if (evnt == false) {
                                getParentFragmentManager().beginTransaction().replace(R.id.frameLayout_clientHome, frag)
                                        .addToBackStack(null).commit();
                            } else {
                                getParentFragmentManager().beginTransaction().replace(R.id.fragment_test_id, frag)
                                        .addToBackStack(null).commit();
                            }


                    } else {
                        Toast.makeText(getContext(), "Please select date for order", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        return view;

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.rating_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.evnt_rating_star:
                openRatingDialog();
                break;
            default:
                break;
        }
        return true;
    }

    private void openRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNeutralButtonText("Later")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"))
                .setDefaultRating(2)
                .setTitle("Rate this package")
                .setDescription("Please select some stars and give your feedback")
                .setCommentInputEnabled(true)
                .setStarColor(R.color.starColor)
                .setNoteDescriptionTextColor(R.color.noteDescriptionTextColor)
                .setTitleTextColor(R.color.titleTextColor)
                .setDescriptionTextColor(R.color.secondarycolor)
                .setHint("Please write your comment here ...")
                .setHintTextColor(R.color.secondarycolor)
                .setCommentTextColor(R.color.commentTextColor)
                .setCommentBackgroundColor(R.color.colorPrimary)
                .setWindowAnimation(R.style.MyDialogFadeAnimation)
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .create((FragmentActivity) getContext())
                //  .setTargetFragment(this, TAG) // only if listener is implemented by fragment
                .show();
    }

    private void initialize_RecyclerView() {

        customCollection.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                foodList = (ArrayList<String>) documentSnapshot.get("Food");
                serviceList = (ArrayList<String>) documentSnapshot.get("Services");
                venueList = (ArrayList<String>) documentSnapshot.get("venue");
                if (documentSnapshot.getString("image") == null) {
                       Glide.with(getContext()).load(R.mipmap.package_icon).into(packageImg);
                } else {
                    Glide.with(getContext()).load(documentSnapshot.getString("image")).into(packageImg);

                }
                packageName.setText(documentSnapshot.getString("PackageName"));
                priceTxt.setText(documentSnapshot.getString("price"));
                if (documentSnapshot.getLong("ratingStar") != null) {
                    ratingBar.setRating(documentSnapshot.getLong("ratingStar"));
                } else {
                    ratingBar.setRating(0);
                }

                if (foodList == null) {
                    empty_foodTxtView.setVisibility(View.VISIBLE);
                }
                if (serviceList == null) {
                    empty_service_TextView.setVisibility(View.VISIBLE);
                }
                if (venueList == null) {
                    empty_venueTxtView.setVisibility(View.VISIBLE);
                }
                food_detail_adapter = new client_package_detail_adapter(getContext(), foodList);
                service_detail_adapter = new client_package_detail_adapter(getContext(), serviceList);
                venue_detail_adapter = new client_package_detail_adapter(getContext(), venueList);
                food_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                food_recyclerView.setAdapter(food_detail_adapter);
                service_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                service_recyclerView.setAdapter(service_detail_adapter);
                  venue_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                  venue_recyclerview.setAdapter(venue_detail_adapter);

            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String myFormat = "MMM dd, yyyy"; //In which you need put here
        sdformat = new SimpleDateFormat(myFormat, Locale.US);
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        Log.d(TAG, "onDateSet: " + sdformat.format(myCalendar.getTime()));
        date = sdformat.format(myCalendar.getTime());
        dateChecker = true;
    }

    @Override
    public void getRatingMethod(int rate, String comment) {
        setRating(rate);
    }

    @Override
    public void getRatingMethod_evntOrg(int rate, String comment) {
        setRating(rate);
    }

    private void setRating(int rate) {
        customCollection.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getLong("ratingStar") != null && documentSnapshot.get("totalRating") != null) {

                    ArrayList<Long> totalRating = (ArrayList<Long>) documentSnapshot.get("totalRating");
                    fiveStars = (Long) totalRating.get(4);
                    fourStars = (Long) totalRating.get(3);
                    threeStars = (Long) totalRating.get(2);
                    twoStars = (Long) totalRating.get(1);
                    oneStar = (Long) totalRating.get(0);
                    Log.d(TAG, String.valueOf(twoStars));
                    long ratingStar = documentSnapshot.getLong("ratingStar");
                    switch (rate) {
                        case 1:
                            oneStar++;
                            new_ratingStar = (int) (((5 * fiveStars) + (4 * fourStars) + (3 * threeStars) + (2 * twoStars) + (oneStar)) / (oneStar + twoStars + threeStars + fourStars + fiveStars));
                            addNewRatingData();
                            break;
                        case 2:
                            twoStars++;
                            new_ratingStar = (int) (((5 * fiveStars) + (4 * fourStars) + (3 * threeStars) + (2 * twoStars) + (oneStar)) / (oneStar + twoStars + threeStars + fourStars + fiveStars));
                            addNewRatingData();
                            break;
                        case 3:
                            threeStars++;
                            new_ratingStar = (int) (((5 * fiveStars) + (4 * fourStars) + (3 * threeStars) + (2 * twoStars) + (oneStar)) / (oneStar + twoStars + threeStars + fourStars + fiveStars));
                            addNewRatingData();
                            break;
                        case 4:
                            fourStars++;
                            new_ratingStar = (int) (((5 * fiveStars) + (4 * fourStars) + (3 * threeStars) + (2 * twoStars) + (oneStar)) / (oneStar + twoStars + threeStars + fourStars + fiveStars));
                            addNewRatingData();
                            break;
                        case 5:
                            fiveStars++;
                            new_ratingStar = (int) (((5 * fiveStars) + (4 * fourStars) + (3 * threeStars) + (2 * twoStars) + (oneStar)) / (oneStar + twoStars + threeStars + fourStars + fiveStars));
                            addNewRatingData();
                            break;
                        default:
                            break;
                    }
                } else {

                    Map data = new HashMap();
                    long ratingStar = 0;
                    ArrayList<Long> add = new ArrayList();
                    switch (rate) {
                        case 1:
                            add.add((long) 1);
                            add.add((long) 0);
                            add.add((long) 0);
                            add.add((long) 0);
                            add.add((long) 0);
                            break;
                        case 2:
                            add.add((long) 0);
                            add.add((long) 1);
                            add.add((long) 0);
                            add.add((long) 0);
                            add.add((long) 0);
                            break;
                        case 3:
                            add.add((long) 0);
                            add.add((long) 0);
                            add.add((long) 1);
                            add.add((long) 0);
                            add.add((long) 0);
                            break;
                        case 4:
                            add.add((long) 0);
                            add.add((long) 0);
                            add.add((long) 0);
                            add.add((long) 1);
                            add.add((long) 0);

                            break;
                        case 5:
                            add.add((long) 0);
                            add.add((long) 0);
                            add.add((long) 0);
                            add.add((long) 0);
                            add.add((long) 1);
                            break;
                        default:
                            break;
                    }
                    data.put("totalRating", add);
                    data.put("ratingStar", rate);
                    customCollection.update(data).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            Toast.makeText(getContext(), "Rated", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }

    private void addNewRatingData() {
        ArrayList<Long> totalRating = new ArrayList<>();
        totalRating.add(oneStar);
        totalRating.add(twoStars);
        totalRating.add(threeStars);
        totalRating.add(fourStars);
        totalRating.add(fiveStars);
        Map data = new HashMap();
        data.put("totalRating", totalRating);
        data.put("ratingStar", new_ratingStar);
        customCollection.update(data).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(getContext(), "Rating", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.package_default_food_arrow:
                if (food_recyclerView.getVisibility() == View.VISIBLE) {
                    food_recyclerView.setVisibility(View.GONE);
                    empty_foodTxtView.setVisibility(View.GONE);

                } else {
                    food_recyclerView.setVisibility(View.VISIBLE);
                    if (foodList == null) {
                        empty_foodTxtView.setVisibility(View.VISIBLE);
                    } else {
                        empty_foodTxtView.setVisibility(View.GONE);
                    }

                }
                break;
            case R.id.package_default_service_arrow:
                if (service_recyclerView.getVisibility() == View.VISIBLE) {
                    service_recyclerView.setVisibility(View.GONE);
                    empty_service_TextView.setVisibility(View.GONE);
                } else {
                    service_recyclerView.setVisibility(View.VISIBLE);
                    if (serviceList == null) {
                        empty_service_TextView.setVisibility(View.VISIBLE);
                    } else {
                        empty_service_TextView.setVisibility(View.GONE);
                    }

                }
                break;
            case R.id.package_default_venue_arrow:
                if (venue_recyclerview.getVisibility() == View.VISIBLE) {
                    venue_recyclerview.setVisibility(View.GONE);
                    empty_venueTxtView.setVisibility(View.GONE);
                } else {

                    venue_recyclerview.setVisibility(View.VISIBLE);
                    if (venueList != null) {
                        empty_venueTxtView.setVisibility(View.GONE);

                    } else {
                        empty_venueTxtView.setVisibility(View.VISIBLE);
                    }
                }
                break;
            default:
                break;

        }
    }
}