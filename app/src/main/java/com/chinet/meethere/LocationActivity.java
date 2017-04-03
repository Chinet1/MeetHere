package com.chinet.meethere;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LocationActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        LocationTracker locationTracker = new LocationTracker(this);
        if (locationTracker.checkPermission()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
        }

        TextView locationInfo = (TextView) findViewById(R.id.locationInfoText);
        if (locationTracker.canGetLocation()) {
            locationTracker.getLocation();
            locationInfo.setText("Lat: " + locationTracker.getLatitude() + " Lon: "
                    + locationTracker.getLongitude());
        } else {
            locationInfo.setText("Error");
            System.out.println("Error while getLocation");
        }
        locationTracker.stopUsingGPS();
    }
}
