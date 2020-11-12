package com.example.eventscheduling.eventorg.ui;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.example.eventscheduling.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.content.ContentValues.TAG;


public class evntOrg_Calender extends Fragment {
// Calender variable
    private CalendarView calendarView;
    private List<String> pendingDates = new ArrayList<>();
    private List<String> completedDates = new ArrayList<>();
    // Firebase Variables
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser ;
    private String currentUserId;
    private FirebaseAuth mAuth;
    private CollectionReference orderCollection;
    private ProgressBar progressBar;
    private Dialog mOverDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_evnt_org__calender, container, false);
        progressBar = view.findViewById(R.id.evntOrg_calendar_progressBar);
        mOverDialog = new Dialog(view.getContext(), android.R.style.Theme_Panel);
        mOverDialog.setCancelable(false);

        progressBar.setVisibility(View.VISIBLE);
        mOverDialog.show();

        calendarView = view.findViewById(R.id.calender_view);
        mAuth = FirebaseAuth.getInstance() ;
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUserId = currentUser.getUid();
            orderCollection = firestore.collection("Users").document(currentUserId).collection("Orders");
        }
      //  calendarView.setHeaderColor(android.co);

        getLists();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void getLists(){
        Query queryPending = orderCollection.whereEqualTo("status", 0);
        Query queryCompleted = orderCollection.whereEqualTo("status", 1);
        Task pendingTask = queryPending.get();
        Task completedTask = queryCompleted.get();
        Task<List<QuerySnapshot>> combined =Tasks.whenAllSuccess(pendingTask, completedTask);
        combined.addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
            @Override
            public void onSuccess(List<QuerySnapshot> querySnapshots) {
                for(QuerySnapshot querySnapshot: querySnapshots){

                    for(QueryDocumentSnapshot queryDocumentSnapshot: querySnapshot){

                        long data = queryDocumentSnapshot.getLong("status");
                        if(data== 0) {
                            pendingDates.add(queryDocumentSnapshot.getString("date"));
                        }
                        else{
                            completedDates.add(queryDocumentSnapshot.getString("date"));
                        }


                    }
                }
                addEvents();
            }
        });

    }
    private void addEvents(){
        List<Calendar> dates = new ArrayList<>();
List<Calendar> datesCalCompleted = new ArrayList<>();
        for(String date1: pendingDates){
            try {
                DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
                Date date = dateFormat.parse(date1);
                Calendar obj = Calendar.getInstance();
                obj.setTime(Objects.requireNonNull(date));
                dates.add(obj);
            }
            catch(ParseException pe ) {
                // handle the failure
            }
        }
        for(String date2: completedDates) {

                try {
                    DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
                    Date dateObj = dateFormat.parse(date2);
                    Calendar obj = Calendar.getInstance();
                    obj.setTime(Objects.requireNonNull(dateObj));
                    datesCalCompleted.add(obj);
                } catch (ParseException pe) {
                    // handle the failure
                }

        }
        List<EventDay> eventDay = new ArrayList<>();
        List<EventDay> eventDayCompleted = new ArrayList<>();
        eventDayCompleted.clear();
        eventDay.clear();
        for(Calendar cal: dates){
            EventDay day = new EventDay(cal,R.color.calender_pending_clr);
            eventDay.add(day);
        }
        for(Calendar calCompleted: datesCalCompleted){
            EventDay day = new EventDay(calCompleted,R.color.colorAccent);
            eventDay.add(day);
        }

        calendarView.setEvents(eventDay);
        progressBar.setVisibility(View.INVISIBLE);
        mOverDialog.dismiss();


    }
}
