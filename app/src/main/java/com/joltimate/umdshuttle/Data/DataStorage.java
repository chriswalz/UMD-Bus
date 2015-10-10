package com.joltimate.umdshuttle.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joltimate.umdshuttle.BusEntry;
import com.joltimate.umdshuttle.Fetchers.FetchRoutes;
import com.joltimate.umdshuttle.MainActivity;
import com.joltimate.umdshuttle.ScreenManagers.RO;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 7/11/2015.
 */
public class DataStorage {
    private static String className = "DataStorage";
    public static boolean isSyncing = false;
    private static void saveDataList(ArrayList<BusEntry> list, String key1, String thisDoesNothnig){
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(RO.mainActivity.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        ArrayList<BusEntry> entries = list;
        Gson gson = new Gson();
        String jsonEntries = gson.toJson(entries);
        //Log.d("TAG", "jsonCars = " + jsonEntries);
        prefsEditor.putString(key1, jsonEntries);
        prefsEditor.commit();

    }
    private static void saveDataList(ArrayList<ArrayList<BusEntry>> list, String key1, int thisDoesNothing){
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(RO.mainActivity.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        ArrayList<ArrayList<BusEntry>> entries = list;
        Gson gson = new Gson();
        String jsonEntries = gson.toJson(entries);
        //Log.d("TAG", "jsonCars = " + jsonEntries);
        prefsEditor.putString(key1, jsonEntries);
        prefsEditor.commit();

    }
    private static void saveDataList(ArrayList<ArrayList<ArrayList<BusEntry>>> list, String key1){
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(RO.mainActivity.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        ArrayList<ArrayList<ArrayList<BusEntry>>> entries = list;
        Gson gson = new Gson();
        String jsonEntries = gson.toJson(entries);
        //Log.d("TAG", "jsonCars = " + jsonEntries);
        prefsEditor.putString(key1, jsonEntries);
        prefsEditor.commit();

    }
    private static Object getDataList(String key, int task){
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(RO.mainActivity.getApplicationContext());
        String jsonString = appSharedPrefs.getString(key, ""); // TODO use empty string as an if statement to determine if you need to recache
        Type type;
        if ( task == RO.ROUTESTASK ){
            type = new TypeToken<ArrayList<BusEntry>>(){}.getType(); // <---
        } else if ( task == RO.DIRECTIONSTASK){
            type = new TypeToken<ArrayList<ArrayList<BusEntry>>>(){}.getType(); // <-----ArrayList<ArrayList<BusEntry>>
        } else {
            type = new TypeToken<ArrayList<ArrayList<ArrayList<BusEntry>>>>(){}.getType(); //<-----
        }
        Gson gson = new Gson();
        return gson.fromJson(jsonString, type);
    }
    public static void saveAllData(){
        String r = "ro";
        String d = "d";
        String s = "s";
        /*
        if getString.equals "" FetchRoutes.start
        else getDataStorage?
         */
        int i,j, x = 0;
        //Log.d(className, "Began Saving");
        RO.mainActivity.mRoRecyclerView.setVisibility(View.GONE);
        saveDataList(RO.routes, r, null);
        saveDataList(RO.directions, d, 0);
        saveDataList(RO.stops, s);
        RO.mainActivity.mRoRecyclerView.setVisibility(View.VISIBLE);
       // Log.d(className, "Finished Saving");

    }
    public static void getAllData(){
        String r = "ro";
        String d = "d";
        String s = "s";

        int i,j, x = 0;
        //Log.d(className, "Began retrieval");
        RO.routes = (ArrayList<BusEntry>)getDataList(r, RO.ROUTESTASK); // <---
        RO.directions = (ArrayList<ArrayList<BusEntry>>)getDataList(d, RO.DIRECTIONSTASK); // <---
        RO.stops = (ArrayList<ArrayList<ArrayList<BusEntry>>>)getDataList(s, RO.STOPSTASK); //<-----

        //Log.d(className, "Completed Retrieval");
    }
    public static void handleDataStorage(boolean force){
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(RO.mainActivity.getApplicationContext());
        String jsonString = appSharedPrefs.getString("ro", "");
        //Log.d("DATA STORAGE", jsonString+"  |||||||||||||||||||||||");
        // "[{\"info\":\"No Times Available\",\"isFavorited\":false,\"link\":\"No Times Available\"}]"
        // 130 is really arbitrary but if there was route inforamiotn it'd be over 9000 characters
        if ( jsonString.length() <= 130 && isNetworkAvailable()){
            Snackbar.make(RO.mainActivity.holder, "Loading data - this only happens once.", Snackbar.LENGTH_LONG).show();
            resyncData();
            return;
        }
        if ( (jsonString.equals("") || force)  ){ // todo this will only run when nothing is saved. Resynch button will actually run getALLdata ??????
            // Toast.makeText(RO.mainActivity.getApplicationContext(),"Loading data - this only happens once.",Toast.LENGTH_LONG).show();
            MainActivity.swipeRefreshLayout.setRefreshing(true);
            //FetchRoutes.startFetch();
            resyncData();
        } else {
           // Log.i("DataStorage","Getting data");
            getAllData();
            updateDataWithFavorites();
            RO.changeToRoutes(null);
        }
    }
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) RO.mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public static void saveFavorites(ArrayList<BusEntry> favorites){
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(RO.mainActivity.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        ArrayList<BusEntry> entries = favorites;
        Gson gson = new Gson();
        String jsonEntries = gson.toJson(entries);
        //Log.d("TAG", "jsonCars = " + jsonEntries);
        prefsEditor.putString("favs", jsonEntries);
        prefsEditor.commit();
    }
    private static void updateDataWithFavorites(){
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(RO.mainActivity.getApplicationContext());
        String jsonString = appSharedPrefs.getString("favs", ""); // TODO use empty string as an if statement to determine if you need to recache
        if ( jsonString.equals("")) return;
        Type type = new TypeToken<ArrayList<BusEntry>>(){}.getType();
        Gson gson = new Gson();
        ArrayList<BusEntry> favorites =  gson.fromJson(jsonString, type);
        ArrayList<BusEntry> temp;
        int i, j, k, m;
        BusEntry entry;
        for ( i = 0; i < RO.stops.size(); i++){ // todo dat O(n^4)
            for ( j = 0; j < RO.stops.get(i).size(); j++){
                temp = RO.stops.get(i).get(j);
                if ( temp != null ) {
                    for (k = 0; k < temp.size(); k++) {
                        for (k = 0; k < temp.size(); k++) {
                            entry = temp.get(k);
                            if (favorites.contains(entry)) {
                                for ( m = 0; m < favorites.size(); m++){
                                    BusEntry fav=favorites.get(m);
                                    String tempRouteLink = RO.routes.get(i).getLink();
                                    String tempDirLink = RO.directions.get(i).get(j).getLink();
                                    if ( fav.getDirTag() != null && fav.getRouteTag() != null){
                                        if(fav.getDirTag().equals(tempDirLink) && fav.getRouteTag().equals(tempRouteLink)){
                                            entry.setIsFavorited(true);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    public static void resyncData(){
        isSyncing = true;
        FetchRoutes.startFetch();
    }
}