package com.joltimate.umdshuttle;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.joltimate.umdshuttle.Adapters.RecyclerFavAdapter;
import com.joltimate.umdshuttle.Adapters.RecyclerNearbyAdapter;
import com.joltimate.umdshuttle.Adapters.RecyclerRouteAdapter;
import com.joltimate.umdshuttle.Data.DataStorage;
import com.joltimate.umdshuttle.Fetchers.FetchPredictions;
import com.joltimate.umdshuttle.Fetchers.FetchXml;
import com.joltimate.umdshuttle.ScreenManagers.FAV;
import com.joltimate.umdshuttle.ScreenManagers.NEAR;
import com.joltimate.umdshuttle.ScreenManagers.Overseer;
import com.joltimate.umdshuttle.ScreenManagers.RO;
import com.joltimate.umdshuttle.SpinnerAdapters.BusSpinnerAdapter;

import java.util.ArrayList;
/*
IF YOU'RE HAVING AN ISSUE RANDOMLY IT'S LIKELY BECAUSE THE ARRAYS HAVEN'T ALLOCATED ENOUGH MEMORY

Before uploading:
    set dryrun to false
    make sure version number is incremented
debugging
    use android device monitor
Features to add:
OnGoing:

    show version number on app
    change menu background color
    add toast when wifi isnt available
    use snackbars -- doesnt seem to work, needs appcompatactivity and othershit
    animations
    get crashlytics working (or alternative)
    BUG: weird data problem, i used work around
    onclick in favorites brings back to routes tab
    fix memory leak
    change stopid to a busentry
   add view abstraction, add base RecylerAdaper and add two more reyclerviews
    increase favoriting speed
    recent menu
    use async executor? AsyncTask.executeOnExecutor(Executor exec, Params... params)
    add version number somewhere to app
    material design
    add compatibility to ice cream sandwich!
Code:
    Comment out important methods (use javadoc?)
    Cleanse main activity (push into fetch xml or RO
    create sub packages
    versioning
Speed:
    Cache routes, directions, stops - add resync option
    Request compressed xml data for fast downloads
Battery:
    Request data 1 minute or greater (data only updates minute by minute anyway). Radio turns goes into low power mode after 20 seconds
    Limit user requests to once every 30 seconds?
Errors:
    too many requests;
    no wifi
    no predictions
    not departed
UX:
    State time of last update.
    only portrait mode
    minimalist
    material design
    nearest stop
    animations
    alerts: "Alert me 10 minutes before the bus arrives
    add bus logo behind listview?
Known Issues:
        add version number
        fix logo quality
        if you click really quickly the app crashes
7/15/2015
    Use a divider
    Share button
    check for internet connection Boolean
    set minified to true
    swipe navigation?
    FAB search button
    Snack bar
    Collapsing toolbar

Added:
    fixed favorites not being added and removed properly
    better navigation, 3 choose buttons that have autocomplete for route, direction and stop,
    Fix direction locating
    make favorites persistent
 add google analytics (make a baseactivyt?)
fix in accurate stops,
    show refresh on initial startup
    when back button is pressed, the list view should go back centered on the last chosen item
    use better save mechanism - app no longer uses a for loop when saving
    settings button to reset persistent data
    make persistent data (shared  preferences)
    add caching cascading of async tasks
    xml parsing
    replace listview with recycler view
    listviews change on click (no activity change either)
    swipe to refresh
    Make parsers more lean
    only show stops based on direction
    actionbar title changes
    backwards navigation
    CLEANSE TO DO AND Logging AND imports
    add predictions as textview in favorites!

    9000GladiatorsWin
 */

public class MainActivity extends BaseJoltimateActivity {
    public static SwipeRefreshLayout swipeRefreshLayout;
    public static Spinner routeSpinner;
    public static Spinner directionSpinner;
    public static Spinner stopSpinner;
    public static TextView routeText;
    public static TextView directionText;
    public static TextView stopText;
    public static TextView etaText;
    public static FloatingActionButton fab;
    public static int directionsPosition = 0;
    public static TabLayout tabLayout;
    // public TextView routeView;
    public RecyclerView mRoRecyclerView;
    public RecyclerView mFavoritesRecyclerView;
    public RecyclerView mNearbyRecyclerView;
    public Menu menuMain;
    public CoordinatorLayout holder;
    public LinearLayout mainLayout;
    //private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager roLinLayoutManager;
    private RecyclerView.LayoutManager favLinLayoutManager;
    private RecyclerView.LayoutManager nearbyLinLayoutManager;
    private boolean ranOnce2 = false;
    private boolean ranOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RO.mainActivity = this;

