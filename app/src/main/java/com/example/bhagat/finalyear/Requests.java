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
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_requests, container, false);
/*
        View view = inflater.inflate(R.layout.fragment_requests, container, false);

        viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        mpager = (ViewPager) view.findViewById(R.id.pager);
        mpager.setAdapter(viewPagerAdapter);

        //Assigning the sliding tab layout view
        mtabs = (SlidingTabLayout) view.findViewById(R.id.tabs);
        mtabs.setDistributeEvenly(true);


        //  Setting the ViewPager For the SlidingTabsLayout
        mtabs.setViewPager(mpager);

        return view;
*/
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //toolbar
        //toolbar = (Toolbar) findViewById(R.id.app_bar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        mpager = (ViewPager) getActivity().findViewById(R.id.pager);
        mpager.setAdapter(viewPagerAdapter);

        //Assigning the sliding tab layout view
        mtabs = (SlidingTabLayout) getActivity().findViewById(R.id.tabs);
        mtabs.setDistributeEvenly(true);


        //  Setting the ViewPager For the SlidingTabsLayout
        mtabs.setViewPager(mpager);

    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        // set the SearchView
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.search:

                onSearchRequested();

                return true;

            default:

                return false;
        }
    }
    */

    @Override
    public void onDestroyView() {

        FragmentManager fragmentManager = getFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        super.onDestroyView();
    }

}


/*
Please document the code properly
*/
