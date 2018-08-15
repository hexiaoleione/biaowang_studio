package com.hex.express.iwant.newactivity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.LocationClient;
import com.framework.base.BaseFragment;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.activities.HWdvertActivity;
import com.hex.express.iwant.activities.RechargeActivity;
import com.hex.express.iwant.activities.RegisterSetImageAndNameActivity;
import com.hex.express.iwant.bean.AreaBean;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.CityBean;
import com.hex.express.iwant.bean.InsBean;
import com.hex.express.iwant.bean.RegisterBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.helper.AreaDboperation;
import com.hex.express.iwant.helper.CheckDbUtils;
import com.hex.express.iwant.helper.CityDbOperation;
import com.hex.express.iwant.helper.DbManager;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.newbaidu.LocationDemo;
import com.hex.express.iwant.newsmain.NewMainActivity;
import com.hex.express.iwant.utils.Logger;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.StringUtil;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.DialogUtils;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import butterknife.Bind;
import butterknife.ButterKnife;

public class SendDepositFragment extends BaseFragment{

	@Bind(R.id.edifaname)
	EditText edifaname;
	@Bind(R.id.edifanphone)
	EditText edifanphone;
	@Bind(R.id.edishouname)
	EditText edishouname;
	@Bind(R.id.edishouphone)
	EditText edishouphone;
	@Bind(R.id.et_wuname)
	EditText et_wuname;
//	@Bind(R.id.edifaname)
//	EditText edifaname;
	@Bind(R.id.et_insure_my)
	EditText et_insure_my;
	@Bind(R.id.et_inumber)
	EditText et_inumber;
	@Bind(R.id.et_address)
	TextView et_address;
	@Bind(R.id.et_add_address)
	TextView et_add_address;
	@Bind(R.id.iv_add)
	ImageView iv_add;
	@Bind(R.id.iv_add2)
	ImageView	iv_add2;
//	@Bind(R.id.topBn)
//	ImageView	topBn;
	
	
	@Bind(R.id.tv_modifySenderAddress)
	TextView tv_modifySenderAddress;
	@Bind(R.id.tv_modifyRecieverAddress)
	TextView tv_modifyRecieverAddress;
	
	@Bind(R.id.toubao)
	TextView toubao;
	
	
	@Bind(R.id.et_zhong)
	Spinner et_zhong;
	@Bind(R.id.et_zhongdanwei)
	Spinner et_zhongdanwei;
	@Bind(R.id.et_tiji)
	Spinner et_tiji;
	@Bind(R.id.et_jian)
	Spinner et_jian;
	
	

	@Bind(R.id.checkbox_chang)
	CheckBox checkbox_chang;
	@Bind(R.id.checkbox_shui)
	CheckBox checkbox_shui;
	@Bind(R.id.checkbox_leng)
	CheckBox checkbox_leng;
	@Bind(R.id.checkbox_sui)
	CheckBox checkbox_sui;
	@Bind(R.id.checkbox_bao)
	CheckBox checkbox_bao;
	@Bind(R.id.checkbox_baojiben)
	CheckBox checkbox_baojiben;
	
	@Bind(R.id.checkbox_shuiguo)
	CheckBox checkbox_shuiguo;
	@Bind(R.id.checkbox_qinxu)
	CheckBox checkbox_qinxu;
	
	
	@Bind(R.id.checkbox_ming)
	CheckBox checkbox_ming;
	@Bind(R.id.imgbao)
	ImageView imgbao;
	
	@Bind(R.id.butsumit)
	Button  butsumit;

	 String  wu="";
	private int  xian=0;
	private boolean wwww1=false;
	private boolean wwww2=true;
	private boolean wwww3=true;
	private boolean ming=false;
	
