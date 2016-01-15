/*
 * Filename	: Settings.java
 * Function	:
 * Comment 	:
 * History	: 2015/11/26, willPark, Create
 *
 * Version	: 1.0
 * Author   : Copyright (c) 2015 by Team ButterFlower. All Rights Reserved.
 */


package com.administrator.hello;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.administrator.hello.api.ApiClient;
import com.administrator.hello.bean.Champions;
import com.administrator.hello.bean.Lolfree;
import com.administrator.hello.widget.BaseActivity;

public class MainActivity extends BaseActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();

    private TextView mTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        // example
        //https://kr.api.pvp.net/api/lol/kr/v1.2/champion?freeToPlay=true&api_key=ace2b701-4682-4dd7-a78b-322a8de6be5b
        String key = Settings.lolKey;

        mTextview = (TextView) findViewById(R.id.champion_1_id);

        Log.d(TAG, "smPark testLog");
        mApiClient.testLol();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onApiResponsed(ApiClient.ApiCommand command, Champions result) {
        if (command == ApiClient.ApiCommand.CMD_TEST_LOL) {
            //Log.d(TAG, "smPark onApiResponse - " + result.toString());
//            if (result.success) {

            String str = "";
            //for(int i=0; i < result.getChampions().size(); i++){
                //str = str + result.getChampions().get(4).toString();
            str = str + result.getChampions().toString();
            //}

            mTextview.setText(str);
//            } else {
//                mTextview.setText("fail....");
//            }
        }
    }
}