package com.hex.express.iwant.activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.bean.AreaBean;
import com.hex.express.iwant.bean.CityBean;
import com.hex.express.iwant.bean.DownSpecialBean;
import com.hex.express.iwant.bean.LogisBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.helper.AreaDboperation;
import com.hex.express.iwant.helper.CheckDbUtils;
import com.hex.express.iwant.helper.CityDbOperation;
import com.hex.express.iwant.helper.DbManager;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.utils.Logger;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.StringUtil;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.TimePickerView;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemSelectedListener;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * 
 * @author huyichuan
 *  发物流界面
 */
public class LogisticalActivity extends BaseActivity implements OnCheckedChangeListener, OnItemSelectedListener{
	@Bind(R.id.lo_show)
	TitleBarView lo_show;
	@Bind(R.id.lo_name_huo)
	EditText lo_name_huo;//货物名称
	@Bind(R.id.togg)
	ToggleButton togg;   //发货地址选择器
	@Bind(R.id.lo_chufa)//发货地址
	EditText lo_chufa;
	@Bind(R.id.et_address_specific)//发货地址2
	EditText et_address_specific;
	@Bind(R.id.togg1)//到达地址选择器
	ToggleButton togg1;
	@Bind(R.id.lo_daoda)//到货地址
	EditText lo_daoda;
	@Bind(R.id.et_add_address_specific)//到货地址2
	EditText et_add_address_specific;
	
	@Bind(R.id.editText1)//货物重量
	EditText editText1;
	@Bind(R.id.editText2)//货物体积
	EditText editText2;
	@Bind(R.id.lo_sendtime)//发货时间
	TextView lo_sendtime;
	@Bind(R.id.lo_sendtext)//发货时间text选择
	TextView lo_sendtext;
	@Bind(R.id.lo_endtime_text)//送达时间text选择
	TextView lo_endtime_text;
	@Bind(R.id.lo_endtime)//送达时间
	TextView lo_endtime;
	@Bind(R.id.lo_name)//收货人姓名
	EditText lo_name;
	@Bind(R.id.lo_phone)//收货人手机号
	EditText lo_phone;
	@Bind(R.id.lo_goodsm)//货物价值
	EditText lo_goodsm;
	@Bind(R.id.lo_remarks)//备注
	EditText lo_remarks;
	@Bind(R.id.lo_submit)//提交按钮
	Button lo_submit;
	@Bind(R.id.spinner1)//重量选择
	Spinner spinner1;
	@Bind(R.id.spinner3)//体积选择
	TextView spinner3;
	//是否买保险
		@Bind(R.id.checkbox_lo)
		CheckBox checkbox_lo;
		@Bind(R.id.edt_prices)
		EditText edt_price;
		@Bind(R.id.v_pps)
		View v_pp;
	
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
		private String icon;
		private Calendar c = null;//时间选择
	    private final static int DATE_DIALOG = 0;
	    private final static int DATE = 1; 
	    private ArrayAdapter<String> adapter; 
	    
	    private boolean up=false;//上门取货
	    private boolean down=false;//送货上门
	    private boolean weith;//重量  顿true
		private boolean chox=false; 
		PopupWindow window02;
		PopupWindow window03;
		TimePickerView pvTime;
		TimePickerView pvTimeto;
		String startPlaceTownCode,townaddressd;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_down_logistical);
		ButterKnife.bind(this);
		initData();
		initView();
		// 时间选择器
		pvTime = new TimePickerView(this, TimePickerView.Type.MONTH_DAY_HOUR_MIN);
		// 控制时间范围
//		 Calendar calendar = Calendar.getInstance();
//		 pvTime.setRange(calendar.get(Calendar.YEAR) - 20,
//		 calendar.get(Calendar.YEAR));
		pvTime.setTime(new Date());
		pvTime.setCyclic(false);
		pvTime.setCancelable(true);
		// 时间选择后回调
		pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

			@Override
			public void onTimeSelect(Date date) {
				   Date now = new Date(); 
				   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");//日期格式
				   String hehe = dateFormat.format(now); 
				   SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm");     
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
//				  if (between>0) {
				
					  lo_sendtime.setText(getTimes(date));
//				}else {
//					ToastUtil.shortToast(LogisticalActivity.this, "您不能选择以前时间");
//				}
				
			}
		});
		pvTimeto= new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
		// 控制时间范围