	  private boolean namesb=false; 
	  private String ftownCode;
		private String fcityCode;
		private double fromLatitude;// 经度
		private double fromLongitude;// 纬度
		private String fromcity;
		private boolean 	fromreceive;
		
		
		private double latitude;
		private double longitude;
		private String city;
		private double mylatitude;// 经度
		private double mylongitude;// 纬度
		private LocationClient client;
		private String cityCode;
		private boolean frist = false;// 是否第一次获取位置成功
		private boolean ding=false; 
		private boolean receive;
		// 收件人经纬度
					private double receiver_longitude;
					private double receiver_latitude;
					private String receiver_citycode;
					private String	townCode,townaddressd;
					private String cityCode2,townCode2,townaddressd2;
					int kes=0;
					int kesse=0;
					int  jians=0;
					int  zhongge=0;
					public LoadingProgressDialog dialog;
	 View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view==null) {
			view=inflater.inflate(R.layout.newdeposit_activity, container, false);
		}
		 ViewGroup p = (ViewGroup) view.getParent(); 
         if (p != null) { 
             p.removeAllViewsInLayout(); 
         } 
         ButterKnife.bind(this, view);
         dialog=new LoadingProgressDialog(getActivity());
//         mContext=view.getContext();
         initView();
		 setOnClick();
		 initData() ;
		 getrequstBalance();
//        
//       
		return view;
	}
	
	/**
	 * 获取
	 */
	private void getrequstBalance() {
		RequestParams params = new RequestParams();
		AsyncHttpUtils.doGet(
				UrlMap.getUrl(MCUrl.BALANCE, "id",
						String.valueOf(PreferencesUtils.getInt( getActivity(), PreferenceConstants.UID))),
				null, null, params, new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
					}
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						Logger.e("json", "" + new String(arg2));
						
						RegisterBean bean = new Gson().fromJson(new String(arg2), RegisterBean.class);
						if (bean.getErrCode()==0) {
							edifaname.setText(bean.data.get(0).userName);
							edifanphone.setText(bean.data.get(0).mobile);
					}
						
					}

				});
	}
	public void initView() {
		// TODO Auto-generated method stub
		iv_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent intent1 = new Intent(Intent.ACTION_PICK);
				intent1.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
				startActivityForResult(intent1, 0);
			}
		});
		iv_add2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent intent1 = new Intent(Intent.ACTION_PICK);
				intent1.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
				startActivityForResult(intent1, 1);
			}
		});
		tv_modifySenderAddress.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent( getActivity(), LocationDemo.class), 3);
			}
		});
		tv_modifyRecieverAddress.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View arg0) {
		// TODO Auto-generated method stub
		startActivityForResult(new Intent( getActivity(), LocationDemo.class), 4);
		
		}
			});
		toubao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.putExtra("name", "1");
				intent.putExtra("url", "http://www.efamax.com/mobile/manual/inSureRuleAndroid.html");
				intent.setClass( getActivity(), HWdvertActivity.class);
				startActivity(intent);
				
			}
		});
	}

	public void initData() {
		// TODO Auto-generated method stub
		List<String> data_list ;
		data_list=new  ArrayList<String>();
//			data_list.add("≤");
	
		for (int i = 1; i <= 1000; i++) {
			data_list.add(i+"");
		}
		final String[] myItems = getResources().getStringArray(
				R.array.zhong);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>( getActivity(),
				android.R.layout.simple_spinner_item, data_list);
		adapter.setDropDownViewResource(R.layout.drop_down_item); 
		et_zhong.setAdapter(adapter);  
		et_zhong.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
						kes=position;
//					}
//				ToastUtil.shortToast(ReleaseActivity.this, "change2 "+change2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		List<String> data_list1 ;
		data_list1=new  ArrayList<String>();
//		data_list1.add("5公斤及以下");
		for (int i = 1; i <= 1000; i++) {
			data_list1.add(i+"件");
		}
		
		final String[] myItems1 = getResources().getStringArray(
				R.array.jianshu);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>( getActivity(),
				android.R.layout.simple_spinner_item, data_list1);
		adapter1.setDropDownViewResource(R.layout.drop_down_item); 
		et_jian.setAdapter(adapter1);  
		et_jian.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
//				submit.setVisibility(View.VISIBLE);
				jians=position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		List<String> data_list11 ;
		data_list11=new  ArrayList<String>();
//		data_list1.add("5公斤及以下");
		for (int i = 1; i <= 100; i++) {
			data_list11.add(i+"立方米");
		}
		
		final String[] myItems11 = getResources().getStringArray(
				R.array.jianshu);
		ArrayAdapter<String> adapter11 = new ArrayAdapter<String>( getActivity(),
				android.R.layout.simple_spinner_item, data_list11);
		adapter11.setDropDownViewResource(R.layout.drop_down_item); 
		et_tiji.setAdapter(adapter11);  
		et_tiji.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
//				submit.setVisibility(View.VISIBLE);
				kesse=position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		et_zhongdanwei.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				zhongge=arg2;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	public void setOnClick() {
		// TODO Auto-generated method stub
		
butsumit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getInsuran();
				
			}
		});
		
