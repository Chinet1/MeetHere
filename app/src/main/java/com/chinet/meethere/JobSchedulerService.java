package com.chinet.meethere;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class JobSchedulerService extends JobService {

    private String lastLocalization;
    private Handler jobHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message message) {
            LocationTracker locationTracker = LocationTracker.getInstance();
            if (locationTracker.canGetLocation()) {
                lastLocalization = locationTracker.getLatitude()
                        + ", " + locationTracker.getLongitude();
            }
            locationTracker.stopUsingGPS();
            int user_id = SaveSharedPreference.getId(getApplicationContext());
            String url = "http://chinet.cba.pl/meethere.php?saveLoc=" + user_id
                    + "&localization=" + lastLocalization
                    + "&key=" + getString(R.string.key_web_service);
            WebServiceHelper webServiceHelper = new WebServiceHelper();
            webServiceHelper.makeDBOperation(url);
            Log.d(JobSchedulerService.class.getSimpleName(), "JobService task running");
            jobFinished((JobParameters) message.obj, false);
            return true;
        }
    });

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        jobHandler.sendMessage(Message.obtain(jobHandler, 1, jobParameters));
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        jobHandler.removeMessages(1);
        return false;
    }
}
