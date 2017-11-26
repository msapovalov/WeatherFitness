package com.ucd.user.weatherfitness;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.ucd.user.weatherfitness.model.FetchWeatherTask;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    //db vars
    public DBAdapter myDb;

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
        //delete all data on create
        //myDb.deleteAll();

        //main activity buttons
        Button btn = findViewById(R.id.button1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(MainActivity.this, MapsActivity.class);
                MainActivity.this.startActivityForResult(newIntent, 1);
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
                MainActivity.this.onClick_StartNow();
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

    public void openDB(){
        myDb = new DBAdapter(this);
        myDb.open();
        //myDb.deleteAll();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                String returnValue = data.getStringExtra("lat");
                String returnValue2 = data.getStringExtra("lng");

                TextView score_id = findViewById(R.id.score_ID);
                FetchWeatherTask weatherTask = new FetchWeatherTask(score_id);
                weatherTask.execute(returnValue, returnValue2);

            }
        }
    }
//
    public void onClick_StartNow() {
        Calendar cal = Calendar.getInstance();
        //Date in simpleformat
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtimestamp =format1.format(cal.getTime());
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

        myDb.insertRow(strtimestamp,location, score, wind, precipitation, temperature, pressure, lat, lon);
        Toast.makeText(getApplicationContext(), "Your activity have been added to Database", Toast.LENGTH_SHORT).show();


    }
    public void populateListView() {
        Cursor cursor = myDb.getAllRows();
        String[] fromFieldNames = new String[]{DBAdapter.KEY_ROWID, DBAdapter.KEY_DATETIME, DBAdapter.KEY_LOCATION, DBAdapter.KEY_SCORE};
        int[] toViewIDs = new int[]{R.id.TextViewID, R.id.TextViewDate, R.id.TextViewLocation, R.id.TextViewScore};
        SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.item_layout, cursor, fromFieldNames, toViewIDs, 0);
        ListView myList = (ListView) findViewById(R.id.Listview_fragment2);
        myList.setAdapter(myCursorAdapter);

    }
}

