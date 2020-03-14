package com.example.eventscheduling.eventorg.ui;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventscheduling.R;
import com.example.eventscheduling.eventorg.model.RecyclerView_Adapter_Message;
import com.example.eventscheduling.eventorg.util.MessageValues;

import java.util.ArrayList;

// Message Fragment to Show messages to Event organizer
public class evntOrg_Messages extends Fragment {
    private static final String TAG = "evntOrg_Messages";
    /// Array List to get Messages Values from messageValue class
    ArrayList<MessageValues> arrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootview = inflater.inflate(R.layout.fragment_evnt_org__messages, container, false);
        initImages(rootview);
        return rootview;
    }

    public void initImages(View view) {
        Log.d(TAG, "initImages: is called");

        // Initialize values to arrayList of MessageValues
        arrayList.add((new MessageValues(R.mipmap.talha_img, "Talha", "Where are you?")));
        arrayList.add((new MessageValues(R.mipmap.ahmed_img, "Ahmed", "Please send me notes...")));
        arrayList.add((new MessageValues(R.mipmap.talmeez_img, "Talmeez", "How are you?")));
        arrayList.add((new MessageValues(R.mipmap.hamza_img, "Hamza", "Namaz parha karo sb...")));
        arrayList.add((new MessageValues(R.mipmap.talha_img, "Talha", "Where are you?")));
        arrayList.add((new MessageValues(R.mipmap.ahmed_img, "Ahmed", "Please send me notes...")));
        arrayList.add((new MessageValues(R.mipmap.talmeez_img, "Talmeez", "How are you?")));
        arrayList.add((new MessageValues(R.mipmap.hamza_img, "Hamza", "Namaz parha karo sb...")));
        initRecyclerView(view);
    }

    public void initRecyclerView(View view) {
        Log.d(TAG, "initRecyclerView: is called");
        // Initialize RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.message_recyclerview);
        //Set adapter for recyclerView
        RecyclerView_Adapter_Message recyclerView_adapterMessage = new RecyclerView_Adapter_Message(view.getContext(), arrayList);
        recyclerView.setAdapter(recyclerView_adapterMessage);

        //Set Layout for recyclerView either it is horizontal or vertical
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }
}
