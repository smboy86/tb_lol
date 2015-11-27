/*
 * Filename	: Utils.java
 * Function	:
 * Comment 	:
 * History	: 2015/11/27, smPark, Create
 *
 * Version	: 1.0
 * Author   : Copyright (c) 2015 by Team ButterFlower. All Rights Reserved.
 */

package com.administrator.hello.util;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.math.BigInteger;
import java.text.DecimalFormat;

public class Utils {
	private static final String TAG = Utils.class.getSimpleName();

	private static final DecimalFormat mMoneyFormat = new DecimalFormat("#,##0");

	private static final String DEFAULT_PHONE_NUMBER = "010-XXXX-xxxx";
	private static final String DEFAULT_ANDROID_ID = "00000000000000";
	private static final String SPLITER = ":";

	private static String mDeviceId = "";
	/**
	 * 핸드폰 번호 + ANDROID_ID 를 결합시켜 유니크한 ID를 생성합니다.
	 * ID는 DESede로 압호화 되어 서버로 전송됩니다.
	 *
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static final String getDeviceId(Context context) {
		if (mDeviceId != null && mDeviceId.length() > 0) {
			return mDeviceId;
		}

		StringBuffer deviceId = new StringBuffer();
		TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (telMgr != null) {
			String phoneNum = telMgr.getLine1Number();
			if (phoneNum != null && phoneNum.length() > 0) {
				deviceId.append(phoneNum);
			} else {
				deviceId.append(DEFAULT_PHONE_NUMBER);
			}

			deviceId.append(SPLITER);

			String devId = telMgr.getDeviceId();
			if (devId != null && devId.length() > 0) {
				deviceId.append(devId);
			} else {
				deviceId.append(DEFAULT_PHONE_NUMBER);
			}
		}

		deviceId.append(SPLITER);

		String androidId = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
		if (androidId != null && androidId.length() > 0) {
			deviceId.append(androidId);
		} else {
			deviceId.append(DEFAULT_ANDROID_ID);
		}

		//		deviceId.append(SPLITER);
		//		deviceId.append(Math.random());	// add random number

		DESedeUtil desUtil = new DESedeUtil();

		try {
			Log.d(TAG, "deviceId(Raw) = " + deviceId);
			String encoded = desUtil.encrypt(deviceId.toString());
			Log.d(TAG, "encoded = " + encoded);
			Log.d(TAG, "trim -> decode : " + desUtil.decrypt(encoded.trim()));

			mDeviceId = desUtil.encrypt(deviceId.toString()).trim();
			return mDeviceId;
		} catch (Exception e) {
			Log.w(TAG, "Util.getDeviceId() fail!", e);
			return "";
		}
	}

	public static final String getPhoneNumber(Context context) {
		String phoneNum = "";
		TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (telMgr != null) {
			phoneNum = telMgr.getLine1Number();
			//			JcLog.i(TAG, "phoneNum = " + phoneNum);
			if (phoneNum != null && phoneNum.startsWith("+82")) {
				phoneNum = phoneNum.replace("+82", "0");
			}
			//			JcLog.i(TAG, "phoneNum (after) = " + phoneNum);
			return phoneNum;
		}
		return phoneNum;
	}

	public static String toMoney(double val) {
		return mMoneyFormat.format(val);
	}

	public static String getSimpleAddress(String address, String regex) {
		String simple = "";
		try {
			String[] splits = address.split(" ");

			if (splits != null) {
				for (int i = splits.length - 1; i >= 0; i--) {
					if (!ValidUtil.find(splits[i], regex)) {
						continue;
					} else {
						simple = splits[i];
						break;
					}
				}
			}
		} catch (Exception e) {
			Log.w(TAG, "get address fail!", e);
		}

		return simple;
	}

	public static String getLastChar(String src, int length) {
		if (src == null || src.length() == 0) {
			return "";
		} else {
			if (src.length() > length) {
				int start = src.length() - length;
				return src.substring(start);
			} else {
				return src;
			}
		}
	}

	public static String toPhoneNum(String num) {
		if (num == null || num.length() == 0) {
			return "";
		}

		if (num.length() == 10) {
			return String.format("%s-%s-%s", num.subSequence(0, 3), num.substring(3, 6), num.substring(6, 10));
		} else if (num.length() == 11) {
			return String.format("%s-%s-%s", num.subSequence(0, 3), num.substring(3, 7), num.substring(7, 11));
		} else {
			return num;
		}
	}

	public static String replaceQuote(String ssid) {
		String result;
		if (ssid.startsWith("\"")) {
			result = ssid.substring(1, ssid.length() - 1); // 따옴표 제거
		} else {
			result = ssid;
		}
		return result;
	}

	public static boolean matchSSID(String target, String scannedSsid) {
		String ssid = "";
		if (scannedSsid != null && scannedSsid.length() > 0) {
			ssid = replaceQuote(scannedSsid);
		}
		return ssid.equals(target);
	}

	public static byte[] deviceIdToBytes(String deviceId) {
		String[] splits = deviceId.split(":");
		byte[] result = new byte[6];
		if (splits != null && splits.length == 6) {
			for (int i = 0; i < splits.length; i++) {
				BigInteger bi = new BigInteger(splits[i], 16);
				result[i] = (byte) bi.intValue();
			}
		}
		return result;
	}
}