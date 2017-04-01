package com.example.bhagat.finalyear;


import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;





public class Registration extends AppCompatActivity {

    static String TAG = "Registration";
    EditText name, mobileNo, address, password, confirmPassword, pinNo;
    RadioButton radioConsumer, radioProvider;
    RadioGroup radioGroup;
    Button submit;
    ProgressDialog pDialog;
    User user;
    Double loc_x, loc_y;
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
        address = (EditText) findViewById(R.id.registration_address);
        pinNo = (EditText) findViewById(R.id.registration_pin_no);
        radioGroup = (RadioGroup) findViewById(R.id.registration_radio_group);
        radioConsumer = (RadioButton) findViewById(R.id.registration_consumer);
        radioProvider = (RadioButton) findViewById(R.id.registration_provider);
        submit = (Button) findViewById(R.id.registration_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = new User(name.getText().toString(), mobileNo.getText().toString(),
                        password.getText().toString(), confirmPassword.getText().toString(),
                        address.getText().toString(), pinNo.getText().toString(), (radioProvider.isChecked() ? "provider" : "consumer"));
                //Log.d("radioprovider", radioProvider.isChecked() + "");
                registerUser();
            }
        });
    }


    public void registerUser() {
        //check whether password and confirm password are same or not -- frontend
        //mobile no is valid or not -- frontend
        //pin no is valid or not -- frontend
        if (checkValidity()) {
            //check mobile no availibility -- backend
            checkMobileNoAvailibility();
                //continued in checkMobileNoAvailability()
        }
    }


    public boolean checkValidity() {
        String password = user.password;
        String confirmPassword = user.confirmPassword;
        String phone = user.mobileNo;
        String pinNo = user.pinNo;

        if (user.name.length() == 0) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_LONG).show();
            return false;
        }
        if (user.mobileNo.length() == 0) {
            Toast.makeText(this, "Please enter your mobile", Toast.LENGTH_LONG).show();
            return false;
        }
        if (user.password.length() == 0) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_LONG).show();
            return false;
        }
        if (user.address.length() == 0) {
            Toast.makeText(this, "Please enter your address", Toast.LENGTH_LONG).show();
            return false;
        }
        if (user.pinNo.length() == 0) {
            Toast.makeText(this, "Please enter your pin no", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Password and Confirm password do not match. Please try again", Toast.LENGTH_LONG).show();
            return false;
        }
        if (phone.length() != 10) {
            Toast.makeText(this, "Please enter a valid mobile no", Toast.LENGTH_LONG).show();
            return false;
        }
        if (pinNo.length() != 6) {
            Toast.makeText(this, "Please enter a valid pin no", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    public void checkMobileNoAvailibility() {
        //Toast.makeText(this,"check mobile entry true" + isMobileNoAvaialble,Toast.LENGTH_LONG).show();
        Map<String, String> params = new HashMap<>();
        params.put("mobile_no", user.mobileNo);
        params.put("user_type", user.userType);
        String url = UserDetails.getInstance().url + "mobileno_availibility.php";
        VolleyNetworkManager.getInstance(getApplicationContext()).makeRequest(params, url,
                new VolleyNetworkManager.Callback() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            if (response.equals("fail")) {
                                isMobileNoAvaialble = false;
                                Toast.makeText(Registration.this, "Sorry, this mobile no is already registered.                                                                 Try again.", Toast.LENGTH_LONG).show();
                            } else {
                                isMobileNoAvaialble = true;

                                Log.d(TAG+"findPlaceCoordinates","calling");
                                findPlaceCoordinates();


                            }
                        } catch (Exception e) {
                            //isMobileNoAvaialble = false;
                        }

                    }
                });
        Log.d(TAG+"checkNumberAvailability","called");
        //return isMobileNoAvaialble;
    }


    public boolean findPlaceCoordinates() {
        String pin = user.pinNo;
        String locationUrl = "http://maps.googleapis.com/maps/api/geocode/json?address=" + pin + "&sensor=false";
        Map<String, String> params = new HashMap<>();
        VolleyNetworkManager.getInstance(getApplicationContext()).makeRequest(params, locationUrl,
                new VolleyNetworkManager.Callback() {
                    @Override
                    public void onSuccess(String response) {
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
                            Log.d("coordinates", loc_x + " " + loc_y);
                            //Toast.makeText(Registration.this,"here" + loc_x + " " + loc_y,Toast.LENGTH_LONG).show();
                            placeCoordinates = true;

                            Log.d(TAG+"callOTPVerification","calling");
                            callOTPVerification();


                        } catch (Exception e) {
                            //placeCoordinates = false;
                            e.printStackTrace();
                        }
                    }


                });

        return placeCoordinates;
    }

    void callOTPVerification(){
        Bundle bundle = new Bundle();
        bundle.putString("user_type", user.userType);
        bundle.putString("name", user.name);
        bundle.putString("mobile_no", user.mobileNo);
        bundle.putString("password", user.password);
        bundle.putString("address", user.address);
        bundle.putString("loc_x", loc_x+"");
        bundle.putString("loc_y", loc_y+"");
        Log.d("CallOTPmobile",user.mobileNo+" "+bundle.getString("mobile_no"));
        Intent i = new Intent(Registration.this,OTPVerification.class);
        i.putExtra("registrationDetailsBundle",bundle);
        startActivity(i);
    }


}
