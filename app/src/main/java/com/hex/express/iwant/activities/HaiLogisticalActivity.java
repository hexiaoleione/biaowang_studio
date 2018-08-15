package com.hex.express.iwant.activities;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.LocationClient;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.activities.HaiLogisticalFreightActivity.HaiMylisAdapter;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.CityBean;
import com.hex.express.iwant.bean.HaiBean;
import com.hex.express.iwant.bean.IconBean;
import com.hex.express.iwant.bean.LogisBean;
import com.hex.express.iwant.bean.MyloginBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.helper.CheckDbUtils;
import com.hex.express.iwant.helper.CityDbOperation;
import com.hex.express.iwant.helper.DbManager;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.Logger;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.StringUtil;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.RoundCornerImageView;
import com.hex.express.iwant.views.TimePickerView;
import com.hex.express.iwant.adapters.SelectListAdapter;
import com.hex.express.iwant.adapters.SelectListAdapter.OnDelBtnClickListener;
import com.hex.express.iwant.adapters.SelectListAdapter.OnItemClickListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.R.integer;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.PopupWindow.OnDismissListener;
import butterknife.Bind;
import butterknife.ButterKnife;

public class HaiLogisticalActivity extends BaseActivity implements OnItemClickListener, OnDelBtnClickListener, OnDismissListener{
	
	@Bind(R.id.hai_name_huo)//货物名称
	EditText hai_name_huo;
	@Bind(R.id.hai_faname)//发货人姓名
	EditText hai_faname;
	@Bind(R.id.hai_faphone)//发货人手机号
	EditText hai_faphone;
	
	@Bind(R.id.hai_chufa)//出发地
	EditText hai_chufa;
	@Bind(R.id.hai_address_specific)//出发详细地址
	EditText hai_address_specific;
	@Bind(R.id.hai_name)//收货人手机号
	EditText hai_name;
	@Bind(R.id.hai_phone)//收货人手机号
	EditText hai_phone;
	@Bind(R.id.hai_daoda)//到达地址
	EditText hai_daoda;
	@Bind(R.id.hai_add_address_specific)//到达详细地址
	EditText hai_add_address_specific;
	@Bind(R.id.hai_zhong)//重量
	EditText hai_zhong;
	@Bind(R.id.hai_tiji)//体积
	EditText hai_tiji;
	@Bind(R.id.spinner1)//重量选择
	Spinner spinner1;
	@Bind(R.id.spinner_leibie)//货物类别
	Spinner spinner_leibie;
	@Bind(R.id.spinner_bao)//保险类别
	Spinner spinner_bao;
	@Bind(R.id.img_headportrait)//车牌号
	RoundCornerImageView img_headportrait;
	@Bind(R.id.hai_qian)//保险钱
	EditText hai_qian;
	@Bind(R.id.hai_carnumber)//车牌号
	EditText hai_carnumber;
	
	@Bind(R.id.hai_submit)//
	Button hai_submit;
	@Bind(R.id.hai_bap)
	TextView hai_bap;
	boolean weith;//重量  顿true
	private Bitmap head;
	private String fileName = path + "haicard.png";
	private static String path = "/sdcard/myHead/";// sd路径
	private Map map = new HashMap<String, String>();
	private Map map_file = new HashMap<String, File>();
	private String result;
	private String icon="";
	private boolean flag;
	private IconBean bean;
	private boolean frist = false;
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
	private String receiver_citycoded;
	private boolean receive;
	int  leibie=0;//1 常规货物类、2 蔬菜水果、3 冷藏冷冻货、4 易碎品
	int  bao=0; //承险类别  1 基本险  2综合险
	
