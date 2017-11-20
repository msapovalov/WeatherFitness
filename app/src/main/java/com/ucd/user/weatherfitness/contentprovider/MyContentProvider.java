package com.ucd.user.weatherfitness.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import com.ucd.user.weatherfitness.database.DatabaseHelper;
import com.ucd.user.weatherfitness.database.WeatherTable;

public class MyContentProvider extends ContentProvider{

    private DatabaseHelper dbHelper;

    private static final int ALL_TIME = 1;
    private static final int SINGLE_TIME = 2;

    // authority is the symbolic name of your provider
    // To avoid conflicts with other providers, you should use
    // Internet domain ownership (in reverse) as the basis of your provider authority.
    private static final String AUTHORITY = "com.ucd.user.weatherfitness.contentprovider";

    // create content URIs from the authority by appending path to database table
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/weather");

    // a content URI pattern matches content URIs using wildcard characters:
    // *: Matches a string of any valid characters of any length.
    // #: Matches a string of numeric characters of any length.
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "weather", ALL_TIME);
        uriMatcher.addURI(AUTHORITY, "weather/#", SINGLE_TIME);
    }

    // system calls onCreate() when it starts up the provider.
    @Override
    public boolean onCreate() {
        // get access to the database helper
        dbHelper = new DatabaseHelper(getContext());
        return false;
    }

    //Return the MIME type corresponding to a content URI
    @Override
    public String getType(Uri uri) {

        switch (uriMatcher.match(uri)) {
            case ALL_TIME:
                return "vnd.android.cursor.dir/vnd.com.ucd.user.weatherfitness.contentprovider.weather";
            case SINGLE_TIME:
                return "vnd.android.cursor.item/vnd.com.ucd.user.weatherfitness.contentprovider.weather";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    // The insert() method adds a new row to the appropriate table, using the values
    // in the ContentValues argument. If a column name is not in the ContentValues argument,
    // you may want to provide a default value for it either in your provider code or in
    // your database schema.
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_TIME:
                //do nothing
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        long id = db.insert(WeatherTable.TABLE_WEATHER, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(CONTENT_URI + "/" + id);
    }

    // The query() method must return a Cursor object, or if it fails,
    // throw an Exception. If you are using an SQLite database as your data storage,
    // you can simply return the Cursor returned by one of the query() methods of the
    // SQLiteDatabase class. If the query does not match any rows, you should return a
    // Cursor instance whose getCount() method returns 0. You should return null only
    // if an internal error occurred during the query process.
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(WeatherTable.TABLE_WEATHER);

        switch (uriMatcher.match(uri)) {
            case ALL_TIME:
                //do nothing
                break;
            case SINGLE_TIME:
                String id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(WeatherTable.COLUMN_ID + "=" + id);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        return cursor;

    }

    // The delete() method deletes rows based on the seletion or if an id is
    // provided then it deleted a single row. The methods returns the numbers
    // of records delete from the database. If you choose not to delete the data
    // physically then just update a flag here.
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_TIME:
                //do nothing
                break;
            case SINGLE_TIME:
                String id = uri.getPathSegments().get(1);
                selection = WeatherTable.COLUMN_ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int deleteCount = db.delete(WeatherTable.TABLE_WEATHER, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return deleteCount;
    }

    // The update method() is same as delete() which updates multiple rows
    // based on the selection or a single row if the row id is provided. The
    // update method returns the number of updated rows.
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_TIME:
                //do nothing
                break;
            case SINGLE_TIME:
                String id = uri.getPathSegments().get(1);
                selection = WeatherTable.COLUMN_ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int updateCount = db.update(WeatherTable.TABLE_WEATHER, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }

}