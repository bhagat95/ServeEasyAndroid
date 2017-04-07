package com.example.bhagat.finalyear;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.content.Context.CONNECTIVITY_SERVICE;

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
        void onError(String error);
    }


    //for Volley API
    public RequestQueue requestQueue;

    private VolleyNetworkManager(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        //other stuff if you need
    }
    //todo : why synchronized is used?
    public static  synchronized  VolleyNetworkManager getInstance(Context context) {
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

        StringRequest request = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    Log.d(TAG + ": ", "somePostRequest Response : " + response);
                    makeRequestResponse = response;
                    callback.onSuccess(response);
                        /*
                        if(null != response.toString())
                            listener.getResult(response.toString());
                            */
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e(TAG , "Error Response code: " + volleyError.toString());//error.networkResponse.statusCode);
                        String message = null;
                        if (volleyError instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                            } else if (volleyError instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                        } else if (volleyError instanceof AuthFailureError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                        } else if (volleyError instanceof NoConnectionError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                        }
                        callback.onError(message);
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
                return params;
            }
        };
        requestQueue.add(request);
    }
}