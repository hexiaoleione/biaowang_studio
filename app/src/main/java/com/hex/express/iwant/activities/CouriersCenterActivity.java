package com.hex.express.iwant.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.adapters.CourierAdapter;
import com.hex.express.iwant.bean.CityBean;
import com.hex.express.iwant.bean.CourierBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.helper.CheckDbUtils;
import com.hex.express.iwant.helper.CityDbOperation;
import com.hex.express.iwant.helper.DbManager;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.StringUtil;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 快递员库页面
 * 
 * @author SCHT-40
 * 
 */
public class CouriersCenterActivity extends BaseActivity {
	@Bind(R.id.tbv_show)
	TitleBarView tbv_show;
	@Bind(R.id.lv)
	com.handmark.pulltorefresh.library.PullToRefreshSwipeListView lv;
	private CourierAdapter adapter;
	@Bind(R.id.rl)
	RelativeLayout rl;
	@Bind(R.id.tv_add)
	TextView tv_add;
	@Bind(R.id.rl_add)
	RelativeLayout rl_add;
	LoadingProgressDialog dialog;
	Intent intent;
	List<CourierBean.Data> list = new ArrayList<CourierBean.Data>();
	CourierBean bean;
	private String cityCode;
	private boolean isLoc;
	private Geocoder coder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_courierscenter);
		iWantApplication.getInstance().addActivity(this);
		ButterKnife.bind(this);
		initData();
	}

	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() {
		super.onResume();
		// initData();
		/*
		 * if(dialog!=null){ dialog.dismiss(); } getNetData();
		 */
	}

	private LocationClient mLocationClient;
	private double latitude;
	private double longitude;

	@Override
	public void initData() {
		dialog = new LoadingProgressDialog(this);
		dialog.show();
		tbv_show.setTitleText("附近快递员列表");
		intent = new Intent();
		mLocationClient = new LocationClient(getApplicationContext());
		initLocation();
		mLocationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation arg0) {
				if (arg0 == null || arg0.getLatitude() == 0
						|| arg0.getLongitude() == 0 || arg0.getCity() == null) {
					if (dialog != null) {
						dialog.dismiss();
					}
					ToastUtil.shortToast(getApplicationContext(), "定位失败");
				} else {
					latitude = arg0.getLatitude();
					longitude = arg0.getLongitude();
					// Log.e("city", arg0.getCity());
					/*
					 * mSearch.reverseGeoCode(new
					 * ReverseGeoCodeOption().location(new LatLng(latitude,
					 * longitude)));
					 */
					if (isLoc == false) {
						cityCode = getCityCode(arg0.getCity());
						getNetData();
						isLoc = true;
					}

				}
			}
		});
		mLocationClient.start();
	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 60000;
		option.setScanSpan(0);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(false);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		mLocationClient.setLocOption(option);
	}

	private String getCityCode(String city) {
		boolean isCopySuccess = CheckDbUtils.checkDb();
		// 成功的将数据库copy到data 中
		if (isCopySuccess) {
			iWantApplication.getInstance().mDbManager = new DbManager(
					iWantApplication.getInstance());
		}
		if (!city.contains("市")) {
			city = city + "市";
		}
		List<CityBean> selectDataFromDb = new CityDbOperation()
				.selectDataFromDb("select * from city where city_name='" + city
						+ "'");

		return selectDataFromDb.get(0).city_code;
	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub

	}

	public void getNetData() {
		AsyncHttpUtils.doSimGet(UrlMap.getThreeUrl(MCUrl.NEARBY_COURIER,
				"latitude", latitude + "", "longitude", longitude + "",
				"cityCode", cityCode), new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				dialog.dismiss();
				bean = new Gson().fromJson(new String(arg2), CourierBean.class);
				if (bean.data == null || bean.data.size() == 0) {
					Log.e("list1", "lsit1");
					rl.setVisibility(View.GONE);
					rl_add.setVisibility(View.VISIBLE);
					return;
				}
				Log.e("data", "edddd");
				list = bean.data;
				adapter = new CourierAdapter(CouriersCenterActivity.this, list);
				lv.setAdapter(adapter);
				rl.setVisibility(View.VISIBLE);
				rl_add.setVisibility(View.GONE);
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						if (dialog == null) {
							dialog = new LoadingProgressDialog(
									CouriersCenterActivity.this);
						}

						dialog.show();
						JSONObject obj = new JSONObject();
						try {
							obj.put("courierId",list.get(position-1).courierId);
							obj.put("userId", PreferencesUtils.getInt(
									getApplicationContext(),
									PreferenceConstants.UID));
							// obj.put("userName",
							// PreferencesUtils.getString(getApplicationContext(),
							// PreferenceConstants.USERNAME));
							obj.put("userName", PreferencesUtils.getString(
									getApplicationContext(),
									PreferenceConstants.USERNAME));
							obj.put("userMobile", PreferencesUtils.getString(
									getApplicationContext(),
									PreferenceConstants.MOBILE));
							obj.put("courierName",
									list.get(position - 1).courierName);
							obj.put("courierMobile",
									list.get(position - 1).courierMobile);
							obj.put("expId", list.get(position - 1).expId);
							obj.put("expName", list.get(position - 1).expName);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						AsyncHttpUtils.doPostJson(getApplicationContext(),
								MCUrl.Add_COURIER, obj.toString(),
								new AsyncHttpResponseHandler() {

									@Override
									public void onSuccess(int arg0,
											Header[] arg1, byte[] arg2) {
										// TODO Auto-generated method stub
										ToastUtil
												.shortToast(
														getApplicationContext(),
														"添加成功");
										finish();
										Log.e("faliure", "ddddd");
									}

									@Override
									public void onFailure(int arg0,
											Header[] arg1, byte[] arg2,
											Throwable arg3) {
									}
								});
					}

				});
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				// Log.e("ppp", new String(arg2));
			}
		});

	}
	@Override
	public void getData() {
		// TODO Auto-generated method stub

	}

}