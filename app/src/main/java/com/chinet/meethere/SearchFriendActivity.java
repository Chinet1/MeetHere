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
    ArrayList<String> friends;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);

        eName = (EditText)findViewById(R.id.nameText);
        eSurname = (EditText)findViewById(R.id.surnameText);
        listView = (ListView) findViewById(R.id.searchFriendsList);

        listView.setOnItemClickListener(itemClicked);
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
        url = "http://chinet.cba.pl/meethere.php?search=" + name + "&surname=" + surname;

        try {
            list = new SearchFriends(this).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        friends = new ArrayList<String>();

        for (int i=0; i<list.length; i++) {
            friends.add(list[i][1] + " " + list[i][2]);
        }

        adapter = new ArrayAdapter<String>(this, R.layout.row, friends);

        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);
    }

    private class SearchFriends extends AsyncTask<String[][], Void, String[][]> {

        private static final String TAG = "SearchFriends";
        SearchFriendActivity searchFriendActivity;

        public SearchFriends(SearchFriendActivity searchFriendActivity) {
            this.searchFriendActivity = searchFriendActivity;
        }

        @Override
        protected String[][] doInBackground(String[][]... strings) {

            WebServiceHandler webServiceHandler = new WebServiceHandler();
            String jsonStr = webServiceHandler.makeServiceCall(url);
            String[] list;

            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);

                    JSONArray u = jsonObject.getJSONArray("user");

                    user = new String[u.length()][3];
                    Log.d(TAG, u.getJSONObject(0).getString("ID"));
                    for (int i=0; i<u.length(); i++) {
                        user[i][0] = u.getJSONObject(i).getString("ID");
                        user[i][1] = u.getJSONObject(i).getString("name");
                        user[i][2] = u.getJSONObject(i).getString("surname");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
            }

            return user;
        }

        @Override
        protected void onPostExecute(String[][] lists) {

        }
    }
}
