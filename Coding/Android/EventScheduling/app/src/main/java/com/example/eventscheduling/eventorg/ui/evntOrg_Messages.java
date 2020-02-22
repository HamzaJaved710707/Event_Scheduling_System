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
import com.example.eventscheduling.eventorg.model.RecyclerView_Adapter_Message;
import com.example.eventscheduling.eventorg.util.MessageValues;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class evntOrg_Messages extends Fragment {

      ArrayList<MessageValues> arrayList = new ArrayList<>();
    private static final String TAG = "evntOrg_Messages";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootview = inflater.inflate(R.layout.fragment_evnt_org__messages, container, false);
        initImages(rootview);
      //  initImages(rootview);
        return  rootview;
    }

    public void initImages(View view){
        Log.d(TAG, "initImages: is called");
        arrayList.add((new MessageValues(R.mipmap.talha_img,"Talha", "Where are you?")));
        arrayList.add((new MessageValues(R.mipmap.ahmed_img,"Ahmed", "Please send me notes...")));
        arrayList.add((new MessageValues(R.mipmap.talmeez_img,"Talmeez", "How are you?")));
        arrayList.add((new MessageValues(R.mipmap.hamza_img,"Hamza", "Namaz parha karo sb...")));
        arrayList.add((new MessageValues(R.mipmap.talha_img,"Talha", "Where are you?")));
        arrayList.add((new MessageValues(R.mipmap.ahmed_img,"Ahmed", "Please send me notes...")));
        arrayList.add((new MessageValues(R.mipmap.talmeez_img,"Talmeez", "How are you?")));
        arrayList.add((new MessageValues(R.mipmap.hamza_img,"Hamza", "Namaz parha karo sb...")));
        initRecyclerView(view);
    }
    public void initRecyclerView(View view){
        Log.d(TAG, "initRecyclerView: is called");
        RecyclerView recyclerView =  view.findViewById(R.id.message_recyclerview);
        RecyclerView_Adapter_Message recyclerView_adapterMessage = new RecyclerView_Adapter_Message(view.getContext(),arrayList);
        recyclerView.setAdapter(recyclerView_adapterMessage);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));}
}
