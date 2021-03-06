/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dotapk.moviesnow.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dotapk.moviesnow.data.NewsContract.NewsEntry;
import com.dotapk.moviesnow.data.NewsContract.DetailsEntry;

/**
 * Manages a local database for weather data.
 */
public class NewsDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "weather.db";

    public NewsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude
        final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE " + NewsEntry.TABLE_NAME + " (" +
                NewsEntry._ID + " INTEGER PRIMARY KEY," +
                
                NewsEntry.COLUMN_NEWSHEAD + " TEXT NOT NULL, " +
              
                " );";

        final String SQL_CREATE_WEATHER_TABLE = "CREATE TABLE " + DetailsEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                DetailsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                DetailsEntry.COLUMN_NEWS_KEY + " INTEGER NOT NULL, " +
                DetailsEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
               

                DetailsEntry.COLUMN_DETAILS + " TEXT NOT NULL, " +
                DetailsEntry.COLUMN_CAPTION + " TEXT NOT NULL, " +

             

                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + DetailsEntry.COLUMN_NEWS_KEY + ") REFERENCES " +
                NewsEntry.TABLE_NAME + " (" + NewsEntry._ID + "); ";

                // To assure the application have just one weather entry per day
                // per location, it's created a UNIQUE constraint with REPLACE strategy
               

        sqLiteDatabase.execSQL(SQL_CREATE_LOCATION_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NewsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DetailsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