        mainLayout = (LinearLayout) findViewById(R.id.main_linear);

        mRoRecyclerView = (RecyclerView) findViewById(R.id.ro_recycler_view);
        mFavoritesRecyclerView = (RecyclerView) findViewById(R.id.favorites_recycler_view);
        mNearbyRecyclerView = (RecyclerView) findViewById(R.id.nearby_recycler_view);
        //mRoRecyclerView.setHasFixedSize(true); // could cause problems, swipe to refresh could as well

        routeText = (TextView) findViewById(R.id.route_text);
        directionText = (TextView) findViewById(R.id.direction_text);
        stopText = (TextView) findViewById(R.id.stop_text);
        etaText = (TextView) findViewById(R.id.eta_text);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        holder = (CoordinatorLayout) findViewById(R.id.coordinator);

        // use a linear layout manager
        roLinLayoutManager = new LinearLayoutManager(this);
        mRoRecyclerView.setLayoutManager(roLinLayoutManager);
        favLinLayoutManager = new LinearLayoutManager(this);
        mFavoritesRecyclerView.setLayoutManager(favLinLayoutManager);
        nearbyLinLayoutManager = new LinearLayoutManager(this);
        mNearbyRecyclerView.setLayoutManager(nearbyLinLayoutManager);//new GridLayoutManager(this, 2));

        // specify an rAdapter (see also next example)
        RO.rAdapter = new RecyclerRouteAdapter();
        mRoRecyclerView.setAdapter(RO.rAdapter);

        FAV.favAdapter = new RecyclerFavAdapter();
        mFavoritesRecyclerView.setAdapter(FAV.favAdapter);

        NEAR.nearbyAdapter = new RecyclerNearbyAdapter();
        mNearbyRecyclerView.setAdapter(NEAR.nearbyAdapter);

