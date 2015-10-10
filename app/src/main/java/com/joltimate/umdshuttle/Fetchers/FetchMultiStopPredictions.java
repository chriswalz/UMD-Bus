package com.joltimate.umdshuttle.Fetchers;

/**
 * Created by Chris on 7/7/2015.
 */
import android.util.Log;

import com.joltimate.umdshuttle.BusEntry;
import com.joltimate.umdshuttle.MainActivity;
import com.joltimate.umdshuttle.Parsers.ParseMultiplePredictions;
import com.joltimate.umdshuttle.Parsers.Parser;
import com.joltimate.umdshuttle.ScreenManagers.FAV;
import com.joltimate.umdshuttle.ScreenManagers.RO;

import java.util.ArrayList;

/**
 * Created by Chris on 6/30/2015.
 */

public class FetchMultiStopPredictions extends FetchXml {
    //ArrayList<BusEntry> favorites;
    public FetchMultiStopPredictions(){
        //currentTask = RO.MULTPREDICTIONSTASK; // todo should this exist?
    }

    @Override
    public Parser createParser(){
        return new ParseMultiplePredictions();
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        MainActivity.swipeRefreshLayout.setRefreshing(true);
    }
    @Override //todo this method might not need to do anything
    public void updateData(ArrayList<BusEntry> s){
        RO.OnePredictionPerStop = s;
        ArrayList<BusEntry> favorites = RO.getFavoritedItemsInList();
        FAV.favAdapter.add(favorites);
        //Log.d("OnePre", s.toString());
        //Log.d("Favs", favorites.toString());
    }
    public static void startFetch(ArrayList<BusEntry> stops){

        String multiPredictions = "http://webservices.nextbus.com/service/publicXMLFeed?command=predictionsForMultiStops&a="+RO.agency.getLink();
        int i;
        String param1 = "&stops=";
        String param2; // = "N|";
        //param2 = RO.route.getLink()+"|";
        String param3 = "6997";
        for ( i = 0; i < stops.size(); i++){
            // set param1, 2, 3 etc;
            param2 = stops.get(i).getRouteTag()+"|";
            param3 = stops.get(i).getExtra(); //todo  dont get stopID need stop tag
           // Log.d("MultiStop", stops.get(i).getInfo() + " // "+ stops.get(i).getLink()+ " // "+ stops.get(i).getExtra());
            multiPredictions+= param1+param2+param3;
        }
        //Log.i("MultiStop", multiPredictions);
        new FetchMultiStopPredictions().execute(multiPredictions);
    }
    protected void onPostExecute(ArrayList<BusEntry> s) {
        super.onPostExecute(s);

       MainActivity.swipeRefreshLayout.setRefreshing(false);
    }
}