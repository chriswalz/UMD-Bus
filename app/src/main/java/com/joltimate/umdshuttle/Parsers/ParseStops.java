package com.joltimate.umdshuttle.Parsers;

import android.location.Location;

import com.joltimate.umdshuttle.BusEntry;
import com.joltimate.umdshuttle.ScreenManagers.RO;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Chris on 6/10/2015.
 */
public class ParseStops extends Parser {
    private final String className = "ParseStops";
    // search terms
    private final String notAvailable = "Stops unavailable";
    private final String direction = "direction";
    private final String stop = "stop";
    // attributes
    private final String title = "title";
    private final String stopId = "stopId";
    // special attribute for finding direction
    private final String tagDirection = "tag";

    @Override
    public ArrayList<BusEntry> readLines(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<BusEntry> allStops = new ArrayList<BusEntry>();
        ArrayList<BusEntry> entries = new ArrayList<BusEntry>();

        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            String name = parser.getName();
            if ( name != null ){
                if ( name.equals(stop) && parser.getEventType() == XmlPullParser.START_TAG){
                    parseTitleStopIDandTagAndStopTag(allStops, parser, title, stopId, tagDirection);
                }
                //Log.i("inCorrectDirection", ""+inCorrectDirection(parser,name));
                if ( inCorrectDirection(parser, name)){
                    if (parser.getEventType() == XmlPullParser.START_TAG && name.equals(stop)){
                        parseJustTheTag(allStops, entries, parser, tagDirection);
                    }
                }
            }
        }
        //Log.i("ParseStops", entries.toString());
        //Log.i("ParseStops", allStops.toString());
        if ( entries.size() == 0 ){
            entries.add(new BusEntry(notAvailable, notAvailable));
        }
        return entries; // todo change back to entries, then fix the entries problem (always empty)
    }
    private boolean hasntReadInitialStops = true;
    private boolean inDirection = false;
    // This retrieves the value of a stop that has a direction parent tag
    void parseJustTheTag(ArrayList<BusEntry> allStops, ArrayList<BusEntry> entries, XmlPullParser parser, String tagDirection){
        String link = getAttributeValue(parser, tagDirection);
        int index;
        //Log.d(className, link);
        if ( !link.equals(error) ){ //TODO this is a work around
            BusEntry entry = new BusEntry(link,link);
            if ( (index = specialContains(allStops, entry)) != -1){
                entries.add(allStops.get(index));
                //allStops.remove(index);
            }
        }
    }
    private int specialContains(ArrayList<BusEntry> allStops, BusEntry entry){
        int i, len = allStops.size();
        for ( i = 0; i < len; i++){
            if (allStops.get(i).getExtra().equals(entry.getLink())){
                //allStops.remove(i);
                return i;
            }
        }
        return -1;
    }
    public boolean inCorrectDirection(XmlPullParser parser, String name)throws XmlPullParserException{
        if ( name.equals(direction) && parser.getEventType() == XmlPullParser.START_TAG){
            String val = getAttributeValue(parser, "tag");
            //Log.i("inCorrectDirection", ""+val+" "+RO.direction.getLink());
            if ( val.equals(RO.direction.getLink())){
                inDirection = true;
                hasntReadInitialStops = false;
            }
        }
        if ( name.equals(direction) && parser.getEventType() == XmlPullParser.END_TAG){
            inDirection = false;
        }
        return inDirection;
    }
    // This method is used to get data of a regular stop line
    void parseTitleStopIDandTagAndStopTag(ArrayList<BusEntry> allstops, XmlPullParser parser, String title, String stopId, String tagDirection){
        String val = getAttributeValue(parser, title);
        String link = getAttributeValue(parser, stopId);
        String extra = getAttributeValue(parser, tagDirection);
        String lat = getAttributeValue(parser, "lat");
        String lon = getAttributeValue(parser, "lon");
        //String stopTag = getAttributeValue(parser, "tag");
        BusEntry entry;
        if ( !val.equals(error) && !link.equals(error) && !extra.equals(error) ){
            entry = new BusEntry(val,link);
            entry.setExtra(extra);
            if ( !lat.equals(error) && !lon.equals(error)){
                Location parsedLocation =  new Location("");
                parsedLocation.setLatitude(Double.valueOf(lat));
                parsedLocation.setLongitude(Double.valueOf(lon));
                entry.location = parsedLocation;
            }
            allstops.add(entry);
        }
    }
}
