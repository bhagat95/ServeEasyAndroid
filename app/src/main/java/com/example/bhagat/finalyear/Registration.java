package com.example.bhagat.finalyear;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

class User {
    String name, mobileNo, address, password,confirmPassword, pinNo,userType;
    User(String name, String mobileNo, String password, String confirmPassword, String address, String pinNo, String userType) {
        this.name = name.trim();
        this.mobileNo = mobileNo.trim();
        this.password = password.trim();
        this.confirmPassword = confirmPassword.trim();
        this.address = address.trim();
        this.pinNo = pinNo.trim();
        this.userType = userType.trim();
    }
}

public class Registration extends AppCompatActivity {

    EditText name, mobileNo, address, password, confirmPassword,pinNo;
    RadioButton radioConsumer, radioProvider;
    RadioGroup radioGroup;
    Button submit;
    ProgressDialog pDialog;
    User user;
    Double loc_x,loc_y;
    boolean isMobileNoAvaialble = false;
    boolean placeCoordinates = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        name = (EditText) findViewById(R.id.registration_name);
        mobileNo = (EditText) findViewById(R.id.registration_phone);
        password = (EditText) findViewById(R.id.registration_password);
        confirmPassword = (EditText) findViewById(R.id.registration_confirm_password);
        address = (EditText)findViewById(R.id.registration_address);
        pinNo = (EditText)findViewById(R.id.registration_pin_no);
        radioGroup = (RadioGroup) findViewById(R.id.registration_radio_group);
        radioConsumer = (RadioButton) findViewById(R.id.registration_consumer);
        radioProvider = (RadioButton) findViewById(R.id.registration_provider);
        submit = (Button) findViewById(R.id.registration_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = new User(name.getText().toString(), mobileNo.getText().toString(),
                        password.getText().toString(),confirmPassword.getText().toString(),
                        address.getText().toString(), pinNo.getText().toString(),(radioProvider.isChecked()?"provider":"consumer"));
                Log.d("radioprovider", radioProvider.isChecked()+"");
                registerUser();

            }
        });
    }


    boolean registerUser() {
        //check whether password and confirm password are same or not -- frontend
        //mobile no is valid or not -- frontend
        //pin no is valid or not -- frontend
        if(checkValidity()){
            //check mobile no availibility -- backend
            if(checkMobileNoAvailibility()){
                if(findPlaceCoordinates()){
                    register();
                }
            }
        }
        return true;
    }


    public boolean checkValidity(){
        String password = user.password;
        String confirmPassword = user.confirmPassword ;
        String phone = user.mobileNo;
        String pinNo = user.pinNo;

        if(user.name.length() == 0){
            Toast.makeText(this,"Please enter your name",Toast.LENGTH_LONG).show();
            return false;
        }
        if(user.mobileNo.length() == 0){
            Toast.makeText(this,"Please enter your mobile",Toast.LENGTH_LONG).show();
            return false;
        }
        if(user.password.length() == 0){
            Toast.makeText(this,"Please enter your password",Toast.LENGTH_LONG).show();
            return false;
        }
        if(user.address.length() == 0){
            Toast.makeText(this,"Please enter your address",Toast.LENGTH_LONG).show();
            return false;
        }
        if(user.pinNo.length() == 0){
            Toast.makeText(this,"Please enter your pin no",Toast.LENGTH_LONG).show();
            return false;
        }
        if(!password.equals(confirmPassword)){
            Toast.makeText(this,"Password and Confirm password do not match. Please try again",Toast.LENGTH_LONG).show();
            return false;
        }
        if(phone.length() != 10){
            Toast.makeText(this,"Please enter a valid mobile no",Toast.LENGTH_LONG).show();
            return false;
        }
        if(pinNo.length() != 6){
            Toast.makeText(this,"Please enter a valid pin no",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    public boolean checkMobileNoAvailibility() {
        //Toast.makeText(this,"check mobile entry true" + isMobileNoAvaialble,Toast.LENGTH_LONG).show();
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.109.41/mobileno_availibility.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("mobileNo available", response);
                        try {
                            if(response.equals("fail")){
                                isMobileNoAvaialble = false;
                                Toast.makeText(Registration.this,"Sorry, this mobile no is already registered.Try again.",Toast.LENGTH_LONG).show();
                            }
                            else{
                                isMobileNoAvaialble = true;
                            }
                        }
                        catch (Exception e){
                            //isMobileNoAvaialble = false;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.d("mobileno",user.mobileNo);
                Log.d("userType",user.userType);

                params.put("mobile_no",user.mobileNo);
                params.put("user_type",user.userType);
                return params;
            }
        };
        queue.add(postRequest);
        return isMobileNoAvaialble;
    }


    public boolean findPlaceCoordinates(){
        String pin  = user.pinNo;
        String locationUrl = "http://maps.googleapis.com/maps/api/geocode/json?address="+ pin +"&sensor=false";
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, locationUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArr = jsonObject.getJSONArray("results");
                            JSONObject jsonObject1 = jsonArr.getJSONObject(0);
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("geometry");
                            JSONObject jsonObject3 = jsonObject2.getJSONObject("location");
                            String locx = jsonObject3.getString("lat");
                            String locy = jsonObject3.getString("lng");
                            loc_x = Double.parseDouble(locx);
                            loc_y = Double.parseDouble(locy);
                            Log.d("coordinates", loc_x +" " + loc_y);
                            //Toast.makeText(Registration.this,"here" + loc_x + " " + loc_y,Toast.LENGTH_LONG).show();
                            placeCoordinates = true;
                        }
                        catch (Exception e){
                            //placeCoordinates = false;
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        //pDialog.hide();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("responseError", error.getMessage());
                        //pDialog.hide();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        queue.add(postRequest);
        return true;
    }

//// TODO: 2/13/17  redirect to login activity on success
    public boolean register(){
        String url = "http://192.168.109.41/se_register.php";
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(this);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Registering...");
        pDialog.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("registration response", response);
                        //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        pDialog.hide();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("registrationError", error.getMessage());
                        pDialog.hide();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_type", user.userType);
                params.put("name", user.name);
                params.put("mobile_no", user.mobileNo);
                params.put("password", user.password);
                params.put("address", user.address);
                params.put("loc_x", loc_x + " 111");
                params.put("loc_y", loc_y + " 222");
                //Log.d("params", user.name);
                return params;
            }
        };
        queue.add(postRequest);
        return true;
    }
}