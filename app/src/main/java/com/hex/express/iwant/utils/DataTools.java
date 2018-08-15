package com.hex.express.iwant.utils;

import java.util.UUID;

import com.hex.express.iwant.activities.WelcomeActivity;
import com.tencent.mm.sdk.modelmsg.GetMessageFromWX;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;
/**
 * 转换工具类
 * @author SCHT-40
 *
 */
public class DataTools {
	/**
	 * dp转px的方法
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * px转dp的方法
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	/**
	 * 获取手机唯一识别号DeviceId的方法
	 * @param mcontext
	 * @return
	 */
	public static String getDeviceId(Context mcontext){
//		TelephonyManager.getDeviceId();
		String imeiCode ="NONE"; 
		TelephonyManager tm;
		 tm = (TelephonyManager) mcontext.getSystemService(Context.TELEPHONY_SERVICE); 
		try {
			imeiCode =tm.getDeviceId();
		} catch (Exception e) {
			Log.e("getDeviceId Exception", e.getMessage());
			// TODO: handle exception
//			imeiCode=getMacAddress(mcontext);
//			WifiManager wifiManager = (WifiManager)mcontext. getSystemService(Context.WIFI_SERVICE);    
//            WifiInfo wifiInfo = wifiManager.getConnectionInfo();    
//            imeiCode = wifiInfo.getMacAddress();    
		}
		return imeiCode;
	}
	 /**
     * 获取设备UUID
     * 
     * @param context
     * @return
     */
    public static String getUUID(Context context) {
        final TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = ""
                + android.provider.Settings.Secure.getString(
                        context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(),
                ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        LogUtils.debug(uniqueId);
        return uniqueId;
    }
    /**
     * 获取app的版本名字
     * @param context
     * @return
     */
     public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
        }
        return versionName;
    }
     /** 
      * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的 
      * @param context 
      * @return true 表示开启 
      */  
     public static final boolean isOPen(final Context context) {  
         LocationManager locationManager   
                                  = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);  
         // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）  
         boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);  
         // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）  
         boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);  
         if (gps || network) {  
             return true;  
         }  
   
         return false;  
     }  
}
