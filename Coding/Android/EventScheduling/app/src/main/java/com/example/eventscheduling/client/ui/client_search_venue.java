package com.example.eventscheduling.client.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventscheduling.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class client_search_venue extends Fragment {


    public client_search_venue() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client_search_venue, container, false);
    }

}
