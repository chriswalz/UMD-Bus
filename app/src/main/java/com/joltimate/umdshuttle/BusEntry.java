package com.joltimate.umdshuttle;

import android.location.Location;
import android.util.Log;
import android.widget.ImageView;

import com.joltimate.umdshuttle.Data.DataStorage;
import com.joltimate.umdshuttle.ScreenManagers.FAV;

/**
 * Created by Chris on 7/4/2015.
 */
public class BusEntry implements Comparable<BusEntry> {
    public final static int DISTANCE = 3;
    public final static int NUMBERS = 2;
    public final static int INFOROUTE = 1;
    public final static int REGULAR = 0;
    public static int state = 0;
    public Location location; //decided to make data public so i dont have to make getters and setters
    public Double distance = 100000.0;
    public int routePosition = -1;
    public int directionPosition = -1;
    public int stopPosition = -1;
    private String info;
    private String link;
    private boolean isFavorited;
    private String extra; // for favorites ??
    private String routeTag;
    private String dirTag;
    private String favRouteDirInfo;
    private String specialCompare;

    public BusEntry(String info, String link){
        this.info = info;
        this.link = link;
        this.isFavorited = false;
        this.distance = 100000.0; //arbitrarily high distance
    }
    public String getFavRouteDirInfo() {
        return favRouteDirInfo;
    }
    public void setFavRouteDirInfo(String favRouteDirInfo) {
        this.favRouteDirInfo = favRouteDirInfo;
    }
    public String getSpecialCompare() {
        return specialCompare;
    }
    public void setSpecialCompare(String specialCompare) {
        this.specialCompare = specialCompare;
    }
    public String getDirTag() {
        return dirTag;
    }
    public void setDirTag(String dirTag) {
        this.dirTag = dirTag;
    }
    public String getRouteTag() {
        return routeTag;
    }
    public void setRouteTag(String routeTag) {
        this.routeTag = routeTag;
    }
    public boolean isFavorited() {
        return isFavorited;
    }
    public void setIsFavorited(boolean isFavorited){
        this.isFavorited = isFavorited;
    }
    public void updateView(ImageView imageView){
        if (isFavorited) {
            imageView.setImageResource(R.drawable.star);
        } else {
            imageView.setImageResource(R.drawable.outlined_star);
        }
    }
    public void toggleFavorited(ImageView imageView) {
        if (isFavorited) {
            isFavorited = false;
            if ( imageView != null){
                imageView.setImageResource(R.drawable.outlined_star);
            }
            DataStorage.saveFavorites(FAV.getFavoritedItemsInList());
        } else {
            isFavorited = true;
            if ( imageView != null ){
                imageView.setImageResource(R.drawable.star);
            }
            DataStorage.saveFavorites(FAV.getFavoritedItemsInList());
        }
    }
    public String getLink(){
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setInfoLink(String info, String link){
        this.info = info;
        this.link = link;
    }
    public String getInfo(){
        return info;
    }

    public String getExtra() {
        return extra;
    }
    public void setExtra(String extra){
        this.extra = extra;
    }

    @Override
    public int compareTo(BusEntry another) {
        // TODO only works with strings that are numbers fix that
        // TODO fix npe from this.getLink()
        if ( another == null || this.getLink() == null || another.getLink() == null ){
            Log.d("BusEntry", "Null when comparing");
            return  0; // ????
        } else {
            if ( state == NUMBERS){
                int num1 = Integer.parseInt(this.getLink());
                int num2 = Integer.parseInt(another.getLink());
                return num1 - num2;
            }
            else if (state == DISTANCE){
                return  (int)( distance - another.distance); // smaller
            }
            else if (state == INFOROUTE ) {
                String th = this.getSpecialCompare();
                String an = another.getSpecialCompare();
                return th.compareTo(an);
            }
            else { //state == REGULAR
                return this.getInfo().compareTo(another.getInfo());
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        return info.equals(((BusEntry)o).info);
    }

    public String toString(){
        return info;
    }
}
