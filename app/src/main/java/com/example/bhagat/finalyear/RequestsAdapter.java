package com.example.bhagat.finalyear;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;

import java.util.List;

/**
 * Created by bhagat on 10/5/16.
 */
public class RequestsAdapter extends ArrayAdapter<ListData> {

    Context context;
    LayoutInflater mInflater;
    List<ListData> objects ;
    public RequestsAdapter(Context context, int resource, List<ListData> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        if(convertView == null)
        {
            convertView = mInflater.inflate(R.layout.requests_adapter, parent, false);
        }

        TextView consumerName = (TextView) convertView.findViewById(R.id.consumer);
        TextView categoryName = (TextView) convertView.findViewById(R.id.category);
        TextView distance = (TextView) convertView.findViewById(R.id.distance);
        TextView quantity = (TextView) convertView.findViewById(R.id.quantity);

        try {
            consumerName.setText(objects.get(position).jOb.getString("consumer_name"));
            categoryName.setText(objects.get(position).jOb.getString("category_name"));
            quantity.setText(objects.get(position).jOb.getString("quantity"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}

