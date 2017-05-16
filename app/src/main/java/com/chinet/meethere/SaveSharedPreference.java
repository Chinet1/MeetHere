package com.chinet.meethere;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static android.content.SharedPreferences.*;

public class SaveSharedPreference {

    static final String PREF_ID = "ID";

    static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setPrefId(Context context, int id) {
        Editor editor = getSharedPreferences(context).edit();
        editor.putInt(PREF_ID, id);
        editor.commit();
    }

    public static int getId(Context context) {
        if (getSharedPreferences(context).contains(PREF_ID)) {
            return getSharedPreferences(context).getInt(PREF_ID, ' ');
        } else {
            return 0;
        }
    }
}
