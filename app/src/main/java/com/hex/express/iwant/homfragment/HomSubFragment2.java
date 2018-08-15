package com.hex.express.iwant.homfragment;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.framework.base.BaseFragment;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.activities.AddressActivity;
import com.hex.express.iwant.activities.AddressReceiveActivity;
import com.hex.express.iwant.activities.DownWindPayActivity;
import com.hex.express.iwant.activities.H5ModelActivity;
import com.hex.express.iwant.activities.HWdvertActivity;
import com.hex.express.iwant.activities.IdCardActivity;
import com.hex.express.iwant.activities.PostDownWindTaskActivity;
import com.hex.express.iwant.adapters.DownwindEscortAdapter;
import com.hex.express.iwant.bean.AreaBean;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.CityBean;
import com.hex.express.iwant.bean.DowInsuBeanse;
import com.hex.express.iwant.bean.DowInsuraBean;
import com.hex.express.iwant.bean.DownSpecialBean;
import com.hex.express.iwant.bean.IconBean;
import com.hex.express.iwant.bean.RegisterBean;
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
import com.hex.express.iwant.utils.DataTools;
import com.hex.express.iwant.utils.Logger;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.StringUtil;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.hex.express.iwant.views.RoundCornerImageView;
import com.hex.express.iwant.views.TimePickerView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.PopupWindow.OnDismissListener;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.a;

public class HomSubFragment2 extends BaseFragment {
	// 寄件人信息
		@Bind(R.id.iv_add)
		ImageView iv_add;
		@Bind(R.id.et_name)
		EditText et_name;
		@Bind(R.id.et_tel)
		EditText et_tel;
		@Bind(R.id.et_address)
		EditText et_address;
		@Bind(R.id.et_address_specific)// 发件人具体地址
		EditText et_address_specific;
		@Bind(R.id.tv_modifySenderAddress)
		TextView tv_modifySenderAddress;// 修改寄件人地址 
		@Bind(R.id.tv_modifyRecieverAddress)
		TextView tv_modifyRecieverAddress;// 修改收件人地址
		// 收件人信息
		@Bind(R.id.iv_add_receiver)
		ImageView iv_add_receiver;
		@Bind(R.id.et_add_name)
		EditText et_add_name;
		@Bind(R.id.et_add_tel)
		EditText et_add_tel;
		@Bind(R.id.et_add_address)
		EditText et_add_address;
		@Bind(R.id.et_add_address_specific)//收件人具体地址
		EditText et_add_address_specific;
		
		// 物品信息
		@Bind(R.id.et_goodname)
		EditText et_goodname;
		@Bind(R.id.et_weight)
		EditText et_weight;
		@Bind(R.id.et_good_detail)
		EditText et_good_detail;
		@Bind(R.id.et_ramk)
		EditText et_ramk;
		
		// 物品长宽高
		@Bind(R.id.et_chang)
		EditText et_chang;
		@Bind(R.id.et_kuan)
		EditText et_kuan;
		@Bind(R.id.et_height)
		EditText et_height;
		//广告
		@Bind(R.id.tv_tip)
		TextView tv_tip;
		@Bind(R.id.texs)
		TextView tex;
		@Bind(R.id.toubaoxuzhi)
		TextView toubaoxuzhi;
		
		@Bind(R.id.rl_bannerTip)
		RelativeLayout rl_bannerTip;
		
		// 拍照
		@Bind(R.id.img_headportrait)
		RoundCornerImageView img_headportrait;
		//是否买保险
		@Bind(R.id.checkbox_bao)
		CheckBox checkbox_bao;
		//是否代收款
		@Bind(R.id.checkbox_diashou)
		CheckBox checkbox_diashou;
		@Bind(R.id.edt_daipric)
		EditText edt_daipric;
		@Bind(R.id.et_updata)//取货时间
		EditText et_updata;		
		
		//是否需要大型货车
		@Bind(R.id.checkbox_detail)
		CheckBox checkbox_detail;
		@Bind(R.id.lout10)
		LinearLayout lout10;
		@Bind(R.id.edt_price)
		EditText edt_price;
		@Bind(R.id.v_pp)
		View v_pp;
		
		
		PopupWindow window02;
		PopupWindow window03;
		private Bitmap imghead;
		private String fileName = path + "imghead.png";
		private static String path = "/sdcard/myHead/";// sd路径
		private Map map = new HashMap<String, String>();
		private Map map_file = new HashMap<String, File>();
		private String result;
		// 当前位置经纬度
		private double latitude;
		private double longitude;
		private String city;
		// 当前位置经纬度
		private double latitude2;
		private double longitude2;
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
		private boolean flag = false;// false表示没有上传物品图片，true相反
		private boolean chox= false; 
		private boolean chox2= false; 
		private boolean choxdel; 
		private boolean ding=false; 
		private boolean frist = false;// 是否第一次获取位置成功
		private String tou,return_address;
		DowInsuraBean	adbean;
		private String	townCode,townaddressd;
		private String cityCode2,townCode2,townaddressd2;
		@Bind(R.id.hlast_step_rayout)
		LinearLayout hlast_step_rayout;//第一个界面
	    @Bind(R.id.hlast_steps2)
	    Button hlast_steps2;//上一步er
	    @Bind(R.id.hlast_stepse2)
	    Button hlast_stepse2;//上一步san
	    
	    @Bind(R.id.hnext_step_rayout)
	    LinearLayout hnext_step_rayout;//第二个界面
	    @Bind(R.id.hnext_steps2)
	    Button hnext_steps2;//下一步
	    @Bind(R.id.zuihourayout)
	    LinearLayout end_rayout;//第三个界面
	    
	    @Bind(R.id.hlast_step2)
	     Button hlast_step2;//上一步
	    @Bind(R.id.hnext_step2)
	     Button hnext_step2;//下一步
	    
	    @Bind(R.id.hsuccess2)
	     Button hsuccess;//  完成
	    @Bind(R.id.hom2_togg)
	    ToggleButton hom2_togg;
//	    private Handler handler;
	    View view;
	    Thread imageViewHander;
	    private static final int    RESULT_OK=-1;
	    @Bind(R.id.che)
	    LinearLayout che;//
	    @Bind(R.id.xiaomian)
	    LinearLayout xiaomian;//
	    @Bind(R.id.zhongmian)
	    LinearLayout zhongmian;//
	    @Bind(R.id.xiaohuo)
	    LinearLayout xiaohuo;//
	    @Bind(R.id.zhonghuo)
	    LinearLayout zhonghuo;//
	    @Bind(R.id.xiaomian1)
	    ImageView xiaomian1;
	    @Bind(R.id.zhongmian1)
	    ImageView zhongmian1;
	    @Bind(R.id.xiaohuo1)
	    ImageView xiaohuo1;
	    @Bind(R.id.zhonghuo1)
	    ImageView zhonghuo1;
	    
	    @Bind(R.id.lout99)
	    LinearLayout lout99;//三种车型
	    @Bind(R.id.lout999)
	    RelativeLayout lout999;//四种车型
	    @Bind(R.id.img_san)
	    ImageView img_san;
	    @Bind(R.id.img_xiaomian)
	    ImageView img_xiaomian;
	    @Bind(R.id.img_xiaohuo)
	    ImageView img_xiaohuo;
	    
