package com.example.eventscheduling.client.ui;



import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eventscheduling.R;
import com.example.eventscheduling.client.model.Adapter_history;
import com.example.eventscheduling.client.util.history_values;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.stepstone.apprating.AppRatingDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class client_history extends Fragment implements  client_home.getRating, Adapter_history.history_adapter_interface {

    private static final String TAG = "client_history";


    private RecyclerView recyclerView;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    private String currentUserId;
    private CollectionReference userReference;
    private ArrayList<String> userIds = new ArrayList<>();
    private ArrayList<history_values> history_values_list = new ArrayList<>();
private String remoteUser;
    private long fiveStars = 0;
    private long fourStars = 0;
    private long threeStars = 0;
    private long twoStars = 0;
    private long oneStar= 0;
private int new_ratingStar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_history, container, false);
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUserId = currentUser.getUid();
            userReference = firestore.collection("Users");
            initRecyclerView(view);
        }
        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }



    public void initValues(View view){
        Log.d(TAG, "initImages: is called");


    }

    public void initRecyclerView(View view){
        userIds.clear();
     userReference.document(currentUserId).collection("Rating").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
         @Override
         public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
             for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                 userIds.add(documentSnapshot.getId());
             }
             if (userIds.size() != 0) {
                 userReference.whereIn(FieldPath.documentId(), userIds).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                     @Override
                     public void onSuccess(QuerySnapshot queryDocumentSnapshots2) {
                         for (QueryDocumentSnapshot documentSnapshot2 : queryDocumentSnapshots2) {
                             history_values_list.add(new history_values(documentSnapshot2.getString("imgUrl"), documentSnapshot2.getString("Name"), documentSnapshot2.getLong("ratingStar"), documentSnapshot2.getString("id")));
                         }
                         recyclerView = view.findViewById(R.id.client_history_recyclerView);
                         Adapter_history adapter_history = new Adapter_history(history_values_list, view.getContext());
                         adapter_history.setHistoryAdapterInterface(client_history.this);
                         recyclerView.setAdapter(adapter_history);

                         recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                     }
                 }).addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {

                     }
                 });
             }
         }
     });

;
    }


    @Override
    public void getRatingMethod(int rate, String comment) {
        if(remoteUser != null) {

            userReference.document(currentUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
                                addNewRatingData(rate, comment);
                                break;
                            case 2:
                                twoStars++;
                                new_ratingStar = (int) (((5 * fiveStars) + (4 * fourStars) + (3 * threeStars) + (2 * twoStars) + (oneStar)) / (oneStar+ twoStars + threeStars + fourStars + fiveStars));
                                addNewRatingData(rate, comment);
                                break;
                            case 3:
                                threeStars++;
                                new_ratingStar = (int) (((5 * fiveStars) + (4 * fourStars) + (3 * threeStars) + (2 * twoStars) + (oneStar)) / (oneStar+ twoStars + threeStars + fourStars + fiveStars));
                                addNewRatingData(rate, comment);

                                break;
                            case 4:
                                fourStars++;
                                new_ratingStar = (int) (((5 * fiveStars) + (4 * fourStars) + (3 * threeStars) + (2 * twoStars) + (oneStar)) / (oneStar+ twoStars + threeStars + fourStars + fiveStars));
                                addNewRatingData(rate, comment);

                                break;
                            case 5:
                                fiveStars++;
                                new_ratingStar = (int) (((5 * fiveStars) + (4 * fourStars) + (3 * threeStars) + (2 * twoStars) + (oneStar)) / (oneStar+ twoStars + threeStars + fourStars + fiveStars));
                                addNewRatingData(rate, comment);

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
                        userReference.document(currentUserId).update(data).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Map ratingData2 = new HashMap();
                                ratingData2.put("rating", rate);
                                ratingData2.put("comment", comment);
                                userReference.document(currentUserId).collection("Rating").document(remoteUser).set(ratingData2);
                                userReference.document(remoteUser).collection("Rating").document(currentUserId).set(ratingData2);
                                Toast.makeText(getContext(), "Rated", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    }

    private void addNewRatingData(int rate, String comment) {
        ArrayList<Long> totalRating = new ArrayList<>();
        totalRating.add(oneStar);
        totalRating.add(twoStars);
        totalRating.add(threeStars);
        totalRating.add(fourStars);
        totalRating.add(fiveStars);
        Map data = new HashMap();
        data.put("totalRating", totalRating);
        data.put("ratingStar", new_ratingStar);
        userReference.document(currentUserId).update(data).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Map ratingData2 = new HashMap();
                ratingData2.put("rating", rate);
                ratingData2.put("comment", comment);
                userReference.document(currentUserId).collection("Rating").document(remoteUser).set(ratingData2);
                userReference.document(remoteUser).collection("Rating").document(currentUserId).set(ratingData2);
                Toast.makeText(getContext(), "Rated", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void history_itemClick(String id){
                remoteUser = id;
                userReference.document(currentUserId).collection("Rating").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        client_home.ratingInterface(client_history.this);
                        long rate = documentSnapshot.getLong("rating");
                        String comment;
                        if(documentSnapshot.getString("comment") != null){
                             comment = documentSnapshot.getString("comment");
                        }
                        else{
                            comment = "No Comment";
                        }

                        new AppRatingDialog.Builder()
                                .setPositiveButtonText("Submit")
                                .setNegativeButtonText("Cancel")
                                .setNeutralButtonText("Later")
                                .setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"))
                                .setDefaultRating((int) rate)
                                .setTitle("Rate this application")
                                .setDescription("Please select some stars and give your feedback")
                                .setCommentInputEnabled(true)
                                .setStarColor(R.color.starColor)
                                .setNoteDescriptionTextColor(R.color.noteDescriptionTextColor)
                                .setTitleTextColor(R.color.titleTextColor)
                                .setDescriptionTextColor(R.color.secondarycolor)
                                .setDefaultComment(Objects.requireNonNull(comment))
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

                });

            }

    }
