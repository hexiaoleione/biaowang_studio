package com.hex.express.iwant.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 不能将创建的AlertDialog再使用AlertDialog.setContentView(int);只能在创建时指定View
 * 
 * @author Administrator
 * 
 */
public class DialogUtils {

	/**
	 * 创建一个包含自定义控件的对话框
	 * 
	 * @param title
	 * @param msg
	 * @param icon
	 * @param view
	 * @param cancelable
	 *            是否可以取消，如果为false,返回键可不会是它消失，只能使用dismiss()方法才能使他消失
	 * @param canceledOnTouchOutside
	 *            点击屏幕其他位置是否可以隐藏
	 * @return
	 */
	public static AlertDialog createAlertDialog(Context context, String title, String msg, int icon, View view, boolean cancelable,
			boolean canceledOnTouchOutside) {
		return createAlertDialogThree(context, title, msg, icon, view, cancelable, canceledOnTouchOutside, null, null, null, null, null);
	}

	/**
	 * 创建一个只包含自定义控件的对话框
	 * 
	 * @param view
	 * @param cancelable
	 *            是否可以取消，如果为false,返回键可不会是它消失，只能使用dismiss()方法才能使他消失
	 * @param canceledOnTouchOutside
	 *            点击屏幕其他位置是否可以隐藏
	 * @return
	 */
	public static AlertDialog createAlertDialog(Context context, View view, boolean cancelable, boolean canceledOnTouchOutside) {
		return createAlertDialogThree(context, null, null, 0, view, cancelable, canceledOnTouchOutside, null, null, null, null, null);
	}

	/**
	 * 创建有两个按钮的对话框，第二个按钮取消功能的按钮
	 * 
	 * @param title
	 * @param msg
	 * @param icon
	 * @param cancelable
	 * @param canceledOnTouchOutside
	 * @param textFirst
	 * @param textSecond
	 * @param listenerFirst
	 * @return AlertDialog
	 */
	public static AlertDialog createAlertDialogTwo(Context context, String title, String msg, int icon, boolean cancelable,
			boolean canceledOnTouchOutside, String textFirst, String textSecond, DialogInterface.OnClickListener listenerFirst) {
		return createAlertDialogThree(context, title, msg, icon, null, cancelable, canceledOnTouchOutside, textFirst, null, textSecond,
				listenerFirst, null);
	}

	/**
	 * 创建有两个按钮的对话框，第二个按钮取消功能的按钮
	 * 
	 * @param title
	 * @param msg
	 * @param icon
	 * @param cancelable
	 * @param canceledOnTouchOutside
	 * @param textFirst
	 * @param textSecond
	 * @param listenerFirst
	 * @return AlertDialog
	 */
	public static AlertDialog createAlertDialogTwo(Context context, String title, String msg, int icon, View view, boolean cancelable,
			boolean canceledOnTouchOutside, String textFirst, String textSecond, DialogInterface.OnClickListener listenerFirst) {
		return createAlertDialogThree(context, title, msg, icon, view, cancelable, canceledOnTouchOutside, textFirst, null, textSecond,
				listenerFirst, null);
	}

	/**
	 * 
	 * @param context
	 * @param title
	 * @param msg
	 * @param icon
	 * @param cancelable
	 * @param canceledOnTouchOutside
	 * @param textFirst
	 * @param textSecond
	 * @param listenerFirst
	 * @param listenerSecond
	 * @return
	 */
	public static AlertDialog createAlertDialogTwo(Context context, String title, String msg, int icon, boolean cancelable,
			boolean canceledOnTouchOutside, String textFirst, String textSecond, DialogInterface.OnClickListener listenerFirst,
			DialogInterface.OnClickListener listenerSecond) {
		return createAlertDialogThree(context, title, msg, icon, null, cancelable, canceledOnTouchOutside, textFirst, null, textSecond,
				listenerFirst, listenerSecond);
	}

