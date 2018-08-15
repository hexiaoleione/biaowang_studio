package com.hex.express.iwant.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.cookie.IgnoreCookiesSpec;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.CityBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.helper.CheckDbUtils;
import com.hex.express.iwant.helper.CityDbOperation;
import com.hex.express.iwant.helper.DbManager;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.newmain.MainTab;
import com.hex.express.iwant.newsmain.NewMainActivity;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 快递员认证界面
 * 
 * @author SCHT-50
 * 
 */
public class CourierIdentificationActivity extends BaseActivity {

	private LinearLayout ll_selectExpCompany;
	@Bind(R.id.tv_exp)
	TextView tv_exp;
	private List<String> autoCompletDatas;
	@Bind(R.id.autoTV_netName)
	AutoCompleteTextView autoTV_netName;
	@Bind(R.id.tv_enterNetNo)
	EditText tv_enterNetNo;
	@Bind(R.id.tv_enterCourierNo)
	EditText tv_enterCourierNo;
	@Bind(R.id.tv_enterNetphone)
	EditText tv_enterNetphone;
	private double latitude;// 经度
	private double longitude;// 纬度
	private LocationClient client;
	private String city;
	private String cityCode;
	private List<String> mList;
	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_courier_identification);
		ButterKnife.bind(this);
		initData();

	}

	@OnClick({ R.id.btnLeft, R.id.ll_selectExpCompany, R.id.btn_OK_cIdentify })
	public void MyOnClick(View view) {
		switch (view.getId()) {
		case R.id.btnLeft:
//			Intent intent = new Intent(CourierIdentificationActivity.this, MainTab.class);
			Intent intent = new Intent(CourierIdentificationActivity.this, NewMainActivity.class);
			
			intent.putExtra("type", "2");
			startActivity(intent);
		     finish();
			break;

		case R.id.btn_OK_cIdentify:
			BtnPost();

			break;
		case R.id.ll_selectExpCompany:
			startActivityForResult(new Intent(CourierIdentificationActivity.this, SelectExpCompanyActivity.class), 1);
			break;
		default:
			break;
		}

	}

	// 快递员认证请求
	public void BtnPost() {	
			JSONObject obj = new JSONObject();
			if (!tv_exp.getText().toString().equals("") && !autoTV_netName.getText().toString().equals("") && expId != 0
					&& cityCode != null && latitude != 0 && longitude != 0) {
				try {
					obj.put("userId", PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID));
					obj.put("pointName", autoTV_netName.getText().toString());
					obj.put("pointNumber", tv_enterNetNo.getText().toString());
					obj.put("expId", expId);
					obj.put("pointPhone", tv_enterNetphone.getText().toString());
					obj.put("expName", tv_exp.getText().toString());
					obj.put("cityCode", cityCode);
					obj.put("latitude", latitude);
					obj.put("longitude",longitude);
					obj.put("courierNumber", tv_enterCourierNo.getText().toString());
					Log.e("obj", obj.toString());
					dialog.show();
					AsyncHttpUtils.doPostJson(CourierIdentificationActivity.this, MCUrl.USERSAUTHENTICATE,
							obj.toString(), new AsyncHttpResponseHandler() {
								@Override
								public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
									Log.e("KSLDKLDS", new String(arg2));
									dialog.dismiss();
									BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
									if (bean.getErrCode() == 0) {
										ToastUtil.shortToast(CourierIdentificationActivity.this,
												"快递员认证已提交，将会在一个工作日后审核通过");
//										Intent intent = new Intent(CourierIdentificationActivity.this, MainTab.class);
										Intent intent = new Intent(CourierIdentificationActivity.this, NewMainActivity.class);
										intent.putExtra("type", "2");
										startActivity(intent);
									     finish();
									}
									ToastUtil.shortToast(CourierIdentificationActivity.this,
											bean.getMessage());
								}

								@Override
								public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							});
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				ToastUtil.shortToast(CourierIdentificationActivity.this, "请填全资料");
			}
		

	}

	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub
	}

	@Override
	public void initData() {
		tv_exp.setFocusable(false);
		mList = new ArrayList<String>();
		autoCompletDatas = new ArrayList<String>();
		client = new LocationClient(getApplicationContext());
		initLocation();
		client.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation arg0) {
				if (arg0 == null)
					return;
				LatLng cenpt = new LatLng(arg0.getLatitude(), arg0.getLongitude());
				latitude = arg0.getLatitude();
				longitude = arg0.getLongitude();
				Log.e("luuudu", latitude + "");
				city = arg0.getCity();
				getCityCode();

			}
		});
		// 初始化定位
		// 打开GPS
		client.start();
		autoTV_netName.setThreshold(1);
		autoTV_netName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				autoCompletDatas.clear();
				JSONObject obj = new JSONObject();
				try {
					obj.put("expId", expId);
					obj.put("name", s.toString());
					obj.put("cityCode", cityCode);
					obj.put("latitude", latitude);
					obj.put("longitude", longitude);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				AsyncHttpUtils.doPostJson(CourierIdentificationActivity.this, MCUrl.MAPPOINTKEYWORD, obj.toString(),
						new AsyncHttpResponseHandler() {

							@Override
							public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
								Log.e("klkooookl", new String(arg2));
								try {
									JSONObject object = new JSONObject(new String(arg2));
									JSONArray array = object.getJSONArray("data");

									for (int i = 0; i < array.length(); i++) {
										autoCompletDatas.add(array.getString(i));
									}
									// Log.e("klkkl",
									// autoCompletDatas.toString());
									// String sourceStr = autoCompletDatas
									// .toString().replace("[", "").replace("]",
									// "");
									// String[] sourceStrArray = sourceStr
									// .split(",");
									// for (int i = 0; i <
									// sourceStrArray.length; i++) {
									// mList.add(sourceStrArray[i]);
									// }

								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								adapter = new ArrayAdapter<String>(CourierIdentificationActivity.this,
										android.R.layout.simple_list_item_1, autoCompletDatas);
								autoTV_netName.setAdapter(adapter);
								adapter.notifyDataSetChanged();
							}

							@Override
							public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
								// TODO Auto-generated method stub

							}
						});

			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {

			}

		});

	}

	private void getCityCode() {
		boolean isCopySuccess = CheckDbUtils.checkDb();
		// 成功的将数据库copy到data 中
		if (isCopySuccess) {
			iWantApplication.getInstance().mDbManager = new DbManager(iWantApplication.getInstance());
		}
		if (city == null || city.equals("")) {
			return;
		}
		Log.e("city", city);
		if (!city.contains("市")) {
			city = city + "市";
		}
		List<CityBean> selectDataFromDb = new CityDbOperation()
				.selectDataFromDb("select * from city where city_name='" + city + "'");
		cityCode = selectDataFromDb.get(0).city_code;
		Log.e("citycode", cityCode);
	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(com.baidu.location.LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
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
		client.setLocOption(option);
	}

	private int expId;

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		switch (arg0) {
		case 1:
			if (arg1 == RESULT_OK && arg2 != null) {
				tv_exp.setText(arg2.getStringExtra("exp"));
				expId = arg2.getIntExtra("expId", 0);
			}

			break;

		default:
			break;
		}
	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

}
