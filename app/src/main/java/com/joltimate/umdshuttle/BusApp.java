package com.joltimate.umdshuttle;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Chris on 7/14/2015.
 */
public class BusApp extends Application {
    public static GoogleAnalytics analytics;
    public static Tracker tracker;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
       // PsiMethod:onCreateFabric.with(this, new Crashlytics());
       analytics = GoogleAnalytics.getInstance(this);
        // Set the log level to verbose.
        //analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
        analytics.setLocalDispatchPeriod(450);

        tracker = analytics.newTracker("UA-58569843-3"); //todo Replace with actual tracker/property Id
        //tracker.enableExceptionReporting(true); Don't enable this fucks with crash reporting!!
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);

        tracker.setScreenName("Entered bus app");

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("UX")
                .setAction("Click icon")
                .setLabel("Bus App")
                .build());
    }
    private Tracker mTracker;

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
       if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
            //mTracker.enableExceptionReporting(true);
            mTracker.enableAdvertisingIdCollection(true);
            mTracker.enableAutoActivityTracking(true);
        }
        return mTracker;
    }
}
