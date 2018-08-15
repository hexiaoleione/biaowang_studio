package com.hex.express.iwant.newactivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.activities.BaseActivity;
import com.hex.express.iwant.bean.LogisBean;
import com.hex.express.iwant.bean.RegisterBean;
import com.hex.express.iwant.bean.ShopBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.newsmain.NewMainActivity;
import com.hex.express.iwant.utils.DataTools;
import com.hex.express.iwant.utils.Logger;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.StringUtil;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.witget.DatePicker;
import com.hex.express.iwant.witget.TimePicker;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 
 * @author huyichuan
 *物流配送
 */
public class LoginseticsActivity extends BaseActivity {
	
	// 寄件人信息
				@Bind(R.id.et_name)
				EditText et_name;//寄件人姓名
				@Bind(R.id.et_tel)
				EditText et_tel;//寄件人电话
//				 收件人信息
				@Bind(R.id.et_add_name)
				EditText et_add_name;
				@Bind(R.id.et_add_tel)
				EditText et_add_tel;
				@Bind(R.id.et_goodname)//物品名字
				EditText et_goodname;
				
				 @Bind(R.id.et_carchang)
					Spinner et_carchang;
					@Bind(R.id.et_tijisp)
					Spinner et_tijisp;
					
					@Bind(R.id.et_tiji)
					Spinner et_tiji;
					
					@Bind(R.id.et_huodizhi)//指定货场
					EditText et_huodizhi;
					@Bind(R.id.houaddse)//指定货场
					LinearLayout	houaddse;
					@Bind(R.id.btn_sumit)//tijao
					Button btn_sumit;
					@Bind(R.id.btn_sumitbao)//tijao
					Button btn_sumitbao;
					
					@Bind(R.id.iv_senderList)
			       ImageView		iv_senderList;
					@Bind(R.id.iv_receiverList)
				       ImageView		iv_receiverList;
					 @Bind(R.id.topButton)
						ImageView topButton;
					 @Bind(R.id.et_time)
					 TextView et_time;
					private boolean chox; 
					private boolean chox2; 
					int	 	tiji=0;
					private boolean change3;
					private Calendar calendar;
					private DatePicker dp_test;
					private TimePicker tp_test;
					private TextView tv_ok,tv_cancel;	//确定、取消button
					private Button btn_naozhong;
					private PopupWindow pw;
					private String selectDate,selectTime;
					//选择时间与当前时间，用于判断用户选择的是否是以前的时间
					private String zerotime = "00";
					private int currentHour,currentMinute,currentDay,selectHour,selectMinute,selectDay,maxDayOfMonth;
					private int  moyu;
					//整体布局
					boolean timeb=true;
					boolean	dadada=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logisetics_activity);
		iWantApplication.getInstance().addActivity(this);
		 ButterKnife.bind(this);
		 getrequstBalance();
		 initView();
		 initData();
		 setOnClick();
	}

	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 获取
	 */
	private void getrequstBalance() {
		RequestParams params = new RequestParams();
		AsyncHttpUtils.doGet(
				UrlMap.getUrl(MCUrl.BALANCE, "id",
						String.valueOf(PreferencesUtils.getInt(LoginseticsActivity.this, PreferenceConstants.UID))),
				null, null, params, new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
					}
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						Logger.e("json", "" + new String(arg2));
						
						final RegisterBean bean = new Gson().fromJson(new String(arg2), RegisterBean.class);
						if (bean.getErrCode()==0) {
							if (bean.getErrCode()==0) {
								if ("1".equals(bean.getData().get(0).shopType)) {
									 et_tel.setText(bean.data.get(0).mobile);
									RequestParams params2 = new RequestParams();
								
									AsyncHttpUtils.doGet(
											UrlMap.getUrl(MCUrl.getChapman, "userId",
													String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID))),
											null, null, params2, new AsyncHttpResponseHandler() {
//										@Override
										public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
										}
										@Override
										public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
											if (arg2 == null)
												return;
											Log.e("json1111 ", "" + new String(arg2));
											ShopBean shopbean = new Gson().fromJson(new String(arg2), ShopBean.class);
											if (shopbean.getErrCode()==0) {
												
												 et_name.setText(shopbean.data.get(0).shopName);
											
											}else {
												 et_name.setText(bean.data.get(0).userName);
											}
										}
									});	
								}else {
									 et_name.setText(bean.data.get(0).userName);
									 et_tel.setText(bean.data.get(0).mobile);
								}
//								if ("2".equals(getIntent().getStringExtra("nochange"))) {
//									et_name.setText( getIntent().getStringExtra("personName"));
//									et_tel.setText( getIntent().getStringExtra("mobile"));
//								}else {
//									 et_name.setText(bean.data.get(0).userName);
//									 et_tel.setText(bean.data.get(0).mobile);
//								}
								}
					}
						
					}

				});
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
	
		if ("2".equals(getIntent().getStringExtra("nochange"))) {
			
			et_name.setText( getIntent().getStringExtra("personName"));
			et_tel.setText( getIntent().getStringExtra("mobile"));
			et_add_name.setText( getIntent().getStringExtra("personNameTo"));
			et_add_tel.setText( getIntent().getStringExtra("mobileTo"));
			et_goodname.setText( getIntent().getStringExtra("matName"));
				chox=getIntent().getBooleanExtra("takeCargo", true);
		
				chox2=getIntent().getBooleanExtra("sendCargo", true);
		}
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		   
        final String[] myItems1 = getResources().getStringArray(
				R.array.quhuo);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(LoginseticsActivity.this,
				android.R.layout.simple_spinner_item, myItems1);
		adapter1.setDropDownViewResource(R.layout.dor_wu); 
		et_carchang.setAdapter(adapter1);  
		et_carchang.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
