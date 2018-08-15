package com.hex.express.iwant.activities;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.madmatrix.zxing.android.CaptureActivity;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.bean.AdvertBean;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.CityBean;
import com.hex.express.iwant.bean.MapPointBean;
import com.hex.express.iwant.bean.POIBean;
import com.hex.express.iwant.bean.RegisterBean;
import com.hex.express.iwant.bean.VersionBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.dialog.NoticeDialogCreater;
import com.hex.express.iwant.helper.CheckDbUtils;
import com.hex.express.iwant.helper.CityDbOperation;
import com.hex.express.iwant.helper.DbManager;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.newmain.MainTab;
import com.hex.express.iwant.utils.HttpUtil;
import com.hex.express.iwant.utils.Logger;
import com.hex.express.iwant.utils.MyBitmapUtils;
import com.hex.express.iwant.utils.NormalLoadPictrue;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.utils.UpdateChecker;
import com.hex.express.iwant.views.DialogUtils;
import com.hex.express.iwant.views.MyInfoWindow;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.R.string;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Audio;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow.OnDismissListener;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class MainActivity extends BaseActivity {
	// 界面标题
	private TitleBarView title_bar;
	//【一级主菜单】
	private LinearLayout ll_mainbutton;
	// 主界面一级button
	private Button fakuaidi;
	//物流
	 private Button wuliu;
	// private Button pairenwu;
	// 【快递二级菜单】——普通用户
	private RelativeLayout rl_menusOfUser;
	// 发快递二级button
	private ImageView onkeysend;
	private ImageView cometopick;
	private Button downwindcourier;
	// 【快递二级菜单】——快递员
	private LinearLayout rl_menusOfCourier;
	// 【物流二级菜单】——普通用户
	private LinearLayout ll_send_freight;
	// 发货运二级button
	private Button sendfreight;
	private Button downwindfreight;
	// 【任务二级菜单】——普通用户
	private LinearLayout ll_send_tasks;
	// 派任务二级button
	private Button publishing_tasks;
	private Button accept_tasks;
	// 定义一个isLogin判断是否登录
	// private boolean isLogin = false;
	// baiduMap相关信息
	private MapView bmapView;
	private BaiduMap map;
	public LocationMode mCurrrentMode = LocationMode.FOLLOWING;
	private double latitude;// 经度
	private double longitude;// 纬度
	private LocationClient client;
	private String cityName;// 城市名字
	private boolean isFirstLoc = true;// 首次定位
	
	private Button btn_location;
	private Button shunfeng;
	private String androidApkPath;
	private String androidVersion = "";
	// private Button shunfengtongcheng;
	private ImageView shunfengsuyun;

	private RelativeLayout ll_send_shunfeng;
	private long exitTime;
	private boolean isFirst = true;
	private List<MapPointBean.Data> list = new ArrayList();
	private List<POIBean.Data> list_POI_COURIER = new ArrayList();
	private List<POIBean.Data> list_POI_DRIVER = new ArrayList();
	private VersionBean bean;
	private int userId;
	@Bind(R.id.ll_update)
	RelativeLayout ll_update;
	@Bind(R.id.pb)
	ProgressBar pb;
	@Bind(R.id.tv_length)
	TextView tv_length;
	@Bind(R.id.iv_luck)
	ImageView iv_luck;
	@Bind(R.id.iv_volume)
	ImageView iv_volume;
	@Bind(R.id.btnLeft)
	ImageView btnLeft;

	@Bind(R.id.btnRight)
	Button btnRight;
	@Bind(R.id.ll_kuaidi)
	LinearLayout ll_kuaidi;
	private String result;
	public RegisterBean userbean;
	public MyReceiver receiver;
	private VersionBean bean2;
	private AdvertBean adbean;
	private View vPopupWindow_recceivedConfirm;// View加载PopupWindow；
	private PopupWindow popupWindow_recceivedConfirm;// 提醒框popupwindow，引导用户去实名认证
	private Button btn_cancle, btn_goto_authenticate;// 提醒框的【取消】和【去认证】按钮
	private MyInfoWindow window;
	private PopupWindow window02;
	BitmapDescriptor bitmap_05_06;
	Timer timer = null;
	public String expnum,codeId,phone;
	 boolean popwind=true;
	 boolean voun=true;

	// 显示周围快递员刷新频率计时器
	TimerTask task_POI_CORIER = new TimerTask() {
		public void run() {
			Message message = new Message();
			message.what = 2;
			handler.sendMessage(message);
		}
	};
	// 显示周围顺风镖师刷新频率计时器
	TimerTask task_POI_DRIVER = new TimerTask() {
		public void run() {
			Message message = new Message();
			message.what = 3;
			handler.sendMessage(message);
		}
	};

	OverlayOptions option_05;
	OverlayOptions option_06;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_main);
		// 首次加载即默认选择为快递项
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		timer = new Timer();
		if (task_POI_CORIER != null) {
			task_POI_CORIER.cancel();
			task_POI_CORIER = null;
		}
		task_POI_CORIER = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message message = new Message();
				message.what = 2;
				handler.sendMessage(message);
			}
		};
		timer.schedule(task_POI_CORIER, 1000, 120000);// 表示 1 秒钟后开始 60秒钟为周期
														// 重复执行，
		iWantApplication.getInstance().addActivity(this);
		ButterKnife.bind(this);