	private RelativeLayout mInputLayout;
	private ImageButton mArrow;
	private TextView mInput;
	private PopupWindow mSelectWindow;
	private LayoutInflater mInflater;
	private ArrayList<String> mAccounts;
	HaiBean haibean1;
	LogisBean Logisbean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hailogistical);//activity_hailogistical  activity_down_logistical
		ButterKnife.bind(this);
		initView();
		getHttprequst();
		initData();
		setOnClick();
	}

	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		mInputLayout=(RelativeLayout) findViewById(R.id.input_layout);
		mArrow = (ImageButton) findViewById(R.id.input_arrow);
		mInput = (TextView) findViewById(R.id.input_et);
		if (isLogin()) {
			if (PreferencesUtils.getString(HaiLogisticalActivity.this, PreferenceConstants.USERNAME) != null
					&& !PreferencesUtils.getString(HaiLogisticalActivity.this, PreferenceConstants.USERNAME).equals("")) {
				hai_faname.setText(PreferencesUtils.getString(HaiLogisticalActivity.this, PreferenceConstants.USERNAME));
			}
			if (!PreferencesUtils.getString(HaiLogisticalActivity.this, PreferenceConstants.MOBILE).equals("")
					&& PreferencesUtils.getString(HaiLogisticalActivity.this, PreferenceConstants.MOBILE) != null) {
				hai_faphone.setText(PreferencesUtils.getString(HaiLogisticalActivity.this, PreferenceConstants.MOBILE));
			}
		}
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mAccounts = new ArrayList<String>();
		
	}
	public void onDownArrowClicked(View view)
	{
		if (mAccounts.size() != 0)
		{
			mArrow.setBackgroundResource(R.drawable.arrow_up);
			showAccountChoiceWindow();
		}
	}

	private void showAccountChoiceWindow()
	{
		View view = mInflater.inflate(R.layout.input_selectlist, null);
		RelativeLayout contentview = (RelativeLayout) view.findViewById(R.id.input_select_listlayout);
		ListView userlist = (ListView) view.findViewById(R.id.input_select_list);
		userlist.setDividerHeight(0);

		SelectListAdapter adapter = new SelectListAdapter(this, mAccounts);
		adapter.setOnItemClickListener(this);
		adapter.setOnDelBtnClickListener(this);
		userlist.setAdapter(adapter);

		mSelectWindow = new PopupWindow(contentview, mInputLayout.getMeasuredWidth(), LayoutParams.WRAP_CONTENT, true);
		mSelectWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_input_bottom_normal));
		int heig = HaiLogisticalActivity.this.getWindowManager().getDefaultDisplay().getHeight();
//		mSelectWindow.setHeight(heig*3/5);
		if (haibean1.data.size()>6) {
			mSelectWindow.setHeight(heig*3/5);
		}
		mSelectWindow.setOutsideTouchable(true);
		mSelectWindow.setFocusable(true);
		mSelectWindow.setOnDismissListener(this);
//		mSelectWindow.showAsDropDown(mInputLayout, 0, 0);
		int[] location = new int[2];
		mInputLayout.getLocationOnScreen(location);
		mSelectWindow.showAtLocation(mInputLayout, Gravity.NO_GRAVITY, location[0], location[1]-mSelectWindow.getHeight());
		mArrow.setBackgroundResource(R.drawable.arrow_up);
	}

	@Override
	public void onDelBtnClicked(int position)
	{
		mSelectWindow.dismiss();
		String inputAccount = mInput.getText().toString();
		if (inputAccount == mAccounts.remove(position))
		{
			String nextAccount = "";

			if (mAccounts.size() != 0)
				nextAccount = mAccounts.get(0) + "";

			mInput.setText(nextAccount);
		}
		
		detegetHttprequst(haibean1.getData().get(position).recId);
//		Toast.makeText(this, "删除账号成功", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onItemClicked(int position)
	{
		mSelectWindow.dismiss();
		mInput.setText(mAccounts.get(position) + "");
	}

	@Override
	public void onDismiss()
	{
		mArrow.setBackgroundResource(R.drawable.arrow_down);
	}
	
	/**
	 * 获取车牌
	 */
	private void getHttprequst() {
		RequestParams params = new RequestParams();
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.HaiCarNumber, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID))), null, null, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("json", new String(arg2));
						haibean1 = new Gson().fromJson(new String(arg2),
								HaiBean.class);
						dialog.dismiss();
						for (int i = 0; i < haibean1.data.size(); i++)
						{
							mAccounts.add(haibean1.data.get(i).carNum);
						}
						if (mAccounts.size()>0) {
							mInput.setText(mAccounts.get(0) + "");
						}
						} 

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						dialog.dismiss();
//						ToastUtil.shortToast(HaiLogisticalActivity.this, "网络请求加载失败");

					}
				});

	}


	/**
	 * 删除车牌
	 */
	private void detegetHttprequst( int recId) {
		RequestParams params = new RequestParams();
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.HaiDeleteCarNum, "recId", ""+recId), null, null, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						BaseBean		bean = new Gson().fromJson(new String(arg2),
								BaseBean.class);
						if (bean.getErrCode()!=0) {
							ToastUtil.shortToast(HaiLogisticalActivity.this, bean.getMessage());
						}
					}
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						dialog.dismiss();
						ToastUtil.shortToast(HaiLogisticalActivity.this, "网络请求加载失败");

					}
				});

	}

	
	
	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub
		hai_chufa.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				startActivityForResult(new Intent(HaiLogisticalActivity.this, AddressActivity.class), 7);
				startActivityForResult(new Intent(HaiLogisticalActivity.this, NewAddressActivity.class), 7);
				
			}
		});
		hai_daoda.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				startActivityForResult(new Intent(HaiLogisticalActivity.this, AddressReceiveActivity.class), 8);
				startActivityForResult(new Intent(HaiLogisticalActivity.this, NewAddressActivity.class), 8);
			}
		});
		hai_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				addPostResult();
