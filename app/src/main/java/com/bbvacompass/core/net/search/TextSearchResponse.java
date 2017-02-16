package com.bbvacompass.core.net.search;

import android.os.Parcel;

import com.bbvacompass.core.net.Response;
import com.bbvacompass.model.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vivek.kallur on 2/15/17.
 */

public class TextSearchResponse extends Response {

    public TextSearchResponse(String responseString) {
        super(responseString);
    }

    @Override
    protected void parseResponse(String responseString) {
        mModel = TextSearchParser.parseTextSearchResponse(responseString);
    }
}
