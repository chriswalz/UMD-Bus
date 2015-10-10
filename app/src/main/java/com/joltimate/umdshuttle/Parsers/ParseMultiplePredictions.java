package com.joltimate.umdshuttle.Parsers;

import android.util.Log;

import com.joltimate.umdshuttle.BusEntry;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Chris on 6/10/2015.
 */
public class ParseMultiplePredictions extends Parser {
    // search terms
    private final String direction = "direction";
    private final String prediction = "prediction";
    // attributes
    private final String seconds = "seconds";
    private final String tag = "seconds";
    String noPredictions = "Bus Not Running";
    private String stopInfo;
    private String routeLink;

    @Override
    public ArrayList<BusEntry> readLines(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<BusEntry> entries = new ArrayList<BusEntry>();
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            String name = parser.getName();
            if ( name != null && parser.getEventType() == XmlPullParser.START_TAG && name.equals("predictions")){
                stopInfo = getAttributeValue(parser, "stopTitle");
                routeLink = getAttributeValue(parser, "routeTag");
                String noPredictions = getAttributeValue(parser, "dirTitleBecauseNoPredictions"); // if this equals error predictions are available
                Log.d("ParseMultPre", "DirTitleValue = "+noPredictions);
                if ( !noPredictions.equals("Error")){
                    BusEntry noP = new BusEntry(stopInfo, "No Bus Running");
                    noP.setSpecialCompare(stopInfo + routeLink);
                    entries.add(noP);
                }
            }
            if (name != null && parser.getEventType() == XmlPullParser.START_TAG && name.equals(direction)){
                parser.next();
                parser.next(); // do one for start tag, one for end tag!
                name = parser.getName();
                if (name != null ){
                }
                if ( name != null && parser.getEventType() == XmlPullParser.START_TAG && name.equals(prediction)){
                    parseTitleAndTag(entries, parser, seconds, tag);
                }
            }
        }
        BusEntry.state = BusEntry.INFOROUTE;
        Collections.sort(entries);
        BusEntry.state = BusEntry.REGULAR;
        // add error text post sort always
        if ( entries.size() == 0 ){
            //Log.e("ParseMultiPredictions","No predictions");
            entries.add(new BusEntry(noPredictions, noPredictions));
        }
        return entries;
    }
    @Override
    public void parseTitleAndTag(ArrayList<BusEntry> entries, XmlPullParser parser, String title, String tag){
        String val = stopInfo;
        String link = parsePrediction(parser, title);
        BusEntry entry = new BusEntry(val,link);
        entry.setSpecialCompare(stopInfo + routeLink);
        entries.add(entry);
    }

    private String parsePrediction(XmlPullParser parser, String attribute){
        String val = getAttributeValue(parser, attribute);
        double num = Integer.valueOf(val);
        double minutes = num/60;
        int seconds = (int)((minutes - Math.floor(minutes))*60);
        Log.e("ParseMultiplePredic", "" + minutes + " " + seconds + " " + (minutes == 0));
        if (minutes == 0) {
            val = "Now";
        } else {
            val = ((int) minutes) + " minutes " + seconds + " seconds";
        }
        return val;
    }
}

/*

            if (name != null && parser.getEventType() == XmlPullParser.START_TAG && name.equals(direction)){
                parser.next();
                parser.next(); // do one for start tag, one for end tag!
                name = parser.getName();
                if (name != null ){
                }
                if ( name != null && parser.getEventType() == XmlPullParser.START_TAG && name.equals(prediction)){
                    parseTitleAndTag(entries, parser, seconds, tag);
                }
            }
 */