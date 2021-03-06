package com.chinet.meethere;


import android.os.AsyncTask;
import android.util.Log;

import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class UserHelper {

    private String[] user;
    private String url;

    public String[] getUserFromWS(String url) {

        this.url = url;

        try {
            user = new UserHelper.GetUser(this).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        String[] userData = getUserData();

        return userData;
    }

    private String[] getUserData() {
        String name = user[1] + " " + user[2];
        Integer age = calculateYears(user[5]);
        String city = "City: " + user[4];
        String ageString = "Age: " + age;

        String[] userData = new String[3];
        userData[0] = name;
        userData[1] = ageString;
        userData[2] = city;
        return userData;
    }

    private Integer calculateYears(String dataFrom) {
        String[] parts = dataFrom.split(", ");
        LocalDate df = new LocalDate(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]),
                Integer.parseInt(parts[2]));
        LocalDate now = new LocalDate();
        Years yearsIns = Years.yearsBetween(df, now);
        Integer years = yearsIns.getYears();
        return years;
    }

    private class GetUser extends AsyncTask<String, Void, String[]> {

        private static final String TAG = "GetUser";
        private UserHelper userHelper;

        private GetUser(UserHelper userHelper) {
            this.userHelper = userHelper;
        }

        @Override
        protected String[] doInBackground(String... params) {

            WebServiceHandler webServiceHandler = new WebServiceHandler();
            String jsonStr = webServiceHandler.makeServiceCall(url);

            Log.d(TAG, "Response form url: " + jsonStr);

            String[] user = new String[8];

            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);

                    JSONObject u = jsonObject.getJSONObject("user");

                    user[0] = u.getString("ID");
                    user[1] = u.getString("name");
                    user[2] = u.getString("surname");
                    user[3] = u.getString("email");
                    user[4] = u.getString("city");
                    user[5] = u.getString("dayOfBirthday");
                    user[6] = u.getString("lastLocalization");
                    user[7] = u.getString("createdAt");

                } catch (JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
            }

            return user;
        }

        @Override
        protected void onPostExecute(String[] user) {
        }
    }
}
