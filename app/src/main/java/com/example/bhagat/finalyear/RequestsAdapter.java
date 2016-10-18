package com.example.bhagat.finalyear;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by bhagat on 10/5/16.
 */
public class RequestsAdapter extends ArrayAdapter<ListData> {

    Context context;
    LayoutInflater mInflater;
    public RequestsAdapter(Context context, int resource, List<ListData> objects) {
        super(context, resource, objects);
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        if(convertView == null)
        {
            convertView = mInflater.inflate(R.layout.requests_adapter, parent, false);
        }
        return convertView;
    }
}

