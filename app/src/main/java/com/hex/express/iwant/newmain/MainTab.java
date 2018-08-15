package com.hex.express.iwant.newmain;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.Header;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.activities.DownWindPayActivity;
import com.hex.express.iwant.activities.DrawCardActivity;
import com.hex.express.iwant.activities.HAdvertActivity;
import com.hex.express.iwant.activities.MainActivity;
import com.hex.express.iwant.activities.MessageActivity;
import com.hex.express.iwant.bean.AdvertBean;
import com.hex.express.iwant.bean.CityBean;
import com.hex.express.iwant.bean.RegisterBean;
import com.hex.express.iwant.bean.VersionBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.fragment.FragmentHome;
import com.hex.express.iwant.fragment.FragmentMessage;
import com.hex.express.iwant.fragment.FragmentMy;
import com.hex.express.iwant.helper.CheckDbUtils;
import com.hex.express.iwant.helper.CityDbOperation;
import com.hex.express.iwant.helper.DbManager;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.DataTools;
import com.hex.express.iwant.utils.Logger;
import com.hex.express.iwant.utils.NormalLoadPictrue;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.utils.UpdateChecker;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.R.string;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import butterknife.Bind;
import android.widget.PopupWindow.OnDismissListener;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class MainTab extends FragmentActivity {

	private String TAG = MainTab.class.getName();

	public TabFragmentHost mTabHost;
	// 标签
	private String[] TabTag = { "tab1", "tab2", "tab3" };
	// 自定义tab布局显示文本和顶部的图片
	private Integer[] ImgTab = { R.layout.tab_main_home,
			R.layout.tab_main_message, R.layout.tab_main_my };

	// tab 选中的activity FragmentHome FragmentMy
	private Class[] ClassTab = { FragmentHome.class, FragmentMessage.class,
			FragmentMy.class };

	// tab选中背景 drawable 样式图片 背景都是同一个,背景颜色都是 白色。。。
	private Integer[] StyleTab = { R.color.white, R.color.white, R.color.white,
			R.color.white };
	private double latitude;// 经度
	private double longitude;// 纬度
	private LocationClient client;
	private String cityName,city;// 城市名字
	private boolean isFirstLoc = true;// 首次定位
	private String cityCode;
	private VersionBean bean2;
	private String androidVersion = "";
	private PopupWindow window02;
	private AdvertBean adbean;
	private long exitTime;
	LinearLayout tabcontent;
	private String escort;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.maintabs);
		tabcontent=(LinearLayout) findViewById(R.id.ma);
		setupView();
//		if (DataTools.isOPen(getApplicationContext())) {
//			Log.e("111111111111111sssss", ""+DataTools.isOPen(getApplicationContext()));
//		}
		initValue();
		setLinstener();
		 if (isLogin()) {
		fillData();
		getVersions();
//			Advertse();
		 }

	}
	

	private void setupView() {

		// 实例化framentTabHost
		mTabHost = (TabFragmentHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(),
				android.R.id.tabcontent);

	}

	private void initValue() {

		// 初始化tab选项卡
		InitTabView();
	}

	private void setLinstener() {
		// imv_back.setOnClickListener(this);

	}

	private void fillData() {
		// TODO Auto-generated method stub
		client = new LocationClient(getApplicationContext());
		initLocation();
		client.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation arg0) {
				if (arg0 == null || isFirstLoc == false)
					return;
				if (arg0 != null && arg0.toString().length() > 0) {
//					Log.e("11111ddddd    ", ""+arg0.getCityCode()+" "+arg0.getDistrict()+" "+arg0.getAddrStr());4.9E-324
					latitude = arg0.getLatitude();
					longitude = arg0.getLongitude();
//					if (latitude==4.9E-324 || latitude==0) {
//						MainTab.this.finish();
//					ToastUtil.shortToast(MainTab.this, "请打开app位置权限");
//					}
					PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.LONGITUDE,
							String.valueOf(longitude));
					PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.LATITUDE,
							String.valueOf(latitude));
					PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.CITYCODE, arg0.getCityCode());
					PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.ADDRESS, arg0.getAddrStr());
					PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.Codedess, arg0.getDistrict());
					Log.e("la", latitude + "");
					city = arg0.getCity();
				}
				getCityCode();
				// isFirstLoc = false;
