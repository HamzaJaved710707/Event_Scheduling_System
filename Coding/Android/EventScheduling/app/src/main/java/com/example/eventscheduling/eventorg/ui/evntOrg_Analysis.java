package com.example.eventscheduling.eventorg.ui;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventscheduling.R;
import com.example.eventscheduling.eventorg.model.evnt_analysis_client_recyclerview_adapter;
import com.example.eventscheduling.eventorg.util.evnt_analysis_client_recyc;
import com.example.eventscheduling.eventorg.util.evnt_analysis_temp_values;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class evntOrg_Analysis extends Fragment {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference userCollection;
    private String currentUserId;
    private int completedOrders = 0;
    private int pendingOrders = 0;
    private TextView completedTxt;
    private TextView pendingTxt;
    private List<evnt_analysis_temp_values> client_valueList = new ArrayList<>();
    private List<evnt_analysis_client_recyc> client_recycs = new ArrayList<>();
    private int count = 0;
    private int count2 = 0;
    private RecyclerView recyclerView;
    private evnt_analysis_client_recyclerview_adapter recyclerview_adapter;
    private String id;

    public evntOrg_Analysis() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evnt_org__analysis, container, false);
       // completedTxt = view.findViewById(R.id.evnt_orders_completed_txt);
        //pendingTxt = view.findViewById(R.id.evnt_orders_pending_txt);
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
            userCollection = firestore.collection("Users");
            recyclerView = view.findViewById(R.id.evnt_analysis_recyclerview);
            LoadData(view);
            loadClients();
        }
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    // Load data
    private void LoadData(View view) {
        userCollection.document(currentUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                completedOrders = 0;
                pendingOrders = 0;
                long number = documentSnapshot.getLong("type");
                if (number == 0) {
                    userCollection.document(currentUserId).collection("Orders").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                long value = document.getLong("status");
                                if (value == 0) {
                                    // Pending
                                    pendingOrders++;

                                } else if (value == 1) {
                                    // completed
                                    completedOrders++;
                                }

                            }
                            initPieChart(view, pendingOrders, completedOrders);
                          //  completedTxt.setText(String.valueOf(completedOrders));
                           // pendingTxt.setText(String.valueOf(pendingOrders));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    public void initPieChart(View view, long pending, long completed){
        PieChart pieChart = view.findViewById(R.id.pie_chart);
pieChart.setHoleColor(R.color.colorPrimary);
        pieChart.getDescription().setEnabled(false);
pieChart.setEntryLabelTextSize(16);
pieChart.setNoDataTextColor(R.color.secondarycolor);
pieChart.setNoDataText("Currently there is no order's data to be displayed here");
List<PieEntry> serviceUsed = new ArrayList<>();

        serviceUsed.add(new PieEntry(pending, "Pending Orders"));
        serviceUsed.add(new PieEntry(completed, "Completed Orders"));
        PieDataSet pieDataSet = new PieDataSet(serviceUsed ,"#Total Orders");
        PieData data = new PieData(pieDataSet);
        data.setValueTextColor(R.color.secondarycolor);
        pieDataSet.setLabel("Data");
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieDataSet.setValueTextSize(18);
     //   pieDataSet.set
        pieChart.setData(data);
        pieChart.invalidate();

    }
    private void loadClients() {
  userCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                client_valueList.clear();
                count2= 0;
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    if (documentSnapshot.getLong("type") != null) {
                        long type = documentSnapshot.getLong("type");
                        if (type == 1) {
                          count2++;

                            Query query2 = userCollection.document(documentSnapshot.getString("id")).collection("Orders").whereEqualTo("to", currentUserId);
                            query2.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    count =0;
                                    for (QueryDocumentSnapshot documentSnapshot1 : queryDocumentSnapshots) {

                                        id = documentSnapshot1.getString("from");
                                        count++;
                                    }
                                    evnt_analysis_temp_values obj = new evnt_analysis_temp_values(id, count);
                                        if(obj.getId() != null){
                                            client_valueList.add(obj);
                                        }

                                    count2--;
                                    if(count2 == 0){
                                        initClientRecyclerView();
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, e.getMessage());
                                }
                            });

                        }
                    }
                }

            }
        });


    }
private void initClientRecyclerView(){

    for(int i = 0; i<= client_valueList.size()-1; i++) {
        int finalI = i;
        client_valueList.size();
        userCollection.document(client_valueList.get(i).getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String Name = documentSnapshot.getString("Name");
                String img = documentSnapshot.getString("imgUrl");
                evnt_analysis_client_recyc obj = new evnt_analysis_client_recyc(Name, img, client_valueList.get(finalI).getCount());
                client_recycs.add(obj);
                Collections.sort(client_recycs, evnt_analysis_client_recyc.DESCENDING_COMPARATOR);
                recyclerview_adapter = new evnt_analysis_client_recyclerview_adapter(getContext(), client_recycs);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(recyclerview_adapter);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

    }
}
