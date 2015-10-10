package com.joltimate.umdshuttle.Adapters;

import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joltimate.umdshuttle.Adapters.ViewHolders.FavViewHolder;
import com.joltimate.umdshuttle.Adapters.ViewHolders.ViewHolder;
import com.joltimate.umdshuttle.BusEntry;
import com.joltimate.umdshuttle.R;
import com.joltimate.umdshuttle.ScreenManagers.FAV;
import com.joltimate.umdshuttle.ScreenManagers.RO;

import java.util.ArrayList;

/**
 * Created by Chris on 7/9/2015.
 */
public class RecyclerFavAdapter extends BaseAdapter {
    // using mDataset from super class
    public RecyclerFavAdapter(){
        super();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        CardView cardView = (CardView)LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview, viewGroup, false);
        ViewHolder vh = new FavViewHolder(cardView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        FavViewHolder favViewHolder = (FavViewHolder) viewHolder;
        //LinearLayout outerOuterLayout = (LinearLayout) favViewHolder.cardView.getChildAt(0);
        LinearLayout outerLayout = (LinearLayout) favViewHolder.cardView.getChildAt(0);
        LinearLayout l = (LinearLayout) outerLayout.getChildAt(0);
        TextView textView= (TextView) l.getChildAt(0);
        if ( FAV.currentFavList != null){
            BusEntry favStop = FAV.currentFavList.get(i);
            textView.setText(favStop.getInfo()); // change mdataset?
            ImageView imageView = (ImageView)l.getChildAt(1);
            Log.d("Favorites", "Stop: " + favStop.getInfo() + "Route: " + favStop.getRouteTag() + "Dir: " + favStop.getDirTag()); // fav stop is null for first thing?
            if (favStop != null){
                favStop.updateView(imageView);
            }
            TextView routeDirView = (TextView) outerLayout.getChildAt(1);
            routeDirView.setText(favStop.getFavRouteDirInfo());

            TextView predictions = (TextView) outerLayout.getChildAt(2);
            if ( RO.OnePredictionPerStop != null && i < RO.OnePredictionPerStop.size()){ // todo fix this shitty work around :)
                predictions.setText(RO.OnePredictionPerStop.get(i).getLink());

            }

        }
    }
    public ArrayList<BusEntry> getDataSet(){
        return mDataset;
    }


}

