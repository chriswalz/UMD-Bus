package com.joltimate.umdshuttle.Parsers;

import com.joltimate.umdshuttle.BusEntry;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Chris on 6/10/2015.
 */
public class ParseRoutes extends Parser{
    // search terms
    private final String notAvailable = "Routes unavailable";
    private final String route = "route";
    // attributes
    private final String title = "title";
    private final String tag = "tag";

    @Override
    public ArrayList<BusEntry> readLines(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<BusEntry> entries = new ArrayList<BusEntry>();
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            String name = parser.getName();
            if ( name != null && parser.getEventType() == XmlPullParser.START_TAG && name.equals(route)){
                parseTitleAndTag(entries, parser, title, tag);
            }
        }
        if ( entries.size() == 0 ){
            entries.add(new BusEntry(notAvailable, notAvailable));
        }
        return entries;
    }
}