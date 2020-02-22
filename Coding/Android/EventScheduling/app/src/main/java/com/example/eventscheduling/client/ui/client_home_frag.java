package com.example.eventscheduling.client.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.eventscheduling.R;
import com.example.eventscheduling.client.model.ViewPager_Adapter_Client;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;


public class client_home_frag extends Fragment {
    ViewPager viewPager;
    TabLayout tabLayout;
    TabItem eventType;
    TabItem serviceType;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client_home_frag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.viewPager_client);
        tabLayout = view.findViewById(R.id.tabLayout_id_client_home);
        eventType = view.findViewById(R.id.tab_event_type);
        serviceType = view.findViewById(R.id.tab_service_type);
        ViewPager_Adapter_Client viewPage_adpt = new ViewPager_Adapter_Client(getChildFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(viewPage_adpt);
        tabLayout.setupWithViewPager(viewPager);
        setUpText();
    }
    public void setUpText(){
        tabLayout.getTabAt(0).setText("Event");
        tabLayout.getTabAt(1).setText("Service");

    }

}
