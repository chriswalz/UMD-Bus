package com.joltimate.umdshuttle.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.joltimate.umdshuttle.Adapters.ViewHolders.ViewHolder;
import com.joltimate.umdshuttle.BusEntry;
import com.joltimate.umdshuttle.DebuggingTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 7/9/2015.
 */
public abstract class BaseAdapter extends RecyclerView.Adapter<ViewHolder> {
    protected ArrayList<BusEntry> mDataset;
    public BaseAdapter(){
        mDataset = new ArrayList<BusEntry>();
        mDataset.add(new BusEntry("No Data Available", ""));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
    private void add(BusEntry entry) {
        mDataset.add(entry);

    }
    public void add(List<BusEntry> entries){
        //DebuggingTools.logCurrentTask();
        mDataset.clear();
        for ( BusEntry entry: entries){
            add(entry);
        }
        notifyDataSetChanged();
    }

    public abstract ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i);
    public abstract void onBindViewHolder(ViewHolder viewHolder, int i);

}