//		JPushInterface.setDebugMode(true);  
		JPushInterface.init(MainActivity.this);  
		// UmengUpdateAgent.setUpdateOnlyWifi(false);
		// UmengUpdateAgent.update(this);
		getData();// 获取传递过来的数据
		initView();// 初始化UI
		initData();// 初始化数据
		setOnClick();// 设置点击事件

	}

	private boolean isClick;

	@OnClick({ R.id.iv_luck, R.id.btn_courierqiangdan, R.id.btn_courierjiedan, R.id.myreceiptCourier, R.id.btn_shunfeng,
			R.id.shunfengxianshi })
	public void onMyClick(View view) {
		Intent intent = new Intent();
		switch (view.getId()) {
		//抽奖功能
		case R.id.iv_luck:

			if (!isLogin()) {
				startActivity(new Intent(MainActivity.this, LoginWeixinActivity.class));
				finish();
			} else {//ExerciseActivity
//				startActivity(new Intent(MainActivity.this, LuckydrawActivity.class));//ExerciseActivity
				startActivity(new Intent(MainActivity.this, NewExerciseActivity.class));//NewExerciseActivity
			}

			break;
		case R.id.btn_courierqiangdan:// 快递员抢单

			// ToastUtil.shortToast(mActivity, "暂未开放");
			if (!isLogin()) {
				intent.setClass(MainActivity.this, LoginWeixinActivity.class);
				startActivity(intent);
				finish();
			} else {
				intent.putExtra("cityCode", cityCode);
				intent.putExtra("latitude", latitude);
				intent.putExtra("longitude", longitude);
				intent.setClass(MainActivity.this, PickCenterActivity.class);
				startActivity(intent);
			}

			break;
		case R.id.btn_courierjiedan:// 快递员用户我的接单 我的快递 快递员

			if (!isLogin()) {
				intent.setClass(MainActivity.this, LoginWeixinActivity.class);
				startActivity(intent);
				finish();

			} else {
				intent.setClass(MainActivity.this, MyPickupActivity.class);
				startActivity(intent);
			}

			break;

		case R.id.myreceiptCourier:// 普通用户我的快递
//			 if (!getCurrVersion().equals(androidVersion)) {
//				 System.out.println("111111c  ");
//			 } else {
			if (!isLogin()) {
				intent.setClass(MainActivity.this, LoginWeixinActivity.class);
				startActivity(intent);
				finish();

			} else {
				intent.setClass(MainActivity.this, MyCourierActivity.class);
				startActivity(intent);
			}

//			 }
			break;
		case R.id.shunfengxianshi:// 限时专递
			if (!isLogin()) {
				intent.setClass(MainActivity.this, LoginWeixinActivity.class);
				startActivity(intent);
				finish();

			} else {
			if ("1".equals(PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE))||PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE)==null) {
				// 普通用户
				if (cityCode != null && latitude != 0 && longitude != 0) {
					intent.putExtra("latitude", latitude);
					intent.putExtra("longitude", longitude);
//					intent.setClass(MainActivity.this, PostLimitedDownwindTaskActivity.class);
					intent.setClass(MainActivity.this, DownWindTimeLimitedActivity.class);
					startActivity(intent);
				} else{
					
//					ToastUtil.shortToast(getApplicationContext(), "定位失败");
				}
			} else//镖师
			{
				if (cityCode != null && latitude != 0 && longitude != 0) {
				intent.putExtra("latitude", latitude);
				intent.putExtra("longitude", longitude);
				intent.setClass(MainActivity.this, DownWindTimeLimitedActivity.class);
				startActivity(intent);
			}else{
				
//				ToastUtil.shortToast(getApplicationContext(), "定位失败");
			}
			}
			}
//			startActivity(
//			new Intent(MainActivity.this, DownWindTimeLimitedActivity.class).putExtra("downwind", "escort"));
			break;
		case R.id.btn_shunfeng:// 我的顺风
			startActivity(new Intent(MainActivity.this, MyDownwindActivity.class));
			break;
			
		default:
			break;
		}

	}

	/**
	 * 获取钱包余额
	 */
	private void getrequstBalance() {
		RequestParams params = new RequestParams();
		AsyncHttpUtils.doGet(
				UrlMap.getUrl(MCUrl.BALANCE, "id",
						String.valueOf(PreferencesUtils.getInt(MainActivity.this, PreferenceConstants.UID))),
				null, null, params, new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
					}
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						Logger.e("json", "" + new String(arg2));
						RegisterBean bean = new Gson().fromJson(new String(arg2), RegisterBean.class);
						String usertype = bean.data.get(0).userType;
						PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.USERTYPE,
								bean.data.get(0).userType);
						PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.REALMANAUTH,
								bean.data.get(0).realManAuth);
						 PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.AgreementType,bean.getData().get(0).agreementType);

						    PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.WLID, bean.getData().get(0).wlid);
						Set tag = new HashSet();
						if (usertype.equals("1")) {
							tag.add("user");
						} else if (usertype.equals("2")) {
							tag.add("courier");
						} else if (usertype.equals("3")) {
							tag.add("driver");
						}
						JPushInterface.setAliasAndTags(getApplicationContext(),
								PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID) + "", tag,new TagAliasCallback() {
									@Override
									public void gotResult(int i, String s, Set<String> set) {
										if (i == 0) {
											Log.d("Jpush", s);
										} else {
											Log.d("Jpush", "error code is " + i);
										}
									}
								});
						Logger.e("tag", tag.toString());
						Logger.e("json555555555",
								PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.REALMANAUTH));
						handler.sendEmptyMessage(0);
					}

				});
	}

	@Override
	public void onWeightClick(View v) {
		final Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.fakuaidi:// 发快递
			// 显示周围的快递员：
			map.clear();// 删除所有覆盖物
			if (option_05 != null) {
				option_05 = null;
			}
			if (option_06 != null) {
				option_06 = null;
			}
			if (timer != null) {
				timer.cancel();
				timer = null;
			}
			timer = new Timer();
			if (task_POI_DRIVER != null) {
				task_POI_DRIVER.cancel();
				task_POI_DRIVER = null;
			}
			if (task_POI_CORIER != null) {
				task_POI_CORIER.cancel();
				task_POI_CORIER = null;
			}
			if (bitmap_05_06 != null) {
				bitmap_05_06.recycle();
			}
			task_POI_CORIER = new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Message message = new Message();
					message.what = 2;
					handler.sendMessage(message);
				}
			};
			timer.schedule(task_POI_CORIER, 1000, 120000);// 表示 1 秒钟后开始 60秒钟为周期
															// 重复执行，
			// getMark_v2();//普通用户点击快递栏，在地图上显示附近的快递员

			if (!PreferencesUtils.getBoolean(getApplicationContext(), PreferenceConstants.ISLOGIN)) {
				rl_menusOfUser.setVisibility(View.VISIBLE);
				rl_menusOfCourier.setVisibility(View.GONE);
			} else {
				if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE).equals("1")) {
					// 普通用户
					rl_menusOfUser.setVisibility(View.VISIBLE);
					rl_menusOfCourier.setVisibility(View.GONE);

				} else if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE)
						.equals("2")) {
					// 快递员用户
					rl_menusOfCourier.setVisibility(View.VISIBLE);
					rl_menusOfUser.setVisibility(View.GONE);
				}
//
			}
//			ll_mainbutton.setBackgroundResource(R.drawable.wuliudandu);
			ll_mainbutton.setBackgroundResource(R.drawable.bg_main_kuaidi);//sanggede
//			ll_mainbutton.setBackgroundResource(R.drawable.bg_main_expresss);
			ll_send_tasks.setVisibility(View.GONE);
			ll_send_freight.setVisibility(View.GONE);
			ll_send_shunfeng.setVisibility(View.GONE);
			ll_kuaidi.setVisibility(View.VISIBLE);

			break;
		 case R.id.wuliu:// 物流
			 map.clear();// 删除所有覆盖物
				if (option_05 != null) {
					option_05 = null;
				}
				if (option_06 != null) {
					option_06 = null;
				}
				if (timer != null) {
					timer.cancel();
					timer = null;
				}
				 timer = new Timer();
				if (task_POI_DRIVER != null) {
					task_POI_DRIVER.cancel();
					task_POI_DRIVER = null;
				}
				if (task_POI_CORIER != null) {
					task_POI_CORIER.cancel();
					task_POI_CORIER = null;
				}
				if (bitmap_05_06 != null) {
					bitmap_05_06.recycle();
				}
				task_POI_CORIER = new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Message message = new Message();
						message.what = 2;
						handler.sendMessage(message);
					}
				};
				timer.schedule(task_POI_CORIER, 1000, 120000);// 表示 1 秒钟后开始 60秒钟为周期
																// 重复执行，
