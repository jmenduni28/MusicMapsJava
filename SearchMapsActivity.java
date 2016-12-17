/*************************************************************************************************
 * File:   SearchMapsActivity.java
 * Author: Joe Menduni
 *
 * Created on December 5, 2016
 * Last Modified on December 14, 2016
 *
 * Purpose: This activity creates and inflates the Search Maps Activity, which has a map
 *          that displays markers for all the events that the user has searched for.
 ************************************************************************************************/

package com.joemenduni.musicmaps;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class SearchMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    /** google map object **/
    private GoogleMap mMap;

    /** list of activity objects **/
    public List<SearchActivity.Event> eventList;

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
        setContentView(R.layout.activity_search_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // gets event list from intent
        eventList = (List<SearchActivity.Event>) getIntent().getSerializableExtra("eventList");
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

        LatLng latLng = null;

        // for all events in the event list
        for (SearchActivity.Event event: eventList) {
            // gets location from event object
            latLng = new LatLng(event.latitude, event.longitude);
            // adds marker for event
            mMap.addMarker(new MarkerOptions().position(latLng).title(event.name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }

        // moves camera to last read location
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
}
