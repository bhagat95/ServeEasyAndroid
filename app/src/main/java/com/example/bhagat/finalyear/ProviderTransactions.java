package com.example.bhagat.finalyear;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

public class ProviderTransactions extends AppCompatActivity {

    RecyclerView myRecyclerView;
    RecyclerView.Adapter myAdapter;
    LinearLayoutManager myLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_transactions);
        myRecyclerView = (RecyclerView) findViewById(R.id.myRecyclerView);
        myRecyclerView.setHasFixedSize(true);
        myLayoutManager = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(myLayoutManager);
        myAdapter = new ProviderTransactionsAdapter(this, 0, getDataSet());
        myRecyclerView.setAdapter(myAdapter);
        Toast.makeText(this, "chal gaya re bhai", Toast.LENGTH_SHORT).show();
    }



    ArrayList<String> getDataSet() {
        ArrayList results = new ArrayList<>();
        for (int index = 0; index < 20; index++) {
            String obj = new String("Halo re halo");
            results.add(obj);
        }
        return results;
    }
}
