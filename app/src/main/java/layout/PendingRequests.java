package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
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

public class PendingRequests extends Fragment implements RequestDetails.RequestDialogResponse,SwipeRefreshLayout.OnRefreshListener {

    ListView requestsList;
    ArrayList<ListData> arrayOfItems;
    RequestsAdapter adapter;
    String request_id = "",consumerAddress,consumerPhno,dueDate;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_pending_requests, container, false);
        requestsList = (ListView) v.findViewById(R.id.list);
        arrayOfItems = new ArrayList<>();
        //getRequests();
        adapter = new RequestsAdapter(getContext(), 0, arrayOfItems);
        requestsList.setAdapter(adapter);

        requestsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView consumerNameView = (TextView)view.findViewById(R.id.consumer);
                String consumerName = consumerNameView.getText().toString();

                TextView categoryNameView = (TextView)view.findViewById(R.id.category);
                String categoryName = categoryNameView.getText().toString();

                //todo: distance is to be calculated (Eucledean/distance-matrix)

                TextView distanceView = (TextView)view.findViewById(R.id.distance);
                String distance = distanceView.getText().toString();

                TextView quantityView = (TextView)view.findViewById(R.id.quantity);
                String quantity = quantityView.getText().toString();

                try {
                    request_id = arrayOfItems.get(i).jOb.getString("request_id");
                    consumerAddress = arrayOfItems.get(i).jOb.getString("address");
                    consumerPhno = arrayOfItems.get(i).jOb.getString("consumer_phno");
                    dueDate = arrayOfItems.get(i).jOb.getString("due_date");
                }
                catch (Exception e){
                }
                showDetails(i,consumerName,categoryName,distance,quantity,request_id,consumerAddress,consumerPhno,dueDate);
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        getRequests();
                                    }
                                }
        );
        return v;
    }


    private void showDetails(int position,String consumerName,String categoryName,String distance, String quantity,String request_id,String address,String consumerPhno,String dueDate) {
        FragmentManager fm = getFragmentManager();

        Bundle args = new Bundle();
        args.putString("consumerName",consumerName);
        args.putString("categoryName",categoryName);
        args.putString("quantity",quantity);
        args.putString("distance",distance);
        args.putString("request_id",request_id);
        args.putString("address",address);
        args.putString("consumerPhno",consumerPhno);
        args.putString("dueDate", dueDate);
        args.putInt("listItemPosition", position);


        RequestDetails details = RequestDetails.newInstance();
        details.setTargetFragment(this, 0);
        details.setArguments(args);
        details.show(fm,"pendingDialogTag");

    }


    @Override
    public void onDialogResponse(String response, int position) {
        if(response.equals("accept")){ // Here, accept == done
            makeRequestStatusDelivered();
            //todo:now check if this moved to transactions
            arrayOfItems.remove(position);
            requestsList.invalidateViews();
        }
        else if (response.equals("decline")){ // Here,decline == cancel
            //adapter.remove(adapter.getItem(position));
           // Log.d("listSize", arrayOfItems.size()+"");
            makeRequestStatusCancelled();
            //todo:now check if this moved to transactions
            arrayOfItems.remove(position);
            requestsList.invalidateViews();
            //Log.d("newListSize", arrayOfItems.size()+"");
        }
    }


    void makeRequestStatusDelivered(){
        Toast.makeText(getActivity(),"Done clicked",Toast.LENGTH_LONG).show();
        Map<String, String> params = new HashMap<>();
        params.put("status","3");// move to transactions
        params.put("request_id",request_id);
        String url = UserDetails.getInstance().url + "update_request_status.php";
        VolleyNetworkManager.getInstance(getContext()).makeRequest(params, url,
                new VolleyNetworkManager.Callback() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d("makeStatusDelivered",response);
                    }
                    @Override
                    public void onError(String error) {
                        Toast.makeText(getActivity(),error,Toast.LENGTH_LONG).show();
                    }
                });
        //here, no notification will be sent, only transactions will move to transactions page for provider
    }

    void makeRequestStatusCancelled(){
        Map<String, String> params = new HashMap<>();
        params.put("status","4"); // move to transactions
        params.put("request_id",request_id);
        String url = UserDetails.getInstance().url + "update_request_status.php";
        VolleyNetworkManager.getInstance(getContext()).makeRequest(params, url,
                new VolleyNetworkManager.Callback() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d("makeStatusCancelled",response);
                    }
                    @Override
                    public void onError(String error) {
                        Toast.makeText(getActivity(),error,Toast.LENGTH_LONG).show();
                    }
                });
    }


    void getRequests() {
        Map<String, String> params = new HashMap<>();
        params.put("provider_id", UserDetails.getInstance().providerId);
        params.put("type","pending");
        String url = UserDetails.getInstance().url + "fetch_requests.php";
        VolleyNetworkManager.getInstance(getContext()).makeRequest(params, url,
                new VolleyNetworkManager.Callback() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d("PendingRequest response", response);
                        try {
                            if(arrayOfItems!=null)
                                arrayOfItems.clear();
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jOb = jsonArray.getJSONObject(i);
                                arrayOfItems.add(new ListData(jOb));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    @Override
                    public void onError(String error) {
                        Toast.makeText(getActivity(),error,Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onRefresh() {
        getRequests();
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