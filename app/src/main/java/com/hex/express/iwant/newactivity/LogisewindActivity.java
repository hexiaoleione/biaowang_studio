package com.hex.express.iwant.newactivity;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.R.id;
import com.hex.express.iwant.activities.BaseActivity;
import com.hex.express.iwant.activities.DownWindPayActivity;
import com.hex.express.iwant.activities.HWdvertActivity;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.DowInsuraBean;
import com.hex.express.iwant.bean.DownSpecialBean;
import com.hex.express.iwant.bean.RegisterBean;
import com.hex.express.iwant.bean.ShopBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.newsmain.NewMainActivity;
import com.hex.express.iwant.utils.DataTools;
import com.hex.express.iwant.utils.Logger;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.StringUtil;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 
 * @author huyichuan
 *顺风
 */
public class LogisewindActivity extends BaseActivity{

		// 寄件人信息
			@Bind(id.et_name)
			EditText et_name;//寄件人姓名
			@Bind(id.et_tel)
			EditText et_tel;//寄件人电话
//			 收件人信息
			@Bind(id.et_add_name)
			EditText et_add_name;
			@Bind(id.et_add_tel)
			EditText et_add_tel;
			@Bind(id.et_goodname)//物品名字
			EditText et_goodname;
			
			//是否买保险
			@Bind(id.checkbox_bao)
			CheckBox checkbox_bao;
			
			@Bind(id.edt_price)
			EditText edt_price;
			//是否代收款
			@Bind(id.checkbox_diashou)
			CheckBox checkbox_diashou;
			@Bind(id.edt_daipric)
			EditText edt_daipric;
			@Bind(id.et_ramk)//备注
			EditText et_ramk;
			@Bind(id.btn_sumit)//tijao
			Button btn_sumit;
			 @Bind(id.layout_iv_add)
				LinearLayout layout_iv_add;
			 @Bind(id.layout_iv_add_receiver)
				LinearLayout layout_iv_add_receiver;
			 @Bind(id.topButton)
				ImageView topButton;
			 @Bind(id.et_tijisp)
				Spinner et_tijisp;
			 
			 @Bind(id.toubaoxuzhi)
			 TextView toubaoxuzhi;
			 
			 @Bind(id.lenbie)
			 RelativeLayout lenbie;
			 
			 @Bind(id.checkbox_baozong)
				CheckBox checkbox_baozong;
			 @Bind(id.checkbox_baojiben)
				CheckBox checkbox_baojiben;
			 
			 
			 int kesse=0;
			private boolean chox= false; 
			private boolean chox2= false; 
			private boolean wuqian= false; 
			PopupWindow window02;
			PopupWindow window03;
			DowInsuraBean	adbean;
			private String tou;
			DownSpecialBean	 bean;
			
			  private boolean namesb=false; 
			  private CharSequence temp;
				 private int editStart ; 
				  private int editEnd ;
				  
