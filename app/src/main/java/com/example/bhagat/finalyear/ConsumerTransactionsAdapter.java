package com.example.bhagat.finalyear;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by bhagat on 2/12/17.
 */
public class ConsumerTransactionsAdapter extends RecyclerView.Adapter<ConsumerTransactionsAdapter.DataObjectHolder> {

    public ArrayList<ListData> arrayOfItems;

    public static MyClickListener myClickListener;

    public ConsumerTransactionsAdapter( ArrayList<ListData> listOfItems) { //ListData

        this.arrayOfItems = listOfItems;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.consumer_transactions_adapter_card_view_row, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        try {
            holder.providerName.setText(arrayOfItems.get(position).jOb.getString("provider_name"));
            holder.categoryName.setText(arrayOfItems.get(position).jOb.getString("category_name"));
            holder.quantity.setText("Qty: "+ arrayOfItems.get(position).jOb.getString("quantity"));
            holder.status.setText(arrayOfItems.get(position).jOb.getString("status"));
            holder.date.setText(arrayOfItems.get(position).jOb.getString("date"));
            Log.d("transactions_quant", arrayOfItems.get(position).jOb.getString("quantity"));

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

    @Override
    public int getItemCount() {
        return arrayOfItems == null ? 0 : arrayOfItems.size();
    }


    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView providerName, categoryName, quantity;
        TextView status,date;

        public DataObjectHolder(View itemView) {
            super(itemView);
            providerName = (TextView) itemView.findViewById(R.id.provider_name);
            categoryName = (TextView) itemView.findViewById(R.id.category_name);
            quantity = (TextView) itemView.findViewById(R.id.quantity);
            status = (TextView) itemView.findViewById(R.id.status);
            date = (TextView) itemView.findViewById(R.id.date);
            Log.i("LOG_TAG", "Adding Listener");
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
         //   myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

}