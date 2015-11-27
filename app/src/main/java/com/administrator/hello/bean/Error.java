/*
 * Filename	: Error.java
 * Function	:
 * Comment 	:
 * History	: 2015/11/27, smPark, Create
 *
 * Version	: 1.0
 * Author   : Copyright (c) 2015 by ButterFlower. All Rights Reserved.
 */

package com.administrator.hello.bean;

public class Error {
	public enum Code {
		// 인증 관련 (ex, API 인증 토큰 만료 등..)
		AUTH_FAIL("001"),

		// 파라미터 검증 관련  (ex, 파라미터 검증 실패 등..)
		INVALID_PARAM("002"),

		// 값 겁증 에러. (ex, 패스워드 불일치 등..)
		INVALID_VALUE("003"),

		// 설정값 미등록 등.
		INTERNAL_ERROR("004"),

		// unkonwn 에러
		UNKNOWN("999")
		;

		private String code;
		private Code(String code) {
			this.code = code;
		}

		public String code() {
			return code;
		}

		public static Code fromCode(String code) {
			Code[] vals = values();
			for (Code cd : vals) {
				if (cd.code().equals(code)) {
					return cd;
				}
			}

			return null;
		}
	}

	public String code;
	public String field;
	public String message;
}
