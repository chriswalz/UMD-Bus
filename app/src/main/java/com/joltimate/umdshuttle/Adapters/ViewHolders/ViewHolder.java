package com.joltimate.umdshuttle.Adapters.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Chris on 7/12/2015.
 */
public abstract class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    // each data item is just a string in this case
    public TextView mTextView;
    public ImageView imageView;
    public ViewHolder(View v) {
        super(v);

    }

    @Override
    public void onClick(View v) {
        //Log.d("onClick", "Position: " + getPosition());
    }
}