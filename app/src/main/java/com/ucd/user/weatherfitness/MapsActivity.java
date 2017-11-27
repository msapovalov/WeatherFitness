package com.ucd.user.weatherfitness;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

/**
 * Created by samev on 07/11/2017.
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void onSearch(View view) throws IOException {
        EditText location_tf = (EditText) findViewById(R.id.address);
        String location = location_tf.getText().toString();

        if (location != null || !location.equals("")) {
            Geocoder geo = new Geocoder(this);
            List<Address> addressList = geo.getFromLocationName(location, 1);
            Address address = addressList.get(0);
            LatLng latlng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latlng).title("Marker on Search"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
            float zoomLevel = 16.0f; //This goes up to 21
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoomLevel));
        }
    }

    public void onSave(View view) throws IOException {
        EditText location_tf = (EditText) findViewById(R.id.address);
        String location = location_tf.getText().toString();
        Geocoder geo = new Geocoder(this);
        List<Address> addressList = geo.getFromLocationName(location, 1);
        Address address = addressList.get(0);

        Intent resultIntent = new Intent();
        // TODO Add extras or a data URI to this intent as appropriate.
        String lat = Double.toString(address.getLatitude());
        String lng = Double.toString(address.getLongitude());
        resultIntent.putExtra("lat", lat);
        resultIntent.putExtra("lng", lng);
        resultIntent.putExtra("location",location );

        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

}