package com.joltimate.umdshuttle.ScreenManagers;

import android.Manifest;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.joltimate.umdshuttle.Adapters.RecyclerNearbyAdapter;
import com.joltimate.umdshuttle.BusEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Chris on 7/12/2015.
 */
public class NEAR {
    public static List<BusEntry> currentNearbyList;
    public static RecyclerNearbyAdapter nearbyAdapter;
    public static Location currentLocation = new Location("null");;

    public static void updateNearbyAdapter(){
        getNearbyLocationsAndUpdate();
        if (currentNearbyList != null){
            nearbyAdapter.add(currentNearbyList);
            //Log.d("NEAR", "currentNearbyList is not null");
        } else {
            //Log.d("NEAR", "currentNearbyList is null");
        }
    }
    public static void getNearbyLocationsAndUpdate(){
        int i, j, k;
        BusEntry entry;
        List<BusEntry> nearbyLocations = new ArrayList<>();
        ArrayList<BusEntry> temp;
        for ( i = 0; i < RO.stops.size(); i++){
            for ( j = 0; j < RO.stops.get(i).size(); j++){
                temp = RO.stops.get(i).get(j);
                if ( temp != null ){
                    for ( k = 0; k < temp.size(); k++){
                        entry = temp.get(k);
                        if ( entry.location!= null){
                            String rTag = "Nearby not available";
                            String dTag = "Nearby not available";
                            entry.setRouteTag(RO.routes.get(i).getLink()); // need link for getting times, info for proper output :/
                            if ( i < RO.directions.size() && j < RO.directions.get(i).size()){
                                entry.setDirTag(RO.directions.get(i).get(j).getLink());
                                rTag = RO.routes.get(i).getInfo();
                                dTag = RO.directions.get(i).get(j).getInfo();
                            }
                            if ( dTag == null){
                                dTag = "No Direction";
                            }
                            if (rTag == null){
                                rTag = "No Route";
                            }
                            entry.distance = getDistanceFromCurrentLoc(entry.location);
                            entry.setDirTag(dTag);
                            entry.setRouteTag(rTag);
                            entry.routePosition = i;
                            entry.directionPosition = j;
                            entry.stopPosition = k;
                            nearbyLocations.add(entry);
                        }
                    }
                }
            }
        }
        BusEntry.state = BusEntry.DISTANCE;
        Collections.sort(nearbyLocations);
        if ( nearbyLocations.size() >= 25){
            nearbyLocations = nearbyLocations.subList(0, 25);
        }
        BusEntry.state = BusEntry.REGULAR;
        if ( nearbyLocations.size() == 0  || currentLocation.getProvider().equals("null")){
            nearbyLocations = new ArrayList<>();
            String errorLocations = "Phone Location unavailable";
            BusEntry b = new BusEntry(errorLocations,errorLocations);
            b.setDirTag(errorLocations);
            b.setRouteTag(errorLocations);
            nearbyLocations.add(b);
            Log.d("Near", "no nearby "+nearbyLocations.size()+" "+currentLocation.getProvider());
        } else {
            Log.d("Near", "should be nearby!!");
        }
        currentNearbyList = nearbyLocations;
    }
    private static double getDistanceFromCurrentLoc(Location location){  //also uses currentLatitude and currenentLongitude
        if ( location == null || currentLocation == null){
            if ( currentLocation == null){
                Log.e("NEAR", "currentLocation was null");
            }
            return 10000;
        }
        return currentLocation.distanceTo(location);
    }

}