//				ll_mainbutton.setBackgroundResource(R.drawable.wuliudandus);
		  ll_mainbutton.setBackgroundResource(R.drawable.bg_main_wuliu);
		  ll_send_tasks.setVisibility(View.GONE);
		  ll_kuaidi.setVisibility(View.GONE);
		  ll_send_shunfeng.setVisibility(View.GONE);
		  ll_send_freight.setVisibility(View.VISIBLE);
		  if (!isLogin()) {
				intent.setClass(MainActivity.this, LoginWeixinActivity.class);
				startActivity(intent);downwindfreight.setBackgroundResource(R.drawable.huoyuanxinxi);	
				finish();
			}else {
//				Log.e("1111", PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.WLID));
		String  wuld=PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.WLID) ;		
		  if (wuld.equals("1") || wuld.equals("2") )//如果为普通用户
			{//select_btn_expbg_shunfengtongcheng  wuliu_ca
			  sendfreight.setBackgroundResource(R.drawable.huoyuanxinxi);	
			  downwindfreight.setBackgroundResource(R.drawable.wuliu_guan);	
			} else {
				sendfreight.setBackgroundResource(R.drawable.select_btn_sendfreight);
//				downwindfreight.setBackgroundResource(R.drawable.wuliu_guan);	
			}
			}
		 break;
		// case R.id.pairenwu:// 派任务
		// ToastUtil.shortToast(MainActivity.this, "此功能暂未开放");
		// // ll_mainbutton.setBackgroundResource(R.drawable.renwu_bg);
		// // ll_kuaidi.setVisibility(View.GONE);
		// // ll_send_freight.setVisibility(View.GONE);
		// // ll_send_shunfeng.setVisibility(View.GONE);
		// // ll_send_tasks.setVisibility(View.VISIBLE);

		// break;
		case R.id.shunfeng:// 顺风
//			ll_mainbutton.setBackgroundResource(R.drawable.wuliudandu);
			ll_mainbutton.setBackgroundResource(R.drawable.bg_main_shunfeng);//三个的
//			ll_mainbutton.setBackgroundResource(R.drawable.bg_main_downwinds);
			ll_kuaidi.setVisibility(View.GONE);
			ll_send_freight.setVisibility(View.GONE);
			ll_send_tasks.setVisibility(View.GONE);
			ll_send_shunfeng.setVisibility(View.VISIBLE);
			if (!isLogin()) {
				intent.setClass(MainActivity.this, LoginWeixinActivity.class);
				startActivity(intent);
				finish();
			}

			// 显示周围的镖师

			map.clear();// 删除所有覆盖物

			if (option_06 != null) {
				option_06 = null;
			}
			if (timer != null) {
				timer.cancel();
				timer = null;
			}
			timer = new Timer();
			if (task_POI_CORIER != null) {
				task_POI_CORIER.cancel();
				task_POI_CORIER = null;
			}
			if (task_POI_DRIVER != null) {
				task_POI_DRIVER.cancel();
				task_POI_DRIVER = null;
			}
			if (bitmap_05_06 != null) {
				bitmap_05_06.recycle();
			}
			task_POI_DRIVER = new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Message message = new Message();
					message.what = 3;
					handler.sendMessage(message);
				}
			};
			timer.schedule(task_POI_DRIVER, 1000, 120000);// 表示 1 秒钟后开始 60秒钟为周期
															// 重复执行，
			break;
		case R.id.onkeysend://一键快递
			if (!isLogin()) {
				intent.setClass(MainActivity.this, LoginWeixinActivity.class);
				startActivity(intent);
				finish();

			} else {
				// 普通用户
				if (cityCode != null && latitude != 0 && longitude != 0) {
					intent.putExtra("cityCode", cityCode);
					intent.putExtra("latitude", latitude);
					intent.putExtra("longitude", longitude);
					intent.setClass(MainActivity.this, OneKeySendExpActivity.class);
					startActivity(intent);
				} else {
//					ToastUtil.shortToast(getApplicationContext(), "定位失败");
				}
			}
			break;
		case R.id.cometopick: // 抢单发布

			if (!isLogin()) {
				intent.setClass(MainActivity.this, LoginWeixinActivity.class);
				startActivity(intent);
				finish();
			} else {
				// 普通用户
				if (cityCode != null && latitude != 0 && longitude != 0) {
					// intent.putExtra("cityCode", cityCode);
					intent.putExtra("latitude", latitude);
					intent.putExtra("longitude", longitude);
					intent.setClass(MainActivity.this, PickOrderActivity.class);
					startActivity(intent);
				} else {
//					ToastUtil.shortToast(getApplicationContext(), "定位失败");
				}
			}
			break;
		case R.id.shunfengsuyun:// 顺风速递
			if (!isLogin()) {
				intent.setClass(MainActivity.this, LoginWeixinActivity.class);
				startActivity(intent);
				finish();
			} else {
				if (cityCode != null && latitude != 0 && longitude != 0) {
					if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE).equals("1"))//如果为普通用户
					{
//						intent.putExtra("downwind", "owner");
//						intent.setClass(MainActivity.this, PostDownWindTaskActivity.class);
						intent.putExtra("downwind", "escort");
						intent.setClass(MainActivity.this, DownWindSpeedActivity.class);
						startActivity(intent);
					} else {
						intent.putExtra("downwind", "escort");
						intent.setClass(MainActivity.this, DownWindSpeedActivity.class);
						startActivity(intent);
					}

				} else {
//					ToastUtil.shortToast(getApplicationContext(), "定位失败");
				}
			}
			break;
		case R.id.sendfreight:// 发物流
			if (!isLogin()) {
				intent.setClass(MainActivity.this, LoginWeixinActivity.class);
				startActivity(intent);
			} else {
				if (cityCode != null && latitude != 0 && longitude != 0) {
					if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.WLID).equals("") ||PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.WLID).equals("0") )//如果为普通用户
					{
						intent.putExtra("cityCode", cityCode);
						intent.putExtra("latitude", latitude);	
						intent.putExtra("longitude", longitude);
						intent.setClass(MainActivity.this, HaiLogisticalActivity.class);//海南
//						intent.setClass(MainActivity.this, LogisticalActivity.class);//用户
						startActivity(intent);
					} else {
						intent.putExtra("cityCode", cityCode);
						intent.putExtra("latitude", latitude);
						intent.putExtra("longitude", longitude);
						intent.setClass(MainActivity.this, HaiLogisticalActivity.class);//海南
//						intent.setClass(MainActivity.this, LogistcaInforseActivity.class);//公司
						startActivity(intent);
					}
					
				} else {
//					ToastUtil.shortToast(getApplicationContext(), "定位失败");
				}
			}
			break;
		case R.id.downwindfreight:// 我的物流
			if (!isLogin()) {
				intent.setClass(MainActivity.this, LoginWeixinActivity.class);
				startActivity(intent);
			} else {
				Log.e("1111 wuliu  ", PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.WLID));
				if (cityCode != null && latitude != 0 && longitude != 0) {
					if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.WLID).equals("") ||PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.WLID).equals("0") )//如果为普通用户
					{
						intent.putExtra("cityCode", cityCode);
						intent.putExtra("latitude", latitude);
						intent.putExtra("longitude", longitude);
						intent.setClass(MainActivity.this, HaiLogisticalFreightActivity.class);//海南
//						intent.setClass(MainActivity.this,	MylogisticakActivity.class);//用户
						
						 startActivity(intent);
					} else {
						intent.putExtra("downwind", "escort");
						intent.setClass(MainActivity.this, HaiLogisticalFreightActivity.class);//海南
//						intent.setClass(MainActivity.this, LogisticalFreightActivity.class);//公司
						startActivity(intent);
					}
					
				} else {
//					ToastUtil.shortToast(getApplicationContext(), "定位失败");
				}
			}
			 
			break;
		case R.id.publishing_tasks:// 发布任务
			intent.setClass(MainActivity.this, PublishingTasksActivity.class);
			startActivity(intent);
			break;
		case R.id.accept_tasks:// 接受任务
			intent.setClass(MainActivity.this, AcceptTasksActivity.class);
			startActivity(intent);
			break;
		case R.id.btnLeft:// 个人中心