//				ceshi();
			}
		});
		
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
	        		if(position==1){
	        			weith=true;
	              }else {
	            	    weith=false;
				   }
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {

				}
       });
	        final String[] myItems2 = getResources().getStringArray(
					R.array.leibie);
			ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, myItems2);
			adapter.setDropDownViewResource(R.layout.drop_down_item);  
		        //第四步：将适配器添加到下拉列表上    
			spinner_leibie.setAdapter(adapter2);  
			spinner_leibie.setOnItemSelectedListener(new OnItemSelectedListener() {
		        	@Override
					public void onItemSelected(AdapterView<?> parent, View view,
							int position, long id) {
		        		leibie=position;

						if (leibie==0) {
							hai_bap.setVisibility(View.GONE);
					    	spinner_bao.setVisibility(View.VISIBLE);
						}else {
//							 Log.e("11122    ", "bao leibie"+leibie);
							hai_bap.setVisibility(View.VISIBLE);
							spinner_bao.setVisibility(View.GONE);
						
						}
					}
					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
	       });
		      
//					ArrayAdapter<String> adapter3;
				  final String[] myItems3 = getResources().getStringArray(
								R.array.baoxianleibie);
				  ArrayAdapter<String>  adapter3 = new ArrayAdapter<String>(this,
								android.R.layout.simple_spinner_item, myItems3);
				adapter.setDropDownViewResource(R.layout.drop_down_item);  
			        //第四步：将适配器添加到下拉列表上    
					spinner_bao.setAdapter(adapter3); 
			    	spinner_bao.setOnItemSelectedListener(new OnItemSelectedListener() {
			        	@Override
						public void onItemSelected(AdapterView<?> parent, View view,
								int position, long id) {
			        		  if (leibie!=0  && position!=0) {
			        			  ToastUtil.longToast(HaiLogisticalActivity.this, "此类物品只支持基本险，请选择基本险");
								}
			        		  bao=position;
			        	 	
//			        		Log.e("111    ", "bao position"+position);
						}
						@Override
						public void onNothingSelected(AdapterView<?> parent) {

						}
		       });
			    
