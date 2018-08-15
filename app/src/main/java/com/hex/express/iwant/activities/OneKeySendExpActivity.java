package com.hex.express.iwant.activities;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.hex.express.iwant.R.id;
import com.hex.express.iwant.bean.AreaBean;
import com.hex.express.iwant.bean.CityBean;
import com.hex.express.iwant.bean.DefaultExpressBean;
import com.hex.express.iwant.bean.PublicCourierBean;
import com.hex.express.iwant.bean.addcoucorBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.helper.AreaDboperation;
import com.hex.express.iwant.helper.CheckDbUtils;
import com.hex.express.iwant.helper.CityDbOperation;
import com.hex.express.iwant.helper.DbManager;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.AppUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.StringUtil;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 一键快递
 * 
 * @author SCHT-40
 * 
 */
public class OneKeySendExpActivity extends BaseActivity {
	private EditText edt_sendpersonname;
	private EditText edt_sendpersonmobile;
	private EditText edt_addcouriers;
	private TextView txt_sendpersonaddress;
	private ImageView img_sendpersonname;
	private ImageView img_addcouriers;
//	private Button btn_oksend;
	private String courierName;
	private String courierMobile;
	private String expName;
	private EditText et_address;
	private LoadingProgressDialog dialog;
	private double latitude;
	private double longitude;
	private String city;
	private String cityCode;
	private int courierId;
	private String name;
	private String phone_number;
	private String receiver_name;
	private String receiver_phone;
	private String receiver_city;
	private String receiver_citycode;
	private String receiver_area;
	private String receiver_address;
	