checkbox_chang.setOnCheckedChangeListener(new OnCheckedChangeListener() {
 			
 			@Override
 			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
 				// TODO Auto-generated method stub
 				if(arg1){
 					wu="1";
 					checkbox_shui.setChecked(false);
 	 				checkbox_leng.setChecked(false);
 	 				checkbox_sui.setChecked(false);
 	 				checkbox_qinxu.setChecked(false);
	 				checkbox_shuiguo.setChecked(false);
	 				checkbox_bao.setVisibility(View.VISIBLE);
 	 				checkbox_baojiben.setVisibility(View.VISIBLE);
// 	 				checkbox_bao.setVisibility(View.VISIBLE);
// 	 				checkbox_baojiben.setVisibility(View.VISIBLE);
// 	 				checkbox_bao.setChecked(true);
// 	 				checkbox_baojiben.setChecked(false);
 				}
 			
 					wwww1=arg1;
				
// 				else {
// 					wu="0";
//				}
 				
// 				else {
// 					w=0;
// 				}
 				}
// 			}
 		});
	checkbox_shui.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
 					wu="2";
 					checkbox_chang.setChecked(false);
 	 				checkbox_leng.setChecked(false);
 	 				checkbox_sui.setChecked(false);
 	 				checkbox_qinxu.setChecked(false);
	 				checkbox_shuiguo.setChecked(false);
 	 				checkbox_bao.setVisibility(View.INVISIBLE);
 	 				checkbox_bao.setChecked(false);
 	 				checkbox_baojiben.setVisibility(View.VISIBLE);
// 	 				checkbox_bao.setChecked(false);
// 	 				checkbox_baojiben.setChecked(true);
 				}
				
 					wwww1=arg1;
				
//				else {
// 					wu="0";
//				}
				
				}
//			}
		});
//	checkbox_leng.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
//				// TODO Auto-generated method stub
//				if(arg1){
//					
// 					wu=3;
// 					checkbox_chang.setChecked(false);
// 	 				checkbox_shui.setChecked(false);
// 	 				checkbox_sui.setChecked(false);
// 	 				checkbox_bao.setVisibility(View.VISIBLE);
// 	 				checkbox_baojiben.setVisibility(View.GONE);
// 				}else {
// 					wu=0;
//				}
//				
//				}
////			}
//		});
	checkbox_sui.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					wu="3";
 					checkbox_chang.setChecked(false);
 	 				checkbox_shui.setChecked(false);
 	 				checkbox_leng.setChecked(false);
 	 				checkbox_qinxu.setChecked(false);
	 				checkbox_shuiguo.setChecked(false);
	 				checkbox_bao.setVisibility(View.INVISIBLE);
	 				checkbox_bao.setChecked(false);
 	 				checkbox_baojiben.setVisibility(View.VISIBLE);
// 	 				checkbox_bao.setChecked(true);
// 	 				checkbox_baojiben.setChecked(false);
 				}
				
 					wwww1=arg1;
			
//				else {
// 					wu="0";
//				}
				
				}
//			}
		});
	checkbox_shuiguo.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub
			if(arg1){
				wu="3";
					checkbox_chang.setChecked(false);
	 				checkbox_shui.setChecked(false);
	 				checkbox_leng.setChecked(false);
	 				checkbox_qinxu.setChecked(false);
	 				checkbox_bao.setVisibility(View.INVISIBLE);
	 				checkbox_bao.setChecked(false);
 	 				checkbox_baojiben.setVisibility(View.VISIBLE);
				}
			
					wwww1=arg1;
		
			
			}
	});
	checkbox_qinxu.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub
			if(arg1){
				wu="4";
					checkbox_chang.setChecked(false);
	 				checkbox_shui.setChecked(false);
	 				checkbox_leng.setChecked(false);
	 				checkbox_shuiguo.setChecked(false);
	 				checkbox_bao.setVisibility(View.INVISIBLE);
	 				checkbox_bao.setChecked(false);
 	 				checkbox_baojiben.setVisibility(View.VISIBLE);
				}
			
					wwww1=arg1;
		
			
			}
	});
		
