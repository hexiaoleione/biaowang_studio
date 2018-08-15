package com.hex.express.iwant.homfragment;


import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.framework.base.BaseFragment;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.activities.AddCouriersActivity;
import com.hex.express.iwant.activities.AddReceiverActivity;
import com.hex.express.iwant.activities.AddressReceiveActivity;
import com.hex.express.iwant.activities.InstructionActivity;
import com.hex.express.iwant.activities.MyCourierActivity;
import com.hex.express.iwant.activities.NewCenterActivity;
import com.hex.express.iwant.activities.OneKeySendExpActivity;
import com.hex.express.iwant.activities.PreferrntialTwoActivity;
import com.hex.express.iwant.activities.RoleAuthenticationActivity;
import com.hex.express.iwant.activities.SendPersonActivity;
import com.hex.express.iwant.activities.UserInstruxtions;
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
import com.hex.express.iwant.newbaidu.LocationDemo;
import com.hex.express.iwant.utils.AppUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.StringUtil;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomSubFragment3 extends BaseFragment  {
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
//	private LoadingProgressDialog dialog;
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
	
	@Bind(R.id.et_courier)
	EditText et_courier;
	@Bind(R.id.edt_sendpersonaddress)
	EditText edt_sendpersonaddress;
	@Bind(R.id.ll_hide)
	LinearLayout ll_hide;
	@Bind(R.id.rl_show)
	RelativeLayout rl_show;
	@Bind(R.id.iv_add)
	ImageView iv_add;
	@Bind(R.id.tv_receiver)
	TextView tv_receiver;
	@Bind(R.id.tv_phone)
	TextView tv_phone;
	@Bind(R.id.tv_address)
	TextView tv_address;
	@Bind(R.id.view_show)
	View view_show;
	@Bind(R.id.iv_receiver_address)
	ImageView iv_receiver_address;
	@Bind(R.id.btn_preferential)
	Button btn_preferential;
	@Bind(R.id.btn_oksendphone)
	Button btn_oksendphone;
	@Bind(R.id.btn_oksend)
	Button btn_oksend;
	
	@Bind(R.id.rl_bannerTip)
	RelativeLayout rl_bannerTip;
	@Bind(R.id.btn_explaination)
	TextView btn_explaination;
	private double mlongitude;
	private double mlatitude;
	boolean flag = false;
	private int  type;
	PublicCourierBean courbean;
	private String	townCode="";
	private String townaddressd;
	View view;
	
//	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view==null) {
			view=inflater.inflate(R.layout.fragment_hom3, container, false);
		}
		 ViewGroup p = (ViewGroup) view.getParent(); 
         if (p != null) { 
             p.removeAllViewsInLayout(); 
         } 
         ButterKnife.bind(this,view);
         initView();// 获取控件
 		initData();
 		getData();// 获取传递或存储的数据
 		// initData();// 初始化数据
 		setOnClick();// 设置监听
		return view;
	}
	private void initView(){
		edt_sendpersonname = (EditText)view. findViewById(R.id.edt_sendpersonname);
		edt_sendpersonmobile = (EditText) view.findViewById(R.id.edt_sendpersonmobile);
		edt_sendpersonaddress = (EditText)view. findViewById(R.id.edt_sendpersonaddress);
		// 快递员信息
		// 后面的图片
		txt_sendpersonaddress = (TextView)view. findViewById(R.id.txt_sendpersonaddress);
		img_sendpersonname = (ImageView)view. findViewById(R.id.img_sendpersonname);
		img_addcouriers = (ImageView)view. findViewById(R.id.img_addcouriers);
		// 确认发布按钮
//		btn_oksend = (Button) findViewById(R.id.btn_oksend);
		et_address = (EditText)view. findViewById(R.id.et_address);
		if (isLogin()) {
			rl_bannerTip.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intents=new Intent();
					intents.putExtra("tiaoguo", "2");
					intents.setClass(getActivity(), RoleAuthenticationActivity.class);
					startActivity(intents);
				}
			});
			
			
		}
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

	private void initData(){
		et_courier.setEnabled(false);
		client = new LocationClient(getActivity());
		setLocationParams();
		client.registerLocationListener(new BDLocationListener() {
			@Override
			public void onReceiveLocation(final BDLocation arg0) {
				// TODO Auto-generated method stub
				if (arg0 == null) {
					ToastUtil.shortToast(getActivity(), "定位失败，请检查设置");
					return;
				} else {
//					if (dialog != null)
//						dialog.dismiss();
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
		getCityCodes();
		AsyncHttpUtils.doSimGet(UrlMap.getfour(
				MCUrl.EXPRESS_DEFAULT,
				"userId",
				PreferencesUtils.getInt(getActivity(),
						PreferenceConstants.UID) + "", "latitude", latitude
						+ "", "longitude", longitude + "", "cityCode",
				cityCode), new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//				if (dialog != null)
//					dialog.dismiss();
				if (arg2 == null || arg2.length == 0)
					return;
				Log.e("nn", new String(arg2));
				DefaultExpressBean bean = new Gson().fromJson(new String(arg2),
						DefaultExpressBean.class);
				Log.e("mess", bean.getMessage());
				if (bean.data.get(0).address.equals("")) {
					edt_sendpersonaddress.setText(address);
					edt_sendpersonname.setText(PreferencesUtils.getString(
							getActivity(),
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
//					ToastUtil.shortToast(getActivity(),"您所处位置附近没有快递员，请手动添加您常用的快递员");
				} else {
					et_courier.setText(bean.data.get(0).expName + "--"
							+ bean.data.get(0).courierName);
				}
				if (bean.data.get(0).mobile.equals("")) {

					edt_sendpersonmobile.setText(PreferencesUtils
							.getString(getActivity(),
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
						getActivity(), PreferenceConstants.USERNAME));
				edt_sendpersonmobile.setText(PreferencesUtils.getString(
						getActivity(), PreferenceConstants.MOBILE));
			}
		});
		/*
		 * if(address!=null){ edt_sendpersonaddress.setText(address); }
		 */
		edt_sendpersonname.setText(PreferencesUtils.getString(
				getActivity(), PreferenceConstants.USERNAME));
		edt_sendpersonmobile.setText(PreferencesUtils.getString(
				getActivity(), PreferenceConstants.MOBILE));
		 if (isLogin()) {
		
		my_latitude =Double.valueOf( PreferencesUtils.getString(getActivity(), PreferenceConstants.LATITUDE)).doubleValue();
		my_longitude = Double.valueOf( PreferencesUtils.getString(getActivity(), PreferenceConstants.LONGITUDE)).doubleValue();
		my_cityCode =PreferencesUtils.getString(getActivity(), PreferenceConstants.CITYCODE);
			}
	}
	private String my_cityCode;
	private double my_latitude;
	private double my_longitude;
	/**
	 * 点击事件
	 * @param 按钮
	 */
	@OnClick({ R.id.iv_add, R.id.img_sendpersonname, R.id.img_addcouriers,
			R.id.btn_oksend, R.id.iv_receiver_address, R.id.iv_add_receiver ,R.id.btn_preferential,R.id.btn_explaination})
	public void onMyClick(View view) {
		switch (view.getId()) {
//		case R.id.btnRight:
//			startActivity(new Intent(getActivity(),
//					UserInstruxtions.class));
//			break;
		case R.id.iv_add://地址
			startActivityForResult(new Intent(getActivity(),
					SendPersonActivity.class), 4);
			break;
		case R.id.img_sendpersonname:
			Intent intent = new Intent(Intent.ACTION_PICK,
					ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent, 0);
			break;
		case R.id.img_addcouriers:
			Intent my_intent = new Intent(getActivity(),
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
		case R.id.btn_oksend://确认发布
//			sendEmptyBackgroundMessage(MsgConstants.MSG_01);
			break;
		case R.id.iv_receiver_address://收件人信息
			startActivityForResult(new Intent(getActivity(),
					AddReceiverActivity.class), 6);
			break;
		case R.id.iv_add_receiver:
			startActivityForResult(new Intent(getActivity(),
					AddReceiverActivity.class), 6);
			break;
		case R.id.btn_preferential://现金信息；
			startActivity(new Intent(getActivity(),
					PreferrntialTwoActivity.class));
			break;
		case R.id.btn_explaination://使用说明
			startActivity(new Intent(getActivity(),
					InstructionActivity.class));
			break;
		default:
			break;
		}
	}
	private void getData(){
		courierName = PreferencesUtils.getString(getActivity(),
				"courierName");
		courierMobile = PreferencesUtils.getString(getActivity(),
				"courierMobile");
		/*
		 * expName = PreferencesUtils .getString(getApplicationContext(),
		 * "expName");
		 */
		edt_sendpersonaddress.setFocusable(false);
	}
	private String return_address;
	private void setOnClick(){
		txt_sendpersonaddress.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
//				intent.setClass(getActivity(), AddressReceiveActivity.class);
//				Bundle bundle=new Bundle();
//				bundle.putString("address", address);
//				intent.putExtras(bundle);
				intent.setClass(getActivity(), LocationDemo.class);
				startActivityForResult(intent, 3);
			}
		});
		btn_oksendphone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (isLogin()) {
					type=2;
//					sendEmptyBackgroundMessage(MsgConstants.MSG_01);
					goToRepublic();
				}else {
					ToastUtil.shortToast(getActivity(), "请登录后发布");
					}
				
			}
		});
		btn_oksend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				type=1;
//				sendEmptyBackgroundMessage(MsgConstants.MSG_01);
			}
		});
		
		}
	private void goToRepublic() {
		JSONObject obj = new JSONObject();
		String string =edt_sendpersonmobile.getText().toString();
		  String tmpstr=string.replace(" ","");
		// StringUtil.isMobileNO(edt_sendpersonmobile.getText().toString()||edt_sendpersonmobile.getText().toString().length()!=11)
		if (!StringUtil.isMobileNO(tmpstr)
				|| (tmpstr.length() != 11)) {
			ToastUtil.shortToast(getActivity(), "请输入正确的手机号码");
			return;
		} else {
			if (receiver_name == null || receiver_name.equals("")) {
				ll_hide.setVisibility(View.GONE);
				rl_show.setVisibility(View.GONE);
				view_show.setVisibility(View.GONE);
			} else {
				ll_hide.setVisibility(View.GONE);
				rl_show.setVisibility(View.VISIBLE);
				view_show.setVisibility(View.VISIBLE);
			}
			if (PreferencesUtils.getInt(getActivity(),
					PreferenceConstants.UID) == -1
					|| edt_sendpersonname.getText().toString().equals("")
					|| edt_sendpersonaddress.getText().toString().equals("")
					|| city.equals("")) {
				ToastUtil.shortToast(getActivity(), "请完善所有信息");
				return;//	|| et_address.getText().toString().equals("")
			}
			try {
				// obj.put("userId",
				// PreferencesUtils.getInt(getApplicationContext(),
				// PreferenceConstants.UID));
				obj.put("userId", PreferencesUtils.getInt(
						getActivity(), PreferenceConstants.UID));
				obj.put("personName", edt_sendpersonname.getText().toString());
				obj.put("mobile", tmpstr);
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
//			dialog = new LoadingProgressDialog(this);
//			dialog.show();
			AsyncHttpUtils.doPostJson(getActivity(), MCUrl.EXPRESS,
					obj.toString(), new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							Log.e("fabuddldl", new String(arg2));
//							dialog.dismiss();
							if (arg2 == null || arg2.length == 0) {
								ToastUtil.shortToast(getActivity(),
										"发布失败");
								return;
							}
							
							courbean = new Gson().fromJson(
									new String(arg2), PublicCourierBean.class);
							if (courbean.isSuccess() == true) {
								ToastUtil.shortToast(getActivity(),
										courbean.getMessage());
								sendEmptyUiMessage(MsgConstants.MSG_02);
							} else {
								ToastUtil.shortToast(getActivity(),
										courbean.getMessage());
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
//							dialog.dismiss();
							ToastUtil.shortToast(getActivity(),
									"发布失败");
							// TODO Auto-generated method stub
							Log.e("ooo1", arg0 + "");
						}
					});
		}

	}
	@Override
	public void handleUiMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleUiMessage(msg);
		switch (msg.what) {
		case MsgConstants.MSG_01:
//			goToRepublic();
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
			AppUtils.intentDial(getActivity(), courbean.data.get(0).courierMobile);
			}
		}
	}
	/**
	 * 处理返回过来的数据
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
				ContentResolver reContentResolverol = getActivity().getContentResolver();
				Uri contactData = data.getData();
				// 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
				Cursor cursor =getActivity(). managedQuery(contactData, null, null, null,
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
				if(Build.VERSION.SDK_INT < 14) {
				    cursor.close();
				   }
			}

			break;
		case 5:
			// 添加快递员的回调
			if (data == null || resultCode != Activity.RESULT_OK)
				return;
			courierMobile = data.getStringExtra("phone");
			courierName = data.getStringExtra("name");
			courierId = data.getIntExtra("courierId", 0);
			et_courier.setText(data.getStringExtra("expName") + "--"
					+ data.getStringExtra("name"));
			break;
		case 6:
			// 返回收件人信息
			if (data == null || resultCode != Activity.RESULT_OK)
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
				ContentResolver reContentResolverol =getActivity(). getContentResolver();
				Uri contactData = data.getData();
				// 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
				Cursor cursor = getActivity().managedQuery(contactData, null, null, null,
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
				if(Build.VERSION.SDK_INT < 14) {
				    cursor.close();
				   }
			}
			break;
		}
	}
	public void getlistdata(final Double mlatitude, final Double mlongitude) {
		AsyncHttpUtils.doSimGet(UrlMap.getfour(
				MCUrl.CORUIERS_LIST,
				"userId",
				PreferencesUtils.getInt(getActivity(),
						PreferenceConstants.UID) + "", "latitude", mlatitude
						+ "", "longitude", mlongitude + "", "cityCode",
				cityCode), new AsyncHttpResponseHandler() {

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
							getActivity());
					ad.setMessage("您所处位置附近没有快递员，请手动添加您常用的快递员");
					ad.setPositiveButton("确认",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									Intent mIntent = new Intent(
											getActivity(),
											AddCouriersActivity.class);
									if (flag) {
										mIntent.putExtra("latitude", mlatitude);
										mIntent.putExtra("longitude",
												mlongitude);
										mIntent.putExtra("cityCode",
												cityCode);

										mIntent.putExtra("latitude1",
												latitude);
										mIntent.putExtra("longitude1",
												longitude);
									} else {
										mIntent.putExtra("latitude",
												latitude);
										mIntent.putExtra("longitude",
												longitude);
										mIntent.putExtra("cityCode",
												cityCode);

									}
									startActivity(mIntent);

//									dialog.dismiss();
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

	private void getCityCodes() {
		boolean isCopySuccess = CheckDbUtils.checkDb();
		// 成功的将数据库copy到data 中
		if (isCopySuccess) {
			iWantApplication.getInstance().mDbManager = new DbManager(iWantApplication.getInstance());
		}
//		Log.e("11111kaudi", city);
		if (city == null || city.equals("")) {
//			ToastUtil.shortToast(getActivity(), "请输入完整信息");
			return;
		}
		Log.e("city", city);
		if (!city.contains("市")) {
			city = city + "市";
		}
		List<CityBean> selectDataFromDb = new CityDbOperation()
				.selectDataFromDb("select * from city where city_name='" + city + "'");
			cityCode = selectDataFromDb.get(0).city_code;
			Log.e("citycode33", cityCode);
			
			
//		if (townaddressd==null) {
//			townaddressd = PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.Codedess);
//		}
		List<AreaBean> selectDataFromDbs = new AreaDboperation()
				.selectDataFromDb("select * from area where area_name='" + townaddressd + "'");
		 
		if (selectDataFromDbs.size()>0 && !selectDataFromDbs.equals("")) {
			townCode=selectDataFromDbs.get(0).area_code;
			Log.e("1111133townCode", townCode);
		}
	}
		
	}
