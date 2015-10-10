package com.joltimate.umdshuttle.SpinnerAdapters;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.joltimate.umdshuttle.BusEntry;
import com.joltimate.umdshuttle.R;

import java.util.ArrayList;
import java.util.List;

public class BusSpinnerAdapter extends ArrayAdapter<BusEntry> implements SpinnerAdapter {

    private ArrayList<BusEntry> mDataset;
    private Context context;

    public BusSpinnerAdapter(Context context, int resource, List<BusEntry> objects) {
        super(context, resource, objects);
        this.mDataset = (ArrayList) objects;
        this.context = context;
        notifyDataSetChanged();
    }

    public int getCount() {
        return mDataset.size();
    }

    public BusEntry getItem(int position) {
        if ( position >=0 && position < mDataset.size()){
            return mDataset.get(position);
        } else {
            //Log.i("Bus spinner", position+"");
            return new BusEntry("err with spinner", "err");
        }
    }

    public long getItemId(int position) {
        return position;
    }
    // sets the individual views
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) context
                    .getApplicationContext().getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.material_item,
                    parent, false);
        }

        TextView route = (TextView) convertView
                .findViewById(R.id.material_item);
        route.setText(mDataset.get(position).getInfo());
        route.setTextColor(context.getResources().getColor(R.color.secondary_text));
        return convertView;
    }
    // sets the inital view (when spinner isnt opened)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) context
                    .getApplicationContext().getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.material_item,
                    parent, false);
        }

        TextView route = (TextView) convertView
                .findViewById(R.id.material_item);
        //Log.d("BusSpinnerAdapter", "Position is: "+position);
        //Log.d("BusSpinnerAdapter", mDataset.toString());
        route.setText(mDataset.get(position).getInfo());
        route.setTypeface(null, Typeface.BOLD);
        route.setTextColor(context.getResources().getColor(R.color.secondary_text));
        return convertView;
    }


 /*   @Override
    public void add(BusEntry object) {
        super.add(object);
        mDataset.add(object);
    }

    @Override
    public void addAll(BusEntry... items) {
        super.addAll(items);
        mDataset.clear();
        for ( BusEntry entry: items){
            add(entry);
        }
        notifyDataSetChanged();
    }*/


}


/*
package com.joltimate.umdshuttle.SpinnerAdapters;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.joltimate.umdshuttle.BusEntry;
import com.joltimate.umdshuttle.R;

import java.util.ArrayList;
import java.util.List;

*/
/**
 * Created by Chris on 7/16/2015.
 *//*

public class BusSpinnerAdapter extends ArrayAdapter<BusEntry> {
    private ArrayList<BusEntry> items;
    private Activity activity;

    public BusSpinnerAdapter(Activity activity, ArrayList<BusEntry> items) {
        super(activity, android.R.layout.simple_list_item_1, items);
        this.items = items;
        this.activity = activity;
       notifyDataSetChanged();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView v = (TextView) super.getView(position, convertView, parent);

        if (v == null) {
            v = new TextView(activity);
        }
        v.setTextColor(Color.BLACK);
        v.setText(items.get(position).getInfo());
        return v;
    }

    @Override
    public BusEntry getItem(int position) {
        return items.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            v = inflater.inflate(R.layout.material_item, null);
        }
        TextView lbl = (TextView) v.findViewById(R.id.material_item);
        //lbl.setTextColor(Color.BLACK);
        lbl.setText(items.get(position).getInfo());
        return convertView;
    }
}
*/
