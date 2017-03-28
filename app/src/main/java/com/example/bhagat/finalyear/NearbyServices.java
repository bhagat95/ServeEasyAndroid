package com.example.bhagat.finalyear;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

public class NearbyServices extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback,LocationListener {

    RequestQueue requestQueue;
    ListView requestsList;
    ArrayList<ListData> arrayOfItems;
    private LocationManager locationManager;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    double latitude,longitude;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nearby_services,container,false);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestsList = (ListView) getActivity().findViewById(R.id.list);
        //  ListView
        arrayOfItems = new ArrayList<>();

        getLocation();
        getServices();
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
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(),
                true));
        if (location != null) {
            onLocationChanged(location);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            ;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE,
                locationListener );
    }


    ///volley
    void getServices() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("consumer_locx", latitude + "");
        params.put("consumer_locy", longitude + "");
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
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        NearbyServicesAdapter adapter = new NearbyServicesAdapter(getActivity(), 0, arrayOfItems);
                        requestsList.setAdapter(adapter);
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(getActivity(),"Permission denied",Toast.LENGTH_LONG).show();
            }
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
        latitude = location.getLatitude();
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
