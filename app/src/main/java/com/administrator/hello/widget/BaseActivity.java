/*
 * Filename	: BaseActivity.java
 * Function	:
 * Comment 	:
 * History	: 2015/11/27, smPark, Create
 *
 * Version	: 1.0
 * Author   : Copyright (c) 2015 by Team ButterFlower. All Rights Reserved.
 */

package com.administrator.hello.widget;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.administrator.hello.Settings;
import com.administrator.hello.api.ApiClient;
import com.administrator.hello.bean.Lolfree;

public abstract class BaseActivity extends AppCompatActivity{
    private static final String TAG = BaseActivity.class.getSimpleName();

    protected Settings mSettings;
    protected ApiClient mApiClient;
    private Handler mHandler;


    private Handler.Callback mCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == ApiClient.MSG_RESPONSE_RECEIVED) {
                Bundle data = msg.getData();
                if (data != null) {
                    int requestCode = data.getInt(ApiClient.EXTRA_REQUEST_CODE);
                    String response = data.getString(ApiClient.EXTRA_RESPONSE_CONTENTS);
                    Log.v(TAG, "response = " + response);
                    //JsonResult result = mApiClient.parseJsonResult(response);
                    Lolfree result = mApiClient.parseJsonResult2(response);
                    ApiClient.ApiCommand command = ApiClient.ApiCommand.getByIndex(requestCode);

                    onApiResponsed(command, result);
                } else {
                    onApiError();
                }
                return true;
            } else if (msg.what == ApiClient.MSG_ERROR) {
                onApiError();
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSettings = Settings.getInstance(this);
        mHandler = new Handler(mCallback);
        mApiClient = new ApiClient(this, mHandler);
    }

    protected void onApiAuthFail() {
        //Intent signUp = new Intent(this, LoginActivity.class);
        //startActivity(signUp);
    }

    protected void onApiError() {
        hideProgressDialog();
        //showToast(R.string.msg_network_error);
    }

    public void hideProgressDialog() {
//        if (mDialogProgress != null) {
//            mDialogProgress.dismiss();
//        }
    }

    protected abstract void onApiResponsed(ApiClient.ApiCommand command, Lolfree result);
}
