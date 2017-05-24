package com.chinet.meethere;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SearchFriendActivity extends AppCompatActivity {

    private String url;
    private String[][] user;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private String[][] list;
    private EditText eName;
    private EditText eSurname;
    private ArrayList<String> friends;
    private WebServiceHelper webServiceHelper;
    private int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);
        userId = SaveSharedPreference.getId(getApplicationContext());

        if (userId == 0) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        } else {
            eName = (EditText) findViewById(R.id.nameText);
            eSurname = (EditText) findViewById(R.id.surnameText);
            listView = (ListView) findViewById(R.id.searchFriendsList);

            listView.setOnItemClickListener(itemClicked);
        }
    }

    private AdapterView.OnItemClickListener itemClicked =  new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView adapterView, View view, int position, long id) {
            Intent intent = new Intent(SearchFriendActivity.this, FriendProfileActivity.class);
            intent.putExtra("friendId", list[position][0]);
            startActivity(intent);
            Log.d("ItemClick", list[position][0]);
        }
    };

    public void searchFriend(View view) {

        String name = eName.getText().toString();
        String surname = eSurname.getText().toString();
        url = "http://chinet.cba.pl/meethere.php?search=" + name + "&surname=" + surname
                + "&key=" + getString(R.string.key_web_service);
        webServiceHelper = new WebServiceHelper();
        list = webServiceHelper.getFriends(url);

        friends = new ArrayList<String>();

        for (int i=0; i<list.length; i++) {
            if (Integer.parseInt(list[i][0]) != userId) {
                friends.add(list[i][1] + " " + list[i][2]);
            }
        }

        adapter = new ArrayAdapter<String>(this, R.layout.row, friends);

        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);
    }
}