//			Log.e("usetype", PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE));
			if (isLogin()) {
				Log.e("usetype", PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE));
				if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE).equals("1")
						|| PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE)
								.equals("3")) {
					// 普通用户中心
					startActivity(new Intent(MainActivity.this, UserCenterActivity.class));
				} else if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE)
						.equals("2")) {
					// 快递员用户中心
					
					startActivity(new Intent(MainActivity.this, CourierCenterActivity.class));
				}

			} else {
				// 登录界面
				startActivity(new Intent(MainActivity.this, LoginWeixinActivity.class));
				finish();
			}
//			Log.e("11112", "111111111111"+PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE));
			break;
		case R.id.btnRight:// 充值
//			 if (!getCurrVersion().equals(androidVersion)) {
//			 upVersion(androidVersion);
//			 } else {
			if (isLogin()) {
				// startActivity(new Intent(MainActivity.this,
				// PickCenterActivity.class));
//				if (!"Y".equals(PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.REALMANAUTH))) {
//					// 跳转到用户实名认证的界面
//					popupWindow_recceivedConfirm.setAnimationStyle(R.style.mypopwindow_anim_style);
//					popupWindow_recceivedConfirm.showAtLocation(v, Gravity.CENTER, 0, 0);
//				} else {
//					startActivity(new Intent(MainActivity.this, RechargeActivity.class));
					
//				startActivity(new Intent(MainActivity.this, MainTab.class));//新的界面
				startActivityForResult(new Intent(MainActivity.this, CaptureActivity.class), 1);
//				}
			} else {
				// 登录界面
				startActivity(new Intent(MainActivity.this, LoginWeixinActivity.class));
				
				finish();
			}
