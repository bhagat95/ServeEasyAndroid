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

    public ConsumerTransactionsAdapter(Context context, int resource, ArrayList<ListData> listOfItems) { //ListData
        //super(context, attrs);
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
            holder.consumerName.setText(arrayOfItems.get(position).jOb.getString("consumer_name"));
            holder.categoryName.setText(arrayOfItems.get(position).jOb.getString("category_name"));
            holder.status.setText(arrayOfItems.get(position).jOb.getString("status"));
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
        TextView consumerName, categoryName;
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
