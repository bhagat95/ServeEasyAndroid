package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PendingRequests.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PendingRequests#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PendingRequests extends Fragment {

    ListView requestsList;
    ArrayList<ListData> arrayOfItems;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

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
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

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
                        Log.d("fetch_services error",error.toString());
                    }
                }
        )

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("provider_id", UserDetails.getInstance().providerId);
                params.put("type", "active");

                return params;
            }
        } ;
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
