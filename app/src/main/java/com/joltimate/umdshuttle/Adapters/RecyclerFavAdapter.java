package com.joltimate.umdshuttle.Adapters;

import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joltimate.umdshuttle.Adapters.ViewHolders.FavViewHolder;
import com.joltimate.umdshuttle.Adapters.ViewHolders.ViewHolder;
import com.joltimate.umdshuttle.BusEntry;
import com.joltimate.umdshuttle.DebuggingTools;
import com.joltimate.umdshuttle.MainActivity;
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
                .inflate(R.layout.favview, viewGroup, false);
        ViewHolder vh = new FavViewHolder(cardView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        FavViewHolder favViewHolder = (FavViewHolder) viewHolder;
        CardView cardView = favViewHolder.cardView;
        LinearLayout l1 = (LinearLayout) cardView.getChildAt(0);
        LinearLayout l = (LinearLayout) l1.getChildAt(1);
        TextView routeView = (TextView) l.getChildAt(0);
        String twoWordString = mDataset.get(i).getFavRouteDirInfo();
        if (twoWordString != null) {
            String[] routeDirArray = twoWordString.split("/");
            routeView.setText(routeDirArray[0]);//(mDataset.get(i).getRouteTag());
            TextView dirView = (TextView) l.getChildAt(1);
            dirView.setText(routeDirArray[1]);
        }
        TextView stopView = (TextView) l.getChildAt(2);
        stopView.setText(mDataset.get(i).getInfo());
        TextView predictionView = (TextView) l.getChildAt(3);
        final BusEntry currentEntry = FAV.currentFavList.get(i);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FAV.currentFavList != null) {
                    RO.setRouteDirectionAndStopAccordingToBusEntry(currentEntry);
                    MainActivity.tabLayout.getTabAt(0).select();

                } else {
                    Log.e("RecyclerNearbyAdapter", "Error on click nearby view");
                }
            }
        });
        if (FAV.currentFavList != null) {
            BusEntry favStop = FAV.currentFavList.get(i);
            //ImageView imageView = (ImageView)l.getChildAt(1);
            DebuggingTools.logd("Favorites", "Stop: " + favStop.getInfo() + "Route: " + favStop.getRouteTag() + "Dir: " + favStop.getDirTag()); // fav stop is null for first thing?
            if (favStop != null) {
                //  favStop.updateView(imageView);
            }
            if (RO.OnePredictionPerStop != null && i < RO.OnePredictionPerStop.size()) { // todo fix this shitty work around :)
                predictionView.setText(RO.OnePredictionPerStop.get(i).getLink());

            }

        }
       /* FavViewHolder favViewHolder = (FavViewHolder) viewHolder;
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

        }*/
    }
    public ArrayList<BusEntry> getDataSet(){
        return mDataset;
    }


}

