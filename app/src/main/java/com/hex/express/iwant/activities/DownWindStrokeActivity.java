package com.hex.express.iwant.activities;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.DownStrokeBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.TimePickerView;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的镖师发布顺风按钮界面
 * 
 * @author SCHT-50
 * 
 */
public class DownWindStrokeActivity extends BaseActivity {
	@Bind(R.id.et_message)
	EditText et_message;
	@Bind(R.id.edit_go)
	EditText edit_go;
	@Bind(R.id.et_time)
	EditText et_time;
	@Bind(R.id.edit_down)
	EditText edit_down;
	@Bind(R.id.tbv_show)
	TitleBarView tbv_show;
	@Bind(R.id.img_car)
	ImageView img_car;
	@Bind(R.id.img_bicycle)
	ImageView img_bicycle;
	@Bind(R.id.img_feiji)
	ImageView img_feiji;
	TimePickerView pvTime;
	// 当前位置经纬度
	private double latitude;
	private double longitude;
	// 出发位置经纬度
	private double startLatitude;
	private double startLongitude;
	// 目的地经纬度
	private double endLatitude;
	private double endLongitude;
	public int TRANSPORTATION = 0;
	public String transportationName;
	private LocationClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_down_stroke);
		ButterKnife.bind(DownWindStrokeActivity.this);
		initData(); 
		// 时间选择器
		pvTime = new TimePickerView(this, TimePickerView.Type.MONTH_DAY_HOUR_MIN);
		// 控制时间范围
		// Calendar calendar = Calendar.getInstance();
		// pvTime.setRange(calendar.get(Calendar.YEAR) - 20,
		// calendar.get(Calendar.YEAR));
		pvTime.setTime(new Date());
		pvTime.setCyclic(false);
		pvTime.setCancelable(true);
		// 时间选择后回调
		pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

			@Override
			public void onTimeSelect(Date date) {
				et_time.setText(getTime(date));
			}
		});
	}

	@Override
	public void onWeightClick(View v) {

	}

	public static String getTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return format.format(date);
	}

	@OnClick({ R.id.et_time, R.id.img_car, R.id.img_bicycle, R.id.img_feiji, R.id.btn_submit, R.id.edit_down,
			R.id.edit_go })
	public void MyOnClick(View view) {
		Intent intent = new Intent();
		switch (view.getId()) {
		case R.id.edit_go:// 出发位置
			intent.setClass(DownWindStrokeActivity.this, AddressActivity.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.edit_down:// 目的位置
			intent.setClass(DownWindStrokeActivity.this, AddressReceiveActivity.class);
			startActivityForResult(intent, 2);
			break;
		case R.id.et_time:// 获取日期
			pvTime.show();
			break;
		case R.id.img_car:// 汽车
			TRANSPORTATION = 1;
			transportationName = "快车";
			img_car.setBackgroundResource(R.drawable.car_lock);
			img_bicycle.setBackgroundResource(R.drawable.bick_unlock);
			img_feiji.setBackgroundResource(R.drawable.hui_fei);//fei_unlock
			break;
		case R.id.img_bicycle:// 自行车
			TRANSPORTATION = 2;
			transportationName = "自行车";
			img_car.setBackgroundResource(R.drawable.car_unlonck);
			img_bicycle.setBackgroundResource(R.drawable.bick_lock);
			img_feiji.setBackgroundResource(R.drawable.hui_fei);
			break;
		case R.id.img_feiji:// 步行
			TRANSPORTATION = 3;
			transportationName = "飞机";
			img_car.setBackgroundResource(R.drawable.car_unlonck);
			img_bicycle.setBackgroundResource(R.drawable.bick_unlock);
			img_feiji.setBackgroundResource(R.drawable.hui_fei);
			break;
		case R.id.btn_submit:// 确认发布
			addPostResult();
			break;
		default:
			break;
		}
	}

	/**
	 * 发布镖师顺风行程
	 */
	private void addPostResult() {
		if (edit_go.getText().toString().equals("") || edit_down.getText().toString().equals("")
				|| et_time.getText().toString().equals("") || transportationName.equals("")) {
			ToastUtil.shortToast(getApplicationContext(), "请完善所有信息");
			return;
		}
		JSONObject obj = new JSONObject();
		try {
			obj.put("userId", String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
			obj.put("toLatitude", endLatitude);
			obj.put("toLongitude", endLongitude);
			obj.put("fromLatitude", startLatitude);
			obj.put("fromLongitude", startLongitude);
			obj.put("latitude", latitude);
			obj.put("longitude", longitude);
			obj.put("address", edit_go.getText().toString());
			obj.put("addressTo", edit_down.getText().toString());
			obj.put("leaveTime", et_time.getText().toString());
			obj.put("transportationId", TRANSPORTATION);
			obj.put("transportationName", transportationName);
			obj.put("remark", et_message.getText().toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.e("obj", obj.toString());
		Log.e("obj", MCUrl.DRIVERROUTE);
		dialog.show();
		AsyncHttpUtils.doPostJson(DownWindStrokeActivity.this, MCUrl.DRIVERROUTE, obj.toString(),
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("msg", new String(arg2));
						dialog.dismiss();
						DownStrokeBean bean = new Gson().fromJson(new String(arg2), DownStrokeBean.class);
						if (bean.getErrCode() == 0) {
							ToastUtil.shortToast(DownWindStrokeActivity.this, bean.getMessage());
//							sendBroadcast(new Intent().setAction("freight").putExtra("flag", "EscortWay"));//镖师发布行程以后到mainactivity以后到再跳到我的顺风里面
							finish();
						} else {
							ToastUtil.shortToast(DownWindStrokeActivity.this, bean.getMessage());
						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if (resultCode == Activity.RESULT_OK) {
				edit_go.setText(data.getStringExtra("address").replace("中国", ""));
				startLatitude = data.getDoubleExtra("latitude", 0);
				startLongitude = data.getDoubleExtra("longitude", 0);
				Log.e(" 发件地经纬度", startLatitude + "+++++" + startLongitude);
				String city = data.getStringExtra("city");

			}
			break;
		case 2:
			if (resultCode == Activity.RESULT_OK) {
				edit_down.setText(data.getStringExtra("address").replace("中国", ""));
				endLatitude = data.getDoubleExtra("latitude", 0);
				endLongitude = data.getDoubleExtra("longitude", 0);
				Log.e(" 目的地经纬度", endLatitude + "+++++" + endLongitude);
				String city = data.getStringExtra("city");
			}
			break;
		default:
			break;
		}
	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(com.baidu.location.LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 60000;
		option.setScanSpan(0);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
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
	public void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		tbv_show.setTitleText("发布顺风行程");
		client = new LocationClient(getApplicationContext());
		initLocation();
		client.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation arg0) {
				latitude = arg0.getLatitude();
				longitude = arg0.getLongitude();
				Log.e("jpppp", latitude + ":::::::::" + longitude);
			}
		});
		// 初始化定位
		// 打开GPS
		client.start();

	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub

	}

}
