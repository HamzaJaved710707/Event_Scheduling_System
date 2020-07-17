package com.example.eventscheduling.client.ui;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventscheduling.R;
import com.example.eventscheduling.client.model.Client_RecyclerAdptr_Msg;
import com.example.eventscheduling.client.util.Client_Msg_Values;

import java.util.ArrayList;


public class client_messages extends Fragment {
    private static final String TAG = "client_messages";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_messages, container, false);
        return view;
    }

    public void initRecyclerView(View view) {
        Log.d(TAG, "initRecyclerView: is called");
        RecyclerView recyclerView = view.findViewById(R.id.client_recyclerview_msg);
    }


}
