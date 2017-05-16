package com.chinet.meethere;

import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class UserActivity extends AppCompatActivity {

    private int userId;
    private String url;
    private String[] user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        userId = SaveSharedPreference.getId(getApplicationContext());

        if (userId == 0) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        } else {

            url = "http://chinet.cba.pl/meethere.php?user=" + userId;
            Log.d("UserActivity", url);
            try {
                user = new GetUser(this).execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            String name = user[1] + " " + user[2];
            Integer age = calculateYears(user[5]);
            String city = "City: " + user[4];

            String ageString = "Age: " + age;

            TextView nameText = (TextView) findViewById(R.id.nameText);
            nameText.setText(name);

            TextView ageText = (TextView) findViewById(R.id.ageText);
            ageText.setText(ageString);

            TextView cityText = (TextView) findViewById(R.id.cityText);
            cityText.setText(city);
        }
    }

    public void goFriends(View view) {
        Intent intent = new Intent(this, FriendsActivity.class);
        intent.putExtra("userID", userId);
        startActivity(intent);
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
        private UserActivity userActivity;

        public GetUser(UserActivity userActivity){
            this.userActivity = userActivity;
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