//				kes=position;
				
				if(position==1){
					 chox=false;
//        			ToastUtil.shortToast(getActivity(), "开 "+chox);
              }else {
            		chox=true;
            	 
			   }
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		houaddse.setVisibility(View.VISIBLE);
		final String[] myItems2 = getResources().getStringArray(
				R.array.songhuo);
		final ArrayAdapter<String> adapter11 = new ArrayAdapter<String>(LoginseticsActivity.this,
				android.R.layout.simple_spinner_item, myItems2);
		adapter11.setDropDownViewResource(R.layout.dor_wu); 
		et_tijisp.setAdapter(adapter11);  
		et_tijisp.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(position==1){
					chox2=false;
//					ToastUtil.shortToast(getActivity(), "开 "+chox2);
					houaddse.setVisibility(View.VISIBLE);
              }else {
            	  houaddse.setVisibility(View.INVISIBLE);
            	  chox2=true;
            	  
//            		ToastUtil.shortToast(getActivity(), "false "+chox2);
			   }
//				ToastUtil.shortToast(LoginseticsActivity.this,"chox2 "+chox2);
//				kes2=position;
//				kese=adapter1.getItem(position);
//				ToastUtil.shortToast(getActivity(), "position"+adapter1.getItem(position));
//				Log.e("11111", "position"+sunmit(5, position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		List<String> data_list ;
		data_list=new  ArrayList<String>();
		if ("2".equals(getIntent().getStringExtra("nochange"))) {
			data_list.add(getIntent().getStringExtra("cargoVolume"));
		}
		data_list.add("1立方米以下");
		for (int i = 2; i <= 50; i++) {
			data_list.add(i+"立方米");
		}
		final String[] myItems = getResources().getStringArray(
				R.array.zhong);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(LoginseticsActivity.this,
				android.R.layout.simple_spinner_item, data_list);
		adapter.setDropDownViewResource(R.layout.drop_down_item); 
		et_tiji.setAdapter(adapter);  
		et_tiji.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				tiji=position;
				if (position==0) {
					if (!"".equals(getIntent().getStringExtra("cargoVolume"))) {
						change3=true;
//						ToastUtil.shortToast(ReleaseActivity.this, "122"+change4);
					}
				}else {
					change3=false;
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		 
			et_time.setText(getIntent().getStringExtra("limitTime"));
		 et_time.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				pvTime.show();
//				submit.setVisibility(View.VISIBLE);
				calendar = Calendar.getInstance();
				View view = View.inflate(LoginseticsActivity.this, R.layout.dialog_select_time, null);
				 if(calendar.get(Calendar.DAY_OF_MONTH)<10 && (calendar.get(Calendar.MONTH) + 1)<10){
					  selectDate = calendar.get(Calendar.YEAR) + "-" +"0"+(calendar.get(Calendar.MONTH) + 1) + "-"
				                +"0"+calendar.get(Calendar.DAY_OF_MONTH);
					}else  if ((calendar.get(Calendar.DAY_OF_MONTH)<10)) {
						  selectDate = calendar.get(Calendar.YEAR) + "-"  +(calendar.get(Calendar.MONTH) + 1) + "-"
								  +"0"+calendar.get(Calendar.DAY_OF_MONTH);
					}else if ((calendar.get(Calendar.MONTH) + 1)<10) {
						selectDate = calendar.get(Calendar.YEAR) + "-" +"0"+ (calendar.get(Calendar.MONTH) + 1) + "-"
				                +calendar.get(Calendar.DAY_OF_MONTH);
					}else {
						  selectDate = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
					                + calendar.get(Calendar.DAY_OF_MONTH);
					}
				//选择时间与当前时间的初始化，用于判断用户选择的是否是以前的时间，如果是，弹出toss提示不能选择过去的时间
				 moyu=(calendar.get(Calendar.MONTH) + 1);
				    selectDay = currentDay = calendar.get(Calendar.DAY_OF_MONTH);
				    maxDayOfMonth= Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
//				    int maxDayOfMonth = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
			        selectMinute = currentMinute = calendar.get(Calendar.MINUTE)+5;
			        selectHour = currentHour = calendar.get(Calendar.HOUR_OF_DAY)+1;
//			        if ((currentMinute+35)>55){
//	                    selectHour = currentHour + 1;
//	                        selectMinute = (currentMinute + 35 - 60) >= ((currentMinute + 35 - 60) / 10 * 10 + 5) ? ((currentMinute + 35 - 60) / 10 + 1) * 10 + 5 : ((currentMinute + 35 - 60) / 10) * 10 + 5;
////	                    selectTime = selectHour + ":" + selectMinute;//zerotime
//	                        selectTime = selectHour + ":" + ((selectMinute < 10) ? ("0" + selectMinute) : selectMinute);//zerotime
//	                }else {
//	                    selectMinute = (currentMinute + 35) >= ((currentMinute + 35) / 10 * 10 + 5) ? ((currentMinute + 35) / 10 + 1) * 10 + 5 : ((currentMinute + 35) / 10) * 10 + 5;
////	                    selectTime = selectHour + ":" + selectMinute;//zerotime
//	                    selectTime = selectHour + ":" + ((selectMinute < 10) ? ("0" + selectMinute) : selectMinute);//zerotime
//	                }
			        if ((currentMinute)>59){
	                    selectHour = currentHour + 1;
//	                        selectMinute = (currentMinute + 5 - 60) >= ((currentMinute + 5 - 60) / 10 * 10 + 5) ? ((currentMinute + 5 - 60) / 10 + 1) * 10 + 5 : ((currentMinute + 5 - 60) / 10) * 10 + 5;
	                    selectMinute = (currentMinute  - 60) >= ((currentMinute  - 60) / 5 * 5) ? ((currentMinute  - 60) / 5 ) * 5 : ((currentMinute  - 60) / 5) *  5;
	                    if (selectHour>23){
	                        selectHour = 0;
	                        selectDay +=1;
	                        if(selectDay <= maxDayOfMonth){
	                        	 selectDate = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
	 	                                + selectDay;
	                        }else  {
	                        	selectDay =selectDay-maxDayOfMonth;
	                        	 selectDate = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 2) + "-"
	 	                                + selectDay;
							}
	                       
	                    }
//	                    if (selectHour==24) {
//	                    	selectDay+=1;
//	                    	selectHour=0;
//						}
	                    Toast.makeText(getApplicationContext(), ""+selectMinute +"  hs "+ currentMinute, Toast.LENGTH_LONG).show();
	                    selectTime = selectHour + ":" + ((selectMinute < 10) ? ("0" + selectMinute) : selectMinute);//zerotime
	                }else {
	                    selectMinute= ((currentMinute ) >= ((currentMinute ) / 5 *  5) ? ((currentMinute ) / 5 ) *  5 : ((currentMinute ) / 5) * 5);
//	                    selectMinute = (currentMinute + 5) >= ((currentMinute + 5) / 10 * 10 + 5) ? ((currentMinute + 5) / 10 + 1) * 10 + 5 : ((currentMinute + 5) / 10) * 10 + 5;
	                    if (selectMinute>59){
	                        selectMinute -=60;
	                        selectHour += 1;
	                    }
	                    if (selectHour>23){
                        selectHour = 0;
                        selectDay +=1;
                        if(selectDay <= maxDayOfMonth){
                        	 selectDate = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
 	                                + selectDay;
                        }else  {
                        	selectDay =selectDay-maxDayOfMonth;
                        	 selectDate = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 2) + "-"
 	                                + selectDay;
						}
                       
                    }
	                    selectTime = selectHour + ":" + ((selectMinute < 10) ? ("0" + selectMinute) : selectMinute);//zerotime
	                }

				
				dp_test = (DatePicker)view.findViewById(R.id.dp_test);
				tp_test = (TimePicker)view.findViewById(R.id.tp_test);
				tv_ok = (TextView) view.findViewById(R.id.tv_ok);
				tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
				//设置滑动改变监听器
				dp_test.setOnChangeListener(dp_onchanghelistener);
				tp_test.setOnChangeListener(tp_onchanghelistener);
				pw = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