	    @Bind(R.id.img_xiaomian1)
	    ImageView img_xiaomian1;
	    @Bind(R.id.img_zhongmian1)
	    ImageView img_zhongmian1;
	    @Bind(R.id.img_xiaohuo1)
	    ImageView img_xiaohuo1;
	    @Bind(R.id.img_zhonghuo1)
	    ImageView img_zhonghuo1;
		@Bind(R.id.et_carchang)
		Spinner et_carchang;
		@Bind(R.id.et_tijisp)
		Spinner et_tijisp;
		 @Bind(R.id.layout_iv_add)
		LinearLayout layout_iv_add;
	    @Bind(R.id.layout_iv_add_receiver)
		LinearLayout layout_iv_add_receiver;
	    private String cartype;
	    private boolean namesb=false; 
		private String namese,phonese,cityCode33;
		public LoadingProgressDialog dialog;
		int  weigts;
		DowInsuBeanse	sbeanse;
		int kes=0;
		int kes2=0;
		String kese;
		TimePickerView pvTime;
		DownSpecialBean bean ;
	    
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			if (view==null) {
				view=inflater.inflate(R.layout.fragment_newhom2, container, false);
			}
			 ViewGroup p = (ViewGroup) view.getParent(); 
	         if (p != null) { 
	             p.removeAllViewsInLayout(); 
	         } 
	         ButterKnife.bind(this,view);
	          imageViewHander = new Thread(new NetImageHandler());
	          dialog=new LoadingProgressDialog(getActivity());
	         initData();
	         setOnClick();
	         intview();
	         setimg();
	         // 时间选择器
	  		pvTime = new TimePickerView(getActivity(), TimePickerView.Type.MONTH_DAY_HOUR_MIN);
	  		// 控制时间范围
//	  		 Calendar calendar = Calendar.getInstance();
//	  		 pvTime.setRange(calendar.get(Calendar.YEAR) - 20,
//	  		 calendar.get(Calendar.YEAR));
	  		pvTime.setTime(new Date());
	  		pvTime.setCyclic(false);
	  		pvTime.setCancelable(true);
	  		// 时间选择后回调
	  		pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
	  			@Override
	  			public void onTimeSelect(Date date) {
	  				   Date now = new Date(); 
	  				   SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm ");//日期格式
	  				   String hehe = dateFormat.format(now); 
	  				   SimpleDateFormat dfs = new SimpleDateFormat("MM-dd HH:mm ");     
	  				   Date begin = null;
	  				   Date end = null ;
	  				try {
	  					begin = dfs.parse(hehe);
	  			        end = dfs.parse(getTimes(date));
	  				} catch (ParseException e) {
	  					// TODO Auto-generated catch block
	  					e.printStackTrace();
	  				}     
	  				Log.e("111shijian", ""+getTimes(date));
	  				  long between=(end.getTime()-begin.getTime())/1000;//除以1000是为了转换成秒   
	  				  
	  				  if (between/(60*60*24)>=0) {
	  					et_updata.setText(getTimes(date));
	 				}else {
	 					ToastUtil.shortToast(getActivity(), "请选择正确的到达日期");
	 				}
	  				 
	  				
	  			}
	  		});
			return view;
		}
		@Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			if (isLogin()) {
//				 getrequstBalance();
				 }else {
					 et_name.setText("");
					 et_tel.setText("");
				}
		}
		private void initData(){
			et_add_address.setFocusable(false);
			et_updata.setFocusable(false);
				try {
					if (isLogin()) {	
						 getrequstBalance();
			}
				} catch (Exception e) {
					// TODO: handle exception
				}
		
			img_headportrait.setBackgroundResource(R.drawable.camre_img);
			
			client = new LocationClient(getActivity());
			initLocation();
			client.registerLocationListener(new BDLocationListener() {

				@Override
				public void onReceiveLocation(BDLocation arg0) {
					if (arg0 == null) {
						ToastUtil.shortToast(getActivity(), "定位失败，请检查设置");
						return;
					} else {
					latitude = arg0.getLatitude();
					longitude = arg0.getLongitude();
					townaddressd=arg0.getDistrict();
					Log.e("jpppp", latitude + ":::::::::" + longitude);
					if (frist) {
						et_address.setText(return_address);	
					}else {
						et_address.setText(""+arg0.getAddrStr());
					}
					getCityCode(false);
					}
				}
			});
			// 初始化定位
			// 打开GPS
			client.start();
		}
		private void intview(){
			hnext_step2.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (isLogin()) {
//						if (!"".equals(et_weight.getText().toString())
//								&& !"".equals(et_add_address.getText().toString())) {
					hlast_step_rayout.setVisibility(View.GONE);
					hnext_step_rayout.setVisibility(View.VISIBLE);
					end_rayout.setVisibility(View.GONE);
					
					hlast_step2.setVisibility(View.GONE);
					hlast_steps2.setVisibility(View.VISIBLE);
					hnext_step2.setVisibility(View.GONE);
					hnext_steps2.setVisibility(View.VISIBLE);
					hsuccess.setVisibility(View.GONE);
//					weitght();
//						}else {
//							ToastUtil.shortToast(getActivity(), "请输入完整信息");
//						}
					}else {
						ToastUtil.shortToast(getActivity(), "请登录后发布");
					}
				}
			});
			hnext_steps2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
//					if (weigts==2) {
//						if (!"".equals(sbeanse.getErrCode()) && sbeanse.getErrCode()==0) {
//							showcar();
//						}else {
//							ToastUtil.shortToast(getActivity(), "请检查地址信息");
//						}
//					}else {
						addPostResult();
