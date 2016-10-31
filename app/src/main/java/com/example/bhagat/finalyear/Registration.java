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

import java.util.HashMap;
import java.util.Map;


class User {
    String name, phone, address, password;
    boolean provider;

    User(String name, String phone, String address, String password, boolean provider) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.provider = provider;
    }
}

public class Registration extends AppCompatActivity {

    EditText name, phone, address, password, confirmPassword;
    RadioButton radioConsumer, radioProvider;
    RadioGroup radioGroup;
    Button submit;
    User user;

    String url = "http://192.168.137.1/se_register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = (EditText) findViewById(R.id.registration_name);
        phone = (EditText) findViewById(R.id.registration_phone);
        password = (EditText) findViewById(R.id.registration_password);
        confirmPassword = (EditText) findViewById(R.id.registration_confirm_password);
        address = (EditText) findViewById(R.id.registration_address);
        radioGroup = (RadioGroup) findViewById(R.id.registration_radio_group);
        radioConsumer = (RadioButton) findViewById(R.id.registration_consumer);
        radioProvider = (RadioButton) findViewById(R.id.registration_provider);
        submit = (Button) findViewById(R.id.registration_submit);
        //gpsTracker = new GPSTracker(this);
        //Toast.makeText(getApplicationContext(), gpsTracker.latitude + " " + gpsTracker.longitude, Toast.LENGTH_LONG).show();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user = new User(name.getText().toString(), phone.getText().toString(), password.getText().toString(),
                        address.getText().toString(), radioProvider.isChecked());
                Log.d("radioprovider", radioProvider.isChecked()+"");

                registerUser();
            }
        });
    }

    ProgressDialog pDialog;

    boolean registerUser() {

        //post request using volley

        com.android.volley.RequestQueue queue = Volley.newRequestQueue(this);

        //setChunkedStreamingMode(0);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Registering...");
        pDialog.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response123", response);
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        pDialog.hide();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("responseError", error.getMessage());
                        pDialog.hide();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                boolean provider = user.provider;
                params.put("name", user.name);
                params.put("phone", user.phone);
                params.put("password", user.password);
                //params.put("email", user.);
                params.put("address", user.address);
                //params.put("phno", user.phno);
                //type 1 -> provider
                Log.d("provider", provider+"");
                params.put("type", provider?"1":"0");
                params.put("loc_x", "123456789");
                params.put("loc_y", "987654321");
                Log.d("params", user.name);
                return params;
            }
        };

        ///TO stop volley from making multiple POST requests !!!
        //postRequest.setRetryPolicy(new DefaultRetryPolicy
          //      (0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        ///Alternatively, you can also use this
        /*postRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        */

        queue.add(postRequest);

        return true;
    }

}


//lawda

/*
Please check the register user name already exists
*/
