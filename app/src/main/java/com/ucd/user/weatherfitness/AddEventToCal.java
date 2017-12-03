package com.ucd.user.weatherfitness;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.util.Calendar;

import static com.ucd.user.weatherfitness.MainActivity.locationfromfetch;

/**
 * Created by User on 11/19/2017.
 */

public class AddEventToCal extends Activity {

    private Context context;

    public AddEventToCal(Context context) {
        this.context = context;
    }

    public void AddEvent(Context context) throws Exception {

        ContentResolver cr = context.getContentResolver();
        ContentValues cv = new ContentValues();
        cv.put(CalendarContract.Events.TITLE, "WeatherFitness");
        cv.put(CalendarContract.Events.DESCRIPTION, "Event created by WeatherFitness app");
        cv.put(CalendarContract.Events.EVENT_LOCATION, "Location" + locationfromfetch);
        cv.put(CalendarContract.Events.DTSTART, Calendar.getInstance().getTimeInMillis());
        cv.put(CalendarContract.Events.DTEND, Calendar.getInstance().getTimeInMillis() + 60 * 60 * 1000);
        cv.put(CalendarContract.Events.CALENDAR_ID, "3");
        cv.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance().getTimeZone().getID());
        if (context.checkSelfPermission(Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_CALENDAR}, 0);
        }
        else {

            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, cv);
            Toast.makeText(context, "Event Added to calendar", Toast.LENGTH_SHORT).show();
        }

    }

}