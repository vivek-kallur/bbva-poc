package com.bbvacompass.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vivek.kallur on 2/16/17.
 */

public class OpeningHours implements Parcelable {

    private boolean mIsOpenNow;

    public OpeningHours() {}

    public OpeningHours(Parcel parcel) {
        mIsOpenNow = parcel.readInt() == 1 ? true : false;
    }

    public void setIsOpenNow(boolean mIsOpenNow) {
        this.mIsOpenNow = mIsOpenNow;
    }

    public boolean isOpenNow() {
        return mIsOpenNow;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mIsOpenNow ? 1 : 0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private static Creator<OpeningHours> CREATOR = new Creator<OpeningHours>() {
        @Override
        public OpeningHours createFromParcel(Parcel parcel) {
            return new OpeningHours(parcel);
        }

        @Override
        public OpeningHours[] newArray(int size) {
            return new OpeningHours[size];
        }
    };
}
