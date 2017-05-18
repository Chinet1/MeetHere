package com.chinet.meethere;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private String url;
    private WebServiceHelper webServiceHelper;
    private ArrayList<String> friends;
    private String[][] list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        Bundle bundle = getIntent().getExtras();

        int userID = bundle.getInt("userID");
        url = "http://chinet.cba.pl/meethere.php?getFriends=" + userID;
        webServiceHelper = new WebServiceHelper();
        list = webServiceHelper.getFriends(url);

        listView = (ListView) findViewById(R.id.friendsListView);
        
        friends = new ArrayList<String>();

        for (int i=0; i<list.length; i++) {
            friends.add(list[i][1] + " " + list[i][2]);
        }

        adapter = new ArrayAdapter<String>(this, R.layout.row, friends);

        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);
    }
}
