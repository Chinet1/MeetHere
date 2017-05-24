package com.chinet.meethere;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FriendProfileActivity extends AppCompatActivity {

    private String url;
    private UserHelper userHelper;
    private int userId;
    private int friendId;
    private WebServiceHelper webServiceHelper;
    private Button buttonAddUser;
    private String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);

        userId = SaveSharedPreference.getId(getApplicationContext());

        if (userId == 0) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        } else {
            Bundle bundle = getIntent().getExtras();

            friendId = Integer.parseInt(bundle.getString("friendId"));

            webServiceHelper = new WebServiceHelper();
            buttonAddUser = (Button) findViewById(R.id.add_user_button);
            url = "http://chinet.cba.pl/meethere.php?idToCheck=" + friendId +"&userId=" + userId
                    + "&key=" + getString(R.string.key_web_service);
            response = webServiceHelper.makeDBOperation(url);

            if (response == "success") {
                buttonAddUser.setText("Delete friend");
            } else {
                buttonAddUser.setText("Add friend");
            }

            url = "http://chinet.cba.pl/meethere.php?user=" + friendId
                    + "&key=" + getString(R.string.key_web_service);

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

    public void addFriend(View view) {
        url = "http://chinet.cba.pl/meethere.php?idToCheck=" + friendId +"&userId=" + userId
                + "&key=" + getString(R.string.key_web_service);
        response = webServiceHelper.makeDBOperation(url);
        if (response == "success") {
            url = "http://chinet.cba.pl/meethere.php?delFriend=" + userId + "&userId=" + friendId
                    + "&key=" + getString(R.string.key_web_service);
            webServiceHelper.makeDBOperation(url);
            buttonAddUser.setText("Add friend");
            Toast.makeText(this, "User delete from friend list!", Toast.LENGTH_LONG).show();
        } else if (userId == Integer.parseInt(response)) {
            url = "http://chinet.cba.pl/meethere.php?addFriend=" + userId + "&friend=" + friendId
                    + "&key=" + getString(R.string.key_web_service);
            webServiceHelper.makeDBOperation(url);
            buttonAddUser.setText("Delete friend");
            Toast.makeText(this, "Added new friend!", Toast.LENGTH_LONG).show();
        } else {
            Log.d("FPA", "fail in addUser");
        }
    }
}
