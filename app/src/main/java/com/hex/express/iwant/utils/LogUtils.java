package com.hex.express.iwant.utils;

import android.util.Log;

public class LogUtils {
	private static boolean isDebug = true;
	private static final String TAG = "slj";

	public static void debug(Object paramObject, String paramString) {
		if (isDebug == true) {
			Log.d(paramObject.getClass().getSimpleName(), paramString);
		}
	}

	public static void debug(String paramString) {
		if (isDebug == true) {
			Log.d(TAG, paramString);
		}
	}

	public static void debug(String paramString, Throwable paramThrowable) {
		if (isDebug == true) {
			Log.d(TAG, paramString, paramThrowable);
		}
	}

	public static void erro(Object paramObject, String paramString) {
		if (isDebug == true) {
			Log.e(paramObject.getClass().getSimpleName(), paramString);
		}
	}

	public static void error(String paramString) {
		if (isDebug == true) {
			Log.e(TAG, paramString);
		}
	}

	public static void error(String paramString, Throwable paramThrowable) {
		if (isDebug == true) {
			Log.e(TAG, paramString, paramThrowable);
		}
	}

	public static void info(Object paramObject, String paramString) {
		if (isDebug == true) {
			Log.i(paramObject.getClass().getSimpleName(), paramString);
		}
	}

	public static void info(String paramString) {
		if (isDebug == true) {
			Log.i(TAG, paramString);
		}
	}
}
