package com.hex.express.iwant.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.adapters.BaseListAdapter;
import com.hex.express.iwant.adapters.ReceiverAdapter;
import com.hex.express.iwant.bean.SendPersonBean;
import com.hex.express.iwant.bean.SendPersonBean.Data;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.StringUtil;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 【常用收件人】
 * @author han
 *
 */
public class AddReceiverActivity extends BaseActivity {
	@Bind(R.id.tbv_show) TitleBarView tbv_show;
	//@Bind(R.id.et_com)   EditText et_com;
	@Bind(R.id.et_name)  EditText et_name;
	@Bind(R.id.et_phone) EditText et_phone;
	@Bind(R.id.et_region) EditText et_region;
	@Bind(R.id.et_region_address)EditText et_region_address;
	@Bind(R.id.iv_loc)ImageView iv_loc;
	private String area;
	private String code;
	private LoadingProgressDialog dialog;
	private GeoCoder search;
	private double latitude;
	private double longitude;
	private String name;
	private String phone;
	private String address;
	private String city;
	private List<SendPersonBean.Data> list=new ArrayList<SendPersonBean.Data>();
	@Bind(R.id.lv) ListView lv;
	@Bind(R.id.rl) LinearLayout rl;
	private BaseListAdapter adapter;
	@Bind(R.id.rl_add) RelativeLayout rl_add;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_receiver);
		ButterKnife.bind(this);
		initData();
	}
	@Override
	public void onWeightClick(View v) {

	}

	@Override
	public void initView() {

	}

	@Override
	public void initData() {
		tbv_show.setTitleText("常用收件人");
		getData();
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
	protected void onResume() {
		super.onResume();
		isRefresh=true;
		if(adapter!=null&&isRefresh){
			getData();
		}
	}
private boolean isRefresh;
	@Override
	public void getData() {
AsyncHttpUtils.doSimGet(UrlMap.getUrl(MCUrl.RECEIVER_LIST, "userId", PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)+""), new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				dialog.dismiss();
				SendPersonBean bean=new Gson().fromJson(new String(arg2),SendPersonBean.class);
			//	Log.e("list",bean.data.get(0).cityCode);
				list=bean.data;
				if(list!=null&&list.size()!=0){
					if(adapter==null){
					adapter=new ReceiverAdapter(AddReceiverActivity.this, list,rl_add);
					rl.setVisibility(View.VISIBLE);
					rl_add.setVisibility(View.GONE);
					lv.setAdapter(adapter);
					}else if(isRefresh){
						adapter.setData(list);
						adapter.notifyDataSetChanged();
					}
					lv.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							SendPersonBean.Data data = (Data) list.get(position);
							Intent intent=new Intent();
							intent.putExtra("name", data.personName);
							Log.e("name", data.personName);
							intent.putExtra("phone", data.mobile);
							intent.putExtra("location",data.areaName);
							intent.putExtra("address", data.address);
							intent.putExtra("latitude", data.latitude);
							intent.putExtra("longitude", data.longitude);
							intent.putExtra("city", data.cityName);
							intent.putExtra("citycode", data.cityCode);
							setResult(RESULT_OK, intent);
							finish();
						}
					});
				}
				else{
					rl.setVisibility(View.GONE);
					rl_add.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Log.e("list", arg0+"");
				//di
				
			}
		});
		// TODO Auto-generated method stub

	}
	private String phone_number;
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
			iv_loc.setVisibility(View.VISIBLE);
		}
		if(arg0==2&&arg1==RESULT_OK){
			if (arg2 == null) {
				return;
			}
				            ContentResolver reContentResolverol = getContentResolver();  
				            Uri contactData = arg2.getData();
				            //查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
				            @SuppressWarnings("deprecation")
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
				                 phone_number = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));  
				                 if(!phone_number.equals(""))
				                 et_phone.setText(phone_number);
				             }  
				    
		}
	}
@SuppressWarnings("deprecation")
@OnClick({R.id.iv_loc,R.id.btn_submit,R.id.iv_rece})
public void add(View view){
	switch (view.getId()) {
	case R.id.iv_loc:
		startActivityForResult(new Intent(AddReceiverActivity.this,ProvCityActivity.class),1);
		break;
	case R.id.btn_submit:
		//com = et_com.getText().toString();
		name=et_name.getText().toString();
		phone=et_phone.getText().toString();
		address=et_region_address.getText().toString();
		Log.e("######", code+"????"+area);
		if(!name.equals("")&&!phone.equals("")&&!address.equals("")&&(city!=null&&!city.equals(""))){
			if (!code.equals("")&&code!=null&&!area.equals("")&&area!=null) {
				String	tmpstr=phone.replace(" ","");
				if(!StringUtil.isMobileNO(tmpstr)||(tmpstr.length() != 11)){
					ToastUtil.shortToast(getApplicationContext(), "请输入正确的手机号码");
					}
					else{
						GeoCodeOption option=new GeoCodeOption();
						option.address(address);
						option.city(city);
						search.geocode(option);
					}	
			}else {
				ToastUtil.shortToast(getApplicationContext(), "请填写地址信息");
			}
			
			
		}else{
			ToastUtil.shortToast(getApplicationContext(), "请输入完整的信息");
		}
		break;
	case R.id.iv_rece:
		Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI); 
		startActivityForResult(intent, 2);
		break;
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
			obj.put("userId", PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID));
			obj.put("addressType","T");
			obj.put("cityCode", code);
			obj.put("cityName", city);
			obj.put("personName", name);
			obj.put("mobile", phone);
			obj.put("areaName",area);
			obj.put("address",address);
			obj.put("latitude", latitude);
			obj.put("longitude", longitude);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e("shuju", obj.toString());
		AsyncHttpUtils.doPostJson(getApplicationContext(), MCUrl.ADDRESS, obj.toString(), new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
			//	LoadingProgressDialog.getInstance(AddCourierActivity.this).dismiss();
				Log.e("address---",new String(arg2));
				//ToastUtil.shortToast(getApplicationContext(), "保存成功");
				Intent intent=new Intent();
				intent.putExtra("name", name);
				intent.putExtra("phone", phone);
				intent.putExtra("location",area);
				intent.putExtra("address", address);
				intent.putExtra("latitude", latitude);
				intent.putExtra("longitude",longitude);
				intent.putExtra("city", city);
				intent.putExtra("citycode", code);
				setResult(RESULT_OK, intent);
				finish();
			}
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Log.e("address---",arg0+""+ new String(arg2));
			}
		});
		break;
	}
}
}
