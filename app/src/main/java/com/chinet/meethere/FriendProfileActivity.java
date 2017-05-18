package com.chinet.meethere;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class FriendProfileActivity extends AppCompatActivity {

    private String url;
    private UserHelper userHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        Bundle bundle = getIntent().getExtras();

        int friendId = Integer.parseInt(bundle.getString("friendId"));

        url = "http://chinet.cba.pl/meethere.php?user=" + friendId;

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