//			 }
			break;
		// 定位到当前位置
		case R.id.btn_location:
			 if (!getCurrVersion().equals(androidVersion)) {
			 upVersion(androidVersion);
			 } else {
			MapStatus mMapStatus = new MapStatus.Builder().target(new LatLng(latitude, longitude)).zoom(15f).build();
			// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
			MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
			// 改变地图状态
			map.setMapStatus(mMapStatusUpdate);
			break;
		 }
		// 弹框的【取消】按钮
		case R.id.btn_cancle:
			popupWindow_recceivedConfirm.dismiss();
			break;
		// 弹框的【去认证】按钮
		case R.id.btn_goto_authenticate:
			startActivity(
					new Intent(MainActivity.this, RegisterSetImageAndNameActivity.class).putExtra("tiaoguo", "2"));
			popupWindow_recceivedConfirm.dismiss();
			break;

		}
	}

	// 点击“确定取货”所弹出的PopupWindow;
	private void showPopupWindow_receivedConfirm() {
		vPopupWindow_recceivedConfirm = LayoutInflater.from(this).inflate(R.layout.popupwindow_warn_to_autnenticate,
				null);
		btn_cancle = (Button) vPopupWindow_recceivedConfirm.findViewById(R.id.btn_cancle);
		btn_goto_authenticate = (Button) vPopupWindow_recceivedConfirm.findViewById(R.id.btn_goto_authenticate);

		popupWindow_recceivedConfirm = new PopupWindow(vPopupWindow_recceivedConfirm, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);
		// 设置背景 使返回键生效（PopupWindow会拦截触摸事件，包括返回键和菜单键）
		popupWindow_recceivedConfirm.setBackgroundDrawable(new ColorDrawable(0x99999999));

		vPopupWindow_recceivedConfirm.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (vPopupWindow_recceivedConfirm.isShown()) {
					popupWindow_recceivedConfirm.dismiss();
				}
				return false;
			}
		});

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE).equals("1")) {
					// 普通用户
					rl_menusOfUser.setVisibility(View.VISIBLE);
					rl_menusOfCourier.setVisibility(View.GONE);
				} else if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE)
						.equals("2")) {
					// 快递员用户
					rl_menusOfCourier.setVisibility(View.VISIBLE);
					rl_menusOfUser.setVisibility(View.GONE);
				}

				break;
			case 1:
				// 下载更新安装包版本号不同弹出对话框，点击确定按钮去下载
				
				upVersion(androidVersion);
				break;
			case 2:// 每隔一段时间，更新一下快递员地图POI标注坐标
				Log.e("Timer", "Timer");
				getMark_v2();
				break;
			case 3:// 每隔一段时间，更新一下顺风镖师地图POI标注坐标
				Log.e("Timer", "Timer");
				getMark_v3();
				break;
			case 4:// 广告
				show();
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void initView() {
		if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERNAME) != null) {
			completed = true;
		}
		userId = PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID);
		llayout_trake = (LinearLayout) findViewById(R.id.llayout_trake);
		// llayout_trake.setVisibility(View.GONE);
		// 初始化转轮
		// 初始化UI
		rl_menusOfUser = (RelativeLayout) findViewById(R.id.rl_menusOfUser);
		rl_menusOfCourier = (LinearLayout) findViewById(R.id.rl_menusOfCourier);
		// 顺风
		ll_send_shunfeng = (RelativeLayout) findViewById(R.id.ll_send_shunfeng);
		// 物流
		ll_send_freight = (LinearLayout) findViewById(R.id.ll_send_freight);
		// 任务
		ll_send_tasks = (LinearLayout) findViewById(R.id.ll_send_tasks);
		// 主界面button
		ll_mainbutton = (LinearLayout) findViewById(R.id.ll_mainbutton);
		// 寻找到button
		// 主界面一级button
		fakuaidi = (Button) findViewById(R.id.fakuaidi);
		wuliu = (Button) findViewById(R.id.wuliu);
		// pairenwu = (Button) findViewById(R.id.pairenwu);
		shunfeng = (Button) findViewById(R.id.shunfeng);
		// 定位
		btn_location = (Button) findViewById(R.id.btn_location);
		// 快递二级button
		onkeysend = (ImageView) findViewById(R.id.onkeysend);
		cometopick = (ImageView) findViewById(R.id.cometopick);
		shunfengsuyun = (ImageView) findViewById(R.id.shunfengsuyun);
		// 货运二级button
		sendfreight = (Button) findViewById(R.id.sendfreight);
		downwindfreight = (Button) findViewById(R.id.downwindfreight);
		// 任务二级button
		publishing_tasks = (Button) findViewById(R.id.publishing_tasks);
		accept_tasks = (Button) findViewById(R.id.accept_tasks);
		// 找到地图
		bmapView = (MapView) findViewById(R.id.bmapView);
		// 设置提示框
		showPopupWindow_receivedConfirm();
		
	}

	private String city;

	@Override
	public void initData() {
		intentReceiver = new IntentReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("freight");
		registerReceiver(intentReceiver, intentFilter);
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("usertype");
		registerReceiver(receiver, filter);
		if (!PreferencesUtils.getBoolean(getApplicationContext(), PreferenceConstants.ISLOGIN)) {
			Log.e("pppp", "1");
			// 没登录显示 普通用户
			rl_menusOfUser.setVisibility(View.VISIBLE);
			rl_menusOfCourier.setVisibility(View.GONE);
		} else {
			getrequstBalance();
		}
		// 下载更新程序
		getVersions();
		// 获取广告图片
		Advert();
		// 获取用户登录次数
		if (isLogin()) {
			Advertse();
		}
		
		// 下面方法5.1系统以上更新安装有问题
		// sendEmptyBackgroundMessage(MsgConstants.MSG_02);
		// 获取地图
		map = bmapView.getMap();
		// 设置缩放级别
		// 允许定位
		map.setMyLocationEnabled(true);
		map.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		map.setMyLocationConfigeration(new MyLocationConfiguration(mCurrrentMode, true, null));
		// 隐藏百度的图标
		View childAt = bmapView.getChildAt(1);
		if (childAt != null && (childAt instanceof ImageView || childAt instanceof ZoomControls)) {
			childAt.setVisibility(View.INVISIBLE);
		}
		// 隐藏比例尺
		bmapView.showScaleControl(false);
		// 隐藏缩放控件
		bmapView.showZoomControls(false);
		/*
		 * map.setMapStatus(MapStatusUpdateFactory .newMapStatus(new
		 * MapStatus.Builder().zoom(6f).build()));
		 */
		client = new LocationClient(getApplicationContext());
		initLocation();
		client.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation arg0) {
				if (arg0 == null || isFirstLoc == false)
					return;
				if (arg0 != null && arg0.toString().length() > 0) {
//					Log.e("11111ddddd    ", ""+arg0.getCityCode()+" "+arg0.getDistrict()+" "+arg0.getAddrStr());
					latitude = arg0.getLatitude();
					longitude = arg0.getLongitude();
					PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.LONGITUDE,
							String.valueOf(longitude));
					PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.LATITUDE,
							String.valueOf(latitude));
					PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.CITYCODE, arg0.getCityCode());
					PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.ADDRESS, arg0.getAddrStr());
					PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.Codedess, arg0.getDistrict());
					Log.e("la", latitude + "");
					city = arg0.getCity();
					LatLng cenpt = new LatLng(arg0.getLatitude(), arg0.getLongitude());
					// 定义地图状态
					MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(15f).build();
					// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
					MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
					// 改变地图状态
					map.setMapStatus(mMapStatusUpdate);
				}
				getCityCode();
				// isFirstLoc = false;
				// 定义Maker坐标点
				LatLng point = new LatLng(latitude, longitude);
				// 构建Marker图标
				BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.bnavi_icon_location_fixed);
				// 构建MarkerOption，用于在地图上添加Marker
				OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
				// 在地图上添加Marker，并显示
				map.addOverlay(option);
				// getMark();
				// getMark_v2();
				// getMark_v3();
				Log.e("ISLOGIN",
						PreferencesUtils.getBoolean(getApplicationContext(), PreferenceConstants.ISLOGIN) + "");
				// 登录后开启一个sevice时时上传经纬度
				if (PreferencesUtils.getBoolean(getApplicationContext(), PreferenceConstants.ISLOGIN)) {
//					double d=29.30;//116.93 34.73 
//					double de=90.15;
//					new UpdateChecker(MainActivity.this, MCUrl.UPLOADMYLOCATION).uploadMyLocation(d, de);
					new UpdateChecker(MainActivity.this, MCUrl.UPLOADMYLOCATION).uploadMyLocation(latitude, longitude);
				}
			}
		});
		
		// 初始化定位
		// 打开GPS
		client.start();
	}
	// 获取附近的网点
	protected void getMark() {
		// TODO Auto-generated method stub
		Logger.e("lo", latitude + "--" + longitude + "");
		sendEmptyBackgroundMessage(MsgConstants.MSG_01);
	}
	// 周围快递员定位；
	protected void getMark_v2() {
		// TODO Auto-generated method stub
		Logger.e("lo", latitude + "--" + longitude + "");
		sendEmptyBackgroundMessage(MsgConstants.MSG_05);
	}

	// 周围镖师定位；
	protected void getMark_v3() {
		// TODO Auto-generated method stub
		Logger.e("lo", latitude + "--" + longitude + "");
		sendEmptyBackgroundMessage(MsgConstants.MSG_06);

	}

	// 获取到位置信息
	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(com.baidu.location.LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 60000;
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(false);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(false);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		client.setLocOption(option);
	}

	@Override
	protected void onResume() {
		super.onResume();
		bmapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		bmapView.onPause();
	}

	public class IntentReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (intent.getAction().equals("freight")) {
				if (intent.getStringExtra("flag").equals("owner")) {// 发布顺风任务完成后发广播到这里，收到跳转到我的顺风界面
					Logger.e("msg", intent.getAction());
					startActivity(
							new Intent(MainActivity.this, MyDownwindActivity.class).putExtra("loadIndex", "Owner"));
				} else if (intent.getStringExtra("flag").equals("escort")) {// 接镖成功以后收到跳转到我的顺风界面
					Logger.e("msg", intent.getAction());
					startActivity(
							new Intent(MainActivity.this, MyDownwindActivity.class).putExtra("loadIndex", "Escort"));
				} 
			}

		}
	}

	public class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, final Intent intent) {// flag是usertype状态
			Logger.e("intent", intent.getAction());
			if (intent.getAction().equals("usertype")) {

				// 快递员和镖师认证在这里收到广播提示用户
				Logger.e("mainflag", intent.getStringExtra("flag"));
				PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.USERTYPE,
						intent.getStringExtra("flag"));
				Logger.e("mainflag1", PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE));
				Builder ad = new Builder(MainActivity.this);
				ad.setTitle("温馨提示");
				ad.setMessage(intent.getStringExtra("message"));
				ad.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (intent.getStringExtra("flag").equals("2")) {
							handler.sendEmptyMessage(0);
						}

					}
				});
				ad.create().show();
			}
		}

	}
	
	public void Noticer(){
		//自定义报警通知（铃声）  
//		Notification	notification =new Notification();
		BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(MainActivity.this);
		builder.statusBarDrawable = R.drawable.ic_luncher;
		builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
		        | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
		builder.notificationDefaults = Notification.DEFAULT_SOUND
		        | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、
//		    notification.sound = Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "6"); 
		JPushInterface.setPushNotificationBuilder(1, builder);
	}
	public void Noticerse(){
		//自定义报警通知（铃声）  
		BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(MainActivity.this);
		builder.statusBarDrawable = R.drawable.ic_luncher;
		builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
		        | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
		builder.notificationDefaults = Notification.DEFAULT_VIBRATE;  // 震动、
		JPushInterface.setPushNotificationBuilder(1, builder);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (task_POI_CORIER != null) {
			task_POI_CORIER.cancel();
			task_POI_CORIER = null;
		}
		if (task_POI_DRIVER != null) {
			task_POI_DRIVER.cancel();
			task_POI_DRIVER = null;
		}
		if (bitmap_05_06 != null) {
			bitmap_05_06.recycle();
		}
		super.onDestroy();
		unregisterReceiver(intentReceiver);
		unregisterReceiver(receiver);
		client.stop();
		bmapView.onDestroy();
	}

	public void Advert(){
		AsyncHttpUtils.doSimGet(MCUrl.Advertise, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				adbean = new Gson().fromJson(new String(arg2), AdvertBean.class);
				String yString=adbean.getData().get(0).advertiseName;
				if (yString.length()>0) {
					String string2=yString.substring(yString.length()-1,yString.length());
					Log.e("111111bean333", string2);
					if (string2.equals("Y")) {
						handler.sendEmptyMessage(4);
					}
				}
				
				
			}
		});
	}

	public void Advertse(){//MCUrl.LoginCoun
		AsyncHttpUtils.doSimGet(UrlMap.getUrl(MCUrl.LoginCoun, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID))), new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
			}
		});
	}
	
	/**
	 * 获取当前服务器上最新版本号
	 */
	public String getVersions() {
		AsyncHttpUtils.doSimGet(MCUrl.VERSION, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if (arg2 == null)
					return;
				bean2 = new Gson().fromJson(new String(arg2), VersionBean.class);
				Log.e("bean2", new String(arg2));
				PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.VERSION,
						bean2.data.get(0).androidVersion);
				Log.e("beanyyyy", PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.VERSION));
				androidVersion = bean2.data.get(0).androidVersion;
				
				handler.sendEmptyMessage(1);
			}
		});
		return androidVersion;
	}
	public void upVersion(String version) {
		Log.e("PPPP", version);
		if (!getCurrVersion().equals(version)) {
			Builder ad = new Builder(MainActivity.this);
			ad.setTitle("温馨提示");
			ad.setCancelable(false);
			ad.setMessage(""+bean2.data.get(0).updateContent);
			ad.setNegativeButton("立刻前往", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					new UpdateChecker(MainActivity.this, MCUrl.VERSION).checkForUpdates();
				}
			}).create().show();
		}
	}