//				Log.e("ISLOGIN",
//						PreferencesUtils.getBoolean(getApplicationContext(), PreferenceConstants.ISLOGIN) + "");
				// 登录后开启一个sevice时时上传经纬度
				if (PreferencesUtils.getBoolean(getApplicationContext(), PreferenceConstants.ISLOGIN)) {
					getrequstBalance();
////					double d=29.30;//116.93 34.73 
////					double de=90.15;
////					new UpdateChecker(MainActivity.this, MCUrl.UPLOADMYLOCATION).uploadMyLocation(d, de);
//					new UpdateChecker(MainTab.this, MCUrl.UPLOADMYLOCATION).uploadMyLocation(latitude, longitude);

				}
			}
		});
		
		// 初始化定位
		// 打开GPS
		client.start();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		
	}
	// 初始化 tab 自定义的选项卡 ///////////////
	private void InitTabView() {
		// 可以传递参数 b;传递公共的userid,version,sid
		Bundle b = new Bundle();
//		escort=;
//		if (!"".equals(getIntent().getStringExtra("downwind"))) {
//			View indicator = getIndicatorView(3);
//			ToastUtil.shortToast(MainTab.this, "1111");
//			mTabHost.addTab(mTabHost.newTabSpec(TabTag[3]).setIndicator(indicator),ClassTab[0], b);// 传递公共参数
//		}else {
			// 循环加入自定义的tab
			for (int i = 0; i < TabTag.length; i++) {
				// 封装的自定义的tab的样式
				View indicator = getIndicatorView(i);
				mTabHost.addTab(mTabHost.newTabSpec(TabTag[i]).setIndicator(indicator),ClassTab[i], b);// 传递公共参数
			}
//		}
		
		mTabHost.getTabWidget().setDividerDrawable(R.color.white);
	}

	// 设置tab自定义样式:注意 每一个tab xml子布局的linearlayout 的id必须一样
	private View getIndicatorView(int i) {
		// 找到tabhost的子tab的布局视图
		View v = getLayoutInflater().inflate(ImgTab[i], null);
		LinearLayout tv_lay = (LinearLayout) v.findViewById(R.id.layout_back);
		tv_lay.setBackgroundResource(StyleTab[i]);

		return v;
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
	private void getCityCode() {
		boolean isCopySuccess = CheckDbUtils.checkDb();
		// 成功的将数据库copy到data 中
		if (isCopySuccess) {
			iWantApplication.getInstance().mDbManager = new DbManager(iWantApplication.getInstance());
		}
		if (city == null || city.equals("")) {
			ToastUtil.longToast(getApplicationContext(), "请在权限管理允许定位");
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
				Log.e("bean2", new String(arg2));
				bean2 = new Gson().fromJson(new String(arg2), VersionBean.class);
				PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.VERSION,
						bean2.data.get(0).androidVersion);
				Log.e("beanyyyy", PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.VERSION));
				androidVersion = bean2.data.get(0).androidVersion;
				handler.sendEmptyMessage(1);
				upVersion(androidVersion);
			}
		});
		return androidVersion;
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
	public void upVersion(String version) {
		Log.e("PPPP", version);
		if (bean2.data.get(0).ifUpdate.equals("1")) {
	
		if (!getCurrVersion().equals(version)) {
			Builder ad = new Builder(MainTab.this);
			ad.setTitle("温馨提示");
			ad.setCancelable(false);
			ad.setMessage(""+bean2.data.get(0).updateContent);
			ad.setNegativeButton("立刻前往", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					new UpdateChecker(MainTab.this, MCUrl.VERSION).checkForUpdates();
				}
			}).create().show();
		}
		}else if (bean2.data.get(0).ifUpdate.equals("0")){
			if (!getCurrVersion().equals(version)) {
				Builder ad = new Builder(MainTab.this);
				ad.setTitle("温馨提示");
				ad.setMessage(""+bean2.data.get(0).updateContent);
				ad.setNegativeButton("立刻前往", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						new UpdateChecker(MainTab.this, MCUrl.VERSION).checkForUpdates();
					}
				});
				ad.setPositiveButton("暂不更新", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
						
					}
				});
				ad.create().show();
				
			}
		}
	}
	/**
	 * 获取钱包余额
	 */
	private void getrequstBalance() {
		RequestParams params = new RequestParams();
		AsyncHttpUtils.doGet(
				UrlMap.getUrl(MCUrl.BALANCE, "id",
						String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID))),
				null, null, params, new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
					}
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						Logger.e("json", "" + new String(arg2));
						
						RegisterBean bean = new Gson().fromJson(new String(arg2), RegisterBean.class);
						if (bean.getErrCode()==0) {

							String usertype = bean.data.get(0).userType;
							PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.USERTYPE,
									bean.data.get(0).userType);
							PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.REALMANAUTH,
									bean.data.get(0).realManAuth);
							PreferencesUtils.putInt(getApplicationContext(), PreferenceConstants.UID,
									bean.data.get(0).userId);
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
//						handler.sendEmptyMessage(0);
					}
						
					}

				});
	}
	public void show(){
		//判断是否显示现金卷的界面
		try {
			if (!"".equalsIgnoreCase(getIntent().getStringExtra("type")) && getIntent().getStringExtra("type")!=null) {
				if (getIntent().getStringExtra("type").equals("1")) {
//					Log.e("111111type", "111111");
					showwindow();
				}
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
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
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				break;
			case 1:
				// 下载更新安装包版本号不同弹出对话框，点击确定按钮去下载
				
				upVersion(androidVersion);
				break;
			case 2:// 每隔一段时间，更新一下快递员地图POI标注坐标
				Log.e("Timer", "Timer");
//				getMark_v2();
				break;
			case 3:// 每隔一段时间，更新一下顺风镖师地图POI标注坐标
				Log.e("Timer", "Timer");
//				getMark_v3();
				break;
			case 4:// 广告
//				show();
				
				break;
			default:
				break;
			}
		};
	};
	
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
//	public void Advertse(){//MCUrl.LoginCoun
//		AsyncHttpUtils.doSimGet(UrlMap.getUrl(MCUrl.LoginCoun, "userId", String
//				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
//						PreferenceConstants.UID))), new AsyncHttpResponseHandler() {
//			@Override
//			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
//				
//			}
//			@Override
//			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//			}
//		});
//	}


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
		window02.showAtLocation(MainTab.this.findViewById(R.id.mat), Gravity.CENTER, 0, 0);
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
				Intent intent = new Intent(MainTab.this,
						HAdvertActivity.class);
				intent.putExtra("url", adbean.getData().get(0).advertiseHtmlUrl);
				Log.e("111url", ""+adbean.getData().get(0).advertiseHtmlUrl);
				if(!adbean.getData().get(0).advertiseHtmlUrl.equals("")){
					startActivity(intent);
					window02.dismiss();	
				}else {
					btnsaves_pan.setText("立即前往");
					Intent intents = new Intent(MainTab.this,
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

	public boolean isLogin(){
		return PreferencesUtils.getBoolean(MainTab.this, PreferenceConstants.ISLOGIN);
		}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		client.stop();
	}
}
