package com.joltimate.umdshuttle.Adapters.ViewHolders;

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
public class RouteViewHolder extends ViewHolder {
    // each data item is just a string in this case
    public LinearLayout linearLayout;
    public RouteViewHolder(LinearLayout l) {
        super(l);
        linearLayout = l;
        mTextView = (TextView) l.getChildAt(0);
//        imageView = (ImageView) l.getChildAt(1);
        mTextView.setOnClickListener(this);
  //      imageView.setOnClickListener(this);
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