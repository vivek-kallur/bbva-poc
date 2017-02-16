package com.bbvacompass.core.net;

import android.os.Parcelable;

import com.bbvacompass.model.Model;

/**
 * Created by vivek.kallur on 2/15/17.
 */

public abstract class Response {

    private String mResponseString;

    private int mResponseCode;

    private String mErrorMessage;

    protected Model mModel;

    public Response() {

    }

    public Response(String responseString) {
        mResponseString = responseString;
        parseResponse(responseString);
    }

    protected abstract void parseResponse(String responseString);

    public String getResponseString() {
        return mResponseString;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public void setResponseCode(int responseCode) {
        mResponseCode = responseCode;
    }

    public int getResponseCode() {
        return mResponseCode;
    }

    public Model getModel() {
        return mModel;
    }
}