				  private int  xian=0;
					private boolean wwww1=false;
					private boolean wwww2=true;
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logiswind_activity);
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
						String.valueOf(PreferencesUtils.getInt(LogisewindActivity.this, PreferenceConstants.UID))),
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
							if ("1".equals(bean.getData().get(0).shopType)) {
								 et_tel.setText(bean.data.get(0).mobile);
								RequestParams params2 = new RequestParams();
							
								AsyncHttpUtils.doGet(
										UrlMap.getUrl(MCUrl.getChapman, "userId",
												String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID))),
										null, null, params2, new AsyncHttpResponseHandler() {
//									@Override
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
//							if ("2".equals(getIntent().getStringExtra("nochange"))) {
//								et_name.setText( getIntent().getStringExtra("personName"));
//								et_tel.setText( getIntent().getStringExtra("mobile"));
//							}else {
//								 et_name.setText(bean.data.get(0).userName);
//								 et_tel.setText(bean.data.get(0).mobile);
//							}
							
					}
						
					}

				});
	}
	@Override
	public void initView() {
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
		
		if ("2".equals(getIntent().getStringExtra("nochange"))) {
			et_name.setText( getIntent().getStringExtra("personName"));
			et_tel.setText( getIntent().getStringExtra("mobile"));
			et_add_name.setText( getIntent().getStringExtra("personNameTo"));
			et_add_tel.setText( getIntent().getStringExtra("mobileTo"));
			et_goodname.setText( getIntent().getStringExtra("matName"));
			
			et_ramk.setText(getIntent().getStringExtra("matRemark"));
			if ("Y".equals(getIntent().getStringExtra("whether"))) {
				edt_price.setText(getIntent().getStringExtra("premium"));
				checkbox_bao.setChecked(true);
				edt_price.setEnabled(true);
				chox=true;
				
			}else {
				checkbox_bao.setChecked(false);
				edt_price.setEnabled(false);
			}
			if (!"false".equals(getIntent().getStringExtra("ifReplaceMoney"))) {
				edt_daipric.setText(getIntent().getStringExtra("replaceMoney"));
				checkbox_diashou.setChecked(true);
				edt_daipric.setEnabled(true);
				chox2=true;
			}else {
				checkbox_diashou.setChecked(false);
				edt_daipric.setEnabled(false);
			}
//			intent.putExtra("replaceMoney", getIntent().getStringExtra("replaceMoney"));
//			intent.putExtra("ifReplaceMoney", getIntent().getStringExtra("ifReplaceMoney"));
//			intent.putExtra("ifTackReplace", getIntent().getStringExtra("ifTackReplace"));
//			intent.putExtra("insureCost", getIntent().getStringExtra("insureCost"));
		}else {
			edt_price.setEnabled(false);
			edt_daipric.setEnabled(false);
		}
		
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		 toubaoxuzhi.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent=new Intent();
					intent.putExtra("name", "1");
//					intent.putExtra("url", "http://www.efamax.com/mobile/InsureSF.html");
//					insureUrl
					try {
						intent.putExtra("url", getIntent().getStringExtra("insureUrl"));
					} catch (Exception e) {
						// TODO: handle exception
						intent.putExtra("url", "http://www.efamax.com/mobile/InsureSF.html");
					}
					
					intent.setClass(LogisewindActivity.this, HWdvertActivity.class);
					startActivity(intent);
				}
			});
		 
		checkbox_bao.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
			if(arg1){
				chox=arg1;
//				edt_price.setVisibility(View.VISIBLE);
//			   	v_pp.setVisibility(View.VISIBLE);
			   			showPaywindow();
			   			edt_price.setEnabled(arg1);
//			}
			}else {
				chox=arg1;
//				edt_price.setVisibility(View.GONE);
//			   	v_pp.setVisibility(View.GONE);
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
//				edt_price.setVisibility(View.VISIBLE);
//			   	v_pp.setVisibility(View.VISIBLE);
				showPaywindowdai();
				edt_daipric.setEnabled(arg1);
//			}
			}else {
				chox2=arg1;
//				edt_price.setVisibility(View.GONE);
//			   	v_pp.setVisibility(View.GONE);
				edt_daipric.setEnabled(arg1);
				edt_daipric.setText("");
			}
			}
		});
		btn_sumit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				addPostResult();
			}
		});
		final String[] myItems2 = getResources().getStringArray(
				R.array.leibie);
		final ArrayAdapter<String> adapter11 = new ArrayAdapter<String>(LogisewindActivity.this,
				android.R.layout.simple_spinner_item, myItems2);
		adapter11.setDropDownViewResource(R.layout.dor_wu); 
		et_tijisp.setAdapter(adapter11);  
		et_tijisp.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				kesse=position;
				if (position!=0) {
					checkbox_baozong.setVisibility(View.GONE);
					checkbox_baozong.setChecked(false);
				}else {
					checkbox_baozong.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	checkbox_baozong.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
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
					checkbox_baozong.setChecked(false);
					xian=1;
 				}else {
 					xian=0;
 				}
				wwww2=arg1;
			}
		});
	}

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
		layout_iv_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent1 = new Intent(Intent.ACTION_PICK);
				intent1.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
				startActivityForResult(intent1, 0);
			}
		});
		layout_iv_add_receiver.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent21 = new Intent(Intent.ACTION_PICK);
				intent21.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
				startActivityForResult(intent21, 9);
			}
		});
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
					if (temp.length()>1) {
//						ToastUtil.shortToast(getActivity(), "保额不能超过5000");
//						editable.delete(editStart-1, editEnd); 
						    int tempSelection = editStart; 
				 if (Integer.valueOf(editable.toString())>5000) {
						    	 et_tijisp.setVisibility(View.VISIBLE);
								    lenbie .setVisibility(View.VISIBLE);
								    wuqian=true;
						}else {
							et_tijisp.setVisibility(View.GONE);
						    lenbie .setVisibility(View.GONE);
						    wuqian=false;
						}
						   
					}else {
						wuqian=false;
					}
				}
			});
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 显示投保提示信息
	 */
	private void showPaywindowdai() {
		
		TextView btnsaves_pan,tet_tishi;
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) this. getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		window02.showAtLocation(LogisewindActivity.this.findViewById(id.checkbox_bao), Gravity.CENTER, 0, 0);
		btnsaves_pan=(TextView) view.findViewById(id.btnsaves_pan);
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
//				addPostResult();
			}
		});

	}
	
	/**
	 * 数据请求
	 */
