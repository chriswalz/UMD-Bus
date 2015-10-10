package com.joltimate.umdshuttle.Adapters;

import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joltimate.umdshuttle.Adapters.ViewHolders.NearbyViewHolder;
import com.joltimate.umdshuttle.Adapters.ViewHolders.ViewHolder;
import com.joltimate.umdshuttle.BusEntry;
import com.joltimate.umdshuttle.MainActivity;
import com.joltimate.umdshuttle.R;
import com.joltimate.umdshuttle.ScreenManagers.NEAR;
import com.joltimate.umdshuttle.ScreenManagers.RO;

/**
 * Created by Chris on 7/9/2015.
 */
public class RecyclerNearbyAdapter extends BaseAdapter {
    // using mDataset from super class
    public RecyclerNearbyAdapter(){
        super();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        CardView c = (CardView)LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.nearbyview, viewGroup, false);
        ViewHolder vh = new NearbyViewHolder(c);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        NearbyViewHolder nearbyViewHolder = (NearbyViewHolder) viewHolder;
        CardView cardView = nearbyViewHolder.cardView;
        LinearLayout l1 = (LinearLayout) cardView.getChildAt(0);
        LinearLayout l = (LinearLayout) l1.getChildAt(1);
        TextView routeView= (TextView) l.getChildAt(0);
        routeView.setText(mDataset.get(i).getRouteTag());
        TextView dirView= (TextView) l.getChildAt(1);
        dirView.setText(mDataset.get(i).getDirTag());
        TextView stopView= (TextView) l.getChildAt(2);
        stopView.setText(mDataset.get(i).getInfo());
        final BusEntry currentEntry = NEAR.currentNearbyList.get(i);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NEAR.currentNearbyList != null) {
                    RO.setRouteDirectionAndStopAccordingToBusEntry(currentEntry);
                    MainActivity.tabLayout.getTabAt(0).select();

                } else {
                    Log.e("RecyclerNearbyAdapter", "Error on click nearby view");
                }
            }
        });
        if (NEAR.currentNearbyList != null){
            //NEAR.currentNearbyList.get(i).updateView(imageView);
        }

    }

}
