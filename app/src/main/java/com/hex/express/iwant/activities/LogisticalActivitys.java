package com.hex.express.iwant.activities;

import java.nio.Buffer;
import java.util.Calendar;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.bean.DownSpecialBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * 
 * @author huyichuan
 * 发物流界面
 *
 */

public class LogisticalActivitys extends BaseActivity{
	@Bind(R.id.lo_show)
	TitleBarView lo_show;
	@Bind(R.id.lo_name_huo)
	EditText lo_name_huo;//货物名称
//	@Bind(R.id.switch1)
//	Switch switch1;   //发货地址选择器
//	@Bind(R.id.lo_chufa)//发货地址
//	EditText lo_chufa;
//	@Bind(R.id.switch2)//到达地址选择器
//	Switch switch2;
	@Bind(R.id.lo_daoda)//到货地址
	EditText lo_daoda;
	@Bind(R.id.lo_weight)//货物重量
	TextView lo_weight;
	@Bind(R.id.lo_volume)//货物体积
	TextView lo_volume;
	@Bind(R.id.lo_sendtime)//发货时间
	TextView lo_sendtime;
	@Bind(R.id.lo_endtime)//送达时间
	TextView lo_endtime;
	@Bind(R.id.lo_name)//收货人姓名
	EditText lo_name;
	@Bind(R.id.lo_phone)//收货人手机号
	EditText lo_phone;
	@Bind(R.id.lo_remarks)//备注
	EditText lo_remarks;
	@Bind(R.id.lo_submit)//提交按钮
	Button lo_submit;
	@Bind(R.id.spinner1)//重量选择
	Spinner spinner1;
	@Bind(R.id.spinner3)//体积选择
	Spinner spinner3;
	// 当前位置经纬度
		private double latitude;
		private double longitude;
		private String city;
		// 寄件人经纬度
		private double mylatitude;// 经度
		private double mylongitude;// 纬度
		private LocationClient client;
		private String cityCode;
		// 收件人经纬度
		private double receiver_longitude;
		private double receiver_latitude;
		private String receiver_citycode;
		private boolean receive;
		private String icon;
		private Calendar c = null;//时间选择
	    private final static int DATE_DIALOG = 0;
	    private final static int DATE = 1; 
	    private ArrayAdapter<String> adapter;  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_down_logistical);
		ButterKnife.bind(LogisticalActivitys.this);
		initData();
	}
	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub
		
	}
//	@OnClick({R.id.switch1,R.id.lo_chufa,R.id.switch2,R.id.lo_daoda,R.id.spinner1,R.id.spinner3,
//		R.id.lo_sendtime,R.id.lo_endtime,R.id.lo_phone,R.id.lo_submit,R.id.lo_name})
//	public void MyOnClick(View view) {
//		switch (view.getId()) {
//		case R.id.switch1://起始发货地址选择
//			startActivity(new Intent(getApplicationContext(), H5ModelActivity.class));
//			break;
//		case R.id.lo_chufa://起始发货地址
//			startActivity(new Intent(getApplicationContext(), H5ModelActivity.class));
//			break;
//		case R.id.switch2://达到地址选择
//			startActivity(new Intent(getApplicationContext(), H5ModelActivity.class));
//			break;
//		case R.id.lo_daoda://达到地址
//			startActivity(new Intent(getApplicationContext(), H5ModelActivity.class));
//			break;
//		case R.id.spinner1://重量
//                 sper();
//			break;
//		case R.id.spinner3://货物体积
//			startActivity(new Intent(getApplicationContext(), H5ModelActivity.class));
//			break;
//		case R.id.lo_sendtime://发货时间
//			  showDialog(DATE_DIALOG);
//			break;
//		case R.id.lo_endtime://达到时间
//			  showDialog(DATE);
//			break;
//		case R.id.lo_phone://手机号
//			Intent intent1 = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//			startActivityForResult(intent1, 9);
//			break;
//		case R.id.lo_name://收货人姓名
//			Intent intent2 = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//			startActivityForResult(intent2,1);
//			break;
//			
//		case R.id.lo_submit://提交
//			addPostResult();
//				break;
//		}
//		}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		
		
	}
	public void sper(){
		final String[] myItems = getResources().getStringArray(
				R.array.languages);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, myItems);
		adapter.setDropDownViewResource(R.layout.drop_down_item);  
	        //第四步：将适配器添加到下拉列表上    
	        spinner1.setAdapter(adapter);  
	        spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
	        	@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
	        		
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {

				}
});
	        
	        
		}
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		lo_show.setTitleText("发布货源");
		
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
	/**
     * 创建日期及时间选择对话框
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch (id) {
            case DATE_DIALOG:
                c = Calendar.getInstance();
                dialog = new DatePickerDialog(
                        this,
                        new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker arg0, int year, int month, int dayOfMonth) {
								// TODO Auto-generated method stub
								lo_sendtime.setText( year + "-" + (month+1) + "-" + dayOfMonth );
							}
                        },
                        c.get(Calendar.YEAR), // 传入年份
                        c.get(Calendar.MONTH), // 传入月份
                        c.get(Calendar.DAY_OF_MONTH) // 传入天数
                );
                break;
            case DATE:
                c = Calendar.getInstance();
                dialog = new DatePickerDialog(
                        this,
                        new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker arg0, int year, int month, int dayOfMonth) {
								// TODO Auto-generated method stub
								lo_sendtime.setText( year + "-" + (month+1) + "-" + dayOfMonth );
							}
                        },
                        c.get(Calendar.YEAR), // 传入年份
                        c.get(Calendar.MONTH), // 传入月份
                        c.get(Calendar.DAY_OF_MONTH) // 传入天数
                );
                break;

        }

        return dialog;
    }
	
	private void addPostResult() {
		JSONObject obj = new JSONObject();
		
		try {
			obj.put("userId",
					String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		dialog.show();
		Log.e("查看数据", obj.toString());
		AsyncHttpUtils.doPostJson(LogisticalActivitys.this, MCUrl.DOWNWINDTASKPUBLISH, obj.toString(),
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("oppo", new String(arg2));
						dialog.dismiss();
						DownSpecialBean bean = new Gson().fromJson(new String(arg2), DownSpecialBean.class);
						Log.e("oppop", bean.data.toString());
						

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						dialog.dismiss();
					}
				});

	}

	
	@Override
	public void onActivityReenter(int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityReenter(resultCode, data);
		switch (resultCode) {
		case 0:// 增加手机收件人联系人的回调
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
							String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
							// 条件为联系人ID
//							et_add_name.setText(name);
							String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
							// 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
							Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
									ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
							while (phone.moveToNext()) {
								String phone_number = phone
										.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
								if (!phone_number.equals(""))
									lo_phone.setText(phone_number);
							}

						}

			break;
			case 1:
				// 增加手机收件人联系人的回调
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
					String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					// 条件为联系人ID
					lo_name.setText(name);
					String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
					// 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
					Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
					while (phone.moveToNext()) {
						String phone_number = phone
								.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//						if (!phone_number.equals(""))
//							lo_phone.setText(phone_number);
					}

				}

				
				break;
		}
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
}
