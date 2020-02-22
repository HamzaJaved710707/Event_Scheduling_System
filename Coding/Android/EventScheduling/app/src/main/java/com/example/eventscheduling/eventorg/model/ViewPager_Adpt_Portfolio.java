package com.example.eventscheduling.eventorg.model;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.eventscheduling.eventorg.ui.evntOrg_Portfolio_Pictures;
import com.example.eventscheduling.eventorg.ui.evntOrg_Portfolio_Videos;

public class ViewPager_Adpt_Portfolio extends FragmentPagerAdapter {
    int noOfTabs;

    public ViewPager_Adpt_Portfolio(@NonNull FragmentManager fm, int noOfTabs) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.noOfTabs = noOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new evntOrg_Portfolio_Pictures();
            case 1:
                return new evntOrg_Portfolio_Videos();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}
