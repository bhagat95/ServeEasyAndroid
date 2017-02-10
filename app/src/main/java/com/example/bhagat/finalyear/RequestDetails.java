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

    String consumerName, categoryName, distance, quantity, request_id;
    Integer listItemPosition;
    Button accept, decline;
    //RequestDialogResponse requestDialogResponse;

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
        Log.d("onAccept", "called");
        Toast.makeText(getActivity(), "Dabaya re bhai", Toast.LENGTH_SHORT).show();
        try {
            RequestDialogResponse requestDialogResponseListener = (RequestDialogResponse)getTargetFragment();
            requestDialogResponseListener.onDialogResponse("accept", listItemPosition);
        }
        catch(Exception e)
        {
            Log.e("onAcceptError", e.toString());
        }
        dismiss();
    }
    public void onDecline(){
        Log.d("onDecline", "called");
        Toast.makeText(getActivity(), "Dabaya re bhai", Toast.LENGTH_SHORT).show();
        try {
            RequestDialogResponse requestDialogResponseListener = (RequestDialogResponse)getTargetFragment();
            requestDialogResponseListener.onDialogResponse("decline", listItemPosition);
        }
        catch(Exception e)
        {
            Log.e("onDeclinetError", e.toString());
        }
        dismiss();
    }

    public interface RequestDialogResponse{
        void onDialogResponse(String response, int position);
    }


/*
    //Attach the interface to parent fragment
    public void onAttachToParentFragment(Fragment fragment)
    {
        try
        {
            requestDialogResponse = (RequestDialogResponse)fragment;

        }
        catch (Exception e)
        {
            Log.e("onAttachToParent", e.toString());
        }
    }
/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        requestDialogResponse = (RequestDialogResponse) context;
    }
*/



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.request_details, null);

        /*
        TextView customerName, customerAddress, quantity;

        customerName = (TextView) view.findViewById(R.id.customerName);
        customerAddress = (TextView) view.findViewById(R.id.customerAddress);
        quantity = (TextView) view.findViewById(R.id.quantity);
        */
        accept = (Button) view.findViewById(R.id.accept);
        decline = (Button) view.findViewById(R.id.decline);
        accept.setOnClickListener(this);
        decline.setOnClickListener(this);
  //      onAttachToParentFragment(getParentFragment());

        Bundle args = getArguments();
        consumerName = args.getString("consumerName");
        categoryName = args.getString("categoryName");
        distance = args.getString("distance");
        quantity = args.getString("quantity");
        request_id = args.getString("request_id");
        listItemPosition = args.getInt("listItemPosition");

        ((TextView) view.findViewById(R.id.customerName) ).setText(consumerName);
        ((TextView) view.findViewById(R.id.customerAddress) ).setText("ADDRESS MISSING");
        ((TextView) view.findViewById(R.id.category) ).setText(categoryName);
        ((TextView) view.findViewById(R.id.quantity) ).setText(quantity);

        //to remove dialog when click outside of it
        //setCancelable(true);

        return view;//inflater.inflate(R.layout.request_details, null);
    }


    /*
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //String consumerName = getArguments().getString("consumerName");
        //String categoryName = args.getString("consumerName");
        //String distance = args.getString("consumerName");
        //String quantity = args.getString("quantity");
        //String request_id = args.getString("request_id");
        //Toast.makeText(getActivity(),consumerName,Toast.LENGTH_LONG).show();
        //return super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //TextView consumer = (TextView)getActivity().findViewById(R.id.consumer);
        //consumer.setText(consumerName);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout


        builder.setView(inflater.inflate(R.layout.request_details, null))
                // Add action buttons
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...

                    }
                })
                .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //LoginDialogFragment.this.getDialog().cancel();
                    }
                });

        TextView name = (TextView)(getActivity()).findViewById(R.id.customerName);
        name.setText(consumerName);

        Bundle backToParentFragment = new Bundle();
        backToParentFragment.putBoolean("accept",true);
        getParentFragment().setArguments(backToParentFragment);
        return builder.create();

    }

    @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            //return super.onCreateView(inflater, container, savedInstanceState);
            return inflater.inflate( R.layout.request_details, container, false);
        }
        */
}




        /*
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Accept", "clicked");
                onAccept();
            }
        });
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //requestDialogResponse.onDialogResponse("decline");
                dismiss();
            }
        });
        */