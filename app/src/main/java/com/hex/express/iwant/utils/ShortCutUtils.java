package com.hex.express.iwant.utils;

import com.hex.express.iwant.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ShortCutUtils {
	public static void addShortCut(Context context, Class<?> activity) {
		// 激活意图
		Intent intent = new Intent();
		//创建意图对象
		intent.setAction("ef.ef.ef");
		//设置意图类型
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		// 设置图标
		//Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.main_icon_36);
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
		// 创建意图
		Intent broadcast = new Intent();
		//设置意图对象
		broadcast.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		//携带数据
		broadcast.putExtra(Intent.EXTRA_SHORTCUT_ICON, bitmap);
		broadcast.putExtra(Intent.EXTRA_SHORTCUT_NAME, "e发快递员ʿ");
		broadcast.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
		broadcast.putExtra("duplicate", false);//设置只允许创建一个图标
		//发送广播
		context.sendBroadcast(broadcast);
	}
}
