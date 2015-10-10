package com.joltimate.umdshuttle.Parsers;

import android.util.Log;
import android.util.Xml;

import com.joltimate.umdshuttle.BusEntry;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Chris on 7/6/2015.
 */
public abstract class Parser {
    String error = "Error";
    public ArrayList<BusEntry> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readLines(parser);
        } finally {
            in.close();
        }
    }
    abstract ArrayList<BusEntry> readLines(XmlPullParser parser) throws XmlPullParserException, IOException;

    void parseTitleAndTag(ArrayList<BusEntry> entries, XmlPullParser parser, String title, String tag){
        String val = getAttributeValue(parser, title);
        String link = getAttributeValue(parser, tag);
        if ( !val.equals(error) && !link.equals(error) ){ //TODO this is a work around
            BusEntry entry = new BusEntry(val,link);
            entries.add(entry);
        }
    }
    String getAttributeValue(XmlPullParser parser, String attribute){
        int i;
        int count = parser.getAttributeCount();
        for ( i = 0; i < count; i++ ){
            if ( attribute.equals(parser.getAttributeName(i))){
                return parser.getAttributeValue(i);
            }
        }
        return error;
    }
}
