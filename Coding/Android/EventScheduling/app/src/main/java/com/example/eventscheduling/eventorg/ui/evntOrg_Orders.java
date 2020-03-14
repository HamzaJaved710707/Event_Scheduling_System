package com.example.eventscheduling.eventorg.ui;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventscheduling.R;
import com.example.eventscheduling.eventorg.model.RecyclerView_Adapter_Order;
import com.example.eventscheduling.eventorg.util.OrderValues;

import java.util.ArrayList;

// Fragment to display orders to event Organizer
public class evntOrg_Orders extends Fragment {
    private static final String TAG = "evntOrg_Orders";
    // Array to initialize the values for order
    ArrayList<OrderValues> arrayList = new ArrayList<>();

    public evntOrg_Orders() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_evnt_org__orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: is called");
        initOrders(view);


    }

    public void initOrders(View view) {
        Log.d(TAG, "initOrders: is called");

        // Initialize the values to arrayList of Order
        arrayList.add(new OrderValues(R.mipmap.hamza_img, "Hamza", "Click here to check detail"));
        arrayList.add(new OrderValues(R.mipmap.talha_img, "Talha", "Click here to check detail"));
        arrayList.add(new OrderValues(R.mipmap.talmeez_img, "Talmeez", "Click here to check detail"));
        arrayList.add(new OrderValues(R.mipmap.hamza_img, "Hamza", "Click here to check detail"));
        arrayList.add(new OrderValues(R.mipmap.ahmed_img, "Ahmed", "Click here to check detail"));
        arrayList.add(new OrderValues(R.mipmap.talha_img, "Talha ", "Click here to check detail"));
        arrayList.add(new OrderValues(R.mipmap.ahmed_img, "Ahmed", "Click here to check detail"));
        arrayList.add(new OrderValues(R.mipmap.talmeez_img, "Talmeez", "Click here to check detail"));
        arrayList.add(new OrderValues(R.mipmap.talha_img, "Talha", "Click here to check detail"));
        arrayList.add(new OrderValues(R.mipmap.hamza_img, "Hamza", "Click here to check detail"));
        AdapterInitilize(view);

    }

    public void AdapterInitilize(View view) {
        Log.d(TAG, "AdapterInitilize: ");
        // Initialize the REcyclerView
        RecyclerView recyclerView = view.findViewById(R.id.order_recyclerView);
        // SeT adapter
        RecyclerView_Adapter_Order recyclerView_adapter_order = new RecyclerView_Adapter_Order(getContext(), arrayList);
        recyclerView.setAdapter(recyclerView_adapter_order);
        // Set Layout for adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


    }

}
