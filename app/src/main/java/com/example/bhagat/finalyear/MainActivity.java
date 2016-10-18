package com.example.bhagat.finalyear;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    ViewPagerAdapter viewPagerAdapter;

    private SlidingTabLayout mtabs;
    private ViewPager mpager;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mpager = (ViewPager) findViewById(R.id.pager);
        mpager.setAdapter(viewPagerAdapter);

        //Assigning the sliding tab layout view
        mtabs = (SlidingTabLayout) findViewById(R.id.tabs);
        mtabs.setDistributeEvenly(true);


        //  Setting the ViewPager For the SlidingTabsLayout
        mtabs.setViewPager(mpager);

        //startActivity(new Intent(MainActivity.this,Registration.class));
        startActivity(new Intent(MainActivity.this, MakeRequest.class));
    }

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
/*
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
}
