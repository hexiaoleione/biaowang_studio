package com.hex.express.iwant.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 吐司工具类
 * 
 * @author SCHT-40
 * 
 */
public class ToastUtil {
	/**
	 * 短吐司
	 * 
	 * @param baseContext
	 * @param string
	 */
	public static void shortToast(Context baseContext, String string) {
		Toast.makeText(baseContext, string, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 短吐司by R.string
	 * 
	 * @param baseContext
	 * @param resId
	 */
	public static void shortToastByRec(Context baseContext, int resId) {
		Toast.makeText(baseContext, resId, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 长吐司
	 * 
	 * @param baseContext
	 * @param string
	 */
	public static void longToast(Context baseContext, String string) {
		Toast.makeText(baseContext, string, Toast.LENGTH_LONG).show();
	}

	/**
	 * 长吐司by R.string
	 * 
	 * @param baseContext
	 * @param resId
	 */
	public static void longToastByRec(Context baseContext, int resId) {
		Toast.makeText(baseContext, resId, Toast.LENGTH_LONG).show();
	}
}
