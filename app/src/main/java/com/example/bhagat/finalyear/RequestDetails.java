package com.example.bhagat.finalyear;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by bhagat on 10/5/16.
 */

public class RequestDetails extends DialogFragment implements View.OnClickListener{

    String consumerName, categoryName, distance, quantity, request_id,address;
    int listItemPosition;
    Button accept, decline;
    public RequestDetails(){
    }
    public static RequestDetails newInstance(){
        return new RequestDetails();
    }
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.accept) {
            Log.d("Accept", "clicked");
            onAccept();
        }
        else if(view.getId() == R.id.decline){
            Log.d("Decline", "clicked");
            onDecline();
        }
    }
    public void onAccept(){
        try {
            RequestDialogResponse requestDialogResponseListener = (RequestDialogResponse)getTargetFragment();
            requestDialogResponseListener.onDialogResponse("accept", listItemPosition);
        }
        catch(Exception e) {
            Log.e("onAcceptError", e.toString());
        }
        dismiss();
    }

    public void onDecline(){
        try {
            RequestDialogResponse requestDialogResponseListener = (RequestDialogResponse)getTargetFragment();
            requestDialogResponseListener.onDialogResponse("decline", listItemPosition);
        }
        catch(Exception e) {
            Log.e("onDeclinetError", e.toString());
        }
        dismiss();
    }

    public interface RequestDialogResponse{
        void onDialogResponse(String response, int position);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.request_details, null);

        accept = (Button) view.findViewById(R.id.accept);
        decline = (Button) view.findViewById(R.id.decline);
        accept.setOnClickListener(this);
        decline.setOnClickListener(this);
        if(this.getTag() == "activeDialogTag"){
            accept.setText("Accept");
            decline.setText("Decline");
        }
        else{
            accept.setText("Done");
            decline.setText("Cancel");
        }
        Bundle args = getArguments();
        consumerName = args.getString("consumerName");
        categoryName = args.getString("categoryName");
        distance = args.getString("distance");
        quantity = args.getString("quantity");
        request_id = args.getString("request_id");
        listItemPosition = args.getInt("listItemPosition");
        address = args.getString("address");
        ((TextView) view.findViewById(R.id.customerName) ).setText(consumerName);
        //todo: Make a request to server for fetching complete address of consumer from REQUEST table
        ((TextView) view.findViewById(R.id.customerAddress) ).setText(address);
        ((TextView) view.findViewById(R.id.category) ).setText(categoryName);
        ((TextView) view.findViewById(R.id.quantity) ).setText(quantity);
        //to remove dialog when click outside of it
            //setCancelable(true);
        return view;
    }
}