//	public void upVersion(String version) {
//		Log.e("PPPP", version);
//		if (!getCurrVersion().equals(version)) {
//			AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
//			ad.setTitle("版本更新");
//			ad.setCancelable(false);
//			ad.setMessage("有新的版本需要更新，安装最新版本才能正常使用");
//			ad.setNegativeButton("确认", new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface arg0, int arg1) {
//					new UpdateChecker(MainActivity.this, MCUrl.VERSION).checkForUpdates();
//				}
//			});
////			ad.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
////				@Override
////				public void onClick(DialogInterface arg0, int arg1) {
////					arg0.dismiss();
////				}
////			});
//			ad.create().show();
//			
//		}
//	}

	// private Marker paramMarker;
	@Override
	public void setOnClick() {
		// 【取消】
		btn_cancle.setOnClickListener(this);
		// 【去认证】
		btn_goto_authenticate.setOnClickListener(this);
		// 定位
		btn_location.setOnClickListener(this);
		// 主界面一级button点击事件
		fakuaidi.setOnClickListener(this);
		//物流
		wuliu.setOnClickListener(this);
		// pairenwu.setOnClickListener(this);
		shunfeng.setOnClickListener(this);

		// 发快递二级button点击事件
		onkeysend.setOnClickListener(this);
		cometopick.setOnClickListener(this);
		// downwindcourier.setOnClickListener(this);
		// 发货运二级button点击事件
		sendfreight.setOnClickListener(this);
		downwindfreight.setOnClickListener(this);
		// 派任务二级button点击事件
		publishing_tasks.setOnClickListener(this);
		accept_tasks.setOnClickListener(this);
		// 个人中心
		btnLeft.setOnClickListener(this);
		// 抢单大厅
		btnRight.setOnClickListener(this);
		// 顺丰专递
		shunfengsuyun.setOnClickListener(this);
		// 首页地图小水滴显示
		map.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker arg0) {
				for (int i = 0; i < list.size(); i++) {
					if (arg0.getPosition().latitude == list.get(i).latitude) {
						Logger.e("xiaoshuiti", list.get(i).toString());
						window = new MyInfoWindow(MainActivity.this,
								LayoutInflater.from(getApplicationContext()).inflate(R.layout.infowindow, null),
								arg0.getPosition(), -80, list.get(i),
								DistanceUtil.getDistance(new LatLng(latitude, longitude),
										new LatLng(list.get(i).latitude, list.get(i).longitude)));
						window.initData();
						map.showInfoWindow(window);
					}
				}
				return true;
			}
		});
		map.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			@Override
			public void onMapClick(LatLng arg0) {
				// TODO Auto-generated method stub
				if (window != null)
					map.hideInfoWindow();
			}
		});
