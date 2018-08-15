package com.hex.express.iwant.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
/**
 * 联网工具类
 * @author SCHT-40
 *
 */
public class NetworkUtils {
	/**
	 * 判断是会否是手机流量联网的方法
	 * @param paramContext
	 * @return
	 */
	public static boolean isMobileConnected(Context paramContext) {
		boolean bool = false;
		if (paramContext != null) {
			NetworkInfo localNetworkInfo = ((ConnectivityManager) paramContext
					.getSystemService("connectivity")).getNetworkInfo(0);
			bool = false;
			if (localNetworkInfo != null)
				bool = localNetworkInfo.isAvailable();
		}
		return bool;
	}
	/**
	 * 判断是会否是web联网的方法
	 * @param paramContext
	 * @return
	 */
	public static boolean isNetworkConnected(Context paramContext) {
		if (paramContext != null) {
			NetworkInfo localNetworkInfo = ((ConnectivityManager) paramContext
					.getSystemService("connectivity")).getActiveNetworkInfo();
			if (localNetworkInfo != null)
				return localNetworkInfo.isAvailable();
		}
		return false;
	}
	/**
	 * 判断是会否是wifi联网的方法
	 * @param paramContext
	 * @return
	 */
	public static boolean isWifiConnected(Context paramContext) {
		if (paramContext != null) {
			NetworkInfo localNetworkInfo = ((ConnectivityManager) paramContext
					.getSystemService("connectivity")).getNetworkInfo(1);
			if (localNetworkInfo != null)
				return localNetworkInfo.isAvailable();
		}
		return false;
	}
}