package com.bbvacompass;

import com.bbvacompass.model.Location;
import com.bbvacompass.model.Place;

import java.util.Comparator;

/**
 * Created by vivek.kallur on 2/16/17.
 */

public class PlaceComparator implements Comparator<Place> {

    private double mLat;
    private double mLng;

    public PlaceComparator(double lat, double lng) {
        mLat = lat;
        mLng = lng;
    }

    @Override
    public int compare(Place place1, Place place2) {
        Location location1 = place1.getGeomerty() != null ? place1.getGeomerty().getLocation() : null;
        Location location2 = place2.getGeomerty() != null ? place2.getGeomerty().getLocation() : null;

        if (location1 != null && location2 != null) {
            double distance1 = distance(mLat, mLng, location1.getLat(), location1.getLng());
            double distance2 = distance(mLat, mLng, location2.getLat(), location2.getLng());
            return (int) (distance1 - distance2);
        }
        return 0;
    }

    public double distance(double fromLat, double fromLon, double toLat, double toLon) {
        double radius = 6378137;   // approximate Earth radius, *in meters*
        double deltaLat = toLat - fromLat;
        double deltaLon = toLon - fromLon;
        double angle = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(deltaLat / 2), 2) +
                Math.cos(fromLat) * Math.cos(toLat) *
                        Math.pow(Math.sin(deltaLon / 2), 2)));
        return radius * angle;
    }
}
