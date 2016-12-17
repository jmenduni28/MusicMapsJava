/*************************************************************************************************
 * File:   MainActivity.java
 * Author: Joe Menduni
 *
 * Created on December 5, 2016
 * Last Modified on December 14, 2016
 *
 * Purpose: This activity creates and inflates the Main Activity, which contains a
 *          bunch of buttons for the user to select to go to.
 ************************************************************************************************/

package com.joemenduni.musicmaps;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    /** database object **/
    DBHelper database;

    /** name of the user preferences files **/
    public static final String PREFS_NAME = "AppPrefsFile";

    /** user preferences **/
    private SharedPreferences settings;

    /** form field for name **/
    TextView nameView;

    /*************************************************************************************************
     * Description: This function creates and inflates the Main activity and layout.
     *
     * Inputs:
     *    @param savedInstanceState - previously saved state of application and data
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializes database object
        database = new DBHelper(this);

        // gets user preferences from file
        settings = getSharedPreferences(PREFS_NAME, 0);

        // makes pointer to view
        nameView = (TextView) findViewById(R.id.welcomeText);

    }

    /*************************************************************************************************
     * Description: This function runs when the activity is resumed and updates the user's name
     *              from the shared preferences.
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    @Override
    protected void onResume() {
        super.onResume();
        // if there is a preference stored in name
        if (settings.contains("name")) {
            String name = settings.getString("name", null);
            nameView.setText("Welcome," + name + "!");
        }
    }

    /*************************************************************************************************
     * Description: This function goes to the add artist page.
     *
     * Inputs: pressed to run the function
     *    @param view - the button that is clicked to run the function
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public void gotoAddArtist(View view) {
        Intent addArtistIntent = new Intent(getApplicationContext(), AddArtistActivity.class);
        startActivity(addArtistIntent);
    }

    /*************************************************************************************************
     * Description: This function goes to the add show page.
     *
     * Inputs: pressed to run the function
     *    @param view - the button that is clicked to run the function
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public void gotoAddShow(View view) {
        Intent addShowIntent = new Intent(getApplicationContext(), AddShowActivity.class);
        startActivity(addShowIntent);
    }

    /*************************************************************************************************
     * Description: This function goes to the add venue page.
     *
     * Inputs: pressed to run the function
     *    @param view - the button that is clicked to run the function
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public void gotoAddVenue(View view) {
        Intent addVenueIntent = new Intent(getApplicationContext(), AddVenueActivity.class);
        startActivity(addVenueIntent);
    }

    /*************************************************************************************************
     * Description: This function goes to the about page.
     *
     * Inputs: pressed to run the function
     *    @param view - the button that is clicked to run the function
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public void gotoAbout(View view) {
        Intent aboutIntent = new Intent(getApplicationContext(), AboutActivity.class);
        startActivity(aboutIntent);
    }

    /*************************************************************************************************
     * Description: This function goes to the settings page.
     *
     * Inputs: pressed to run the function
     *    @param view - the button that is clicked to run the function
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public void gotoSettings(View view) {
        Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(settingsIntent);
    }

    /*************************************************************************************************
     * Description: This function goes to the map page.
     *
     * Inputs: pressed to run the function
     *    @param view - the button that is clicked to run the function
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public void gotoMap(View view) {
        Intent mapIntent = new Intent(getApplicationContext(), MapActivity.class);
        startActivity(mapIntent);
    }

    /*************************************************************************************************
     * Description: This function goes to the search page.
     *
     * Inputs: pressed to run the function
     *    @param view - the button that is clicked to run the function
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public void gotoSearch(View view) {
        Intent searchIntent = new Intent(getApplicationContext(), SearchActivity.class);
        startActivity(searchIntent);
    }





}
