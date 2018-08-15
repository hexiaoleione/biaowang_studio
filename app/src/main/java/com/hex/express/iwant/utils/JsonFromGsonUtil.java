package com.hex.express.iwant.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.text.TextUtils;
/**
 * Json工具类 (依据gson原理)
 * @author SCHT-40
 *
 */
public class JsonFromGsonUtil {

	/**
	 * 把一个map变成json字符串
	 * @param map
	 * @return
	 */
	public static String parseMapToJson(Map<?, ?> map) {
		try {
			Gson gson = new Gson();
			return gson.toJson(map);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 把一个json字符串变成对象
	 * @param json
	 * @param cls
	 * @return
	 */
	public static <T> T parseJsonToBean(String json, Class<T> cls) {
		Gson gson = new Gson();
		T t = null;
		try {
			t = gson.fromJson(json, cls);
		} catch (Exception e) {
		}
		return t;
	}

	/**
	 * 把json字符串变成map
	 * @param json
	 * @return
	 */
	public static HashMap<String, Object> parseJsonToMap(String json) {
		Gson gson = new Gson();
		Type type = new TypeToken<HashMap<String, Object>>() {
		}.getType();
		HashMap<String, Object> map = null;
		try {
			map = gson.fromJson(json, type);
		} catch (Exception e) {
		}
		return map;
	}

	/**
	 * 把json字符串变成集合
	 * params: new TypeToken<List<yourbean>>(){}.getType(),
	 * 
	 * @param json
	 * @param type  new TypeToken<List<yourbean>>(){}.getType()
	 * @return
	 */
	public static List<?> parseJsonToList(String json, Type type) {
		Gson gson = new Gson();
		List<?> list = gson.fromJson(json, type);
		return list;
	}

	/**
	 * 
	 * 获取json串中某个字段的值，注意，只能获取同一层级的value
	 * 
	 * @param json
	 * @param key
	 * @return
	 */
	public static String getFieldValue(String json, String key) {
		if (TextUtils.isEmpty(json))
			return null;
		if (!json.contains(key))
			return "";
		JSONObject jsonObject = null;
		String value = null;
		try {
			jsonObject = new JSONObject(json);
			value = jsonObject.getString(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return value;
	}
	public static JSONArray coverModelToJSONArray(List paramList)
			throws Exception {
		if (paramList.isEmpty())
			return null;
		JSONArray localJSONArray = new JSONArray();
		Iterator localIterator = paramList.iterator();
		while (true) {
			if (!localIterator.hasNext())
				return localJSONArray;
			localJSONArray.put(coverModelToJSONObject(localIterator.next()));
		}
	}

	public static JSONObject coverModelToJSONObject(Object paramObject)
			throws Exception {
		JSONObject localJSONObject = new JSONObject();
		Class localClass = paramObject.getClass();
		Field[] arrayOfField = localClass.getDeclaredFields();
		for (int i = 0;; i++) {
			if (i >= arrayOfField.length)
				return localJSONObject;
			Field localField = arrayOfField[i];
			localJSONObject
					.put(localField.getName(),
							invokeMethod(localClass, localField.getName(),
									paramObject));
		}
	}

	public static String getString(JSONObject paramJSONObject,
			String paramString) throws JSONException {
		if (paramJSONObject.isNull(paramString))
			return "";
		return paramJSONObject.getString(paramString);
	}

	private static Object invokeMethod(Class paramClass, String paramString,
			Object paramObject) {
		String str = paramString.substring(0, 1).toUpperCase()
				+ paramString.substring(1);
		try {
			Object localObject = paramClass
					.getMethod("get" + str, new Class[0]).invoke(paramObject,
							new Object[0]);
			return localObject;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return "";
	}

	public static Object jsonToBean(JSONObject paramJSONObject, Class paramClass) {
		Object localObject = null;
		while (true) {
			int i;
			try {
				localObject = paramClass.newInstance();
				Method[] arrayOfMethod = paramClass.getMethods();
				i = 0;
				if (i >= arrayOfMethod.length)
					return localObject;
				String str1 = arrayOfMethod[i].getName();
				Class[] arrayOfClass = arrayOfMethod[i].getParameterTypes();
				if ((arrayOfClass.length == 1) && (str1.indexOf("set") >= 0)) {
					String str2 = arrayOfClass[0].getSimpleName();
					String str3 = str1.substring(3, 4).toLowerCase()
							+ str1.substring(4);
					if ((paramJSONObject.has(str3))
							&& (paramJSONObject.get(str3) != null))
						setValue(str2, paramJSONObject.get(str3),
								arrayOfMethod[i], localObject);
				}
			} catch (Exception localException) {
				localException.printStackTrace();
				LogUtils.debug("JSONObject转JavaBean失败");
				return localObject;
			}
			i++;
		}
	}

	private static void setValue(String str2, Object object, Method method,
			Object localObject) {
		
	}
}
