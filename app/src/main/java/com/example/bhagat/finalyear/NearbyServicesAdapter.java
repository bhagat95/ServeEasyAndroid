package com.example.bhagat.finalyear;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import org.json.JSONException;

import java.util.List;
import java.util.Random;

import static android.graphics.Color.GREEN;

/**
 * Created by bhagat on 10/25/16.
 */
public class NearbyServicesAdapter extends ArrayAdapter<ListData>{

    Context context;
    LayoutInflater mInflater;
    List<ListData> objects ;
    ColorGenerator generator; // or use DEFAULT

    String randomColor[]={"#2B60DE","#0070FF","#4169E1","#0038A8",	"#3284BF"};

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
        ImageView letterImage = (ImageView) convertView.findViewById(R.id.letter_image);
        TextView serviceName = (TextView) convertView.findViewById(R.id.service_name);
        TextView providerName = (TextView) convertView.findViewById(R.id.provider_name);
        TextView distance = (TextView) convertView.findViewById(R.id.distance);
        generator =   ColorGenerator.MATERIAL;
        try {
            String firstLetter = objects.get(position).jOb.getString("service_name").charAt(0)+"";
            //int color = generator.getRandomColor();
            int color = Color.parseColor(randomColor[(new Random().nextInt(5))]);//.getColor(firstLetter);
            TextDrawable drawable = TextDrawable.builder().beginConfig().width(40).height(40).endConfig().buildRoundRect(firstLetter.toUpperCase(),color,4);
            letterImage.setImageDrawable(drawable);
            serviceName.setText(objects.get(position).jOb.getString("service_name"));
            providerName.setText(objects.get(position).jOb.getString("provider_name"));
            distance.setText(objects.get(position).jOb.getString("distance") + "km");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
