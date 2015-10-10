package com.joltimate.umdshuttle;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.joltimate.umdshuttle.ScreenManagers.NEAR;
import com.joltimate.umdshuttle.ScreenManagers.Overseer;

/**
 * Created by Chris on 7/14/2015.
 */
public class BaseJoltimateActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private String TAG = "com.joltimate.mainActivity";
    private String screenName = "Routes Screen";
    public static Tracker t;
    protected Location mLastLocation;

    protected GoogleApiClient mGoogleApiClient;
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    private static final String DIALOG_ERROR = "dialog_error";
    private boolean mResolvingError = false;

    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 4;
    LocationRequest locationRequest = getLocationRequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtain the shared Tracker instance.
       BusApp application =  (BusApp) getApplication();
        t = application.getDefaultTracker();
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "Setting screen name: " + screenName);
       /* t.setScreenName("Image~" + screenName);
        t.send(new HitBuilders.ScreenViewBuilder().build()); */
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mResolvingError ) {  // more about this later
            mGoogleApiClient.connect();
        } else {
            Log.d("BaseJolt", "Rut roh");
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if ( mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }
    private LocationRequest getLocationRequest(){
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(1000);
        return locationRequest;
    }
    @Override
    public void onConnected(Bundle bundle) {
        Log.d("BaseJolt", "App is connected");
        initalizeLocationUpdates();
    }
    private void initalizeLocationUpdates(){
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        updateLocation(mLastLocation);
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (connectionResult.hasResolution()) {
            try {
                mResolvingError = true;
                connectionResult.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            Toast.makeText(getApplicationContext(), "No location "+connectionResult.getErrorCode(), Toast.LENGTH_LONG).show();
            mResolvingError = true;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("BaseJolt", "AOEU --- --- -ON LOCATION WAS CALLED- -------------AOEUAOEU");
        updateLocation(location);
    }
    private void updateLocation(Location location){
        mLastLocation = location;
        if ( mLastLocation != null){
            mLastLocation.setProvider("notnull");
            Log.d("BaseJolt", "Location was a ref!");
        } else {
            Log.d("BaseJolt", "Location was null!");
            mLastLocation = new Location("null");
        }
        NEAR.currentLocation = mLastLocation;
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }
    private boolean isLocationPermissionGranted(){
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
        //return true;
    }
    private void startLocationUpdates() {
        if ( isLocationPermissionGranted())
        {
            //LocationRequest locationReq = createLocationRequest();
            //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationReq, this);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,  locationRequest, this);
            Log.d("BaseJolt", "there is permission");
        } else {
            // do nothing permission wasnt granted :(
            Log.d("BaseJolt", "no permission");
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initalizeLocationUpdates();
                    Overseer.changeToNEARView();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        if ( mGoogleApiClient.isConnected()){
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }
    public void enableLocationPermission(){
        if ( !isLocationPermissionGranted()){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
            }
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
}
