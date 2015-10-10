package com.joltimate.umdshuttle.Parsers;

/**
 * Created by Chris on 7/8/2015.
 */

import com.joltimate.umdshuttle.BusEntry;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
public class ParseDirections extends Parser {
    private final String notAvailable = "Directions unavailable";
    // search terms
    private final String agency = "direction";
    // attributes
    private final String title = "title";
    private final String tag = "tag";

    @Override
    public ArrayList<BusEntry> readLines(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<BusEntry> entries = new ArrayList<BusEntry>();
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            String name = parser.getName();
            if ( name != null && parser.getEventType() == XmlPullParser.START_TAG && name.equals(agency)){
                parseTitleAndTag(entries, parser, title, tag);
            }
        }
        if ( entries.size() == 0 ){
            entries.add(new BusEntry(notAvailable, notAvailable));
        }
        return entries;
    }
}