//		Noticer();
		iv_volume.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (voun) {
					Noticer();
					iv_volume.setBackgroundResource(R.drawable.volumeon);
					voun=false;
				}else {	
					Noticerse();
					iv_volume.setBackgroundResource(R.drawable.volumeoff);
					voun=true;
				}
			}
		});
	}

	boolean completed = true;
	// private ImageView btnReLocate1;
	private LinearLayout llayout_trake;
	/**
	 * 定位SDK监听函数，在initMap中调用
	 * 
	 * @author bruce
	 * @date 2015-11-22
	 */
	private String cityCode;

	@Override
	public void getData() {
	}

	private void getCityCode() {
		boolean isCopySuccess = CheckDbUtils.checkDb();
		// 成功的将数据库copy到data 中
		if (isCopySuccess) {
			iWantApplication.getInstance().mDbManager = new DbManager(iWantApplication.getInstance());
		}
		if (city == null || city.equals("")) {
//			ToastUtil.longToast(getApplicationContext(), "请在权限管理允许定位");
			return;
		}
		Logger.e("city", city);
		if (!city.contains("市")) {
			city = city + "市";
		}
		List<CityBean> selectDataFromDb = new CityDbOperation()
				.selectDataFromDb("select * from city where city_name='" + city + "'");
		if (selectDataFromDb.size() > 0 && !selectDataFromDb.equals("")) {
			cityCode = selectDataFromDb.get(0).city_code;
			Logger.e("citycode", cityCode);
			PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.CITYCODE, cityCode);
		}
		
		
				
	}
	/**
	 * 主界面点击2次退出APP
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			exit();// 退出
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	public void exit() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			ToastUtil.shortToastByRec(getApplicationContext(), R.string.exitapp);
			exitTime = System.currentTimeMillis();
		} else {
			// finish();
			iWantApplication.getInstance().exit();
			System.exit(0);
		}
	}

	@Override
	public void handleBackgroundMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleBackgroundMessage(msg);
		switch (msg.what) {
		case MsgConstants.MSG_01:
			result = HttpUtil
					.get(UrlMap.getTwo(MCUrl.MAPPOINT, "latitude", latitude + "", "longitude", longitude + ""));
			if (result == null)
				return;
			// MapPointBean bean=new Gson().fromJson(result,
			// MapPointBean.class);
			sendEmptyUiMessage(MsgConstants.MSG_01);
			break;
		case MsgConstants.MSG_05:// 获取周围的快递员
			result = HttpUtil.get(UrlMap.getThreeUrl(MCUrl.POI_OF_NEARBY, "latitude", latitude + "", "longitude",
					longitude + "", "userType", "2"));
			if (result == null)
				return;
			// MapPointBean bean=new Gson().fromJson(result,
			// MapPointBean.class);
			sendEmptyUiMessage(MsgConstants.MSG_05);
			break;
		case MsgConstants.MSG_06:// 获取周围的镖师
			result = HttpUtil.get(UrlMap.getThreeUrl(MCUrl.POI_OF_NEARBY, "latitude", latitude + "", "longitude",
					longitude + "", "userType", "3"));
			if (result == null)
				return;
			// MapPointBean bean=new Gson().fromJson(result,
			// MapPointBean.class);
			sendEmptyUiMessage(MsgConstants.MSG_06);
			break;
		case MsgConstants.MSG_02:// 获取版本号
			AsyncHttpUtils.doSimGet(MCUrl.VERSION, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					// TODO Auto-generated method stub
					Logger.e("gdkd", new String(arg2));
					if (arg2 == null)
						return;
					bean = new Gson().fromJson(new String(arg2), VersionBean.class);
					Logger.e("bean", bean.data.get(0).androidApkPath);

					if (!getCurrVersion().equals(bean.data.get(0).androidVersion)) {
						DialogUtils.createAlertDialogTwo(MainActivity.this, "发现新版本",
								"版本号为" + bean.data.get(0).androidVersion + "/n" + bean.data.get(0).updateContent, 0,
								true, false, "确认", "取消", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										androidApkPath = bean.data.get(0).androidApkPath;
										sendEmptyUiMessage(MsgConstants.MSG_03);
									}
								}, new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated
										// method
										// stub
									}
								}).show();
					}
				}

				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
					// TODO Auto-generated method stub
					Logger.e("ccc", arg0 + "");
				}
			});

			break;
		default:
			break;
		}
	}

	private String getCurrVersion() {
		PackageManager manager = this.getPackageManager();
		PackageInfo info = null;
		try {
			info = manager.getPackageInfo(this.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String version = info.versionName;
//		System.out.println("111111   vers"+version);
		return version;
	}

	@Override
	public void handleUiMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleUiMessage(msg);
		switch (msg.what) {
		case MsgConstants.MSG_01:
			Logger.e("cc", result);
			final MapPointBean bean = new Gson().fromJson(result, MapPointBean.class);
			Logger.e("bwan", bean.data.toString());
			if (bean.data == null || bean.data.size() == 0)
				return;
			list = bean.data;
			BitmapDescriptor bitmap;
			OverlayOptions option;
			Bundle bundle = new Bundle();
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).pointId == 0) {
					MarkerOptions mark = new MarkerOptions();
					bitmap = BitmapDescriptorFactory.fromResource(R.drawable.loc_reg);
					// bundle.putSerializable("data",bean.data.get(i));
					option = mark.position(new LatLng(list.get(i).latitude, list.get(i).longitude)).icon(bitmap);
					map.addOverlay(option);
				} else {
					MarkerOptions mark = new MarkerOptions();
					// bundle.putSerializable("data",bean.data.get(i));
					bitmap = BitmapDescriptorFactory.fromResource(R.drawable.loc_unreg);
					option = mark.position(new LatLng(list.get(i).latitude, list.get(i).longitude)).icon(bitmap);
					map.addOverlay(option);
				}
			}
			break;
		case MsgConstants.MSG_05:// 获取周围的快递员；
			// 周围的快递员
			Logger.e("05------------", result + "=================");
			final POIBean bean_05 = new Gson().fromJson(result, POIBean.class);
			Logger.e("5555555555555555", bean_05.data.toString());
			if (bean_05.data == null || bean_05.data.size() == 0)
				return;
			list_POI_COURIER = bean_05.data;
			Bundle bundle_05 = new Bundle();
			map.clear();// 删除所有覆盖物
			
			for (int i = 0; i < list_POI_COURIER.size(); i++) {
				MarkerOptions mark = new MarkerOptions();
				// bundle.putSerializable("data",bean.data.get(i));
				if (bitmap_05_06 != null) {
					bitmap_05_06.recycle();
				}
				bitmap_05_06 = BitmapDescriptorFactory.fromResource(R.drawable.courier_icon);
				if (!list_POI_COURIER.get(i).userId
						.equals(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID))) {
					option_05 = mark
							.position(new LatLng(list_POI_COURIER.get(i).latitude, list_POI_COURIER.get(i).longitude))
							.icon(bitmap_05_06);
					map.addOverlay(option_05);
				}

			}
			LatLng point = new LatLng(latitude, longitude);
			// 构建Marker图标
			BitmapDescriptor bitmap1 = BitmapDescriptorFactory.fromResource(R.drawable.bnavi_icon_location_fixed);
			// 构建MarkerOption，用于在地图上添加Marker
			OverlayOptions option1 = new MarkerOptions().position(point).icon(bitmap1);
			// 在地图上添加自己的Marker，并显示
			map.addOverlay(option1);
			break;
		case MsgConstants.MSG_06:// 获取周围的镖师；
			Logger.e("06------------", result + "=================");
			final POIBean bean_06 = new Gson().fromJson(result, POIBean.class);
			Logger.e("6666666666666666", bean_06.data.toString());
			if (bean_06.data == null || bean_06.data.size() == 0)
				return;
			list_POI_DRIVER = bean_06.data;

			Bundle bundle_06 = new Bundle();
			map.clear();// 删除所有覆盖物
			for (int i = 0; i < list_POI_DRIVER.size(); i++) {
				Logger.e("list_POI_DRIVER", list_POI_DRIVER.size() + "--------------------");
				MarkerOptions mark = new MarkerOptions();
				// bundle.putSerializable("data",bean.data.get(i));
				if (bitmap_05_06 != null) {
					bitmap_05_06.recycle();
				}
				bitmap_05_06 = BitmapDescriptorFactory.fromResource(R.drawable.driver_icon);
				if (!list_POI_DRIVER.get(i).userId
						.equals(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID))) {
					option_06 = mark
							.position(new LatLng(list_POI_DRIVER.get(i).latitude, list_POI_DRIVER.get(i).longitude))
							.icon(bitmap_05_06);
					map.addOverlay(option_06);
				}

			}
			LatLng point1 = new LatLng(latitude, longitude);
			// 构建Marker图标
			BitmapDescriptor bitmap2 = BitmapDescriptorFactory.fromResource(R.drawable.bnavi_icon_location_fixed);
			// 构建MarkerOption，用于在地图上添加Marker
			OverlayOptions option2 = new MarkerOptions().position(point1).icon(bitmap2);
			// 在地图上添加自己的Marker，并显示
			map.addOverlay(option2);
			break;
		case MsgConstants.MSG_03:
			new MyTask().execute(androidApkPath);
			break;
		case MsgConstants.MSG_04:
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (window != null)
				map.hideInfoWindow();
			break;

		default:
			break;
		}
		return true;
	}

	protected void installApk(File file) {
		Intent intent = new Intent();
		// 执行动作
		intent.setAction(Intent.ACTION_VIEW);
		// 执行的数据类型
		Logger.e("fileapk", file.toString());
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	private int progress;
	private int length;
	private IntentReceiver intentReceiver;

	class MyTask extends AsyncTask<String, Integer, File> {

		@Override
		protected void onPostExecute(File result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Logger.e("pre3", "pre3");
			ll_update.setVisibility(View.GONE);
			Logger.e("result", MainActivity.this.result.length() + "");
			installApk(result);
		}

		@Override
		protected File doInBackground(String... path) {
			Logger.e("pre2", "pre2");
			// TODO Auto-generated method stub
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				File file = null;
				try {
					URL url = new URL(path[0]);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(120000);
					// 获取到文件的大小
					length = conn.getContentLength();
					pb.setMax(conn.getContentLength());
					InputStream is = conn.getInputStream();
					file = new File(Environment.getExternalStorageDirectory(), "iwant.apk");
					Logger.e("file", file.toString());
					FileOutputStream fos = new FileOutputStream(file, true);
					BufferedInputStream bis = new BufferedInputStream(is);
					byte[] buffer = new byte[1024];
					int len;
					int total = 0;
					while ((len = bis.read(buffer)) != -1) {
						fos.write(buffer, 0, len);
						total += len;
						// 获取当前下载量
						publishProgress(total);
					}
					fos.close();
					bis.close();
					is.close();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return file;
			} else {
				return null;
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			Logger.e("pre1", "pre1");
			int rate = values[0] * 100 / length;
			tv_length.setText(rate + "/100");
			pb.setProgress(values[0]);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Logger.e("pre", "pre");
			ll_update.setVisibility(View.VISIBLE);
			tv_length.setText("0/0");
			MainActivity.this.setProgressBarVisibility(true);
			pb.setProgress(progress);

		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

	}
	
//	private void shuo(){
//		//判断是否显示现金卷的界面
//		SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
//		   
//				boolean isFirst = sp.getBoolean("isFirsts", true);
//				if (isFirst) {
////					showPaywindow();
//					SharedPreferences.Editor edit = sp.edit();
//					edit.putBoolean("isFirsts", false);
//					edit.commit();
//					Log.e("111112222222isFirsts", ""+isFirst);
//					}else {
////					showPaywindow();
//					Log.e("1111122223isFirsts", ""+isFirst);
//					}
//	}
	public void show(){
		//判断是否显示现金卷的界面
		try {
			if (!getIntent().getStringExtra("type").equals("") && getIntent().getStringExtra("type")!=null) {
				if (getIntent().getStringExtra("type").equals("1")) {
//					Log.e("111111type", "111111");
					showwindow();
				}
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	/**
	 * 现金卷弹窗
	 */
	
	public void showwindow() {
	
		final TextView btnsaves_pan;
		TextView pay_ext;
		ImageView adv_bg;
		 final Intent intent=new Intent() ;
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.mainpopwindow_logsiti, null);
		// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
		window02 = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT);
		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window02.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		// ColorDrawable dw = new ColorDrawable(R.color.transparent01);
		ColorDrawable dw = new ColorDrawable(android.R.color.white);
		window02.setBackgroundDrawable(dw);
		window02.setOutsideTouchable(false);// 这是点击外部不消失
		// 设置popWindow的显示和消失动画
		window02.setAnimationStyle(R.style.mypopwindow_anim_style);
		// 在底部显示
		window02.showAtLocation(MainActivity.this.findViewById(R.id.iv_luck), Gravity.CENTER, 0, 0);
		adv_bg=(ImageView) view.findViewById(R.id.adv_bg);
		btnsaves_pan=(TextView) view.findViewById(R.id.btnsaves_panse);
		pay_ext=(TextView) view.findViewById(R.id.pay_ext);
