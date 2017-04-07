package com.example.bhagat.finalyear;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
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

import java.util.HashMap;
import java.util.Map;


/**
 * Created by bhagat on 2/6/17.
 */
public class ProviderBackgroundService extends Service {

    RequestQueue requestQueue;
    NotificationCompat.Builder notification;
    private static int notificationID;
    BackgroundService backgroundService;
    boolean isRunning = false;
    @Override
    public void onCreate() {
        //what's the use of calling super?
        super.onCreate();
        /*
        The system invokes this method to perform one-time setup procedures when the service is initially
        created (before it calls either onStartCommand() or onBind()). If the service is already running,
        this method is not called.
         */
        backgroundService = new BackgroundService();

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);
        notificationID = 1;
    }


    public void generateNotifications(String categoryName, String consumerName, String quantity){
        //Build the notification
        int requestCount = 2;
        notification.setSmallIcon(R.drawable.serve_easy_logo);
        notification.setTicker("New request");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle(categoryName);
        notification.setContentText(consumerName+" requested for "+quantity+" "+categoryName);
        Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.freeze_sound);
        notification.setSound(sound);
        Intent intent = new Intent(this, ProviderHome.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);
        //Builds notification and issues it
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(notificationID, notification.build());

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        Log.d("ProviderService", "started");
        if (!backgroundService.isRunning()) {
            backgroundService.start();
            isRunning = true;
        }

        return START_STICKY;//super.onStartCommand(intent, flags, startId);
    }





    class BackgroundService extends Thread {

        public long TIME_GAP = 6000;
        // Handler networkRequest= new Handler();
        Runnable runTask = new Runnable() {
            int count = 0;
            @Override
            public void run() {
                isRunning = true;
                while (isRunning) {
                    count++;
                    try {
                        Log.d("PBackgroundServiceCount", count+"");
                        fetchNotifications();
                        Thread.sleep(TIME_GAP);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        isRunning = false;
                    }
                }
            }
        };
        public boolean isRunning() {
            return isRunning;
        }
        @Override
        public void run() {
            super.run();
            runTask.run();
        }
    }

    public void fetchNotifications(){
        String url = UserDetails.getInstance().url + "fetch_provider_notifications.php";
        StringRequest request = new StringRequest( Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ProviderServiceResponse", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jOb = jsonArray.getJSONObject(i);
                                //arrayOfItems.add(new ListData(jOb));
                                markRequestSeen(jOb);
                                //category_name, quantity, consumer_name, category_name
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("fetch_requests error",error.toString());
                    }
                }
        )

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("provider_id", UserDetails.getInstance().userId);
                return params;
            }
        } ;
        requestQueue.add(request);

    }

    public void markRequestSeen(final JSONObject jOb){
        String url = UserDetails.getInstance().url + "mark_request_seen.php";
        String temp=null;
        try{
            temp = jOb.getString("request_id");
        }catch (Exception e){
            Log.e("markRequestSeenID",e.toString());
        }
        final String requestID = temp;
        StringRequest request = new StringRequest( Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("markRequestSeenResponse", response);
                        try{
                            generateNotifications(jOb.getString("category_name"),
                                    jOb.getString("consumer_name"),
                                    jOb.getString("quantity"));
                            notificationID++;
                        }
                        catch (Exception e){
                            Log.e("generateNotifJSON",e.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("markRequestSeen error",error.toString());
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("request_id",requestID);
                params.put("seen_val","1");
                return params;
            }
        } ;
        requestQueue.add(request);
    }



    @Override
    public void onDestroy() {
            Log.d("destroy_pservice","Provider Service Destroyed");
            backgroundService.interrupt();
            isRunning = false;
            backgroundService = null;
        super.onDestroy();
        /*
        The system invokes this method when the service is no longer used and is being destroyed.
        Your service should implement this to clean up any resources such as threads, registered listeners,
        or receivers. This is the last call that the service receives.
         */

    }

    @Override
    public boolean stopService(Intent name) {
        isRunning = false;
        return super.stopService(name);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}