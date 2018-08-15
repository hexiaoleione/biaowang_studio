package com.hex.express.iwant.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.framework.base.ObjectUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class AppUtils {

	/** 新浪微博包名 */
	public static final String PACKAGE_NAME_SINA_WEIBO = "com.sina.weibo";
	/** qq包名 */
	public static final String PACKAGE_NAME_TENCENT_QQ = "com.tencent.mobileqq";
	/** 人人网包名 */
	public static final String PACKAGE_NAME_RENREN = "com.renren.mobile.android";
	/** 微信包名 */
	public static final String PACKAGE_NAME_WEIXIN = "com.tencent.mm";
	/** 开心网包名 */
	public static final String PACKAGE_NAME_KAIXIN = "com.kaixin001.activity";

	/**
	 * 判断给出的进程名称是不是就是所在的进程
	 */
	public static boolean isNamedProcess(Context context, String processName) {
		if (context == null) {
			return false;
		}

		int pid = android.os.Process.myPid();
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> processInfoList = manager.getRunningAppProcesses();
		if (processInfoList == null) {
			return true;
		}

		for (RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
			if (processInfo.pid == pid && ObjectUtil.isEquals(processName, processInfo.processName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * ScrollView和ListView冲突解决方法 在ListView.setAdapter()后调用该方法就能解决冲突
	 */
	public static void dealWithScrolListView(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/** 打开拨号界面 */
	public static void intentDial(Context context, String phone) {
		intentAction(context, "tel:" + phone, Intent.ACTION_DIAL);
	}

	/** 不打开拨号界面，直接拨号 */
	public static void intentCall(Context context, String phone) {
		intentAction(context, "tel:" + phone, Intent.ACTION_CALL);
	}

	/** 打开一个网页 */
	public static void intentWebUrl(Context context, String url) {
		intentAction(context, url, Intent.ACTION_VIEW);
	}

	/** 发送短信，不会进入短信编辑页面，需要android.permission.SEND_SMS权限 */
	public static void sendMsg(String phone, String text) {
		SmsManager manager = SmsManager.getDefault();
		manager.sendTextMessage(phone, null, text, null, null);
	}

	/** 发送短信，不会进入短信编辑页面，需要android.permission.SEND_SMS权限 */
	public static void sendMsg(Context context, String phone, String text) {
		Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phone));
		/** 用于附带短信内容，可不加 */
		sendIntent.putExtra("sms_body", text);
		context.startActivity(sendIntent);
	}

	/**
	 * 发送彩信
	 * 
	 * @param context
	 * @param uri
	 *            附件的uri
	 * @param subject
	 *            彩信的主题
	 * @param phoneNumber
	 *            彩信发送目的号码
	 * @param text
	 *            彩信中文字内容
	 */
	public static void sendMMS(Context context, String imagePath, String subject, String phoneNumber, String text) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(imagePath)));
		intent.putExtra("subject", subject);
		intent.putExtra("address", phoneNumber);
		intent.putExtra("sms_body", text);
		intent.putExtra(Intent.EXTRA_TEXT, "it's EXTRA_TEXT");
		intent.setType("image/*");
		intent.setClassName("com.android.mms", "com.android.mms.ui.ComposeMessageActivity");
		context.startActivity(intent);
	}

	private static void intentAction(Context context, String value, String action) {
		try {
			Uri uri = Uri.parse(value);
			Intent it = new Intent(action, uri);
			context.startActivity(it);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 获取版本号 */
	public static int getVersionCode(Context context) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return verCode;
	}

	/** 获取版本名 */
	public static String getVersionName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return verName;
	}

	/**
	 * 获取应用名称
	 * 
	 * @param context
	 * @return
	 */
	public static String getApplicationName(Context context) {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = context.getApplicationContext().getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
		return applicationName;
	}

	/** BaseAdapter在getView中通过这个方法获取对应的控件 */
	@SuppressWarnings("unchecked")
	public static <T extends View> T getViewHolder(View view, int id) {
		SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
		if (viewHolder == null) {
			viewHolder = new SparseArray<View>();
			view.setTag(viewHolder);
		}
		View childView = viewHolder.get(id);
		if (childView == null) {
			childView = view.findViewById(id);
			viewHolder.put(id, childView);
		}
		return (T) childView;
	}

	/** 将bitmap保存 */
	public static boolean saveBitmap(Bitmap bitmap, File file) {
		if (bitmap != null && file != null) {
			FileOutputStream out = null;
			try {
				out = new FileOutputStream(file);
				/** quality，质量，0-100 */
				return bitmap.compress(Bitmap.CompressFormat.PNG, 80, out);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (out != null) {
					try {
						out.flush();
						out.close();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		}
		return false;
	}

	/**
	 * 从本地图片路径转化成Bitmap
	 * 
	 * @param url
	 *            本地图片路径
	 */
	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 获取sdcard目录 ，/mnt/sdcard */
	public static String getSDCard() {
		return Environment.getExternalStorageDirectory().getPath();
	}

	/** 获取屏幕分辨率,Point.x为屏幕的宽，Point.y为屏幕的高 */
	public static Point getScreenPoint(Context context) {
		Point point = new Point();
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		point.x = dm.widthPixels; // 屏幕宽（像素，如：480px）
		point.y = dm.heightPixels; // 屏幕高（像素，如：800px）
		return point;
	}

	/**
	 * 自动弹出然键盘<br>
	 * 对于刚跳到一个新的界面就要弹出软键盘的情况上述代码可能由于界面为加载完全而无法弹出软键盘。 此时应该适当的延迟弹出软键盘（保证界面的数据加载完成）
	 * 
	 * @param delay
	 *            延时时间
	 */
	public static void showSoftInput(final Context context, final View view, int delay) {

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				InputMethodManager inputManager = (InputMethodManager) context
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(view, 0);
			}
		}, delay);
	}

	/** 隐藏软键盘 */
	public static void hideSoftInput(Context context) {
		((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
				((Activity) context).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public static Spanned getHtmlValue(String color, String value) {
		return Html.fromHtml("<font color=#" + color + ">" + value + "</font>");
	}

	/**
	 * 获取经纬度 两点之间的距离
	 */
	public static double getDistatce(double lat1, double lat2, double lon1, double lon2) {
		double R = 6371;
		double distance = 0.0;
		double dLat = (lat2 - lat1) * Math.PI / 180;
		double dLon = (lon2 - lon1) * Math.PI / 180;
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(lat1 * Math.PI / 180)
				* Math.cos(lat2 * Math.PI / 180) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
		distance = (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))) * R;
		return distance;
	}

	/**
	 * 需要android.permission.WRITE_EXTERNAL_STORAGE权限
	 * 
	 * @return Uri 在onActivityResult中，如果uri不为null,则表示拍照的图片Uri
	 */
	public static void takePhoto(Activity activity, int requestCode, String imagePaths) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(imagePaths)));
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 从相册中选择图片
	 * 
	 * @param requestCode
	 *            选择的图片Uri，在onActivityResult中使用data.getData();获取
	 */
	public static void pickPhoto(Activity activity, int requestCode) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 从拍照、相册中选择图片中选择的Uri中，获取图片的实际路径
	 * 
	 * @param context
	 * @param uri
	 * @return
	 */
	public static String getPathFromUri(Context context, Uri uri) {

		String picPath = null;
		String[] pojo = { MediaStore.Images.Media.DATA };

		Cursor cursor = context.getContentResolver().query(uri, pojo, null, null, null);
		if (cursor != null && !cursor.isClosed()) {
			int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
			cursor.moveToFirst();
			picPath = cursor.getString(columnIndex);
			try {
				// 4.0以上的版本会自动关闭 (4.0--14;; 4.0.3--15) ,不需要再关闭
				if (Build.VERSION.SDK_INT < 14) {
					cursor.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return picPath;
	}

	/**
	 * 系统自带的裁剪图片方法
	 * 
	 * @param uri
	 *            图片的URI
	 * @param requestCode
	 *            请求码
	 * @param width
	 *            剪切后图片需要的宽度
	 * @param height
	 *            剪切后图片需要的高度
	 * @param isAtWillDrag
	 *            是否可随意拖动，true：四个边都是可拉动的，false:只能根据宽度、高度比拖动
	 * 
	 *            获取剪切后的图片在onActivityResult中获取：
	 * 
	 *            <pre>
	 * Bundle extras = data.getExtras();
	 * if (extras != null) {
	 * 	Bitmap photo = extras.getParcelable(&quot;data&quot;);
	 * 	iv.setImageBitmap(photo);
	 * }
	 * </pre>
	 */
	public static void cutPhoto(Activity activity, Uri uri, int requestCode, int width, int height, boolean isAtWillDrag) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");

		double percentX = width;
		double percentY = height;

		// 有小数可随意拖动，为整数只能以比例拖动
		if (isAtWillDrag) {
			// aspectX aspectY 是宽高的比例
			intent.putExtra("aspectX", percentX / 10000);
			intent.putExtra("aspectY", percentY / 10000);
		} else {
			// aspectX aspectY 是宽高的比例
			intent.putExtra("aspectX", (int) percentX);
			intent.putExtra("aspectY", (int) percentY);
		}

		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", width);
		intent.putExtra("outputY", height);
		intent.putExtra("return-data", true);
		activity.startActivityForResult(intent, requestCode);
	}

	/** 获取控件在屏幕中最左上角X坐标 */
	public static int getViewLocationX(View view) {
		/** 其中location[0]为x，location[1]为y */
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		return location[0];
	}

	/** 获取控件在屏幕中最左上角Y坐标 */
	public static int getViewLocationY(View view) {
		/** 其中location[0]为x，location[1]为y */
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		return location[1];
	}

	/** 控件还没有显示时，只有在measure才能获取宽高 */
	public static int getMeasureHeight(View view) {
		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h);
		return view.getMeasuredHeight();
	}

	/** 控件还没有显示时，只有在measure才能获取宽高 */
	public static int getMeasureWidth(View view) {
		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h);
		return view.getMeasuredWidth();
	}

	/**
	 * 格式化double数值，保留指定的有效位
	 * 
	 * @param value
	 * @param length
	 *            有效为数
	 * @return
	 */
	public static double parseDouble(double value, int length) {
		if (length > 0) {
			StringBuffer sb = new StringBuffer("0.");
			for (int i = 0; i < length; i++) {
				sb.append("0");
			}
			DecimalFormat decimalFormat = new DecimalFormat(sb.toString());
			return Double.parseDouble(decimalFormat.format(value));
		}
		return value;
	}

	/**
	 * 通过访问权限获取provider
	 * 
	 * @param context
	 * @param permission
	 * @return 通过这个方法遍历，会有1-2S的延时，循环可能有100多次，所以应该把返回的数据保存到文件、数据库中
	 */
	public static String getAuthorityFromPermission(Context context, String permission) {
		if (permission == null)
			return null;
		List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
		if (packs != null) {
			for (PackageInfo pack : packs) {
				ProviderInfo[] providers = pack.providers;
				if (providers != null) {
					for (ProviderInfo provider : providers) {
						if (permission.equals(provider.readPermission))
							return provider.authority;
						if (permission.equals(provider.writePermission))
							return provider.authority;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 是否已经创建快捷方式
	 * 
	 * @param context
	 * @param name
	 * @param authority
	 *            主机名（或叫Authority） 需要通过getAuthorityFromPermission(context,
	 *            "com.android.launcher.permission.READ_SETTINGS"
	 *            );获取返回值，并保存起来，下次判断从文件、数据库中读取<br>
	 *            循环遍历了已经安装的应用，而后找出你本机对应的launcher的包名
	 */
	public static boolean hasShortcut(Context context, String name, String authority) {

		String url = "";

		if (authority == null) {
			if (getSystemVersion() < 8) {
				url = "content://com.android.launcher.settings/favorites?notify=true";
			} else {
				url = "content://com.android.launcher2.settings/favorites?notify=true";
			}
		} else
			url = "content://" + authority + "/favorites?notify=true";

		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(Uri.parse(url), null, "title=?", new String[] { name }, null);

		if (cursor != null && cursor.moveToFirst()) {
			cursor.close();
			return true;
		}

		return false;
	}

	/**
	 * 创建快捷方式
	 * 
	 * @param activity
	 * @param iconRes
	 *            快捷方式显示的图片资源ID
	 * @param name
	 *            快捷方式上显示的内容
	 * 
	 *            <b>注意</b><br>
	 *            需要权限：<br>
	 *            com.android.launcher.permission.INSTALL_SHORTCUT<br>
	 *            com.android.launcher.permission.READ_SETTINGS
	 */
	public static void createShortcut(Activity activity, int iconRes, String name) {
		Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

		// 快捷方式的名称
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
		shortcut.putExtra("duplicate", false); // 不允许重复创建

		// 写在程序后自动删除快捷方式
		Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
		shortcutIntent.addCategory("android.intent.category.LAUNCHER");
		shortcutIntent.setClassName(activity, activity.getClass().getName());
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);

		// 快捷方式的图标
		ShortcutIconResource shortcutIconResource = ShortcutIconResource.fromContext(activity, iconRes);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, shortcutIconResource);

		activity.sendBroadcast(shortcut);
	}

	/**
	 * 创建快捷方式,以应用名称作为快捷方式的名称
	 * 
	 * @param activity
	 * @param iconRes
	 *            快捷方式显示的图片资源ID
	 * 
	 *            <b>注意</b><br>
	 *            需要权限：<br>
	 *            com.android.launcher.permission.INSTALL_SHORTCUT<br>
	 *            com.android.launcher.permission.READ_SETTINGS
	 */
	public static void createShortcut(Activity activity, int iconRes) {
		createShortcut(activity, iconRes, getApplicationName(activity));
	}

	/**
	 * 删除快捷方式
	 * 
	 * @param activity
	 * @param name
	 *            快捷方式的名称 <b>注意</b><br>
	 *            需要权限：<br>
	 *            com.android.launcher.permission.UNINSTALL_SHORTCUT
	 */
	public static void removeShortcut(Activity activity, String name) {
		Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");

		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
		String appClass = activity.getPackageName() + "." + activity.getLocalClassName();
		ComponentName comp = new ComponentName(activity.getPackageName(), appClass);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));

		activity.sendBroadcast(shortcut);

	}

	/**
	 * 获取系统的SDK版本号<br>
	 * 1 ---> The original, first, version of Android.<br>
	 * 2 ---> First Android update, officially called 1.1.<br>
	 * 3 ---> Android 1.5.<br>
	 * 5 ---> Android 1.6.<br>
	 * 6 ---> Android 2.0.1.<br>
	 * 7 ---> Android 2.1.<br>
	 * 8 ---> Android 2.2.<br>
	 * 9 ---> Android 2.3.<br>
	 * 10 ---> Android 2.3.3.<br>
	 * 11 ---> Android 3.0.<br>
	 * 12 ---> Android 3.1.<br>
	 * 13 ---> Android 3.2.<br>
	 * 14 ---> Android 4.0.<br>
	 * 15 ---> Android 4.0.3.<br>
	 * 16 ---> Android 4.1.<br>
	 */
	public static int getSystemVersion() {
		return Build.VERSION.SDK_INT;
	}

	/**
	 * 转换成DP
	 */
	public static float getDensity(Context context, float value) {
		return context.getResources().getDisplayMetrics().density * value;
	}

	/**
	 * 将域名解析成IP
	 */
	public static String getIpByHost(String host) {
		String IPAddress = null;
		InetAddress ReturnStr1 = null;
		try {
			ReturnStr1 = InetAddress.getByName(host);
			IPAddress = ReturnStr1.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return IPAddress;
		}
		return IPAddress;
	}

	/** 获取Key Hash */
	public static String printKeyHash(Activity context) {
		PackageInfo packageInfo;
		String key = null;
		try {
			String packageName = context.getApplicationContext().getPackageName();
			packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
			for (Signature signature : packageInfo.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				key = new String(Base64.encode(md.digest(), 0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return key;
	}

	/**
	 * 通过package name检查APP是否已经安装
	 */
	public static boolean checkAppInstall(Context context, String packageName) {
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> packs = pm.getInstalledPackages(0);
		for (PackageInfo pi : packs) {
			if (pi.applicationInfo.packageName.equals(packageName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 将Bitmap转化成 byte[]
	 * 
	 * @param bm
	 * @return
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * 将字节数组转化成Bitmap
	 * 
	 * @param b
	 * @return
	 */
	public static Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	/**
	 * 判断在AndroidManifest.xml中是否添加了权限
	 * 
	 * @param context
	 * @param permission
	 *            Manifest.permission.INTERNET
	 * @return
	 */
	public static boolean isAppHavePermission(Context context, String permission) {
		return context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
	}

	/**
	 * 读取SDcard文件的文件流，包括图片
	 * 
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 */
	public static InputStream getSDFile(String path) throws FileNotFoundException {
		return new FileInputStream(new File(path));
	}

	/**
	 * 将assert目录下的图片转化成Drawable
	 * 
	 * @param context
	 * @param relativePath
	 * @param isNinePatch
	 *            是否是.9图片
	 * @return
	 */
	public static Drawable getDrawableFromAssert(Context context, String relativePath, boolean isNinePatch) {
		Drawable rtDrawable = null;
		AssetManager asseets = context.getAssets();
		InputStream is = null;
		try {
			is = asseets.open(relativePath);
			if (is != null) {
				Bitmap bitmap = BitmapFactory.decodeStream(is);
				DisplayMetrics metrics = context.getResources().getDisplayMetrics();
				if (isNinePatch) {
					Configuration config = context.getResources().getConfiguration();
					Resources res = new Resources(context.getAssets(), metrics, config);
					rtDrawable = new NinePatchDrawable(res, bitmap, bitmap.getNinePatchChunk(), new Rect(0, 0, 0, 0),
							null);
				} else {
					bitmap.setDensity(metrics.densityDpi);
					rtDrawable = new BitmapDrawable(context.getResources(), bitmap);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

				is = null;
			}
		}
		return rtDrawable;
	}

	/**
	 * 检测是否有SDCard
	 * 
	 * @return
	 */
	public static boolean hasSDCard() {
		File localFile = null;
		boolean bool = Environment.getExternalStorageState().equals("mounted");

		if (bool) {
			localFile = Environment.getExternalStorageDirectory();
		}

		return (localFile != null);
	}

	/**
	 * 通过其他应用的包名启动该应用
	 * 
	 * @param context
	 * @param packageName
	 */
	public static boolean openAppByPackageName(Context context, String packageName) {
		try {
			if (!TextUtils.isEmpty(packageName)) {
				if (checkAppInstall(context, packageName)) {
					PackageManager pm = context.getPackageManager();
					Intent intent = pm.getLaunchIntentForPackage(packageName);
					if (intent != null) {
						List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, 0);
						if (list != null) {
							// 如果这个Intent有1个及以上应用可以匹配处理，则选择第一个匹配的处理，防止选择处理类ResolverActivity缺失导致异常崩溃
							if (list.size() > 0) {
								ResolveInfo ri = list.iterator().next();
								if (ri != null) {
									ComponentName cn = new ComponentName(ri.activityInfo.packageName,
											ri.activityInfo.name);
									Intent launchIntent = new Intent();
									launchIntent.setComponent(cn);
									launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									context.startActivity(launchIntent);
									return true;
								}
							}
						}
					}
				} else {
					// 该应用已卸载
				}
			}
		} catch (Exception e) {
			// 该应用已卸载
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 对参数值做md5加密
	 */
	public static String md5Encrypt(String value) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(value.getBytes("utf8"));

			byte[] bytes = md.digest();
			StringBuilder ret = new StringBuilder(bytes.length << 1);
			for (int i = 0; i < bytes.length; i++) {
				ret.append(Character.forDigit((bytes[i] >> 4) & 0xf, 16));
				ret.append(Character.forDigit(bytes[i] & 0xf, 16));
			}
			return ret.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 指定apk路径进行安装
	 * 
	 * @param apkPath
	 *            apk的路径
	 */
	public static void installAPK(Context context, String apkPath) {
		File apkfile = new File(apkPath);
		if (!apkfile.exists()) {
			Log.e("Log", apkPath + " not exist.");
			return;
		}
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/**
	 * 禁止换行
	 */
	public static void forbidEditEnter(EditText editText) {
		editText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
			}
		});
	}

	/**
	 * 在后台运行
	 */
	public static void runInBack(Context context) {
		PackageManager pm = context.getPackageManager();
		ResolveInfo homeInfo = pm.resolveActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), 0);
		ActivityInfo ai = homeInfo.activityInfo;
		Intent startIntent = new Intent(Intent.ACTION_MAIN);
		startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		startIntent.setComponent(new ComponentName(ai.packageName, ai.name));
		startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			context.startActivity(startIntent);
		} catch (Exception e) {

		}
	}

	/**
	 * 输入框设置明文，密文
	 * 
	 * @param input
	 * @param visible
	 *            true:明文； false：密文
	 */
	public static void setPasswordVisible(EditText input, boolean visible) {
		if (visible)
			input.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
		else
			input.setTransformationMethod(PasswordTransformationMethod.getInstance());
	}

	public static String getAssetDirectory() {
		return "file:///android_asset/";
	}

	public static boolean isAppInstalled(Context context, String packageName) {
		final PackageManager packageManager = context.getPackageManager();
		// 获取所有已安装程序的包信息
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
		for (int i = 0; i < pinfo.size(); i++) {
			if (pinfo.get(i).packageName.equalsIgnoreCase(packageName))
				return true;
		}
		return false;
	}

	/**
	 * 退出当前应用
	 */
	public static void exitAppication(Context context) {
		// 友盟统计
		// 如果开发者调用Process.kill或者System.exit之类的方法杀死进程，请务必在此之前调用MobclickAgent.onKillProcess(Context
		// context)方法，用来保存统计数据。
	//	MobclickAgent.onKillProcess(context);
		android.os.Process.killProcess(android.os.Process.myPid()); // 获取PID
		System.exit(0);

		// Intent intent = new Intent(Intent.ACTION_MAIN);
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// intent.addCategory(Intent.CATEGORY_HOME);
		// startActivity(intent);
		// finish();
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 生成二维码
	 * 
	 * @param content
	 * @return
	 */
	public static Bitmap createQr(String content, int widthAndHeight) {
		try {
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, widthAndHeight,
					widthAndHeight);
			int width = matrix.getWidth();
			int height = matrix.getHeight();
			int[] pixels = new int[width * height];

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (matrix.get(x, y)) {
						pixels[y * width + x] = 0xff000000;
					}
				}
			}
			Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			return bitmap;
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 绑定手机输入框，不能输入已0开头的数据
	 */
	public static void bindPhoneEdit(final EditText text) {
		text.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence val, int arg1, int arg2, int arg3) {
				if (val.toString().length() == 1 && "0".equals(val.toString()))
					text.setText("");
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});
	}

	/**
	 * 根据身份证获取年龄
	 * @param s
	 * @return
	 */
	public static int getAgeByCard(String s) {
		try {
			// 通过String[]的substring方法来读取信息
			String yy1 = s.substring(6, 10); // 出生的年份
			String mm1 = s.substring(10, 12); // 出生的月份
			String dd1 = s.substring(12, 14); // 出生的日期
			String birthday = yy1.concat("-").concat(mm1).concat("-").concat(dd1);
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
			String s1 = sdf.format(date);
			Date today = sdf.parse(s1);
			/* parse方法可以自己查api，他就是将文档（此处是String）格式转成sdf（自己定义的日期格式）。 */
			Date birth = sdf.parse(birthday);
			return today.getYear() - birth.getYear();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static String formatDistance(double dis){
		int distance = (int)dis;
		if(distance < 1000)
			return distance + "m";
		else{
			int km = distance / 1000;
			int less = distance - km * 1000;
			if(less < 100)
				return km + ".1km";
			else
				return km + "." +(less / 100 + 1) + "km";
		}
	}
	
}