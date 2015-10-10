package com.joltimate.umdshuttle.Parsers;
import android.util.Log;

import com.joltimate.umdshuttle.BusEntry;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class ParsePredictions extends Parser {
    // search terms
    private final String direction = "direction";
    private final String prediction = "prediction";
    // attributes
    private final String seconds = "seconds";
    private final String tag = "seconds";
    String noPredictions = "Bus Not Running";

    @Override
    public ArrayList<BusEntry> readLines(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<BusEntry> entries = new ArrayList<BusEntry>();
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            String name = parser.getName();
            if ( name != null && parser.getEventType() == XmlPullParser.START_TAG && name.equals(prediction)){
                parseTitleAndTag(entries, parser, seconds, tag);
            }

        }
        BusEntry.state = BusEntry.NUMBERS;
        Collections.sort(entries);
        BusEntry.state = BusEntry.REGULAR;
        // add error text post sort always
        if ( entries.size() == 0 ){
            Log.e("ParseMultiPredictions", "No predictions");
            entries.add(new BusEntry(noPredictions, noPredictions));
        }
        return entries;
    }
    @Override
    public void parseTitleAndTag(ArrayList<BusEntry> entries, XmlPullParser parser, String title, String tag){
        String val = parsePrediction(parser, title);
        String link = getAttributeValue(parser, tag);
        BusEntry entry = new BusEntry(val,link);
        entries.add(entry);
    }

    private String parsePrediction(XmlPullParser parser, String attribute){
        String val = getAttributeValue(parser, attribute);
        double num = Integer.valueOf(val);
        double minutes = num/60;
        int seconds = (int)((minutes - Math.floor(minutes))*60);
        Log.e("ParsePred", "" + minutes + " " + seconds + " " + (minutes == 0));
        if (Math.floor(minutes) == 0) {
            val = "Now";
        } else {
            val = ((int) minutes) + " minutes " + seconds + " seconds";
        }
        return val;
    }
}




/*package com.joltimate.umdshuttle.Parsers;

import android.util.Log;
import android.util.Xml;

import com.joltimate.umdshuttle.BusEntry;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;


public class ParsePredictions extends Parser {
    String noPredictions = "Bus Not Running";

    // search terms
    private final String prediction = "prediction";
    // attributes
    private final String seconds = "seconds";
    private final String tag = "seconds";

    @Override
    public ArrayList<BusEntry> readLines(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<BusEntry> entries = new ArrayList<BusEntry>();
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            String name = parser.getName();
            if ( name != null && parser.getEventType() == XmlPullParser.START_TAG && name.equals(prediction)){
                parseTitleAndTag(entries, parser, seconds, tag);
            }
        }
        Collections.sort(entries);
        // add error text post sort always
        if ( entries.size() == 0 ){
            Log.e("ParsePredictions","No predictions");
            entries.add(new BusEntry(noPredictions, noPredictions));
        }
        return entries;
    }
    @Override
    public void parseTitleAndTag(ArrayList<BusEntry> entries, XmlPullParser parser, String title, String tag){
        String val = parsePrediction(parser, seconds);
        String link = getAttributeValue(parser, tag);
        BusEntry entry = new BusEntry(val,link);
        entries.add(entry);
    }

    private String parsePrediction(XmlPullParser parser, String attribute){
        String val = getAttributeValue(parser, attribute);
        double num = Integer.valueOf(val);
        double minutes = num/60;
        int seconds = (int)((minutes - Math.floor(minutes))*60);
        val = ((int)minutes)+" minutes "+seconds+" seconds";
        return val;
    }
}
*/