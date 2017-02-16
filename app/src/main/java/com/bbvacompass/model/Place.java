package com.bbvacompass.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vivek.kallur on 2/15/17.
 */

public class Place implements Parcelable {
    private String mFormattedAddress;
    private Geometry mGeomerty;
    private String mName;

    private OpeningHours mOpeningHours;

    public Place() {
    }

    public Place(Parcel parcel) {
        mFormattedAddress = parcel.readString();
        mGeomerty = parcel.readParcelable(Geometry.class.getClassLoader());
        mName = parcel.readString();
        mOpeningHours = parcel.readParcelable(OpeningHours.class.getClassLoader());
    }

    public String getFormattedAddress() {
        return mFormattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        mFormattedAddress = formattedAddress;
    }

    public Geometry getGeomerty() {
        return mGeomerty;
    }

    public void setGeomerty(Geometry geomerty) {
        mGeomerty = geomerty;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        mOpeningHours = openingHours;
    }

    public OpeningHours getOpeningHours() {
        return mOpeningHours;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mFormattedAddress);
        parcel.writeParcelable(mGeomerty, 0);
        parcel.writeString(mName);
        parcel.writeParcelable(mOpeningHours, 0);
    }

    public static Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel parcel) {
            return new Place(parcel);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };
}
