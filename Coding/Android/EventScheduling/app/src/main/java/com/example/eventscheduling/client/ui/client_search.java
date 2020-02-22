package com.example.eventscheduling.client.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.eventscheduling.client.model.ViewPager_client_search;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.example.eventscheduling.R;

// Client Search class
public class client_search extends Fragment {

    SearchView searchView;
    ViewPager viewPager;
    TabLayout tabLayout;
    TabItem venueType;
    TabItem locationType;
    TabItem serviceType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_search, container, false);

        // For changing the color of search icon in searchview
     /*   searchView = view.findViewById(R.id.searchview_client);
        ImageView searchIcon = view.findViewById(androidx.appcompat.R.id.search_mag_icon);
        searchIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_search_white));
*/
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.viewPager_client_search);
        tabLayout = view.findViewById(R.id.tabLayout_client_search);
        venueType = view.findViewById(R.id.tab_search_venue);
        serviceType = view.findViewById(R.id.tab_search_service);
        locationType = view.findViewById(R.id.tab_search_location);
        ViewPager_client_search viewPage_adpt = new ViewPager_client_search(getChildFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(viewPage_adpt);
        tabLayout.setupWithViewPager(viewPager);
        setUpText();
    }
    public void setUpText(){
        tabLayout.getTabAt(0).setText("Venue");
        tabLayout.getTabAt(1).setText("Service");
        tabLayout.getTabAt(2).setText("Location");


    }
}
