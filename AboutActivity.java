/*************************************************************************************************
 * File:   AboutActivity.java
 * Author: Joe Menduni
 *
 * Created on December 5, 2016
 * Last Modified on December 14, 2016
 *
 * Purpose: This activity creates and inflates the About Activity, which contains a
 *              couple of TextViews with information about the app.
 *************************************************************************************************/

package com.joemenduni.musicmaps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AboutActivity extends AppCompatActivity {

    /*************************************************************************************************
     * Description: This function creates and inflates the About activity and layout.
     *
     * Inputs:
     *    @param savedInstanceState - previously saved state of application and data
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
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
