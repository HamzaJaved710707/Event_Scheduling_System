package com.example.eventscheduling.eventorg.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.example.eventscheduling.R;
import com.example.eventscheduling.client.ui.client_home;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stepstone.apprating.AppRatingDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class evnt_profile_view extends Fragment implements View.OnClickListener, client_home.getRating, evntOrg_home.getRating_evntOrg {

    private static final String TAG = "evnt_profile_view";
    private ImageView friendImg;
    private ImageView msgImg;
    private ImageView portfolioImg;
    private MaterialTextView busniessTxt;
    private MaterialTextView businessCatTxt;
    private MaterialTextView emailTxt;
    private MaterialTextView numberTxt;
    private CircleImageView profileImg;
    private TextView nameTxt;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    private String currentUserId;
    private CollectionReference userCollection;
    private String remoteUserId;
    private CollectionReference friendCollection;
    private String id;
    private String backStackString;
    private long type;
    private String realUser;
    private int new_ratingStar;
    private long fiveStars;
    private long fourStars;
    private long threeStars;
    private long twoStars;
    private long oneStar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evnt_profile_view, container, false);
        setHasOptionsMenu(true);
        busniessTxt = view.findViewById(R.id.evnt_profile_view_about_businessNameTxt);
        businessCatTxt = view.findViewById(R.id.evnt_profile_view_about_businessCatTxt);
        emailTxt = view.findViewById(R.id.evnt_profile_view_about_business_emailTxt);
        numberTxt = view.findViewById(R.id.evnt_profile_view_about_business_numberTxt);
        portfolioImg = view.findViewById(R.id.evnt_profile_view_order_icon);
        portfolioImg.setOnClickListener(this);
        friendImg = view.findViewById(R.id.evnt_profile_view_followers_icon);
        friendImg.setOnClickListener(this);
        msgImg = view.findViewById(R.id.evnt_profile_view_msg_icon);
        msgImg.setOnClickListener(this);
        profileImg = view.findViewById(R.id.evnt_profile_view_pic);
        nameTxt = view.findViewById(R.id.evnt_profile_view_business_name);
        currentUser = mAuth.getCurrentUser();
        assert getArguments() != null;
        id = getArguments().getString("id");
        if (currentUser != null) {
            if (id == null) {
                currentUserId = currentUser.getUid();
                realUser = currentUser.getUid();
            } else {
                currentUserId = id;
                realUser = currentUser.getUid();
            }

            userCollection = firestore.collection("Users");
            friendCollection = userCollection.document(currentUserId).collection("Friends");
            LoadData(view);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.evnt_profile_view_order_icon:
                // Portfolio
                evntOrg_Portfolio obj = new evntOrg_Portfolio();
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putLong("type", type);
                obj.setArguments(bundle);
                if (type == 0) {
                    getParentFragmentManager().beginTransaction().replace(R.id.fragment_test_id, obj).addToBackStack(backStackString).commit();
                } else if (type == 1) {
                    getParentFragmentManager().beginTransaction().replace(R.id.frameLayout_clientHome, obj).addToBackStack(backStackString).commit();

                }
                break;
            case R.id.evnt_profile_view_followers_icon:
                // Friends
                AddFriend();
                break;
            case R.id.evnt_profile_view_msg_icon:
             Intent intent = new Intent(getContext(), evntOrg_MessageDetail.class);
             intent.putExtra("chatId",currentUserId);
             startActivity(intent);
                break;
            default:
                break;

        }
    }

    // Load data from firebase
    private void LoadData(View view) {
        userCollection.document(currentUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot2) {
                userCollection.document(realUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        busniessTxt.setText(documentSnapshot2.getString("businessName"));
                        businessCatTxt.setText(documentSnapshot2.getString("businessCat"));
                        emailTxt.setText(documentSnapshot2.getString("email"));
                        numberTxt.setText(documentSnapshot2.getString("mobileNumber"));
                        nameTxt.setText(documentSnapshot2.getString("Name"));
                        Glide.with(view).load(documentSnapshot2.getString("imgUrl")).into(profileImg);
                        type = documentSnapshot.getLong("type");
                        if (type == 1) {
                            client_home.ratingInterface(evnt_profile_view.this);
                        } else if (type == 0) {
                            evntOrg_home.ratingInterface_evntOrg(evnt_profile_view.this);
                        }
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
                .setTitle("Rate this service  provider")
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
                .setDefaultComment("No comments")
                .create((FragmentActivity) getContext())
                //  .setTargetFragment(this, TAG) // only if listener is implemented by fragment
                .show();

    }


    private void AddFriend() {

        friendCollection.document(currentUserId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Log.d(TAG, "onSuccess: document exist");
                            Toast.makeText(getContext(), "You are already friends", Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d(TAG, "onSuccess: does not exists");
                            Map value = new HashMap();
                            value.put("friends", true);
                            friendCollection.document(currentUserId).set(value).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(), "Added to friend List", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.getMessage());
            }
        });
    }

    @Override
    public void getRatingMethod(int rate, String comment) {
        Log.d(TAG, "getRatingMethod: from client" + comment);

        userCollection.document(currentUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.get("totalRating") != null && documentSnapshot.getLong("ratingStar") != null) {
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
                        new_ratingStar = (int) (((5 * fiveStars) + (4 * fourStars) + (3 * threeStars) + (2 * twoStars) + (oneStar)) / (oneStar+ twoStars + threeStars + fourStars + fiveStars));
                         addNewRatingData();
                            break;
                        case 2:
                            twoStars++;
                            new_ratingStar = (int) (((5 * fiveStars) + (4 * fourStars) + (3 * threeStars) + (2 * twoStars) + (oneStar)) / (oneStar+ twoStars + threeStars + fourStars + fiveStars));
                            addNewRatingData();
                            break;
                        case 3:
                            threeStars++;
                            new_ratingStar = (int) (((5 * fiveStars) + (4 * fourStars) + (3 * threeStars) + (2 * twoStars) + (oneStar)) / (oneStar+ twoStars + threeStars + fourStars + fiveStars));
                            addNewRatingData();
                            break;
                        case 4:
                            fourStars++;
                            new_ratingStar = (int) (((5 * fiveStars) + (4 * fourStars) + (3 * threeStars) + (2 * twoStars) + (oneStar)) / (oneStar+ twoStars + threeStars + fourStars + fiveStars));
                            addNewRatingData();
                            break;
                        case 5:
                            fiveStars++;
                            new_ratingStar = (int) (((5 * fiveStars) + (4 * fourStars) + (3 * threeStars) + (2 * twoStars) + (oneStar)) / (oneStar+ twoStars + threeStars + fourStars + fiveStars));
                            addNewRatingData();
                            break;
                        default:
                            break;
                    }
                    Log.d(TAG, String.valueOf(totalRating.get(1)));
                    //     long new_rateStar = ((ratingStar * totalRating) + rate) / (totalRating + 1);
        /*           totalRating = totalRating + rate;
                   Map ratingData = new HashMap();
                   ratingData.put("ratingStar", new_rateStar);
                   ratingData.put("totalRating", totalRating);
                   userCollection.document(currentUserId).update(ratingData).addOnCompleteListener(new OnCompleteListener() {
                       @Override
                       public void onComplete(@NonNull Task task) {
                           Map ratingData2 = new HashMap();
                           ratingData2.put("rating", rate);
                           ratingData2.put("comment", comment);
                           userCollection.document(currentUserId).collection("Rating").document(realUser).set(ratingData2);
                           userCollection.document(realUser).collection("Rating").document(currentUserId).set(ratingData2);
                       }
                   });*/
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
                    userCollection.document(currentUserId).update(data).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            Map ratingData2 = new HashMap();
                            ratingData2.put("rating", rate);
                            ratingData2.put("comment", comment);
                            userCollection.document(currentUserId).collection("Rating").document(realUser).set(ratingData2);
                            userCollection.document(realUser).collection("Rating").document(currentUserId).set(ratingData2);
                            Toast.makeText(getContext(), "Rated", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
private void addNewRatingData(){
    ArrayList<Long> totalRating = new ArrayList<>();
            totalRating.add(oneStar);
            totalRating.add(twoStars);
            totalRating.add(threeStars);
            totalRating.add(fourStars);
            totalRating.add(fiveStars);
            Map data = new HashMap();
            data.put("totalRating", totalRating);
            data.put("ratingStar", new_ratingStar);
            userCollection.document(currentUserId).update(data).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Toast.makeText(getContext(), "Rating", Toast.LENGTH_SHORT).show();
                }
            });

}
    @Override
    public void getRatingMethod_evntOrg(int rate, String comment) {
        Log.d(TAG, "getRatingMethod_evntOrg: " + comment);
    }
}