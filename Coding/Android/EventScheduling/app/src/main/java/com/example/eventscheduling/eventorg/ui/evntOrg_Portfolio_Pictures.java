package com.example.eventscheduling.eventorg.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.eventscheduling.R;
import com.example.eventscheduling.eventorg.model.Portfolio_Pictures_Adapter;
import com.example.eventscheduling.eventorg.util.portfolio_pictures_values;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class evntOrg_Portfolio_Pictures extends Fragment {
    private static final String TAG = "evntOrg_Portfolio_Pictu";

    ArrayList<portfolio_pictures_values> arrayList = new ArrayList<>();
    private final static int NUM_COLUMNS = 2;

    public evntOrg_Portfolio_Pictures() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_evnt_org__portfolio__pictures, container, false);
        initPictures(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void initPictures(View view){
        Log.d(TAG, "initPictures: ");
        arrayList.add(new portfolio_pictures_values(R.mipmap.marriage_hall_1));
        arrayList.add(new portfolio_pictures_values(R.mipmap.marriage_hall_2));
        arrayList.add(new portfolio_pictures_values(R.mipmap.marriage_hall_3));
        arrayList.add(new portfolio_pictures_values(R.mipmap.marriage_hall_4));
        arrayList.add(new portfolio_pictures_values(R.mipmap.marriage_hall_5));
        arrayList.add(new portfolio_pictures_values(R.mipmap.marriage_hall_1));
        arrayList.add(new portfolio_pictures_values(R.mipmap.marriage_hall_3));
        arrayList.add(new portfolio_pictures_values(R.mipmap.marriage_hall_2));
        arrayList.add(new portfolio_pictures_values(R.mipmap.marriage_hall_4));
        arrayList.add(new portfolio_pictures_values(R.mipmap.marriage_hall_5));
        arrayList.add(new portfolio_pictures_values(R.mipmap.marriage_hall_2));
        arrayList.add(new portfolio_pictures_values(R.mipmap.marriage_hall_3));

        initAdapter(view);
    }
    public void initAdapter(View view){
        Log.d(TAG, "initAdapter: ");
        RecyclerView recyclerView = view.findViewById(R.id.portfolio_pictures_recyclerView);
        Portfolio_Pictures_Adapter pic_adapter = new Portfolio_Pictures_Adapter(view.getContext(),arrayList);
        recyclerView.setAdapter(pic_adapter);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        Log.d(TAG, "initAdapter:  set every thing" );
        }


}
