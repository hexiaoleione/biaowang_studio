package com.hex.express.iwant.activities;

import java.util.List;

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
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.CityBean;
import com.hex.express.iwant.bean.DefaultExpressBean;
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
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 【抢单发布】
 * 
 * @author huyichuan
 *
 */
public class PickOrderActivity extends BaseActivity {

	@Bind(R.id.tbv_show)
	TitleBarView tvb;// 此Activity标题；
	@Bind(R.id.rl_bannerTip)
	RelativeLayout rl_bannerTip;// Banner现金信息列；
	@Bind(R.id.tv_tip)
	TextView tv_tip;// Banner现金信息内容；
	@Bind(R.id.et_senderName)
	EditText et_senderName;// 发件人名字
	@Bind(R.id.iv_senderList)
	ImageView iv_senderList;// 发件人获取手机通讯录；
	@Bind(R.id.et_senderPhone)
	EditText et_senderPhone;// 发件人手机号码；
	@Bind(R.id.et_senderLocation)
	EditText et_senderLocation;// 发件人地址；
	@Bind(R.id.add_address)
	TextView add_address;// 根据定位获取位置；
	@Bind(R.id.et_detailLocationOfSender)
	EditText et_detailLocationOfSender;// 发件人详细地址；

	@Bind(R.id.et_receiverName)
	EditText et_receiverName;// 收件人名字
	@Bind(R.id.iv_receiverList)
	ImageView iv_receiverList;// 收件人获取手机通讯录；
	@Bind(R.id.et_receiverPhone)
	EditText et_receiverPhone;// 收件人手机号码；
	@Bind(R.id.et_receiverLocation)
	EditText et_receiverLocation;// 收件人地址；
	@Bind(R.id.text_receiver_address)
	TextView text_receiver_address;// 根据定位获取位置；
	@Bind(R.id.et_detailLocationOfReceiver)
	EditText et_detailLocationOfReceiver;// 收件人详细地址；

	@Bind(R.id.et_goods)
	EditText et_goods;// 物品信息；
	// 发布按钮
	@Bind(R.id.btn_send)
	Button btn_send;
	private String name;
	private String phone_number;
	private LocationClient client;
	// 定位得到的地址
	private String address;
	// 选择地址回调得到的地址
	private String return_address;

