package com.chinet.meethere;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static com.chinet.meethere.SaveSharedPreference.getSharedPreferences;

public class FacebookLogin extends Fragment {

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private boolean postingEnabled = false;
    private ProfileTracker profileTracker;
    private TextView greeting;
    private String url;
    private String emailCheckStatus;
    private FacebookLogin instance;
    private String lastLocalization;
    private String addingNewUser;
    private int userId;
    private AccessTokenTracker fbTracker;
    private WebServiceHelper webServiceHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;

        FacebookSdk.sdkInitialize(getActivity());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.facebook_login, parent, false);
        greeting = (TextView) v.findViewById(R.id.greeting);

        loginButton = (LoginButton) v.findViewById(R.id.login_button);
        loginButton.setFragment(this);
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_location"));
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Toast toast = Toast.makeText(getActivity(), "Logged In", Toast.LENGTH_SHORT);
            postingEnabled = true;
            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    String email = object.getString("email");
                    String birthdaySource = object.getString("birthday");
                    String[] date = birthdaySource.split("/");
                    String birthday = date[2] + ", " + date[1] + ", " + date[0];
                    String city = object.getJSONObject("location").getString("name");
                    url = "http://chinet.cba.pl/meethere.php?thatEmail=" + email
                            + "&key=" + getString(R.string.key_web_service);
                    webServiceHelper = new WebServiceHelper();
                    emailCheckStatus = webServiceHelper.makeDBOperation(url);
                    if (Objects.equals(emailCheckStatus, "success")) {
                        LocationTracker locationTracker = LocationTracker.getInstance();

                        if (locationTracker.canGetLocation()) {
                            lastLocalization = locationTracker.getLatitude()
                                    + ", " + locationTracker.getLongitude();
                        }
                        locationTracker.stopUsingGPS();

                        url = "http://chinet.cba.pl/meethere.php?addUser="
                                + first_name + "&surname=" + last_name + "&email="
                                + email + "&city=" + city + "&dayOfBirthday="
                                + birthday + "&lastLocalization=" + lastLocalization
                                + "&key=" + getString(R.string.key_web_service);

                        addingNewUser = webServiceHelper.makeDBOperation(url);
                        userId = Integer.parseInt(addingNewUser);
                    } else {
                        Log.d("Email check", "fail");
                        userId = Integer.parseInt(emailCheckStatus);
                        }

                        SaveSharedPreference.setPrefId(getContext(), userId);

                        Log.d("UserFacebookData", email + birthday + city);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,first_name,last_name,birthday,location,link,email");
            request.setParameters(parameters);
            request.executeAsync();

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile"));
                    if(!postingEnabled) {
                        postingEnabled = true;
                    }else{
                        postingEnabled = false;

                    }
                }
            });

            toast.show();
            updateUI();
            }


            @Override
            public void onCancel() {
                updateUI();
            }

            @Override
            public void onError(FacebookException exception) {
                updateUI();

            }
        });

        fbTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken2) {
                if (accessToken2 == null) {
                    Log.d("FB", "User Logged Out.");
                    SharedPreferences.Editor editor = getSharedPreferences(getContext()).edit();
                    editor.clear();
                    editor.commit();
                }
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                updateUI();
            }
        };

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Call the 'activateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onResume methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.activateApp(getActivity());

        updateUI();
    }

    @Override
    public void onPause() {
        super.onPause();

        // Call the 'deactivateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onPause methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.deactivateApp(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }

    private void updateUI() {
        boolean enableButtons = AccessToken.getCurrentAccessToken() != null;

        Profile profile = Profile.getCurrentProfile();
        if (enableButtons && profile != null) {
            greeting.setText(getString(R.string.hello_user, profile.getFirstName()));
        } else {
            greeting.setText(null);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}