//		if (wu==2) {
//			tezong.setText("基本险");
//		}
//		lyzong.setOnClickListener(new OnClickListener() {
//	
//			@Override
//		public void onClick(View arg0) {
//		// TODO Auto-generated method stub
//		imgzong.setBackgroundResource(R.drawable.xuanzhong);
//		
//			}
//		});
		imgbao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				imgbao.setBackgroundResource(R.drawable.xuanzhong);
			}
		});
		checkbox_ming.setOnCheckedChangeListener(new OnCheckedChangeListener() {
 			
 			@Override
 			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
 				// TODO Auto-generated method stub
 				if(arg1){
 					ming=arg1;
 				
 				}else {
 					ming=arg1;
 				}
 				}
// 			}
 		});
		checkbox_bao.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					xian=2;
					checkbox_baojiben.setChecked(false);
 				}else {
 					xian=0;
 				}
				wwww2=arg1;
			}
		});
		checkbox_baojiben.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					checkbox_bao.setChecked(false);
					xian=1;
 				}else {
 					xian=0;
 				}
				wwww2=arg1;
			}
		});
	}

	public void getInsuran(){
		
		if ("".equals(edifaname.getText().toString()) || "".equals(edishouname.getText().toString()) ) {
			ToastUtil.shortToast( getActivity(), "请查看收发件人姓名");
			return;
		}
		String string =edifanphone.getText().toString();
		String string2 =edishouphone.getText().toString();
		  String tmpstr=string.replace(" ","");
		  String tmpstr2=string2.replace(" ","");
		if (!StringUtil.isMobileNO(tmpstr)
				|| (tmpstr.length() != 11)
						&& !StringUtil.isMobileNO(tmpstr2)
				|| (tmpstr2.length() != 11)) {
			ToastUtil.shortToast( getActivity(), "请输入正确的手机号码");
			return;
		}
		if ("".equals(et_address.getText().toString()) || "".equals(et_add_address.getText().toString()) ) {
			ToastUtil.shortToast( getActivity(), "请填写地址");
			return;
		}
		if ("".equals(et_wuname.getText().toString()) || "".equals(et_insure_my.getText().toString())
				|| "".equals(et_inumber.getText().toString())) {
			ToastUtil.shortToast( getActivity(), "请查看货物名称或价值，车牌号是否完成");
			return;
		}
//		if ("0".equals(wu) || "".equals(wu)) {
//			ToastUtil.shortToast( getActivity(), "请选择物品种类");
//			return;
//		}
		if ( !wwww1 ) {
			ToastUtil.shortToast( getActivity(), "请选择物品种类");
			return;
		}
//		if ( !wwww2 ) {
//			ToastUtil.shortToast( getActivity(), "请选择承险种类");
//			return;
//		}
		
		if (et_insure_my.getText().toString().equals("") ||  et_inumber.getText().toString().equals("")) {
			ToastUtil.shortToast( getActivity(), "请补充完信息");
			return;	
		}
		if (!ming) {
			ToastUtil.shortToast( getActivity(), "请阅读并同意说明");
			return;	
		}
		if (xian==0) {
			ToastUtil.shortToast( getActivity(), "请选择保险类别");
			return;
		}
		JSONObject obj = new JSONObject();
		try {
//			obj.put("recId", getIntent().getIntExtra("recId", 0));
			
			obj.put("userId",String.valueOf(PreferencesUtils.getInt( getActivity(), PreferenceConstants.UID)));
			obj.put("cargoCost", et_insure_my.getText().toString());
			obj.put("carNumImg", et_inumber.getText().toString());
			obj.put("category", wu);
			
		   obj.put("insurance", xian);
		   if (zhongge==0) {
				obj.put("cargoWeight",sunmit(1, kes)+"吨");//重量
			}else {
				obj.put("cargoWeight",sunmit(1, kes)+"公斤");//重量
			}
		   
		   obj.put("cargoSize", sunmit(1, jians));
		   obj.put("cargoVolume", sunmit(1, kesse)+"立方米");
		   
		   
			obj.put("startPlace", et_address.getText().toString());
			obj.put("entPlace",et_add_address.getText().toString()+"北京");
			obj.put("sendPerson", edifaname.getText().toString());
			obj.put("sendPhone", tmpstr);
			obj.put("cargoName", et_wuname.getText().toString());//货物名称  l
		   obj.put("takeCargo", true);//是否需要取货  up
			obj.put("sendCargo", true);//是否需要送货  down
			obj.put("takeName", edishouname.getText().toString());//收货人姓名  
//			obj.put("takeMobile", et_add_tel.getText().toString());//收货电话 
			obj.put("takeMobile", tmpstr2);//收货电话
			
			obj.put("entPlaceCityCode", receiver_citycode);
			obj.put("startPlaceCityCode", fcityCode);
			obj.put("startPlaceTownCode", ftownCode);
			
			obj.put("fromLatitude",  fromLatitude);
			obj.put("fromLongitude", fromLongitude);
	
			obj.put("latitudeTo", receiver_latitude);
			obj.put("longitudeTo", receiver_longitude);
		   
		} catch (JSONException e) {
			e.printStackTrace();
		}
		dialog.show();
		Log.e("查看数据", obj.toString());
		AsyncHttpUtils.doPostJson( getActivity(), MCUrl.publishInsureNew, obj.toString(),
				new AsyncHttpResponseHandler() {
//		RequestParams params = new RequestParams();
//		AsyncHttpUtils.doGet(UrlMap.getfour(MCUrl.LogAddInsurance, "recId",getIntent().getIntExtra("WLBId", 0)+"",
//				"rate",adbean.getData().get(0).value, "cargoCost",jiazhi.getText().toString(), "carNumImg",icon),
//				null, null, params,
//				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("json", new String(arg2));
						dialog.dismiss();
						final InsBean	 inensb = new Gson().fromJson(new String(arg2),
								InsBean.class);
						if (inensb.errCode==0) {
//							Intent intent=new Intent();
//							intent.setClass(InsureActivity.this, NewMainActivity.class);
//							startActivity(intent);
//							finish();
							Builder ad = new Builder( getActivity());
							ad.setTitle("温馨提示");
							ad.setMessage("此单保费"+inensb.getData().get(0).insureCost+"元");
							
							ad.setPositiveButton("立即支付", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									getaddsurplus(inensb.getData().get(0).recId,inensb.getData().get(0).insureCost);
								}
							});
							ad.setNegativeButton("暂不支付", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									arg0.dismiss();
									Intent intent=new Intent();
									intent.setClass( getActivity(), NewMainActivity.class);
									startActivity(intent);
								}
							});
							ad.create().show();
						}else if (inensb.errCode==-4) {
							Builder ad = new Builder( getActivity());
							ad.setTitle("温馨提示");
							ad.setMessage(""+inensb.message);
							
							ad.setPositiveButton("立即认证", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0, int arg1) {
//									arg0.dismiss();
									startActivity(new Intent(getActivity(),
											RegisterSetImageAndNameActivity.class).putExtra("tiaoguo", "2"));
								}
							});
							ad.setNegativeButton("暂不认证", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									arg0.dismiss();
//									Intent intent=new Intent();
//									intent.setClass( getActivity(), NewMainActivity.class);
//									startActivity(intent);
								}
							});
							ad.create().show();
							
						} else {
							ToastUtil.shortToast( getActivity(), inensb.message);
						}
					}
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub
						
					}
				});
	}
	/**
	 * 余额支付的接口
	 */
	public void getaddsurplus(int recId, String insureCost )   {
//		if (billCode.equals("") || money.equals("")) {
//			ToastUtil.shortToast(getApplicationContext(), "该订单有问题");
//			return;
//		}
//		Log.e("1111   余额obj", obj.toString());
		dialog.show();
		AsyncHttpUtils.doGet(UrlMap.getThreeUrl(MCUrl.payInsure, "recId", ""+recId, "","", "", ""), null,
				null, null, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method
						// stub
						dialog.dismiss();
						if (arg2 == null)
							return;
						Log.e("msg", new String(arg2));
						BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
						if (bean.getErrCode() == 0) {
							ToastUtil.shortToast( getActivity(), bean.getMessage());
							Intent intent=new Intent();
							intent.setClass( getActivity(), NewMainActivity.class);
							startActivity(intent);
//							finish();
						}else
						if (bean.getErrCode() ==-3 ) {
							ToastUtil.shortToast( getActivity(), bean.getMessage());
//							Intent intent=new Intent();
//							intent.setClass(DepositNewActivity.this, NewMainActivity.class);
//							startActivity(intent);
//							finish();
							DialogUtils.createAlertDialogTwo(getActivity(), "温馨提示", bean.getMessage(), 0, "去充值",
									"取消", new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog, int which) {

											startActivity(new Intent(getActivity(), RechargeActivity.class));
										}

									}).show();
						}else {
							ToastUtil.shortToast(getActivity(), bean.getMessage());
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method
						// stub
						dialog.dismiss();
					}
				});
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("11111s", "requestCode"+requestCode+"resultCode"+resultCode+"data"+data);
		switch (requestCode) {
		case 0:// 增加手机寄件人联系人的回调
			if (data == null) {
				return;
			}
			if (resultCode == Activity.RESULT_OK) {
				ContentResolver reContentResolverol =  getActivity().getContentResolver();
				Uri contactData = data.getData();
				// 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
				Cursor cursor = getActivity().managedQuery(contactData, null, null, null, null);
				cursor.moveToFirst();
				if (cursor.moveToFirst()) {
					namesb=true;
				// 获得DATA表中的名字
				String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				// 条件为联系人ID
				edifaname.setText(name);
//				namese=name;
				 String[] contact = new String[2];
                // 查看联系人有多少个号码，如果没有号码，返回0
				int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
				String[] strs = cursor.getColumnNames();
	            for (int i = 0; i < strs.length; i++) {
	                if (strs[i].equals("data1")) {
	                    ///手机号
	                    contact[1] = cursor.getString(cursor.getColumnIndex(strs[i])).replace("+86","").replaceAll(" ","").trim();
	                    edifanphone.setText(""+contact[1]);
//	                	phonese=contact[1];
	                }
	            }
//              
//				String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//				// 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
//				Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//						ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
//				while (phone.moveToNext()) {
//					String phone_number = phone
//							.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//					if (!phone_number.equals("")){
//						et_tel.setText(phone_number);
//						phonese=phone_number;
//					}
//				}
				if(Build.VERSION.SDK_INT < 14) {
				    cursor.close();
				   }
//				phone.close();
				}else{  
	                // 没有权限，跳到设置界面，调用Android系统“应用程序信息（Application Info）”界面  
	                new Builder( getActivity())
	                .setMessage("app需要开启读取联系人权限")  
	                .setPositiveButton("设置", new DialogInterface.OnClickListener() {  
	                    @Override  
	                    public void onClick(DialogInterface dialogInterface, int i) {  
	                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);  
	                        intent.setData(Uri.parse("package:" + getActivity().getPackageName()));  
	                        startActivity(intent);  
	                    }  
	                })  
	                .setNegativeButton("取消", null)  
	                .create()  
	                .show();  
	                  
	            }   
			}

			break;
		case 1:// 增加手机收件人联系人的回调
			if (data == null) {
				return;
			}
			if (resultCode == Activity.RESULT_OK) {
				ContentResolver reContentResolverol =  getActivity().getContentResolver();
				Uri contactData = data.getData();
				// 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
				Cursor cursor =   getActivity().managedQuery(contactData, null, null, null, null);
//				cursor.moveToFirst();
				if (cursor.moveToFirst()) {
				// 获得DATA表中的名字
				String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				// 条件为联系人ID
				edishouname.setText(name);
				
//				String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//				// 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
//				Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//						ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
////			   if (phone==null) {et_add_tel
////				return;
////			   }
//				while (phone.moveToNext()) {
//					String phone_number = phone
//							.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//					if (!phone_number.equals("")){
//						et_add_tel.setText(phone_number);
//					}
//				}
				 String[] contact = new String[2];
	                // 查看联系人有多少个号码，如果没有号码，返回0
					int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
					String[] strs = cursor.getColumnNames();
		            for (int i = 0; i < strs.length; i++) {
		                if (strs[i].equals("data1")) {
		                    ///手机号
		                    contact[1] = cursor.getString(cursor.getColumnIndex(strs[i])).replace("+86","").replaceAll(" ","").trim();
		                    edishouphone.setText(""+contact[1]);
		                }
		            }
				if(Build.VERSION.SDK_INT < 14) {
				    cursor.close();
				   }
//				phone.close();
			}else{  
				 // 没有权限，跳到设置界面，调用Android系统“应用程序信息（Application Info）”界面  
                new Builder( getActivity())
                .setMessage("app需要开启读取联系人权限")  
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {  
                    @Override  
                    public void onClick(DialogInterface dialogInterface, int i) {  
                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);  
                        intent.setData(Uri.parse("package:" +  getActivity().getPackageName()));  
                        startActivity(intent);  
                    }  
                })  
                .setNegativeButton("取消", null)  
                .create()  
                .show();  
            }   
		}

			break;
		case 3:
			if (data == null) {
				return;
			}
			if (resultCode == -1) {
				 fromLatitude = data.getDoubleExtra("latitude", 0);
				fromLongitude = data.getDoubleExtra("longitude", 0);
				fromcity = data.getStringExtra("city");
				fromreceive = true;
				getCityCodefrom(true);
				et_address.setText(data.getStringExtra("address").replace("中国", ""));
			}
			break;
		case 4:
			if (data == null) {
				return;
			}
			if (resultCode == -1) {
				receiver_latitude = data.getDoubleExtra("latitude", 0);
				receiver_longitude = data.getDoubleExtra("longitude", 0);
				city = data.getStringExtra("city");
				receive = true;
				getCityCode(true);
				et_add_address.setText(data.getStringExtra("address").replace("中国", ""));
			}
			break;
		case 10:
			
			break;
		
		}
		}
	private void getCityCode(boolean dingwei) {
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
			if (dingwei) {
		if (receive) {
			receiver_citycode = selectDataFromDb.get(0).city_code;
			Log.e("citycode", receiver_citycode);

		} else {
			ding=true;
			cityCode2 = selectDataFromDb.get(0).city_code;
//			Log.e("citycode", cityCode);
			List<AreaBean> selectDataFromDbs = new AreaDboperation()
					.selectDataFromDb("select * from area where area_name='" + townaddressd2 + "'");
			if (selectDataFromDbs.size()>0 && !selectDataFromDbs.equals("")) {
				townCode2=selectDataFromDbs.get(0).area_code;
//				Log.e("11111townCode", townCode);
			}
		}
		}else {
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
//		if (townaddressd.equals("")) {
//			townaddressd = PreferencesUtils.getString(getActivity(), PreferenceConstants.Codedess);
//		}
	}
	private void getCityCodefrom(boolean dingwei) {
		boolean isCopySuccess = CheckDbUtils.checkDb();
		// 成功的将数据库copy到data 中
		if (isCopySuccess) {
			iWantApplication.getInstance().mDbManager = new DbManager(iWantApplication.getInstance());
		}
		if (fromcity == null || fromcity.equals("")) {
//			ToastUtil.shortToast(getActivity(), "请输入完整信息");
			return;
		}
		if (!fromcity.contains("市")) {
			fromcity = fromcity + "市";
		}
		List<CityBean> selectDataFromDb = new CityDbOperation()
				.selectDataFromDb("select * from city where city_name='" + fromcity + "'");
		if (selectDataFromDb.size() > 0 && !selectDataFromDb.equals("")) {
//		if (receive) {
//			fcityCode = selectDataFromDb.get(0).city_code;
//			Log.e("citycode", receiver_citycode);
//}
			fcityCode = selectDataFromDb.get(0).city_code;
//			Log.e("citycode", cityCode);
			List<AreaBean> selectDataFromDbs = new AreaDboperation()
					.selectDataFromDb("select * from area where area_name='" + townaddressd2 + "'");
			if (selectDataFromDbs.size()>0 && !selectDataFromDbs.equals("")) {
				ftownCode=selectDataFromDbs.get(0).area_code;
//				Log.e("11111townCode", townCode);
			}
		}
//		if (townaddressd.equals("")) {
//			townaddressd = PreferencesUtils.getString(getActivity(), PreferenceConstants.Codedess);
//		}
	}
	public int sunmit(int a ,int b){
		int sum=a+b;
		return sum;
	}
}

