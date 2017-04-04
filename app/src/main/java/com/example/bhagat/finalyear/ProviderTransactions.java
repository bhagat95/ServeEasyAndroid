package com.example.bhagat.finalyear;

import android.app.ProgressDialog;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ProviderTransactions extends Fragment {

    RecyclerView myRecyclerView;
    RecyclerView.Adapter myAdapter;
    LinearLayoutManager myLayoutManager;
    ArrayList<ListData> arrayOfItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_provider_transactions);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_provider_transactions, container, false);
        return v;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myRecyclerView = (RecyclerView) getActivity().findViewById(R.id.myRecyclerView);
        myRecyclerView.setHasFixedSize(true);
        myLayoutManager = new LinearLayoutManager(getContext());
        myRecyclerView.setLayoutManager(myLayoutManager);
        arrayOfItems = new ArrayList<>();
        getDataSet();
        //myAdapter = new ProviderTransactionsAdapter(arrayOfItems);
        //myRecyclerView.setAdapter(myAdapter);

    }

    void getDataSet() {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        Map<String, String> params = new HashMap<>();
        params.put("provider_id",UserDetails.getInstance().userId);
        String url = UserDetails.getInstance().url + "fetch_provider_transactions.php";
        VolleyNetworkManager.getInstance(getContext()).makeRequest(params,
                url, new VolleyNetworkManager.Callback() {
                    @Override
                    public void onSuccess(String response) {
                        pDialog.hide();
                        Log.d("ProviderTransactions", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jOb = jsonArray.getJSONObject(i);
                                arrayOfItems.add(new ListData(jOb));
                            }
                            Collections.reverse(arrayOfItems);
                            myAdapter = new ProviderTransactionsAdapter(arrayOfItems,getActivity());
                            myRecyclerView.setAdapter(myAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(String error) {
                        pDialog.hide();
                        Toast.makeText(getActivity(),error,Toast.LENGTH_LONG).show();
                    }
                });
    }
}