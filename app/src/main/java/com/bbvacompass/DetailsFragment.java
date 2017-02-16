package com.bbvacompass;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bbvacompass.model.Location;
import com.bbvacompass.model.OpeningHours;
import com.bbvacompass.model.Place;

/**
 * Created by vivek.kallur on 2/16/17.
 */

public class DetailsFragment extends Fragment {

    private static final String ARG_PLACE = "place";

    private static final String ARG_CURRENT_LAT = "lat";

    private static final String ARG_CURRENT_LNG = "lng";

    private Place mPlace;

    private double mLat;

    private double mLng;

    private TextView mNameValue;

    private TextView mAddressValue;

    private TextView mOpeningHrs;

    private Button mMapButton;

    public DetailsFragment() {

    }

    public static DetailsFragment newInstance(Place place, double lat, double lng) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PLACE, place);
        args.putDouble(ARG_CURRENT_LAT, lat);
        args.putDouble(ARG_CURRENT_LNG, lng);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mPlace = getArguments().getParcelable(ARG_PLACE);
            mLat = getArguments().getDouble(ARG_CURRENT_LAT);
            mLng = getArguments().getDouble(ARG_CURRENT_LNG);
        } else if (savedInstanceState != null) {
            mPlace = savedInstanceState.getParcelable("MODEL");
            mLat = savedInstanceState.getDouble(ARG_CURRENT_LAT);
            mLng = savedInstanceState.getDouble(ARG_CURRENT_LNG);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_view, container, false);
        Context context = view.getContext();

        mNameValue = (TextView) view.findViewById(R.id.name_value);

        mAddressValue = (TextView) view.findViewById(R.id.address_value);

        mOpeningHrs = (TextView) view.findViewById(R.id.opening_hrs_value);

        mMapButton = (Button) view.findViewById(R.id.map);

        initUI();

        mMapButton.setOnClickListener(mMapButtonClickListener);
        return view;
    }

    private void initUI() {
        mNameValue.setText(mPlace != null ? mPlace.getName() : "NA");
        mAddressValue.setText(mPlace != null ? mPlace.getFormattedAddress() : "NA");
        if (mPlace != null && mPlace.getOpeningHours() != null) {
            OpeningHours openingHours = mPlace.getOpeningHours();
            // TODO read "weekday_text" if isOpen is false
            mOpeningHrs.setText(openingHours.isOpenNow() ? "Open now" : "Closed or Closing soon!!");

        }
    }

    private View.OnClickListener mMapButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mPlace != null) {
                Location location = mPlace.getGeomerty().getLocation();
                if (location != null)
                    showMap(Uri.parse(String.format("http://maps.google.com/maps?saddr=%s,%s&daddr=%s,%s", mLat, mLng, location.getLat(), location.getLng())));
            }
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("MODEL", mPlace);
        outState.putDouble(ARG_CURRENT_LAT, mLat);
        outState.putDouble(ARG_CURRENT_LNG, mLng);
        super.onSaveInstanceState(outState);
    }

    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
