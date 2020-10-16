package com.example.eventscheduling.eventorg.model;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.eventscheduling.eventorg.ui.evntOrg_Portfolio_Pictures;
import com.example.eventscheduling.eventorg.ui.evntOrg_Portfolio_Videos;

public class ViewPager_Adpt_Portfolio extends FragmentPagerAdapter {
    int noOfTabs;
String id;
    public ViewPager_Adpt_Portfolio(@NonNull FragmentManager fm, int noOfTabs, String id) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.noOfTabs = noOfTabs;
        this.id = id;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Bundle bundle = new Bundle();
                bundle.putString("id",id );
                evntOrg_Portfolio_Pictures obj = new evntOrg_Portfolio_Pictures();
                obj.setArguments(bundle);
                return obj;
            case 1:
               Bundle bundle1 = new Bundle();
               bundle1.putString("id",id );
               evntOrg_Portfolio_Videos obj2 = new evntOrg_Portfolio_Videos();
               obj2.setArguments(bundle1);
               return  obj2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}
