package com.example.bhagat.finalyear;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by bhagat on 1/19/17.
 */
public class ProviderTransactionsAdapter extends RecyclerView
        .Adapter<ProviderTransactionsAdapter.DataObjectHolder> {

    public ArrayList<String> listOfItems;

    public static MyClickListener myClickListener;

    public ProviderTransactionsAdapter(Context context, int resource, ArrayList<String> listOfItems) { //ListData
        this.listOfItems = listOfItems;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.provider_transactions_card_view_row, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.consumerName.setText("launda"+position);//listOfItems.get(position).getmText1());
        holder.status.setText("delivered"+position);//listOfItems.get(position).getmText2());
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

    @Override
    public int getItemCount() {
        return listOfItems.size();
    }





    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView consumerName;
        TextView status;

        public DataObjectHolder(View itemView) {
            super(itemView);
            consumerName = (TextView) itemView.findViewById(R.id.consumer_name);
            status = (TextView) itemView.findViewById(R.id.status);
            Log.i("LOG_TAG", "Adding Listener");
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

}