//						hlast_step_rayout.setVisibility(View.GONE);
//						hnext_step_rayout.setVisibility(View.GONE);
//						end_rayout.setVisibility(View.VISIBLE);
//						hlast_step2.setVisibility(View.VISIBLE);
//						hlast_steps2.setVisibility(View.GONE);
//						hnext_step2.setVisibility(View.GONE);
//						hnext_steps2.setVisibility(View.GONE);
//						hsuccess.setVisibility(View.VISIBLE);
//					}
				
				}
			});
			hlast_step2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					hlast_step_rayout.setVisibility(View.GONE);
					hnext_step_rayout.setVisibility(View.VISIBLE);
					end_rayout.setVisibility(View.GONE);
					
					hlast_step2.setVisibility(View.GONE);
					hlast_steps2.setVisibility(View.VISIBLE);
					hnext_step2.setVisibility(View.GONE);
					hnext_steps2.setVisibility(View.VISIBLE);
					hsuccess.setVisibility(View.GONE);
				}
			});
			hlast_steps2.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					hlast_step_rayout.setVisibility(View.VISIBLE);
					hnext_step_rayout.setVisibility(View.GONE);
					end_rayout.setVisibility(View.GONE);
					
					hlast_step2.setVisibility(View.GONE);
					hlast_steps2.setVisibility(View.GONE);
					hnext_step2.setVisibility(View.VISIBLE);
					hnext_steps2.setVisibility(View.GONE);
					hsuccess.setVisibility(View.GONE);
				}
			});
			edt_price.setEnabled(false);
			edt_daipric.setEnabled(false);
			checkbox_bao.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					// TODO Auto-generated method stub
				if(arg1){
					chox=arg1;
//					edt_price.setVisibility(View.VISIBLE);
//				   	v_pp.setVisibility(View.VISIBLE);
				   			showPaywindow();
				   			edt_price.setEnabled(arg1);
//				}
				}else {
					chox=arg1;
//					edt_price.setVisibility(View.GONE);
//				   	v_pp.setVisibility(View.GONE);
					edt_price.setEnabled(arg1);
					edt_price.setText("");
				}
				}
			});
			
			checkbox_diashou.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					// TODO Auto-generated method stub
				if(arg1){
					chox2=arg1;
//					edt_price.setVisibility(View.VISIBLE);
//				   	v_pp.setVisibility(View.VISIBLE);
					showPaywindowdai();
					edt_daipric.setEnabled(arg1);
//				}
				}else {
					chox2=arg1;
//					edt_price.setVisibility(View.GONE);
//				   	v_pp.setVisibility(View.GONE);
					edt_daipric.setEnabled(arg1);
					edt_daipric.setText("");
				}
				}
			});
			hom2_togg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	 			@Override
	 			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	 				// TODO Auto-generated method stub
	 				if (isChecked) {  
	 					choxdel=true;
	 					che.setVisibility(View.VISIBLE);
	 				} else {  
	 					choxdel=false;
	 					che.setVisibility(View.GONE);
	                 }  
	 			}
	 		});
			toubaoxuzhi.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent=new Intent();
					intent.putExtra("name", "1");
					intent.putExtra("url", "http://www.efamax.com/mobile/InsureSF.html");
					intent.setClass(getActivity(), HWdvertActivity.class);
					startActivity(intent);
				}
			});
//			  edt_daipric.addTextChangedListener(new TextWatcher() {
//					
//		        	 public void onTextChanged(CharSequence s, int start, int before, int count) {  
//		                 if (edt_daipric.toString().equals("0") || s.toString().startsWith("0")  
//		                     || edt_daipric.toString().equals("") || s.toString().trim().length() == 0) {  
//		                	 edt_daipric.setText("1");  
//		                 }  
//		             }  
//					
//					@Override
//					public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//						// TODO Auto-generated method stub
//						
//					}
//					
//					@Override
//					public void afterTextChanged(Editable arg0) {
//						// TODO Auto-generated method stub
//						
//					}
//				});
			  edt_price.addTextChangedListener(new TextWatcher() {
		        	 /** 
		     	     * 编辑框的内容发生改变之前的回调方法 
		     	     */
		     	 public void onTextChanged(CharSequence s, int start, int before, int count) {  
		     		temp=s;
		     		 
		          }  
		     	 /** 
		     	     * 编辑框的内容正在发生改变时的回调方法 >>用户正在输入 
		     	     * 我们可以在这里实时地 通过搜索匹配用户的输入 
		     	     */  
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int afte) {
						// TODO Auto-generated method stub
						
					}
					/** 
				     * 编辑框的内容改变以后,用户没有继续输入时 的回调方法 
				     */
					@Override
					public void afterTextChanged(Editable editable) {
						// TODO Auto-generated method stub
						editStart = edt_price.getSelectionStart(); 
						   editEnd = edt_price.getSelectionEnd(); 
						if (temp.length()>4) {
//							ToastUtil.shortToast(getActivity(), "保额不能超过5000");
							showwind();
							editable.delete(editStart-1, editEnd); 
							    int tempSelection = editStart; 
							    edt_price.setText(editable); 
							    edt_price.setSelection(tempSelection);
						}
					}
				});
			  xiaomian.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						cartype="smallMinibus";
						xiaomian1.setBackgroundResource(R.drawable.cheoff);
						zhongmian1.setBackgroundResource(R.drawable.cheon);
						xiaohuo1.setBackgroundResource(R.drawable.cheon);
						zhonghuo1.setBackgroundResource(R.drawable.cheon);
					}
				});
		         zhongmian.setOnClickListener(new OnClickListener() {
		 			
		 			@Override
		 			public void onClick(View arg0) {
		 				// TODO Auto-generated method stub
		 				cartype="middleMinibus";
		 				xiaomian1.setBackgroundResource(R.drawable.cheon);
						zhongmian1.setBackgroundResource(R.drawable.cheoff);
						xiaohuo1.setBackgroundResource(R.drawable.cheon);
						zhonghuo1.setBackgroundResource(R.drawable.cheon);
		 			}
		 		});
		         xiaohuo.setOnClickListener(new OnClickListener() {
		 			
		 			@Override
		 			public void onClick(View arg0) {
		 				// TODO Auto-generated method stub
		 				cartype="smallTruck";
		 				xiaomian1.setBackgroundResource(R.drawable.cheon);
						zhongmian1.setBackgroundResource(R.drawable.cheon);
						xiaohuo1.setBackgroundResource(R.drawable.cheoff);
						zhonghuo1.setBackgroundResource(R.drawable.cheon);
		 			}
		 		});
		         zhonghuo.setOnClickListener(new OnClickListener() {
		 			
		 			@Override
		 			public void onClick(View arg0) {
		 				// TODO Auto-generated method stub
		 				cartype="middleTruck";
		 				xiaomian1.setBackgroundResource(R.drawable.cheon);
						zhongmian1.setBackgroundResource(R.drawable.cheon);
						xiaohuo1.setBackgroundResource(R.drawable.cheon);
						zhonghuo1.setBackgroundResource(R.drawable.cheoff);
		 			}
		 		});
		     	final String[] myItems = getResources().getStringArray(
						R.array.carchang);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
						android.R.layout.simple_spinner_item, myItems);
				adapter.setDropDownViewResource(R.layout.spier_item); 
				et_carchang.setAdapter(adapter);  
				et_carchang.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						kes=position;
//						Log.e("11111", "position"+sunmit(5, position));
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
				});
				final String[] myItems2 = getResources().getStringArray(
						R.array.tiji);
				final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),
						android.R.layout.simple_spinner_item, myItems2);
				adapter1.setDropDownViewResource(R.layout.spier_tiji); 
				et_tijisp.setAdapter(adapter1);  
				et_tijisp.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						kes2=position;
						kese=adapter1.getItem(position);
