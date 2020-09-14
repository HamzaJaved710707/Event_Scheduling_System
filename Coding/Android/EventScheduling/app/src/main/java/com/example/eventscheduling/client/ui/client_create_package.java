package com.example.eventscheduling.client.ui;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventscheduling.R;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.rendering.ModelRenderable;

import java.lang.ref.WeakReference;


public class client_create_package extends Fragment {

    SceneView model3D;
    private WeakReference<client_create_package> owner;
    RecyclerView food_item_recyclerView;
    RecyclerView services_item_recyclerView;
    RecyclerView venue_item_recyclerView;
    public client_create_package() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_create_package, container, false);
        food_item_recyclerView = view.findViewById(R.id.client_package_recyc_food_items);
        services_item_recyclerView = view.findViewById(R.id.client_package_recyc_services);
        venue_item_recyclerView = view.findViewById(R.id.client_package_recyc_venue);
return view;
    }
}
