package com.example.eventscheduling.eventorg.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventscheduling.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class evntOrg_Orders extends Fragment {


    public evntOrg_Orders() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_evnt_org__orders, container, false);
    }

}
