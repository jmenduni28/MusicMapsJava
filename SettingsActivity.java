/*************************************************************************************************
 * File:   SettingsActivity.java
 * Author: Joe Menduni
 *
 * Created on December 5, 2016
 * Last Modified on December 14, 2016
 *
 * Purpose: This activity creates and inflates the Settings Activity, which contains a
 *              form fields that set the user's preferences.
 ************************************************************************************************/

package com.joemenduni.musicmaps;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;

public class SettingsActivity extends AppCompatActivity {

    /** form fields **/
    private EditText userName;
    private RadioButton musicianYes;
    private RadioButton musicianNo;
    private RadioButton locationYes;
    private RadioButton locationNo;
    private SeekBar range;

    /** user preferences **/
    private SharedPreferences settings;

    /*************************************************************************************************
     * Description: This function creates and inflates the Settings activity and layout.
     *
     * Inputs:
     *    @param savedInstanceState - previously saved state of application and data
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // gets user preferences from file
        settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);

        setViewPointers();
        loadUserPreferences();

        // sets listener for name field
        userName.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                handleNameChange();
            }
        });

        // sets listeners for radio buttons
        musicianYes.setOnClickListener(radio_listener_musician);
        musicianNo.setOnClickListener(radio_listener_musician);
        locationYes.setOnClickListener(radio_listener_location);
        locationNo.setOnClickListener(radio_listener_location);

        // sets listener for seekbar
        range.setOnSeekBarChangeListener(seekbar_listener);
    }

    /*************************************************************************************************
     * Description: This function sets pointers to the views in the display.
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public void setViewPointers() {
        userName = (EditText) findViewById(R.id.userName);
        musicianYes = (RadioButton) findViewById(R.id.settings_musician_yes);
        musicianNo = (RadioButton) findViewById(R.id.settings_musician_no);
        locationYes = (RadioButton) findViewById(R.id.settings_location_yes);
        locationNo = (RadioButton) findViewById(R.id.settings_location_no);
        range = (SeekBar) findViewById(R.id.seekBar);
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
     * Description: This function loads the user's preferences from Android SharedPreferneces.
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public void loadUserPreferences() {
        // if settings contains user's name
        if (settings.contains("name")) {
            String name = settings.getString("name", null);
            userName.setText(name);
        }
        // if settings contain a musician
        if (settings.contains("musician")) {
            Boolean musician = settings.getBoolean("musician", true);
            if (musician) {
                if (!musicianYes.isChecked()) {
                    musicianYes.toggle();
                    if (musicianNo.isChecked()) {
                        musicianNo.toggle();
                    }
                }
            } else {
                if (!musicianNo.isChecked()) {
                    musicianNo.toggle();
                    if (musicianYes.isChecked()) {
                        musicianYes.toggle();
                    }
                }
            }
        }
        // if settings contain a user's location prefernece
        if (settings.contains("location")) {
            Boolean location = settings.getBoolean("location", true);
            if (location) {
                if (!locationYes.isChecked()) {
                    locationYes.toggle();
                    if (locationNo.isChecked()) {
                        locationNo.toggle();
                    }
                }
            } else {
                if (!locationNo.isChecked()) {
                    locationNo.toggle();
                    if (locationYes.isChecked()) {
                        locationYes.toggle();
                    }
                }
            }
        }
        // if settings contains milage range
        if (settings.contains("range")) {
            int rangeDisplay = settings.getInt("range", 0);
            range.setProgress(rangeDisplay);
        }
    }

    /*************************************************************************************************
     * Description: This function runs on the change of the EditText and puts it into preferences.
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    private void handleNameChange() {
        settings.edit().putString("name", userName.getText().toString()).apply();
    }

    /*************************************************************************************************
     * Description: This listener listens for a click of the radio button and changes the
     *              musician's setting in the user preferences.
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    View.OnClickListener radio_listener_musician = new View.OnClickListener(){
        public void onClick(View v) {
            boolean isMusician = false;
            // if the yes button was selected
            if (v == musicianYes) {
                isMusician = true;
            }
            // if the no button was selected
            else if (v == musicianNo) {
                isMusician = false;
            }
            // add musician to user preferences
            settings.edit().putBoolean("musician", isMusician).apply();
        }
    };

    /*************************************************************************************************
     * Description: This listener listens for a click of the radio button and changes the
     *              location's setting in the user preferences.
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    View.OnClickListener radio_listener_location = new View.OnClickListener(){
        public void onClick(View v) {
            boolean useLocation = false;
            // if the yes button was selected
            if (v == locationYes) {
                useLocation = true;
            }
            // if the no button was selected
            else if (v == locationNo) {
                useLocation = false;
            }
            // add location to user preferences
            settings.edit().putBoolean("location", useLocation).apply();
        }
    };

    /*************************************************************************************************
     * Description: This listener listens for a change in the seekbar and changes the
     *              range's setting in the user preferences.
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    SeekBar.OnSeekBarChangeListener seekbar_listener = new SeekBar.OnSeekBarChangeListener() {

        public void onStartTrackingTouch(SeekBar seekBar) {};

        public void onStopTrackingTouch (SeekBar seekBar) {};

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            settings.edit().putInt("range", progress).apply();
        }
    };



}
