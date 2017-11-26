package com.ucd.user.weatherfitness;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;




public class MainActivity extends AppCompatActivity {

    //db vars
    public DBAdapter myDb;
    String location = " ";
    private FusedLocationProviderClient mFusedLocationClient;
    String lng = "-6";
    String lat = "53";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                Log.d("LocationtoString", location.toString());
                                lng = String.valueOf(location.getLongitude());
                                lat = String.valueOf(location.getLatitude());
                                Toast toast = Toast.makeText(MainActivity.this,"location added",Toast.LENGTH_SHORT);
                                toast.show();
                                // Got last known location. In some rare situations this can be null.
                                if (location == null) {
                                    toast = Toast.makeText(MainActivity.this,"location null",Toast.LENGTH_SHORT);
                                    toast.show();
                                    // Logic to handle location object
                                }
                            }});

        //current weather implementation code
        TextView score_id = findViewById(R.id.score_ID);
        FetchWeatherTask weatherTask = new FetchWeatherTask(score_id);
        //hardcoded to Wicklow
        weatherTask.execute(lat,lng);
        try {
            Double latitude = Double.parseDouble(lat);
            Double longtitude = Double.parseDouble(lng);

            Geocoder geo = new Geocoder(this);
            List<Address> addressList = geo.getFromLocation(latitude, longtitude, 1);
            String address = addressList.get(0).getLocality();

            location = String.valueOf(address);
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }

        //sqlite db implementation
        openDB();

        //main activity buttons
        Button btn = findViewById(R.id.button1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(MainActivity.this, MapsActivity.class);
                MainActivity.this.startActivityForResult(newIntent,1);
            }
        });

        Button btn1 = findViewById(R.id.button2);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEventToCal objEvent = new AddEventToCal(MainActivity.this);
                try {
                    objEvent.AddEvent(MainActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }

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

    //sqllite methods//

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
                String returnValue3 = data.getStringExtra("address");
                location = returnValue3;
                TextView score_id = findViewById(R.id.score_ID);
                FetchWeatherTask weatherTask = new FetchWeatherTask(score_id);
                weatherTask.execute(returnValue, returnValue2);

            }
        }
    }

    public void onClick_StartNow() {
        Calendar cal = Calendar.getInstance();
        //Date in simpleformat
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strtimestamp =format1.format(cal.getTime());
         /// need to implement this method
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

///