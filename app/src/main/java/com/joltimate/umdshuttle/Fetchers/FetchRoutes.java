package com.joltimate.umdshuttle.Fetchers;

/**
 * Created by Chris on 7/7/2015.
 */


import android.view.View;

import com.joltimate.umdshuttle.BusEntry;
import com.joltimate.umdshuttle.MainActivity;
import com.joltimate.umdshuttle.Parsers.ParseRoutes;
import com.joltimate.umdshuttle.Parsers.Parser;
import com.joltimate.umdshuttle.ScreenManagers.RO;

import java.util.ArrayList;

/**
 * Created by Chris on 6/30/2015.
 */

public class FetchRoutes extends FetchXml {
    private static BusEntry entry;
    public FetchRoutes(){
        currentTask = RO.ROUTESTASK;
    }

    @Override
    public Parser createParser(){
        return new ParseRoutes();
    }
    @Override // core
    protected void onPreExecute() {
        super.onPreExecute();
        if ( !MainActivity.swipeRefreshLayout.isRefreshing()){
            MainActivity.swipeRefreshLayout.setRefreshing(true);
        }
    }
    @Override
    public void updateData(ArrayList<BusEntry> s){
        RO.changeToRoutes(s);
        commonPostClick(s);
    }
    // make abstract
    public static void startFetch(){
        // TODO psuedo prefetch area
        RO.mainActivity.mRoRecyclerView.setVisibility(View.GONE);
        String routes = "http://webservices.nextbus.com/service/publicXMLFeed?command=routeList&a="+ RO.agency.getLink();
        new FetchRoutes().execute(routes);
    }
    // make abstract
    public static void setDataOnClick(int position){
        entry = RO.routes.get(position);
        RO.route.setInfoLink(entry.getInfo(),entry.getLink());
        RO.changeToDirections(null, null);
    }
    @Override
    protected void onPostExecute(ArrayList<BusEntry> s) { //param is updated time, update view
        super.onPostExecute(s);
        MainActivity.swipeRefreshLayout.setRefreshing(false);
        FetchDirections.cacheFetch();

    }
}


