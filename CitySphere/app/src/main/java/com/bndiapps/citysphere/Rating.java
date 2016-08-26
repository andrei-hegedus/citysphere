package com.bndiapps.citysphere;

import android.provider.BaseColumns;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class Rating implements ClusterItem, BaseColumns {

    private double latitude;
    private double longitude;
    private int rating;

    public Rating(double latitude, double longitude, int rating) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getRating() {
        return rating;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(latitude, longitude);
    }
}
