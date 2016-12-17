/*************************************************************************************************
 * File:   DBHelper.java
 * Author: Joe Menduni
 *
 * Created on December 5, 2016
 * Last Modified on December 14, 2016
 *
 * Purpose: This class contains all of the direct interactions with the SQLite Database,
 *          including creating the database & tables, inserting data, and querying the database.
 *************************************************************************************************/

package com.joemenduni.musicmaps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DBHelper extends SQLiteOpenHelper {

    /** version and name **/
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MusicMapsDB";

    /** table names **/
    private static final String GENRE_TABLE_NAME = "genre";
    private static final String ARTIST_TABLE_NAME = "artist";
    private static final String VENUE_TABLE_NAME = "venue";
    private static final String SHOW_TABLE_NAME = "show";
    private static final String ARTIST_SHOW_TABLE_NAME = "artistToshow";

    /** column names in genre table **/
    private static final String GENRE_ID = "_id";
    private static final String GENRE_NAME = "name";

    /** column names in artist table **/
    private static final String ARTIST_id = "_id";
    private static final String ARTIST_name = "name";
    private static final String ARTIST_genre_id = "genre_id";
    private static final String ARTIST_members = "members";
    private static final String ARTIST_website = "website";
    private static final String ARTIST_picture_url = "picture_url";
    private static final String ARTIST_town = "town";
    private static final String ARTIST_state = "state";
    private static final String ARTIST_zip_code = "zip_code";

    /** column names in venue table **/
    private static final String VENUE_id = "_id";
    private static final String VENUE_name = "name";
    private static final String VENUE_website = "website";
    private static final String VENUE_picture_url = "picture_url";
    private static final String VENUE_address = "address";
    private static final String VENUE_town = "town";
    private static final String VENUE_state = "state";
    private static final String VENUE_zip_code = "zip_code";
    private static final String VENUE_latitude = "latitude";
    private static final String VENUE_longitude = "longitude";

    /** column names in show table **/
    private static final String SHOW_id = "_id";
    private static final String SHOW_name = "name";
    private static final String SHOW_website = "website";
    private static final String SHOW_picture_url = "picture_url";
    private static final String SHOW_venue_id = "venue_id";
    private static final String SHOW_start_datetime = "start_datetime";
    private static final String SHOW_end_datetime = "end_datetime";
    private static final String SHOW_attendance = "attendance";

    /** column names in artist to show join table table **/
    private static final String ARTISTSHOW_id = "_id";
    private static final String ARTISTSHOW_artist_id = "artist_id";
    private static final String ARTISTSHOW_show_id = "show_id";

    /** counts for incrementing ids for table **/
    private static int genreCount = 0;
    private static int artistCount = 0;
    private static int showCount = 0;
    private static int venueCount = 0;
    private static int artistShowCount = 0;

    /** current database **/
    private SQLiteDatabase currentDB = null;

    /*************************************************************************************************
     * Description: This function creates the database helper and creates the database and tables
     *              if they're not already created.
     *
     * Inputs:
     *    @param context - application context
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // gets current database
        currentDB = this.getWritableDatabase();

        // list of table names
        String[] tables = {GENRE_TABLE_NAME, ARTIST_TABLE_NAME, VENUE_TABLE_NAME, SHOW_TABLE_NAME, ARTIST_SHOW_TABLE_NAME};

        // for all table
        for (String table: tables) {
            // drop tables if they exist
            currentDB.execSQL("DROP TABLE IF EXISTS " + table);
        }

        // create tables
        createTables();
    }

    /*************************************************************************************************
     * Description: This function creates the database helper and creates the database and tables
     *              if they're not already created.
     *
     * Inputs:
     *    @param database - the database from the SQLiteOpenHelper
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    @Override
    public void onCreate(SQLiteDatabase database) {
        // create tables
        createTables();
        // gets current database
        currentDB = database;
        // resets current database to null
        currentDB = null;
    }

    /*************************************************************************************************
     * Description: This function runs to upgrade the database from one version to another.
     *
     * Inputs:
     *    @param database - the database from the SQLiteOpenHelper
     *    @param oldVersion - old version number of the database
     *    @param newVersion - new version number of the database
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    @Override
    public void onUpgrade(SQLiteDatabase database,
                          int oldVersion,
                          int newVersion) {
        // list of table names
        String[] tables = {GENRE_TABLE_NAME, ARTIST_TABLE_NAME, VENUE_TABLE_NAME, SHOW_TABLE_NAME, ARTIST_SHOW_TABLE_NAME};

        // for all table
        for (String table: tables) {
            // drop tables if they exist
            currentDB.execSQL("DROP TABLE IF EXISTS " + table);
        }
        // calls on create
        onCreate(database);
    }

    /*************************************************************************************************
     * Description: This creates the 5 database tables, create indices, and inserts the intial data.
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    private void createTables() {
        // gets current database
        currentDB = this.getWritableDatabase();
        // sql commands to create genre table
        String genreTable = "CREATE TABLE " + GENRE_TABLE_NAME + "(" + makeGenreTableSQL() + ")";
        // sql commands to create artist table
        String artistTable = "CREATE TABLE " + ARTIST_TABLE_NAME + "(" + makeArtistTableSQL() + ")";
        // sql commands to create venue table
        String venueTable = "CREATE TABLE " + VENUE_TABLE_NAME + "(" + makeVenueTableSQL() + ")";
        // sql commands to create show table
        String showTable = "CREATE TABLE " + SHOW_TABLE_NAME + "(" + makeShowTableSQL() + ", FOREIGN KEY (" + SHOW_venue_id + ")" + "REFERENCES " + VENUE_TABLE_NAME + "(" + VENUE_id + ")" + ")";
        // sql commands to create artist to show join table
        String artistToshowTable = "CREATE TABLE " + ARTIST_SHOW_TABLE_NAME + "(" + makeArtistShowTableSQL() +
                ", FOREIGN KEY (" + ARTISTSHOW_artist_id + ")" + "REFERENCES " + ARTIST_TABLE_NAME + "(" + ARTIST_id + ") ON DELETE CASCADE, " +
                "FOREIGN KEY (" + ARTISTSHOW_show_id + ")" + "REFERENCES " + SHOW_TABLE_NAME + "(" + SHOW_id + ") ON DELETE CASCADE" + ")";
        // executes sql to build genre table
        currentDB.execSQL(genreTable);
        // executes sql to build artist table
        currentDB.execSQL(artistTable);
        // executes sql to build venue table
        currentDB.execSQL(venueTable);
        // executes sql to build show table
        currentDB.execSQL(showTable);
        // executes sql to build artisttoshow table
        currentDB.execSQL(artistToshowTable);
        // executes sql to create indexes
        currentDB.execSQL("CREATE INDEX artist_index ON " + ARTIST_SHOW_TABLE_NAME + " (artist_id);");
        currentDB.execSQL("CREATE INDEX show_index ON " + ARTIST_SHOW_TABLE_NAME + " (show_id);");
        // inserts initial genres into the database
        makeInitialGenres();
        // inserts initial venues into the database
        makeInitialVenues();
        // inserts initial shows into the database
        makeInitialShows();
    }

    /*************************************************************************************************
     * Description: This creates the SQL commands to add the columns to the genre table.
     *
     * Outputs:
     *      String - SQL commands to insert columns into the genre table
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    private String makeGenreTableSQL() {
        // map to store column key and column type
        Map<String, String> genreDBMap = new HashMap<String, String>();
        // adds key & type to map
        genreDBMap.put(GENRE_ID, "INTEGER PRIMARY KEY");
        genreDBMap.put(GENRE_NAME, "TEXT");
        // returns map
        return makeDBStringFromMap(genreDBMap);
    }

    /*************************************************************************************************
     * Description: This creates the SQL commands to add the columns to the artist table.
     *
     * Outputs:
     *      String - SQL commands to insert columns into the artist table
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    private String makeArtistTableSQL() {
        // map to store column key and column type
        Map<String, String> artistDBMap = new HashMap<String, String>();
        // adds key & type to map
        artistDBMap.put(ARTIST_id, "INTEGER PRIMARY KEY");
        artistDBMap.put(ARTIST_name, "TEXT");
        artistDBMap.put(ARTIST_genre_id, "INTEGER");
        artistDBMap.put(ARTIST_members, "INTEGER");
        artistDBMap.put(ARTIST_website, "TEXT");
        artistDBMap.put(ARTIST_picture_url, "TEXT");
        artistDBMap.put(ARTIST_town, "TEXT");
        artistDBMap.put(ARTIST_state, "TEXT");
        artistDBMap.put(ARTIST_zip_code, "INTEGER");
        artistDBMap.put("FOREIGN KEY (" + ARTIST_genre_id + ")", "REFERENCES " + GENRE_TABLE_NAME + "(" + GENRE_ID + ")");
        // returns map
        return makeDBStringFromMap(artistDBMap);
    }

    /*************************************************************************************************
     * Description: This creates the SQL commands to add the columns to the venue table.
     *
     * Outputs:
     *      String - SQL commands to insert columns into the venue table
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    private String makeVenueTableSQL() {
        // map to store column key and column type
        Map<String, String> venueDBMap = new HashMap<String, String>();
        // adds key & type to map
        venueDBMap.put(VENUE_id, "INTEGER PRIMARY KEY");
        venueDBMap.put(VENUE_name, "TEXT");
        venueDBMap.put(VENUE_website, "TEXT");
        venueDBMap.put(VENUE_picture_url, "TEXT");
        venueDBMap.put(VENUE_address, "TEXT");
        venueDBMap.put(VENUE_town, "TEXT");
        venueDBMap.put(VENUE_state, "TEXT");
        venueDBMap.put(VENUE_zip_code, "INTEGER");
        venueDBMap.put(VENUE_latitude, "REAL");
        venueDBMap.put(VENUE_longitude, "REAL");
        // returns map
        return makeDBStringFromMap(venueDBMap);
    }

    /*************************************************************************************************
     * Description: This creates the SQL commands to add the columns to the show table.
     *
     * Outputs:
     *      String - SQL commands to insert columns into the show table
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    private String makeShowTableSQL() {
        // map to store column key and column type
        Map<String, String> showDBMap = new HashMap<String, String>();
        // adds key & type to map
        showDBMap.put(SHOW_id, "INTEGER PRIMARY KEY");
        showDBMap.put(SHOW_name, "TEXT");
        showDBMap.put(SHOW_website, "TEXT");
        showDBMap.put(SHOW_picture_url, "TEXT");
        showDBMap.put(SHOW_venue_id, "INTEGER");
        showDBMap.put(SHOW_start_datetime, "INTEGER");
        showDBMap.put(SHOW_end_datetime, "INTEGER");
        showDBMap.put(SHOW_attendance, "INTEGER");
        // returns map
        return makeDBStringFromMap(showDBMap);
    }

    /*************************************************************************************************
     * Description: This creates the SQL commands to add the columns to the artisttoshow table.
     *
     * Outputs:
     *      String - SQL commands to insert columns into the artisttoshow table
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    private String makeArtistShowTableSQL() {
        // map to store column key and column type
        Map<String, String> artistshowDBMap = new HashMap<String, String>();
        // adds key & type to map
        artistshowDBMap.put(ARTISTSHOW_id, "INTEGER PRIMARY KEY");
        artistshowDBMap.put(ARTISTSHOW_artist_id, "INTEGER");
        artistshowDBMap.put(ARTISTSHOW_show_id, "INTEGER");
        // returns map
        return makeDBStringFromMap(artistshowDBMap);
    }

    /*************************************************************************************************
     * Description: This function creates the SQL commands to insert columns in a table.
     *
     * Inputs:
     *      @param dbMap - map of the keys and types to be converted to a string
     *
     * Outputs:
     *      String - SQL commands to insert columns
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    private String makeDBStringFromMap(Map<String, String> dbMap) {
        // string to return
        String returnString = "";
        // iterator to go through map
        Iterator iterator = dbMap.entrySet().iterator();
        // while there is another entry
        while (iterator.hasNext()) {
            // gets pair of key and value
            Map.Entry pair = (Map.Entry)iterator.next();
            // all except the last one
            if (iterator.hasNext()) {
                // has key, value, and comman
                returnString += pair.getKey() + " " + pair.getValue() + ", ";
            }
            // the last one
            else {
                // has just key and value
                returnString += pair.getKey() + " " + pair.getValue();
            }
            // avoids a ConcurrentModificationException
            iterator.remove();
        }
        // returns created string
        return returnString;
    }

    /*************************************************************************************************
     * Description: This function adds a hardcoded list of genres to the database.
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    private void makeInitialGenres() {
        // list of genre games
        String[] genres = {"Rock 'n Roll", "Pop", "Heavy Metal", "Rap", "Country", "Punk", "R & B", "Jazz", "Classical", "Alternative", "Hip Hop", "Soul", "Reggae", "Techno", "Grunge", "EDM", "Hard Rock", "Blues"};
        // for all genre names
        for (String genre: genres) {
            // increment id count
            genreCount ++;
            // content values hold keys & values to put in database
            ContentValues values = new ContentValues();
            // puts values in contentvalues
            values.put(GENRE_ID, genreCount);
            values.put(GENRE_NAME, genre);
            // adds row to database
            this.getWritableDatabase().insert(GENRE_TABLE_NAME, null, values);
        }
    }

    /*************************************************************************************************
     * Description: This function adds a hardcoded list of venues to the database.
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    private void makeInitialVenues() {
        // list of venus with names, websites, latitude, and longitude
        String[] venues = {"Hangar Theatre,http://www.hangartheatre.org/,42.4553429,-76.51731889999999,1",
                            "The Haunt,http://www.thehaunt.com/,42.4514511,-76.5051489,2",
                            "The Dock,http://thedockithaca.com/,42.4519932,-76.5133232,3",
                            "Lot 10,http://www.lot-10.com/,42.4391302,-76.4992535,4",
                            "State Theatre of Ithaca,http://www.stateofithaca.com/,42.4392627,-76.49960229999999,5",
                             "Trumansburg Fairground,http://www.tburgevents.com/venue/trumansburg-fair-grounds/,42.5360253,-76.6466288,6",
                             "Bernie Milton Pavilion,http://ithacafestival.org/,42.4393319,-76.49696639999999,7",
                            "Ithaca College,http://www.ithaca.edu,42.4217,-76.4986,8"};
        // for all venues
        for (String venue: venues) {
            // increment id count
            venueCount ++;
            // split entry by comma
            String[] lineSplit = venue.split(",");
            // content values hold keys & values to put in database
            ContentValues values = new ContentValues();
            // puts values in contentvalues
            values.put(VENUE_id, Integer.valueOf(lineSplit[4]));
            values.put(VENUE_name,lineSplit[0]);
            values.put(VENUE_website, lineSplit[1]);
            values.put(VENUE_latitude, Double.valueOf(lineSplit[2]));
            values.put(VENUE_longitude, Double.valueOf(lineSplit[3]));
            // adds row to database
            this.getWritableDatabase().insert(VENUE_TABLE_NAME, null, values);
        }
    }

    /*************************************************************************************************
     * Description: This function adds a hardcoded list of shows to the database.
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    private void makeInitialShows() {
        // list of shows with names, websites, and venue id
        String[] venues = {"Grassroots 2016,http://www.grassrootsfest.org/festival/,1",
                            "Grassroots 2015,http://www.grassrootsfest.org/festival/,2",
                            "Ithaca Festival 2016,http://www.grassrootsfest.org/festival/,3",
                            "Ithaca AppleFest 2016,http://www.downtownithaca.com/ithaca-events/Apple%20Harvest%20Festival%20Presented%20by%20Tompkins%20Trust,4",
                            "John Brown's Body,http://dansmallspresents.com/john-browns-body,5",
                            "The Blind Spots: Willy Wonka and The Chocolate Factory,http://dansmallspresents.com/the-blind-spots-willy-wonka-and-the-chocolate-factory,6",
                            "Driftwood,http://dansmallspresents.com/driftwood,7",
                            "Jimkata,http://dansmallspresents.com/jimkata,8",
                            "Big Mean Sound Machine,http://dansmallspresents.com/big-mean-sound-machine,1",
                            "Ben Harper & The Innocent Criminals,http://dansmallspresents.com/ben-harper-the-innocent-criminals,2"};
        // for all shows
        for (String venue: venues) {
            // increment id count
            showCount ++;
            // split entry by comma
            String[] lineSplit = venue.split(",");
            // content values hold keys & values to put in database
            ContentValues values = new ContentValues();
            // puts values in contentvalues
            values.put(SHOW_id, showCount);
            values.put(SHOW_name,lineSplit[0]);
            values.put(SHOW_website, lineSplit[1]);
            values.put(SHOW_venue_id, Integer.valueOf(lineSplit[2]));
            // adds row to database
            this.getWritableDatabase().insert(SHOW_TABLE_NAME, null, values);
        }
    }

    /*************************************************************************************************
     * Description: This function gets a list of the names of all genres.
     *
     * Outputs:
     *      List<String> - list of genre names
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public List<String> getAllGenres() {
        // makes new list
        List<String> genreList = new ArrayList<>();
        // database query string
        String selectQuery = "SELECT * FROM " + GENRE_TABLE_NAME + ";";
        // executes database query and gets cursor of all genres
        Cursor cursor = this.getReadableDatabase().rawQuery(selectQuery, null);
        // if there is a result
        if ((cursor.moveToFirst())) {
            do {
                // add the name of the genre to the string list
                genreList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        // closes cursor
        cursor.close();
        // returns string list
        return genreList;
    }

    /*************************************************************************************************
     * Description: This function gets a map of all artist ids and names.
     *
     * Outputs:
     *      Map<Integer, String> - map of all artist ids and names
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public Map<Integer, String> getAllArtists() {
        // creates new map
        Map<Integer, String> artistMap = new HashMap<Integer, String>();
        // database query string
        String selectQuery = "SELECT * FROM " + ARTIST_TABLE_NAME + ";";
        // executes database query and gets cursor of all artists
        Cursor cursor = this.getReadableDatabase().rawQuery(selectQuery, null);
        // if there is a result
        if ((cursor.moveToFirst())) {
            do {
                // adds id and name to the map
                artistMap.put(cursor.getInt(0), cursor.getString(1));
            } while (cursor.moveToNext());
        }
        // closes cursor
        cursor.close();
        // returns map
        return artistMap;
    }

    /*************************************************************************************************
     * Description: This function gets a list of the names of all venues.
     *
     * Outputs:
     *      List<String> - list of venues names
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public List<String> getAllVenues() {
        // makes new list
        List<String> venueList = new ArrayList<>();
        // database query string
        String selectQuery = "SELECT * FROM " + VENUE_TABLE_NAME + ";";
        // executes database query and gets cursor of all venues
        Cursor cursor = this.getReadableDatabase().rawQuery(selectQuery, null);
        // if there is a result
        if ((cursor.moveToFirst())) {
            do {
                // add the name of the venue to the string list
                venueList.add(cursor.getString(6));
            } while (cursor.moveToNext());
        }
        // closes cursor
        cursor.close();
        // returns string list
        return venueList;
    }

    /*************************************************************************************************
     * Description: This function gets a map of all venue ids and names.
     *
     * Outputs:
     *      Map<Integer, String> - map of all venue ids and names
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public Map<Integer, String> getAllVenuesMap() {
        // creates new map
        Map<Integer, String> venueMap = new HashMap<Integer, String>();
        // database query string
        String selectQuery = "SELECT * FROM " + VENUE_TABLE_NAME + ";";
        // executes database query and gets cursor of all venues
        Cursor cursor = this.getReadableDatabase().rawQuery(selectQuery, null);
        // if there is a result
        if ((cursor.moveToFirst())) {
            do {
                // adds id and name to the map
                venueMap.put(cursor.getInt(9), cursor.getString(6));
            } while (cursor.moveToNext());
        }
        // closes cursor
        cursor.close();
        // returns map
        return venueMap;
    }

    /*************************************************************************************************
     * Description: This function gets a cursor of all venues.
     *
     * Outputs:
     *      Cursor - cursor of all venues.
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public Cursor getAllVenuesCursors() {
        // database query string
        String selectQuery = "SELECT * FROM " + VENUE_TABLE_NAME + ";";
        // executes database query and gets cursor of all venues
        Cursor cursor = this.getReadableDatabase().rawQuery(selectQuery, null);
        // returns cursor
        return cursor;
    }

    /*************************************************************************************************
     * Description: This function gets a cursor of all shows.
     *
     * Outputs:
     *      Cursor - cursor of all shows.
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public Cursor getAllShowsCursors() {
        // database query string
        String selectQuery = "SELECT * FROM " + SHOW_TABLE_NAME + ";";
        // executes database query and gets cursor of all shows
        Cursor cursor = this.getReadableDatabase().rawQuery(selectQuery, null);
        // returns cursor
        return cursor;
    }

    /*************************************************************************************************
     * Description: This function adds an artist to the database.
     *
     * Inputs:
     *      @param theName - artist's name
     *      @param theGenre - artist's genre
     *      @param theMembers - artist's member count
     *      @param theWebsite - artist's website
     *      @param thePictureURL - artist's picture url
     *      @param theTown - artist's town
     *      @param theState - artist's state
     *      @param theZipCode - artist's zip code
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public void addArtist(String theName, String theGenre, int theMembers, String theWebsite, String thePictureURL, String theTown, String theState, String theZipCode) {
        // increments artist id count
        artistCount ++;
        // content values hold keys & values to put in database
        ContentValues values = new ContentValues();
        // puts values in contentvalues
        values.put(ARTIST_id, artistCount);
        values.put(ARTIST_name, theName);
        values.put(ARTIST_genre_id, theGenre);
        values.put(ARTIST_members, theMembers);
        values.put(ARTIST_website, theWebsite);
        values.put(ARTIST_picture_url, thePictureURL);
        values.put(ARTIST_town, theTown);
        values.put(ARTIST_state, theState);
        values.put(ARTIST_zip_code, theZipCode);
        // insert row in database
        currentDB.insert(ARTIST_TABLE_NAME, null, values);
    }

    /*************************************************************************************************
     * Description: This function adds a show to the database.
     *
     * Inputs:
     *      @param theName - show's name
     *      @param theVenue   - show's venue
     *      @param theWebsite   - show's website
     *      @param thePictureURL   - show's picture url
     *      @param theArtists   - show's list of artists
     *      @param theStartDateTime   - show's start date & time
     *      @param theEndDateTime   - show's end date & time
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public void addShow(String theName, String theVenue, String theWebsite, String thePictureURL, String[] theArtists, String theStartDateTime, String theEndDateTime) {
        // increments show id count
        showCount ++;
        // content values hold keys & values to put in database
        ContentValues values = new ContentValues();
        // puts values in contentvalues
        values.put(SHOW_id, showCount);
        values.put(SHOW_name, theName);
        values.put(SHOW_venue_id, theVenue);
        values.put(SHOW_website, theWebsite);
        values.put(SHOW_picture_url, thePictureURL);
        values.put(SHOW_start_datetime, theStartDateTime);
        values.put(SHOW_end_datetime, theEndDateTime);
        // insert row in database
        currentDB.insert(SHOW_TABLE_NAME, null, values);
        // makes rows in artisttoshow join table
        makeArtistsToShows(showCount, theArtists);
    }

    /*************************************************************************************************
     * Description: This function gets the id of the artist by looking up by name.
     *
     * Inputs:
     *      @param artistName - the artist name to lookup
     *
     * Outputs:
     *      int - id of the artist
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public int findArtistByName(String artistName) {
        // map of all artists
        Map<Integer, String> artistMap = getAllArtists();
        // iterator to go through map
        Iterator it = artistMap.entrySet().iterator();
        // while there is another entry
        while (it.hasNext()) {
            // gets pair of key and value
            Map.Entry pair = (Map.Entry)it.next();
            // if the input name equals the artist name
            if (artistName.equals(pair.getValue())) {
                // return the artist's id
                return (int) pair.getKey();
            }
            // avoids a ConcurrentModificationException
            it.remove();
        }
        // returns a null value
        return -1;
    }
    /*************************************************************************************************
     * Description: This function gets the venue's latitude and longitude by the venue id.
     *
     * Inputs:
     *      @param venueID - the venue ID to find
     *
     * Outputs:
     *      double[] - index 0 is latitude, index 1 is longitude
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public double[] findVenueLatLngByID(int venueID) {
        // creates new map
        Map<Integer, String> venueMap = new HashMap<Integer, String>();
        // database query string
        String selectQuery = "SELECT * FROM " + VENUE_TABLE_NAME + ";";
        // executes database query and gets cursor of all artists
        Cursor cursor = this.getReadableDatabase().rawQuery(selectQuery, null);
        // if there is a result
        if ((cursor.moveToFirst())) {
            do {
                // if there is a venue found
                if (venueID == cursor.getInt(9)) {
                    // return the venue's latitude and longitude
                    return new double[]{cursor.getDouble(5), cursor.getDouble(2)};
                }
            } while (cursor.moveToNext());
        }
        // closes cursor
        cursor.close();
        // returns array of null values
        return new double[] {-1, -1};
    }

    /*************************************************************************************************
     * Description: This function gets the venue name by the venue id.
     *
     * Inputs:
     *      @param venueID - the venue ID to find
     *
     * Outputs:
     *      string - venue name.
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public String findVenueNameByID(int venueID) {
        // creates new map
        Map<Integer, String> venueMap = new HashMap<Integer, String>();
        // database query string
        String selectQuery = "SELECT * FROM " + VENUE_TABLE_NAME + ";";
        // executes database query and gets cursor of all artists
        Cursor cursor = this.getReadableDatabase().rawQuery(selectQuery, null);
        // if there is a result
        if ((cursor.moveToFirst())) {
            do {
                // if there is a venue found
                if (venueID == cursor.getInt(9)) {
                    // return the venue's id
                    return cursor.getString(6);
                }
            } while (cursor.moveToNext());
        }
        // closes cursor
        cursor.close();
        // returns map
        return "";
    }

    /*************************************************************************************************
     * Description: This function adds artsits to shows to the database for a specific show.
     *
     * Inputs:
     *      @param show_id - id of the show
     *      @param artists - list of artists
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    private void makeArtistsToShows(int show_id, String[] artists) {
        // for all artists in the list
       for (String artistName: artists) {
           // increments artistshow id
           artistShowCount ++;
           // content values hold keys & values to put in database
           ContentValues values = new ContentValues();
           // puts values in contentvalues
           values.put(ARTISTSHOW_id, artistShowCount);
           values.put(ARTISTSHOW_show_id, show_id);
           // if the artist hasn't been created yet
           if (findArtistByName(artistName) == -1) {
               // increments artist id count
               artistCount ++;
               // adds artist to database
               addArtist(artistName, null, -1, null, null, null, null, null);
           }
           // adds artist count
           values.put(ARTISTSHOW_artist_id, artistCount);
           // insert row in database
           currentDB.insert(ARTIST_SHOW_TABLE_NAME, null, values);
        }
    }

    /*************************************************************************************************
     * Description: This function adds a venue to the database.
     *
     * Inputs:
     *      @param theName - venue's name
     *      @param theWebsite - venue's website
     *      @param thePictureURL - venue's picture URL
     *      @param theStreetAddress - venue's street address
     *      @param theTown - venue's town
     *      @param theState - venue's state
     *      @param theZipCode - venue's zip code
     *      @param latitude - venue's latitude
     *      @param longitude - venue's longitude
     *
     * Last Modified: 12/14/16
     *************************************************************************************************/
    public void addVenue(String theName, String theWebsite, String thePictureURL, String theStreetAddress, String theTown, String theState, String theZipCode, double latitude, double longitude) {
        // increments venue id count
        venueCount ++;
        // content values hold keys & values to put in database
        ContentValues values = new ContentValues();
        // puts values in contentvalues
        values.put(VENUE_id, venueCount);
        values.put(VENUE_name, theName);
        values.put(VENUE_website, theWebsite);
        values.put(VENUE_picture_url, thePictureURL);
        values.put(VENUE_address, theStreetAddress);
        values.put(VENUE_town, theTown);
        values.put(VENUE_state, theState);
        values.put(VENUE_zip_code, theZipCode);
        values.put(VENUE_latitude, latitude);
        values.put(VENUE_longitude, longitude);
        // makes rows in artisttoshow join table
        currentDB.insert(VENUE_TABLE_NAME, null, values);
    }
}
