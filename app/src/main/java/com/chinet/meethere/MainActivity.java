package com.chinet.meethere;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private JobScheduler jobScheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationTrackerPermission();

        int user_id = SaveSharedPreference.getId(getApplicationContext());
        jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);

        if (user_id != 0) {
            JobInfo.Builder builder = new JobInfo.Builder( 1,
                    new ComponentName(getPackageName(), JobSchedulerService.class.getName()));

            builder.setPeriodic(1000 * 100);

            if(jobScheduler.schedule(builder.build())<=0)  {
                Log.d("LocationUpdater", "Something was wrong");
            }
        } else {
            jobScheduler.cancelAll();
        }
    }

    private void locationTrackerPermission() {
        LocationTracker locationTracker = LocationTracker.getInstance();
        locationTracker.init(this);
        if (locationTracker.checkPermission()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
        if (locationTracker.checkPermission()) {
            Toast.makeText(this, "You must allow localization to use all functions.", Toast.LENGTH_LONG).show();
        }
    }

    public void goLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void goUserView(View view) {
        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
    }

    public void goLocationActivity(View view) {
        Intent intent = new Intent(this, LocationActivity.class);
        startActivity(intent);
    }


    public void goMapsActivity(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void goSearchActivity(View view) {
        Intent intent = new Intent(this, SearchFriendActivity.class);
        startActivity(intent);
    }
}
