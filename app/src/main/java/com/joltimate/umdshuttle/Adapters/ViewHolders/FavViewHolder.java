package com.joltimate.umdshuttle.Adapters.ViewHolders;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joltimate.umdshuttle.Fetchers.FetchXml;
import com.joltimate.umdshuttle.ScreenManagers.FAV;
import com.joltimate.umdshuttle.ScreenManagers.Overseer;
import com.joltimate.umdshuttle.ScreenManagers.RO;

/**
 * Created by Chris on 7/12/2015.
 */
public class FavViewHolder extends ViewHolder {
    // each data item is just a string in this case
    public CardView cardView;
    public FavViewHolder (CardView cv) {
        super(cv);
        cardView = cv;
        LinearLayout outerLayout = (LinearLayout) cardView.getChildAt(0);
        LinearLayout l = (LinearLayout) outerLayout.getChildAt(0);
        mTextView = (TextView) l.getChildAt(0);
        imageView = (ImageView) l.getChildAt(1);
        mTextView.setOnClickListener(this);
        imageView.setOnClickListener(this);
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
                } else if ( v instanceof ImageView){
                    FAV.currentFavList.get(getPosition()).toggleFavorited(imageView);
                } else {

                }
                break;
        }
    }
}