private void addPostResult() {
		String money = null;
		JSONObject obj = new JSONObject();
//		if (!flag) {
//			ToastUtil.shortToast(getActivity(), "请上传物品照片");
//			return;
//		}
		if (chox) {
			if (edt_price.getText().toString().equals("")) {
				ToastUtil.shortToast(LogisewindActivity.this, "请填写投保费用");
				return;
			}
		}
		if (chox2) {
			if (edt_daipric.getText().toString().equals("")) {
				ToastUtil.shortToast(LogisewindActivity.this, "请填写代收费用");
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
			ToastUtil.shortToast(LogisewindActivity.this, "请输入正确的手机号码");
			return;
		}
		if (et_add_name.getText().toString().equals("") || et_add_tel.getText().toString().equals("")
				|| et_name.getText().toString().equals("") || et_tel.getText().toString().equals("")
				|| et_goodname.getText().toString().equals("") 
				) {
			ToastUtil.shortToast(LogisewindActivity.this, "请完善信息");
			return;
		}
		if (wuqian) {
				if ( !wwww2 ) {
				ToastUtil.shortToast( LogisewindActivity.this, "请选择承险种类");
				return ;
			}
				
		}
		
		try {
			obj.put("userId",
					String.valueOf(PreferencesUtils.getInt(LogisewindActivity.this, PreferenceConstants.UID)));
			
			obj.put("personName", et_name.getText().toString());
//			obj.put("mobile", et_tel.getText().toString());
			obj.put("mobile", tmpstr);
		
			obj.put("personNameTo", et_add_name.getText().toString());
//			obj.put("mobileTo", et_add_tel.getText().toString());
			obj.put("mobileTo", tmpstr2);
			obj.put("publshDeviceId", DataTools.getDeviceId(LogisewindActivity.this));
			
			obj.put("matName", et_goodname.getText().toString());
			obj.put("matRemark", et_ramk.getText().toString());
			obj.put("latitude", getIntent().getDoubleExtra("latitude", 0.0));
			obj.put("longitude", getIntent().getDoubleExtra("longitude", 0.0));
			
				if ("2".equals(getIntent().getStringExtra("nochange"))) {
					if ("1".equals(getIntent().getStringExtra("nochangefa"))) {
						obj.put("fromLatitude", ""+getIntent().getDoubleExtra("fromLatitude", 0.0));
						obj.put("fromLongitude", getIntent().getDoubleExtra("fromLongitude", 0.0));
					}else {
						obj.put("fromLatitude",  getIntent().getStringExtra("fromLatitude"));
						obj.put("fromLongitude", getIntent().getStringExtra("fromLongitude"));
					}
					
//					obj.put("fromLatitude", ""+getIntent().getStringExtra("fromLatitude"));
//					obj.put("fromLongitude", getIntent().getStringExtra("fromLongitude"));
					if ("1".equals(getIntent().getStringExtra("nochangedi"))) {
						obj.put("toLatitude", getIntent().getDoubleExtra("toLatitude", 0.0));
						obj.put("toLongitude", getIntent().getDoubleExtra("toLongitude", 0.0));
						
					}else {
						obj.put("toLatitude", getIntent().getStringExtra("toLatitude"));
						obj.put("toLongitude", getIntent().getStringExtra("toLongitude"));
					}
				
//					obj.put("toLatitude", getIntent().getStringExtra("toLatitude"));
//					obj.put("toLongitude", getIntent().getStringExtra("toLongitude"));
//					obj.put("cargoSize",  getIntent().getStringExtra("cargoSize"));
//					obj.put("matWeight",Integer.parseInt(getIntent().getStringExtra("matWeight")));//重量
					if ("1".equals(getIntent().getStringExtra("nochange1"))) {
						obj.put("matWeight",getIntent().getIntExtra("matWeight", 0));//重量
					}else {
						obj.put("matWeight",Integer.parseInt(getIntent().getStringExtra("matWeight")));//重量
					}
					if ("1".equals(getIntent().getStringExtra("nochange2"))) {
						obj.put("cargoSize",  getIntent().getIntExtra("cargoSize",0));
					}else {
						obj.put("cargoSize",  getIntent().getStringExtra("cargoSize"));
					}
				}else {
				obj.put("fromLatitude", ""+getIntent().getDoubleExtra("fromLatitude", 0.0));
				obj.put("fromLongitude", getIntent().getDoubleExtra("fromLongitude", 0.0));
				obj.put("toLatitude", getIntent().getDoubleExtra("toLatitude", 0.0));
				obj.put("toLongitude", getIntent().getDoubleExtra("toLongitude", 0.0));
				obj.put("matWeight",getIntent().getIntExtra("matWeight", 0));//重量
				obj.put("cargoSize",  getIntent().getIntExtra("cargoSize",0));
				
			}
				
				
			obj.put("cityCodeTo", getIntent().getStringExtra("cityCodeTo"));
//			obj.put("fromLatitude", ""+getIntent().getDoubleExtra("fromLatitude", 0.0));
//			obj.put("fromLongitude", getIntent().getDoubleExtra("fromLongitude", 0.0));
			obj.put("cityCode", getIntent().getStringExtra("cityCode"));
			obj.put("townCode", getIntent().getStringExtra("townCode"));
			obj.put("address", getIntent().getStringExtra("address"));
			obj.put("addressTo",getIntent().getStringExtra("addressTo"));
			obj.put("weatherId",getIntent().getStringExtra("weatherId"));
			obj.put("temp",getIntent().getIntExtra("temp", 0));
			
//			obj.put("addressTo", et_address.getText().toString());
//			obj.put("cityCodeTo", getIntent().getStringExtra("cityCodeTo"));
//			obj.put("toLatitude", getIntent().getDoubleExtra("toLatitude", 0.0));
//			obj.put("toLongitude", getIntent().getDoubleExtra("toLongitude", 0.0));
		
//			obj.put("high",  getIntent().getStringExtra("high"));
//			obj.put("wide",  getIntent().getStringExtra("wide"));
//			obj.put("length",  getIntent().getStringExtra("length"));
			obj.put("useTime",  getIntent().getStringExtra("limitTime"));
			
			obj.put("carType",  getIntent().getStringExtra("carType"));
			obj.put("matVolume",  getIntent().getStringExtra("matVolume"));
			if (wuqian) {
//				if(sunmit(1, kesse)==4){
//					obj.put("category",  ""+4);
//				}else {
					obj.put("category",  sunmit(1, kesse));
//				}
					  obj.put("insurance", xian);
//					if ( !wwww2 ) {
					  ToastUtil.shortToast( LogisewindActivity.this, "请选择承险种类");
//					return;
//				}
			}else {
				obj.put("category",  "");
			}
			
			
//			sunmit
			
			
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
//		AsyncHttpUtils.doPostJson(getActivity(), MCUrl.DOWNWINDTASKPUBLISHS, obj.toString(),
		AsyncHttpUtils.doPostJson(LogisewindActivity.this, MCUrl.PUblishTaskNew, obj.toString(),	
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("oppo", new String(arg2));
						dialog.dismiss();
							 bean = new Gson().fromJson(new String(arg2), DownSpecialBean.class);
						Log.e("oppop", bean.data.toString());
						if (bean.getErrCode() == 0) {
							DecimalFormat df = new DecimalFormat("######0.00");
//								ToastUtil.shortToast(LogisewindActivity.this, bean.getMessage());
//								ToastUtil.shortToast(LogisewindActivity.this, "money  "+bean.data.get(0).transferMoney);
								Intent intent = new Intent(LogisewindActivity.this, DownWindPayActivity.class);
								intent.putExtra("money", bean.data.get(0).transferMoney);
								intent.putExtra("recId", bean.data.get(0).recId);
								
								intent.putExtra("payUserId", "");
//								intent.putExtra("insureCost", String.valueOf(df.format(bean.data.get(0).insureCost)));
								intent.putExtra("insureCost", bean.data.get(0).insureCost);
								intent.putExtra("billCode", bean.data.get(0).billCode);
								intent.putExtra("distance", bean.data.get(0).distance);
								intent.putExtra("baofei", edt_price.getText().toString());
								intent.putExtra("daishou",edt_daipric.getText().toString());
								intent.putExtra("tyy","顺路送");
								intent.putExtra("carType", bean.data.get(0).carType);
							Logger.e("billCode数据", bean.data.get(0).billCode);
								startActivityForResult(intent, 10);
//							}
							
						}else {
							ToastUtil.shortToast(LogisewindActivity.this, bean.getMessage());
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						dialog.dismiss();
					}
				});

	}
	/**
	 * 显示投保提示信息
	 */
	private void showPaywindow() {
		
		TextView btnsaves_pan,tet_tishi;
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) this. getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		window02.showAtLocation(LogisewindActivity.this.findViewById(id.checkbox_bao), Gravity.CENTER, 0, 0);
		btnsaves_pan=(TextView) view.findViewById(id.btnsaves_pan);
		tet_tishi=(TextView) view.findViewById(id.tet_tishi);
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
				ContentResolver reContentResolverol = LogisewindActivity.this.getContentResolver();
				Uri contactData = data.getData();
				// 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
				Cursor cursor = LogisewindActivity.this.managedQuery(contactData, null, null, null, null);
				cursor.moveToFirst();
				if (cursor.moveToFirst()) {
					namesb=true;
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
	                new AlertDialog.Builder(LogisewindActivity.this)  
	                .setMessage("app需要开启读取联系人权限")  
	                .setPositiveButton("设置", new DialogInterface.OnClickListener() {  
	                    @Override  
	                    public void onClick(DialogInterface dialogInterface, int i) {  
	                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);  
	                        intent.setData(Uri.parse("package:" + LogisewindActivity.this.getPackageName()));  
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
				ContentResolver reContentResolverol = LogisewindActivity.this.getContentResolver();
				Uri contactData = data.getData();
				// 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
				Cursor cursor =  LogisewindActivity.this.managedQuery(contactData, null, null, null, null);
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
                new AlertDialog.Builder(LogisewindActivity.this)  
                .setMessage("app需要开启读取联系人权限")  
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {  
                    @Override  
                    public void onClick(DialogInterface dialogInterface, int i) {  
                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);  
                        intent.setData(Uri.parse("package:" + LogisewindActivity.this.getPackageName()));  
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
			if (data!=null) {
				if (resultCode == Activity.RESULT_OK) {
					if (!"".equals(data.getStringExtra("type")) && data.getStringExtra("type").equals("3") ) {
//						finish();	
						shoudairen();
					}else {
						if (!"".equals(data.getStringExtra("type")) && data.getStringExtra("type").equals("2")) {
							getrequst(bean.data.get(0).recId);
						}else if (!"".equals(data.getStringExtra("type")) && data.getStringExtra("type").equals("1")) {
							
						}
						else {
							Intent intent=new Intent();
							intent.setClass(LogisewindActivity.this, NewMainActivity.class);
							startActivity(intent);
							finish();
						}
						
						
						
						
					
					}
				}
				
			}
			
			break;
		
		}
		}
	/**
	 * 显示投保提示信息
	 */
	private void shoudairen() {

	        LayoutInflater inflater = LayoutInflater.from(this);  
	        final View textEntryView = inflater.inflate(  
	                R.layout.popwidndow_daifu, null);  
	        final EditText edtInput=(EditText)textEntryView.findViewById(id.edtInput);
	        final AlertDialog.Builder builder = new AlertDialog.Builder(this);  
	        builder.setCancelable(false);  
	        builder.setTitle("请输入代付人手机号");  
	        builder.setView(textEntryView);  
	        builder.setPositiveButton("确认",  
	                new DialogInterface.OnClickListener() {  
	                    public void onClick(DialogInterface dialog, int whichButton) {  
//	                        setTitle(edtInput.getText());  
	                        getHttpRequst(edtInput.getText().toString());
	                    }  
	                });  
	        builder.setNegativeButton("取消",  
	                new DialogInterface.OnClickListener() {  
	                    public void onClick(DialogInterface dialog, int whichButton) {  
	                        setTitle("");  
	                    }  
	                });  
	        builder.show();  
		
	}
	/**
	 * 获取网络数据  
	 */
	@SuppressWarnings("unchecked")
	private void getHttpRequst(String phone) {
		String url = UrlMap.getTwo(MCUrl.replace, "recId", ""+bean.data.get(0).recId,
				"mobile", phone);
//		dialog.show();
		AsyncHttpUtils.doSimGet(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.e("sdds ", new String(arg2));
//				dialog.dismiss();
			BaseBean baseBean=new Gson().fromJson(new String(arg2), BaseBean.class);
			if (baseBean.getErrCode()==0) {
				ToastUtil.shortToast(LogisewindActivity.this, baseBean.getMessage());
				Intent intent=new Intent();
				intent.setClass(LogisewindActivity.this, NewMainActivity.class);
				startActivity(intent);
				finish();
			}else {
				ToastUtil.shortToast(LogisewindActivity.this, baseBean.getMessage());
			}

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
			}
		});
	}
	public int sunmit(int a ,int b){
		int sum=a+b;
		return sum;
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
							ToastUtil.longToast(LogisewindActivity.this, beans.getMessage());
						}
						Intent intent=new Intent();
						intent.setClass(LogisewindActivity.this, NewMainActivity.class);
						startActivity(intent);
						finish();
					}

				});
	}
}