	/**
	 * 创建有两个按钮的对话框，第二个按钮取消功能的按钮,默认可取消，点击外面可隐藏
	 * 
	 * @param context
	 * @param title
	 * @param msg
	 * @param icon
	 * @param textFirst
	 * @param textSecond
	 * @param listenerFirst
	 * @return
	 */
	public static AlertDialog createAlertDialogTwo(Context context, String title, String msg, int icon, String textFirst, String textSecond,
			DialogInterface.OnClickListener listenerFirst) {
		return createAlertDialogThree(context, title, msg, icon, null, true, true, textFirst, null, textSecond, listenerFirst, null);
	}

	/**
	 * 创建有两个按钮的对话框，第二个按钮取消功能的按钮,默认可取消，点击外面可隐藏,第一个按钮文字为‘确定’，第二个为‘取消’
	 * 
	 * @param context
	 * @param title
	 * @param msg
	 * @param icon
	 * @param listenerFirst
	 * @return
	 */
	public static AlertDialog createAlertDialogTwo(Context context, String title, String msg, int icon, DialogInterface.OnClickListener listenerFirst) {
		return createAlertDialogThree(context, title, msg, icon, null, true, true, "确定", null, "取消", listenerFirst, null);
	}

	/**
	 * 显示有三个按钮的对话框,默认可取消，点击外面取消
	 * 
	 * @return
	 */
	public static AlertDialog createAlertDialogThree(Context context, String title, String msg, String textFirst, String textSecond,
			String textThird, DialogInterface.OnClickListener listenerFirst, DialogInterface.OnClickListener listenerSencond) {
		return createAlertDialogThree(context, title, msg, 0, null, true, true, textFirst, textSecond, textThird, listenerFirst, listenerSencond);
	}

	/**
	 * 显示有三个按钮的对话框,默认可取消，点击外面取消
	 * 
	 * @return
	 */
	public static AlertDialog createAlertDialogThree(Context context, String title, String msg, int icon, String textFirst, String textSecond,
			String textThird, DialogInterface.OnClickListener listenerFirst, DialogInterface.OnClickListener listenerSencond) {
		return createAlertDialogThree(context, title, msg, icon, null, true, true, textFirst, textSecond, textThird, listenerFirst, listenerSencond);
	}

	public static AlertDialog createAlertDialogThree(Context context, String title, String msg, int icon, View view, boolean cancelable,
			boolean canceledOnTouchOutside, String textFirst, String textSecond, String textThird, DialogInterface.OnClickListener listenerFirst,
			DialogInterface.OnClickListener listenerSencond) {

		DialogInterface.OnClickListener listenerThird = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		};

