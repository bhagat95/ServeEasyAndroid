package com.example.bhagat.finalyear;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

public class NearbyServices extends AppCompatActivity {

    RequestQueue requestQueue;
    ListView requestsList;
    ArrayList<ListData> arrayOfItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_services);

        requestsList = (ListView) findViewById(R.id.list);

        //  ListView
        arrayOfItems = new ArrayList<ListData>();

        getServices();

        requestsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {

                    startActivity(new Intent(NearbyServices.this, MakeRequest.class).
                            putExtra("serviceId", arrayOfItems.get(i).jOb.getString("service_id")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        //temporary add
        //for (int i = 0; i < 10; i++)
          //  arrayOfItems.add(new ListData());

    }

    ///volley
    void getServices() {
        requestQueue = Volley.newRequestQueue(this);

        String url = UserDetails.getInstance().url + "fetch_services.php";
        //->GET REQUEST USING VOLLEY
        JsonArrayRequest request = new JsonArrayRequest(url, // Request.Method.POST
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jOb = response.getJSONObject(i);
                                arrayOfItems.add(new ListData(jOb));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        NearbyServicesAdapter adapter = new NearbyServicesAdapter(NearbyServices.this, 0, arrayOfItems);
                        requestsList.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("fetch_services error",error.toString());
                    }
                }
        )
        /*
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("consumer_locx", "3");
                params.put("consumer_locy", "3");
                return params;
            }
        }
        */
        ;
        requestQueue.add(request);
    }


}


