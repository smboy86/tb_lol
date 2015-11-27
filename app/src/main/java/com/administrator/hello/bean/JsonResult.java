/*
 * Filename	: JsonResult.java
 * Function	:
 * Comment 	:
 * History	: 2015/11/27, smPark, Create
 *
 * Version	: 1.0
 * Author   : Copyright (c) 2015 by ButterFlower. All Rights Reserved.
 */

package com.administrator.hello.bean;

import com.google.gson.JsonElement;

import java.util.List;

public class JsonResult {
	public boolean success;
	public JsonElement bean;
	public List<Error> errors;

	public JsonResult(JsonElement bean) {
		this.success = true;
		this.bean = bean;
		this.errors = null;
	}

	public JsonResult(List<Error> errors) {
		this.success = false;
		this.errors = errors;
		this.bean = null;
	}

	public JsonResult(boolean success, JsonElement bean, List<Error> errors) {
		this.success = success;
		this.bean = bean;
		this.errors = errors;
	}

	public boolean isAuthError() {
		if (errors == null || errors.size() == 0) {
			return false;
		} else {
			for (Error err : errors) {
				Error.Code code = Error.Code.fromCode(err.code);
				if (Error.Code.AUTH_FAIL == code) {
					return true;
				}
			}

			return false;
		}
	}

	public String getFirstErrorMessage() {
		if (errors != null && errors.size() > 0) {
			Error error = errors.get(0);
			return error.message;
		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		return "JsonResult{" +
				"success=" + success +
				", bean=" + bean +
				", errors=" + errors +
				'}';
	}
}