//				//设置这2个使得点击pop以外区域可以去除pop
//				pw.setOutsideTouchable(true);
//				pw.setBackgroundDrawable(new BitmapDrawable());
				
				//出现在布局底端
//				pw.showAtLocation(Rl_all, 0, 0,  Gravity.END);
				ColorDrawable dw = new ColorDrawable(android.R.color.white);
				pw.setBackgroundDrawable(dw);
				pw.setOutsideTouchable(false);// 这是点击外部不消失
				// 设置popWindow的显示和消失动画
				pw.setAnimationStyle(R.style.mypopwindow_anim_style);
				pw.showAtLocation(LoginseticsActivity.this.findViewById(R.id.btn_sumit), Gravity.END, 1, 1);
				
				//点击确定
				tv_ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						
							  
						   int curDay = currentDay;
	                        int curHour = currentHour;
	                        int curMinute = currentMinute-5;
	                        if (curMinute > 59){
	                            curMinute -=60;
	                            curHour +=1;
	                        }
	                        if (curHour>=23){
	                            curHour -=23;
	                            if ((curDay +=1) <= maxDayOfMonth) {
	                            	 curDay +=1;//这里最好再判断一下本月的最大天数，判断是否比较月份
								}else {
									curDay=((curDay+=1)-maxDayOfMonth);
								}
	                           
	                        }
	                       