//		 Calendar calendar = Calendar.getInstance();
//		 pvTime.setRange(calendar.get(Calendar.YEAR) - 20,
//		 calendar.get(Calendar.YEAR));
		pvTimeto.setTime(new Date());
		pvTimeto.setCyclic(false);
		pvTimeto.setCancelable(true);
		// 时间选择后回调
		pvTimeto.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

			@Override
			public void onTimeSelect(Date date) {
				   Date now = new Date(); 
				   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//日期格式
				   String hehe = dateFormat.format(now); 
				   SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     
				   Date begin = null;
				   Date end = null ;
				try {
					begin = dfs.parse(hehe);
			        end = dfs.parse(getTime(date));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}     
//				Log.e("111shijian", ""+getTime(date));
//				  long between=(end.getTime()-begin.getTime())/1000;//除以1000是为了转换成秒   
//				  if (between>0) {
				Log.e("111shijian", ""+getTime(date));
					  lo_endtime.setText(getTime(date));
//				}else {
//					ToastUtil.shortToast(LogisticalActivity.this, "您不能选择以前时间");
//				}
				
			}
		});
	}
//	
	
	//选择到达时间
	public static String getTime(Date date) {
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd ");
//		SimpleDateFormat formats = new SimpleDateFormat("yyyy");
//		return "2016-"+format.format(date);
		  Date now = new Date(); 
		  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");//日期格式
		   String hehe = dateFormat.format(date); 
		return dateFormat.format(date);
	}
	//选择时间
		public static String getTimes(Date date) {
			SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm:ss");
			SimpleDateFormat formats = new SimpleDateFormat("yyyy");
			return "2017-"+format.format(date);
//			return format.format(date);
//			Date now = new Date(); 
//			  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");//日期格式
//			   String hehe = dateFormat.format(date); 
//			return dateFormat.format(date);
		}
	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
//		togg.setOnCheckedChangeListener(this);
		togg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {  
					up=true;
					lo_sendtext.setText("上门取货时间");
//					ToastUtil.shortToast(LogisticalActivity.this, "开 "+isChecked);
				} else {  
					up=false;
					lo_sendtext.setText("送到货场时间");
//					ToastUtil.shortToast(LogisticalActivity.this, "关 "+isChecked); 
                }  
			}
		});
//		togg1.setOnCheckedChangeListener(this);
		togg1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {  
					
					down=true;
//					lo_endtime_text.setText("送货上门时间");
//					ToastUtil.shortToast(LogisticalActivity.this, "开 "+isChecked);
				} else {  
					down=false;
//					lo_endtime_text.setText("用户自提时间");
//					ToastUtil.shortToast(LogisticalActivity.this, "关 "+isChecked); 
                }  
			}
		});
		lo_chufa.setOnClickListener(this);
		lo_daoda.setOnClickListener(this);
		spinner1.setOnItemSelectedListener(this);
		spinner3.setOnClickListener(this);
		lo_sendtime.setOnClickListener(this);
		lo_endtime.setOnClickListener(this);
		lo_phone.setOnClickListener(this);
		lo_submit.setOnClickListener(this);
//		lo_submit.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				ToastUtil.shortToast(getApplicationContext(), "提交");
//				
//			}
//		});
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
//	        			ToastUtil.shortToast(LogisticalActivity.this, "开 "+weith);
	              }else {
	            	    weith=false;
//	            	  ToastUtil.shortToast(LogisticalActivity.this, " guan  "+weith);
					   
				}
//	        		editText1.setText("");
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {

				}
       });
		lo_name.setOnClickListener(this);
		checkbox_lo.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {
					chox=arg1;
					edt_price.setVisibility(View.VISIBLE);
				   	v_pp.setVisibility(View.VISIBLE);
//					showPaywindow();
				}else {
					edt_price.setVisibility(View.GONE);
				   	v_pp.setVisibility(View.GONE);
					chox=arg1;
				}
			}
		});
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
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
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.togg://起始发货地址选择
//			togg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//				@Override
//				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//					// TODO Auto-generated method stub
//					if (isChecked) {  
//						up=false;
//						lo_sendtext.setText("上门取货时间");
//						ToastUtil.shortToast(LogisticalActivity.this, "开 "+isChecked);
//					} else {  
//						up=true;
//						ToastUtil.shortToast(LogisticalActivity.this, "关 "+isChecked); 
//	                }  
//				}
//			});
			break;
		case R.id.lo_chufa://起始发货地址
			startActivityForResult(new Intent(LogisticalActivity.this, AddressActivity.class), 7);
			break;
		case R.id.togg1://达到地址选择
			break;
		case R.id.lo_daoda://达到地址  AddressReceiveActivity   AddressActivity
			startActivityForResult(new Intent(LogisticalActivity.this, AddressReceiveActivity.class), 8);
			break;
		case R.id.spinner1://重量
