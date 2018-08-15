package com.hex.express.iwant.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.framework.base.BaseWorkerFragmentActivity;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.utils.LogUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
/**
 * 基类
 * @author Eric
 *
 */
public abstract class BaseActivity extends BaseWorkerFragmentActivity implements OnClickListener{
	private BroadcastReceiver connectionReceiver;
	public LoadingProgressDialog dialog;
	/**this*/
	protected Activity mActivity=null;
	public ImageLoader loader;
	public DisplayImageOptions options;
	/**
	 * 实现点击事件
	 * @param v
	 */
	public abstract void onWeightClick(View v);
	/**
	 * 初始化UI
	 */
	public abstract void initView();
	/**
	 * 初始化数据
	 */
	public abstract void initData();
	/**
	 * 设置点击事件
	 */
	public abstract void setOnClick();
	/**
	 * 获取存储或者传递过来的数据
	 */
	public abstract void getData();
	
	public void onClick(View v){
		onWeightClick(v);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 SDKInitializer.initialize(getApplicationContext());  
		//实时监听网络的广播
//		iWantApplication.getInstance().registerMessageReceiver();
//		 registerMessageReceiver();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		initView();
//		initData();
//		 setOnClick();
		loader = ImageLoader.getInstance();
		loader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
		options = new DisplayImageOptions.Builder().showStubImage(R.drawable.bg_new_tou) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.bg_new_tou) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.bg_new_tou) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.displayer(new RoundedBitmapDisplayer(300))// 设置成圆角图片
				.build();
		mActivity=this;
		dialog=new LoadingProgressDialog(mActivity);
		iWantApplication.getInstance().addActivity(mActivity);
		getResources();
	}
	@Override
	protected void onResume() {
		super.onResume();
		//registerMessageReceiver();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			//销毁时暂停广播
//			iWantApplication.getInstance().unRegisterMessageReceiver();
			unRegisterMessageReceiver();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 返回键关闭本页面
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
			return true;
		}
		return super.dispatchKeyEvent(event);
	}
	/**
	 * 实时监听网络连接的广播    注册
	 */
	/*public void registerMessageReceiver() {
		connectionReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				ConnectivityManager connectMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
				NetworkInfo mobNetInfo = connectMgr
						.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				NetworkInfo wifiNetInfo = connectMgr
						.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
					final MessageDialog dialog = new MessageDialog(getApplicationContext());
					TextView textView = (TextView) dialog.getTextView();
					Button noButton = (Button) dialog.getNoButton();
					Button peButton = (Button) dialog.getPeButton();
					peButton.setText(R.string.goset);
					noButton.setText(R.string.know);
					textView.setText(R.string.intenetisnotnull);
					dialog.setOnPositiveListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
							 // 跳转到系统的网络设置界面
		                    Intent intent = null;
		                    
		                    // 先判断当前系统版本
		                    if(android.os.Build.VERSION.SDK_INT > 10){  // 3.0以上
//		                        intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
		                        intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
//		                        LogUtils.debug("系统版本号:"+android.os.Build.VERSION.SDK_INT);
		                        startActivity(intent);
		                    }else{
		                        intent = new Intent();
		                        intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
		                        startActivity(intent);
		                    }
						}
					});
					dialog.setOnNegativeListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							dialog.dismiss();
//							PreferencesUtils.putBoolean(getApplicationContext(), "isLogin", false);
						}
					});
					dialog.setCanceledOnTouchOutside(false);
					dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
					dialog.show();
				}
			}
		};
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(connectionReceiver, filter);
	}*/
	/**
	 * 取消广播
	 */
	public void unRegisterMessageReceiver(){
		//unregisterReceiver(connectionReceiver);
	}
	
	public boolean isLogin(){
	return PreferencesUtils.getBoolean(getApplicationContext(), PreferenceConstants.ISLOGIN);
	}
	public Resources getResources() {  
	    Resources res = super.getResources();    
	    Configuration config=new Configuration();    
	    config.setToDefaults();    
	    res.updateConfiguration(config,res.getDisplayMetrics() );  
	    return res;  
	}
}
