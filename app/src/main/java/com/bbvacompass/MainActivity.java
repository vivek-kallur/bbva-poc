package com.bbvacompass;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.bbvacompass.core.net.Command;
import com.bbvacompass.core.net.Controller;
import com.bbvacompass.model.Geometry;
import com.bbvacompass.model.Place;
import com.bbvacompass.model.TextSearchModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MainActivity";

    private static final String LIST_VIEW = "List View";

    private static final String MAP_VIEW = "Map View";

    private static final int REQUEST_CODE_FINE_LOCATION = 1;

    private GoogleMap mMap;

    private GoogleApiClient mGoogleApiClient;

    private Location mLocation;

    private View mContainer;

    private TextSearchModel mTextSearchModel;

    // use this to spoof the location to 32.8205865, -96.8714244
    private static final boolean SPOOF = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        mapFragment.setHasOptionsMenu(true);

        if (savedInstanceState != null)
            mTextSearchModel = savedInstanceState.getParcelable("MODEL");

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(mConnectionsCallbacks)
                    .addOnConnectionFailedListener(mConnectionFailedListener)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register the receiver here to listen to the response form http request
        IntentFilter intentFilter = new IntentFilter(Controller.RECEIVER_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("MODEL", mTextSearchModel);
        super.onSaveInstanceState(outState);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO dismiss progress dialog
            if (intent != null && Controller.RECEIVER_ACTION.equals(intent.getAction())) {
                mTextSearchModel = intent.getParcelableExtra("RESPONSE");


                if (mTextSearchModel != null) {
                    // Sort and set the list back to model
                    List<Place> placeArrayList = mTextSearchModel.getPlaceList();
                    Collections.sort(placeArrayList, new PlaceComparator(mLocation.getLatitude(), mLocation.getLongitude()));
                    mTextSearchModel.setPlaceList(placeArrayList);
                    plotMarkers(mTextSearchModel);
                }
            }
        }
    };

    // plot the markers
    private void plotMarkers(TextSearchModel textSearchModel) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        boolean canMoveCamera = false;
        for (Place place : textSearchModel.getPlaceList()) {
            Geometry geometry = place.getGeomerty();
            com.bbvacompass.model.Location location = geometry.getLocation();
            if (location != null) {
                debug("Lat: " + location.getLat() + ", Lng: " + location.getLng());
                LatLng latLng = new LatLng(location.getLat(), location.getLng());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black));
                markerOptions.title(place.getName() != null ? place.getName() : "");
                markerOptions.snippet(place.getFormattedAddress() != null ? place.getFormattedAddress() : "");
                Marker marker = mMap.addMarker(markerOptions);
                //marker.setTag(location);
                marker.setTag(place);
                marker.setVisible(true);
                builder.include(latLng);
                canMoveCamera = true;
            }
        }
        // info window click listener
        mMap.setOnInfoWindowClickListener(mOnInfoWindowClick);
        try {
            // in case if we missed including latlng to builder it will crash
            if (canMoveCamera)
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
        } catch (IllegalStateException e) {

        }
    }


    private GoogleMap.OnInfoWindowClickListener mOnInfoWindowClick = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            /*com.bbvacompass.model.Location location = (com.bbvacompass.model.Location) marker.getTag();
            if (location != null) {
                showMap(Uri.parse(String.format("http://maps.google.com/maps?saddr=%s,%s&daddr=%s,%s", mLocation.getLatitude(), mLocation.getLongitude(), location.getLat(), location.getLng())));
            }*/
            Place place = (Place) marker.getTag();
            showDetailsFragment(place);
        }
    };

    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        // stop listening to the network response
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        super.onStop();
    }

    private GoogleApiClient.OnConnectionFailedListener mConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            // TODO
        }
    };


    private GoogleApiClient.ConnectionCallbacks mConnectionsCallbacks = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(@Nullable Bundle bundle) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLocation != null) {
                    // for testing spoof location
                    if (SPOOF)
                        spoofDallasLocation();
                    updateLocation(mLocation.getLatitude(), mLocation.getLongitude());
                }
            }
        }

        @Override
        public void onConnectionSuspended(int i) {

        }
    };

    // TODO to test
    private void spoofDallasLocation() {
        mLocation.setLatitude(32.8205865);
        mLocation.setLongitude(-96.8714244);
    }

    private void updateLocation(double lat, double lng) {
        Bundle params = new Bundle();
        params.putDouble("LAT", lat);
        params.putDouble("LNG", lng);
        params.putString("KEY", getString(R.string.google_maps_key));
        // start async task only if search model is null
        if (mTextSearchModel == null)
            Command.SEARCH_TEXT.execute(Command.SEARCH_TEXT, this, params, true);
        else
            plotMarkers(mTextSearchModel);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
    }


    private void updateUI(boolean isRequestGranted) {
        if (isRequestGranted) {
            if (mLocation != null) {
                updateLocation(mLocation.getLatitude(), mLocation.getLongitude());
            }
        } else {
            // show error message
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     *
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        initMap();

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            updateUI(true);
        } else {
            // Show rationale and request permission.
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_FINE_LOCATION);
        }
    }

    // init map with controls
    private void initMap() {
        if (mMap != null) {
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setAllGesturesEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);
        }
    }

    /**
     * callback after user made the selection for permission handling
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        debug("onRequestPermissionsResult " + requestCode + " " + permissions + " " + grantResults);
        if (requestCode == REQUEST_CODE_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    mMap.setMyLocationEnabled(true);
                } catch (SecurityException e) {

                }
                updateUI(true);
            } else {
                updateUI(false);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.map_toggle, menu);
        return true;
    }


    /**
     * TODO handle the use case when back pressed is used from the list fragment
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (LIST_VIEW.equals(item.getTitle())) {
            // show list view
            showListViewFragment();
            item.setTitle(MAP_VIEW);
        } else {
            // show map view
            showMap();
            item.setTitle(LIST_VIEW);
        }
        return true;
    }

    private void showMap() {
        android.app.FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.container);
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        }
    }

    private void showListViewFragment() {
        android.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        Fragment fragment = PlaceFragment.newInstance(mTextSearchModel);
        fragmentTransaction.add(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void showDetailsFragment(Place place) {
        android.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        Fragment fragment = DetailsFragment.newInstance(place, mLocation.getLatitude(), mLocation.getLongitude());
        fragmentTransaction.add(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void debug(String message) {
        Log.d(TAG, message);
    }
}
