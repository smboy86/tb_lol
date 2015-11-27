/*
 * Filename	: RequestClient.java
 * Function	:
 * Comment 	:
 * History	: 2015/11/27, smPark, Create
 *
 * Version	: 1.0
 * Author   : Copyright (c) 2015 by Team ButterFlower. All Rights Reserved.
 */

package com.administrator.hello.api;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.administrator.hello.BuildConfig;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RequestClient {
	private static final String TAG = RequestClient.class.getSimpleName();
	
	protected static boolean D = BuildConfig.DEBUG;
	private static long CACHE_TIMEOUT = 1 * 60 * 1000;

	private static class Cache {
		Map<String, Long> lastUpdate = new HashMap<String, Long>();
		Map<String, String> datas = new HashMap<String, String>();
	}

	private static Cache mCache = new Cache();

	public static final int MSG_RESPONSE_RECEIVED = 2346;
	public static final int MSG_ERROR = MSG_RESPONSE_RECEIVED + 1;

	public static final String EXTRA_RESPONSE_CONTENTS = "response_contents";
	public static final String EXTRA_REQUEST_CODE = "req_code";
	
	private Thread mThread;
	protected Handler mHandler;

	public RequestClient(Handler handler) {
		mHandler = handler;
	}
	
	public void setDebug(boolean debug) {
		this.D = debug;
	}

	public void setCacheTimeout(long timeout) {
		this.CACHE_TIMEOUT = timeout;
	}

	public static void clearCache() {
		mCache.lastUpdate.clear();
		mCache.datas.clear();
	}

	private void sendMessage(Message msg) {
		if (mHandler != null) {
			mHandler.sendMessage(msg);
		}
	}

	protected void request(final boolean useCache, final int requestCode, final String url, final List<NameValuePair> params, final int timeout) {
		request(useCache, true, requestCode, url, params, null, timeout);
	}

	protected void requestGet(final boolean useCache, final int requestCode, final String url, final List<NameValuePair> params, final int timeout) {
		request(useCache, false, requestCode, url, params, null, timeout);
	}


	/**
	 * http 요청을 보냅니다.
	 * @param isPost
	 * @param requestCode
	 * @param url
	 * @param params
	 * @param uploadFile - isPost 가 false 일 경우  무시됩니다.
	 * @param timeout
	 */
	protected void request(final boolean useCache, final boolean isPost, final int requestCode, final String url, final List<NameValuePair> params, final Hashtable<String, File> uploadFile, final int timeout) {
		mThread = new Thread() {
			@Override
			public void run() {
				Message msg = new Message();
				Bundle data = new Bundle();
				msg.setData(data);
				data.putInt(EXTRA_REQUEST_CODE, requestCode);
				BufferedReader br = null;
				try {
					HttpParams httpParams = new BasicHttpParams();
					HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
					HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
					httpParams.setBooleanParameter("http.protocol.expect-continue", false);
					HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
					HttpConnectionParams.setSoTimeout(httpParams, timeout);
					
					DefaultHttpClient client = new DefaultHttpClient();
					
					HttpUriRequest request = null;

					String paramStr = URLEncodedUtils.format(params, "utf-8");
					String fullUrl;
					if (url.endsWith("?")) {
						fullUrl = url + paramStr;
					} else {
						fullUrl = url + "?" + paramStr;
					}

					if (useCache) {
						Long lastUpdate = mCache.lastUpdate.get(fullUrl);
						String cachedData = mCache.datas.get(fullUrl);

						if (lastUpdate != null
								&& (System.currentTimeMillis() - lastUpdate) < CACHE_TIMEOUT
								&& cachedData != null && cachedData.length() > 0) {
							msg.what = MSG_RESPONSE_RECEIVED;
							data.putString(EXTRA_RESPONSE_CONTENTS, cachedData);
							msg.setData(data);

							sendMessage(msg);

							Log.i(TAG, "use cache!! - " + url);
							return;
						}
					}


					if (isPost) {
						HttpPost post = new HttpPost(url);
						
						if (uploadFile != null && uploadFile.size() > 0) {
							MultipartEntityBuilder builder = MultipartEntityBuilder.create();
							HttpEntity entity = null;
							Enumeration<String> keys = uploadFile.keys();
							while(keys.hasMoreElements()){
								String key = keys.nextElement();
								File file = uploadFile.get(key);
								ContentBody body = new FileBody(file);
								builder.addPart(key, body);
							}
							
							Iterator<NameValuePair> itr = params.iterator();
							while(itr.hasNext()){
								NameValuePair param = itr.next();
								builder.addPart(param.getName(), new StringBody(param.getValue(), ContentType.TEXT_PLAIN));
//								multiEntity.addPart(param.getName(), new StringBody(param.getValue(), Charset.forName("utf-8")));
							}

							entity = builder.build();
							
							if (D) Log.d(TAG, "multipart = " + entity.toString());
							post.setEntity(entity);
						} else {
							UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "utf-8");
							post.setEntity(entity);
						}
						
						request = post;
						if (D) Log.i(TAG, "url(POST) = " + url);
						if (D) Log.i(TAG, "params = " + params.toString());
						if (D) Log.i(TAG, "uploadFile = " + (uploadFile != null ? uploadFile.toString() : "null"));
					} else {
						request = new HttpGet(fullUrl);
					}
					//request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
					//request.addHeader("Cache-control", "no-cache");
					
					HttpResponse response = client.execute(request);
					if (D) Log.i(TAG, "response status = " + response.getStatusLine());
					br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
					StringBuffer buf = new StringBuffer();
					String line = null;
					while ((line = br.readLine()) != null) {
						buf.append(line);
//						if (D) Log.d(TAG, line);
					}
					
					Log.e(TAG, "response length = " + buf.length());

					// save for cache
					mCache.lastUpdate.put(fullUrl, System.currentTimeMillis());
					mCache.datas.put(fullUrl, buf.toString());

					msg.what = MSG_RESPONSE_RECEIVED;
					data.putString(EXTRA_RESPONSE_CONTENTS, buf.toString());
					msg.setData(data);

					sendMessage(msg);
					
					br.close();
				} catch (NumberFormatException e) {
					Log.w(TAG, "NumberFormatException", e);
					msg.what = MSG_ERROR;
					sendMessage(msg);
				} catch (UnsupportedEncodingException e) {
					Log.w(TAG, "UnsupportedEncodingException", e);
					msg.what = MSG_ERROR;
					sendMessage(msg);
				} catch (ClientProtocolException e) {
					Log.w(TAG, "ClientProtocolException", e);
					msg.what = MSG_ERROR;
					sendMessage(msg);
				} catch (IOException e) {
					Log.w(TAG, "IOException", e);
					msg.what = MSG_ERROR;
					sendMessage(msg);
				} finally {
					try {
						if (br != null) br.close();
						
					} catch (IOException e) {
						msg.what = MSG_ERROR;
						sendMessage(msg);
					}
				}
			}
		};
		mThread.start();
	}
	
	protected void downloadImage(final boolean isPost, final int requestCode, final String url, final List<NameValuePair> params, final String savePath, final int timeout) {
		mThread = new Thread() {
			@Override
			public void run() {
				Message msg = new Message();
				Bundle data = new Bundle();
				msg.setData(data);
				data.putInt(EXTRA_REQUEST_CODE, requestCode);
				BufferedInputStream in = null;
				try {
					HttpParams httpParams = new BasicHttpParams();
					HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
					HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
					httpParams.setBooleanParameter("http.protocol.expect-continue", false);
					HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
					HttpConnectionParams.setSoTimeout(httpParams, timeout);

					DefaultHttpClient client = new DefaultHttpClient();

					HttpUriRequest request = null;
					if (isPost) {
						HttpPost post = new HttpPost(url);
						UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "utf-8");
						post.setEntity(entity);

						request = post;
						if (D) Log.d(TAG, "url(POST) = " + url);
						if (D) Log.d(TAG, "params = " + params.toString());
					} else {
						String paramStr = URLEncodedUtils.format(params, "utf-8");
						if (url.endsWith("?")) {
							request = new HttpGet(url + paramStr);
							if (D) Log.d(TAG, "url(GET) = " + url + paramStr);
						} else {
							request = new HttpGet(url + "?" + paramStr);
							if (D) Log.d(TAG, "url(GET) = " + url + "?" + paramStr);
						}
					}
					//request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
					//request.addHeader("Cache-control", "no-cache");

					HttpResponse response = client.execute(request);
					if (D) Log.i(TAG, "response status = " + response.getStatusLine());
					in = new BufferedInputStream(response.getEntity().getContent());
					if (response.getStatusLine().getStatusCode() == 200) {	// OK
						// save image
//						Bitmap imgBitmap = BitmapFactory.decodeStream(in);
//						saveImage(imgBitmap, savePath);
						saveFile(in, savePath);
					} else {
						// 에러시 500결과 파일 저장	// tomcat 에러 파일등.. 확인용
					}

					in.close();
					msg.what = MSG_RESPONSE_RECEIVED;
					msg.setData(data);

					sendMessage(msg);

				} catch (NumberFormatException e) {
					Log.w(TAG, "NumberFormatException", e);
					msg.what = MSG_ERROR;
					sendMessage(msg);
				} catch (UnsupportedEncodingException e) {
					Log.w(TAG, "UnsupportedEncodingException", e);
					msg.what = MSG_ERROR;
					sendMessage(msg);
				} catch (ClientProtocolException e) {
					Log.w(TAG, "ClientProtocolException", e);
					msg.what = MSG_ERROR;
					sendMessage(msg);
				} catch (IOException e) {
					Log.w(TAG, "IOException", e);
					msg.what = MSG_ERROR;
					sendMessage(msg);
				} finally {
					try {
						if (in != null) in.close();

					} catch (IOException e) {
						msg.what = MSG_ERROR;
						sendMessage(msg);
					}
				}
			}
		};
		mThread.start();
	}

	public void stop() {
		if (mThread != null) {
			mThread.interrupt();
		}
		mThread = null;
	}
	
	private void saveFile(InputStream in, String filename) {
		File file = new File(filename);
		File dir = file.getParentFile();
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		try {
			FileOutputStream out = new FileOutputStream(filename);
			int cnt = 0;
			byte[] buf = new byte[1024];
			while((cnt = in.read(buf, 0, 1024)) > 0) {
				out.write(buf, 0, cnt);
			}
			out.close();
		} catch (Exception e) {
			Log.w(TAG, "image save fail!", e);
		}
	}
}
