package com.joltimate.umdshuttle.Fetchers;

/**
 * Created by Chris on 7/7/2015.
 */

import com.joltimate.umdshuttle.BusEntry;
import com.joltimate.umdshuttle.MainActivity;
import com.joltimate.umdshuttle.Parsers.ParsePredictions;
import com.joltimate.umdshuttle.Parsers.Parser;
import com.joltimate.umdshuttle.ScreenManagers.RO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Chris on 6/30/2015.
 */

public class FetchPredictions extends FetchXml {
    private static boolean ranOnce = false;
    public FetchPredictions(){
        currentTask = RO.PREDICTIONTASK;
    }

    public static void startFetch(ArrayList<BusEntry> stops) {

        String multiPredictions = "http://webservices.nextbus.com/service/publicXMLFeed?command=predictionsForMultiStops&a=" + RO.agency.getLink();
        int i;
        String param1 = "&stops=";
        String param2; // = "N|";
        //param2 = RO.route.getLink()+"|";
        String param3 = "6997";

        for (i = 0; i < stops.size(); i++) {
            // set param1, 2, 3 etc;
            // Log.d("Predictions", ""+RO.stop.getInfo()+" "+RO.stop.getLink()+" "+RO.route.getExtra()+" "+RO.route.getRouteTag());
            param2 = RO.route.getLink() + "|";
            param3 = stops.get(i).getExtra(); //todo  dont get stopID need stop tag
            multiPredictions += param1 + param2 + param3;
        }
        //Log.d("Predictions: ", multiPredictions);
        new FetchPredictions().execute(multiPredictions);
    }

    @Override
    public Parser createParser(){
        return new ParsePredictions();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        MainActivity.swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void updateData(ArrayList<BusEntry> s){
        GregorianCalendar now = new GregorianCalendar();
        MainActivity.etaText.setText("ETA - Updated at " + now.get(Calendar.HOUR) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND));
        RO.changeToPredictions(s);
        commonPostClick(s); // neccesary for updates (most dont use because the screen would constantly be changing when getting all data for caching
        //Log.d("FetchPre", s.toString());

        /*if ( ranOnce){
            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                Animator reveal = ViewAnimationUtils.createCircularReveal(
                        RO.mainActivity.mRoRecyclerView, // The new View to reveal
                        500,      // x co-ordinate to start the mask from
                        50,      // y co-ordinate to start the mask from
                        150,  // radius of the starting mask
                        700);   // radius of the final mask
                reveal.start();
            }
        }
        ranOnce = true; */
    }

    protected void onPostExecute(ArrayList<BusEntry> s) {
        super.onPostExecute(s);
        MainActivity.swipeRefreshLayout.setRefreshing(false);
    }
}

/*

    public static void startFetch(){
        String predictions = "http://webservices.nextbus.com/service/publicXMLFeed?command=predictions&a="+ RO.agency.getLink()+"&stopId="+ RO.stop.getLink(); // todo add get link to stopid
        Log.i("FetchPredictions",predictions);
        Log.i("FetchPredictions",RO.direction.getLink() + " "+RO.stop);
        new FetchPredictions().execute(predictions);
    }
 */