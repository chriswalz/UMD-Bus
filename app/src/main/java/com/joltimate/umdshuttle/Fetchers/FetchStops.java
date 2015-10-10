package com.joltimate.umdshuttle.Fetchers;

/**
 * Created by Chris on 7/7/2015.
 */

import com.joltimate.umdshuttle.BusEntry;
import com.joltimate.umdshuttle.MainActivity;
import com.joltimate.umdshuttle.Parsers.ParseStops;
import com.joltimate.umdshuttle.Parsers.Parser;
import com.joltimate.umdshuttle.ScreenManagers.RO;

import java.util.ArrayList;

public class FetchStops extends FetchXml {
    public FetchStops(){
        currentTask = RO.STOPSTASK;
    }

    @Override
    public Parser createParser(){
        return new ParseStops();
    }
    @Override
    public void updateData(ArrayList<BusEntry> s){
        RO.changeToStops(s, null);
    }
    @Override
    protected void onPostExecute(ArrayList<BusEntry> s) {
        super.onPostExecute(s);
        cacheFetch();
    }
    public static void startFetch(){
        String stops = "http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a="+ RO.agency.getLink()+"&r="+ RO.route.getLink();
        //Log.i("FetchStops",stops);
        new FetchStops().execute(stops);
    }
    public static void setDataOnClick(int position){
       // RO.stop.setLink(String.valueOf(RO.stops[RO.getRoutePosition()][RO.getDirectionPosition()].get(position).getLink()));
        RO.stop = RO.stops.get(RO.getRoutePosition()).get(RO.getDirectionPosition()).get(position);
        RO.stop.routePosition = FetchXml.cacheI;
        RO.stop.directionPosition = FetchXml.cacheJ;
        FetchPredictions.startFetch();
    }
    public static void cacheFetch(){
        if ( FetchXml.cacheJ >= RO.directions.get(FetchXml.cacheI).size()){
            FetchXml.cacheI++;
            FetchXml.cacheJ = 0;
        }
        if ( FetchXml.cacheI >= RO.routes.size()){
            FetchXml.cacheJ = 0;
            FetchXml.cacheI = 0;
            MainActivity.swipeRefreshLayout.setRefreshing(false);
            RO.onCacheFinish();
            return;
        }
        RO.route = RO.routes.get(FetchXml.cacheI);
        if ( RO.directions.size() == 0){
            RO.directions.add(new ArrayList<BusEntry>());
            RO.directions.get(0).add(new BusEntry("No Directions", ""));
        }
        RO.direction = RO.directions.get(FetchXml.cacheI).get(FetchXml.cacheJ);
        /* RO.stop.routePosition = FetchXml.cacheI;
        RO.stop.directionPosition = FetchXml.cacheJ;
        Log.d("FetchStops", "ROstop "+FetchXml.cacheI+" "+FetchXml.cacheJ); This wasnt working */
        FetchStops.startFetch();
        FetchXml.cacheJ++;
    }
}