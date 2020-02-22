package com.example.eventscheduling.client.model;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.eventscheduling.client.ui.client_home_event;
import com.example.eventscheduling.client.ui.client_home_service;

public class ViewPager_Adapter_Client extends FragmentPagerAdapter {
    int noOfTabs;

    public ViewPager_Adapter_Client(@NonNull FragmentManager fm, int noOfTabs) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.noOfTabs = noOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new client_home_event();
            case 1:
                return new client_home_service();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}
