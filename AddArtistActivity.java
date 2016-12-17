/*************************************************************************************************
 * File:   AddArtistActivity.java
 * Author: Joe Menduni
 *
 * Created on December 5, 2016
 * Last Modified on December 14, 2016
 *
 * Purpose: This activity creates and inflates the Add Artist Activity, which contains a
 *              a form for the user of the app to add an artist to the database.
 *************************************************************************************************/

package com.joemenduni.musicmaps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class AddArtistActivity extends AppCompatActivity {

    /** form fields **/
    private EditText artistName;
    private Spinner genre;
    private EditText numberMembers;
    private EditText website;
    private EditText pictureURL;
    private EditText town;
    private EditText state;
    private EditText zipCode;

    /** database object **/
    DBHelper database;

    /*************************************************************************************************
     * Description: This function creates and inflates the Add Artist activity and layout.
     *
     * Inputs:
     *    @param savedInstanceState - previously saved state of application and data
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_artist);

        // sets pointers to views
        setViewPointers();

        // initializes database object
        database = new DBHelper(this);

        // set options for dropdown field
        setGenres();
    }

    /*************************************************************************************************
     * Description: When this activity is re-opened, it sets the options for the dropdown results.
     *              This is necessary, since people could have added a genre.
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    @Override
    protected void onResume() {
        super.onResume();

        // set options for dropdown field
        setGenres();
    }

    /*************************************************************************************************
     * Description: This function sets pointers to the views in the display.
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public void setViewPointers() {
        artistName = (EditText) findViewById(R.id.artistName);
        genre = (Spinner) findViewById(R.id.artistGenre);
        numberMembers = (EditText) findViewById(R.id.artistMemberCount);
        website = (EditText) findViewById(R.id.artistWebsite);
        pictureURL = (EditText) findViewById(R.id.artistPictureURL);
        town = (EditText) findViewById(R.id.artistTown);
        state = (EditText) findViewById(R.id.artistState);
        zipCode = (EditText) findViewById(R.id.artistZipCode);
    }

    /*************************************************************************************************
     * Description: This function sets the genres which appear in the form's dropdown field.
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public void setGenres() {
        // gets a list of genre names from the database
        List<String> list = database.getAllGenres();
        // creates array of adapter using list and layout
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        // sets dropdown adapter layout
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // sets adapter
        genre.setAdapter(dataAdapter);
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
        artistName.setText("");
        setGenres();
        numberMembers.setText("");
        website.setText("");
        pictureURL.setText("");
        town.setText("");
        state.setText("");
        zipCode.setText("");
    }

    /*************************************************************************************************
     * Description: This function adds an artist to the database.
     *
     * Inputs: pressed to run the function
     *    @param view - the button that is clicked to run the function
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public void addArtist(View view) {
        // gets data from form fields
        String theName = artistName.getText().toString();
        String theGenre = genre.getSelectedItem().toString();
        int theMembers = Integer.valueOf(numberMembers.getText().toString());
        String theWebsite = website.getText().toString();
        String thePictureURL = pictureURL.getText().toString();
        String theTown = town.getText().toString();
        String theState = state.getText().toString();
        String theZipCode = zipCode.getText().toString();

        // adds artist to the database
        database.addArtist(theName, theGenre, theMembers, theWebsite, thePictureURL, theTown, theState, theZipCode);

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
