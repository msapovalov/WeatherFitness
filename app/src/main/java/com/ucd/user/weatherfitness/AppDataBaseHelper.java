package com.ucd.user.weatherfitness;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mihhail on 18.11.2017
 */

public class AppDataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "weatherapp_database";

    public AppDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(AppDataBaseCreateTable.DataSummary.CREATE_TABLE);
        sqLiteDatabase.execSQL(AppDataBaseCreateTable.Location.CREATE_TABLE);
        sqLiteDatabase.execSQL(AppDataBaseCreateTable.WeatherHour.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + AppDataBaseCreateTable.DataSummary.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + AppDataBaseCreateTable.Location.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + AppDataBaseCreateTable.WeatherHour.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}