//						ToastUtil.shortToast(getActivity(), "position"+adapter1.getItem(position));
//						Log.e("11111", "position"+sunmit(5, position));
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
				});
				et_updata.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						pvTime.show();
					}
				});
		}
		public int sunmit(int a ,int b){
			int sum=a+b;
			return sum;
		}
		private void weitght(){
			String str=et_weight.getText().toString();
			int wt=0;
			try {
				wt = Integer.parseInt(str);
			} catch (NumberFormatException e) {
			    e.printStackTrace();
			}
			if (wt<450) {
				weigts=1;
				choxdel=false;
			}else {
				choxdel=true;
				weigts=2;
			}
//			if (wt<100) {
//				lout99.setVisibility(View.GONE);
//				lout999.setVisibility(View.GONE);
//			}
//			if (wt>100 && wt<500) {
//				lout99.setVisibility(View.VISIBLE);
//				lout999.setVisibility(View.GONE);
//			}
//			if (wt>500) {
//				lout99.setVisibility(View.GONE);
//				lout999.setVisibility(View.VISIBLE);
//			}
			request();
		}
		private void request(){
			
			 double fromLatitude;
			 double fromLongitude;
			 String stcityCode;
			if (ding) {
//				obj.put("cityCode", cityCode2);
//				obj.put("townCode", townCode2);
//				obj.put("fromLatitude", mylatitude);
//				obj.put("fromLongitude", mylongitude);
				stcityCode=cityCode2;
				fromLatitude=mylatitude;
				fromLongitude=mylongitude;
			}else {
				
				if ("".equals(cityCode)) {
					stcityCode=cityCode33;
				}else {
					stcityCode=cityCode;
				}
				if (latitude2==0.0) {
					fromLatitude=latitude;
//					obj.put("fromLatitude", latitude);
				}else {
					fromLatitude=latitude2;
//					obj.put("fromLatitude", latitude2);
				}
				if (longitude2==0.0) {
					fromLongitude=longitude;
//					obj.put("fromLongitude", longitude);
				}else {
					fromLongitude=longitude2;
//					obj.put("fromLongitude", longitude2);
				}
			}
			if (fromLatitude==0.0 || fromLongitude==0.0
					|| receiver_latitude==0.0 || receiver_longitude==0.0
				
					) {
				ToastUtil.shortToast(getActivity(), "请完善信息");
				return;
			}
			String url = UrlMap.getfsix(MCUrl.DOWNWINDTASKLIMpice, "userId",
					String.valueOf(PreferencesUtils.getInt(getActivity(), PreferenceConstants.UID)), "fromLatitude",
					""+fromLatitude, "fromLongitude", ""+fromLongitude,
					"toLatitude", ""+receiver_latitude,"toLongitude",""+receiver_longitude,"cityCode",stcityCode);
			dialog.show();
			Log.e("1111111ss", url);
			AsyncHttpUtils.doSimGet(url, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					
					dialog.dismiss();
					Log.e("pkdkoo", new String(arg2));
					sbeanse = new Gson().fromJson(new String(arg2), DowInsuBeanse.class);
//					ToastUtil.shortToast(getActivity(), ""+sbeanse.data.get(0).smallMinibusPrice);
					if (sbeanse.getErrCode()!=0) {
						ToastUtil.shortToast(getActivity(), sbeanse.getMessage());
					}
				}
				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
					dialog.dismiss();
				}
			});
			
		}
		//选择时间
				public static String getTimes(Date date) {
//					SimpleDateFormat format = new SimpleDateFormat("MM-dd ");
//					SimpleDateFormat formats = new SimpleDateFormat("yyyy");
//					return "2017-"+format.format(date);
//					return format.format(date);
					Date now = new Date(); 
					  SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm ");//日期格式
					   String hehe = dateFormat.format(date); 
					return dateFormat.format(date);
				}
		private void showcar(){
			RelativeLayout re_xiaomian,re_zhongmian,re_xiaohuo,re_zhonghuo;
			TextView hom_xiaomianmoney,hom_zhongmianmoney,hom_xioahuomoney,hom_zhonghuomoney;
			// 利用layoutInflater获得View
			LayoutInflater inflater = (LayoutInflater) getActivity(). getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.popwidow_car, null);
			// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
			window02 = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
					WindowManager.LayoutParams.MATCH_PARENT);
			// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
			window02.setFocusable(true);
			// 实例化一个ColorDrawable颜色为半透明