        //adapter.addAll(RO.routes);
        //setUpSpinner(spinner);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.primary);
        swipeRefreshLayout.setColorSchemeResources(R.color.white);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        //Log.i("Main Activity", "onRefresh called from SwipeRefreshLayout");
                        //Log.i("Main Activity", "hello");
                        //RO.route = RO.routeAdapter.getItem(0); //<<<<fix
                        // RO.route = RO.routes.get(RO.getRoutePosition());
                        //Log.d("MainActivity", String.valueOf(FetchXml.currentTask == RO.PREDICTIONTASK));
                        if (FetchXml.currentTask == RO.PREDICTIONTASK) {
                            //FetchXml.startFetch(); // refreshing current list
                            ArrayList<BusEntry> stopList = new ArrayList<>();
                            stopList.add(RO.stop);
                            FetchPredictions.startFetch(stopList);
                        } else {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }
        );
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RO.stop.toggleFavorited(null);
                setUpMenuStar();
                if (RO.stop.isFavorited()) {
                    Snackbar.make(holder, RO.stop.getInfo() + " added to favorites", Snackbar.LENGTH_SHORT)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    RO.stop.toggleFavorited(null);
                                    setUpMenuStar();
                                }
                            })
                            .show();
                } else {
                    Snackbar.make(holder, RO.stop.getInfo() + " removed from favorites", Snackbar.LENGTH_SHORT)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    RO.stop.toggleFavorited(null);
                                    setUpMenuStar();
                                }
                            })
                            .show();
                }
            }
        });


        makeSnackBar();
        DataStorage.handleDataStorage(false); // must be run after setOnRefreshlistener
        //RO.changeToPredictions(null);
        setUpRouteSpinner();
        setUpDirectionSpinner();
        setUpStopSpinner();

        setUpThemeColors();
        setUpTabs();

    }

    private void setUpRouteSpinner() {
        routeSpinner = (Spinner) findViewById(R.id.route_spinner);
        if ( RO.routes != null ){
            RO.routeAdapter = new BusSpinnerAdapter(this, R.layout.material_item, RO.routes);
            routeSpinner.setAdapter(RO.routeAdapter);
        }
        routeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //TextView textView = (TextView) view.findViewById(R.id.material_item);
                //Log.i("MainActivity", RO.route.getInfo());
                if ( !RO.nearbyIsClicked){
                    RO.route = RO.routeAdapter.getItem(position);
                    RO.updateDirections(position, directionSpinner);
                    //Log.d("MainActivity", "routeSpinner was selected");
                }
                //FetchPredictions.startFetch();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpDirectionSpinner() {
        directionSpinner = (Spinner) findViewById(R.id.dir_spinner);
        RO.updateDirections(0, directionSpinner);
        directionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //TextView textView = (TextView) view.findViewById(R.id.material_item);
                //Log.i("MainActivity", " "+RO.direction.getInfo());
                if ( RO.direction != null && !RO.nearbyIsClicked){
                    RO.direction = RO.directionAdapter.getItem(position);
                    RO.updateStops(position, stopSpinner);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpStopSpinner() {
        stopSpinner = (Spinner) findViewById(R.id.stop_spinner);
        RO.updateStops(0, stopSpinner);
        stopSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RO.stop = RO.stopAdapter.getItem(position);
                //Log.i("MainActivity", RO.stop.getRouteTag());
                if (!RO.nearbyIsClicked) {
                } else {
                    RO.nearbyIsClicked = false;
                }
                specialOnItemSelected();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void specialOnItemSelected(){
        ArrayList<BusEntry> stopList = new ArrayList<>();
        stopList.add(RO.stop);
        FetchPredictions.startFetch(stopList);
        setUpMenuStar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuMain = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        } */
        if (id == R.id.resync_item) {
            //Log.d("Main Activity","Resync Clicked");
            if ( DataStorage.isNetworkAvailable()){
                Snackbar.make(RO.mainActivity.holder, "Updating route information.", Snackbar.LENGTH_LONG).show();
                DataStorage.resyncData();
            } else {
                Snackbar.make(RO.mainActivity.holder, "No internet connection.", Snackbar.LENGTH_LONG).show();
            }
            return true;
        } else if (id == R.id.action_refresh) {
            Overseer.refreshCurrentView();
        }


        return super.onOptionsItemSelected(item);
    }

    public void setUpMenuStar(){
        if (RO.stop.isFavorited()){
            fab.setImageResource(R.drawable.star);
            //menuMain.getItem(0).setIcon(R.drawable.star); //.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
        } else {
            fab.setImageResource(R.drawable.outlined_star);
           // menuMain.getItem(0).setIcon(R.drawable.outlined_star);
        }
    }

    private void setUpThemeColors(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //window.setStatusBarColor(Color.rgb(211, 47, 47));
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.primary));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            final ActionBar supportActionBar = getSupportActionBar();
            if (supportActionBar != null) {
                getSupportActionBar().setTitle("UMD Bus");
                getSupportActionBar().setIcon(R.mipmap.ic_launcher);
            }
        }
    }

    private void makeSnackBar(){
        Snackbar.make(holder, "Welcome", Snackbar.LENGTH_SHORT)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .show();
    }

    private void setUpTabs(){
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.addTab(tabLayout.newTab().setText("Transit"));
        tabLayout.addTab(tabLayout.newTab().setText("Favorites"));
        tabLayout.addTab(tabLayout.newTab().setText("Nearby"));
        tabLayout.setTabTextColors(Color.rgb(230, 235, 235), Color.WHITE);
        tabLayout.setSelectedTabIndicatorColor(Color.rgb(245,124,0));
       // tabLayout.setSelectedTabIndicatorHeight(TabLayout);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabChosen(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                tabChosen(tab.getPosition());
            }
        });
    }
    private void tabChosen(int tabPosition){
        if ( tabPosition ==  0 ){
            Overseer.changeToRVIEW();

        }
        else if ( tabPosition == 1){
            Overseer.changeToFAVVIEW();
        } else {
            //Snackbar.make(holder, "Recents & Nearby coming soon", Snackbar.LENGTH_LONG).show();
            Overseer.changeToNEARView();
        }
    }
    @Override
    public void onBackPressed() {

    }

}
