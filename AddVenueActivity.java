/*************************************************************************************************
 * File:   AddVenueActivity.java
 * Author: Joe Menduni
 *
 * Created on December 5, 2016
 * Last Modified on December 14, 2016
 *
 * Purpose: This activity creates and inflates the Add Venue Activity, which contains a
 *              a form for the user of the app to add a show to the database.
 *************************************************************************************************/

package com.joemenduni.musicmaps;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class AddVenueActivity extends AppCompatActivity {

    /** form fields **/
    private EditText venueName;
    private EditText website;
    private EditText pictureURL;
    private EditText streetAddress;
    private EditText town;
    private EditText state;
    private EditText zipCode;

    /** database object **/
    DBHelper database;

    /** default values for latitude and longitude **/
    double latitude = -1;
    double longitude = -1;

    /*************************************************************************************************
     * Description: This function creates and inflates the Add Venue activity and layout.
     *
     * Inputs:
     *    @param savedInstanceState - previously saved state of application and data
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_venue);

        // sets pointers to views
        setViewPointers();

        // initializes database object
        database = new DBHelper(this);
    }

    /*************************************************************************************************
     * Description: This function sets pointers to the views in the display.
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public void setViewPointers() {
        venueName = (EditText) findViewById(R.id.venueName);
        website = (EditText) findViewById(R.id.venueWebsite);
        pictureURL = (EditText) findViewById(R.id.venuePictureURL);
        streetAddress = (EditText) findViewById(R.id.venueStreetAddress);
        town = (EditText) findViewById(R.id.venueTown);
        state = (EditText) findViewById(R.id.venueState);
        zipCode = (EditText) findViewById(R.id.venueZipCode);
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
        venueName.setText("");
        website.setText("");
        pictureURL.setText("");
        streetAddress.setText("");
        town.setText("");
        state.setText("");
        zipCode.setText("");
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
     * Description: This function adds a venue to the database.
     *
     * Inputs: pressed to run the function
     *    @param view - the button that is clicked to run the function
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public void addVenue(View view) {
        // gets data from form fields
        String theName = venueName.getText().toString();
        String theWebsite = website.getText().toString();
        String thePictureURL = pictureURL.getText().toString();
        String theStreetAddress = streetAddress.getText().toString();
        String theTown = town.getText().toString();
        String theState = state.getText().toString();
        String theZipCode = zipCode.getText().toString();

        // creates string of the total address
        String fullAddress = theStreetAddress + ", " + theTown + ", " + theState + " " + theZipCode;

        // gets the location object
        GeocodingLocation locationAddress = new GeocodingLocation();

        // sends the request to get the latitude and longitude
        locationAddress.getAddressFromLocation(fullAddress,
                getApplicationContext(), new GeocoderHandler());

        // adds venue to the database
        database.addVenue(theName, theWebsite, thePictureURL, theStreetAddress, theTown, theState, theZipCode, latitude, longitude);

        // goes back to the main menu
        finish();
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


}
