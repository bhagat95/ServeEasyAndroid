package com.example.bhagat.finalyear;

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
        arrayOfItems = new ArrayList<ListData>();

        getDataSet();

        myAdapter = new ProviderTransactionsAdapter(getContext(), 0, arrayOfItems);
        myRecyclerView.setAdapter(myAdapter);

        Toast.makeText(getContext(), "chal gaya re bhai", Toast.LENGTH_SHORT).show();
    }

    void getDataSet() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("provider_id",UserDetails.getInstance().providerId);
        VolleyNetworkManager.getInstance(getContext()).makeRequest(params,
                "fetch_provider_transactions.php", new VolleyNetworkManager.Callback() {
                    @Override
                    public void onSuccess(String response) {

                        Log.d("ProviderTransactions", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jOb = jsonArray.getJSONObject(i);
                                arrayOfItems.add(new ListData(jOb));
                            }

                            myAdapter = new ProviderTransactionsAdapter(getContext(), 0, arrayOfItems);
                            myRecyclerView.setAdapter(myAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

    }
}