//	             sper();
			break;
		case R.id.spinner3://货物体积
			break;
		case R.id.lo_sendtime://发货时间
//			  showDialog(DATE_DIALOG);
			  pvTime.show();
			break;
		case R.id.lo_endtime://达到时间
//			  showDialog(DATE);
			  pvTimeto.show();
			break;
		case R.id.lo_phone://手机号
//			ToastUtil.shortToast(getApplicationContext(), "手机号");
//			Intent intent1 = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//			startActivityForResult(intent1, 0);
			break;
		case R.id.lo_name://收货人姓名
			Intent intent2 = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent2,1);
			break;
			
		case R.id.lo_submit://提交
			addPostResult();
//			ceshi();
				break;
		default:
			break;
		}
	}
	//speer选择器
//	public void sper(){
//		final String[] myItems = getResources().getStringArray(
//				R.array.languages);
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_spinner_item, myItems);
//		adapter.setDropDownViewResource(R.layout.drop_down_item);  
//	        //第四步：将适配器添加到下拉列表上    
//	        spinner1.setAdapter(adapter);  
//	        spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
//	        	@Override
//				public void onItemSelected(AdapterView<?> parent, View view,
//						int position, long id) {
//	        		if(position==1){
//	        			weith=true;
//	              }else {
//					    weith=false;
//				}
////	        		editText1.setText("");
//				}
//				@Override
//				public void onNothingSelected(AdapterView<?> parent) {
//
//				}
//});
//	        
//		}
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
								lo_endtime.setText( year + "-" + (month+1) + "-" + dayOfMonth );
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
    private void ceshi(){
 	   Intent intent = new Intent(LogisticalActivity.this, LogisPayActivity.class);
 	   intent.putExtra("evaluationStatus","2");//余额支付 的价格	
// 		intent.putExtra("insureCost", String.valueOf(df.format(bean.data.get(0).insureCost)));
 		intent.putExtra("evaluationScore", "22");//其它支付的价格
 		intent.putExtra("billCode", "72795WL201612130833");//物流单号
 		intent.putExtra("way", "1");
 		startActivityForResult(intent, 10);
    }
	
	private void addPostResult() {
		JSONObject obj = new JSONObject();
		if (!StringUtil.isMobileNO(lo_phone.getText().toString())
				|| (lo_phone.getText().toString().trim().length() != 11)
						) {
			ToastUtil.shortToast(getApplicationContext(), "请输入正确的手机号码");
			return;
		}
		if(lo_name.getText().toString().equals("") 
				|| lo_endtime.getText().toString().equals("") || lo_sendtime.getText().toString().equals("")
				|| editText2.getText().toString().equals("") || editText1.getText().toString().equals("")
				|| lo_daoda.getText().toString().equals("") || lo_chufa.getText().toString().equals("")
				|| lo_name_huo.getText().toString().equals("")){
			ToastUtil.shortToast(getApplicationContext(), "请填写完资料");
			return;
		}
		
		try {
			obj.put("userId",
					String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
			obj.put("latitude", latitude);
			obj.put("longitude", longitude);
			obj.put("startPlaceCityCode", cityCode);
			obj.put("startPlaceTownCode", startPlaceTownCode);
			obj.put("cargoName", lo_name_huo.getText().toString());//货物名称  lo_name_huo.getText().toString()
			obj.put("startPlace", lo_chufa.getText().toString()+et_address_specific.getText().toString());//货物起始地点  lo_chufa.getText().toString()
			obj.put("entPlace",  lo_daoda.getText().toString()+et_add_address_specific.getText().toString());//货物到达地点   lo_daoda.getText().toString()
			obj.put("takeCargo", up);//是否需要取货  up
			obj.put("sendCargo", down);//是否需要送货  down
			obj.put("latitudeTo", mylatitude);
			obj.put("longitudeTo", mylongitude);
			obj.put("entPlaceCityCode", receiver_citycode);//
			if(weith==true){
				obj.put("cargoWeight", editText1.getText().toString()+"千克");//货物	
			}else {
				obj.put("cargoWeight", editText1.getText().toString()+"吨");//货物重量  editText1.getText().toString()
			}
			obj.put("cargoVolume", editText2.getText().toString());//货物体积  editText2.getText().toString()
			obj.put("takeTime", lo_sendtime.getText().toString());//发货时间  lo_sendtime.getText().toString()
			obj.put("arriveTime", lo_endtime.getText().toString());//到达时间 lo_endtime.getText().toString()
			obj.put("takeName", lo_name.getText().toString());//收货人姓名  lo_name.getText().toString()
			obj.put("takeMobile", lo_phone.getText().toString());//收货电话  lo_phone.getText().toString()
			obj.put("cargoCost", lo_goodsm.getText().toString());//货物价值
			obj.put("remark", lo_remarks.getText().toString());//备注	 lo_remarks.getText().toString()
		} catch (JSONException e) {
			e.printStackTrace();
		}
		dialog.show();
		Log.e("查看数据", obj.toString());
		//PublishNew    LOGISTICS
		AsyncHttpUtils.doPostJson(LogisticalActivity.this, MCUrl.PublishNew, obj.toString(),
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("11111111111 wwww   ", new String(arg2));
						dialog.dismiss();
						
						LogisBean bean = new Gson().fromJson(new String(arg2), LogisBean.class);
//						Log.e("oppop", bean.data.toString());
						if(bean.getErrCode()==0){
							Intent intent = new Intent(LogisticalActivity.this, LogisPayActivity.class);
						 	   intent.putExtra("evaluationStatus",bean.getData().get(0).evaluationStatus);//余额支付 的价格	
//						 		intent.putExtra("insureCost", String.valueOf(df.format(bean.data.get(0).insureCost)));
						 		intent.putExtra("evaluationScore", bean.getData().get(0).evaluationScore);//其它支付的价格
						 		intent.putExtra("billCode", bean.getData().get(0).billCode);//物流单号
						 		intent.putExtra("way", "1");
						 		startActivityForResult(intent, 10);
							
//							
						}
						else {
//							Intent intent = new Intent(LogisticalActivity.this, DownWindPayActivity.class);
//							intent.putExtra("money", "");
////							intent.putExtra("insureCost", String.valueOf(df.format(bean.data.get(0).insureCost)));
//							intent.putExtra("insureCost", "");
//							intent.putExtra("billCode","");
//							Logger.e("billCode数据", "");
//							startActivityForResult(intent, 10);
							ToastUtil.shortToast(getApplicationContext(), bean.getMessage());
						}
						

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						dialog.dismiss();
					}
				});

	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("11111111  ", resultCode+" ssss   "+data);
		switch (requestCode) {
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
							if(Build.VERSION.SDK_INT < 14) {
							    cursor.close();
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
					if(Build.VERSION.SDK_INT < 14) {
					    cursor.close();
					   }
				}

				
				break;
			case 7:
				if (resultCode == RESULT_OK) {
					latitude = data.getDoubleExtra("latitude", 0);
					longitude = data.getDoubleExtra("longitude", 0);
					city = data.getStringExtra("city");
					townaddressd= data.getStringExtra("townaddressd");
					getCityCode();
					lo_chufa.setText(data.getStringExtra("address").replace("中国", ""));
					Logger.e("111111     "+data.getStringExtra("address").replace("中国", ""));
//					ToastUtil.shortToast(getApplicationContext(), "111111");
				}
				break;
			case 8:
				if (resultCode == RESULT_OK) {
					mylatitude = data.getDoubleExtra("latitude", 0);
					mylongitude = data.getDoubleExtra("longitude", 0);
					city = data.getStringExtra("city");
					receive = true;
					getCityCode();
					lo_daoda.setText(data.getStringExtra("address").replace("中国", ""));
				}
				break;
			case 10:
//				if (resultCode == RESULT_OK) {
//				showwindows();
				finish();
//				}
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
	private void getCityCode() {
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
		if (receive) {
			receiver_citycode = selectDataFromDb.get(0).city_code;
			Log.e("citycode", receiver_citycode);
		} else {
			cityCode = selectDataFromDb.get(0).city_code;
			Log.e("citycodess", cityCode);
			List<AreaBean> selectDataFromDbs = new AreaDboperation()
					.selectDataFromDb("select * from area where area_name='" + townaddressd + "'");
			if (selectDataFromDbs.size()>0 && !selectDataFromDbs.equals("")) {
				startPlaceTownCode=selectDataFromDbs.get(0).area_code;
			}
		}
//		if (townaddressd.equals("")) {
//			townaddressd = PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.Codedess);
//		}
		
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
		if (receive) {
			receiver_citycoded = selectDataFromDb.get(0).city_code;
			Log.e("citycode", receiver_citycoded);

		} else {
			cityCode = selectDataFromDb.get(0).city_code;
			Log.e("citycode", cityCode);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 显示投保提示信息
	 */
	private void showPaywindow() {
		
		TextView btnsaves_pan;
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		window02.showAtLocation(LogisticalActivity.this.findViewById(R.id.checkbox_lo), Gravity.CENTER, 0, 0);
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
	/**
	 * 显示提示信息
	 */
	private void showwindows() {
		
		TextView btnsaves_pan;
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.popwidnow_wuliu, null);
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
		window02.showAtLocation(LogisticalActivity.this.findViewById(R.id.lo_submit), Gravity.CENTER, 0, 0);
		btnsaves_pan=(TextView) view.findViewById(R.id.btnsaves_pans);
		btnsaves_pan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				window02.dismiss();
				finish();
			}
		});
		// popWindow消失监听方法
		window02.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				finish();
			}
		});

	}
	/**
	 * 显示支付方式
	 */
	private void showPay() {
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.popwidndow_logistical_pay, null);
		// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
		window03 = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT);
		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window03.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		// ColorDrawable dw = new ColorDrawable(R.color.transparent01);
		ColorDrawable dw = new ColorDrawable(android.R.color.white);
		window03.setBackgroundDrawable(dw);
		window03.setOutsideTouchable(false);// 这是点击外部不消失
		// 设置popWindow的显示和消失动画
		window03.setAnimationStyle(R.style.mypopwindow_anim_style);
		// 在底部显示
		window03.showAtLocation(LogisticalActivity.this.findViewById(R.id.checkbox_lo), Gravity.CENTER, 0, 0);
		LinearLayout ll_alipay,ll_month,ll_surplus,ll_weixin;
		RadioButton cb_gift;
		ImageView img_cancle02;
		ll_alipay = (LinearLayout) view.findViewById(R.id.ll_alipay);//支付宝
		ll_surplus = (LinearLayout) view.findViewById(R.id.ll_surplus);
		ll_weixin = (LinearLayout) view.findViewById(R.id.ll_weixin);
		cb_gift = (RadioButton) view.findViewById(R.id.cb_gift);
		img_cancle02 = (ImageView) view.findViewById(R.id.img_cancel02);

//		ll_surplus.
		img_cancle02.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				window03.dismiss();

			}
		});
		cb_gift.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
//					Intent intent = new Intent(PublicOtherDetailActivity.this, CardActivity.class);
//					intent.putExtra("gift", true);
//					startActivityForResult(intent, 2);
				}
			}
		});
		//支付宝
		ll_alipay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				MessageUtils.alertMessageCENTER(PublicOtherDetailActivity.this, "正在唤醒支付宝");
//				getAlipay();
				window02.dismiss();
			}
		});
		//微信
		ll_weixin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				MessageUtils.alertMessageCENTER(PublicOtherDetailActivity.this, "正在唤醒微信");
//				getWxChat();
				window03.dismiss();

			}

		});
		ll_surplus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Builder ad = new Builder(LogisticalActivity.this);
				ad.setTitle("温馨提示");
				ad.setMessage("确认是否使用余额支付？");
				ad.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
//						getaddsurplus();
						window03.dismiss();
					}
				});
				ad.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				ad.create().show();

			}

		});
		// popWindow消失监听方法
		window03.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {

			}
		});

	}


	
}
