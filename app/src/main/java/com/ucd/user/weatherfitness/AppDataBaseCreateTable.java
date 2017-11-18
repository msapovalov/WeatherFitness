package com.ucd.user.weatherfitness;

import android.provider.BaseColumns;
/**
 * Created by mihhail_shapovalov on 11/18/17.
 */

public class AppDataBaseCreateTable {

    public static final String SELECT_WORKOUT_LAST_WEEK = "SELECT * " +
            "FROM " + DataSummary.TABLE_NAME + " ds INNER JOIN " + " WHERE " +
            "ds." + DataSummary.COLUMN_DATE + " like ?";

    private AppDataBaseCreateTable() {
    }

    public static class DataSummary implements BaseColumns {
        public static final String TABLE_NAME = "datasummary";
        public static final String COLUMN_NAME = "locationname";
        public static final String COLUMN_DESCRIPTION = "score";
        public static final String COLUMN_DATE = "date";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_DATE + " INTEGER" + ")";
    }

    public static class Location implements BaseColumns {
        public static final String TABLE_NAME = "location";
        public static final String COLUMN_NAME = "locationname";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGTITUDE = "longtitude";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_LATITUDE + " INTEGER, " +
                COLUMN_LONGTITUDE + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_NAME + ") REFERENCES " +
                DataSummary.TABLE_NAME + "(" + DataSummary.COLUMN_NAME + ") " + ")";

    }

    public static class WeatherHour implements BaseColumns {
        public static final String TABLE_NAME = "weatherhour";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TEMPERATURE = "temperature";
        public static final String COLUMN_WIND = "windspeed";
        public static final String COLUMN_HUMIDITY = "humidity";
        public static final String COLUMN_PRECIPITATION = "precipitation";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " INTEGER, " +
                COLUMN_TEMPERATURE + " INTEGER, " +
                COLUMN_WIND + " INTEGER, " +
                COLUMN_HUMIDITY + " INTEGER, " +
                COLUMN_PRECIPITATION + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_DATE + ") REFERENCES " +
                DataSummary.TABLE_NAME + "(" + DataSummary.COLUMN_DATE + ") " + ")";

    }
}