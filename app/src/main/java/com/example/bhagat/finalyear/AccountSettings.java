package com.example.bhagat.finalyear;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**]
 * Created by Shashank on 09-10-2016.
 * Improved by bhagat on 10/28/16.
 */
public class AccountSettings extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback,LocationListener {
    ArrayList<String> categoryName;
    EditText textIn;
    Button buttonSave;
    FloatingActionButton buttonAdd;
    ImageView updateLocation;
    LinearLayout container;
    EditText service_name;
    TextView tvcategoryName;
    ProgressDialog pDialog;
    static String SERVICE_NAME = "";
    double latitude,longitude;

    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;

    ArrayList<ListData> arrayOfItems;
    private LocationManager locationManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_settings, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        arrayOfItems = new ArrayList<>();
        categoryName = new ArrayList<>();

        updateLocation = (ImageView) getActivity().findViewById(R.id.update_location);
        service_name = (EditText) getActivity().findViewById(R.id.etservice_name);
        tvcategoryName = (TextView) getActivity().findViewById(R.id.tvcat_name);
        buttonSave = (Button) getActivity().findViewById(R.id.buttonSave);
        textIn = (EditText) getActivity().findViewById(R.id.textin);
        buttonAdd = (FloatingActionButton) getActivity().findViewById(R.id.add);
        container = (LinearLayout) getActivity().findViewById(R.id.container);

        getLocation();
        getProviderServices();
        categoryName.clear();


        updateLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<>();
                params.put("provider_id", UserDetails.getInstance().providerId);
                params.put("loc_x", latitude+"");
                params.put("loc_y", longitude+"");
                Toast.makeText(getActivity(),latitude +" "+longitude,Toast.LENGTH_LONG).show();
                String url = UserDetails.getInstance().url + "update_location.php";
                VolleyNetworkManager.getInstance(getContext()).makeRequest(params,
                        url, new VolleyNetworkManager.Callback() {
                            @Override
                            public void onSuccess(String response) {
                                Log.d("RESPONSE", response);
                            }
                            @Override
                            public void onError(String error) {
                                Toast.makeText(getActivity(),error,Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                LayoutInflater layoutInflater =
                        (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.remove_category, null);
                TextView textOut = (TextView) addView.findViewById(R.id.textout);
                textOut.setText(textIn.getText().toString());
                Button buttonRemove = (Button) addView.findViewById(R.id.remove);
                buttonRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((LinearLayout) addView.getParent()).removeView(addView);
                    }
                });
                container.addView(addView);
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SERVICE_NAME = service_name.getText().toString();
                for (int i = 0; i < container.getChildCount(); i++) {
                    RelativeLayout temp = (RelativeLayout) container.getChildAt(i);
                    TextView tvCategory = (TextView) temp.findViewById(R.id.textout);
                    categoryName.add(tvCategory.getText().toString() + "");
                }
                postRequest();
            }
        });
    }
    public void postRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("providerID", UserDetails.getInstance().providerId);
        params.put("serviceName", SERVICE_NAME);
        params.put("noOfCategories", categoryName.size() + "");
        for (int i = 0; i < categoryName.size(); i++) {
            params.put("category" + i, categoryName.get(i));
            Log.d("category", categoryName.get(i));
        }
        categoryName.clear();
        String url = UserDetails.getInstance().url + "se_addService.php";
        VolleyNetworkManager.getInstance(getContext()).makeRequest(params,
                url, new VolleyNetworkManager.Callback() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d("RESPONSE", response);
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(getActivity(),error,Toast.LENGTH_LONG).show();
                    }
                });
    }
    public void getProviderServices(){
        Map<String,String> params = new HashMap<>();
        params.put("provider_id",UserDetails.getInstance().providerId);
        String url = UserDetails.getInstance().url + "get_provider_services.php";
        VolleyNetworkManager.getInstance(getContext()).makeRequest(params,
                url, new VolleyNetworkManager.Callback() {
                    @Override
                    public void onSuccess(String response) {
                        if(response != null){
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jOb1 = jsonArray.getJSONObject(0);
                                String provider_service_name = jOb1.getString("service_name");
                                service_name.setText(provider_service_name);
                                if(jsonArray.length() >= 2){
                                    JSONObject jOb2 = jsonArray.getJSONObject(1);
                                    for(int i = 1 ; i <= jOb2.length();i++){
                                        categoryName.add(jOb2.getString("cat" + i));
                                    }
                                    LayoutInflater layoutInflater =
                                            (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    for(int i = 0 ; i < categoryName.size();i++){
                                        final View addView = layoutInflater.inflate(R.layout.remove_category, null);
                                        TextView textOut = (TextView) addView.findViewById(R.id.textout);
                                        textOut.setText(categoryName.get(i));
                                        Button buttonRemove = (Button) addView.findViewById(R.id.remove);
                                        buttonRemove.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                ((LinearLayout) addView.getParent()).removeView(addView);
                                            }
                                        });
                                        container.addView(addView);
                                    }

                                }
                            }
                            catch (Exception e){

                            }
                        }
                    }
                    @Override
                    public void onError(String error) {
                        Toast.makeText(getActivity(),error,Toast.LENGTH_LONG).show();
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
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        UserDetails.getInstance().latitude = latitude+"";
        UserDetails.getInstance().longitude = longitude+"";

        Log.d("Longitude", longitude + " ");
        latitude = location.getLatitude();
        Log.d("Latitude", latitude + " ");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}