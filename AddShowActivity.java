/*************************************************************************************************
 * File:   AddShowActivity.java
 * Author: Joe Menduni
 *
 * Created on December 5, 2016
 * Last Modified on December 14, 2016
 *
 * Purpose: This activity creates and inflates the Add Show Activity, which contains a
 *              a form for the user of the app to add a show to the database.
 *************************************************************************************************/

package com.joemenduni.musicmaps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class AddShowActivity extends AppCompatActivity {

    /** form fields **/
    private EditText showName;
    private Spinner venue;
    private EditText website;
    private EditText pictureURL;
    private EditText artistsPerforming;
    private EditText startDateTime;
    private EditText endDateTime;

    /** database object **/
    DBHelper database;

    /*************************************************************************************************
     * Description: This function creates and inflates the Add Show activity and layout.
     *
     * Inputs:
     *    @param savedInstanceState - previously saved state of application and data
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_show);

        // sets pointers to views
        setViewPointers();

        // initializes database object
        database = new DBHelper(this);

        // set options for dropdown field
        setVenues();
    }

    /*************************************************************************************************
     * Description: When this activity is re-opened, it sets the options for the dropdown results.
     *              This is necessary, since people could have added a venue.
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    @Override
    protected void onResume() {
        super.onResume();

        // set options for dropdown field
        setVenues();
    }

    /*************************************************************************************************
     * Description: This function sets pointers to the views in the display.
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public void setViewPointers() {
        showName = (EditText) findViewById(R.id.showName);
        venue = (Spinner) findViewById(R.id.showVenue);
        website = (EditText) findViewById(R.id.showWebsite);
        pictureURL = (EditText) findViewById(R.id.showPictureURL);
        artistsPerforming = (EditText) findViewById(R.id.showArtists);
        startDateTime = (EditText) findViewById(R.id.startDateTime);
        endDateTime = (EditText) findViewById(R.id.endDateTime);
    }

    /*************************************************************************************************
     * Description: This function sets the venues which appear in the form's dropdown field.
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public void setVenues() {
        // gets a list of venue names from the database
        List<String> list = database.getAllVenues();
        // creates array of adapter using list and layout
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        // sets dropdown adapter layout
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // sets adapter
        venue.setAdapter(dataAdapter);
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
        showName.setText("");
        setVenues();
        website.setText("");
        pictureURL.setText("");
        artistsPerforming.setText("");
        startDateTime.setText("");
        endDateTime.setText("");
    }

    /*************************************************************************************************
     * Description: This function adds a show to the database.
     *
     * Inputs: pressed to run the function
     *    @param view - the button that is clicked to run the function
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public void addShow(View view) {
        /* gets data from form fields **/
        String theName = showName.getText().toString();
        String theVenue = venue.getSelectedItem().toString();
        String theWebsite = website.getText().toString();
        String thePictureURL = pictureURL.getText().toString();
        String artistsString = artistsPerforming.getText().toString();
        String[] theArtists = artistsString.split(",");
        String theStartDateTime = startDateTime.toString();
        String theEndDateTime = endDateTime.toString();

        // adds venue to the database
        database.addShow(theName, theVenue, theWebsite, thePictureURL, theArtists, theStartDateTime, theEndDateTime);

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
