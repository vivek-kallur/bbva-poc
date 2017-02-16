package com.bbvacompass.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vivek.kallur on 2/15/17.
 */

public class Location implements Parcelable {
    private double mLat;
    private double mLng;

    public Location() {
    }

    public Location(Parcel parcel) {
        mLat = parcel.readDouble();
        mLng = parcel.readDouble();
    }

    public double getLat() {
        return mLat;
    }

    public void setLat(double lat) {
        mLat = lat;
    }

    public double getLng() {
        return mLng;
    }

    public void setLng(double lng) {
        mLng = lng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(mLat);
        parcel.writeDouble(mLng);
    }

    private static Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel parcel) {
            return new Location(parcel);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}
