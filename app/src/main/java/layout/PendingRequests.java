package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bhagat.finalyear.ListData;
import com.example.bhagat.finalyear.R;
import com.example.bhagat.finalyear.RequestDetails;
import com.example.bhagat.finalyear.RequestsAdapter;
import com.example.bhagat.finalyear.UserDetails;
import com.example.bhagat.finalyear.VolleyNetworkManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link //PendingRequests.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PendingRequests#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PendingRequests extends Fragment implements RequestDetails.RequestDialogResponse {

    ListView requestsList;
    ArrayList<ListData> arrayOfItems;
    RequestsAdapter adapter;
    String request_id = "";

    // TODO: update showDetails()


    public PendingRequests() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PendingRequests.
     */
    // TODO: Rename and change types and number of parameters
    public static PendingRequests newInstance(String param1, String param2) {
        PendingRequests fragment = new PendingRequests();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        */

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pending_requests, container, false);

        requestsList = (ListView) v.findViewById(R.id.list);

        //  ListView
        arrayOfItems = new ArrayList<ListData>();
        //temporary add
        //for(int i =0; i<10;i++)
        //  arrayOfItems.add(new ListData());
        arrayOfItems = new ArrayList<ListData>();

        getRequests();

        adapter = new RequestsAdapter(getContext(), 0, arrayOfItems);
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

                TextView consumerNameView = (TextView)view.findViewById(R.id.consumer);
                String consumerName = consumerNameView.getText().toString();

                TextView categoryNameView = (TextView)view.findViewById(R.id.category);
                String categoryName = categoryNameView.getText().toString();

                TextView distanceView = (TextView)view.findViewById(R.id.distance);
                String distance = distanceView.getText().toString();

                TextView quantityView = (TextView)view.findViewById(R.id.quantity);
                String quantity = quantityView.getText().toString();

                try {
                    request_id = arrayOfItems.get(i).jOb.getString("request_id");
                }
                catch (Exception e){

                }

                showDetails(i,consumerName,categoryName,distance,quantity,request_id);
            }
        });

        return v;
    }


    private void showDetails(int position,String consumerName,String categoryName,String distance, String quantity,String request_id) {
        FragmentManager fm = getFragmentManager();

        Bundle args = new Bundle();
        args.putInt("position",position);
        args.putString("consumerName",consumerName);
        args.putString("categoryName",categoryName);
        args.putString("quantity",quantity);
        args.putString("distance",distance);
        args.putString("request_id",request_id);
        args.putInt("listItemPosition", position);

        RequestDetails details = RequestDetails.newInstance();
        details.setTargetFragment(this, 0);
        details.setArguments(args);

        details.show(fm, "PendingDialogTag");

    }


    @Override
    public void onDialogResponse(String response, int position) {
        if(response.equals("accept")){
            Toast.makeText(getActivity(), "Accept ho gaya re bhai", Toast.LENGTH_SHORT).show();
            makeRequestStatusDelivered();
            arrayOfItems.remove(position);
            requestsList.invalidateViews();
        }
        else if (response.equals("decline")){
            //adapter.remove(adapter.getItem(position));
            Log.d("listSize", arrayOfItems.size()+"");
            //adapter.remove(adapter.getItem(position));
            makeRequestStatusCancelled();
            arrayOfItems.remove(position);
            requestsList.invalidateViews();
            //adapter.notifyDataSetChanged();
            Log.d("newListSize", arrayOfItems.size()+"");
            Toast.makeText(getActivity(), "serverUpdate "+position+arrayOfItems.size(), Toast.LENGTH_SHORT).show();
        }
    }


    void makeRequestStatusDelivered(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("status","3");
        params.put("request_id",request_id);
        VolleyNetworkManager.getInstance(getContext()).makeRequest(params, "update_request_status.php",
                new VolleyNetworkManager.Callback() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d("makeStatusDelivered",response);
                    }
                });
    }

    void makeRequestStatusCancelled(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("status","4");
        params.put("request_id",request_id);
        VolleyNetworkManager.getInstance(getContext()).makeRequest(params, "update_request_status.php",
                new VolleyNetworkManager.Callback() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d("makeStatusCancelled",response);
                    }
                });
    }


    void getRequests() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        String url = UserDetails.getInstance().url + "fetch_requests.php";
        //->GET REQUEST USING VOLLEY
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("PendingRequest response", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jOb = jsonArray.getJSONObject(i);
                                arrayOfItems.add(new ListData(jOb));

                            }
                            RequestsAdapter adapter = new RequestsAdapter(getActivity(), 0, arrayOfItems);
                            requestsList.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("fetch_services error", error.toString());
                    }
                }
        )

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("provider_id", UserDetails.getInstance().providerId);
                params.put("type", "pending");

                return params;
            }
        };
        requestQueue.add(request);

    }














    /*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    /*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    */
}
