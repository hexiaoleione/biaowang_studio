package com.hex.express.iwant.activities;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.StringUtil;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class ChangeReceiverActivity extends BaseActivity {
	@Bind(R.id.tbv_show) TitleBarView tbv_show;
	//@Bind(R.id.et_com)   EditText et_com;
	@Bind(R.id.et_name)  EditText et_name;
	@Bind(R.id.et_phone) EditText et_phone;
	@Bind(R.id.et_region) EditText et_region;
	@Bind(R.id.et_region_address)EditText et_region_address;
	private String area;
	private String code;
	private LoadingProgressDialog dialog;
	private GeoCoder search;
	private double latitude;
	private double longitude;
	private String[] str;
	private String com;
	private String name;
	private String phone;
	private String address;
	private String city;
	private String pro;
	private String distract;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_receiver);
		ButterKnife.bind(this);
		initData();
	}
	@Override
	public void onWeightClick(View v) {

	}

	@Override
	public void initView() {

	}
	private int addressId;
	@Override
	public void initData() {
		name = getIntent().getStringExtra("personName");
		area = getIntent().getStringExtra("areaName");
		address = getIntent().getStringExtra("address");
		phone = getIntent().getStringExtra("mobile");
		city = getIntent().getStringExtra("cityName");
		code = getIntent().getStringExtra("cityCode");
		et_name.setText(name);
		et_phone.setText(phone);
		et_region_address.setText(address);
		et_region.setText(area);
		addressId=getIntent().getIntExtra("addressId",0);
		tbv_show.setTitleText("常用地址编辑");
		et_region.setFocusable(false);
		dialog=new LoadingProgressDialog(this);
		search=GeoCoder.newInstance();
		search.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
			
			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
				// TODO Auto-generated method stub
				if(arg0==null||arg0.error!=SearchResult.ERRORNO.NO_ERROR||arg0.getLocation().latitude==0||arg0.getLocation().longitude==0)
				{
					dialog.dismiss();
					ToastUtil.shortToast(getApplicationContext(), "百度地图出现错误了，请重写输入地址");
				}
				latitude=arg0.getLocation().latitude;
				longitude=arg0.getLocation().longitude;
				Log.e("llll", latitude+"");
				Log.e("1111", longitude+"");
				sendEmptyUiMessage(MsgConstants.MSG_01);
				
			}
			
			@Override
			public void onGetGeoCodeResult(GeoCodeResult arg0) {
				// TODO Auto-generated method stub
				if(arg0==null||arg0.error!=SearchResult.ERRORNO.NO_ERROR||arg0.getLocation().latitude==0||arg0.getLocation().longitude==0)
				{
					dialog.dismiss();
					ToastUtil.shortToast(getApplicationContext(), "百度地图出现错误了，请重写输入地址");
					return;
				}
				latitude=arg0.getLocation().latitude;
				longitude=arg0.getLocation().longitude;
				Log.e("llll2", latitude+"");
				Log.e("11112", longitude+"");
				sendEmptyUiMessage(MsgConstants.MSG_01);
				
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
	private String phone_nubmer;
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if(arg0==1&&arg1==RESULT_OK){
			if(arg2.getStringExtra("name")==null)
				return;
			area=arg2.getStringExtra("name");
			code=arg2.getStringExtra("code");
			city=arg2.getStringExtra("city");
			et_region.setText(arg2.getStringExtra("name"));
		}
		if(arg0==2&&arg1==RESULT_OK){
			if (arg2 == null) {
				return;
			}
				            ContentResolver reContentResolverol = getContentResolver();  
				            Uri contactData = arg2.getData();
				            //查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
				            Cursor cursor = managedQuery(contactData, null, null, null, null);  
				            cursor.moveToFirst(); 
				            //获得DATA表中的名字
				            name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));  
				            //条件为联系人ID
				            et_name.setText(name);
				            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));  
				            // 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
				            Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,   
				                     null,   
				                     ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,   
				                     null,   
				                     null);  
				             while (phone.moveToNext()) {  
				            	 phone_nubmer = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));  
				                 if(!phone_nubmer.equals(""))
				                 et_phone.setText(phone_nubmer);
				             }  
				    
		}
	}
@SuppressWarnings("deprecation")
@OnClick({R.id.iv_loc,R.id.btn_submit})
public void add(View view){
	switch (view.getId()) {
	case R.id.iv_loc:
		startActivityForResult(new Intent(ChangeReceiverActivity.this,ProvCityActivity.class),1);
		break;
	case R.id.btn_submit:
		//com = et_com.getText().toString();
		name=et_name.getText().toString();
		phone=et_phone.getText().toString();
		address=et_region_address.getText().toString();
		if(!name.equals("")&&!phone.equals("")&&!code.equals("")&&!area.equals("")&&!address.equals("")&&(city!=null&&!city.equals(""))){
			if(!StringUtil.isMobileNO(phone)||(phone.length() != 11)){
			ToastUtil.shortToast(getApplicationContext(), "请输入正确的手机号码");
			}
			else{
			//	search.geocode(new GeoCodeOption().city(city).address(address));
				sendEmptyUiMessage(MsgConstants.MSG_01);
			}	
		//	search.geocode(new GeoCodeOption().city(city));
		}else{
			ToastUtil.shortToast(getApplicationContext(), "请输入完整的信息");
		}
		break;
	/*case R.id.iv_rece:
		Uri uri = Uri.parse("content://contacts/people");
		Intent intent = new Intent(Intent.ACTION_PICK, uri);
		startActivityForResult(intent, 2);*/
}
}
@Override
public void handleUiMessage(Message msg) {
	// TODO Auto-generated method stub
	super.handleUiMessage(msg);
	switch (msg.what) {
	case MsgConstants.MSG_01:
		JSONObject obj=new JSONObject();
		try {
			//obj.put("addressType","F");
			obj.put("cityCode", code);
			obj.put("cityName", city);
			obj.put("personName", name);
			obj.put("mobile", phone);
			obj.put("areaName",area);
			obj.put("address",address);
			obj.put("latitude", latitude);
			obj.put("longitude", longitude);
//			/obj.put("ifDefault", PreferencesUtils.getBoolean(getApplicationContext(), PreferenceConstants.BUTTON));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AsyncHttpUtils.doPut(getApplicationContext(), MCUrl.ADDRESS+"/"+addressId, obj.toString(), null, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				BaseBean bean=new Gson().fromJson(new String(arg2),BaseBean.class);
				if(bean.getErrCode()==0){
					ToastUtil.shortToast(getApplicationContext(), "修改成功");
					finish();
				}
				else{
					ToastUtil.shortToast(getApplicationContext(), "修改失败");
				}
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				Log.e("ddddd", arg0+"");
			}
		});
		break;
	}
}
}
