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
 * Created by bhagat on 10/25/16.
 */
public class NearbyServicesAdapter extends ArrayAdapter<ListData>{

    Context context;
    LayoutInflater mInflater;
    List<ListData> objects ;
    public NearbyServicesAdapter(Context context, int resource, List<ListData> objects) {
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
            convertView = mInflater.inflate(R.layout.nearby_services_adapter, parent, false);
        }
        TextView serviceName = (TextView) convertView.findViewById(R.id.service_name);
        TextView providerName = (TextView) convertView.findViewById(R.id.provider_name);
        TextView distance = (TextView) convertView.findViewById(R.id.distance);
        try {
            serviceName.setText(objects.get(position).jOb.getString("service_name"));
            providerName.setText(objects.get(position).jOb.getString("provider_name"));
            distance.setText(objects.get(position).jOb.getString("distance") + "km");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