		return createAlertDialogThree(context, title, msg, icon, view, cancelable, canceledOnTouchOutside, textFirst, textSecond, textThird,
				listenerFirst, listenerSencond, listenerThird);
	}

	/**
	 * 显示有三个按钮的对话框
	 * 
	 * @param title
	 *            标题
	 * @param msg
	 *            提示的内容
	 * @param icon
	 * @param view
	 * @param cancelable
	 *            是否可以取消，如果为false,返回键可不会是它消失，只能使用dismiss()方法才能使他消失
	 * @param canceledOnTouchOutside
	 *            点击屏幕其他位置是否可以隐藏
	 * @param textFirst
	 *            第一个按钮的文字
	 * @param textSecond
	 *            第二个按钮的文字
	 * @param textThird
	 *            第三个按钮的文字，第三个按钮的点击事件是隐藏对话框
	 * @param listenerFirst
	 *            第一个按钮的点击监听事件
	 * @param listenerSencond
	 *            第二个按钮的点击监听事件
	 * @return
	 */
	public static AlertDialog createAlertDialogThree(Context context, String title, String msg, int icon, View view, boolean cancelable,
			boolean canceledOnTouchOutside, String textFirst, String textSecond, String textThird, DialogInterface.OnClickListener listenerFirst,
			DialogInterface.OnClickListener listenerSencond, DialogInterface.OnClickListener listenerThird) {
		Builder builder = new Builder(context);

		if (!TextUtils.isEmpty(title))
			builder.setTitle(title);

		if (!TextUtils.isEmpty(msg))
			builder.setMessage(msg);

		if (!TextUtils.isEmpty(textFirst))
			builder.setPositiveButton(textFirst, listenerFirst);

		if (!TextUtils.isEmpty(textSecond))
			builder.setNeutralButton(textSecond, listenerSencond);

		if (!TextUtils.isEmpty(textThird))

			builder.setNegativeButton(textThird, listenerThird);

		if (icon != 0)
			builder.setIcon(icon);

		if (view != null) {
			builder.setView(view);

			if (view.getParent() != null) {
				ViewGroup group = (ViewGroup) view.getParent();
				group.removeAllViews();
			}
		}

		AlertDialog dialog = builder.create();

		dialog.setCancelable(cancelable);
		dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);

		return dialog;
	}

	public static Dialog createDialog(Activity activity, int progressDrawableRes) {

		LinearLayout layout = new LinearLayout(activity);
		LayoutParams params1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layout.setLayoutParams(params1);
		layout.setGravity(Gravity.CENTER);

		ProgressBar pb = new ProgressBar(activity);
		LayoutParams params2 = new LayoutParams(30, 30);
		pb.setLayoutParams(params2);

		pb.setIndeterminate(true);
		pb.setIndeterminateDrawable(activity.getResources().getDrawable(progressDrawableRes));

		TextView tv = new TextView(activity);
		tv.setText("正在加载");

		layout.setOrientation(LinearLayout.HORIZONTAL);

		layout.addView(pb);
		layout.addView(tv);

		Dialog dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
		dialog.setContentView(layout);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);

		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);
		lp.x = 100; // 新位置X坐标
		lp.y = 100; // 新位置Y坐标
		lp.width = 300; // 宽度
		lp.height = 300; // 高度
		lp.alpha = 1f; // 透明度

		dialogWindow.setAttributes(lp);

		return dialog;
	}

	/**
	 * 创建全屏幕的，内容在底部的对话框
	 * 
	 * @param activity
	 * @param view
	 * @param theme
	 *            默认如果不设置样式，是有标题的，如果这是样式，有背景框。
	 * 
	 *            <pre>
	 * &lt;style name="dialog" parent="android:style/Theme.Dialog">
	 * 	&lt;item name="android:windowBackground">@drawable/tra&lt;/item>
	 * &lt;/style>
	 *     
	 * &lt;?xml version="1.0" encoding="utf-8"?>
	 * &lt;shape xmlns:android="http://schemas.android.com/apk/res/android" >
	 *     &lt;solid android:color="#00000000"/>
	 * &lt;/shape>
	 * 
	 * </pre>
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Dialog getFullScreenDialog(Activity activity, View view, int theme) {
		Dialog dialog = null;
		if (theme != 0)
			dialog = new Dialog(activity, theme);
		else
			dialog = new Dialog(activity);

		dialog.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		Window window = dialog.getWindow();
		// 设置显示动画
		// window.setWindowAnimations(R.style.main_menu_animstyle);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = activity.getWindowManager().getDefaultDisplay().getHeight();
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = LayoutParams.MATCH_PARENT;
		wl.height = LayoutParams.WRAP_CONTENT;

		// 设置显示位置
		dialog.onWindowAttributesChanged(wl);
		// 设置点击外围解散
		dialog.setCanceledOnTouchOutside(true);
		return dialog;
	}
	
	
	/**
	 * 显示ProgressDialog,@android:style/Theme.Translucent.NoTitleBar
	 */
	public static ProgressDialog getProgressDialog(Context context, String message) {
		ProgressDialog dialog = new ProgressDialog(context);
		// 设置进度条风格，风格为圆形，旋转的
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// 设置ProgressDialog 提示信息
		dialog.setMessage(message);
		// 设置ProgressDialog 的进度条是否不明确
		dialog.setIndeterminate(false);
		// 设置ProgressDialog 是否可以按退回按键取消
		dialog.setCancelable(true);
		return dialog;
	}
}