//			 ColorDrawable dw = new ColorDrawable(R.color.transparent01);
			ColorDrawable dw = new ColorDrawable(android.R.color.white);
			window02.setBackgroundDrawable(dw);
			window02.update();
			window02.setOutsideTouchable(false);// 这是点击外部不消失
			// 设置popWindow的显示和消失动画
			window02.setAnimationStyle(R.style.mypopwindow_anim_style);
			// 在底部显示
			window02.showAtLocation(HomSubFragment2.this.view.findViewById(R.id.checkbox_bao), Gravity.CENTER, 0, 0);
			re_xiaomian=(RelativeLayout) view.findViewById(R.id.re_xiaomian);
			re_zhongmian=(RelativeLayout) view.findViewById(R.id.re_zhongmian);
			re_xiaohuo=(RelativeLayout) view.findViewById(R.id.re_xiaohuo);
			re_zhonghuo=(RelativeLayout) view.findViewById(R.id.re_zhonghuo);
			
			hom_xiaomianmoney=(TextView) view.findViewById(R.id.hom_xiaomianmoney);
			hom_zhongmianmoney=(TextView) view.findViewById(R.id.hom_zhongmianmoney);
			hom_xioahuomoney=(TextView) view.findViewById(R.id.hom_xioahuomoney);
			hom_zhonghuomoney=(TextView) view.findViewById(R.id.hom_zhonghuomoney);
			if (sbeanse.getErrCode()==0) {
				DecimalFormat df = new DecimalFormat("######0.00");
			hom_xiaomianmoney.setText("镖费："+df.format(sbeanse.data.get(0).smallMinibusPrice) +"元");
			hom_zhongmianmoney.setText("镖费："+df.format(sbeanse.data.get(0).middleMinibusPrice)+"元");
			hom_xioahuomoney.setText("镖费："+df.format(sbeanse.data.get(0).smallTruckPrice)+"元");
			hom_zhonghuomoney.setText("镖费："+df.format(sbeanse.data.get(0).middleTruckPrice)+"元");
			
			}else {
				
			}
			re_xiaomian.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					cartype="smallMinibus";
					addPostResult();
					window02.dismiss();
				}
			});
			re_zhongmian.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					cartype="middleMinibus";
					addPostResult();
					window02.dismiss();
				}
			});
			re_xiaohuo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					cartype="smallTruck";
					addPostResult();
					window02.dismiss();
				}
			});
			re_zhonghuo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					cartype="middleTruck";
					addPostResult();
					window02.dismiss();
				}
			});
			
			// popWindow消失监听方法
			window02.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					window02.dismiss();
				}
			});

		}
		private void setimg(){
			img_san.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
//					img_san.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.xiaohuo));
					img_san.setBackgroundResource(R.drawable.xiaohuo);
				}
			});
			img_xiaomian.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			img_xiaohuo.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			img_xiaomian1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			img_zhongmian1.setOnClickListener(new OnClickListener() {
	
				@Override
				public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
					}
				});
			img_xiaohuo1.setOnClickListener(new OnClickListener() {
	
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
		
			}
		});
			img_zhonghuo1.setOnClickListener(new OnClickListener() {
	
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
		
	 		}
			});
		}
		/**
		 * 获取
		 */
		private void getrequstBalance() {
			RequestParams params = new RequestParams();
			AsyncHttpUtils.doGet(
					UrlMap.getUrl(MCUrl.BALANCE, "id",
							String.valueOf(PreferencesUtils.getInt(getActivity(), PreferenceConstants.UID))),
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
							
							PreferencesUtils.putString(getActivity(), PreferenceConstants.USERTYPE,
									bean.data.get(0).userType);
							PreferencesUtils.putString(getActivity(), PreferenceConstants.REALMANAUTH,
									bean.data.get(0).realManAuth);
							PreferencesUtils.putInt(getActivity(), PreferenceConstants.UID,
									bean.data.get(0).userId);
							 PreferencesUtils.putString(getActivity(), PreferenceConstants.AgreementType,bean.getData().get(0).agreementType);
							 PreferencesUtils.putString(getActivity(), PreferenceConstants.WLID, bean.getData().get(0).wlid);
//							handler.sendEmptyMessage(0);
							
							 if (namesb) {
								 et_name.setText(namese);
								 et_tel.setText(phonese);
							}else {
								 et_name.setText(bean.data.get(0).userName);
								 et_tel.setText(bean.data.get(0).mobile);
							}
							 latitude2=bean.data.get(0).latitude;
							 longitude2=bean.data.get(0).longitude;
							 cityCode33=bean.data.get(0).cityCode;
						}
							
						}

					});
		}
		 private CharSequence temp;
		 private int editStart ; 
		  private int editEnd ;
		public void setOnClick() {
			// TODO Auto-generated method stub
			AsyncHttpUtils.doSimGet(MCUrl.DowInsura, new AsyncHttpResponseHandler() {
				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
					
				}
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					Log.e("111111", new String(arg2));
						adbean = new Gson().fromJson(new String(arg2), DowInsuraBean.class);
					
					
				}
			});
		}
		
		@OnClick({ R.id.hsuccess2, R.id.img_headportrait, R.id.tv_modifySenderAddress, R.id.tv_modifyRecieverAddress, R.id.iv_add,
			R.id.iv_add_receiver,R.id.rl_bannerTip ,R.id.checkbox_bao,R.id.et_address,R.id.et_add_address,R.id.layout_iv_add,R.id.layout_iv_add_receiver})
	public void MyOnClick(View view) {
		switch (view.getId()) {
		case R.id.rl_bannerTip://广告点击去镖师认证
//			startActivity(new Intent(getActivity(), H5ModelActivity.class));
			break;
		case R.id.hsuccess2:
			addPostResult();
//			ceshi();
			break;
		case R.id.img_headportrait:// 添加物品照片
			showPopwindow();
			break;
		case R.id.iv_add:// 从手机通讯录选择发件人name,phone
//			Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//			HomSubFragment2.this.	startActivityForResult(intent, 0);
			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
			startActivityForResult(intent, 0);
			// startActivityForResult(new Intent(DownWindSpecialActivity.this,
			// SendPersonActivity.class), 4);
			break;
		case R.id.layout_iv_add:// 从手机通讯录选择发件人name,phone
//			Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//			HomSubFragment2.this.	startActivityForResult(intent, 0);
			Intent intent1 = new Intent(Intent.ACTION_PICK);
			intent1.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
			startActivityForResult(intent1, 0);
			// startActivityForResult(new Intent(DownWindSpecialActivity.this,
			// SendPersonActivity.class), 4);
			break;
		case R.id.iv_add_receiver:// 从手机通讯录选择收件人name,phone
//			Intent intent1 = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//			HomSubFragment2.this.	startActivityForResult(intent1, 9);
			Intent intent2 = new Intent(Intent.ACTION_PICK);
			intent2.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
			startActivityForResult(intent2, 9);
			// startActivityForResult(new Intent(DownWindSpecialActivity.this,
			// AddReceiverActivity.class), 5);
			break;
		case R.id.layout_iv_add_receiver:// 从手机通讯录选择收件人name,phone
//			Intent intent1 = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//			HomSubFragment2.this.	startActivityForResult(intent1, 9);
			Intent intent21 = new Intent(Intent.ACTION_PICK);
			intent21.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
			startActivityForResult(intent21, 9);
			// startActivityForResult(new Intent(DownWindSpecialActivity.this,
			// AddReceiverActivity.class), 5);
			break;
		case R.id.tv_modifySenderAddress:// 选择寄件人地址
//			HomSubFragment2.this.startActivityForResult(new Intent(getActivity(), AddressActivity.class), 7);
			HomSubFragment2.this.startActivityForResult(new Intent(getActivity(), LocationDemo.class), 7);
			break;
		case R.id.tv_modifyRecieverAddress:// 选择收件人地址
//			HomSubFragment2.this.startActivityForResult(new Intent(getActivity(), AddressReceiveActivity.class), 8);
			HomSubFragment2.this.startActivityForResult(new Intent(getActivity(), LocationDemo.class), 8);
			break;
		case R.id.et_address:// 选择寄件人地址
//			HomSubFragment2.this.startActivityForResult(new Intent(getActivity(), AddressActivity.class), 7);
			HomSubFragment2.this.startActivityForResult(new Intent(getActivity(), LocationDemo.class), 7);
			break;
		case R.id.et_add_address:// 选择收件人地址
//			HomSubFragment2.this.startActivityForResult(new Intent(getActivity(), AddressReceiveActivity.class), 8);
			HomSubFragment2.this.startActivityForResult(new Intent(getActivity(), LocationDemo.class), 8);
			break;
		default:
			break;
		}
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
					ContentResolver reContentResolverol = getActivity().getContentResolver();
					Uri contactData = data.getData();
					// 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
					Cursor cursor = getActivity().managedQuery(contactData, null, null, null, null);
					cursor.moveToFirst();
					if (cursor.moveToFirst()) {
						namesb=true;
					// 获得DATA表中的名字
					String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					// 条件为联系人ID
					et_name.setText(name);
					namese=name;
					 String[] contact = new String[2];
	                // 查看联系人有多少个号码，如果没有号码，返回0
					int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
					String[] strs = cursor.getColumnNames();
		            for (int i = 0; i < strs.length; i++) {
		                if (strs[i].equals("data1")) {
		                    ///手机号
		                    contact[1] = cursor.getString(cursor.getColumnIndex(strs[i])).replace("+86","").replaceAll(" ","").trim();
		                    et_tel.setText(""+contact[1]);
		                	phonese=contact[1];
		                }
		            }
//	              
//					String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//					// 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
//					Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//							ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
//					while (phone.moveToNext()) {
//						String phone_number = phone
//								.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//						if (!phone_number.equals("")){
//							et_tel.setText(phone_number);
//							phonese=phone_number;
//						}
//					}
					if(Build.VERSION.SDK_INT < 14) {
					    cursor.close();
					   }
//					phone.close();
					}else{  
		                // 没有权限，跳到设置界面，调用Android系统“应用程序信息（Application Info）”界面  
		                new AlertDialog.Builder(getActivity())  
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
			// 如果是直接从相册获取
			case 1:
				if (resultCode == RESULT_OK) {
					if (data != null) {
					cropPhoto(data.getData());// 裁剪图片
					}
				}
				break;
			// 如果是调用相机拍照时
			case 2:
				if (resultCode == RESULT_OK) {
					File temp = new File(Environment.getExternalStorageDirectory() + "/goodsphoto.png");
					cropPhoto(Uri.fromFile(temp));// 裁剪图片
				}
				break;

			// 取得裁剪后的图片
			case 3:
				if (data != null) {
					Bundle extras = data.getExtras();
					imghead = extras.getParcelable("data");
					if (imghead != null) {
						/**
						 * 上传服务器代码 //TODO 实现头衔上传
						 */
						setPicToView(imghead);
						map.put("userId",
								String.valueOf(PreferencesUtils.getInt(getActivity(), PreferenceConstants.UID)));
						map.put("fileName", "顺风物品");
						map_file.put("file", new File(fileName));
						imageViewHander.start();
//						sendEmptyBackgroundMessage(MsgConstants.MSG_01);
//						sendEmptyUiMessage(MsgConstants.MSG_02);
//						Message msg = new Message(); 
//			               msg.what = 1; 
//						 handler.sendMessage(msg); 
					}
				}
				break;
			case 4:
				if (data == null) {
					return;
				}
				if (resultCode == Activity.RESULT_OK) {
					et_name.setText(data.getStringExtra("name"));
					et_tel.setText(data.getStringExtra("phone"));
					et_address.setText(data.getStringExtra("address"));
					mylatitude = data.getDoubleExtra("latitude", 0);
					mylongitude = data.getDoubleExtra("longitude", 0);
					city = data.getStringExtra("city");
					getCityCode(true);
					Log.e("PPPPPPPPPPPPPPPPPPPP",
							data.getStringExtra("name") + data.getStringExtra("phone") + data.getStringExtra("address"));
				}
				break;

			case 5:
				if (data == null) {
					return;
				}
				if (resultCode == Activity.RESULT_OK) {
					// receiver_city = data.getStringExtra("city");
					receiver_latitude = data.getDoubleExtra("latitude", 0);
					receiver_longitude = data.getDoubleExtra("longitude", 0);
					Log.e("收件人", receiver_latitude + "++++++" + receiver_longitude);
					receiver_citycode = data.getStringExtra("citycode");
					et_add_name.setText(data.getStringExtra("name"));
					et_add_tel.setText(data.getStringExtra("phone"));
					et_add_address.setText(data.getStringExtra("location") + data.getStringExtra("address"));
				}

				break;
			case 7:
				if (data == null) {
					return;
				}
				if (resultCode == RESULT_OK) {
					mylatitude = data.getDoubleExtra("latitude", 0);
					mylongitude = data.getDoubleExtra("longitude", 0);
					city = data.getStringExtra("city");
					townaddressd2 =data.getStringExtra("townaddressd");
					receive = false;
					frist=true;
					getCityCode(true);
					return_address= data.getStringExtra("address").replace("中国", "");
					et_address.setText(data.getStringExtra("address").replace("中国", ""));
				}
				break;
			case 8:
				if (data == null) {
					return;
				}
				if (resultCode == RESULT_OK) {
					receiver_latitude = data.getDoubleExtra("latitude", 0);
					receiver_longitude = data.getDoubleExtra("longitude", 0);
					city = data.getStringExtra("city");
					receive = true;
					getCityCode(true);
					et_add_address.setText(data.getStringExtra("address").replace("中国", ""));
				}
				break;
			case 9:// 增加手机收件人联系人的回调
				if (data == null) {
					return;
				}
				if (resultCode == Activity.RESULT_OK) {
					ContentResolver reContentResolverol = getActivity().getContentResolver();
					Uri contactData = data.getData();
					// 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
					Cursor cursor = getActivity().managedQuery(contactData, null, null, null, null);
//					cursor.moveToFirst();
					if (cursor.moveToFirst()) {
					// 获得DATA表中的名字
					String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					// 条件为联系人ID
					et_add_name.setText(name);
					
//					String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//					// 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
//					Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//							ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
////				   if (phone==null) {et_add_tel
////					return;
////				   }
//					while (phone.moveToNext()) {
//						String phone_number = phone
//								.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//						if (!phone_number.equals("")){
//							et_add_tel.setText(phone_number);
//						}
//					}
					 String[] contact = new String[2];
		                // 查看联系人有多少个号码，如果没有号码，返回0
						int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
						String[] strs = cursor.getColumnNames();
			            for (int i = 0; i < strs.length; i++) {
			                if (strs[i].equals("data1")) {
			                    ///手机号
			                    contact[1] = cursor.getString(cursor.getColumnIndex(strs[i])).replace("+86","").replaceAll(" ","").trim();
			                    et_add_tel.setText(""+contact[1]);
			                }
			            }
					if(Build.VERSION.SDK_INT < 14) {
					    cursor.close();
					   }
//					phone.close();
				}else{  
	                // 没有权限，跳到设置界面，调用Android系统“应用程序信息（Application Info）”界面  et_add_tel.setText(phone_number);
	                new AlertDialog.Builder(getActivity())  
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
			case 10:
//				if (resultCode == RESULT_OK) {
//					finish();
//				}
				if (resultCode == RESULT_OK) {
					if (data!=null) {
						if (!"".equals(data.getStringExtra("type")) && !data.getStringExtra("type").equals("1") ) {
//							finish();	
							remo();
							getrequst(bean.data.get(0).recId);
						}
					}
					} 
//				remo();
				break;
			
			}
			}
		
		private void remo(){
			hlast_step_rayout.setVisibility(View.VISIBLE);
			hnext_step_rayout.setVisibility(View.GONE);
			end_rayout.setVisibility(View.GONE);
			
			hlast_step2.setVisibility(View.GONE);
			hlast_steps2.setVisibility(View.GONE);
			hnext_step2.setVisibility(View.VISIBLE);
			hnext_steps2.setVisibility(View.GONE);
			hsuccess.setVisibility(View.GONE);
			checkbox_bao.setChecked(false);
			checkbox_diashou.setChecked(false);
			img_headportrait.setBackgroundDrawable(null);// 切换图片前先置空
			che.setVisibility(View.GONE);
			cartype="else";
			chox=false;
			et_add_name.setText("");
			et_add_tel.setText("");
			et_add_address.setText("");
			et_add_address_specific.setText("");
			et_goodname.setText("");
			et_weight.setText("");
			et_ramk.setText("");
			img_headportrait.setBackgroundResource(R.drawable.camre_img);
			edt_price.setText("");
		}
//		private void ceshi(){
//			Intent intent = new Intent(getActivity(), DownWindPayActivity.class);
////			intent.putExtra("money", String.valueOf(df.format(bean.data.get(0).transferMoney)));
//			intent.putExtra("money", "11");
//			intent.putExtra("insureCost", "22");
//			intent.putExtra("billCode","33");
//			intent.putExtra("baofei", "");
//			intent.putExtra("distance", "123");
//			intent.putExtra("daishou","22");
//			intent.putExtra("tyy","顺风镖");
//			startActivityForResult(intent, 11);
//		}
		/**
		 * 数据请求
		 */
private void addPostResult() {
			String money = null;
			JSONObject obj = new JSONObject();
//			if (!flag) {
//				ToastUtil.shortToast(getActivity(), "请上传物品照片");
//				return;
//			}
			if (chox) {
				if (edt_price.getText().toString().equals("")) {
					ToastUtil.shortToast(getActivity(), "请填写投保费用");
					return;
				}
			}
			if (chox2) {
				if (edt_daipric.getText().toString().equals("")) {
					ToastUtil.shortToast(getActivity(), "请填写代收费用");
					return;
				}
				
			}
			String string =et_tel.getText().toString();
			String string2 =et_add_tel.getText().toString();
			  String tmpstr=string.replace(" ","");
			  String tmpstr2=string2.replace(" ","");
			if (!StringUtil.isMobileNO(tmpstr)
					|| (tmpstr.length() != 11)
							&& !StringUtil.isMobileNO(tmpstr2)
					|| (tmpstr2.length() != 11)) {
				ToastUtil.shortToast(getActivity(), "请输入正确的手机号码");
				return;
			}
			if (et_add_name.getText().toString().equals("") || et_add_tel.getText().toString().equals("")
					|| et_name.getText().toString().equals("") || et_tel.getText().toString().equals("")
					|| et_goodname.getText().toString().equals("") || et_weight.getText().toString().equals("")
					) {
				ToastUtil.shortToast(getActivity(), "请完善信息");
				return;
			}
			if (et_add_address.getText().toString().equals("") || et_address.getText().toString().equals("")
					 || receiver_citycode.equals("")) {
				ToastUtil.shortToast(getActivity(), "请核对你输入的地址信息");
				return;
			}
//			if (choxdel) {
//				if ( et_chang.getText().toString().equals("") || et_kuan.getText().toString().equals("")
//			    || et_height.getText().toString().equals("")) {
//					ToastUtil.shortToast(getActivity(), "请完善信息");
//					return;
//				}
//			}
//			else {
//				et_chang.setText("0");
//				et_kuan.setText("0");
//				et_height.setText("0");
//				
//			}
		
			try {
				obj.put("userId",
						String.valueOf(PreferencesUtils.getInt(getActivity(), PreferenceConstants.UID)));
				obj.put("latitude", latitude);
				obj.put("longitude", longitude);
				if (ding) {
					obj.put("cityCode", cityCode2);
					obj.put("townCode", townCode2);
					obj.put("fromLatitude", mylatitude);
					obj.put("fromLongitude", mylongitude);
				}else {
					if ("".equals(cityCode)) {
						obj.put("cityCode", cityCode33);
					}else {
						obj.put("cityCode", cityCode);
					}
					
					obj.put("townCode", townCode);
					if (latitude2==0.0) {
						obj.put("fromLatitude", latitude);
					}else {
						obj.put("fromLatitude", latitude2);
					}
					if (longitude2==0.0) {
						obj.put("fromLongitude", longitude);
					}else {
						obj.put("fromLongitude", longitude2);
					}
//					obj.put("fromLatitude", latitude);
//					obj.put("fromLongitude", longitude);
				}
				obj.put("personName", et_name.getText().toString());
//				obj.put("mobile", et_tel.getText().toString());
				obj.put("mobile", tmpstr);
				obj.put("address", et_address.getText().toString()+et_address_specific.getText().toString());
			
				obj.put("personNameTo", et_add_name.getText().toString());
//				obj.put("mobileTo", et_add_tel.getText().toString());
				obj.put("mobileTo", tmpstr2);
				obj.put("addressTo", et_add_address.getText().toString()+et_add_address_specific.getText().toString());
				obj.put("cityCodeTo", receiver_citycode);
				obj.put("toLatitude", receiver_latitude);
				obj.put("toLongitude", receiver_longitude);
				obj.put("publshDeviceId", DataTools.getDeviceId(getActivity()));
				
				obj.put("matName", et_goodname.getText().toString());
				obj.put("matWeight", et_weight.getText().toString());
				obj.put("matImageUrl", icon);
				obj.put("matRemark", et_ramk.getText().toString());
				obj.put("high", "0");
				obj.put("wide","0");
				obj.put("carLength",sunmit(1, kes));
				obj.put("matVolume",kese);
				obj.put("useTime",et_updata.getText().toString());
				
//				sunmit
				
				if (choxdel) {
					obj.put("carType", cartype);
					obj.put("length", "135");
				}else {
					obj.put("carType", "else");
					obj.put("length", "10");
				}
				
				if(chox){
					tou="Y";
					obj.put("whether", tou);
					obj.put("premium", edt_price.getText().toString().trim());
				}else {
					tou="N";
					obj.put("whether", tou);
					obj.put("premium", "");
				}
				if (chox2) {
					obj.put("ifReplaceMoney", chox2);
					obj.put("replaceMoney", edt_daipric.getText().toString().trim());
				}else {
					obj.put("ifReplaceMoney", chox2);
					obj.put("replaceMoney", "");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			dialog.show();
			Log.e("查看数据", obj.toString());
//			AsyncHttpUtils.doPostJson(getActivity(), MCUrl.DOWNWINDTASKPUBLISHS, obj.toString(),
			AsyncHttpUtils.doPostJson(getActivity(), MCUrl.PUblishTaskNew, obj.toString(),	
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
							Log.e("oppo", new String(arg2));
							dialog.dismiss();
							 bean = new Gson().fromJson(new String(arg2), DownSpecialBean.class);
							Log.e("oppop", bean.data.toString());
							if (bean.getErrCode() == 0) {
								DecimalFormat df = new DecimalFormat("######0.00");
									ToastUtil.shortToast(getActivity(), bean.getMessage());
									Intent intent = new Intent(getActivity(), DownWindPayActivity.class);
									intent.putExtra("money", bean.data.get(0).transferMoney);
//									intent.putExtra("insureCost", String.valueOf(df.format(bean.data.get(0).insureCost)));
									intent.putExtra("insureCost", bean.data.get(0).insureCost);
									intent.putExtra("billCode", bean.data.get(0).billCode);
									intent.putExtra("distance", bean.data.get(0).distance);
									intent.putExtra("baofei", edt_price.getText().toString());
									intent.putExtra("daishou",edt_daipric.getText().toString());
									intent.putExtra("tyy","顺风镖");
									Logger.e("billCode数据", bean.data.get(0).billCode);
									startActivityForResult(intent, 10);
//								}
								
							}else {
								ToastUtil.shortToast(getActivity(), bean.getMessage());
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
							dialog.dismiss();
						}
					});

		}
/**
 * 
 */
private void getrequst(int   resid) {
	RequestParams params = new RequestParams();
	AsyncHttpUtils.doGet(
			UrlMap.getUrl(MCUrl.afterPublish, "recId",
					""+resid),
			null, null, params, new AsyncHttpResponseHandler() {
				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				}
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					// TODO Auto-generated method stub
					Log.e("11111json", "" + new String(arg2));
					BaseBean beans = new Gson().fromJson(new String(arg2), BaseBean.class);
					if (beans.getErrCode()==-2) {
						ToastUtil.longToast(getActivity(), beans.getMessage());
					}
				}

			});
}
		/**
		 * 显示popupWindow
		 */
		private void showPopwindow() {
			// 利用layoutInflater获得View
			LayoutInflater inflater = (LayoutInflater)getActivity(). getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
			window.showAtLocation(getActivity().findViewById(R.id.img_headportrait), Gravity.BOTTOM, 0, 0);

			// 这里检验popWindow里的button是否可以点击
			TextView tv_camera = (TextView) view.findViewById(R.id.tv_camera);
			TextView tv_show = (TextView) view.findViewById(R.id.tv_show);
			tv_show.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startActivity(new Intent(getActivity(), IdCardActivity.class).putExtra("iconpath", "goodpath"));
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
							Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "goodsphoto.png")));
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
					// 相册选择
					Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(intent1, 1);

