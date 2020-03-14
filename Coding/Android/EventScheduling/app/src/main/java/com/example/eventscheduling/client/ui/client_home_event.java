package com.example.eventscheduling.client.ui;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.eventscheduling.R;
import com.example.eventscheduling.client.model.client_recycler_event_adapter;
import com.example.eventscheduling.client.util.home_event_values;

import java.util.ArrayList;

public class client_home_event extends Fragment {
    private static final String TAG = "client_home_event";
    ArrayList<home_event_values> arrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_client_home_event, container, false);
        initValue(view);
        return view;
    }

    public void initValue(View view) {
        Log.d(TAG, "initImages: is called");
        arrayList.add((new home_event_values("Wedding Ceremony")));
        arrayList.add((new home_event_values("Birthday Party")));
        arrayList.add((new home_event_values("Islamic Event")));
        arrayList.add((new home_event_values("Academic Conference")));
        arrayList.add((new home_event_values("Family Gathering")));
        arrayList.add((new home_event_values("Seminar")));

        initRecyclerView(view);
    }

    public void initRecyclerView(View view) {
        Log.d(TAG, "initRecyclerView: is called");
        RecyclerView recyclerView = view.findViewById(R.id.recycler_client_home_event);
        client_recycler_event_adapter recyclerView_adapterMessage = new client_recycler_event_adapter(view.getContext(), arrayList);
        recyclerView.setAdapter(recyclerView_adapterMessage);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

}
