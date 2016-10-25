package com.example.bhagat.finalyear;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.util.Locale;
import java.util.Map;


public class MakeRequest extends AppCompatActivity {

    com.android.volley.RequestQueue requestQueue;
    JSONArray jArr;
    String selectedCategoryId = "", serviceId= "";
    ImageButton dueDate, callButton;
    Spinner selectCategory;
    Button submit;
    //EditText quan
    int year, month, day;
    static final int DIALOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_request);

        requestQueue = Volley.newRequestQueue(this);

        serviceId = getIntent().getStringExtra("serviceId");

        submit  = (Button) findViewById(R.id.submit);
        callButton = (ImageButton) findViewById(R.id.call_button);
        dueDate = (ImageButton) findViewById(R.id.pick_date_button);
        selectCategory = (Spinner) findViewById(R.id.select_category);

        //set current date
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        year = calendar.get(java.util.Calendar.YEAR);
        month = calendar.get(java.util.Calendar.MONTH);
        day = calendar.get(java.util.Calendar.DAY_OF_MONTH);

        //call
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = "tel:"+"09199095326";
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(phoneNumber));

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Log.d("Call permission", "denied");
                    return;
                }

                Log.d("Call", "success");
                startActivity(callIntent);
            }
        });

        //set date
        dueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showDialogOnClick();
                showDialog(DIALOG_ID);
            }
        });

        //inflate spinner
        inflateCategorySpinner();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitRequst();
            }
        });
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == DIALOG_ID)
            return new DatePickerDialog(this,dplistener,year,month,day);
        return null;
    }


    DatePickerDialog.OnDateSetListener dplistener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            year = i ;
            month = i1 + 1;
            day = i2;
            Log.d("Date ","              "+day+"/"+ month +"/"+year);
        }
    };


    ///volley
    public void inflateCategorySpinner(){
        String url = UserDetails.getInstance().url+"fetch_categories.php";
        //->GET REQUEST USING VOLLEY
          StringRequest request = new StringRequest(Request.Method.POST,url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                    Log.d("Spinner Response", response);

                    ArrayList<String> categoryArrayList = new ArrayList<String>();

                    try {
                        JSONObject jOb = new JSONObject(response);
                        jArr = jOb.getJSONArray("category_list");
                        for(int i=0; i<jArr.length(); i++){
                            categoryArrayList.add(jArr.getJSONObject(i).getString("category_name"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(MakeRequest.this,
                            android.R.layout.simple_spinner_dropdown_item, categoryArrayList);
                    selectCategory.setAdapter(categoryAdapter);
                    selectCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            try {
                                selectedCategoryId = (String) jArr.getJSONObject(position).getString("category_id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            Log.d("spinner category","nothing selected");
                        }
                    });

                }
            },
            new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        ) {
              @Override
              protected Map<String, String> getParams()
              {
                  Map<String, String> params = new HashMap<String, String>();
                  params.put("service_id", "3");
                  return params;
              }
          };
        requestQueue.add(request);
    }



    public  void  submitRequst() {
        final ProgressDialog progressDialog;
        String url = UserDetails.getInstance().url+"make_request.php";

        // prepare the Request
        //-> POST REQUEST USING VOLLEY
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        progressDialog.hide();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
                        progressDialog.hide();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("service_id", "3");
                //params.put("provider_id", "");
                params.put("consumer_id", UserDetails.getInstance().consumerId);
                params.put("category_id", selectedCategoryId);
                params.put("quantity", "4");
                params.put("consumer_locx", "23.33");
                params.put("consumer_locy", "24.3333");
                params.put("day", String.valueOf(day));
                params.put("month", month+"");
                params.put("year", String.valueOf(year));
                return params;
            }
        };
        requestQueue.add(postRequest);

    }


}
