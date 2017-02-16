package com.bbvacompass.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vivek.kallur on 2/15/17.
 */

public class Geometry implements Parcelable {
    private Location mLocation;

    public Geometry() {
    }

    public Geometry(Parcel parcel) {
        mLocation = parcel.readParcelable(Location.class.getClassLoader());
    }

    public void setLocation(Location location) {
        mLocation = location;
    }

    public Location getLocation() {
        return mLocation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(mLocation, 0);
    }

    private static Creator<Geometry> CREATOR = new Creator<Geometry>() {
        @Override
        public Geometry createFromParcel(Parcel parcel) {
            return new Geometry(parcel);
        }

        @Override
        public Geometry[] newArray(int size) {
            return new Geometry[size];
        }
    };
}
