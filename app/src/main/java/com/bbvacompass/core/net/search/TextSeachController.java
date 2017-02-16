package com.bbvacompass.core.net.search;

import android.app.Activity;
import android.os.Bundle;

import com.bbvacompass.Bbva;
import com.bbvacompass.Util;
import com.bbvacompass.core.net.Controller;
import com.bbvacompass.core.net.Server;

/**
 * Created by vivek.kallur on 2/15/17.
 */

public class TextSeachController extends Controller {

    private static final String BASE_URL = "https://maps.googleapis.com/maps/";
    @Override
    protected void execute(Activity context, Bundle params) throws Exception {
        double lat = params.getDouble("LAT");
        double lng = params.getDouble("LNG");
        String key = params.getString("KEY");

        TextSearchResponse textSearchResponse = null;
        // read from local file if set
        if (Bbva.LOAD_LOCAL_FILE) {
            textSearchResponse = new TextSearchResponse(Util.getFileFromSrcMainAssets("search_results.json"));
        } else {
            TextSearchRequest textSearchRequest = new TextSearchRequest(lat, lng, key);
            Server server = new Server(BASE_URL);
            textSearchResponse = (TextSearchResponse) server.sendGetRequest(textSearchRequest);
        }
        setResponse(textSearchResponse);
    }
}