//				img_headportrait.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View arg0) {
//						// TODO Auto-generated method stub
//						showPopwindow();
//					}
//				});
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}
//	private void ceshi(){
//		Intent intent = new Intent(HaiLogisticalActivity.this, HaiLogisPayActivity.class);
//	 	   intent.putExtra("evaluationStatus","0");//余额支付 的价格	
//	 		intent.putExtra("insureCost", "0.1");
//	 		intent.putExtra("evaluationScore", "0.1");//其它支付的价格
//	 		intent.putExtra("billCode", "72795WL20170110102855474");//物流单号
//	 		intent.putExtra("way", "1");
//			intent.putExtra("recId", 72);
//			if (leibie!=0) {
//				Log.e("111111", ""+leibie);
//			}else {
//				Log.e("1111112", ""+leibie);
//			}
//	 		startActivityForResult(intent, 10);
//	}
	private void addPostResult() {
		JSONObject obj = new JSONObject();
		if (!StringUtil.isMobileNO(hai_faphone.getText().toString())
				|| (hai_faphone.getText().toString().trim().length() != 11)
					|| !StringUtil.isMobileNO(hai_phone.getText().toString())
					|| (hai_phone.getText().toString().trim().length() != 11)	) {
			ToastUtil.shortToast(getApplicationContext(), "请输入正确的手机号码");
			return;
		}
		if(hai_name_huo.getText().toString().equals("") 
				|| hai_faname.getText().toString().equals("") || hai_chufa.getText().toString().equals("")
				|| hai_name.getText().toString().equals("") || hai_daoda.getText().toString().equals("")
				|| hai_qian.getText().toString().equals("") || hai_name_huo.getText().toString().equals("")
				){
			ToastUtil.shortToast(getApplicationContext(), "请填写完资料");
			return;
		}
		if ( hai_zhong.getText().toString().equals("") && hai_tiji.getText().toString().equals("")) {
			ToastUtil.shortToast(getApplicationContext(), "请填货物的重量货或件数");
			return;
		}
		if (mInput.getText().toString().equals("")) {
			ToastUtil.shortToast(getApplicationContext(), "请上传汽车牌号");
			return;
		}
		try {
			obj.put("userId",
					String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
			obj.put("latitude", latitude);
			obj.put("longitude", longitude);
			obj.put("startPlaceCityCode", cityCode);
			obj.put("cargoName", hai_name_huo.getText().toString());//货物名称  lo_name_huo.getText().toString()
			obj.put("startPlace", hai_chufa.getText().toString()+hai_address_specific.getText().toString());//货物起始地点  lo_chufa.getText().toString()
			obj.put("entPlace",  hai_daoda.getText().toString()+hai_add_address_specific.getText().toString());//货物到达地点   lo_daoda.getText().toString()
			obj.put("entPlaceCityCode", receiver_citycode);//
			if(weith==true){
				obj.put("cargoWeight", hai_zhong.getText().toString()+"千克");//货物	
			}else {
				obj.put("cargoWeight", hai_zhong.getText().toString()+"吨");//货物重量  editText1.getText().toString()
			}
			obj.put("cargoVolume", hai_tiji.getText().toString());//  editText2.getText().toString()
			obj.put("latitudeTo", mylatitude);//mylatitude
			obj.put("longitudeTo", mylongitude);//mylatitude
			obj.put("entPlaceCityCode", receiver_citycoded);
			obj.put("sendName", hai_faname.getText().toString());//收货人姓名  lo_name.getText().toString()
			obj.put("sendMobile", hai_faphone.getText().toString());//收货电话  lo_phone.getText().toString()
			obj.put("takeName", hai_name.getText().toString());//发货人姓名  lo_name.getText().toString()
			obj.put("takeMobile", hai_phone.getText().toString());//发货电话  lo_phone.getText().toString()
//			obj.put("sendName", hai_name.getText().toString());//发货人姓名  lo_name.getText().toString()
//			obj.put("sendMobile", hai_phone.getText().toString());//发货电话  lo_phone.getText().toString()
			obj.put("category", ""+2);
			obj.put("insurance", ""+1);
//			obj.put("category", leibie+1);
//			if (leibie==0) {
//				obj.put("insurance", bao+1);
////				Log.e("111111", ""+leibie);
//			}else {
//				obj.put("insurance", 1);
////				Log.e("1111112", ""+leibie);
//			}
			obj.put("cargoCost", hai_qian.getText().toString());
//			obj.put("carNumImg", icon);
			obj.put("carNumImg", mInput.getText().toString());
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		dialog.show();
		Log.e("查看数据", obj.toString());
		AsyncHttpUtils.doPostJson(HaiLogisticalActivity.this, MCUrl.PublishHn, obj.toString(),
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("11111111111 wwww   ", new String(arg2));
						dialog.dismiss();
						Logisbean	  = new Gson().fromJson(new String(arg2), LogisBean.class);
//						Log.e("oppop", bean.data.toString());
						if(Logisbean.getErrCode()==0){
							Intent intent = new Intent(HaiLogisticalActivity.this, HaiLogisPayActivity.class);
					 	    intent.putExtra("evaluationStatus","0");//余额支付 的价格	
					 		intent.putExtra("insureCost", Logisbean.data.get(0).insureCost);
					 		intent.putExtra("evaluationScore", "0");//其它支付的价格
					 		intent.putExtra("billCode", Logisbean.getData().get(0).billCode);//物流单号\
					 		intent.putExtra("recId", Logisbean.getData().get(0).recId);//物流单号
					 		intent.putExtra("way", "1");
					 		startActivityForResult(intent, 10);
//						 		showReplaceMoney();
						}
						else {
							ToastUtil.shortToast(getApplicationContext(), Logisbean.getMessage());
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						dialog.dismiss();
					}
				});

	}

	/**
	 * 头像popupWindow
	 */
	private void showPopwindow() {
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.pop_window_lauoutitem, null);
		// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
		final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(R.color.transparent);
		window.setBackgroundDrawable(dw);
		window.setOutsideTouchable(false);// 这是点击外部不消失
		// 设置popWindow的显示和消失动画
		window.setAnimationStyle(R.style.mypopwindow_anim_style);
		// 在底部显示
		window.showAtLocation(HaiLogisticalActivity.this.findViewById(R.id.hai_submit), Gravity.BOTTOM, 0, 0);

		// 这里检验popWindow里的button是否可以点击
		TextView tv_camera = (TextView) view.findViewById(R.id.tv_camera);
		TextView tv_show = (TextView) view.findViewById(R.id.tv_show);
		tv_show.setVisibility(View.GONE);
		tv_show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(HaiLogisticalActivity.this, IdCardActivity.class));
				window.dismiss();
			}
		});
		tv_camera.setClickable(true);
		// 点击是拍照
		tv_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				window.dismiss();
				Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent2.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "haicard.png")));
				// intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivityForResult(intent2, 2);// 采用ForResult打开
			}
		});
		// 从相册弄
		TextView tv_photofromalbum = (TextView) view.findViewById(R.id.tv_photofromalbum);
		tv_photofromalbum.setClickable(true);
		tv_photofromalbum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				window.dismiss();
				Intent intent1 = new Intent(Intent.ACTION_PICK, null);
				intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				// intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivityForResult(intent1, 1);
			}
		});
		TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
		tv_cancel.setClickable(true);
		tv_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				window.dismiss();
			}
		});
		// popWindow消失监听方法
		window.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {

			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		// 如果是直接从相册获取
		case 1:
			if (resultCode == RESULT_OK) {
				cropPhoto(data.getData());// 裁剪图片
			}
			break;
		// 如果是调用相机拍照时
		case 2:
			if (resultCode == RESULT_OK) {
				File temp = new File(Environment.getExternalStorageDirectory() + "/haicard.png");
				cropPhoto(Uri.fromFile(temp));// 裁剪图片
			}
			break;

		// 取得裁剪后的图片
		case 3:
			if (data != null) {
				Bundle extras = data.getExtras();
				head = extras.getParcelable("data");
				if (head != null) {
					/**
					 * 上传服务器代码 //TODO 实现头衔上传
					 */
					setPicToView(head);
					map.put("userId", PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID) + "");
					map.put("fileName", "海南专属");
					map_file.put("file", new File(fileName));
					sendEmptyBackgroundMessage(MsgConstants.MSG_01);
					img_headportrait.setBackgroundDrawable(null);// 将原背景图片置空，避免与新图片叠加显示，尤其是在上面图层圆角或者有透明度时；
					img_headportrait.setImageBitmap(head);
				}
			}
			break;
		case 7:
			if (resultCode == RESULT_OK) {
				if (data!=null) {
//				latitude = data.getDoubleExtra("latitude", 0);
//				longitude = data.getDoubleExtra("longitude", 0);
//				city = data.getStringExtra("city");
//				getCityCode();
					Log.e("11111hhhh", ""+data.getStringExtra("address"));
				hai_chufa.setText(data.getStringExtra("address"));
//				hai_chufa.setText(data.getStringExtra("address").replace("中国", ""));
//				Logger.e("111111     "+data.getStringExtra("address").replace("中国", ""));
//				ToastUtil.shortToast(getApplicationContext(), "111111");
			}
			}
			break;
		case 8:
			if (resultCode == RESULT_OK) {
				if (data!=null) {
//				mylatitude = data.getDoubleExtra("latitude", 0);
//				mylongitude = data.getDoubleExtra("longitude", 0);
//				city = data.getStringExtra("city");
//				receive = true;
//				getCityCode();
				hai_daoda.setText(data.getStringExtra("address"));
			}
				}
			break;
			case 10:
				if (resultCode == RESULT_OK) {
					if (data!=null) {
						if (data.getStringExtra("type").equals("1")) {
							finish();	
						}
					}
					}
				
				break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	};
	private void getCityCode() {
		boolean isCopySuccess = CheckDbUtils.checkDb();
		// 成功的将数据库copy到data 中
		if (isCopySuccess) {
			iWantApplication.getInstance().mDbManager = new DbManager(iWantApplication.getInstance());
		}
		if (city == null || city.equals("")) {
//			ToastUtil.shortToast(getApplicationContext(), "请输入完整信息");
			return;
		}
		Log.e("city", city);
		if (!city.contains("市")) {
			city = city + "市";
		}
		List<CityBean> selectDataFromDb = new CityDbOperation()
				.selectDataFromDb("select * from city where city_name='" + city + "'");
		if (receive) {
			receiver_citycoded = selectDataFromDb.get(0).city_code;
//			Log.e("citycode", receiver_citycode);
//			ToastUtil.shortToast(HaiLogisticalActivity.this, "city_code"+receiver_citycoded);

		} else {
			cityCode = selectDataFromDb.get(0).city_code;
			Log.e("citycodess", cityCode);
		}
	}
	private void getCityCodes() {
		boolean isCopySuccess = CheckDbUtils.checkDb();
		// 成功的将数据库copy到data 中
		if (isCopySuccess) {
			iWantApplication.getInstance().mDbManager = new DbManager(iWantApplication.getInstance());
		}
		if (city == null || city.equals("")) {
//			ToastUtil.shortToast(getApplicationContext(), "请输入完整信息");
			return;
		}
		Log.e("city", city);
		if (!city.contains("市")) {
			city = city + "市";
		}
		List<CityBean> selectDataFromDb = new CityDbOperation()
				.selectDataFromDb("select * from city where city_name='" + city + "'");
		if (receive) {
			receiver_citycoded = selectDataFromDb.get(0).city_code;
			Log.e("citycode", receiver_citycoded);

		} else {
			cityCode = selectDataFromDb.get(0).city_code;
			Log.e("citycode", cityCode);
		}
	}

	@Override
	public void handleBackgroundMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleBackgroundMessage(msg);
		switch (msg.what) {
		case MsgConstants.MSG_01:
			try {
				Log.e("logurl", MCUrl.IDCDHEAD);
				result = post(MCUrl.HPublishHn, map, map_file);
				sendEmptyUiMessage(MsgConstants.MSG_01);
			} catch (IOException e) {
				e.printStackTrace();
			}
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
			Log.e("result", result);
			bean = new Gson().fromJson(result, IconBean.class);
			if (bean.getErrCode()==0) {
			if (bean.data.size() != 0) {
				frist = true;
				icon = bean.data.get(0).filePath;
				Log.e("filePath", bean.data.get(0).filePath);
				setPicToView(head);
				PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.ICON_PATH, icon);
//				img_headportrait.setBackgroundDrawable(null);// 将原背景图片置空，避免与新图片叠加显示，尤其是在上面图层圆角或者有透明度时；
//				img_headportrait.setImageBitmap(head);
			}
			}
			break;

		default:
			break;
		}
	}

	/***
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void cropPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1.5);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 350);
		intent.putExtra("outputY", 350);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
		
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param head2
	 */

	private void setPicToView(Bitmap mBitmap) {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
			return;
		}
		FileOutputStream b = null;
		File file = new File(path);
		file.mkdirs();// 创建文件夹
		try {
			b = new FileOutputStream(fileName);
			mBitmap.compress(Bitmap.CompressFormat.PNG, 100, b);// 把数据写入文件
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭流
				b.flush();
				b.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
	 * 
	 * @param actionUrl
	 *            访问的服务器URL
	 * @param params
	 *            普通参数
	 * @param files
	 *            文件参数
	 * @return
	 * @return
	 * @throws IOException
	 */
	public static String post(String actionUrl, Map<String, String> params, Map<String, File> files)
			throws IOException {

		StringBuilder sb2 = new StringBuilder();

		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";

		URL uri = new URL(actionUrl);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		conn.setReadTimeout(5 * 1000); // 缓存的最长时间
		conn.setDoInput(true);// 允许输入
		conn.setDoOutput(true);// 允许输出
		conn.setUseCaches(false); // 不允许使用缓存
		conn.setRequestMethod("POST");
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", "UTF-8");
		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);

		// 首先组拼文本类型的参数
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);
			sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
			sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
			sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
			sb.append(LINEND);
			sb.append(entry.getValue());
			sb.append(LINEND);
		}

		DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
		outStream.write(sb.toString().getBytes());
		InputStream in = null;
		// 发送文件数据
		if (files != null) {
			for (Map.Entry<String, File> file : files.entrySet()) {
				StringBuilder sb1 = new StringBuilder();
				sb1.append(PREFIX);
				sb1.append(BOUNDARY);
				sb1.append(LINEND);
				// name是post中传参的键 filename是文件的名称
				sb1.append(
						"Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getKey() + "\"" + LINEND);
				sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
				sb1.append(LINEND);
				outStream.write(sb1.toString().getBytes());
				InputStream is = new FileInputStream(file.getValue());
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}
				is.close();
				outStream.write(LINEND.getBytes());
			}

			// 请求结束标志
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
			outStream.write(end_data);
			outStream.flush();
			// 得到响应码
			int res = conn.getResponseCode();
			if (res == 200) {
				in = conn.getInputStream();
				int ch;

				while ((ch = in.read()) != -1) {
					sb2.append((char) ch);
				}
			}
			outStream.close();
			conn.disconnect();
		}
		return sb2.toString();
	}

	/* 显示Dialog的method */
	private void showDialog(String mess) {
	}

	public InputStream getStream(File file) {
		// 第2步、通过子类实例化父类对象
		// File f= new File("d:" + File.separator + "test.txt") ; // 声明File对象
		// 第2步、通过子类实例化父类对象
		InputStream input = null; // 准备好一个输入的对象
		try {
			input = new FileInputStream(file); // 通过对象多态性，进行实例化
			// 第3步、进行读操作
			// byte b[] = new byte[input..available()] ; 跟使用下面的代码是一样的
			byte b[] = new byte[(int) file.length()]; // 数组大小由文件决定
			int len = input.read(b); // 读取内容
			// 第4步、关闭输出流

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return input;
	}

	private boolean fileIsExist(String path) {
		File file = new File(path);
		if (file.exists()) {
			return true;
		}
		return false;
	}

	private void fileDelete(String path) {

		if (fileIsExist(path)) {
			File file = new File(path);
			file.delete();
		}
	}

	// 点击空白区域关闭输入法
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(HaiLogisticalActivity.this.getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
		return super.onTouchEvent(event);
	}

	public Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}
	/**
	 * 显示popowind
	 */
	private void showReplaceMoney() {
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.haipop_windows, null);
		// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
		final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT);
		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(android.R.color.white);
		window.setBackgroundDrawable(dw); 
		window.setOutsideTouchable(false);// 这是点击外部不消失
		// 设置popWindow的显示和消失动画
		window.setAnimationStyle(R.style.mypopwindow_anim_style);
		// 在底部显示
		window.showAtLocation(HaiLogisticalActivity.this.findViewById(R.id.hai_submit), Gravity.BOTTOM, 0, 0);

		// 这里检验popWindow里的button是否可以点击
		Button tv_show = (Button) view.findViewById(R.id.btnsaves_pan);
		TextView textView3= (TextView) view.findViewById(R.id.textView3);
		TextView tet_tishi= (TextView) view.findViewById(R.id.tet_tishi);
		// 1快递员 2 镖师 3 物流个人 4 物流公司
		tet_tishi.setText("保费最低"+Logisbean.getData().get(0).remark+"元");
		tv_show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				getReplace();
				Intent intent = new Intent(HaiLogisticalActivity.this, HaiLogisPayActivity.class);
			 	    intent.putExtra("evaluationStatus","0");//余额支付 的价格	
			 		intent.putExtra("insureCost", Logisbean.data.get(0).insureCost);
			 		intent.putExtra("evaluationScore", "0");//其它支付的价格
			 		intent.putExtra("billCode", Logisbean.getData().get(0).billCode);//物流单号\
			 		intent.putExtra("recId", Logisbean.getData().get(0).recId);//物流单号
			 		intent.putExtra("way", "1");
			 		startActivityForResult(intent, 10);
			 		window.dismiss();
			}
		});
		// popWindow消失监听方法
		window.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {

			}
		});

	}
}
