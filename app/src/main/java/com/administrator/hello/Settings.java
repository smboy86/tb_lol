/*
 * Filename	: Settings.java
 * Function	:
 * Comment 	:
 * History	: 2015/11/26, smPark, Create
 *
 * Version	: 1.0
 * Author   : Copyright (c) 2015 by Team ButterFlower. All Rights Reserved.
 */

package com.administrator.hello;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {
    private static final String TAG = Settings.class.getSimpleName();

    private static Settings mInstance;
    private final Context mContext;
    private final SharedPreferences mPref;

    // key for setting
    public static final String SETTING_TEST = "test";


    // init variable
    public static final String TEAM_NAME = "Team ButterFlower";
    public static final String API_SERVER = "https://kr.api.pvp.net";
    public static final String API_SERVER_1 = "http://api.openweathermap.org";

    public static final String lolKey = "ace2b701-4682-4dd7-a78b-322a8de6be5b";


    // base configuration
    public static final int TIMEOUT = 5 * 1000;


    private Settings(Context context){
        mContext = context;
        mPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static Settings getInstance(Context context){
        if(mInstance == null){
            mInstance = new Settings(context);
        }

        return mInstance;
    }

    // custom get Set Method

    public String getSettingTest(){
        return mPref.getString(SETTING_TEST, "");
    }

    public void setSettingTest(String str){
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(SETTING_TEST, str);
        editor.commit();
    }


}
