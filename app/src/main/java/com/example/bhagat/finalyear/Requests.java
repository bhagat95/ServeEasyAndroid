package com.example.bhagat.finalyear;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Requests extends android.support.v4.app.Fragment {
    ViewPagerAdapter viewPagerAdapter;
    private SlidingTabLayout mtabs;
    private ViewPager mpager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_requests, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        mpager = (ViewPager) getActivity().findViewById(R.id.pager);
        mpager.setAdapter(viewPagerAdapter);
        //Assigning the sliding tab layout view
        mtabs = (SlidingTabLayout) getActivity().findViewById(R.id.tabs);
        mtabs.setDistributeEvenly(true);
        //  Setting the ViewPager For the SlidingTabsLayout
        mtabs.setViewPager(mpager);
    }
}