package com.example.eventscheduling.client.model;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentManager;
import androidx.annotation.NonNull;

import com.example.eventscheduling.client.ui.client_search_location;
import com.example.eventscheduling.client.ui.client_search_service;
import com.example.eventscheduling.client.ui.client_search_venue;

public class ViewPager_client_search extends FragmentPagerAdapter {

    int noOfTabs;
    public ViewPager_client_search(@NonNull FragmentManager fm, int noOfTabs) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.noOfTabs = noOfTabs;
    }

    @NonNull
    @Override

    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new client_search_venue();
            case 1:
                return new client_search_service();
            case 2:
                return new client_search_location();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}