	private double latitude;
	private double longitude;
	private String city;//寄件城市名称
	private String cityCode;//发件地址的城市代码
	private String cityReceiverCode;//收件地址的城市代码
	private String cityTo;//收件城市名称
	private String defa_address;// 默认的地址如果为空显示定位的地址
	private boolean frist = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_order);
		iWantApplication.getInstance().addActivity(this);
		ButterKnife.bind(this);
		initData();
	}

	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub

	}

	@OnClick({ R.id.rl_bannerTip, R.id.btn_send, R.id.iv_senderList, R.id.add_address, R.id.iv_receiverList,
			R.id.text_receiver_address, R.id.iv_sendMessage, R.id.iv_receiverMessage })
	public void MyOnclick(View view) {
		Intent intent = null;
		Bundle bundle = new Bundle();
		switch (view.getId()) {
		case R.id.rl_bannerTip:// 现金信息
			startActivity(new Intent(PickOrderActivity.this, PreferrntialTwoActivity.class));
			break;
		case R.id.btn_send:// 发布按钮
			HttpPostResult();
			break;
		case R.id.iv_senderList:// 获取寄件人手机通讯录
			intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent, 0);
			break;
		case R.id.add_address:// 获取寄件人定位的地点
			intent = new Intent(PickOrderActivity.this, AddressActivity.class);
			bundle.putString("address", address);
			intent.putExtras(bundle);
			startActivityForResult(intent, 3);
			break;
		case R.id.iv_receiverList:// 获取收件人手机通讯录
			intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent, 1);
			break;
		case R.id.text_receiver_address:// 获取收件人定位的地点
			intent = new Intent(PickOrderActivity.this, AddressReceiveActivity.class);
			intent.putExtras(bundle);
			startActivityForResult(intent,4);
			break;
		case R.id.iv_sendMessage:// 从寄件人列表中选择寄件人
			startActivityForResult(new Intent(PickOrderActivity.this, SendPersonActivity.class), 5);
			break;
		case R.id.iv_receiverMessage:// 添加收件人库里面的收件人
			startActivityForResult(new Intent(PickOrderActivity.this, AddReceiverActivity.class), 6);
			break;
		default:
			break;
		}
	}

	/**
	 * 发布按钮提交数据
	 */
	private void HttpPostResult() {
		if (!StringUtil.isMobileNO(et_senderPhone.getText().toString())
				|| (et_senderPhone.getText().toString().length() != 11)
						&& !StringUtil.isMobileNO(et_receiverPhone.getText().toString())
				|| (et_receiverPhone.getText().toString().length() != 11)) {
			ToastUtil.shortToast(getApplicationContext(), "请输入正确的手机号码");
			return;
		}
		if (PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID) == -1
				|| et_detailLocationOfReceiver.getText().toString().equals("")
				|| et_detailLocationOfSender.getText().toString().equals("") || et_goods.getText().toString().equals("")
				|| city.equals("") || et_receiverLocation.getText().toString().equals("")
				|| et_receiverName.getText().toString().equals("") || et_receiverPhone.getText().toString().equals("")
				|| et_senderLocation.getText().toString().equals("") || et_senderName.getText().toString().equals("")
				|| et_senderPhone.getText().toString().equals("") || cityTo.equals("")) {
			ToastUtil.shortToast(getApplicationContext(), "请完善所有信息");
			return;
		}
		JSONObject obj = new JSONObject();
		try {
			obj.put("userId", PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID));
			obj.put("personName", et_senderName.getText().toString());
			obj.put("mobile", et_senderPhone.getText().toString());
			obj.put("areaName", et_senderLocation.getText().toString());
			obj.put("address", et_detailLocationOfSender.getText().toString());
			Log.e("address", et_detailLocationOfSender.getText().toString()+"----------");
			obj.put("latitude", latitude);
			obj.put("longitude", longitude);
			obj.put("fromCity", cityCode);
			obj.put("fromCityName", city);
			obj.put("personNameTo", et_receiverName.getText().toString());
			obj.put("mobileTo", et_receiverPhone.getText().toString());
			obj.put("areaNameTo", et_receiverLocation.getText().toString());
			obj.put("addressTo", et_detailLocationOfReceiver.getText().toString());
			Log.e("addressTo", et_detailLocationOfReceiver.getText().toString()+"----------");
			obj.put("toCity", cityReceiverCode);
			obj.put("toCityName", cityTo);
			obj.put("matName", et_goods.getText().toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e("obj-------", "所有信息："+obj.toString());
		dialog.show();
		AsyncHttpUtils.doPostJson(getApplicationContext(), MCUrl.NEWEXPRESS, obj.toString(),
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("fabuddldl", new String(arg2));
						dialog.dismiss();
						if (arg2 == null || arg2.length == 0) {
							ToastUtil.shortToast(getApplicationContext(), "发布失败");
							return;
						}
						BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
						if (bean.isSuccess() == true) {
							ToastUtil.shortToast(getApplicationContext(), bean.getMessage());
							startActivity(new Intent(PickOrderActivity.this, MyCourierActivity.class));
							finish();
						} else {
							ToastUtil.shortToast(getApplicationContext(), bean.getMessage());
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						dialog.dismiss();
						ToastUtil.shortToast(getApplicationContext(), "发布失败");
						// TODO Auto-generated method stub
						Log.e("ooo1", arg0 + "");
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0:
			if (data == null) {
				return;
			}
			if (resultCode == Activity.RESULT_OK) {
				ContentResolver reContentResolverol = getContentResolver();
				Uri contactData = data.getData();
				// 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
				Cursor cursor = managedQuery(contactData, null, null, null, null);
				cursor.moveToFirst();
				// 获得DATA表中的名字
				name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				// 条件为联系人ID
				et_senderName.setText(name);
				String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
				// 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
				Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
				while (phone.moveToNext()) {
					phone_number = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					if (!phone_number.equals(""))
						et_senderPhone.setText(phone_number);
				}

			}

			break;
		case 1:
			if (data == null) {
				return;
			}
			if (resultCode == Activity.RESULT_OK) {
				ContentResolver reContentResolverol = getContentResolver();
				Uri contactData = data.getData();
				// 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
				Cursor cursor = managedQuery(contactData, null, null, null, null);
				cursor.moveToFirst();
				// 获得DATA表中的名字
				name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				// 条件为联系人ID
				et_receiverName.setText(name);
				String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
				// 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
				Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
				while (phone.moveToNext()) {
					phone_number = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					if (!phone_number.equals(""))
						et_receiverPhone.setText(phone_number);
				}
			}
			break;
		case 3:// 修改寄件人的寄件地址
			if (resultCode == Activity.RESULT_OK) {
				return_address = data.getStringExtra("address").replace("中国", "");
				et_senderLocation.setText(return_address);
				city = data.getStringExtra("city");
				Log.e("city", city);
				getCityCode(true, city);
				frist = true;
				return;
			}
			break;
		case 4:// 修改收件人的收件地址
			if (resultCode == Activity.RESULT_OK) {
				et_receiverLocation.setText(data.getStringExtra("address").replace("中国", ""));
				cityTo = data.getStringExtra("city");
				Log.e("cityTo", cityTo);
				getCityCode(false, cityTo);
				return;
			}
			break;
		case 5:// 从寄件人列表中选择寄件人
			if (resultCode == Activity.RESULT_OK) {
				return_address = data.getStringExtra("location");
				et_senderName.setText(data.getStringExtra("name"));
				et_senderPhone.setText(data.getStringExtra("phone"));
				et_detailLocationOfSender.setText(data.getStringExtra("address"));
				getCityCode(true, data.getStringExtra("city"));
				frist = true;
			}
			break;
		case 6:// 从收件人列表中选择收件人
			if (resultCode == Activity.RESULT_OK) {
				et_receiverName.setText(data.getStringExtra("name"));
				et_receiverPhone.setText(data.getStringExtra("phone"));
				et_receiverLocation.setText(data.getStringExtra("location"));
				et_detailLocationOfReceiver.setText(data.getStringExtra("address"));
				cityReceiverCode=data.getStringExtra("citycode");
				cityTo=data.getStringExtra("city");
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 获取城市代码
	 * @param flag
	 * @param city
	 */
	private void getCityCode(boolean flag, String city) {
		boolean isCopySuccess = CheckDbUtils.checkDb();
		// 成功的将数据库copy到data 中
		if (isCopySuccess) {
			iWantApplication.getInstance().mDbManager = new DbManager(iWantApplication.getInstance());
		}
		if (city == null || city.equals("")) {
			ToastUtil.shortToast(getApplicationContext(), "请输入完整信息");
			return;
		}
		Log.e("city", city);
		if (!city.contains("市")) {
			city = city + "市";
		}
		List<CityBean> selectDataFromDb = new CityDbOperation()
				.selectDataFromDb("select * from city where city_name='" + city + "'");
		if (selectDataFromDb.size() > 0 && !selectDataFromDb.equals("")) {
			if (flag) {
				cityCode = selectDataFromDb.get(0).city_code;
				Log.e("citycode", cityCode);
			} else {
				cityReceiverCode = selectDataFromDb.get(0).city_code;
				Log.e("cityReceiverCode", cityReceiverCode);
			}

		}
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		et_senderLocation.setFocusable(false);
		et_receiverLocation.setFocusable(false);
		tvb.setTitleText("抢单发布");
		latitude=getIntent().getDoubleExtra("latitude", 0);
		longitude=getIntent().getDoubleExtra("longitude", 0);
//		city=getIntent().getStringExtra("city");
		//
		client = new LocationClient(getApplicationContext());
		setLocationParams();
		client.registerLocationListener(new BDLocationListener() {
			@Override
			public void onReceiveLocation(final BDLocation arg0) {
				// TODO Auto-generated method stub
				if (arg0 == null) {
					ToastUtil.shortToast(getApplicationContext(), "定位失败，请检查定位设置");
					return;
				} else {
					if (dialog != null)
						dialog.dismiss();
					city = arg0.getCity();
//					latitude = arg0.getLatitude();
//					longitude = arg0.getLongitude();
					address = arg0.getAddrStr();
					getCityCode(true, city);
////					Log.e("PP----address", address);
//					Log.e("PP----latitude", latitude + "");
//					Log.e("PP----longitude", longitude + "");
					if (!address.equals("")) {
						if (frist) {
							et_senderLocation.setText(return_address);
							Log.e("et_senderLocation--", et_senderLocation.getText().toString());
						} else {
							et_senderLocation.setText(address);
						}
					}
				}
			}
		});
		client.start();
		getDefaultMessage();
	}

	/**
	 * 百度地图定位设置
	 */
	private void setLocationParams() {
		// TODO Auto-generated method stub
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 60000;
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
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

	/**
	 * 获取默认寄件人信息
	 */
	private void getDefaultMessage() {
		dialog.show();
		AsyncHttpUtils.doSimGet(
				UrlMap.getfour(MCUrl.EXPRESS_DEFAULT, "userId",
						PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID) + "", "latitude",
						latitude + "", "longitude", longitude + "", "cityCode", cityCode),
				new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						if (dialog != null)
							dialog.dismiss();
						if (arg2 == null || arg2.length == 0)
							return;
						Log.e("nn", new String(arg2));
						DefaultExpressBean bean = new Gson().fromJson(new String(arg2), DefaultExpressBean.class);
						if (bean.getErrCode() == 0) {
							if (!bean.data.get(0).address.equals("")) {
								et_senderName.setText(bean.data.get(0).personName);
								et_senderPhone.setText(bean.data.get(0).mobile);
								et_senderLocation.setText(bean.data.get(0).areaName);
								et_detailLocationOfSender.setText(bean.data.get(0).address);
							} else {
								et_senderName.setText(PreferencesUtils.getString(getApplicationContext(),
										PreferenceConstants.USERNAME));
								et_senderPhone.setText(PreferencesUtils.getString(getApplicationContext(),
										PreferenceConstants.MOBILE));
							}
						}

					}
				});

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
