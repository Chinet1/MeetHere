package com.chinet.meethere;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

public class LocationUpdater extends Activity {

    private JobScheduler jobScheduler;
    private boolean updateOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
    }

    public void startBackgroundLocationUpdate() {
        JobInfo.Builder builder = new JobInfo.Builder( 1,
                new ComponentName(getPackageName(), JobSchedulerService.class.getName()));

        builder.setPeriodic(1000 * 3600);

        if(jobScheduler.schedule(builder.build())<=0)  {
            Log.d("LocationUpdater", "Something was wrong");
        }
        updateOn = true;
    }

    public void stopBackgroundLocationUpdate() {
        jobScheduler.cancelAll();
        updateOn = false;
    }

    public boolean isUpdateOn() {
        return updateOn;
    }
}
