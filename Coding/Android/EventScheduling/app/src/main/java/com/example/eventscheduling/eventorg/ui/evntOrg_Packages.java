package com.example.eventscheduling.eventorg.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.eventscheduling.R;
import com.example.eventscheduling.eventorg.model.RecyclerView_Adapter_Packages;
import com.example.eventscheduling.eventorg.util.PackagesValues;

import java.util.ArrayList;

public class evntOrg_Packages extends Fragment {
    private static final String TAG = "evntOrg_Packages";
    ArrayList<PackagesValues> arrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_evnt_org__packages, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initImages(view);

    }
    public void initImages(View view){
        arrayList.add(new PackagesValues(R.mipmap.food_1,"Special Package 1","Rs:1400/per head","Bandhan Marriage Hall",5));
        arrayList.add(new PackagesValues(R.mipmap.food_2,"Shadi Package ","Rs:900/per head","Bandhan Marriage Hall",4));
        arrayList.add(new PackagesValues(R.mipmap.food_3,"Special Package 2","Rs:1000/per head","Bandhan Marriage Hall",5));
        arrayList.add(new PackagesValues(R.mipmap.food_4,"Local Offer","Rs:800/per head","Bandhan Marriage Hall",3));
        arrayList.add(new PackagesValues(R.mipmap.food_5,"Limited Package","Rs:1400/per head","Bandhan Marriage Hall",5));


        initRecyclerView(view);
    }
    public void initRecyclerView(View view){
        Log.d(TAG, "initRecyclerView: is called");
        RecyclerView recyclerView =  view.findViewById(R.id.package_recycler);
        RecyclerView_Adapter_Packages recyclerView_adapter_packages = new RecyclerView_Adapter_Packages(view.getContext(),arrayList);
        recyclerView.setAdapter(recyclerView_adapter_packages);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));}




}
