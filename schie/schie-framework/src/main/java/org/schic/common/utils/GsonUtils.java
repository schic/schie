package org.schic.common.utils;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 
 * @Description: Json工具类. 
 * @author Caiwb
 * @date 2019年4月29日 下午4:46:29
 */
public class GsonUtils {
	private static Gson gson = new GsonBuilder().create();

	private GsonUtils() {

	}

	public static String toJson(Object value) {
		return gson.toJson(value);
	}

	public static <T> T fromJson(String json, Class<T> classOfT) {
		return gson.fromJson(json, classOfT);
	}

	public static <T> T fromJson(String json, Type typeOfT) {
		return gson.fromJson(json, typeOfT);
	}
}
