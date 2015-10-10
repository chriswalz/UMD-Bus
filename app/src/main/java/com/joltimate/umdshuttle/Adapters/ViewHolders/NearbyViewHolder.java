package com.joltimate.umdshuttle.Adapters.ViewHolders;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.joltimate.umdshuttle.Fetchers.FetchXml;
import com.joltimate.umdshuttle.ScreenManagers.FAV;
import com.joltimate.umdshuttle.ScreenManagers.Overseer;
import com.joltimate.umdshuttle.ScreenManagers.RO;

/**
 * Created by Chris on 7/12/2015.
 */
public class NearbyViewHolder extends ViewHolder {
    // each data item is just a string in this case
    public CardView cardView;
    public NearbyViewHolder (CardView cardView1) {
        super(cardView1);
        cardView = cardView1;

        //mTextView = (TextView) l.getChildAt(0);
        //mTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (Overseer.currentView){
            case Overseer.RVIEW:
                if ( v instanceof TextView){
                    FetchXml.nextTask(getPosition()); //todo fix
                } else {
                    RO.currentRList.get(getPosition()).toggleFavorited(imageView);
                }
                break;
            case Overseer.FAVVIEW:
                if ( v instanceof TextView){
                    //
                } else {
                    FAV.currentFavList.get(getPosition()).toggleFavorited(imageView);
                }
                break;
        }
    }
}