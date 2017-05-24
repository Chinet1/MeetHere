package com.chinet.meethere;

import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class UserActivity extends AppCompatActivity {

    private int userId;
    private String url;
    private UserHelper userHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        userId = SaveSharedPreference.getId(getApplicationContext());

        if (userId == 0) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        } else {

            url = "http://chinet.cba.pl/meethere.php?user=" + userId
                    + "&key=" + getString(R.string.key_web_service);
            Log.d("UserActivity", url);

            userHelper = new UserHelper();

            String[] userData = userHelper.getUserFromWS(url);

            TextView nameText = (TextView) findViewById(R.id.nameText);
            nameText.setText(userData[0]);

            TextView ageText = (TextView) findViewById(R.id.ageText);
            ageText.setText(userData[1]);

            TextView cityText = (TextView) findViewById(R.id.cityText);
            cityText.setText(userData[2]);
        }
    }

    public void goFriends(View view) {
        Intent intent = new Intent(this, FriendsActivity.class);
        intent.putExtra("userID", userId);
        startActivity(intent);
    }
}