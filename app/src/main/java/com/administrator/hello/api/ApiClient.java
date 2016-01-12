/*
 * Filename	: ApiClient.java
 * Function	:
 * Comment 	:
 * History	: 2015/11/27, smPark, Create
 *
 * Version	: 1.0
 * Author   : Copyright (c) 2015 by Team ButterFlower. All Rights Reserved.
 */

package com.administrator.hello.api;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.administrator.hello.Settings;
import com.administrator.hello.bean.JsonResult;
import com.administrator.hello.bean.Lolfree;
import com.administrator.hello.util.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class ApiClient extends RequestClient{
	private static final String TAG = ApiClient.class.getSimpleName();
	Gson gson = new Gson();

	public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT = "yyyy-MM-dd";

	public enum ApiCommand {
		CMD_LOGIN("/home/api/login.action", false),
		CMD_SET_AUTO_HEAT("/home/api/setAutoHeat.action", false),

		CMD_TEST_LOL("/api/lol/kr/v1.2/champion", false)
		;

		private String path;
		private boolean useCache;
		private ApiCommand(String path, boolean useCache) {
			this.path = path;
			this.useCache = useCache;
		}

		public String getUrl() {
			return String.format("%s%s", Settings.API_SERVER, path);
		}

		public String getUrl2() {
			return String.format("%s%s", Settings.API_SERVER_1, path);
		}

		public boolean isUseCache() {
			return useCache;
		}

		public static ApiCommand getByIndex(int idx) {
			return ApiCommand.values()[idx];
		}
	}

	private final Context mContext;
	private String mDeviceId;
	private final Settings mSettings;

	private static final String JSON_DATE_FORMAT = "yyyy-MM-dd kk:mm:ss";
	protected Gson mGson;

	public ApiClient(Context context, Handler handler) {
		super(handler);
		mContext = context;
		mDeviceId = Utils.getDeviceId(context);
		mGson = new GsonBuilder().setDateFormat(JSON_DATE_FORMAT).create();

		mSettings = Settings.getInstance(context);
	}

	public void stop() {
		super.stop();
	}

	/**
	 * 기본 Parameter(token / deviceId)를 포함한 NameValuePair list를 반환합니다.
	 * @return
	 */
	private List<NameValuePair> getDefaultParam() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("api_key", Settings.lolKey));
		return params;
	}

//	private List<Map> getDefaultParam() {
//		List<Map> params = new ArrayList<Map>();
//		Map map = new HashMap<String, String>();
//		map.put("api_key", Settings.lolKey);
//		params.add(map);
//		return params;
//	}

	/*
	 * 파싱 메소드들 모음
	 * 1. parseJsonTestLol champions
	 *
	 */
	public Lolfree parseJsonTestLol(String response) {
		JsonElement element = gson.fromJson (response, JsonElement.class);
		JsonObject jsonObj = element.getAsJsonObject();

		//Log.d("smpark string 테스트", String.valueOf(jsonObj.get("champions").getAsJsonArray().get(0).getAsJsonObject().get("id")));

		Lolfree result = new Lolfree();
		if (response != null && response.length() > 0) {
			try {
				result = mGson.fromJson(response, Lolfree.class);
				String str = "";

				//Log.d("smpark string 테스트2222", result.toString());
			} catch (Exception e) {
				Log.w(TAG, "ApiClient.parseJsonResult() fail!", e);
			}
		}
		return result;
	}

	public void testLol(){
		ApiCommand cmd = ApiCommand.CMD_TEST_LOL;
		String url = cmd.getUrl();

		List<NameValuePair> params = getDefaultParam();
		params.add(new BasicNameValuePair("freeToPlay", "true"));

		requestGet(cmd.isUseCache(), cmd.ordinal(), url, params, Settings.TIMEOUT);
	}
}
