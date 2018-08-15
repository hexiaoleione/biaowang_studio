package com.hex.express.iwant.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;

import com.hex.express.iwant.iWantApplication;

/**
 * 封装一些零碎的工具方法
 * @author Administrator
 *
 */
public class CommonUtil {
	/**
	 * 在主线程执行Runnable
	 * @param r
	 */
	public static void runOnUIThread(Runnable r){
		iWantApplication.getHandler().post(r);
	}
	
	/**
	 * 将自己从父view移除
	 * @param child
	 */
	public static void removeSelfFromParent(View child){
		if(child!=null){
			ViewParent parent = child.getParent();
			if(parent instanceof ViewGroup){
				ViewGroup group = (ViewGroup) parent;
				group.removeView(child);//移除子view
			}
		}
	}
	
	/**
	 * 获取Resource对象
	 * @return
	 */
	public static Resources getResources(){
		return iWantApplication.getInstance().getResources();
	}
	
	/**
	 * 获取字符串的资源
	 * @param resId
	 * @return
	 */
	public static String getString(int resId){
		return getResources().getString(resId);
	}
	
	/**
	 * 获取字符串数组的资源
	 * @param resId
	 * @return
	 */
	public static String[] getStringArray(int resId){
		return getResources().getStringArray(resId);
	}
	
	/**
	 * 获取图片资源
	 * @param resId
	 * @return
	 */
	public static Drawable getDrawable(int resId){
		return getResources().getDrawable(resId);
	}
	/**
	 * 获取dp资源
	 * @param resId
	 * @return
	 */
	public static float getDimen(int resId){
		//会自动将dp值转为像素值
		return getResources().getDimension(resId);
	}
	
	/**
	 * 获取颜色资源
	 * @param resId
	 * @return
	 */
	public static int getColor(int resId){
		return getResources().getColor(resId);
	}

	public static int getWIndowWidth(Context mContext){
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getWidth();

	}
	
	public static int getWindowHeigh(Context mContext){
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getHeight();
	}
}
