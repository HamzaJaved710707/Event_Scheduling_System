package com.example.eventscheduling.client.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import com.example.eventscheduling.R;
import com.example.eventscheduling.client.model.client_recycler_event_adapter;
import com.example.eventscheduling.client.model.client_recycler_service_adapter;
import com.example.eventscheduling.client.util.home_event_values;
import com.example.eventscheduling.client.util.home_service_values;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class client_home_service extends Fragment {

    private static final String TAG = "client_home_service";
    ArrayList<home_service_values> arrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_client_home_service, container, false);
        initValue(view);
        return view;
    }

    public void initValue(View view) {
        Log.d(TAG, "initImages: is called");
        arrayList.add((new home_service_values("Event Organizer")));
        arrayList.add((new home_service_values("Catering ")));
        arrayList.add((new home_service_values("Decoration")));
        arrayList.add((new home_service_values("Car Rent")));
        arrayList.add((new home_service_values("Photographer")));
        arrayList.add((new home_service_values("Invitation")));

        initRecyclerView(view);
    }

    public void initRecyclerView(View view) {
        Log.d(TAG, "initRecyclerView: is called");
        RecyclerView recyclerView = view.findViewById(R.id.recycler_client_home_service);
        client_recycler_service_adapter recyclerView_adapterMessage = new client_recycler_service_adapter(view.getContext(), arrayList);
        recyclerView.setAdapter(recyclerView_adapterMessage);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

}
