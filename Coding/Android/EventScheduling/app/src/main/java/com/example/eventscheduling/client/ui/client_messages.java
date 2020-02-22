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
    ArrayList<Client_Msg_Values> arrayList = new ArrayList<>();
    private static final String TAG = "client_messages";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_messages, container, false);
        initImages(view);
        return view;
    }

    public void initImages(View view) {
        Log.d(TAG, "initImages: is called");
        arrayList.add((new Client_Msg_Values(R.mipmap.talha_img, "Talha", "Where are you?")));
        arrayList.add((new Client_Msg_Values(R.mipmap.ahmed_img, "Ahmed", "Please send me notes...")));
        arrayList.add((new Client_Msg_Values(R.mipmap.talmeez_img, "Talmeez", "How are you?")));
        arrayList.add((new Client_Msg_Values(R.mipmap.hamza_img, "Hamza", "Namaz parha karo sb...")));
        arrayList.add((new Client_Msg_Values(R.mipmap.talha_img, "Talha", "Where are you?")));
        arrayList.add((new Client_Msg_Values(R.mipmap.ahmed_img, "Ahmed", "Please send me notes...")));
        arrayList.add((new Client_Msg_Values(R.mipmap.talmeez_img, "Talmeez", "How are you?")));
        arrayList.add((new Client_Msg_Values(R.mipmap.hamza_img, "Hamza", "Namaz parha karo sb...")));
        initRecyclerView(view);
    }

    public void initRecyclerView(View view) {
        Log.d(TAG, "initRecyclerView: is called");
        RecyclerView recyclerView = view.findViewById(R.id.client_recyclerview_msg);
        Client_RecyclerAdptr_Msg recyclerView_adapterMessage = new Client_RecyclerAdptr_Msg(view.getContext(), arrayList);
        recyclerView.setAdapter(recyclerView_adapterMessage);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }
}
