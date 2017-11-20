package com.ucd.user.weatherfitness.database;

/**
 * Created by Mike on 11/19/2017.
 */

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ucd.user.weatherfitness.model.Weather;

public class WeatherTable {

    // Database table
    private static final String LOG_TAG = "WeatherTable";
    public static final String TABLE_WEATHER = "Weather";


    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATETIME = "_id";
    public static final String COLUMN_LOCATION = "_id";
    public static final String COLUMN_LAT = "_id";
    public static final String COLUMN_LON = "_id";
    public static final String COLUMN_WIND = "wind";
    public static final String COLUMN_PRECIPITATION = "precipitation";
    public static final String COLUMN_TEMPERATURE = "temperature";
    public static final String COLUMN_PRESSURE = "pressure";
    public static final String COLUMN_SCORE = "score";


    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table if not exists "
            + TABLE_WEATHER
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_DATETIME + " text not null, "
            + COLUMN_LOCATION + " text not null, "
            + COLUMN_LAT + " text not null, "
            + COLUMN_LON + " text not null, "
            + COLUMN_WIND + " text not null, "
            + COLUMN_PRECIPITATION + " text not null,"
            + COLUMN_TEMPERATURE + " text not null,"
            + COLUMN_PRESSURE + " text not null,"
            + COLUMN_SCORE + " text not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        Log.w(LOG_TAG, DATABASE_CREATE);
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(Weather.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER);
        onCreate(database);
    }
}