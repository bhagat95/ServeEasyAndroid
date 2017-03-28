package com.example.bhagat.finalyear;


import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

public class ConsumerHome extends AppCompatActivity {
    DrawerLayout dlayout;
    ActionBarDrawerToggle toggle;
    FragmentManager manager;
    ConsumerTransactions consumerTransactionsFragment;
    Requests requestsFragment;
    AccountSettings accountSettingsFragment;
    NearbyServices nearbyServicesFragment;
    SharedPreferences sharedPreferences;
    TextView welcome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_consumer_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dlayout = (DrawerLayout) findViewById(R.id.mydrawerlayout);
        consumerTransactionsFragment = new ConsumerTransactions();
        //accountSettingsFragment = new AccountSettings();
        nearbyServicesFragment = new NearbyServices();
        toggle = new ActionBarDrawerToggle(this, dlayout, 0, 0);
        dlayout.addDrawerListener(toggle);
        toggle.syncState();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();//.detach(requestsFragment).attach(requestsFragment);
        ft.replace(R.id.dummy_consumer,nearbyServicesFragment);
        //ft.replace(R.id.dummy, new Requests());
        ft.commit();


        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view_consumer);
        String username = sharedPreferences.getString("username","User");
        View header = (navigationView.getHeaderView(0));
        welcome = (TextView)header.findViewById(R.id.welcomehead);
        welcome.setText("Welcome " + username +"!");

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                final int id = item.getItemId();
                if (id == R.id.nearby_services) {
                    FragmentTransaction ft = manager.beginTransaction();
                    ft.replace(R.id.dummy_consumer, nearbyServicesFragment);
                    //ft.replace(R.id.dummy, new Requests());
                    //ft.replace(R.id.account_settings_fragment, requestsFragment);

                    ft.commit();

                }
                /*else if (id == R.id.settings) {
                    FragmentTransaction ft = manager.beginTransaction();
                    ft.replace(R.id.dummy, accountSettingsFragment);
                    //ft.replace(R.id.requests_fragment, accountSettingsFragment);
                    ft.commit();
                }*/

                //Todo: conditional statement to check if ProviderTransactions or ConsumerTransactions
                else if (id == R.id.consumer_transactions) {
                    FragmentTransaction ft = manager.beginTransaction();
                    ft.replace(R.id.dummy_consumer, consumerTransactionsFragment);
                    //ft.replace(R.id.requests_fragment, accountSettingsFragment);
                    ft.commit();
                }
                else if(id == R.id.consumer_logout){
                    editor.putString("username", "guest");
                    editor.putBoolean("loggedin", false);
                    editor.apply();
                    stopService(new Intent(ConsumerHome.this, ConsumerBackgroundService.class));
                    startActivity(new Intent(ConsumerHome.this, Login.class));
                    finish();
                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.mydrawerlayout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        try {
            //temporary service testing
            startService(new Intent(this, ConsumerBackgroundService.class));
        }
        catch (Exception e) {
            Log.e("startService",e.toString());
        }
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