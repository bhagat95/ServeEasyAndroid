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
import android.support.v4.util.Pair;
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
    ArrayList<Pair<String, String> > categoryDetails;
    EditText textIn;
    Button buttonSave;
    FloatingActionButton buttonAdd;
    ImageView updateLocation;
    LinearLayout container;
    EditText service_name, categoryPrice;
    TextView tvcategoryName;
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
        categoryDetails = new ArrayList<>();

        updateLocation = (ImageView) getActivity().findViewById(R.id.update_location);
        service_name = (EditText) getActivity().findViewById(R.id.etservice_name);
        tvcategoryName = (TextView) getActivity().findViewById(R.id.tvcat_name);
        buttonSave = (Button) getActivity().findViewById(R.id.buttonSave);
        textIn = (EditText) getActivity().findViewById(R.id.category_name);
        categoryPrice = (EditText) getActivity().findViewById(R.id.category_price);
        buttonAdd = (FloatingActionButton) getActivity().findViewById(R.id.add);
        container = (LinearLayout) getActivity().findViewById(R.id.container);

        getLocation();
        getProviderServices();
        categoryDetails.clear();


        updateLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<>();
                params.put("provider_id", UserDetails.getInstance().providerId);
                params.put("loc_x", latitude+"");
                params.put("loc_y", longitude+"");

                final ProgressDialog pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Loading...");
                pDialog.show();

                String url = UserDetails.getInstance().url + "update_location.php";
                VolleyNetworkManager.getInstance(getContext()).makeRequest(params,
                        url, new VolleyNetworkManager.Callback() {
                            @Override
                            public void onSuccess(String response) {
                                pDialog.dismiss();
                                Toast.makeText(getActivity(),"Current Location Updated",Toast.LENGTH_LONG).show();
                                Log.d("RESPONSE", response);

                            }
                            @Override
                            public void onError(String error) {
                                pDialog.dismiss();
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
                TextView categoryPriceTextView = (TextView) addView.findViewById(R.id.category_price_textout);
                if(textIn.getText().toString().trim().length() == 0 || categoryPrice.getText().toString().trim().length() == 0){
                    Toast.makeText(getActivity(),"Please enter complete details",Toast.LENGTH_LONG).show();
                }
                    else{
                        textOut.setText(textIn.getText().toString());
                        categoryPriceTextView.setText(categoryPrice.getText().toString());
                        ImageView buttonRemove = (ImageView) addView.findViewById(R.id.remove);
                        buttonRemove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((LinearLayout) addView.getParent()).removeView(addView);
                            }
                        });
                        container.addView(addView);
                        Toast.makeText(getActivity(),textIn.getText().toString()  + " added",Toast.LENGTH_LONG).show();
                    }
                }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SERVICE_NAME = service_name.getText().toString();

                categoryDetails.clear();

                for (int i = 0; i < container.getChildCount(); i++) {
                    LinearLayout temp = (LinearLayout) container.getChildAt(i);
                    TextView tvCategory = (TextView) temp.findViewById(R.id.textout);
                    TextView tvPrice = (TextView) temp.findViewById(R.id.category_price_textout);
                    categoryDetails.add(Pair.create(tvCategory.getText().toString() + "", tvPrice.getText().toString()));
                }

                postRequest();
            }
        });
    }
    public void postRequest() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        Map<String, String> params = new HashMap<>();
        params.put("providerID", UserDetails.getInstance().providerId);
        params.put("serviceName", SERVICE_NAME);
        params.put("noOfCategories", categoryDetails.size() + "");
        for (int i = 0; i < categoryDetails.size(); i++) {
            params.put("category" + i, categoryDetails.get(i).first);
            params.put("price" + i,  categoryDetails.get(i).second);
            Log.d("category123", categoryDetails.get(i).first+" "+categoryDetails.get(i).second);
        }
        categoryDetails.clear();
        String url = UserDetails.getInstance().url + "se_addService.php";
        VolleyNetworkManager.getInstance(getContext()).makeRequest(params,
                url, new VolleyNetworkManager.Callback() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d("RESPONSE", response);
                        pDialog.hide();
                        if(response.equals("success")){
                            Toast.makeText(getActivity(),"Details saved successfully",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        pDialog.hide();
                        Toast.makeText(getActivity(),error,Toast.LENGTH_LONG).show();
                    }
                });
    }
    public void getProviderServices(){
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        Map<String,String> params = new HashMap<>();
        params.put("provider_id",UserDetails.getInstance().providerId);
        String url = UserDetails.getInstance().url + "get_provider_services.php";
        VolleyNetworkManager.getInstance(getContext()).makeRequest(params,
                url, new VolleyNetworkManager.Callback() {
                    @Override
                    public void onSuccess(String response) {
                        if(response != null){
                            try {
                                pDialog.hide();
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jOb1 = jsonArray.getJSONObject(0);
                                String provider_service_name = jOb1.getString("service_name");
                                service_name.setText(provider_service_name);
                                if(jsonArray.length() >= 2){
                                    JSONObject jOb2 = jsonArray.getJSONObject(1);
                                    categoryDetails.clear();
                                    Log.d("categoryDetails0",jOb2.length()+"");
                                    Log.d("categoryDetails1",jOb2.getString("price1"));
                                    for(int i = 1 ; i <= jOb2.length()/2;i++){
                                        //Log.d("categoryDetails4","haha");
                                        categoryDetails.add(Pair.create(jOb2.getString("cat" + i), jOb2.getString("price" + i)));
                                        Log.d("categoryDetails2",jOb2.getString("price"+i));
                                        Log.d("categoryDetails3",categoryDetails.get(i-1).first + " " + categoryDetails.get(i-1).second );
                                    }
                                    Log.d("categoryDetails5",categoryDetails.size()+"");
                                    LayoutInflater layoutInflater =
                                            (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                    for(int i = 0 ; i < categoryDetails.size();i++){
                                        final View addView = layoutInflater.inflate(R.layout.remove_category, null);
                                        TextView textOut = (TextView) addView.findViewById(R.id.textout);
                                        TextView textOutPrice = (TextView) addView.findViewById(R.id.category_price_textout);
                                        textOut.setText(categoryDetails.get(i).first);
                                        textOutPrice.setText(categoryDetails.get(i).second);
                                        ImageView buttonRemove = (ImageView) addView.findViewById(R.id.remove);
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
                                pDialog.hide();
                            }
                        }
                    }
                    @Override
                    public void onError(String error) {
                        Toast.makeText(getActivity(),error,Toast.LENGTH_LONG).show();
                        pDialog.hide();
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
        }
        else{
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},200);
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
                }
            }
        }
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