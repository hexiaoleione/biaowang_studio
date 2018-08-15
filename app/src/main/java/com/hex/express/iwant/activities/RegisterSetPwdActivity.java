package com.hex.express.iwant.activities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.bean.AreaBean;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.CityBean;
import com.hex.express.iwant.bean.RegisterBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.helper.AreaDboperation;
import com.hex.express.iwant.helper.CheckDbUtils;
import com.hex.express.iwant.helper.CityDbOperation;
import com.hex.express.iwant.helper.DbManager;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.newactivity.ReleaseActivity;
import com.hex.express.iwant.utils.DataTools;
import com.hex.express.iwant.utils.MD5Util;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 注册2/3
 * 
 * @author Eric
 *
 */
public class RegisterSetPwdActivity extends BaseActivity {
	private TitleBarView tbv_show;
	private EditText edt_pwdlength;
	private Button btn_nextstep;
	private String userName;
	private String smsCode;
	private String password;
	private String result;
	private String mobile;
	private String recommendMobile;
	TextView text_tiao;
	ImageView text_lout;
	String code;
	
	private LocationClient client;//百度地图的Client
	private double latitude;
	private double longitude;
	private String 	city,cityCode,townCode,townaddressd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registersetpwd);
		iWantApplication.getInstance().addActivity(this);
		getData();
		initView();
		initData();
		setOnClick();
	}

//	@Override
//	public void onWeightClick(View v) {
//		switch (v.getId()) {
//		case R.id.btn_nextstep:// 下一步
//			String pwd = edt_pwdlength.getText().toString().trim();
//			if (pwd == null || TextUtils.isEmpty(pwd)) {
//				ToastUtil.shortToastByRec(getBaseContext(), R.string.pwdnotnull);
//			} else if (TextUtils.isEmpty(pwd)) {
//				ToastUtil.shortToast(getBaseContext(), "请输入密码");
//			} else if (pwd.length() < 6 || pwd.length() > 20) {
//				ToastUtil.shortToastByRec(getBaseContext(), R.string.pwdshortandlong);
//			} else {
//				password = pwd;
//				// LoadingProgressDialog.getInstance(RegisterSetPwdActivity.this).show();
//				sendEmptyUiMessage(MsgConstants.MSG_01);
//			}
//			break;
//		}
//	}

	@Override
	public void handleBackgroundMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleBackgroundMessage(msg);
		switch (msg.what) {
		case MsgConstants.MSG_01:
			Set tag = new HashSet();
			tag.add("user");
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
			break;

		default:
			break;
		}
	}

	@Override
	public void initView() {
		// 标题
		tbv_show = (TitleBarView) findViewById(R.id.tbv_show);
		// 密码
		edt_pwdlength = (EditText) findViewById(R.id.edt_pwdlength);
		// 按钮
		btn_nextstep = (Button) findViewById(R.id.btn_nextstep);
		// 服务条款
		text_tiao = (TextView) findViewById(R.id.text_tiao);
		// 点击下一步的时候弹出遮盖物
		text_lout = (ImageView) findViewById(R.id.text_lout);
	}

	@Override
	public void handleUiMessage(Message msg) {
		super.handleUiMessage(msg);
		switch (msg.what) {
		case MsgConstants.MSG_01:
			btn_nextstep.setClickable(false);
			JSONObject obj = new JSONObject();
			try {
				obj.put("recommendMobile", recommendMobile);
				obj.put("mobile", mobile);
				obj.put("password", MD5Util.MD5Encode(password));
				obj.put("smsCode", code);
				obj.put("cityCode", cityCode);
//				//海南用户
//				obj.put("agreementType", "2");
				String id = DataTools.getDeviceId(getApplicationContext());
				obj.put("deviceId", id);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Log.e("success", ""+obj);
			text_lout.setVisibility(View.VISIBLE);
			dialog.show();
			AsyncHttpUtils.doPostJson(getApplicationContext(), MCUrl.register, obj.toString(),//register REGISTER
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
							text_lout.setVisibility(View.GONE);
							Log.e("success", "success");
							Log.e("json", new String(arg2));
							RegisterBean bean = new Gson().fromJson(new String(arg2), RegisterBean.class);
							// LoadingProgressDialog.getInstance(RegisterSetPwdActivity.this).dismiss();
							if (dialog != null)
								dialog.dismiss();
							btn_nextstep.setClickable(true);
							if (bean.getErrCode()==0) {
//							Log.e("userid", bean.getData().get(0).userId + "");
							PreferencesUtils.putInt(getApplicationContext(), PreferenceConstants.UID,
									bean.getData().get(0).userId);
							PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.MOBILE,
									bean.getData().get(0).mobile);
							sendEmptyBackgroundMessage(MsgConstants.MSG_01);
							PreferencesUtils.putBoolean(getApplicationContext(), PreferenceConstants.ISLOGIN, true);
							PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.PASSWORD,
									bean.getData().get(0).password);
							PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.WLID, bean.getData().get(0).wlid);
							PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.USERTYPE,
									bean.getData().get(0).userType);
							PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.REALMANAUTH,
									bean.getData().get(0).realManAuth);
							PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.AgreementType,bean.getData().get(0).agreementType);
							PreferencesUtils.putBoolean(getApplicationContext(),PreferenceConstants.ISLOGIN, true);
							Intent intent = new Intent(RegisterSetPwdActivity.this,
									RoleAuthenticationActivity.class);
						//商户标识
							PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.shopType,bean.getData().get(0).shopType);
						 
							// 跳转注册3/3
//							intent.putExtra("flag", true);
							intent.putExtra("tiaoguo", "1");
							startActivity(intent);
