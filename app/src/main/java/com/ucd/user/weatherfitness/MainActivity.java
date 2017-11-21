package com.ucd.user.weatherfitness;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.ucd.user.weatherfitness.model.FetchWeatherTask;


public class MainActivity extends AppCompatActivity {


    //db vars
    Time today = new Time(Time.getCurrentTimezone());
    DBAdapter myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         //current weather implementation code
        TextView score_id = findViewById(R.id.score_ID);
        FetchWeatherTask weatherTask = new FetchWeatherTask(score_id);
        weatherTask.execute("7778677");

        //sqlite db implementation

        openDB();
        populateListView();

        //main activity buttons
        Button btn = findViewById(R.id.button1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(MainActivity.this, MapsActivity.class);
                MainActivity.this.startActivity(newIntent);
            }
        });

        Button btn1 = findViewById(R.id.button2);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button btn2 = findViewById(R.id.button3);
        btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

            }
        });

        Button btn3 = findViewById(R.id.button4);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(MainActivity.this, HistoryActivity.class);
                MainActivity.this.startActivity(newIntent);
            }
        });

    }

    //sqllite methods

    private void openDB(){
        myDb = new DBAdapter(this);
        myDb.open();

    }

    public void onClick_StartNow (View v) {
        today.setToNow();
        String timestamp = today.format("%Y-%m-%d %H:%M:%S");
        String location = "Current Address"; /// need to implement this method
        String score = "5"; //score algorithm (Sam)
        //We need to parse through openweathermap json and get these values, same passed to score algorithm
        //possibly modify FetchWeatherClass?

        String wind = "10 m/s";
        String precipitation = "snow";
        String temperature = "10";
        String pressure = "1007";
        String lat = "53.305344";
        String lon = "-6.220654";

        myDb.insertRow(timestamp,location, score, wind, precipitation, temperature, pressure, lat, lon);
        populateListView();

    }

    private void populateListView() {
        Cursor cursor = myDb.getAllRows();
        String[] fromFieldNames = new String[]{DBAdapter.KEY_ROWID, DBAdapter.KEY_DATETIME, DBAdapter.KEY_LOCATION, DBAdapter.KEY_SCORE};
        int[] toViewIDs = new int[]{R.id.TextViewID, R.id.TextViewDate, R.id.TextViewLocation, R.id.TextViewScore};
        SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.item_layout, cursor, fromFieldNames, toViewIDs, 0);
        ListView myList = (ListView) findViewById(R.id.Listview_fragment2);
        myList.setAdapter(myCursorAdapter);
    }
}

