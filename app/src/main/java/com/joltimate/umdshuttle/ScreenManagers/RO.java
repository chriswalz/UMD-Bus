package com.joltimate.umdshuttle.ScreenManagers;

import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import com.google.android.gms.analytics.HitBuilders;
import com.joltimate.umdshuttle.Adapters.RecyclerRouteAdapter;
import com.joltimate.umdshuttle.BusEntry;
import com.joltimate.umdshuttle.Data.DataStorage;
import com.joltimate.umdshuttle.Fetchers.FetchXml;
import com.joltimate.umdshuttle.MainActivity;
import com.joltimate.umdshuttle.R;
import com.joltimate.umdshuttle.SpinnerAdapters.BusSpinnerAdapter;

import java.util.ArrayList;

/**
 * Created by Chris on 6/30/2015.
 */
public class RO {
    public static final int MULTPREDICTIONSTASK = 88;
    public static final int PREDICTIONTASK = 89;
    public static final int STOPSTASK = 90;
    public static final int DIRECTIONSTASK = 91;
    public static final int ROUTESTASK = 92;
    public static final int AGENCIESTASK = 93;
    // TODO this is probably bad practice..
    public static MainActivity mainActivity;
    public static RecyclerRouteAdapter rAdapter;
    public static BusSpinnerAdapter routeAdapter;
    public static BusSpinnerAdapter directionAdapter;
    public static BusSpinnerAdapter stopAdapter;
    public static BusEntry agency = new BusEntry("UMD", "umd");
   // public static BusEntry agency = new BusEntry("Bronx", "bronx");
    public static BusEntry route = new BusEntry("Bio Park", "701");
    public static BusEntry direction = new BusEntry("Denton","");
    public static BusEntry stop = new BusEntry("Regents Garage","47113"); // change to bus entry
    //important data
    public static ArrayList<BusEntry> agencies;

    public static ArrayList<BusEntry> routes;
    public static ArrayList<ArrayList<BusEntry>> directions = new ArrayList<ArrayList<BusEntry>>();
    public static ArrayList<ArrayList<ArrayList<BusEntry>>> stops = new ArrayList<ArrayList<ArrayList<BusEntry>>>(); // new ArrayList[40][6];
    public static ArrayList<BusEntry> predictions = new ArrayList<>();
    public static ArrayList<BusEntry> OnePredictionPerStop;
    public static ArrayList<BusEntry> currentRList;
    public static boolean nearbyIsClicked = false;

    public static int lastRoutePosition = 0;
    public static int lastDirectionPosition = 0;
    public static int lastStopPosition = 0;

    static {
        routes = new ArrayList<BusEntry>();
        routes.add(new BusEntry("No Routes", ""));
        directions.add(new ArrayList<BusEntry>());
        directions.get(0).add(new BusEntry("No Directions", ""));
        directions.get(0).get(0); //test line
        stops.add(new ArrayList<ArrayList<BusEntry>>());
        stops.get(0).add(new ArrayList<BusEntry>());
        //stops.get(0).get(0).add(new ArrayList<BusEntry>());
        stops.get(0).get(0).add(new BusEntry("No Stops", ""));
        predictions.add(new BusEntry("No Predictions",""));
    }

    public static void updateDirections(int position, Spinner directionSpinner){
        if ( RO.directions != null ){
            RO.directionAdapter = new BusSpinnerAdapter(mainActivity, R.layout.material_item, RO.directions.get(position));
            directionSpinner.setAdapter(RO.directionAdapter);
            MainActivity.directionsPosition = position;
            //Snackbar.make(mainActivity.holder, "change to Directions", Snackbar.LENGTH_LONG).show();
        }
        /*
                if ( r == null && newDirection != null){
            rAdapter.add(RO.directions[newDirection.routePosition]);
            currentRList = RO.directions[newDirection.routePosition];

        }
         */
    }

    public static void updateStops(int position, Spinner stopsSpinner){
        if ( RO.stops != null ){
            RO.stopAdapter = new BusSpinnerAdapter(mainActivity, R.layout.material_item, RO.stops.get(MainActivity.directionsPosition).get(position));
            stopsSpinner.setAdapter(RO.stopAdapter);
            //Snackbar.make(mainActivity.holder, "change to STOPS", Snackbar.LENGTH_LONG).show();
        }
    }

    public static void changeToAgencies(ArrayList<BusEntry> a){
        mainActivity.setTitle("Agencies");
        FetchXml.currentTask = RO.AGENCIESTASK;
        if ( a == null ){
            rAdapter.add(RO.agencies);
            currentRList = RO.agencies;
        } else {
            agencies = a;
            currentRList = a;
        }
        commonTasks();
    }

    public static void changeToRoutes(ArrayList<BusEntry> r){
        mainActivity.setTitle("Routes");
        FetchXml.currentTask = RO.ROUTESTASK;
        if ( r == null ){
            rAdapter.add(RO.routes);
            currentRList = RO.routes;
        } else {
            routes = r;
            currentRList = r;
        }
        commonTasks();
    }

    public static void changeToDirections(ArrayList<BusEntry> r, BusEntry newDirection){
        mainActivity.setTitle("Directions");
        FetchXml.currentTask = RO.DIRECTIONSTASK;
        int routePos = getRoutePosition();
        lastRoutePosition = routePos;
         if ( r == null ){
            rAdapter.add(RO.directions.get(routePos));
            currentRList = RO.directions.get(routePos);
        } else {
             int difference = routePos - directions.size();
             for ( int i = difference; i >= 0; i--){
                 directions.add(new ArrayList<BusEntry>());
             }
             directions.set(routePos, r);
             currentRList = r;
             /*if (routePos >= directions.size()){
                directions.add(new ArrayList<BusEntry>());
             }
             if ( routePos < directions.size()){
                 directions.set(routePos, r);
                 currentRList = r;
             } */
        }
        commonTasks();
    }

