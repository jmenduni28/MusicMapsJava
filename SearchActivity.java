/*************************************************************************************************
 * File:   SearchActivity.java
 * Author: Joe Menduni
 *
 * Created on December 5, 2016
 * Last Modified on December 14, 2016
 *
 * Purpose: This activity creates and inflates the Search Activity, which allows a user to
 *          enter in search queries and get results, either by text or plotted on a map.
 ************************************************************************************************/
package com.joemenduni.musicmaps;

import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchActivity extends AppCompatActivity {

    /** form fields **/
    EditText searchTitle;
    EditText searchZipCode;
    SeekBar searchRange;
    TextView currentRange;

    /** default values for latitude and longitude **/
    Double latitude;
    Double longitude;

    /** google api client **/
    GoogleApiClient mGoogleApiClient;

    /** database object **/
    DBHelper database;

    /*************************************************************************************************
     * Description: This function creates and inflates the Search activity and layout.
     *
     * Inputs:
     *    @param savedInstanceState - previously saved state of application and data
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // sets pointers to views
        setViewPointers();

        // sets listener for seekbar
        searchRange.setOnSeekBarChangeListener(seekbar_listener);

        // builds google api client for getting location
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                    .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                    .addApi(LocationServices.API)
                    .build();
        }

        // initializes database object
        database = new DBHelper(this);
    }

    /*************************************************************************************************
     * Description: This function runs when the activity is started and connects the google api client.
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    protected void onStart() {
        // connects google api client
        mGoogleApiClient.connect();
        super.onStart();
    }

    /*************************************************************************************************
     * Description: This function runs when the activity is stopped and disconnects the google api client.
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    protected void onStop() {
        // disconnects google api client
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    /*************************************************************************************************
     * Description: This function exits this activity and goes back to the Main Activity.
     *
     * Inputs: pressed to run the function
     *    @param view - the button that is clicked to run the function
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public void goBack(View view) {
        finish();
    }

    /*************************************************************************************************
     * Description: This function sets pointers to the views in the display.
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public void setViewPointers() {
        searchTitle = (EditText) findViewById(R.id.searchTitle);
        searchZipCode = (EditText) findViewById(R.id.searchZip);
        searchRange = (SeekBar) findViewById(R.id.searchSeek);
        currentRange = (TextView) findViewById(R.id.currentRange);
    }

    /*************************************************************************************************
     * Description: This function clears all the form fields, including resetting the dropdown list.
     *
     * Inputs:
     *    @param view - the button that is pressed to run the function
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public void clearFields(View view) {
        searchTitle.setText("");
        searchZipCode.setText("");
        searchRange.setProgress(50);
        currentRange.setText("50 miles");
    }

    /*************************************************************************************************
     * Description: This listener listens for a change in hte seekbar and changes the
     *              milerange in the search
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    SeekBar.OnSeekBarChangeListener seekbar_listener = new SeekBar.OnSeekBarChangeListener() {

        public void onStartTrackingTouch(SeekBar seekBar) {};

        public void onStopTrackingTouch (SeekBar seekBar) {};

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            currentRange.setText(progress + " miles");
        }
    };

    /*************************************************************************************************
     * Description: This class stores data about an event to displayed from the search, either in the
     *              textviews or on the map.
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public class Event implements Serializable {

        String name;
        String startDateTime;
        String venueName;
        double latitude;
        double longitude;

        public Event(String name, String startDateTime, String venueName, double latitude, double longitude) {
            this.name = name;
            this.startDateTime = startDateTime;
            this.venueName = venueName;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }


    /*************************************************************************************************
     * Description: This function displays the results of the search as text.
     *
     * Inputs:
     *    @param view - the button that is pressed to run the function
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public void displaySearchResults(View view) {
        // gets search results
        List<Event> eventList = getSearchResults();
        // list of string to be used in the adapter
        List<String> eventStringList = new ArrayList<String>();
        // for every event
        for (Event event: eventList) {
            // add the display string to the string list
            eventStringList.add(event.name + "\n" + event.startDateTime + " at " + event.venueName);
        }
        // if there are mathing events
        if (eventStringList.size() > 0) {
            // creates array adapter with layout and array
            final ArrayAdapter<String> mShadeAdapter =
                    new ArrayAdapter<String>(this, // The current context (this activity)
                            R.layout.item_event, // The name of the layout ID.
                            R.id.event_name, // The ID of the textview to populate.
                            eventStringList);
        }
    }

    /*************************************************************************************************
     * Description: This function displays the results of the search on a map.
     *
     * Inputs:
     *    @param view - the button that is pressed to run the function
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public void viewMap(View view) {
        // gets search results
        List<Event> eventList = getSearchResults();
        // creates intent to go to search maps activity
        Intent mapIntent = new Intent(this, SearchMapsActivity.class);
        // creates new bundle
        Bundle theBundle = new Bundle();
        // adds list of events to bundle
        theBundle.putSerializable("eventList", (Serializable) eventList);
        // adds bundle to intent
        mapIntent.putExtras(theBundle);
        // starts map activity
        startActivity(mapIntent);
    }

    /*************************************************************************************************
     * Description: This function gets the input from the search form and returns a list of the result to
     *              be displayed in another function, depending on which button the user has clicked.
     *
     * Outputs:
     *      List<Event> - list of events returned from the search
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public List<Event> getSearchResults() {
        // gets form data
        String title = searchTitle.getText().toString();
        int mileRange = searchRange.getProgress();

        // find location to use
        Double[] location = getLocationToUse();

        // gets range of latitude and longitudes
        double minLatitude = location[0] - convertMileageToDegrees(mileRange);
        double maxLatitude = location[0] + convertMileageToDegrees(mileRange);
        double minLongitude = location[1] - convertMileageToDegrees(mileRange);
        double maxLongitude = location[1] + convertMileageToDegrees(mileRange);

        // list of events that fit the search query
        List<Event> eventList = new ArrayList<Event>();

        // cursor of shows
        Cursor cursor = database.getAllShowsCursors();
        // if there is a result
        if ((cursor.moveToFirst())) {
            do {
                // name of show
                String name = cursor.getString(3);
                // latitude and longitude of venue
                double[] latlng = database.findVenueLatLngByID(cursor.getInt(0));
                // name of venue
                String venueName = database.findVenueNameByID(cursor.getInt(0));
                Double latitude = latlng[0];
                Double longitude = latlng[1];
                // if there is a name match
                if (stringComparison(title, name)) {
                    // if the location is in the selected range
                    if (latitude >= minLatitude && latitude <= maxLatitude && longitude >= minLongitude && longitude <= maxLongitude) {
                        String startDateTime = cursor.getString(7);
                        // creates new event object
                        Event newEvent = new Event(name, startDateTime, venueName, latitude, longitude);
                        // adds event to list
                        eventList.add(newEvent);

                    }
                }
            } while (cursor.moveToNext());
        }
        // closes cursor
        cursor.close();
        // returns list of events
        return eventList;
    }


    /*************************************************************************************************
     * Description: This function compares the input from the search to the name of all of the shows
     *              and tries to find a match.
     *
     * Inputs:
     *      @param userSearch - the user's input title
     *      @param showName - the name of the show to check
     *
     * Outputs:
     *      bool - true if there is a match, false if there is no match
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public boolean stringComparison(String userSearch, String showName) {
        // splits the user's input into an array of words
        String[] userSearchSplit = userSearch.split(" ");
        // splits the show name into an array of words
        String[] showNameSplit = showName.split(" ");
        // for all words in the user's input
        for (String word: userSearchSplit) {
            // if the show name contains any of the user's input
            if (showName.contains(word)) {
                // there is a match - return true
                return true;
            }
        }
        // for all words in the show name
        for (String word: showNameSplit) {
            // if the user's search contains of the show name
            if (userSearch.contains(word)) {
                // there is a match - return true
                return true;
            }
        }
        // no match - return false
        return false;
    }

    /*************************************************************************************************
     * Description: This function converts a range in miles to an amount of degrees to use
     *              for latitude and longitude searches.
     *
     * Inputs:
     *      @param amountMiles - the amount of miles to convert
     *
     * Outputs:
     *      double - amount of degrees
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public double convertMileageToDegrees(double amountMiles) {
        // amount of kilometers
        Double amountKM = amountMiles * 0.621371;
        // amount of degrees
        Double amountDeg = ( 1 / 110.54 ) * amountKM;
        return amountDeg;
    }

    /*************************************************************************************************
     * Description: This is the class that calls the Geocoder, which converts an street address
     *              to a latitude and longitude.
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    private class GeocoderHandler extends Handler {
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    latitude = bundle.getDouble("latitude");
                    longitude = bundle.getDouble("longitude");
                    break;
                default:
            }
        }
    }

    /*************************************************************************************************
     * Description: This function determines which location to use in the search,
     *              either the user inputted zip code or the user's actual location.
     *
     * Outputs:
     *      double[] - index 0 is latitude, index 1 is longitude
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public Double[] getLocationToUse() {
        // array to be returned
        Double[] returnDouble = new Double[2];
        // zip code from form
        String zipCodeString = searchZipCode.getText().toString();
        // regex to check against (from string resource)
        String zipRegex = getString(R.string.zipCodeRegex);
        // checks regex
        Pattern pattern = Pattern.compile(zipRegex);
        Matcher matcher = pattern.matcher(zipCodeString);

        int zipCode = -1;
        // if the zip code matches the regex
        if (matcher.matches()) {
            zipCode = Integer.valueOf(zipCodeString);
        }
        // if there is a zip code
        if (zipCode != -1) {
            // gets lat and long from zip
            GeocodingLocation locationAddress = new GeocodingLocation();
            locationAddress.getAddressFromLocation(String.valueOf(zipCode),
                    getApplicationContext(), new GeocoderHandler());
            // returns list
            returnDouble = new Double[]{latitude, longitude};
        }
        else {
            try {
                // gets last known location
                Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLastLocation != null) {
                    // sets lat and long to be in the array
                    returnDouble[0] = mLastLocation.getLatitude();
                    returnDouble[1] = mLastLocation.getLongitude();
                }
            }
            catch (SecurityException se) {

            }
        }
        //returns array
        return returnDouble;
    }



}
