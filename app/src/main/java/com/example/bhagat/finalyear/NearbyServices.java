package com.example.bhagat.finalyear;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NearbyServices extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback,LocationListener{

    SharedPreferences sharedPreferences;
    ListView requestsList;
    ArrayList<ListData> arrayOfItems;
    private LocationManager locationManager;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    double latitude,longitude;
    String radialDistance = "100";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nearby_services,container,false);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        requestsList = (ListView) getActivity().findViewById(R.id.list);
        //  ListView
        arrayOfItems = new ArrayList<>();

        getLocation();

        requestsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    startActivity(new Intent(getActivity(), MakeRequest.class).
                            putExtra("serviceId", arrayOfItems.get(i).jOb.getString("service_id"))
                            .putExtra("serviceName",arrayOfItems.get(i).jOb.getString("service_name"))
                            .putExtra("providerName",arrayOfItems.get(i).jOb.getString("provider_name"))
                            .putExtra("distance",arrayOfItems.get(i).jOb.getString("distance"))
                            .putExtra("providerPhno",arrayOfItems.get(i).jOb.getString("provider_phno")));
                    getActivity().finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    LocationListener locationListener = new LocationListener(){
        @Override
        public void onLocationChanged(Location location) {
        }
        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }
        @Override
        public void onProviderEnabled(String s) {
        }
        @Override
        public void onProviderDisabled(String s) {
        }
    };

    void getLocation(){
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if ( ContextCompat.checkSelfPermission( getActivity(), Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), true));
            if (location != null) {
                onLocationChanged(location);
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE,
                    locationListener );
            getServices();
        }
        else{
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},200);
            }
        }

    }





    ///volley
    void getServices() {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        Map<String, String> params = new HashMap<>();
        params.put("consumer_locx", latitude + "");
        params.put("consumer_locy", longitude + "");
        //Toast.makeText(getActivity(),latitude+" "+longitude,Toast.LENGTH_LONG).show();
        //Toast.makeText(getActivity(),sharedPreferences.getString("radial_distance","100"),Toast.LENGTH_LONG).show();
        params.put("radial_distance",sharedPreferences.getString("radial_distance","100"));
        String url = UserDetails.getInstance().url + "fetch_services.php";
        VolleyNetworkManager.getInstance(getContext()).makeRequest(params,
                url, new VolleyNetworkManager.Callback() {
                    @Override
                    public void onSuccess(String response) {
                        try{
                            Log.d("services",response);
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jOb = jsonArray.getJSONObject(i);
                                arrayOfItems.add(new ListData(jOb));
                            }
                            pDialog.hide();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        NearbyServicesAdapter adapter = new NearbyServicesAdapter(getActivity(), 0, arrayOfItems);
                        requestsList.setAdapter(adapter);
                    }
                    @Override
                    public void onError(String error) {
                        pDialog.hide();
                        Toast.makeText(getActivity(),error,Toast.LENGTH_LONG).show();
                    }
                });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 200: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getServices();
                    Log.d("permission","granted");
                    // permission was granted, yay! do the
                    // calendar task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        UserDetails.getInstance().latitude = latitude+"";
        UserDetails.getInstance().longitude = longitude+"";
        Log.d("Longitude", longitude + " ");
        //Toast.makeText(getActivity(),latitude +" "+longitude,Toast.LENGTH_LONG).show();
        //latitude = location.getLatitude();
        Log.d("Latitude", latitude + " ");
    }
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }
    @Override
    public void onProviderEnabled(String s) {
    }
    @Override
    public void onProviderDisabled(String s) {
    }
}