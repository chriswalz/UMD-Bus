package com.joltimate.umdshuttle;

import android.util.Log;

import com.joltimate.umdshuttle.Fetchers.FetchXml;
import com.joltimate.umdshuttle.ScreenManagers.RO;

/**
 * Created by Chris on 7/12/2015.
 */
public class DebuggingTools {
    public static final boolean ISDEBUG = false;
    private static String className = "Debugging Tools";

    public static void logCurrentTask(){
        switch (FetchXml.currentTask){
            case RO.AGENCIESTASK:
                Log.d(className, "Agencies Task");
                break;
            case RO.ROUTESTASK:
                Log.d(className, "Routes Task");
                break;
            case RO.DIRECTIONSTASK:
                Log.d(className, "Directions Task");
                break;
            case RO.STOPSTASK:
                Log.d(className, "Stops Task");
                break;
            case RO.PREDICTIONTASK:
                Log.d(className, "Predictions Task");
                break;
        }
    }

    public static void logd(String className, String message) {
        if (ISDEBUG) {
            Log.d(className, message);
        }
    }
}