//		relativeLayout1.setim
//		Log.e("11111url", ""+adbean.getData().get(0).advertiseImageUrl);
		if (!adbean.getData().get(0).advertiseImageUrl.equals("")) {
			String string="http://images.csdn.net/20130609/zhuanti.jpg";
			new NormalLoadPictrue().getPicture(adbean.getData().get(0).advertiseImageUrl,adv_bg);
//			new MyBitmapUtils().display(adv_bg,adbean.getData().get(0).advertiseImageUrl);
		}else {
			adv_bg.setBackgroundResource(R.drawable.dandandans);
		}
//		
		adv_bg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						HAdvertActivity.class);
				intent.putExtra("url", adbean.getData().get(0).advertiseHtmlUrl);
				Log.e("111url", ""+adbean.getData().get(0).advertiseHtmlUrl);
				if(!adbean.getData().get(0).advertiseHtmlUrl.equals("")){
					startActivity(intent);
					window02.dismiss();	
				}else {
					btnsaves_pan.setText("立即前往");
					Intent intents = new Intent(MainActivity.this,
							DrawCardActivity.class);
					startActivity(intents);
					window02.dismiss();	
				}
				
							
			}
		});
		pay_ext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				window02.dismiss();
			}
		});
		
		// popWindow消失监听方法
		window02.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {

			}
		});

	}

	
}
