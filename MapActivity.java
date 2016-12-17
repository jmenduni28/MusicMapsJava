/*************************************************************************************************
 * File:   SearchMapsActivity.java
 * Author: Joe Menduni
 *
 * Created on December 5, 2016
 * Last Modified on December 14, 2016
 *
 * Purpose: This activity creates and inflates the Maps Activity, which has a map
 *          that displays markers for all the venues and shows, based on what the user wants.
 ************************************************************************************************/
package com.joemenduni.musicmaps;

import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    /** google map object **/
    private GoogleMap mMap;

    /** database object **/
    DBHelper database;

    /*************************************************************************************************
     * Description: This function creates and inflates the Search Maps activity and layout.
     *
     * Inputs:
     *    @param savedInstanceState - previously saved state of application and data
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // initializes database object
        database = new DBHelper(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // google map object
        mMap = googleMap;

        // cursor of all venues
        Cursor cursor = database.getAllVenuesCursors();
        LatLng latLng = null;

        // if there is data in the cursor
        if ((cursor.moveToFirst())) {
            do {
                // venue name
                String name = cursor.getString(6);
                // venue latitude
                Double latitude = cursor.getDouble(5);
                // venue longitude
                Double longitude = cursor.getDouble(2);
                // creates location object
                latLng = new LatLng(latitude, longitude);
                // adds marker for venue
                mMap.addMarker(new MarkerOptions().position(latLng).title(name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            // while there is another object in the cursor
            } while (cursor.moveToNext());
        }
        // closes cursor
        cursor.close();
        // cursor of all shows
        cursor = database.getAllShowsCursors();
        latLng = null;

        // if there is data in the cursor
        if ((cursor.moveToFirst())) {
            do {
                // show name
                String name = cursor.getString(3);
                // show latitude & longitude
                double[] latlng = database.findVenueLatLngByID(cursor.getInt(0));
                // puts latitude a little away from real so that we can see both pins
                Double latitude = latlng[0] + .00001;
                // puts longitude a little away from real so that we can see both pins
                Double longitude = latlng[1] + .00001;
                // creates location object
                latLng = new LatLng(latitude, longitude);
                // adds marker for show
                mMap.addMarker(new MarkerOptions().position(latLng).title(name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                // while there is another object in the cursor
            } while (cursor.moveToNext());
        }
        // closes cursor
        cursor.close();
        // moves camera to last read location
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }


}
