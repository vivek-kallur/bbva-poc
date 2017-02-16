package com.bbvacompass.model;

import android.os.Parcel;

import com.bbvacompass.core.net.search.TextSearchResponse;

import java.util.List;

/**
 * Created by vivek.kallur on 2/15/17.
 */

public class TextSearchModel implements Model {
    private String mNextPageToken;
    private String mStatus;
    private List<Place> mPlaceList;

    public TextSearchModel() {

    }

    public String getNextPageToken() {
        return mNextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        mNextPageToken = nextPageToken;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public List<Place> getPlaceList() {
        return mPlaceList;
    }

    public void setPlaceList(List<Place> placeList) {
        mPlaceList = placeList;
    }

    public TextSearchModel(Parcel parcel) {
        mNextPageToken = parcel.readString();
        mStatus = parcel.readString();
        mPlaceList = parcel.readArrayList(Place.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mNextPageToken);
        parcel.writeString(mStatus);
        parcel.writeList(mPlaceList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TextSearchModel> CREATOR = new Creator<TextSearchModel>() {
        @Override
        public TextSearchModel createFromParcel(Parcel parcel) {
            return new TextSearchModel(parcel);
        }

        @Override
        public TextSearchModel[] newArray(int size) {
            return new TextSearchModel[size];
        }
    };
}
