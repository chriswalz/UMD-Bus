package com.joltimate.umdshuttle.Fetchers;

/**
 * Created by Chris on 7/7/2015.
 */


import com.joltimate.umdshuttle.BusEntry;
import com.joltimate.umdshuttle.Parsers.ParseAgencies;
import com.joltimate.umdshuttle.Parsers.Parser;
import com.joltimate.umdshuttle.ScreenManagers.RO;

import java.util.ArrayList;

/**
 * Created by Chris on 6/30/2015.
 */

public class FetchAgency extends FetchXml {
    private static BusEntry entry;
    public FetchAgency(){
        currentTask = RO.AGENCIESTASK;
    }

    @Override
    public Parser createParser(){
        return new ParseAgencies();
    }
    @Override
    public void updateData(ArrayList<BusEntry> s){
            RO.changeToAgencies(s);
    }
    // make abstract
    public static void startFetch(){
        String agencies = "http://webservices.nextbus.com/service/publicXMLFeed?command=agencyList";
        new FetchAgency().execute(agencies);
    }
    // make abstract
    public static void setDataOnClick(int position){
        entry = RO.agencies.get(position);
        RO.agency.setInfoLink(entry.getInfo(),entry.getLink());
        RO.changeToAgencies(null);
    }
}