	@Bind(id.et_courier)
	EditText et_courier;
	@Bind(id.edt_sendpersonaddress)
	EditText edt_sendpersonaddress;
	@Bind(id.ll_hide)
	LinearLayout ll_hide;
	@Bind(id.rl_show)
	RelativeLayout rl_show;
	@Bind(id.iv_add)
	ImageView iv_add;
	@Bind(id.tv_receiver)
	TextView tv_receiver;
	@Bind(id.tv_phone)
	TextView tv_phone;
	@Bind(id.tv_address)
	TextView tv_address;
	@Bind(id.text_title)
	TextView text_title;
	@Bind(id.view_show)
	View view_show;
	@Bind(id.iv_receiver_address)
	ImageView iv_receiver_address;
	@Bind(id.btn_preferential)
	Button btn_preferential;
	@Bind(id.btn_oksendphone)
	Button btn_oksendphone;
	@Bind(id.btn_oksend)
	Button btn_oksend;
	
	
	@Bind(id.btn_explaination)
	TextView btn_explaination;
	private double mlongitude;
	private double mlatitude;
	boolean flag = false;
	private int  type;
	PublicCourierBean courbean;
	private String	townCode="";
	private String townaddressd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_onkeysendexp);
		iWantApplication.getInstance().addActivity(this);
		ButterKnife.bind(this);
		initView();// 获取控件
		initData();
		getData();// 获取传递或存储的数据
		// initData();// 初始化数据
		setOnClick();// 设置监听
	}

	@Override
	public void onWeightClick(View v) {
		final Intent intent = new Intent();
		switch (v.getId()) {
		case id.txt_sendpersonaddress:// 选择地址
//			intent.setClass(OneKeySendExpActivity.this, AddressActivity.class);
			intent.setClass(OneKeySendExpActivity.this, AddressReceiveActivity.class);
			Bundle bundle=new Bundle();
			bundle.putString("address", address);
			intent.putExtras(bundle);
			startActivityForResult(intent, 3);
			break;
		/*
		 * case R.id.img_sendpersonname:// 获取寄件人信息 Intent intent1 = new
		 * Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
		 * startActivityForResult(intent1, 0); break; case R.id.btn_oksend: //
		 * 确认发布 intent.setClass(OneKeySendExpActivity.this, MainActivity.class);
		 * // intent.putExtra("isLogin", true); // goToRepublic();
		 * startActivity(intent); finish(); break;
		 */
		}
	}

	private void goToRepublic() {
		JSONObject obj = new JSONObject();
		// StringUtil.isMobileNO(edt_sendpersonmobile.getText().toString()||edt_sendpersonmobile.getText().toString().length()!=11)
		if (!StringUtil.isMobileNO(edt_sendpersonmobile.getText().toString())
				|| (edt_sendpersonmobile.getText().toString().length() != 11)) {
			ToastUtil.shortToast(getApplicationContext(), "请输入正确的手机号码");
			return;
		} else {
			if (receiver_name == null || receiver_name.equals("")) {
				ll_hide.setVisibility(View.VISIBLE);
				rl_show.setVisibility(View.GONE);
				view_show.setVisibility(View.GONE);
			} else {
				ll_hide.setVisibility(View.VISIBLE);
				rl_show.setVisibility(View.VISIBLE);
				view_show.setVisibility(View.VISIBLE);
			}
			if (PreferencesUtils.getInt(getApplicationContext(),
					PreferenceConstants.UID) == -1
					|| edt_sendpersonname.getText().toString().equals("")
					|| edt_sendpersonaddress.getText().toString().equals("")
					|| city.equals("")) {
				ToastUtil.shortToast(getApplicationContext(), "请完善所有信息");
				return;//	|| et_address.getText().toString().equals("")
			}
			try {
				// obj.put("userId",
				// PreferencesUtils.getInt(getApplicationContext(),
				// PreferenceConstants.UID));
				obj.put("userId", PreferencesUtils.getInt(
						getApplicationContext(), PreferenceConstants.UID));
				obj.put("personName", edt_sendpersonname.getText().toString());
				obj.put("mobile", edt_sendpersonmobile.getText().toString());
				obj.put("areaName", edt_sendpersonaddress.getText().toString());
				obj.put("address", et_address.getText().toString());
				obj.put("fromTown", townCode);
				obj.put("latitude", latitude);
				obj.put("longitude", longitude);
				obj.put("fromCity", cityCode);
				obj.put("fromCityName", city);
				obj.put("courierId", courierId);
				obj.put("assigned", "U");
				obj.put("personNameTo", receiver_name);
				obj.put("mobileTo", receiver_phone);
				obj.put("areaNameTo", receiver_area);
				obj.put("addressTo", receiver_address);
				obj.put("toCity", receiver_citycode);
				obj.put("toCityName", receiver_city);
				obj.put("status", type);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.e("fabuddldl", edt_sendpersonaddress.getText().toString());
			dialog = new LoadingProgressDialog(this);
			dialog.show();
			AsyncHttpUtils.doPostJson(getApplicationContext(), MCUrl.EXPRESS,
					obj.toString(), new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							Log.e("fabuddldl", new String(arg2));
							dialog.dismiss();
							if (arg2 == null || arg2.length == 0) {
								ToastUtil.shortToast(getApplicationContext(),
										"发布失败");
								return;
							}
							
							courbean = new Gson().fromJson(
									new String(arg2), PublicCourierBean.class);
							if (courbean.isSuccess() == true) {
								ToastUtil.shortToast(getApplicationContext(),
										courbean.getMessage());
								sendEmptyUiMessage(MsgConstants.MSG_02);
								startActivity(new Intent(
										OneKeySendExpActivity.this,
										MyCourierActivity.class));
								finish();
							} else {
								ToastUtil.shortToast(getApplicationContext(),
										courbean.getMessage());
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							dialog.dismiss();
							ToastUtil.shortToast(getApplicationContext(),
									"发布失败");
							// TODO Auto-generated method stub
							Log.e("ooo1", arg0 + "");
						}
					});
		}

	}

	/**
	 * 点击事件
	 * @param 按钮
	 */
	@OnClick({ id.iv_add, id.img_sendpersonname, id.img_addcouriers,
			id.btn_oksend, id.iv_receiver_address, id.iv_add_receiver , id.btnLeft, id.btnRight, id.btn_preferential, id.btn_explaination})
	public void onMyClick(View view) {
		switch (view.getId()) {
		case id.btnLeft://返回
			finish();
			break;
		case id.btnRight:
			startActivity(new Intent(OneKeySendExpActivity.this,
					UserInstruxtions.class));
			break;
		case id.iv_add://地址
			startActivityForResult(new Intent(OneKeySendExpActivity.this,
					SendPersonActivity.class), 4);
			break;
		case id.img_sendpersonname:
			Intent intent = new Intent(Intent.ACTION_PICK,
					ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent, 0);
			break;
		case id.img_addcouriers:
			Intent my_intent = new Intent(OneKeySendExpActivity.this,
					AddCouriersActivity.class);
			if (flag) {
				my_intent.putExtra("latitude", mlatitude);
				my_intent.putExtra("longitude", mlongitude);
				my_intent.putExtra("cityCode", my_cityCode);
				my_intent.putExtra("latitude1", my_latitude);
				my_intent.putExtra("longitude1", my_longitude);
			} else {
				my_intent.putExtra("latitude", my_latitude);
				my_intent.putExtra("longitude", my_longitude);
				my_intent.putExtra("cityCode", my_cityCode);
			}

			Log.e("llll", my_latitude + "****888" + my_longitude + "******"
					+ my_cityCode);
			startActivityForResult(my_intent, 5);
			break;
		case id.btn_oksend://确认发布
//			sendEmptyBackgroundMessage(MsgConstants.MSG_01);
			break;
		case id.iv_receiver_address://收件人信息
			startActivityForResult(new Intent(OneKeySendExpActivity.this,
					AddReceiverActivity.class), 6);
			break;
		case id.iv_add_receiver:
			startActivityForResult(new Intent(OneKeySendExpActivity.this,
					AddReceiverActivity.class), 6);
			break;
		case id.btn_preferential://现金信息；
			startActivity(new Intent(OneKeySendExpActivity.this,
					PreferrntialTwoActivity.class));
			break;
		case id.btn_explaination://使用说明
			startActivity(new Intent(OneKeySendExpActivity.this,
					InstructionActivity.class));
			break;
		default:
			break;
		}
	}

	private String my_cityCode;
	private double my_latitude;
	private double my_longitude;

	@Override
	public void initView() {
		// 标题
		text_title.setText(R.string.onkeysend);
		// 寄件人信息
		edt_sendpersonname = (EditText) findViewById(id.edt_sendpersonname);
		edt_sendpersonmobile = (EditText) findViewById(id.edt_sendpersonmobile);
		edt_sendpersonaddress = (EditText) findViewById(id.edt_sendpersonaddress);
		// 快递员信息
		// 后面的图片
		txt_sendpersonaddress = (TextView) findViewById(id.txt_sendpersonaddress);
		img_sendpersonname = (ImageView) findViewById(id.img_sendpersonname);
		img_addcouriers = (ImageView) findViewById(id.img_addcouriers);
		// 确认发布按钮
//		btn_oksend = (Button) findViewById(R.id.btn_oksend);
		et_address = (EditText) findViewById(id.et_address);
		
		my_latitude = getIntent().getDoubleExtra("latitude", 0);
		my_longitude = getIntent().getDoubleExtra("longitude", 0);
		my_cityCode = getIntent().getStringExtra("cityCode");
		// img_addcouriers=findViewById(R.id.img_a)
	}

	private LocationClient client;
	private BDLocation location;

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

	private String address;
	private String defa_address;

	@Override
	public void initData() {
		dialog = new LoadingProgressDialog(this);
		dialog.show();
		et_courier.setEnabled(false);
		client = new LocationClient(getApplicationContext());
		setLocationParams();
		client.registerLocationListener(new BDLocationListener() {
			@Override
			public void onReceiveLocation(final BDLocation arg0) {
				// TODO Auto-generated method stub
				if (arg0 == null) {
					ToastUtil.shortToast(getApplicationContext(), "定位失败，请检查设置");
					return;
				} else {
					if (dialog != null)
						dialog.dismiss();
					city = arg0.getCity();
					latitude = arg0.getLatitude();
					longitude = arg0.getLongitude();
					address = arg0.getAddrStr();
					townaddressd=arg0.getDistrict();
					if (defa_address == null && address != null) {
						if (flag) {
							edt_sendpersonaddress.setText(return_address);
						} else {
							edt_sendpersonaddress.setText(address);
						}
					}
				}
			}
		});
		client.start();
		Log.e("///////////", UrlMap.getfour(
				MCUrl.EXPRESS_DEFAULT,
				"userId",
				PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID) + "", "latitude", my_latitude
						+ "", "longitude", my_longitude + "", "cityCode",
				my_cityCode));
		AsyncHttpUtils.doSimGet(UrlMap.getfour(
				MCUrl.EXPRESS_DEFAULT,
				"userId",
				PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID) + "", "latitude", my_latitude
						+ "", "longitude", my_longitude + "", "cityCode",
				my_cityCode), new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if (dialog != null)
					dialog.dismiss();
				if (arg2 == null || arg2.length == 0)
					return;
				Log.e("nn", new String(arg2));
				DefaultExpressBean bean = new Gson().fromJson(new String(arg2),
						DefaultExpressBean.class);
				Log.e("mess", bean.getMessage());
				if (bean.data.get(0).address.equals("")) {
					edt_sendpersonaddress.setText(address);
					edt_sendpersonname.setText(PreferencesUtils.getString(
							getApplicationContext(),
							PreferenceConstants.USERNAME));
					courierId = bean.data.get(0).courierId;
				} else {
					edt_sendpersonname.setText(bean.data.get(0).personName);
					et_address.setText(bean.data.get(0).address);
					edt_sendpersonaddress.setText(bean.data.get(0).areaName);
					latitude = bean.data.get(0).latitude;
					longitude = bean.data.get(0).longitude;
					city = bean.data.get(0).fromCityName;
					courierId = bean.data.get(0).courierId;
					cityCode = bean.data.get(0).fromCity;
					defa_address = bean.data.get(0).areaName;
				}
				if (bean.data.get(0).expName.equals("")
						&& bean.data.get(0).courierName.equals("")) {
					ToastUtil.shortToast(OneKeySendExpActivity.this,
							"您所处位置附近没有快递员，请手动添加您常用的快递员");
				} else {
					et_courier.setText(bean.data.get(0).expName + "--"
							+ bean.data.get(0).courierName);
				}
				if (bean.data.get(0).mobile.equals("")) {

					edt_sendpersonmobile.setText(PreferencesUtils
							.getString(getApplicationContext(),
									PreferenceConstants.MOBILE));
				} else {

					edt_sendpersonmobile.setText(bean.data.get(0).mobile);
				}
				
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// edt_sendpersonaddress.setText(address);
				edt_sendpersonname.setText(PreferencesUtils.getString(
						getApplicationContext(), PreferenceConstants.USERNAME));
				edt_sendpersonmobile.setText(PreferencesUtils.getString(
						getApplicationContext(), PreferenceConstants.MOBILE));
			}
		});
		/*
		 * if(address!=null){ edt_sendpersonaddress.setText(address); }
		 */
		edt_sendpersonname.setText(PreferencesUtils.getString(
				getApplicationContext(), PreferenceConstants.USERNAME));
		edt_sendpersonmobile.setText(PreferencesUtils.getString(
				getApplicationContext(), PreferenceConstants.MOBILE));
		getCityCodes();
	}

	public void getlistdata(final Double mlatitude, final Double mlongitude) {
		AsyncHttpUtils.doSimGet(UrlMap.getfour(
				MCUrl.CORUIERS_LIST,
				"userId",
				PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID) + "", "latitude", mlatitude
						+ "", "longitude", mlongitude + "", "cityCode",
				my_cityCode), new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.e("+++++++++++++++++", new String(arg2));
				;
				// TODO Auto-generated method stub
				addcoucorBean bean1 = new Gson().fromJson(new String(arg2),
						addcoucorBean.class);
				Log.e("mess", bean1.getMessage());
				if (bean1.data == null || bean1.data.size() < 1) {
					et_courier.setText("");
					Builder ad = new Builder(
							OneKeySendExpActivity.this);
					ad.setMessage("您所处位置附近没有快递员，请手动添加您常用的快递员");
					ad.setPositiveButton("确认",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									Intent mIntent = new Intent(
											OneKeySendExpActivity.this,
											AddCouriersActivity.class);
									if (flag) {
										mIntent.putExtra("latitude", mlatitude);
										mIntent.putExtra("longitude",
												mlongitude);
										mIntent.putExtra("cityCode",
												my_cityCode);

										mIntent.putExtra("latitude1",
												my_latitude);
										mIntent.putExtra("longitude1",
												my_longitude);
									} else {
										mIntent.putExtra("latitude",
												my_latitude);
										mIntent.putExtra("longitude",
												my_longitude);
										mIntent.putExtra("cityCode",
												my_cityCode);

									}
									startActivity(mIntent);

									dialog.dismiss();
								}
							});
					ad.create().show();
				} else {
					et_courier.setText(bean1.data.get(0).expName + "--"
							+ bean1.data.get(0).courierName);
				}

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// edt_sendpersonaddress.setText(address);

			}
		});
	}

	private String return_address;

	@Override
	public void setOnClick() {
		txt_sendpersonaddress.setOnClickListener(this);
		btn_oksendphone.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
					type=2;
					sendEmptyBackgroundMessage(MsgConstants.MSG_01);
				
			}
		});
		btn_oksend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				type=1;
				sendEmptyBackgroundMessage(MsgConstants.MSG_01);
			}
		});
		
	}

	/**
	 * 处理返回过来的数据
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			break;

		case 2:
			if (resultCode == Activity.RESULT_OK) {
				Bundle bundle = data.getExtras();
				if (bundle != null) {

				}
			}
			break;
		case 3:// 强制定位的回调
			if (resultCode == Activity.RESULT_OK) {
				return_address = data.getStringExtra("address");
				edt_sendpersonaddress.setText(return_address);
				latitude= data.getDoubleExtra("latitude", 0);
				mlatitude = data.getDoubleExtra("latitude", 0);
				longitude = data.getDoubleExtra("longitude", 0);
				mlongitude = data.getDoubleExtra("longitude", 0);
				cityCode =data.getStringExtra("city");
				city = data.getStringExtra("city");
				townaddressd=data.getStringExtra("townaddressd");
				getlistdata(mlatitude, mlongitude);
//				ToastUtil.shortToast(OneKeySendExpActivity.this, "cityCode"+cit);
				flag = true;
				// sendEmptyBackgroundMessage(MsgConstants.MSG_01);
				getCityCodes();
				return;
			}
			break;
		case 4:// 寄件人信息
			if (resultCode == Activity.RESULT_OK) {
				edt_sendpersonname.setText(data.getStringExtra("name"));
				edt_sendpersonmobile.setText(data.getStringExtra("phone"));
				edt_sendpersonaddress.setText(data.getStringExtra("location"));
				// city=data.getStringExtra("city");
				et_address.setText(data.getStringExtra("address"));
				latitude = data.getDoubleExtra("latitude", 0);
				longitude = data.getDoubleExtra("longitude", 0);
				city = data.getStringExtra("city");
			}
			break;
		case 0:// 增加手机联系人的回调
			if (data == null) {
				return;
			}
			if (resultCode == Activity.RESULT_OK) {
				ContentResolver reContentResolverol = getContentResolver();
				Uri contactData = data.getData();
				// 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
				Cursor cursor = managedQuery(contactData, null, null, null,
						null);
				cursor.moveToFirst();
				// 获得DATA表中的名字
				name = cursor
						.getString(cursor
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				// 条件为联系人ID
				edt_sendpersonname.setText(name);
				String contactId = cursor.getString(cursor
						.getColumnIndex(ContactsContract.Contacts._ID));
				// 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
				Cursor phone = reContentResolverol.query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + contactId, null, null);
				while (phone.moveToNext()) {
					phone_number = phone
							.getString(phone
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					if (!phone_number.equals(""))
						edt_sendpersonmobile.setText(phone_number);
				}

			}

			break;
		case 5:
			// 添加快递员的回调
			if (data == null || resultCode != RESULT_OK)
				return;
			courierMobile = data.getStringExtra("phone");
			courierName = data.getStringExtra("name");
			courierId = data.getIntExtra("courierId", 0);
			et_courier.setText(data.getStringExtra("expName") + "--"
					+ data.getStringExtra("name"));
			break;
		case 6:
			// 返回收件人信息
			if (data == null || resultCode != RESULT_OK)
				return;
			Log.e("bbb", data.toString());
			ll_hide.setVisibility(View.VISIBLE);
			rl_show.setVisibility(View.VISIBLE);
			view_show.setVisibility(View.VISIBLE);
			receiver_name = data.getStringExtra("name");
			receiver_phone = data.getStringExtra("phone");
			receiver_city = data.getStringExtra("city");
			receiver_area = data.getStringExtra("location");
			receiver_address = data.getStringExtra("address");
			receiver_citycode = data.getStringExtra("citycode");
			tv_receiver.setText(receiver_name);
			tv_phone.setText(receiver_phone);
			tv_address.setText(receiver_area + receiver_address);
			break;
		case 7:
			if (data == null) {
				return;
			}
			if (resultCode == Activity.RESULT_OK) {
				ContentResolver reContentResolverol = getContentResolver();
				Uri contactData = data.getData();
				// 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
				Cursor cursor = managedQuery(contactData, null, null, null,
						null);
				cursor.moveToFirst();
				// 获得DATA表中的名字
				name = cursor
						.getString(cursor
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				// 条件为联系人ID
				tv_receiver.setText(name);
				String contactId = cursor.getString(cursor
						.getColumnIndex(ContactsContract.Contacts._ID));
				// 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
				Cursor phone = reContentResolverol.query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + contactId, null, null);
				while (phone.moveToNext()) {
					phone_number = phone
							.getString(phone
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					if (!phone_number.equals(""))
						tv_phone.setText(phone_number);
				}
				rl_show.setVisibility(View.VISIBLE);
			}
			break;
		}
	}

	// 获取联系人电话
	private String getContactPhone(Cursor cursor) {

		int phoneColumn = cursor
				.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
		int phoneNum = cursor.getInt(phoneColumn);
		cursor.getString(phoneNum);
		String phoneResult = "";
		// System.out.print(phoneNum);
		if (phoneNum > 0) {
			// 获得联系人的ID号
			int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
			int nameIndex = cursor
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
			String contactId = cursor.getString(idColumn);
			String name = cursor.getString(nameIndex);
			// 获得联系人的电话号码的cursor;
			Cursor phones = this.getContentResolver().query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
							+ contactId, null, null);
			if (phones.moveToFirst()) {
				// 遍历所有的电话号码
				for (; !phones.isAfterLast(); phones.moveToNext()) {
					int index = phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
					int typeindex = phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
					int phone_type = phones.getInt(typeindex);
					String phoneNumber = phones.getString(index);

					switch (phone_type) {
					case 2:
						phoneResult = phoneNumber;
						break;
					}
					// allPhoneNum.add(phoneNumber);
				}
				if (!phones.isClosed()) {
					phones.close();
				}
			}
			phoneResult = name + "&" + phoneResult;
		}
		return phoneResult;
	}

	@Override
	public void getData() {
		courierName = PreferencesUtils.getString(getApplicationContext(),
				"courierName");
		courierMobile = PreferencesUtils.getString(getApplicationContext(),
				"courierMobile");
		/*
		 * expName = PreferencesUtils .getString(getApplicationContext(),
		 * "expName");
		 */
		edt_sendpersonaddress.setFocusable(false);
	}

	private void getCityCode() {
		boolean isCopySuccess = CheckDbUtils.checkDb();
		// 成功的将数据库copy到data 中
		if (isCopySuccess) {
			iWantApplication.getInstance().mDbManager = new DbManager(
					iWantApplication.getInstance());
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
				.selectDataFromDb("select * from city where city_name='" + city
						+ "'");
		cityCode = selectDataFromDb.get(0).city_code;
		Log.e("citycode", cityCode);
		sendEmptyUiMessage(MsgConstants.MSG_01);
	}
	private void getCityCodes() {
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
	}

	@Override
	public void handleBackgroundMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleBackgroundMessage(msg);
		switch (msg.what) {
		case MsgConstants.MSG_01:
			getCityCode();
			break;

		default:
			break;
		}
	}

	@Override
	public void handleUiMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleUiMessage(msg);
		switch (msg.what) {
		case MsgConstants.MSG_01:
			goToRepublic();
			break;
		case MsgConstants.MSG_02:
			tomobile();
			break;
		default:
			break;
		}
	}
	public void tomobile(){
		if (!courbean.data.get(0).courierMobile.equals("") && courbean.data.get(0).courierMobile!=null) {
			if (type==2) {
			AppUtils.intentDial(OneKeySendExpActivity.this, courbean.data.get(0).courierMobile);
			}
		}
	}
	
}
