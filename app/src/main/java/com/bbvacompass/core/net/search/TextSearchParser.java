package com.bbvacompass.core.net.search;

import com.bbvacompass.model.Geometry;
import com.bbvacompass.model.Location;
import com.bbvacompass.model.Model;
import com.bbvacompass.model.OpeningHours;
import com.bbvacompass.model.Place;
import com.bbvacompass.model.TextSearchModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by vivek.kallur on 2/15/17.
 */

public class TextSearchParser {
    public static Model parseTextSearchResponse(String response) {
        TextSearchModel textSearchModel = new TextSearchModel();
        try {
            JSONObject jsonObject = new JSONObject(response);

            textSearchModel.setNextPageToken(jsonObject.optString("next_page_token"));

            JSONArray jsonArray = jsonObject.optJSONArray("results");
            textSearchModel.setPlaceList(parsePlaceResults(jsonArray));

            textSearchModel.setStatus(jsonObject.optString("status"));
        } catch (JSONException exception) {

        }

        return textSearchModel;
    }

    private static ArrayList<Place> parsePlaceResults(JSONArray jsonArray) throws JSONException {
        ArrayList<Place> places = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            Place place = new Place();
            place.setFormattedAddress(jsonArray.getJSONObject(i).optString("formatted_address"));

            JSONObject geoObject = jsonArray.getJSONObject(i).optJSONObject("geometry");
            place.setGeomerty(parseGrometryObject(geoObject));
            place.setName(jsonArray.getJSONObject(i).getString("name"));

            JSONObject openingHrs = jsonArray.getJSONObject(i).optJSONObject("opening_hours");
            place.setOpeningHours(parseOpeningHours(openingHrs));

            places.add(place);
        }
        return places;
    }

    private static OpeningHours parseOpeningHours(JSONObject jsonObject) throws JSONException {
        OpeningHours openingHours = new OpeningHours();
        if (jsonObject != null)
            openingHours.setIsOpenNow(jsonObject.optBoolean("open_now"));
        return openingHours;
    }

    private static Geometry parseGrometryObject(JSONObject jsonObject) throws JSONException {
        Geometry geometry = new Geometry();

        JSONObject locationJson = jsonObject.getJSONObject("location");
        Location location = new Location();
        location.setLat(locationJson.getDouble("lat"));
        location.setLng(locationJson.getDouble("lng"));

        geometry.setLocation(location);
        return geometry;
    }
}
