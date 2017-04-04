package com.example.bhagat.finalyear;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by bhagat on 10/5/16.
 */

public class RequestDetails extends DialogFragment implements View.OnClickListener{

    String consumerName, categoryName, distance, quantity, request_id,address,phno,dueDate;
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
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
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
        phno = args.getString("consumerPhno");
        address = args.getString("address");
        dueDate = args.getString("dueDate");

        ((TextView) view.findViewById(R.id.customerName) ).setText(consumerName);
        //todo: Make a request to server for fetching complete address of consumer from REQUEST table
        ((TextView) view.findViewById(R.id.customerAddress) ).setText(address);
        ((TextView) view.findViewById(R.id.category) ).setText(categoryName);
        ((TextView) view.findViewById(R.id.quantity) ).setText(quantity);
        ((TextView) view.findViewById(R.id.due_date)).setText("Due date: " + dueDate);

        TextView phoneNo = (TextView) view.findViewById(R.id.consumer_phno);
        phoneNo.setText(phno);

        phoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPermissionGranted()){

                    call_action();
                }
            }
        });

        //to remove dialog when click outside of it
            //setCancelable(true);
        return view;
    }

    public void call_action(){
        String phoneNumber = "tel:"+phno;
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(phoneNumber));
        startActivity(callIntent);
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();
                    call_action();
                } else {
                    Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    public  boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }
}