//	                  	   Toast.makeText(getApplicationContext(), selectMinute + " " + curMinute + "a", Toast.LENGTH_LONG).show();
	                        if (selectDay == curDay) {    //在当前日期情况下可能出现选中过去时间的情况
	                            if (selectHour < curHour) {
	                                Toast.makeText(getApplicationContext(), "不能选择过去的时间\n请重新选择", Toast.LENGTH_LONG).show();
//	                                Toast.makeText(getApplicationContext(), selectHour +"  s "+ curHour, Toast.LENGTH_LONG).show();
	                            } else if ((selectHour == curHour) && (selectMinute < curMinute)) {
	                            	
//	                                Toast.makeText(getApplicationContext(), selectHour +"  s1 "+ curMinute, Toast.LENGTH_LONG).show();
	                                Toast.makeText(getApplicationContext(), "不能选择过去的时间\n 请重新选择", Toast.LENGTH_LONG).show();
	                           	
	                            }
//	                            else if ((selectHour > curHour) && (selectMinute < curMinute)) {
//	                                Toast.makeText(getApplicationContext(), "不能选择过去的时间\n  请重新选择", Toast.LENGTH_LONG).show();
//	                           	
//	                            } 
	                            else {
//	                            	if (selectHour>23) {
////	  	                      		  Toast.makeText(getApplicationContext(), "不能选择过去的时间\n        请重选择日期", Toast.LENGTH_LONG).show();
//	  	                      	     	selectHour=0;
//	  	                      		  selectTime = selectHour + ":" + ((selectMinute < 10) ? ("0" + selectMinute) : selectMinute);//zerotime
//	  	                      		  selectDay+=1;
//	  	                      		  if (selectDay>30) {
//	  	                      			if (!dadada) {
//	  	                      				 Toast.makeText(getApplicationContext(), "获取月份日期失败！请重选择月份日期", Toast.LENGTH_LONG).show();
//	  	                      				pw.dismiss();
//	  	                      				 return;
//	  	                      			
//	  									}
//	  								}
//	  	                      		 if(calendar.get(Calendar.DAY_OF_MONTH)<10 && (calendar.get(Calendar.MONTH) + 1)<10){
//	  	           					  selectDate = calendar.get(Calendar.YEAR) + "-" +"0"+(calendar.get(Calendar.MONTH) + 1) + "-"
//	  	           				                +"0"+selectDay;
//	  	           					}else  if ((calendar.get(Calendar.DAY_OF_MONTH)<10)) {
//	  	           						  selectDate = calendar.get(Calendar.YEAR) + "-"  +(calendar.get(Calendar.MONTH) + 1) + "-"
//	  	           								  +"0"+selectDay;
//	  	           					}else if ((calendar.get(Calendar.MONTH) + 1)<10) {
//	  	           						selectDate = calendar.get(Calendar.YEAR) + "-" +"0"+ (calendar.get(Calendar.MONTH) + 1) + "-"
//	  	           				                +selectDay;
//	  	           					}else {
//	  	           						  selectDate = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
//	  	           					                +selectDay;
//	  	           					}
	                            	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	                        		try
	                        		{
	                        		Date date=new Date();
	                        		 DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
	                        		 String time=format.format(date);
	                        		  Date d1 = df.parse(selectDate+" "+selectTime);
	                        		  Date d2 = df.parse(time);
	                        		  long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
	                        		  long days = diff / (1000 * 60 * 60 * 24);
	                        		  long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
	                        		  long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
//	                        		  System.out.println(""+days+"天"+hours+"小时"+minutes+"分");
	                        		  if(hours<=0){
	                            		ToastUtil.shortToast(LoginseticsActivity.this, "不能选择过去的时间\n请重新选择。");
	                            		  et_time.setText("");
//	                            	 return;
	                                  }else {
//	                                	  et_time.setText(selectDate+" "+selectTime);
	                                	  if (selectDay <=maxDayOfMonth  && selectHour  <  24) {
	                                		  et_time.setText(selectDate+" "+selectTime);
										}else {
										
											 Toast.makeText(getApplicationContext(), "获取月份日期失败！请重选择月份日期", Toast.LENGTH_LONG).show();
										}
									}
	                        		}
	                        		catch (Exception e)
	                        		{
	                        		}
							
							
							pw.dismiss();
//						}
	                   }
						}else{
							if (selectHour>23 ) {
//	                      		  Toast.makeText(getApplicationContext(), "不能选择过去的时间\n        请重选择日期", Toast.LENGTH_LONG).show();
	                      	     	selectHour=0;
	                      		  selectTime = selectHour + ":" + ((selectMinute < 10) ? ("0" + selectMinute) : selectMinute);//zerotime
	                      		  
	     	                        if(selectDay <= maxDayOfMonth){
	     	                        	selectDay+=1;
	     	                        }else  {
	     	                        	selectDay=(selectDay-maxDayOfMonth);
	     							}
//	     	                       Toast.makeText(getApplicationContext(), selectDay+"请重选择月份日期.", Toast.LENGTH_LONG).show();
	                      		  if (selectDay  >  maxDayOfMonth) {
	                      			if (!dadada) {
	                      				 Toast.makeText(getApplicationContext(), "获取月份日期失败！请重选择月份日期.", Toast.LENGTH_LONG).show();
	                      				pw.dismiss();
	                      				 return;
									}
								}
	                      		 if(calendar.get(Calendar.DAY_OF_MONTH)<10 && (calendar.get(Calendar.MONTH) + 1)<10){
	           					  selectDate = calendar.get(Calendar.YEAR) + "-" +"0"+(calendar.get(Calendar.MONTH) + 1) + "-"
	           				                +"0"+selectDay;
	           					}else  if ((calendar.get(Calendar.DAY_OF_MONTH)<10)) {
	           						  selectDate = calendar.get(Calendar.YEAR) + "-"  +(calendar.get(Calendar.MONTH) + 1) + "-"
	           								  +"0"+selectDay;
	           					}else if ((calendar.get(Calendar.MONTH) + 1)<10) {
	           						selectDate = calendar.get(Calendar.YEAR) + "-" +"0"+ (calendar.get(Calendar.MONTH) + 1) + "-"
	           				                +selectDay;
	           					}else {
	           						  selectDate = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
	           					                +selectDay;
	           					}
//	                      		et_time.setText(selectDate +" "+ selectTime);
	                      		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        		try
                        		{
                        		Date date=new Date();
                        		 DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        		 String time=format.format(date);
                        		  Date d1 = df.parse(selectDate+" "+selectTime);
                        		  Date d2 = df.parse(time);
                        		  long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
                        		  long days = diff / (1000 * 60 * 60 * 24);
                        		  long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
                        		  long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
//                        		  System.out.println(""+days+"天"+hours+"小时"+minutes+"分");
                        		  if(hours<=0){
                            		ToastUtil.shortToast(LoginseticsActivity.this, "不能选择过去的时间，\n请重新选择");
//                            	 return;
                            		  et_time.setText("");
                                  }else {
                                	  if (selectDay <=maxDayOfMonth  && selectHour  <  24) {
                                		  et_time.setText(selectDate+" "+selectTime);
									}else {
										 Toast.makeText(getApplicationContext(), "获取月份日期失败！请重选择月份日期。", Toast.LENGTH_LONG).show();
									}
                                	
								}
                        		}
                        		catch (Exception e)
                        		{
                        		}
								}else {
//									et_time.setText(selectDate +" "+ selectTime);
									DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	                        		try
	                        		{
	                        		Date date=new Date();
	                        		 DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
	                        		 String time=format.format(date);
	                        		
	                        		  Date d1 = df.parse(selectDate+" "+selectTime);
	                        		  Date d2 = df.parse(time);
	                        		  long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
	                        		  long days = diff / (1000 * 60 * 60 * 24);
	                        		  long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
	                        		  long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
//	                        		  System.out.println(""+days+"天"+hours+"小时"+minutes+"分");
	                        		  if(hours<=0){
	                            		ToastUtil.shortToast(LoginseticsActivity.this, "不能选择过去的时间\n 请重新选择!");
//	                            	 return;
	                            		  et_time.setText("");
	                                  }else {
//	                                	  et_time.setText(selectDate+" "+selectTime);
	                                	  
	                                	  if (selectDay <=maxDayOfMonth  && selectHour  <  24) {
	                                		  et_time.setText(selectDate+" "+selectTime);
										}else {
											 Toast.makeText(getApplicationContext(), "获取月份日期失败，请重选择月份日期！", Toast.LENGTH_LONG).show();
										}
									}
	                        		}
	                        		catch (Exception e)
	                        		{
	                        		}
								}
//							et_time.setText(selectDate+" "+selectTime);
							pw.dismiss();
						}
					}
				});
				
				//点击取消
				tv_cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						pw.dismiss();
					}
				});
			}
		});
	}
	DatePicker.OnChangeListener dp_onchanghelistener = new DatePicker.OnChangeListener() {
		@Override
		public void onChange(int year, int month, int day, int day_of_week) {
			selectDay = day;
			dadada=true;
//			selectDate = year + "年" + month + "月" + day + "日" + DatePicker.getDayOfWeekCN(day_of_week);
//			selectDate = year + "-" + month + "-" + day + " " ;//+ DatePicker.getDayOfWeekCN(day_of_week)
			if(day<10 && month<10){
				selectDate = year + "-" +"0"+month + "-" +"0"+ day + " "  ;
			}else if (day<10) {
				selectDate = year + "-" +month + "-" +"0"+ day + " "  ;
			}else if (month<10) {
				selectDate = year + "-"+"0"+month + "-" + day + " "  ;
			}
			else {
				selectDate = year + "-" + month + "-" + day + " "  ;
			}
		}
	};
	TimePicker.OnChangeListener tp_onchanghelistener = new TimePicker.OnChangeListener() {
		@Override
		public void onChange(int hour, int minute) {
			
				  selectTime = hour + ":" + ((minute < 10) ? ("0" + minute) : minute) + "";
				  selectHour = hour;
		           selectMinute = minute;
//			}
			timeb=false;
		}
	};

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub
	topButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btn_sumit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				addPostResult();
			}
		});
		btn_sumitbao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				addPostResultbao();
			}
		});
		// TODO Auto-generated method stub
		iv_senderList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent1 = new Intent(Intent.ACTION_PICK);
				intent1.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
				startActivityForResult(intent1, 0);
			}
		});
		iv_receiverList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent21 = new Intent(Intent.ACTION_PICK);
				intent21.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
				startActivityForResult(intent21, 9);
			}
		});
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}
	private void addPostResult() {
		JSONObject obj = new JSONObject();
		String string =et_tel.getText().toString();
		String string2 =et_add_tel.getText().toString();
		  String tmpstr=string.replace(" ","");
		  String tmpstr2=string2.replace(" ","");
		if (!StringUtil.isMobileNO(tmpstr)
				|| (tmpstr.length() != 11)
						) {
			ToastUtil.shortToast(LoginseticsActivity.this, "请输入正确发件人的手机号码");
			return;
		}
		if (!chox2) {
			if (et_huodizhi.getText().toString().equals("")) {
				ToastUtil.shortToast(LoginseticsActivity.this, "请填写指定物流园区地址");
				return;
			}
			
		}
		if (!StringUtil.isMobileNO(tmpstr2)
				|| (tmpstr2.length() != 11)
						) {
			ToastUtil.shortToast(LoginseticsActivity.this, "请输入正确收件人的手机号码");
			return;
		}
		if( et_name.getText().toString().equals("") 
				){
			ToastUtil.shortToast(LoginseticsActivity.this, "请查看发货人信息是否完整");
			return;
		}
		if(getIntent().getStringExtra("address").equals("") 
				){
			ToastUtil.shortToast(LoginseticsActivity.this, "请查看发货地信息");
			return;
		}
		if(  et_add_tel.getText().toString().equals("")
				|| et_add_name.getText().toString().equals("")
				|| et_goodname.getText().toString().equals("")){
			ToastUtil.shortToast(LoginseticsActivity.this, "请查看信息是否完整");
			return;
		}
//		ToastUtil.shortToast(LoginseticsActivity.this, "请查看high"+ getIntent().getStringExtra("high"));
//		ToastUtil.shortToast(LoginseticsActivity.this, "请查看wide"+ getIntent().getStringExtra("wide"));
//		ToastUtil.shortToast(LoginseticsActivity.this, "请查看length"+ getIntent().getStringExtra("length"));
		try {
			obj.put("userId",
					String.valueOf(PreferencesUtils.getInt(LoginseticsActivity.this, PreferenceConstants.UID)));
			obj.put("sendPerson", et_name.getText().toString());
//			obj.put("mobile", et_tel.getText().toString());
			obj.put("sendPhone", tmpstr);
		
			obj.put("takeName", et_add_name.getText().toString());
//			obj.put("mobileTo", et_add_tel.getText().toString());
			obj.put("takeMobile", tmpstr2);
			obj.put("publshDeviceId", DataTools.getDeviceId(LoginseticsActivity.this));
			
			obj.put("cargoName", et_goodname.getText().toString());
			
			obj.put("startPlace", getIntent().getStringExtra("address"));
			obj.put("entPlace",getIntent().getStringExtra("addressTo"));
			obj.put("entPlaceCityCode", getIntent().getStringExtra("cityCodeTo"));
			obj.put("startPlaceCityCode", getIntent().getStringExtra("cityCode"));
			obj.put("startPlaceTownCode", getIntent().getStringExtra("townCode"));
			if ("2".equals(getIntent().getStringExtra("nochange"))) {
				obj.put("latitude", ""+getIntent().getStringExtra("latitude"));
				obj.put("longitude", getIntent().getStringExtra("longitude"));
//				obj.put("fromLatitude", ""+getIntent().getStringExtra("latitude"));
//				obj.put("fromLongitude", getIntent().getStringExtra("longitude"));
				if ("1".equals(getIntent().getStringExtra("nochangefa"))) {
					obj.put("fromLatitude", ""+getIntent().getDoubleExtra("fromLatitude", 0.0));
					obj.put("fromLongitude", getIntent().getDoubleExtra("fromLongitude", 0.0));
				}else {
					obj.put("fromLatitude",  getIntent().getStringExtra("fromLatitude"));
					obj.put("fromLongitude", getIntent().getStringExtra("fromLongitude"));
				}
			
				if ("1".equals(getIntent().getStringExtra("nochangedi"))) {
					obj.put("latitudeTo", getIntent().getDoubleExtra("toLatitude", 0.0));
					obj.put("longitudeTo", getIntent().getDoubleExtra("toLongitude", 0.0));
					
				}else {
					obj.put("latitudeTo", getIntent().getStringExtra("toLatitude"));
					obj.put("longitudeTo", getIntent().getStringExtra("toLongitude"));
				}
				if ("1".equals(getIntent().getStringExtra("nochange1"))) {
					obj.put("cargoWeight",getIntent().getIntExtra("matWeight", 0)+"公斤");//重量
				}else {
					obj.put("cargoWeight",Integer.parseInt(getIntent().getStringExtra("matWeight"))+"公斤");//重量
				}
				if ("1".equals(getIntent().getStringExtra("nochange2"))) {
					obj.put("cargoSize",  getIntent().getIntExtra("cargoSize",0));
				}else {
					obj.put("cargoSize",  getIntent().getStringExtra("cargoSize"));
				}
			}else {
				obj.put("latitude", ""+getIntent().getDoubleExtra("fromLatitude", 0.0));
				obj.put("longitude", getIntent().getDoubleExtra("fromLongitude", 0.0));
				obj.put("cargoSize",  getIntent().getIntExtra("cargoSize",0));
				obj.put("cargoWeight",getIntent().getIntExtra("matWeight", 0)+getIntent().getStringExtra("matWeightdan"));//重量
				obj.put("fromLatitude", ""+getIntent().getDoubleExtra("fromLatitude", 0.0));
				obj.put("fromLongitude", getIntent().getDoubleExtra("fromLongitude", 0.0));
				obj.put("latitudeTo", getIntent().getDoubleExtra("toLatitude", 0.0));
				obj.put("longitudeTo", getIntent().getDoubleExtra("toLongitude", 0.0));
				
			}
//			obj.put("high",  getIntent().getStringExtra("high"));
//			obj.put("wide",  getIntent().getStringExtra("wide"));
//			obj.put("length",  getIntent().getStringExtra("length")); 
			obj.put("arriveTime",  et_time.getText().toString());
			obj.put("sendPerson", et_name.getText().toString());
			obj.put("sendPhone", tmpstr);
			obj.put("cargoName", et_goodname.getText().toString());//货物名称  l
			

			obj.put("takeCargo", chox);//是否需要取货  up
			obj.put("sendCargo", chox2);//是否需要送货  down
			
			obj.put("takeName", et_add_name.getText().toString());//收货人姓名  
//			obj.put("takeMobile", et_add_tel.getText().toString());//收货电话 
			obj.put("takeMobile", tmpstr2);//收货电话  
			obj.put("appontSpace", et_huodizhi.getText().toString());// 
			obj.put("carType",  getIntent().getStringExtra("carType"));
			obj.put("carName",  getIntent().getStringExtra("matVolume"));
			if (change3==false) {
					if (tiji==0) {
					obj.put("cargoVolume", "1立方米以下");// 
				}else {
					obj.put("cargoVolume", sunmit(1, tiji)+"立方米");// 
				}
			}else {
				obj.put("cargoVolume", getIntent().getStringExtra("cargoVolume"));// 
			}
			
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.e("查看数据", obj.toString());
		dialog.show();
		//PublishNew    LOGISTICS
		AsyncHttpUtils.doPostJson(LoginseticsActivity.this, MCUrl.PublishNew, obj.toString(),
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("11111111111 wwww   ", new String(arg2));
						dialog.dismiss();
						LogisBean bean = new Gson().fromJson(new String(arg2), LogisBean.class);
//						Log.e("oppop", bean.data.toString());
						if(bean.getErrCode()==0){
							ToastUtil.longToast(LoginseticsActivity.this, bean.getMessage());
//							Intent intent = new Intent(LogisticalActivity.this, LogisPayActivity.class);
//						 	   intent.putExtra("evaluationStatus",bean.getData().get(0).evaluationStatus);//余额支付 的价格	
////						 		intent.putExtra("insureCost", String.valueOf(df.format(bean.data.get(0).insureCost)));
//						 		intent.putExtra("evaluationScore", bean.getData().get(0).evaluationScore);//其它支付的价格
//						 		intent.putExtra("billCode", bean.getData().get(0).billCode);//物流单号
//						 		intent.putExtra("way", "1");
//						 		startActivityForResult(intent, 10);
							Intent intent=new Intent();
							intent.setClass(LoginseticsActivity.this, NewMainActivity.class);
							startActivity(intent);
							finish();
						}
						else {
							ToastUtil.shortToast(LoginseticsActivity.this, bean.getMessage());
						}
						
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						dialog.dismiss();
						
					}
				});

	}
	private void addPostResultbao() {
		JSONObject obj = new JSONObject();
		String string =et_tel.getText().toString();
		String string2 =et_add_tel.getText().toString();
		  String tmpstr=string.replace(" ","");
		  String tmpstr2=string2.replace(" ","");
		if (!StringUtil.isMobileNO(tmpstr)
				|| (tmpstr.length() != 11)
						) {
			ToastUtil.shortToast(LoginseticsActivity.this, "请输入正确发件人的手机号码");
			return;
		}
		if (!chox2) {
			if (et_huodizhi.getText().toString().equals("")) {
				ToastUtil.shortToast(LoginseticsActivity.this, "请填写指定物流园区地址");
				return;
			}
			
		}
		if (!StringUtil.isMobileNO(tmpstr2)
				|| (tmpstr2.length() != 11)
						) {
			ToastUtil.shortToast(LoginseticsActivity.this, "请输入正确收件人的手机号码");
			return;
		}
		if( et_name.getText().toString().equals("") 
				){
			ToastUtil.shortToast(LoginseticsActivity.this, "请查看发货人信息是否完整");
			return;
		}
		if(getIntent().getStringExtra("address").equals("") 
				){
			ToastUtil.shortToast(LoginseticsActivity.this, "请查看发货地信息");
			return;
		}
		if(  et_add_tel.getText().toString().equals("")
				|| et_add_name.getText().toString().equals("")
				|| et_goodname.getText().toString().equals("")){
			ToastUtil.shortToast(LoginseticsActivity.this, "请查看信息是否完整");
			return;
		}
//		ToastUtil.shortToast(LoginseticsActivity.this, "请查看high"+ getIntent().getStringExtra("high"));
//		ToastUtil.shortToast(LoginseticsActivity.this, "请查看wide"+ getIntent().getStringExtra("wide"));
//		ToastUtil.shortToast(LoginseticsActivity.this, "请查看length"+ getIntent().getStringExtra("length"));
		try {
			obj.put("userId",
					String.valueOf(PreferencesUtils.getInt(LoginseticsActivity.this, PreferenceConstants.UID)));
			obj.put("sendPerson", et_name.getText().toString());
//			obj.put("mobile", et_tel.getText().toString());
			obj.put("sendPhone", tmpstr);
		
			obj.put("takeName", et_add_name.getText().toString());
//			obj.put("mobileTo", et_add_tel.getText().toString());
			obj.put("takeMobile", tmpstr2);
			obj.put("publshDeviceId", DataTools.getDeviceId(LoginseticsActivity.this));
			
			obj.put("cargoName", et_goodname.getText().toString());
			
			obj.put("startPlace", getIntent().getStringExtra("address"));
			obj.put("entPlace",getIntent().getStringExtra("addressTo"));
			obj.put("entPlaceCityCode", getIntent().getStringExtra("cityCodeTo"));
			obj.put("startPlaceCityCode", getIntent().getStringExtra("cityCode"));
			obj.put("startPlaceTownCode", getIntent().getStringExtra("townCode"));
			if ("2".equals(getIntent().getStringExtra("nochange"))) {
				obj.put("latitude", ""+getIntent().getStringExtra("latitude"));
				obj.put("longitude", getIntent().getStringExtra("longitude"));
//				obj.put("fromLatitude", ""+getIntent().getStringExtra("latitude"));
//				obj.put("fromLongitude", getIntent().getStringExtra("longitude"));
				if ("1".equals(getIntent().getStringExtra("nochangefa"))) {
					obj.put("fromLatitude", ""+getIntent().getDoubleExtra("fromLatitude", 0.0));
					obj.put("fromLongitude", getIntent().getDoubleExtra("fromLongitude", 0.0));
				}else {
					obj.put("fromLatitude",  getIntent().getStringExtra("fromLatitude"));
					obj.put("fromLongitude", getIntent().getStringExtra("fromLongitude"));
				}
			
				if ("1".equals(getIntent().getStringExtra("nochangedi"))) {
					obj.put("latitudeTo", getIntent().getDoubleExtra("toLatitude", 0.0));
					obj.put("longitudeTo", getIntent().getDoubleExtra("toLongitude", 0.0));
					
				}else {
					obj.put("latitudeTo", getIntent().getStringExtra("toLatitude"));
					obj.put("longitudeTo", getIntent().getStringExtra("toLongitude"));
				}
				if ("1".equals(getIntent().getStringExtra("nochange1"))) {
					obj.put("cargoWeight",getIntent().getIntExtra("matWeight", 0)+"公斤");//重量
				}else {
					obj.put("cargoWeight",Integer.parseInt(getIntent().getStringExtra("matWeight"))+"公斤");//重量
				}
				if ("1".equals(getIntent().getStringExtra("nochange2"))) {
					obj.put("cargoSize",  getIntent().getIntExtra("cargoSize",0));
				}else {
					obj.put("cargoSize",  getIntent().getStringExtra("cargoSize"));
				}
			}else {
				obj.put("latitude", ""+getIntent().getDoubleExtra("fromLatitude", 0.0));
				obj.put("longitude", getIntent().getDoubleExtra("fromLongitude", 0.0));
				obj.put("cargoSize",  getIntent().getIntExtra("cargoSize",0));
				obj.put("cargoWeight",getIntent().getIntExtra("matWeight", 0)+"公斤");//重量
				obj.put("fromLatitude", ""+getIntent().getDoubleExtra("fromLatitude", 0.0));
				obj.put("fromLongitude", getIntent().getDoubleExtra("fromLongitude", 0.0));
				obj.put("latitudeTo", getIntent().getDoubleExtra("toLatitude", 0.0));
				obj.put("longitudeTo", getIntent().getDoubleExtra("toLongitude", 0.0));
				
			}
//			obj.put("high",  getIntent().getStringExtra("high"));
//			obj.put("wide",  getIntent().getStringExtra("wide"));
//			obj.put("length",  getIntent().getStringExtra("length")); 
			obj.put("arriveTime",  et_time.getText().toString());
			obj.put("sendPerson", et_name.getText().toString());
			obj.put("sendPhone", tmpstr);
			obj.put("cargoName", et_goodname.getText().toString());//货物名称  l
			

			obj.put("takeCargo", chox);//是否需要取货  up
			obj.put("sendCargo", chox2);//是否需要送货  down
			
			obj.put("takeName", et_add_name.getText().toString());//收货人姓名  
//			obj.put("takeMobile", et_add_tel.getText().toString());//收货电话 
			obj.put("takeMobile", tmpstr2);//收货电话  
			obj.put("appontSpace", et_huodizhi.getText().toString());// 
			obj.put("carType",  getIntent().getStringExtra("carType"));
			obj.put("carName",  getIntent().getStringExtra("matVolume"));
			if (change3==false) {
					if (tiji==0) {
					obj.put("cargoVolume", "1立方米以下");// 
				}else {
					obj.put("cargoVolume", sunmit(1, tiji)+"立方米");// 
				}
			}else {
				obj.put("cargoVolume", getIntent().getStringExtra("cargoVolume"));// 
			}
			
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.e("查看数据", obj.toString());
		dialog.show();
		//PublishNew    LOGISTICS
		AsyncHttpUtils.doPostJson(LoginseticsActivity.this, MCUrl.publishInsure, obj.toString(),
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("11111111111 wwww   ", new String(arg2));
						dialog.dismiss();
						LogisBean bean = new Gson().fromJson(new String(arg2), LogisBean.class);
//						Log.e("oppop", bean.data.toString());
						if(bean.getErrCode()==0){
							ToastUtil.longToast(LoginseticsActivity.this, bean.getMessage());
//							Intent intent = new Intent(LogisticalActivity.this, LogisPayActivity.class);
//						 	   intent.putExtra("evaluationStatus",bean.getData().get(0).evaluationStatus);//余额支付 的价格	
////						 		intent.putExtra("insureCost", String.valueOf(df.format(bean.data.get(0).insureCost)));
//						 		intent.putExtra("evaluationScore", bean.getData().get(0).evaluationScore);//其它支付的价格
//						 		intent.putExtra("billCode", bean.getData().get(0).billCode);//物流单号
//						 		intent.putExtra("way", "1");
//						 		startActivityForResult(intent, 10);
							Intent intent=new Intent();
							intent.setClass(LoginseticsActivity.this, InsureActivity.class);
							intent.putExtra("recId", bean.getData().get(0).recId);
							startActivity(intent);
							finish();
						}
						else {
							ToastUtil.shortToast(LoginseticsActivity.this, bean.getMessage());
						}
						
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
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
				ContentResolver reContentResolverol = LoginseticsActivity.this.getContentResolver();
				Uri contactData = data.getData();
				// 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
				Cursor cursor = LoginseticsActivity.this.managedQuery(contactData, null, null, null, null);
				cursor.moveToFirst();
				if (cursor.moveToFirst()) {
				// 获得DATA表中的名字
				String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				// 条件为联系人ID
				et_name.setText(name);
//				namese=name;
				 String[] contact = new String[2];
                // 查看联系人有多少个号码，如果没有号码，返回0
				int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
				String[] strs = cursor.getColumnNames();
	            for (int i = 0; i < strs.length; i++) {
	                if (strs[i].equals("data1")) {
	                    ///手机号
	                    contact[1] = cursor.getString(cursor.getColumnIndex(strs[i])).replace("+86","").replaceAll(" ","").trim();
	                    et_tel.setText(""+contact[1]);
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
	                new AlertDialog.Builder(LoginseticsActivity.this)  
	                .setMessage("app需要开启读取联系人权限")  
	                .setPositiveButton("设置", new DialogInterface.OnClickListener() {  
	                    @Override  
	                    public void onClick(DialogInterface dialogInterface, int i) {  
	                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);  
	                        intent.setData(Uri.parse("package:" + LoginseticsActivity.this.getPackageName()));  
	                        startActivity(intent);  
	                    }  
	                })  
	                .setNegativeButton("取消", null)  
	                .create()  
	                .show();  
	                  
	            }   
			}

			break;
		case 9:// 增加手机收件人联系人的回调
			if (data == null) {
				return;
			}
			if (resultCode == Activity.RESULT_OK) {
				ContentResolver reContentResolverol = LoginseticsActivity.this.getContentResolver();
				Uri contactData = data.getData();
				// 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
				Cursor cursor =  LoginseticsActivity.this.managedQuery(contactData, null, null, null, null);
//				cursor.moveToFirst();
				if (cursor.moveToFirst()) {
				// 获得DATA表中的名字
				String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				// 条件为联系人ID
				et_add_name.setText(name);
				
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
		                    et_add_tel.setText(""+contact[1]);
		                }
		            }
				if(Build.VERSION.SDK_INT < 14) {
				    cursor.close();
				   }
//				phone.close();
			}else{  
				 // 没有权限，跳到设置界面，调用Android系统“应用程序信息（Application Info）”界面  
                new AlertDialog.Builder(LoginseticsActivity.this)  
                .setMessage("app需要开启读取联系人权限")  
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {  
                    @Override  
                    public void onClick(DialogInterface dialogInterface, int i) {  
                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);  
                        intent.setData(Uri.parse("package:" + LoginseticsActivity.this.getPackageName()));  
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
			break;
		
		}
		}
	public int sunmit(int a ,int b){
		int sum=a+b;
		return sum;
	}
}
