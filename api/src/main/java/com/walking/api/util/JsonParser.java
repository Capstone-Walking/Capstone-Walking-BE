package com.walking.api.util;

import lombok.experimental.UtilityClass;
import org.json.JSONException;
import org.json.JSONObject;

@UtilityClass
public class JsonParser {

	public static String getValue(String jsonString, String key) {

		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			return jsonObject.getString(key);
		} catch (JSONException e) {
			throw new IllegalArgumentException("Json parsing error");
		}
	}
}
