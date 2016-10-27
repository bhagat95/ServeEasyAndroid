package layout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bhagat.finalyear.ListData;
import com.example.bhagat.finalyear.MainActivity;
import com.example.bhagat.finalyear.NearbyServicesAdapter;
import com.example.bhagat.finalyear.R;
import com.example.bhagat.finalyear.RequestDetails;
import com.example.bhagat.finalyear.RequestsAdapter;
import com.example.bhagat.finalyear.UserDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bhagat on 10/9/16.
 */
public class ActiveRequests extends Fragment {

    ListView requestsList;
    ArrayList<ListData> arrayOfItems;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_active_requests, container, false);

        requestsList = (ListView) v.findViewById(R.id.list);

        //  ListView
        arrayOfItems = new ArrayList<ListData>();
        //temporary add
        //for(int i =0; i<10;i++)
        //  arrayOfItems.add(new ListData());
        arrayOfItems = new ArrayList<ListData>();

        getRequests();

        RequestsAdapter adapter = new RequestsAdapter(getContext(), 0, arrayOfItems);
        requestsList.setAdapter(adapter);


//  To open the dialog for details
/*
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showDetails();
            }
        });
*/
        requestsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showDetails(i);
            }
        });


        return v;

    }
    private void showDetails(int position)
    {
        RequestDetails dialog = new RequestDetails();
        dialog.show(getFragmentManager(), "dialogTag");
    }

    void getRequests(){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        String url = UserDetails.getInstance().url + "fetch_requests.php";
        //->GET REQUEST USING VOLLEY
        StringRequest request = new StringRequest( Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ActiveRequests response", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jOb = jsonArray.getJSONObject(i);
                                arrayOfItems.add(new ListData(jOb));
                            }

                            } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        RequestsAdapter adapter = new RequestsAdapter(getActivity(), 0, arrayOfItems);
                        requestsList.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("fetch_requests error",error.toString());
                    }
                }
        )

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("provider_id", UserDetails.getInstance().providerId);
                params.put("type", "active");
                //params.put("consumer_locy", "3");
                return params;
            }
        } ;
        requestQueue.add(request);

    }

}
