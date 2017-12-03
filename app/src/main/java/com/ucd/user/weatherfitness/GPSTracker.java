package com.ucd.user.weatherfitness;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

//Class to get current location from GPS sensor
public class GPSTracker extends Service implements LocationListener{

    private final Context context;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    Location location;

    double latitude;
    double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    protected LocationManager locationManager;

    public GPSTracker(Context context) {
        this.context = context;
        getLocation();
    }
    //Get location
    @SuppressLint("MissingPermission")
    public Location getLocation() {
        try {
            //checking if location is enabled
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            //checking if GPS sensor is enabled    
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            //checking if Network is enabled 
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(!isGPSEnabled && !isNetworkEnabled) {//GPS and Network are enabled

            } else {
                this.canGetLocation = true;//we can get location 

                if (isNetworkEnabled) {//request location via Network

                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null) {

                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }

                }

                if(isGPSEnabled) {//request location via GPS
                    if(location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if(locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);//We also can get a last known location if location is null

                            if(location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    //stop using GPS if location is found
    public void stopUsingGPS() {
        if(locationManager != null) {
            locationManager.removeUpdates(GPSTracker.this);
        }
    }
    //get latitude
    public double getLatitude() {
        if(location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }
    //get longitude
    public double getLongitude() {
        if(location != null) {
            longitude = location.getLongitude();
        }

        return longitude;
    }
    //boolean that we can get location
    public boolean canGetLocation() {
        return this.canGetLocation;
    }
    //alert to user that GPS is not enabled, asking user to enable it and open Location settings if yes
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setTitle("GPS settings");

        alertDialog.setMessage("GPS is not enabled and app will not work properly. Do you want to go to Settings menu and enable it?");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }
    //standard autogenerated methods    
    @Override
    public void onLocationChanged(Location arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

}
