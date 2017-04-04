package com.example.bhagat.finalyear;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
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

/**
 * Created by bhagat on 10/5/16.
 */
public class RequestsAdapter extends ArrayAdapter<ListData> {
    Context context;
    LayoutInflater mInflater;
    List<ListData> listOfItems;
    private SparseBooleanArray mSelectedItemsIds;
    ColorGenerator generator; // or use DEFAULT
    String randomColor[]={"#2B60DE","#0070FF","#4169E1","#0038A8",	"#3284BF"};
    public RequestsAdapter(Context context, int resource, List<ListData> listOfItems) {
        super(context, resource, listOfItems);
        this.context = context;
        this.listOfItems = listOfItems;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.requests_adapter, parent, false);
        }
        if (listOfItems == null) {
            Log.d("NULL", "0");
        } else {
            ImageView letterImage = (ImageView) convertView.findViewById(R.id.letter_image);
            TextView consumerName = (TextView) convertView.findViewById(R.id.consumer);
            TextView categoryName = (TextView) convertView.findViewById(R.id.category);
            //todo: distance is to be calculated (Eucledean/distance-matrix)
            TextView distance = (TextView) convertView.findViewById(R.id.distance);
            TextView quantity = (TextView) convertView.findViewById(R.id.quantity);
            generator =   ColorGenerator.MATERIAL;
            try {
                String firstLetter = listOfItems.get(position).jOb.getString("consumer_name").charAt(0)+"";
                //int color = generator.getRandomColor();
                int color = Color.parseColor(randomColor[(new Random().nextInt(5))]);//.getColor(firstLetter);
                TextDrawable drawable = TextDrawable.builder().beginConfig().width(40).height(40).endConfig().buildRoundRect(firstLetter.toUpperCase(),color,4);
                letterImage.setImageDrawable(drawable);
                consumerName.setText(listOfItems.get(position).jOb.getString("consumer_name"));
                categoryName.setText(listOfItems.get(position).jOb.getString("category_name"));
                quantity.setText("Qty: " + listOfItems.get(position).jOb.getString("quantity"));
                distance.setText(listOfItems.get(position).jOb.getString("distance") + " km");
                //Log.d("requestsAdapter", quantity.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return convertView;
    }
}


    /* for multiple select


    @Override
    public void remove(ListData object) {// int position){//
        listOfItems.remove(object);
        notifyDataSetChanged();
    }


    // get List after update or delete
    public List<ListData> getMyList() {
        return listOfItems;
    }


    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    // Remove selection after unchecked
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }


    // Item checked on selection

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }


    // Get number of selected item
    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }


    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
    */

