package com.example.bhagat.finalyear;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by bhagat on 2/5/17.
 */
public class VolleyNetworkManager {

    //makeRequestString is not a local variable but an instance so inner class can reference it from container object.
    String makeRequestResponse="";

    private static final String TAG = "MakeVolleyRequest";

    private static VolleyNetworkManager instance = null;

    private static final String prefixURL = UserDetails.getInstance().url;


    public interface Callback{
        void onSuccess(String response);
    }


    //for Volley API
    public RequestQueue requestQueue;

    private VolleyNetworkManager(Context context)
    {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        //other stuff if you need
    }
//todo : why synchronized is used?
    public static synchronized VolleyNetworkManager getInstance(Context context)
    {
        if (null == instance)
            instance = new VolleyNetworkManager(context);
        return instance;
    }
/*
    //this is so you don't need to pass context each time
    public static synchronized VolleyNetworkManager getInstance()
    {
        if (null == instance)
        {
            throw new IllegalStateException(VolleyNetworkManager.class.getSimpleName() +
                   " is not initialized, call getInstance(...) first");
        }
        return instance;
    }
*/
    public void makeRequest(final Map<String, String> params, String url, final Callback callback) //SomeCustomListener<String> listener) final Response.Listener<String> listener
    {
        //update prefixURL

        StringRequest request = new StringRequest(Request.Method.POST, prefixURL + url, //new JSONObject(jsonParams),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.d(TAG + ": ", "somePostRequest Response : " );
                        makeRequestResponse = response;
                        callback.onSuccess(response);
                        /*
                        if(null != response.toString())
                            listener.getResult(response.toString());
                            */
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e(TAG , "Error Response code: " + error.toString());//error.networkResponse.statusCode);
                        /*
                        if (null != error.networkResponse)
                        {
                            listener.getResult(false);
                        }
                        */
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() {
                        //iterate all over hashmap to put all the strings
                        Iterator<Map.Entry<String, String>> entries = params.entrySet().iterator();
                        while (entries.hasNext()) {
                            Map.Entry<String, String> entry = entries.next();
                            Log.d("Key=" + entry.getKey() , ": Value = " + entry.getValue());
                            params.put(entry.getKey(),entry.getValue());
                        }
                        /*
                        params.put("status","2");
                        params.put("request_id",request_id);
                        */
                        return params;
                    }
                };

        requestQueue.add(request);
    }
}
