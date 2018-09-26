package com.javasampleapproach.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Utils {

	public static JsonObject convertToJSON(String jsonString) {
		System.out.println("jsonString = " + jsonString);
		JsonObject json = (JsonObject) new JsonParser().parse(jsonString);
		System.out.println("json = " + json);
		return json;
	}
}