    public static void changeToStops(ArrayList<BusEntry> s, BusEntry newStop){
        mainActivity.setTitle("Stops");
        FetchXml.currentTask = RO.STOPSTASK;
        int routePos = getRoutePosition();
        int dirPos = getDirectionPosition();
        lastRoutePosition = routePos;
        lastDirectionPosition = dirPos;
        if ( routePos <= -1 || dirPos <= -1){
            if (!DataStorage.isSyncing){
                Log.e("Ro", "Data was never downloaded from the internet");
                Snackbar.make(mainActivity.holder, "Resync data or restart app when you have internet connection", Snackbar.LENGTH_LONG).show();
            }
            return;
        }
        if ( s == null ){
            rAdapter.add(RO.stops.get(routePos).get(dirPos));
            currentRList = RO.stops.get(routePos).get(dirPos);
           // RO.stop = RO.stops[routePos][dirPos];
        } else {
            if (routePos >= stops.size()){
                stops.add(new ArrayList<ArrayList<BusEntry>>());
            }
            if ( dirPos >= stops.get(routePos).size()){
                stops.get(routePos).add(new ArrayList<BusEntry>());
            }
            stops.get(routePos).set(dirPos, s); // [routePos][dirPos] = s;
            currentRList = s;
        }
        commonTasks();
    }

    public static void changeToPredictions(ArrayList<BusEntry> p){
        mainActivity.setTitle("UMD Bus");
        FetchXml.currentTask = RO.PREDICTIONTASK;
        if ( p == null ){
            rAdapter.add(RO.predictions);
            currentRList = RO.predictions;
        } else {
            predictions = p;
            currentRList = p;
        }
        commonTasks();
        sendAnalytics("Routes");
    }

    private static void commonTasks(){
        // Set screen name.
       //sendAnalytics();
    }

    public static void sendAnalytics(String screenTitle){
        if ( !DataStorage.isSyncing){
            MainActivity.t.setScreenName(screenTitle);
            MainActivity.t.send(new HitBuilders.ScreenViewBuilder().build());
        }
    }

    public static int getRoutePosition(){
        int routePos = routes.indexOf(route);
        if ( routePos == -1 ){
            Log.i("RO", "Routepos was -1 ");
            routePos = 0;
        }
        return routePos;
    }

    public static int getDirectionPosition(){
        int routePos = getRoutePosition();
        int dirPos = directions.get(routePos).indexOf(direction);
        if ( dirPos == -1 ){
            dirPos = 0;
        }
        return dirPos;
    }

    public static void onCacheFinish(){
        RO.changeToRoutes(null);
        DataStorage.saveAllData();
        DataStorage.handleDataStorage(false); //todo remove this work around
        //MainActivity.swipeRefreshLayout.setRefreshing(false);
        //Log.e("NOO", "nooooo");
        RO.routeAdapter = new BusSpinnerAdapter(mainActivity, R.layout.material_item, RO.routes);
        MainActivity.routeSpinner.setAdapter(RO.routeAdapter);
        RO.directionAdapter = new BusSpinnerAdapter(mainActivity, R.layout.material_item, RO.directions.get(0));
        MainActivity.directionSpinner.setAdapter(RO.directionAdapter);
        RO.stopAdapter = new BusSpinnerAdapter(mainActivity, R.layout.material_item, RO.stops.get(0).get(0));
        MainActivity.stopSpinner.setAdapter(RO.stopAdapter);
        RO.mainActivity.mRoRecyclerView.setVisibility(View.VISIBLE);
        DataStorage.isSyncing = false;
    }

    public static void setRouteDirectionAndStopAccordingToLastPosition() {
        nearbyIsClicked = true;
        RO.route = RO.routes.get(RO.lastRoutePosition);
        MainActivity.routeSpinner.setSelection(RO.lastRoutePosition);

        updateDirections(RO.lastRoutePosition, MainActivity.directionSpinner);
        RO.direction = RO.directions.get(RO.lastRoutePosition).get(RO.lastDirectionPosition);
        MainActivity.directionSpinner.setSelection(RO.lastDirectionPosition);

        updateStops(RO.lastDirectionPosition, MainActivity.stopSpinner);
        RO.stop = RO.stops.get(RO.lastRoutePosition).get(RO.lastDirectionPosition).get(lastStopPosition);
        MainActivity.stopSpinner.setSelection(lastStopPosition);
        MainActivity.tabLayout.getTabAt(0).select();
    }

    public static void setRouteDirectionAndStopAccordingToBusEntry(BusEntry busEntry){
        int routePos = busEntry.routePosition;
        int dirPos = busEntry.directionPosition;
        int stopPos = busEntry.stopPosition;
        if ( stopPos < 0 ){
            return;
        }
        nearbyIsClicked = true;
        RO.route = RO.routes.get(routePos);
        MainActivity.routeSpinner.setSelection(routePos);

        updateDirections(routePos, MainActivity.directionSpinner);
        RO.direction = RO.directions.get(routePos).get(dirPos);
        MainActivity.directionSpinner.setSelection(dirPos);

        updateStops(dirPos, MainActivity.stopSpinner);
        RO.stop = RO.stops.get(routePos).get(dirPos).get(busEntry.stopPosition);
        MainActivity.stopSpinner.setSelection(stopPos);
    }

    public static boolean isValidRouteDirStopPosition(int routePos, int dirPos, int stopPos) {
        if (routePos < routes.size()) {
            if (directions.get(routePos).size() < dirPos) {
                if (stops.get(routePos).get(dirPos).size() < stopPos) {
                    return true;
                }
            }
        }
        return false;
    }
}
