/*
 * Filename	: DESedeUtil.java
 * Function	:
 * Comment 	:
 * History	: 2015/03/10, ruinnel, Create
 *
 * Version	: 1.0
 * Author   : Copyright (c) 2015 by JC Square Inc. All Rights Reserved.
 */

package com.administrator.hello.util;

import android.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public class DESedeUtil {
	private static final String TAG = DESedeUtil.class.getSimpleName();

	private static final String TRANSFORMATION = "DESede/ECB/PKCS5Padding";

	// 어플에서 암호화한 키와 일치해야 합니다. 변경하지 마세요.
	private static final String PASSWORD = "5l19gla-1lbz90*)_ad^0-a;dl%@!5akavalk+_lakslfn!414lasdf!4120falbl/aKJLHOadf!"; // byte 변환시 24 byte이상

	private static Key mKey;

	public DESedeUtil() {
		try {
			loadKey(PASSWORD);
		} catch (Exception e) {
			Log.w(TAG, "DESedeUtil gen fail!", e);
		}
	}

	private void loadKey(String keyStr) throws Exception {
		DESedeKeySpec desKeySpec = new DESedeKeySpec(keyStr.getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
		mKey = keyFactory.generateSecret(desKeySpec);
	}

	/**
	 * 문자열 대칭 암호화
	 *
	 * @param strVal 암호화할 문자열
	 * @return 암호화된 문자열
	 * @throws Exception
	 */
	public String encrypt(String strVal) throws Exception {
		if (strVal == null || strVal.length() == 0)
			return "";
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		cipher.init(Cipher.ENCRYPT_MODE, mKey);
		String amalgam = strVal;

		byte[] inputBytes1 = amalgam.getBytes("UTF-8");
		byte[] outputBytes1 = cipher.doFinal(inputBytes1);
		String outputStr1 = Base64.encodeToString(outputBytes1, Base64.DEFAULT);
		return outputStr1;
	}

	/**
	 * ByteArray 대칭 암호화
	 *
	 * @param bytes 암호화할 ByteArray
	 * @return 암호화된 ByteArray
	 * @throws Exception
	 */
	public byte[] encrypt(byte[] bytes) throws Exception {
		if (bytes == null || bytes.length == 0)
			return null;
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		cipher.init(Cipher.ENCRYPT_MODE, mKey);

		return cipher.doFinal(bytes);
	}


	/**
	 * 문자열 대칭 복호화
	 *
	 * @param strVal 복호화할 문자열
	 * @return 복호화된 문자열
	 * @throws Exception
	 */
	public String decrypt(String strVal) throws Exception {
		if (strVal == null || strVal.length() == 0)
			return "";
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		cipher.init(Cipher.DECRYPT_MODE, mKey);

		byte[] inputBytes1 = Base64.decode(strVal, Base64.DEFAULT);
		byte[] outputBytes2 = cipher.doFinal(inputBytes1);

		String strResult = new String(outputBytes2, "UTF-8");
		return strResult;
	}

	/**
	 * ByteArray 대칭 복호화
	 *
	 * @param bytes 복호화할 ByteArray
	 * @return 복호화된 ByteArray
	 * @throws Exception
	 */
	public byte[] decrypt(byte[] bytes) throws Exception {
		if (bytes == null || bytes.length == 0)
			return null;
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		cipher.init(Cipher.DECRYPT_MODE, mKey);

		return cipher.doFinal(bytes);
	}
}