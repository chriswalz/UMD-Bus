package com.joltimate.umdshuttle.Fetchers;

import com.joltimate.umdshuttle.BusEntry;
import com.joltimate.umdshuttle.Parsers.ParseDirections;
import com.joltimate.umdshuttle.Parsers.Parser;
import com.joltimate.umdshuttle.ScreenManagers.RO;

import java.util.ArrayList;

/**
 * Created by Chris on 7/8/2015.
 */
public class FetchDirections extends FetchXml {
    private static BusEntry entry;
    public FetchDirections(){
        currentTask = RO.DIRECTIONSTASK;
    }

    @Override
    public Parser createParser(){
        return new ParseDirections();
    }
    @Override
    public void updateData(ArrayList<BusEntry> s){
        RO.changeToDirections(s, null);
    }
    @Override
    protected void onPostExecute(ArrayList<BusEntry> s) { //param is updated time, update view
        super.onPostExecute(s);
        cacheFetch();
    }
    public static void startFetch(){
        String directions = "http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a="+ RO.agency.getLink()+"&r="+ RO.route.getLink();
        new FetchDirections().execute(directions);
    }
    public static void setDataOnClick(int position){
        entry = RO.directions.get(RO.getRoutePosition()).get(position);
        RO.direction.setInfoLink(entry.getInfo(),entry.getLink());
        RO.changeToStops(null, null);
    }
    public static void cacheFetch(){
        if (FetchXml.cacheI >= RO.routes.size() ){
            FetchXml.cacheI = 0;
            FetchStops.cacheFetch();
        } else {
            RO.route = RO.routes.get(FetchXml.cacheI);
            FetchDirections.startFetch();
            FetchXml.cacheI++;
        }
    }

}
