package com.chinet.meethere;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class LocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        LocationTracker locationTracker = LocationTracker.getInstance();

        locationTracker.init(this);

        TextView locationInfo = (TextView) findViewById(R.id.locationInfoText);
        if (locationTracker.canGetLocation()) {
            locationTracker.getLocation();
            locationInfo.setText("Lat: " + locationTracker.getLatitude() + " Lon: "
                    + locationTracker.getLongitude() + " Name: " + locationTracker.getLocationName());
        } else {
            locationInfo.setText("Error");
            Log.d("Location","Error while getLocation");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocationTracker locationTracker = LocationTracker.getInstance();
        locationTracker.stopUsingGPS();
        Log.d("Pause", "Stopped GPS");
    }

}
