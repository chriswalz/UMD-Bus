package com.joltimate.umdshuttle.Adapters;

import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joltimate.umdshuttle.Adapters.ViewHolders.RouteViewHolder;
import com.joltimate.umdshuttle.Adapters.ViewHolders.ViewHolder;
import com.joltimate.umdshuttle.Fetchers.FetchXml;
import com.joltimate.umdshuttle.R;
import com.joltimate.umdshuttle.ScreenManagers.RO;

/**
 * Created by Chris on 7/9/2015.
 */
public class RecyclerRouteAdapter extends BaseAdapter {
    // using mDataset from super class
    public RecyclerRouteAdapter(){
        super();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        LinearLayout l= (LinearLayout)LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.listrow, viewGroup, false);
        ViewHolder vh = new RouteViewHolder(l);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        RouteViewHolder routeViewHolder = (RouteViewHolder) viewHolder;
        TextView textView= (TextView) routeViewHolder.linearLayout.getChildAt(0);
        textView.setTypeface(null, Typeface.ITALIC);
        textView.setText(mDataset.get(i).getInfo());

        textView.setTextColor(ContextCompat.getColor(RO.mainActivity.getApplicationContext(), R.color.prediction_text));
        ImageView imageView = (ImageView) routeViewHolder.linearLayout.getChildAt(1);
        imageView.setVisibility(View.GONE);
        if ( FetchXml.currentTask == RO.STOPSTASK){
            imageView.setVisibility(View.VISIBLE);
        }
        if ( RO.currentRList != null){
            RO.currentRList.get(i).updateView(imageView);
        }

    }

}