//					Intent intent1 = new Intent(Intent.ACTION_PICK, null);
//					intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//					// intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//					
					
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
		private void getCityCode(boolean dingwei) {
			boolean isCopySuccess = CheckDbUtils.checkDb();
			// 成功的将数据库copy到data 中
			if (isCopySuccess) {
				iWantApplication.getInstance().mDbManager = new DbManager(iWantApplication.getInstance());
			}
			if (city == null || city.equals("")) {
//				ToastUtil.shortToast(getActivity(), "请输入完整信息");
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
//				Log.e("citycode", cityCode);
				List<AreaBean> selectDataFromDbs = new AreaDboperation()
						.selectDataFromDb("select * from area where area_name='" + townaddressd2 + "'");
				if (selectDataFromDbs.size()>0 && !selectDataFromDbs.equals("")) {
					townCode2=selectDataFromDbs.get(0).area_code;
//					Log.e("11111townCode", townCode);
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
//			if (townaddressd.equals("")) {
//				townaddressd = PreferencesUtils.getString(getActivity(), PreferenceConstants.Codedess);
//			}
			
		}
		
		 class NetImageHandler implements Runnable {
		        @Override
		        public void run() {
		            try {
		                
		                //发送消息，通知UI组件显示图片
		            	result =HomSubFragment2. post(MCUrl.FILEDOWNWIND, map, map_file);
		                handler.sendEmptyMessage(0);
		            } catch (IOException e) {
		                e.printStackTrace();
		            }
		        }
		    }

		    Handler handler = new Handler(){
		        @Override
		        public void handleMessage(Message msg) {
		            if(msg.what == 0){
		            	sendEmptyUiMessage(MsgConstants.MSG_01);
		            }
		        }
		    };
		
		public void handleUiMessage1(Message msg) {
			// TODO Auto-generated method stub
			super.handleUiMessage(msg);
			switch (msg.what) {
			case MsgConstants.MSG_02:
				try {
					result =HomSubFragment2. post(MCUrl.FILEDOWNWIND, map, map_file);
					Log.e("111111result", result);
					sendEmptyUiMessage(MsgConstants.MSG_01);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
		}

		public void handleUiMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleUiMessage(msg);
			switch (msg.what) {
			case MsgConstants.MSG_02:
				try {
					result =HomSubFragment2.post(MCUrl.FILEDOWNWIND, map, map_file);
					Log.e("111111result", result);
					sendEmptyUiMessage(MsgConstants.MSG_01);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case MsgConstants.MSG_01:
				Log.e("111111result", result);
				IconBean bean = new Gson().fromJson(result, IconBean.class);
				if (bean.getErrCode()==0) {
				if (bean.data.size() != 0) {
					flag = true;
					icon = bean.data.get(0).filePath;
					PreferencesUtils.putString(getActivity(), PreferenceConstants.GOODPATH, icon);
					setPicToView(imghead);
					img_headportrait.setBackgroundDrawable(null);// 切换图片前先置空
					img_headportrait.setImageBitmap(imghead);
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
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			// outputX outputY 是裁剪图片宽高
			intent.putExtra("outputX", 150);
			intent.putExtra("outputY", 150);
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
		public boolean onTouchEvent(MotionEvent event) {
			InputMethodManager inputMethodManager = (InputMethodManager)getActivity(). getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
			return super.getActivity().onTouchEvent(event);
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
		 * 显示投保提示信息
		 */
		private void showPaywindow() {
			
			TextView btnsaves_pan,tet_tishi;
			// 利用layoutInflater获得View
			LayoutInflater inflater = (LayoutInflater) getActivity(). getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.popwidndow_baojia, null);
			// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
			window02 = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
					WindowManager.LayoutParams.MATCH_PARENT);
			// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
			window02.setFocusable(true);
			// 实例化一个ColorDrawable颜色为半透明
			// ColorDrawable dw = new ColorDrawable(R.color.transparent01);
			ColorDrawable dw = new ColorDrawable(android.R.color.white);
			window02.setBackgroundDrawable(dw);
			window02.setOutsideTouchable(false);// 这是点击外部不消失
			// 设置popWindow的显示和消失动画
			window02.setAnimationStyle(R.style.mypopwindow_anim_style);
			// 在底部显示
			window02.showAtLocation(HomSubFragment2.this.view.findViewById(R.id.checkbox_bao), Gravity.CENTER, 0, 0);
			btnsaves_pan=(TextView) view.findViewById(R.id.btnsaves_pan);
			tet_tishi=(TextView) view.findViewById(R.id.tet_tishi);
			tet_tishi.setText(adbean.getMessage());
			btnsaves_pan.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					window02.dismiss();
				}
			});
			// popWindow消失监听方法
			window02.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {

				}
			});

		}
		/**
		 * 提示信息
		 */
		private void showwind() {
			
			TextView btnsaves_pan,tet_tishi;
			// 利用layoutInflater获得View
			LayoutInflater inflater = (LayoutInflater) getActivity(). getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.popwidndow_toubao, null);
			// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
			window02 = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
					WindowManager.LayoutParams.MATCH_PARENT);
			// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
			window02.setFocusable(true);
			// 实例化一个ColorDrawable颜色为半透明
			// ColorDrawable dw = new ColorDrawable(R.color.transparent01);
			ColorDrawable dw = new ColorDrawable(android.R.color.white);
			window02.setBackgroundDrawable(dw);
			window02.setOutsideTouchable(false);// 这是点击外部不消失
			// 设置popWindow的显示和消失动画
			window02.setAnimationStyle(R.style.mypopwindow_anim_style);
			// 在底部显示
			window02.showAtLocation(HomSubFragment2.this.view.findViewById(R.id.checkbox_bao), Gravity.CENTER, 0, 0);
			btnsaves_pan=(TextView) view.findViewById(R.id.btnsaves_panb);
			btnsaves_pan.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					window02.dismiss();
				}
			});
			// popWindow消失监听方法
			window02.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {

				}
			});

		}
		/**
		 * 显示投保提示信息
		 */
		private void showPaywindowdai() {
			
			TextView btnsaves_pan,tet_tishi;
			// 利用layoutInflater获得View
			LayoutInflater inflater = (LayoutInflater) getActivity(). getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.popwidndow_daishou, null);
			// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
			window02 = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
					WindowManager.LayoutParams.MATCH_PARENT);
			// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
			window02.setFocusable(true);
			// 实例化一个ColorDrawable颜色为半透明
			// ColorDrawable dw = new ColorDrawable(R.color.transparent01);
			ColorDrawable dw = new ColorDrawable(android.R.color.white);
			window02.setBackgroundDrawable(dw);
			window02.setOutsideTouchable(false);// 这是点击外部不消失
			// 设置popWindow的显示和消失动画
			window02.setAnimationStyle(R.style.mypopwindow_anim_style);
			// 在底部显示
			window02.showAtLocation(HomSubFragment2.this.view.findViewById(R.id.checkbox_bao), Gravity.CENTER, 0, 0);
			btnsaves_pan=(TextView) view.findViewById(R.id.btnsaves_pan);
			btnsaves_pan.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					window02.dismiss();
				}
			});
			// popWindow消失监听方法
			window02.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {

				}
			});

		}
}
