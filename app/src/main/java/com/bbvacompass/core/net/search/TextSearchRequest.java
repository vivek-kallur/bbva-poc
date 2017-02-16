package com.bbvacompass.core.net.search;

import com.bbvacompass.core.net.Request;
import com.bbvacompass.core.net.Response;

import java.io.IOException;

/**
 * Created by vivek.kallur on 2/15/17.
 */

public class TextSearchRequest extends Request {

//    private static String URL = "api/place/textsearch/json?query=BBVA+Compass&location=%1s,%2s&radius=10000&key=%3s";
private static String URL = "api/place/textsearch/json?query=BBVA+Compass+dallas&location=%1s,%2s&radius=10000&key=%3s";

    private double mLat;
    private double mLng;
    private String mKey;

    public TextSearchRequest(double lat, double lng, String key) {
        super(URL, CONTENT_TYPE.JSON);
        mLat = lat;
        mLng = lng;
        mKey = key;
    }

    @Override
    public Response getResponse(String responseString) {
        return new TextSearchResponse(responseString);
    }

    @Override
    protected String build() throws IOException {
        return String.format(URL, mLat, mLat, mKey);
//        return String.format(URL_DALLAS, "32.7830600", "-96.8066700", mKey);

    }
}
