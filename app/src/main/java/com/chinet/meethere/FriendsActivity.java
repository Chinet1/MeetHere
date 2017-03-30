package com.chinet.meethere;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FriendsActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        Bundle bundle = getIntent().getExtras();

        int userID = bundle.getInt("userID");

        DatabaseHandler db = new DatabaseHandler(this);

        List<User> friendsList = db.getAllFriends(userID);

        listView = (ListView) findViewById(R.id.friendsListView);

        ArrayList<String> friendsL = new ArrayList<String>();

        for (User user : friendsList) {
            friendsL.add(user.getName() + " " + user.getSurname());
        }

        adapter = new ArrayAdapter<String>(this, R.layout.row, friendsL);

        listView.setAdapter(adapter);

        db.closeDB();
    }
}
