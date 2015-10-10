package com.joltimate.umdshuttle.ScreenManagers;

import android.animation.Animator;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.joltimate.umdshuttle.BusEntry;
import com.joltimate.umdshuttle.Fetchers.FetchMultiStopPredictions;
import com.joltimate.umdshuttle.Fetchers.FetchPredictions;
import com.joltimate.umdshuttle.Fetchers.FetchXml;
import com.joltimate.umdshuttle.MainActivity;

import java.util.ArrayList;

/**
 * Created by Chris on 7/12/2015.
 */
public class Overseer {
    public static final int RVIEW = 100;
    public static final int FAVVIEW = 101;
    public static final int NEARBYVIEW = 102;

    public static int currentView = RVIEW;

    public static void makeAllGoneExcept(){
        // all gone
        RO.mainActivity.mRoRecyclerView.setVisibility(View.GONE);
        RO.mainActivity.mFavoritesRecyclerView.setVisibility(View.GONE);
        RO.mainActivity.mNearbyRecyclerView.setVisibility(View.GONE);
        MainActivity.routeSpinner.setVisibility(View.GONE);
        MainActivity.directionSpinner.setVisibility(View.GONE);
        MainActivity.stopSpinner.setVisibility(View.GONE);

        MainActivity.routeText.setVisibility(View.GONE);
        MainActivity.directionText.setVisibility(View.GONE);
        MainActivity.stopText.setVisibility(View.GONE);
        MainActivity.etaText.setVisibility(View.GONE);
        //RO.mainActivity.menuMain.getItem(0).setVisible(false);

        MainActivity.fab.setVisibility(View.GONE);

        // except
        switch (currentView){
            case RVIEW:
                RO.mainActivity.mRoRecyclerView.setVisibility(View.VISIBLE);
                MainActivity.routeSpinner.setVisibility(View.VISIBLE);
                MainActivity.directionSpinner.setVisibility(View.VISIBLE);
                MainActivity.stopSpinner.setVisibility(View.VISIBLE);
                MainActivity.routeText.setVisibility(View.VISIBLE);
                MainActivity.directionText.setVisibility(View.VISIBLE);
                MainActivity.stopText.setVisibility(View.VISIBLE);
                //RO.mainActivity.etaText.setVisibility(View.VISIBLE);
                MainActivity.fab.setVisibility(View.VISIBLE);
                //RO.mainActivity.menuMain.getItem(0).setVisible(true);
                break;
            case FAVVIEW:
                RO.mainActivity.mFavoritesRecyclerView.setVisibility(View.VISIBLE);
                break;
            case NEARBYVIEW:
                RO.mainActivity.mNearbyRecyclerView.setVisibility(View.VISIBLE);
                break;
        }
    }
    public static void changeToRVIEW(){
        // todo use different reycler rAdapter for each view, as well as different, arraylist
        currentView = RVIEW;
        FetchXml.currentTask = RO.PREDICTIONTASK; //todo is this relevant?
        RO.mainActivity.setTitle("UMD Bus");
        switch (FetchXml.currentTask){
            case RO.PREDICTIONTASK:
                RO.changeToPredictions(null);
                break;
            case RO.STOPSTASK:
                RO.changeToStops(null, null);
                // TODO move rAdapter changing somewhere to RO?
                break;
            case RO.DIRECTIONSTASK:
                RO.changeToDirections(null, null);
                break;
            case RO.ROUTESTASK:
                RO.changeToRoutes(null);
                break;
            // TODO add a task for directionos
        }
        makeAllGoneExcept();
        ArrayList<BusEntry> stopList = new ArrayList<BusEntry>();
        stopList.add(RO.stop);
        FetchPredictions.startFetch(stopList);
        RO.mainActivity.setUpMenuStar();
        //animateViewChange(RO.mainActivity.mainLayout, 200);
        RO.sendAnalytics("Routes");
    }
    public static void changeToFAVVIEW(){
        currentView = FAVVIEW;
        RO.mainActivity.setTitle("Favorites");
        ArrayList<BusEntry> favorites = RO.getFavoritedItemsInList(); // todo change getFavorited to only access favorites in current area?
        FAV.currentFavList = favorites;
        FetchXml.currentTask = RO.MULTPREDICTIONSTASK; //todo is this relevant?
        FetchMultiStopPredictions.startFetch(favorites);
        //FAV.favAdapter.add(favorites); adapter updated on postExecute in multpredictions
        makeAllGoneExcept();
        //animateViewChange(RO.mainActivity.mainLayout, 500);
        RO.sendAnalytics("Favorites");
    }
    public static void changeToNEARView(){
        currentView = NEARBYVIEW;
        makeAllGoneExcept();
        RO.mainActivity.setTitle("Nearby");
        NEAR.updateNearbyAdapter();
        //animateViewChange(RO.mainActivity.mainLayout, 900);
        RO.sendAnalytics("Nearby");
        RO.mainActivity.enableLocationPermission();
    }

    public static void refreshCurrentView() {
        if (currentView == RVIEW) {
            changeToRVIEW();
        } else if (currentView == FAVVIEW) {
            changeToFAVVIEW();
        } else if (currentView == NEARBYVIEW) {
            changeToNEARView();
        } else {
            Log.e("Overseer", "currentView was invalid");
        }
    }
    public static void animateViewChange(View view, int x ){
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Animator reveal = ViewAnimationUtils.createCircularReveal(
                    view, // The new View to reveal
                    x,      // x co-ordinate to start the mask from
                    300,      // y co-ordinate to start the mask from
                    500,  // radius of the starting mask
                    1600);   // radius of the final mask
            reveal.start();
        }
    }
}
