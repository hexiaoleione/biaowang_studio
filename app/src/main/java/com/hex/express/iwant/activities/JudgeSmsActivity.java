package com.hex.express.iwant.activities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.bean.AreaBean;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.CityBean;
import com.hex.express.iwant.bean.RegisterBean;
import com.hex.express.iwant.constance.CommonConstants;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.helper.AreaDboperation;
import com.hex.express.iwant.helper.CheckDbUtils;
import com.hex.express.iwant.helper.CityDbOperation;
import com.hex.express.iwant.helper.DbManager;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.newmain.MainTab;
import com.hex.express.iwant.newsmain.NewMainActivity;
import com.hex.express.iwant.utils.DataTools;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.StringUtil;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class JudgeSmsActivity extends BaseActivity {

	private Timer timer;
	private TimerTask task;
	private int currentTime;
	private TitleBarView tbv_show;
	private EditText edt_registephonenumber;
	private EditText edt_registecode;
	private Button btn_getsmscode;
	private Button btn_nextstep;
	private String phone_number;
	private String code;
	private String 	city,cityCode,townCode,townaddressd;

	private LocationClient client;//百度地图的Client
	private double latitude;
	private double longitude;

	// private String smsCode;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registerinfo);
		initView();
		initData();
		setOnClick();
	}

	private void resetCurrentTime() {
		currentTime = CommonConstants.GET_CODE_TIME;
	}

	public void handleUiMessage(Message msg) {
		super.handleUiMessage(msg);
		switch (msg.what) {
		case MsgConstants.MSG_01:
			if (currentTime == 0) {
				resetCurrentTime();
				btn_getsmscode.setText("获取验证码");
				task.cancel();
			} else
				btn_getsmscode.setText(currentTime-- + "秒");
			// btn_getsmscode.setClickable(false);
			break;
		case MsgConstants.MSG_02:
			Set tag = new HashSet();
			tag.add("user");
			JPushInterface.setAliasAndTags(
					getApplicationContext(),
					PreferencesUtils.getInt(getApplicationContext(),
							PreferenceConstants.UID) + "", tag,new TagAliasCallback() {
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

		/*
		 * case MsgConstants.MSG_02: etCode.setText(""); currentTime = 0; break;
		 */
		}
	}

	private RegisterBean bean;
	private LoadingProgressDialog dialog;
	private TextView tv_tuijian;
	private LinearLayout ll_tuijian;

	@Override
	public void onWeightClick(View v) {
		switch (v.getId()) {
		case R.id.btn_getsmscode:// 获取验证码
			String username = edt_registephonenumber.getText().toString()
					.trim();
			if (!StringUtil.isMobileNO(username) || (username.length() != 11)) { // 判断是否是正确的手机号码
				// Log.e("bbb",use)
				ToastUtil.shortToastByRec(JudgeSmsActivity.this,
						R.string.mobileisnotvolid);
				return;
			} else if (currentTime == CommonConstants.GET_CODE_TIME) {
				PreferencesUtils.putString(getApplicationContext(),
						PreferenceConstants.MOBILE, username);
				task = new TimerTask() {

					@Override
					public void run() {
						sendEmptyUiMessage(MsgConstants.MSG_01);
					}
				};
				timer.schedule(task, 0, 1000);
				final String smsCode = edt_registecode.getText().toString()
						.trim();
				Log.e("sms", "codddddd");
				AsyncHttpUtils.doSimGet(UrlMap.getUrl(MCUrl.LOGINAUTH,
						PreferenceConstants.MOBILE, username),
						new AsyncHttpResponseHandler() {

							@Override
							public void onSuccess(int arg0, Header[] arg1,
									byte[] arg2) {
								if (arg1 == null)
									return;
								BaseBean bean = new Gson().fromJson(new String(
										arg2), BaseBean.class);
								if (bean.getErrCode() == 0) {
									ToastUtil.shortToast(
											getApplicationContext(), "验证码发送成功");
								} else {
									ToastUtil.shortToast(
											getApplicationContext(),
											bean.getMessage());
								}
							}

							@Override
							public void onFailure(int arg0, Header[] arg1,
									byte[] arg2, Throwable arg3) {
								Log.e("ccc", arg0 + "");
							}
						});
			}
			break;
		case R.id.btn_nextstep:// 下一步
			dialog = new LoadingProgressDialog(JudgeSmsActivity.this);
			dialog.show();
			String userName = edt_registephonenumber.getText().toString()
					.trim();
			final String smsCode = edt_registecode.getText().toString().trim();
			if (!smsCode.equals("") && smsCode.length() == 4) {
				JSONObject obj = new JSONObject();
				try {
					obj.put("openId", openId);
					obj.put("accessToken", token);
					obj.put("nickName", nickName);
					obj.put("sex", sex);
					obj.put("headImageUrl", headImageUrl);
					obj.put("mobile", mobile);
					obj.put("code", smsCode);
					obj.put("deviceId", deviceId);
					obj.put("cityCode", cityCode);
					Log.e("obj", obj.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.e("obju", MCUrl.LOGINBINDING);
				AsyncHttpUtils.doPostJson(getApplicationContext(),
						MCUrl.LOGINBINDING, obj.toString(),
						new AsyncHttpResponseHandler() {

							@Override
							public void onSuccess(int arg0, Header[] arg1,
									byte[] arg2) {
								Log.e("json", new String(arg2));
								// TODO Auto-generated method stub
								if (arg2 == null)
									return;
								RegisterBean bean = new Gson().fromJson(
										new String(arg2), RegisterBean.class);
								if (bean.getErrCode() == 0 && bean.data != null
										&& bean.data.size() != 0) {
									dialog.dismiss();
									PreferencesUtils.putString(
											getApplicationContext(),
											PreferenceConstants.PASSWORD, bean
													.getData().get(0).userCode);
									PreferencesUtils.putInt(
											getApplicationContext(),
											PreferenceConstants.UID,
											bean.data.get(0).userId);
									sendEmptyBackgroundMessage(MsgConstants.MSG_02);
									PreferencesUtils.putBoolean(
											getApplicationContext(),
											PreferenceConstants.ISLOGIN, true);
									PreferencesUtils.putString(
											getApplicationContext(),
											PreferenceConstants.MOBILE, bean
													.getData().get(0).mobile);
									PreferencesUtils.putString(
											getApplicationContext(),
											PreferenceConstants.USERNAME, bean
													.getData().get(0).userName);
									PreferencesUtils
											.putBoolean(
													getApplicationContext(),
													PreferenceConstants.COMPLETE,
													bean.getData().get(0).completed);
									PreferencesUtils.putString(
											getApplicationContext(),
											PreferenceConstants.IDCARD, bean
													.getData().get(0).idCard);
									PreferencesUtils.putString(
											getApplicationContext(),
											PreferenceConstants.ICON_PATH,
											bean.getData().get(0).idCardPath);
									PreferencesUtils.putString(
											getApplicationContext(),
											PreferenceConstants.REALMANAUTH,
											bean.data.get(0).realManAuth);
									PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.USERTYPE,bean.getData().get(0).userType);
									sendEmptyBackgroundMessage(MsgConstants.MSG_01);
									PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.WLID, bean.getData().get(0).wlid);
									PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.AgreementType,bean.getData().get(0).agreementType);
									// PreferencesUtils.putString(getApplicationContext(),
									// PreferenceConstants.USERTYPE,
									// bean.getData().get(0).userType);
									// Log.e("json5555",bean.getData().get(0).userType);

//									Intent intent = new Intent(JudgeSmsActivity.this, MainActivity.class);
//									Intent intent = new Intent(JudgeSmsActivity.this, MainTab.class);
									Intent intent = new Intent(JudgeSmsActivity.this, NewMainActivity.class);
									intent.putExtra("type", "1");
									startActivity(intent);
									finish();
								} else {
									ToastUtil.shortToast(
											getApplicationContext(),
											bean.getMessage());
								}

							}

							@Override
							public void onFailure(int arg0, Header[] arg1,
									byte[] arg2, Throwable arg3) {
								// TODO Auto-generated method stub

							}
						});
			} else {
				ToastUtil.shortToast(getApplication(), "请输入正确的验证码");
			}
			break;
		}
	}

	@Override
	public void initView() {
		// 标题
		tbv_show = (TitleBarView) findViewById(R.id.tbv_show);
		this.tbv_show.setTitleText("手机号验证");
		// 手机号和验证码
		edt_registephonenumber = (EditText) findViewById(R.id.edt_registephonenumber);
		edt_registecode = (EditText) findViewById(R.id.edt_registecode);
		// 按钮
		btn_getsmscode = (Button) findViewById(R.id.btn_getsmscode);
		btn_nextstep = (Button) findViewById(R.id.btn_nextstep);

		// 隐藏推荐人
		tv_tuijian = (TextView) findViewById(R.id.tv_tuijian);
		ll_tuijian = (LinearLayout) findViewById(R.id.ll_tuijian);

		tv_tuijian.setVisibility(View.GONE);
		ll_tuijian.setVisibility(View.GONE);

		// 设置edittext的监听
		client = new LocationClient(JudgeSmsActivity.this);
		initLocation();
		client.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation arg0) {
				if (arg0 == null) {
					ToastUtil.shortToast(JudgeSmsActivity.this, "定位失败，请检查设置");
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

	private String deviceId;
	private String openId;
	private String token;
	private String nickName;
	private String sex;
	private String headImageUrl;
	private String mobile;

	@Override
	public void initData() {
		deviceId = DataTools.getDeviceId(getBaseContext());
		btn_nextstep.setText("完成");
		openId = getIntent().getStringExtra("openId");
		token = getIntent().getStringExtra("accessToken");
		nickName = getIntent().getStringExtra("nickName");
		sex = getIntent().getStringExtra("sex");
		headImageUrl = getIntent().getStringExtra("headImageUrl");
		mobile = getIntent().getStringExtra("mobile");
		edt_registephonenumber.setText(mobile);
		btn_nextstep.setText("登录");
		edt_registephonenumber.setFocusable(false);
		timer = new Timer();
		// edt_registephonenumber.getText().toString()
		resetCurrentTime();
	}

	@Override
	public void setOnClick() {
		btn_getsmscode.setOnClickListener(this);
		btn_nextstep.setOnClickListener(this);
	}

	@Override
	public void getData() {

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (timer != null)
			timer.cancel();
		if (task != null)
			task.cancel();
		client.stop();
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


}
