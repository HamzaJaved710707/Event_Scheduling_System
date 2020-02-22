package com.example.eventscheduling.eventorg.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.Toolbar;
import com.example.eventscheduling.R;
import com.example.eventscheduling.eventorg.model.ViewPager_Adpt_Portfolio;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;


public class evntOrg_Portfolio extends Fragment {
    ViewPager viewPager;
    TabLayout tabLayout;
    PagerAdapter pagerAdapter;
    Toolbar toolbar;
    TabItem videoTab;
    TabItem pictureTab;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_evnt_org__portfolio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.viewPager_id);
        tabLayout = view.findViewById(R.id.tabLayout_id);
        videoTab = view.findViewById(R.id.tab_videos);
        pictureTab = view.findViewById(R.id.tab_pictures);
        ViewPager_Adpt_Portfolio viewPage_adpt = new ViewPager_Adpt_Portfolio(getChildFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(viewPage_adpt);
        tabLayout.setupWithViewPager(viewPager);
        setUpText();
    }
    public void setUpText(){
        tabLayout.getTabAt(0).setText("Pictures");
        tabLayout.getTabAt(1).setText("Videos");

    }

}
