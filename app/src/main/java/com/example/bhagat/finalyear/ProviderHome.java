package com.example.bhagat.finalyear;

import android.accounts.Account;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class ProviderHome extends AppCompatActivity {
    DrawerLayout dlayout;
    ActionBarDrawerToggle toggle;
    FragmentManager manager;

    Requests requestsFragment;
    AccountSettings accountSettingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        dlayout = (DrawerLayout) findViewById(R.id.mydrawerlayout);
        requestsFragment = new Requests();
        accountSettingsFragment = new AccountSettings();
        toggle = new ActionBarDrawerToggle(this, dlayout, 0, 0);
        dlayout.addDrawerListener(toggle);
        toggle.syncState();

        manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();//.detach(requestsFragment).attach(requestsFragment);
        ft.replace(R.id.dummy,requestsFragment);
        //ft.replace(R.id.dummy, new Requests());
        ft.commit();


        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                final int id = item.getItemId();
                if (id == R.id.requests) {
                    FragmentTransaction ft = manager.beginTransaction();
                    ft.replace(R.id.dummy, requestsFragment);
                    //ft.replace(R.id.dummy, new Requests());
                    //ft.replace(R.id.account_settings_fragment, requestsFragment);

                    ft.commit();

                } else if (id == R.id.settings) {
                    FragmentTransaction ft = manager.beginTransaction();
                    ft.replace(R.id.dummy, accountSettingsFragment);
                    //ft.replace(R.id.requests_fragment, accountSettingsFragment);
                    ft.commit();
                }
                /*
                else if (id == R.id.logout) {
                    //    backgrnd_frag_is_home = false;
                    editor.putString("username", "guest");
                    editor.putBoolean("loggedin", false);
                    editor.apply();
                    Intent i = new Intent(Requests.this, Login.class);
                    startActivity(i);
                    finish();
                    return true;
                }
                */
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.mydrawerlayout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                dlayout.openDrawer(GravityCompat.START);  // OPEN DRAWER
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

}
