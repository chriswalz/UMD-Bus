package com.joltimate.umdshuttle.Fetchers;

import android.os.AsyncTask;
import android.util.Log;

import com.joltimate.umdshuttle.BusEntry;
import com.joltimate.umdshuttle.MainActivity;
import com.joltimate.umdshuttle.Parsers.Parser;
import com.joltimate.umdshuttle.ScreenManagers.RO;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Chris Walz on 6/30/2015.
 */

public abstract class FetchXml extends AsyncTask<String, String, ArrayList<BusEntry> > {
    public static String dataError = "No Times Available";
    public static int currentTask = 0;
    public static int cacheI = 0;
    public static int cacheJ = 0;

    public static void commonPostClick(ArrayList<BusEntry> s) {
        RO.rAdapter.add(s);
    }

    // refreshes current list
    public static void startFetch() {
        switch (FetchXml.currentTask) {
            case RO.AGENCIESTASK:
                FetchAgency.startFetch();
                break;
            case RO.ROUTESTASK:
                FetchRoutes.startFetch();
                break;
            case RO.DIRECTIONSTASK:
                FetchDirections.startFetch();
                break;
            case RO.STOPSTASK:
                FetchStops.startFetch();
                break;
            case RO.PREDICTIONTASK:
                FetchPredictions.startFetch();
                break;
            default:
                Log.e("FetchXml", "Unknown current task");
                break;
        }
    }

    public static void nextTask(int position) {
        switch (FetchXml.currentTask) {
            case RO.AGENCIESTASK:
                FetchAgency.setDataOnClick(position);
                break;
            case RO.ROUTESTASK:
                FetchRoutes.setDataOnClick(position);
                break;
            case RO.DIRECTIONSTASK:
                FetchDirections.setDataOnClick(position);
                break;
            case RO.STOPSTASK:
                FetchStops.setDataOnClick(position);
                break;
            case RO.PREDICTIONTASK:
                // nothing?
                break;
        }
    }

    @Override // core
    protected void onPreExecute() {
        super.onPreExecute();
        MainActivity.swipeRefreshLayout.setRefreshing(true);
    }

    // core
    @Override
    protected ArrayList<BusEntry> doInBackground(String... params) {
        String urlString = params[0];
        ArrayList<BusEntry> resultToDisplay = null;
        InputStream in = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(3000);
            in = new BufferedInputStream(urlConnection.getInputStream());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

        Parser parse = createParser();

        ArrayList<BusEntry> list = null;
        try {
            list = parse.parse(in);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // todo remove this line
        if (list != null && list.size() > 0) {
            resultToDisplay = list;
        } else {
            resultToDisplay = null;
        }
        return resultToDisplay; // return updated times
    }

    // will be abstract
    public abstract Parser createParser();

    @Override
    protected void onPostExecute(ArrayList<BusEntry> s) { //param is updated time, update view
        super.onPostExecute(s);
        // Todo add error cases: no internet, error response, no predictions, predictions
        if (s == null) {
            s = new ArrayList<BusEntry>();
            s.add(new BusEntry(dataError, dataError));
        } else {
            //System.out.println(s.get(0).getInfo());
        }
        updateData(s);
        // MainActivity.swipeRefreshLayout.setRefreshing(false);

        // set last updated text
    }

    // make abstract
    public abstract void updateData(ArrayList<BusEntry> s);
}
