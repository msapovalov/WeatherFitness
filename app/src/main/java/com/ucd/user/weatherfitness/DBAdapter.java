package com.ucd.user.weatherfitness;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class DBAdapter {

    private static final String LOG_TAG = "DBAdapter"; //logging DB version

    // Field Names
    public static final String KEY_ROWID = "_id";
    public static final String KEY_DATETIME = "datetime";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_SCORE = "score";
    public static final String KEY_WIND = "wind";
    public static final String KEY_PRECIPITATION = "precipitation";
    public static final String KEY_TEMPERATURE = "temperature";
    public static final String KEY_PRESSURE = "pressure";
    public static final String KEY_LAT = "lat";
    public static final String KEY_LON = "lon";


    public final String[] ALL_KEYS = new String[]{KEY_ROWID, KEY_DATETIME, KEY_LOCATION, KEY_SCORE,KEY_WIND, KEY_PRECIPITATION, KEY_TEMPERATURE, KEY_PRESSURE, KEY_LAT, KEY_LON};


    //DB info:

    public static final String DATABASE_NAME = "db";
    public static final String DATABASE_TABLE = "weather";
    public static final int DATABASE_VERSION = 3;

    //SQL Statement to create DB

    private static final String DATABASE_CREATE_SQL = "create table "
            + DATABASE_TABLE
            + " ("
            + KEY_ROWID + " integer primary key autoincrement, "
            + KEY_DATETIME + " text, "
            + KEY_LOCATION + " text, "
            + KEY_LAT + " text not null, "
            + KEY_LON + " text not null, "
            + KEY_WIND + " text not null, "
            + KEY_PRECIPITATION + " text not null, "
            + KEY_TEMPERATURE + " text not null, "
            + KEY_PRESSURE + " text not null, "
            + KEY_SCORE + " text"
            + ");";

    private final Context context;
    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    public DBAdapter open () {
        db = myDBHelper.getWritableDatabase();
        return this;
    }
    public DBAdapter openread() {
        db = myDBHelper.getReadableDatabase();
        return this;
    }

    public void close() {
        myDBHelper.close();
    }

    //insert a row to db

    public long insertRow(String datetime, String location, String score, String wind, String precipitation, String temperature, String pressure, String lat, String lon) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DATETIME, String.valueOf(datetime));
        initialValues.put(KEY_LOCATION, location);
        initialValues.put(KEY_SCORE, score);
        initialValues.put(KEY_WIND, wind);
        initialValues.put(KEY_PRECIPITATION, precipitation);
        initialValues.put(KEY_TEMPERATURE, temperature);
        initialValues.put(KEY_PRESSURE, pressure);
        initialValues.put(KEY_LAT, lat);
        initialValues.put(KEY_LON, lon);

        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //delete a row in db

    public boolean deleteRow(long rowID) {
        String where = KEY_ROWID + "=" + rowID;
        return db.delete(DATABASE_TABLE, where, null) != 0;
    }

    public Cursor deleteAll() {
        Cursor c = getAllRows();
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
        if (c.moveToFirst()) {
            do {
                deleteRow(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
        return c;
    }

    //select all
    public Cursor getAllRows() {
        Cursor c = db.rawQuery("SELECT * FROM weather", null);
            if (c != null) {
                c.moveToFirst();
            }
        return c;
    }

    //select single row

    public Cursor getRow(String datetime) {
        Cursor c = db.rawQuery("SELECT _id, datetime, location, score FROM weather WHERE datetime > ?", new String[]{datetime});
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    //change row

    public boolean updateRow(long rowID, String datetime, String location, String score, String wind, String precipitation, String temperature, String pressure, String lat, String lon) {
        String where = KEY_ROWID + "=" + rowID;
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_DATETIME, datetime);
        newValues.put(KEY_LOCATION, location);
        newValues.put(KEY_SCORE, score);
        newValues.put(KEY_WIND, wind);
        newValues.put(KEY_PRECIPITATION, precipitation);
        newValues.put(KEY_TEMPERATURE, temperature);
        newValues.put(KEY_PRESSURE, pressure);
        newValues.put(KEY_LAT, lat);
        newValues.put(KEY_LON, lon);
        return  db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper{
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "UPgrading application database from verion" + oldVersion + "to " + newVersion + ", which will destroy all data!");
            //drop old db
            _db.execSQL(String.format("DROP TABLE IF EXISTS %s", DATABASE_TABLE));
            //create newdb
            onCreate(_db);


        }

    }
}



    
    
    