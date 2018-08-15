package com.hex.express.iwant.activities;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ChangeAddressActivity extends BaseActivity {

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
		setContentView(R.layout.activity_change_address);
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
	protected void onResume() {
		super.onResume();
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
			public void onGetReverseGeoCodeResult(final ReverseGeoCodeResult arg0) {
				// TODO Auto-generated method stub
				if(arg0==null||arg0.error!=SearchResult.ERRORNO.NO_ERROR||arg0.getLocation().latitude==0||arg0.getLocation().longitude==0)
				{
					dialog.dismiss();
					Builder ad = new Builder(ChangeAddressActivity.this);
					ad.setTitle("温馨提示");
					ad.setMessage("请确认地址信息无误");
					ad.setPositiveButton("确认", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							sendEmptyUiMessage(MsgConstants.MSG_01);
						}
					}); 
					ad.setNegativeButton("重新填写", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					ad.create().show();
//					ToastUtil.shortToast(getApplicationContext(), "百度地图出现错误了，请重写输入地址");
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
					Builder ad = new Builder(ChangeAddressActivity.this);
					ad.setTitle("温馨提示");
					ad.setMessage("请确认地址信息无误");
					ad.setPositiveButton("确认", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							
							sendEmptyUiMessage(MsgConstants.MSG_01);
						}
					}); 
					ad.setNegativeButton("重新填写", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					ad.create().show();
//					ToastUtil.shortToast(getApplicationContext(), "百度地图出现错误了，请重写输入地址");
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
	}
@SuppressWarnings("deprecation")
@OnClick({R.id.btn_submit,R.id.iv_loc})
public void add(View view){
	switch (view.getId()) {
	case R.id.iv_loc:
		startActivityForResult(new Intent(ChangeAddressActivity.this,ProvCityActivity.class),1);
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
				search.geocode(new GeoCodeOption().city(city).address(address));
			}	
		}else{
			ToastUtil.shortToast(getApplicationContext(), "请输入完整的信息");
		}
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
