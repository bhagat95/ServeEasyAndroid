package com.example.bhagat.finalyear;
import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.example.bhagat.finalyear.Login.context;


public class MakeRequest extends AppCompatActivity {

    com.android.volley.RequestQueue requestQueue;
    JSONArray jArr;
    String selectedCategoryId = "", serviceIdVal= "",providerNameVal="",serviceNameVal="",providerPhnoVal="";
    ImageView dueDate;
    FloatingActionButton callButton;
    TextView selectDate,serviceName,providerName,providerPhno;
    EditText quantity,address;
    Spinner selectCategory;
    CardView submit;
    int year, month, day, dueDay,dueMonth, dueYear;
    static final int DIALOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_request);


        requestQueue = Volley.newRequestQueue(this);

        serviceIdVal = getIntent().getStringExtra("serviceId");
        providerNameVal = getIntent().getStringExtra("providerName");
        serviceNameVal = getIntent().getStringExtra("serviceName");
        providerPhnoVal = getIntent().getStringExtra("providerPhno");

        selectDate = (TextView)findViewById(R.id.select_date);
        providerName = (TextView)findViewById(R.id.provider_name);
        serviceName = (TextView)findViewById(R.id.service_name);
        providerPhno = (TextView)findViewById(R.id.provider_phno);
        quantity = (EditText)findViewById(R.id.quantity);
        address = (EditText)findViewById(R.id.address);

        providerName.setText(providerNameVal);
        serviceName.setText(serviceNameVal);
        providerPhno.setText(providerPhnoVal);

        Toast.makeText(this,serviceIdVal +"",Toast.LENGTH_LONG).show();

        submit  = (CardView) findViewById(R.id.submit_button);
        callButton = (FloatingActionButton) findViewById(R.id.call_button);
        dueDate = (ImageView) findViewById(R.id.pick_date_button);
        selectCategory = (Spinner) findViewById(R.id.select_category);

        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        Toast.makeText(this,"date" + date,Toast.LENGTH_LONG).show();

        //set current date
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        year = calendar.get(java.util.Calendar.YEAR);
        month = calendar.get(java.util.Calendar.MONTH) + 1;
        day = calendar.get(java.util.Calendar.DAY_OF_MONTH);


        dueDay = day;
        dueMonth = month;
        dueYear = year;


        //call
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = "tel:"+providerPhnoVal;
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(phoneNumber));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
            dueYear = i ;
            dueMonth = i1 + 1;
            dueDay = i2;
            selectDate.setText(dueDay+"/"+dueMonth+"/"+dueYear);
            Log.d("Date ",""+dueDay+"/"+ dueMonth +"/"+dueYear);
        }
    };

    /*DatePickerDialog datePickerDialog = new DatePickerDialog(context, DatePickerListener, year, month, day);
    datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
    datePickerDialog.show();*/



    ///volley
    public void inflateCategorySpinner(){
        String url = UserDetails.getInstance().url+"fetch_categories.php";
        Map<String, String> params = new HashMap<>();
        params.put("service_id", serviceIdVal);
        VolleyNetworkManager.getInstance(getApplicationContext()).makeRequest(params,
                url, new VolleyNetworkManager.Callback() {
                    @Override
                    public void onSuccess(String response) {
                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        Log.d("Spinner Response", response);

                        ArrayList<String> categoryArrayList = new ArrayList<>();

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
                                    selectedCategoryId = jArr.getJSONObject(position).getString("category_id");
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

                    @Override
                    public void onError(String error) {
                        Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();
                    }
                });
    }

    public  void  submitRequst() {

        String url = UserDetails.getInstance().url+"make_request.php";

        Map<String, String> params = new HashMap<>();
        params.put("day", String.valueOf(day));
        params.put("month", String.valueOf(month));
        params.put("year", String.valueOf(year));

        //Log.d("day",String.valueOf(day));

        params.put("service_id", serviceIdVal);
        params.put("consumer_id", UserDetails.getInstance().consumerId);
        params.put("category_id", selectedCategoryId);
        params.put("quantity", quantity.getText().toString());
        params.put("consumer_locx", UserDetails.getInstance().latitude);
        params.put("consumer_locy", UserDetails.getInstance().longitude);
        params.put("address",address.getText().toString());
        params.put("due_day", String.valueOf(dueDay));
        params.put("due_month", String.valueOf(dueMonth));
        params.put("due_year", String.valueOf(dueYear));






        VolleyNetworkManager.getInstance(getApplicationContext()).makeRequest(params,
                url, new VolleyNetworkManager.Callback() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d("Response123", response);
                        //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),ConsumerHome.class);
                        startActivity(intent);
                        finish();
                        /// /new Intent(getApplicationContext(),NearbyServices.class));
                    }
                    @Override
                    public void onError(String error) {
                        Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();
                    }
                });
    }


}
