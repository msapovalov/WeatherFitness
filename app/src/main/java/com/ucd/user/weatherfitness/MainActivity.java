package com.ucd.user.weatherfitness;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    //db vars
    public DBAdapter myDb;
    public static String locationfromfetch = " ";
    public String lng = "";
    public String lat = " ";
    public static int score = 0;
    public static double wind = 0;
    public static String precipitation = " ";
    public static double temperature = 0;
    public static double pressure = 0;
    private ViewFlipper vf;
    private  float lastX;
    String location = "";
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set activity view
        setContentView(R.layout.activity_main);

        GPSPermission gpsPermission;
        gpsPermission = new GPSPermission(MainActivity.this);

        if (!gpsPermission.checkLocationPermission()) {
            gpsPermission.requestPermissionForLocation(GPSPermission.LOCATION_PERMISSION_REQUEST_CODE);
        }
        else
        {
            //open database
            openDB();

            //Get GPS location
            gps = new GPSTracker(MainActivity.this);
            if (gps.canGetLocation()) {
                lat = String.valueOf(gps.getLatitude());
                lng = String.valueOf(gps.getLongitude());
                //toast for troubleshooting purposes
                //Toast.makeText(
                  //      getApplicationContext(),
                    //    "Your Location is -\nLat: " + lat + "\nLong: "
                      //          + lng, Toast.LENGTH_LONG).show();

                //create flipper view
                vf = findViewById(R.id.myflipper);

                //try to fetch current weather and calculate score
                TextView score_id = findViewById(R.id.location_view);
                FetchWeatherTask weatherTask = new FetchWeatherTask(score_id);

                // Waiting on async task dangerous
                try {
                    weatherTask.execute(lat, lng).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

                TextView iscore = findViewById(R.id.score_ID);
                String strscore = "SCORE     " + score;
                iscore.setText(strscore);

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
            else {
                gps.showSettingsAlert(); //show alert and ask user to turn location on
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case GPSPermission.LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    //open database
                    openDB();

                    //Get GPS location
                    gps = new GPSTracker(MainActivity.this);
                    if (gps.canGetLocation()) {
                        lat = String.valueOf(gps.getLatitude());
                        lng = String.valueOf(gps.getLongitude());
                        //Test toast for tests
                        // Toast.makeText(
                          //      getApplicationContext(),
                            //    "Your Location is -\nLat: " + lat + "\nLong: "
                              //          + lng, Toast.LENGTH_LONG).show();

                    //create flipper view
                    vf = findViewById(R.id.myflipper);

                    //try to fetch current weather and calculate score
                    TextView score_id = findViewById(R.id.location_view);
                    FetchWeatherTask weatherTask = new FetchWeatherTask(score_id);

                    // Waiting on async task dangerous
                    try {
                        weatherTask.execute(lat, lng).get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }

                    TextView iscore = findViewById(R.id.score_ID);
                    String strscore = "SCORE     " + score;
                    iscore.setText(strscore);

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
         else {
            gps.showSettingsAlert(); //show alert and ask user to turn location on
        }
    } else {
        Toast.makeText(getApplicationContext(), "Permission denied. GPS required for application to work", Toast.LENGTH_SHORT).show();
    }
                break;
}}

    //Sqllite method to open connection to db//
    public void openDB(){
        myDb = new DBAdapter(this);
        myDb.open();
    }


    //return from Map Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                String returnValue = data.getStringExtra("lat");
                String returnValue2 = data.getStringExtra("lng");

                //location = String.valueOf(address);
                location = data.getStringExtra("location");

                TextView score_id = findViewById(R.id.location_view);
                FetchWeatherTask weatherTask = new FetchWeatherTask(score_id);
                try {
                    weatherTask.execute(returnValue, returnValue2).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

                TextView iscore = findViewById(R.id.score_ID);
                String strscore = "SCORE     "+ score;
                iscore.setText(strscore);
                if (score <= 4) {
                    iscore.setTextColor(Color.RED);
                }
                else if  (score <= 7) {
                    iscore.setTextColor(Color.BLUE);
                }
                else {
                        iscore.setTextColor(Color.GREEN);
                    }

            }
        }
    }

    public void onClick_StartNow() {
        Calendar cal = Calendar.getInstance();
        //Date using SimpleDateFormat constructor
        //added Locale.getDefault so that Date and Time presented in User preferred format.
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String strtimestamp =format1.format(cal.getTime());
        myDb.insertRow(strtimestamp,locationfromfetch, String.valueOf(score), String.valueOf(wind), precipitation, String.valueOf(temperature), String.valueOf(pressure), lat, lng);
        Toast.makeText(getApplicationContext(), "Your activity saved to Database", Toast.LENGTH_SHORT).show();
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

    // Method to handle touch events for flipper like left to right swap and right to left swap
    public boolean onTouchEvent(MotionEvent touchevent)
    {
        switch (touchevent.getAction())
        {
            // when user first touches the screen to swap
            case MotionEvent.ACTION_DOWN:
            {
                lastX = touchevent.getX();
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                float currentX = touchevent.getX();

                // if left to right swipe on screen
                if (lastX < currentX)
                {
                    // If no more View/Child to flip
                    if (vf.getDisplayedChild() == 0)
                        break;

                    // set the required Animation type to ViewFlipper
                    // The Next screen will come in form Left and current Screen will go OUT from Right
                    vf.setInAnimation(this, R.anim.in_from_left);
                   vf.setOutAnimation(this, R.anim.out_to_right);
                    // Show the next Screen
                    vf.showNext();
                }

                /// if right to left swipe on screen
                if (lastX > currentX)
                {
                    if (vf.getDisplayedChild() == 1)
                        break;
                    // set the required Animation type to ViewFlipper
                    // The Next screen will come in form Right and current Screen will go OUT from Left
                    vf.setInAnimation(this, R.anim.in_from_right);
                    vf.setOutAnimation(this, R.anim.out_to_left);
                    // Show The Previous Screen
                    vf.showPrevious();
                }
                break;
            }
        }
        return false;
    }
}
