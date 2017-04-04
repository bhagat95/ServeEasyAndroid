package com.example.bhagat.finalyear;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OTPVerification extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback,LocationListener{
    private ViewPagerAdapter adapter;
    private Button resendOTP, submit;
    private EditText inputOtp;
    private ProgressBar progressBar;
    private ImageButton btnEditMobile;
    private TextView txtEditMobile;
    private LocationManager locationManager;
    private LinearLayout layoutEditMobile;
    double latitude,longitude;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    RequestQueue requestQueue;
    String OTP;
    String userType, userName, mobileNo, userPassword, userAddress, loc_x, loc_y;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);
        inputOtp = (EditText) findViewById(R.id.inputOtp);
        submit = (Button) findViewById(R.id.btn_verify_otp);
        resendOTP = (Button) findViewById(R.id.resend_otp);
        //progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //txtEditMobile = (TextView) findViewById(R.id.txt_edit_mobile);
        //layoutEditMobile = (LinearLayout) findViewById(R.id.layout_edit_mobile);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        getLocation();

        Bundle bundle = getIntent().getBundleExtra("registrationDetailsBundle");
        userType = bundle.getString("user_type");
        userName = bundle.getString("name");
        mobileNo = bundle.getString("mobile_no");
        userPassword = bundle.getString("password");
        userAddress = bundle.getString("address");
        loc_x = latitude+"";
        loc_y = longitude+"";

        Log.d("OTPmobile", mobileNo);
        Log.d("OTPname", userName);

        SMSReceiver smsReceiver = new SMSReceiver(new SMSReceiver.Callback() {
            @Override
            public void onSMSReceive(String response) {
                inputOtp.setText(response);
                if(response.equals(OTP)){
                    register();
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputOtp.getText().length() != 4){
                    Toast.makeText(OTPVerification.this, "Please Enter a valid OTP", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(inputOtp.getText().toString().trim().equals(OTP))
                       register();
                    else {
                        Toast.makeText(OTPVerification.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOTP();
            }
        });

        sendOTP();
    }

    void sendOTP(){

        OTP = (int)(Math.random()*9000)+1000+"";
        Log.d("OTP",OTP);
        /*final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();*/
        String url = "https://control.msg91.com/api/sendotp.php?authkey="+getResources().getString(R.string.API_KEY)+
                "&mobile=91"+mobileNo+"&message=Your%20otp%20is%20"+OTP+"&sender=SRVESY&otp="+OTP;
        StringRequest request = new StringRequest( Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("sendOTPResponse", response);
                        try {
                            JSONObject jOb = new JSONObject(response);
                            //arrayOfItems.add(new ListData(jOb));
                            String message = jOb.getString("message");
                            String type = jOb.getString("type");
                            //pDialog.hide();
                            if(!type.equals("success"))
                                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_SHORT).show();
                            //category_name, quantity, consumer_name, category_name
                        } catch (JSONException e) {
                            //pDialog.hide();
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //pDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("fetch_requests error",error.toString());
                    }
                }
        );

        requestQueue.add(request);
    }

/*
    void resendOTP(String type){
        OTP = (mobileNo.hashCode()+"").substring(0,3);
        //String urlResendOTP = "https://control.msg91.com/api/retryotp.php?authkey=YourAuthKey&mobile=919999999990" +
           //     "&retrytype=voice";
        String url = "https://control.msg91.com/api/sendotp.php?authkey="+getResources().getString(R.string.API_KEY)+
                "&mobile=91"+mobileNo+"&rettrytype="+type;
        StringRequest request = new StringRequest( Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("sendOTPResponse", response);
                        try {
                            JSONObject jOb = new JSONObject(response);
                            //arrayOfItems.add(new ListData(jOb));
                            String message = jOb.getString("message");
                            String type = jOb.getString("type");
                            if(!type.equals("success"))
                                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_SHORT).show();
                            //category_name, quantity, consumer_name, category_name
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("fetch_requests error",error.toString());
                    }
                }
        );
        requestQueue.add(request);
    }
    */

    //// TODO: 2/13/17  redirect to login activity on success
    public boolean register() {
        //String url = "http://192.168.109.41/se_register.php";
        Map<String, String> params = new HashMap<>();

        params.put("user_type", userType);
        params.put("name", userName);
        params.put("mobile_no", mobileNo);
        params.put("password", userPassword);
        params.put("address", userAddress);
        params.put("loc_x", loc_x+"");
        params.put("loc_y", loc_y+"");

        Log.d("Register", mobileNo);

        String url = UserDetails.getInstance().url + "se_register.php";
        VolleyNetworkManager.getInstance(getApplicationContext()).makeRequest(params, url,
                new VolleyNetworkManager.Callback() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d("registration response", response);
                        Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(OTPVerification.this, Login.class);
                        i.putExtra("isNewUser","true");
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();
                    }
                });

        return true;
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
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), true));
        if (location != null) {
            onLocationChanged(location);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            ;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE,
                locationListener );
    }

    @Override
    public void onLocationChanged(Location location) {
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