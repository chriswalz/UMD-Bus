package com.joltimate.umdshuttle.ScreenManagers;

import com.joltimate.umdshuttle.Adapters.RecyclerFavAdapter;
import com.joltimate.umdshuttle.BusEntry;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Chris on 7/12/2015.
 */
public class FAV {
    public static ArrayList<BusEntry> currentFavList;
    public static RecyclerFavAdapter favAdapter;

    public static ArrayList<BusEntry> getFavoritedItemsInList() {
        int i, j, k;
        BusEntry entry;
        ArrayList<BusEntry> favorites = new ArrayList<>();
        ArrayList<BusEntry> temp;
        for (i = 0; i < RO.stops.size(); i++) {
            for (j = 0; j < RO.stops.get(i).size(); j++) {
                temp = RO.stops.get(i).get(j);
                if (temp != null) {
                    for (k = 0; k < temp.size(); k++) {
                        entry = temp.get(k);
                        if (entry.isFavorited()) {
                            entry.setRouteTag(RO.routes.get(i).getLink()); // need link for getting times, info for proper output :/
                            entry.setDirTag(RO.directions.get(i).get(j).getLink());
                            String rTag = RO.routes.get(i).getInfo();
                            String dTag = RO.directions.get(i).get(j).getInfo();
                            if (dTag == null) {
                                dTag = "No Direction";
                            }
                            if (rTag == null) {
                                rTag = "No Route";
                            }
                            //entry.setFavRouteDirInfo(rTag);//setRouteTag(rTag);
                            //entry.setExtra(dTag); //setDirTag(dTag);
                            entry.routePosition = i;
                            entry.directionPosition = j;
                            entry.stopPosition = k;
                            entry.setFavRouteDirInfo(rTag + "/" + dTag);
                            entry.setSpecialCompare(entry.getInfo() + entry.getRouteTag());
                            favorites.add(entry);
                        }
                    }
                }
            }
        }
        BusEntry.state = BusEntry.INFOROUTE;
        Collections.sort(favorites);
        BusEntry.state = BusEntry.REGULAR;
        if (favorites.size() == 0) {
            favorites.add(new BusEntry("No Favorites", "No favorites"));
        }
        return favorites;
    }

}