//							finish();
							JSONObject obj = new JSONObject();
							try {
								// obj.put("userId",
								// PreferencesUtils.getInt(getApplicationContext(),
								// PreferenceConstants.UID));
								obj.put("userId", PreferencesUtils.getInt(
										getApplicationContext(), PreferenceConstants.UID));
								obj.put("userType", PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE));
								obj.put("latitude", latitude);
								obj.put("longitude", longitude);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							AsyncHttpUtils.doPostJson(getApplicationContext(), MCUrl.UPLOADMYLOCATION,
									obj.toString(), new AsyncHttpResponseHandler() {

										@Override
										public void onSuccess(int arg0, Header[] arg1,
												byte[] arg2) {
											Log.e("_000000000000000___", new String(arg2));
											dialog.dismiss();
											if (arg2 == null || arg2.length == 0) {
//												ToastUtil.shortToast(getApplicationContext(),
//														"获取位置失败");
												Log.e("__111111111111111__", "获取位置失败");
												return;
											}
											BaseBean bean = new Gson().fromJson(
													new String(arg2), BaseBean.class);
											if (bean.isSuccess() == true) {
//												ToastUtil.shortToast(getApplicationContext(),
//														bean.getMessage());
												Log.e("__222222222222___", bean.getMessage());
												finish();
											} else {
//												ToastUtil.shortToast(getApplicationContext(),
//														bean.getMessage());
												Log.e("_3333333333333__", "获取位置失败");
											}
										}

										@Override
										public void onFailure(int arg0, Header[] arg1,
												byte[] arg2, Throwable arg3) {
											dialog.dismiss();
//											ToastUtil.shortToast(getApplicationContext(),
//													"获取位置失败");
											BaseBean bean = new Gson().fromJson(
													new String(arg2), BaseBean.class);
											Log.e("_4444444444444__", bean.getMessage());
											// TODO Auto-generated method stub
											Log.e("ooo1", arg0 + "");
										}
									});
							
							finish();
							
							}else {
								ToastUtil.shortToast(RegisterSetPwdActivity.this, bean.getMessage());
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
							dialog.dismiss();
							text_lout.setVisibility(View.GONE);
							btn_nextstep.setClickable(true);
						}
					});
		default:
			break;
		}
	}

	@Override
	public void initData() {
		tbv_show.setTitleText(R.string.next2);
		mobile = getIntent().getStringExtra("mobile");
		recommendMobile = getIntent().getStringExtra("recommendMobile");
		code = getIntent().getStringExtra("code");
		
//		ToastUtil.shortToast(RegisterSetPwdActivity.this, "code"+code);
	}

	@Override
	public void setOnClick() {
		text_tiao.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(RegisterSetPwdActivity.this, ServiceActivity.class));
			}
		});
		btn_nextstep.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String pwd = edt_pwdlength.getText().toString().trim();
				if (pwd == null || TextUtils.isEmpty(pwd)) {
					ToastUtil.shortToastByRec(getBaseContext(), R.string.pwdnotnull);
				} else if (TextUtils.isEmpty(pwd)) {
					ToastUtil.shortToast(getBaseContext(), "请输入密码");
				} else if (pwd.length() < 6 || pwd.length() > 20) {
					ToastUtil.shortToastByRec(getBaseContext(), R.string.pwdshortandlong);
				} else {
					password = pwd;
					// LoadingProgressDialog.getInstance(RegisterSetPwdActivity.this).show();
					if (latitude==4.9E-324 || latitude==5e-324) {
						ToastUtil.shortToast(RegisterSetPwdActivity.this, "请开启定位");
					}else {
						sendEmptyUiMessage(MsgConstants.MSG_01);
					}
					
				}
			}
		});
	}

	@Override
	public void getData() {
		Intent intent = getIntent();
		client = new LocationClient(RegisterSetPwdActivity.this);
		initLocation();
		client.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation arg0) {
				if (arg0 == null) {
					ToastUtil.shortToast(RegisterSetPwdActivity.this, "定位失败，请检查设置");
					return;
				} else {
				latitude = arg0.getLatitude();
				longitude = arg0.getLongitude();
				city=arg0.getCity();
				townaddressd=arg0.getDistrict();
				getCityCode();
//				townaddressd=arg0.getDistrict();
//				Log.e("jpppp", latitude + ":::::::::" + longitude);
				}
			}
		});
		// 初始化定位
		// 打开GPS
		client.start();
	}
	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(com.baidu.location.LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 0;
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
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub
		
	}
	private void getCityCode() {
		boolean isCopySuccess = CheckDbUtils.checkDb();
		// 成功的将数据库copy到data 中
		if (isCopySuccess) {
			iWantApplication.getInstance().mDbManager = new DbManager(iWantApplication.getInstance());
		}
		if (city == null || city.equals("")) {
//			ToastUtil.shortToast(getActivity(), "请输入完整信息");
			return;
		}
		if (!city.contains("市")) {
			city = city + "市";
		}
		List<CityBean> selectDataFromDb = new CityDbOperation()
				.selectDataFromDb("select * from city where city_name='" + city + "'");
		if (selectDataFromDb.size() > 0 && !selectDataFromDb.equals("")) {
			cityCode = selectDataFromDb.get(0).city_code;
			Log.e("citycode", cityCode);
			List<AreaBean> selectDataFromDbs = new AreaDboperation()
					.selectDataFromDb("select * from area where area_name='" + townaddressd + "'");
			if (selectDataFromDbs.size()>0 && !selectDataFromDbs.equals("")) {
				townCode=selectDataFromDbs.get(0).area_code;
				Log.e("11111townCode", townCode);
		}
		}
	}
  @Override
protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
	client.stop();
}
}
