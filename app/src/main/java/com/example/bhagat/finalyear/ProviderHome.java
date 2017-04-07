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
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;


public class ProviderHome extends AppCompatActivity {
    DrawerLayout dlayout;
    ActionBarDrawerToggle toggle;
    FragmentManager manager;
    ProviderTransactions providerTransactionsFragment;
    Requests requestsFragment;
    AccountSettings accountSettingsFragment;
    SharedPreferences sharedPreferences;
    TextView welcome;
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
        providerTransactionsFragment = new ProviderTransactions();
        accountSettingsFragment = new AccountSettings();
        toggle = new ActionBarDrawerToggle(this, dlayout, 0, 0);
        dlayout.addDrawerListener(toggle);
        toggle.syncState();
        manager = getSupportFragmentManager();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        FragmentTransaction ft = manager.beginTransaction();

        Intent intent = getIntent();
        try {
            //String isNewUser = intent.getStringExtra("isNewUser");
            if (intent.getStringExtra("isNewUser").equals("true")) {
                ft.replace(R.id.dummy, accountSettingsFragment);
                ft.commit();
            }
        }catch (Exception e){
            Log.d("Problem",e.getMessage());
        }


        ft.replace(R.id.dummy,requestsFragment);
        ft.commit();



        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View header = (navigationView.getHeaderView(0));


        SwitchCompat availability = (SwitchCompat) navigationView.getMenu().getItem(0).getActionView();
        String username = sharedPreferences.getString("username","User");
        boolean available = sharedPreferences.getBoolean("available",true);
        availability.setChecked(available);

        availability.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                Map<String, String> params = new HashMap<>();
                params.put("user_id",UserDetails.getInstance().userId);
                if(isChecked) {
                    params.put("availability_status", "1");
                    editor.putBoolean("available",isChecked);
                    editor.commit();
                }
                else{
                    params.put("availability_status", "0");
                    editor.putBoolean("available",isChecked);
                    editor.commit();
                }
                String url = UserDetails.getInstance().url + "availability.php";
                VolleyNetworkManager.getInstance(getApplicationContext()).makeRequest(params, url,
                        new VolleyNetworkManager.Callback() {
                            @Override
                            public void onSuccess(String response) {
                                Log.d("Availability",response);
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(ProviderHome.this,error,Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });


        welcome = (TextView)header.findViewById(R.id.welcomehead);
        welcome.setText(username);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                final int id = item.getItemId();
                if(id == R.id.drawer_switch){
                    ;
                }
                else if (id == R.id.provider_requests) {
                    FragmentTransaction ft = manager.beginTransaction();
                    requestsFragment = new Requests();
                    ft.replace(R.id.dummy, requestsFragment);
                    ft.commit();
                } else if (id == R.id.provider_settings) {
                    FragmentTransaction ft = manager.beginTransaction();
                    ft.replace(R.id.dummy, accountSettingsFragment);
                    ft.commit();
                }
                else if (id == R.id.provider_transactions) {
                    FragmentTransaction ft = manager.beginTransaction();
                    ft.replace(R.id.dummy, providerTransactionsFragment);
                    ft.commit();
                }
                else if(id == R.id.provider_logout){
                    editor.putString("username", "guest");
                    editor.putString("userType", "guest");
                    editor.putBoolean("loggedin", false);
                    editor.putBoolean("available",false);
                    editor.putString("userId","0");
                    editor.putString("radial_distance","50");
                    editor.commit();
                    stopService(new Intent(ProviderHome.this, ProviderBackgroundService.class));
                    startActivity(new Intent(ProviderHome.this, Login.class));
                    finish();
                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.mydrawerlayout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        try {
            //temporary service testing
            this.startService(new Intent(this, ProviderBackgroundService.class));
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