package com.chinet.meethere;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class WebServiceHelper {

    private String url;
    private String operation;
    private String[][] user;

    public String makeDBOperation(String url) {

        this.url = url;

        try {
            operation = new dbOperation(this).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return operation;
    }

    public String[][] getFriends(String url) {

        this.url = url;

        try {
            user = new GetFriends(this).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return user;
    }

    private class dbOperation extends AsyncTask<String, Void, String> {

        private static final String TAG = "dbOperation";
        private WebServiceHelper webServiceHelper;

        private dbOperation(WebServiceHelper webServiceHelper) {
            this.webServiceHelper = webServiceHelper;
        }


        @Override
        protected String doInBackground(String... strings) {

            WebServiceHandler webServiceHandler = new WebServiceHandler();
            String jsonStr = webServiceHandler.makeServiceCall(url);

            Log.d(TAG, "Response form url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    String response = jsonObject.getString("operation");
                    String id = jsonObject.getString("id");

                    if (Objects.equals(response, "success")) {
                        return "success";
                    } else {
                        return id;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return "fail";
        }
    }

    private class GetFriends extends AsyncTask<String[][], Void, String[][]> {

        private static final String TAG = "GetFriends";
        private WebServiceHelper webServiceHelper;

        private GetFriends(WebServiceHelper webServiceHelper) {
            this.webServiceHelper = webServiceHelper;
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
