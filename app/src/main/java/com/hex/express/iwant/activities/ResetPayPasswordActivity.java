package com.hex.express.iwant.activities;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.SmsBean;
import com.hex.express.iwant.constance.CommonConstants;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.DataTools;
import com.hex.express.iwant.utils.MD5Util;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.StringUtil;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class ResetPayPasswordActivity extends BaseActivity{
	
	private EditText et_setupPayCode,et_confirmPayCode;
	private Button btn_DONE;
	private TitleBarView tbv_show;
	
	 private SharedPreferences preferences;
	 private Editor editor;
	 String payCode01;
	private TextView tv_checkID,huangphone;
	private LinearLayout ll_checkID;
	private EditText et_ID,edt_registecode;
	private Button btn_getsmscode;
	String ID_number;
	
	
	private Timer timer;
	private TimerTask task;
	private int currentTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.activity_setuppassword);
		initView();
		initData();
	}
	
	     private void resetCurrentTime() {
	 		currentTime = CommonConstants.CODETIME;
	 	}

	 	public void handleUiMessage(Message msg) {
	 		super.handleUiMessage(msg);
	 		switch (msg.what) {
	 		case MsgConstants.MSG_01:
	 			if (currentTime == 0) {
	 				resetCurrentTime();
	 				btn_getsmscode.setText("获取验证码");
	 				task.cancel();
	 			} else
	 				btn_getsmscode.setText(currentTime-- + "秒");
	 			// btn_getsmscode.setClickable(false);
	 			break;

	 		/*
	 		 * case MsgConstants.MSG_02: etCode.setText(""); currentTime = 0; break;
	 		 */
	 		}
	 	}

	     
	     
	     
	     /**
	 	 * 重置支付密码:
	 	 */
	 	private void postResetPayPassword() {
	 		JSONObject obj = new JSONObject();
	 		if ("".equals(edt_registecode.getText().toString())) {
	 			ToastUtil.shortToast(ResetPayPasswordActivity.this, "验证码不能为空");
				return;
			}
	 		try {
	 			obj.put("userId", Integer.valueOf(PreferencesUtils.getInt(
	 					getApplicationContext(), PreferenceConstants.UID)));
	 			obj.put("payPassword", MD5Util.MD5Encode(payCode01));
	 			obj.put("smsCode",edt_registecode.getText().toString());
//	 			Log.e("json", "++++++" + obj.toString());
	 		} catch (JSONException e) {
	 			e.printStackTrace();
	 		}
//	 		Log.e("json", "_____" + MCUrl.MYPASSWORD);
	 		AsyncHttpUtils.doPostJson(ResetPayPasswordActivity.this, MCUrl.upDatePaypassword,
	 				obj.toString(), new AsyncHttpResponseHandler() {

	 					@Override
	 					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
	 							Throwable arg3) {
//	 						dialog.dismiss();
	 					}

	 					@Override
	 					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
	 						Log.e("json22", new String(arg2));
//	 						dialog.dismiss();
	 						BaseBean bean = new Gson().fromJson(new String(arg2),
	 								BaseBean.class);
	 						if (bean.isSuccess()) {
//	 							AlertDialog.Builder ad = new Builder(MypassActivity.this);
//	 							ad.setTitle("温馨提示");
//	 							ad.setMessage("转账成功");
//	 							ad.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//	 								public void onClick(DialogInterface dialog, int which) {
//	 									Intent intent = new Intent(MypassActivity.this,
//	 											MainActivity.class);
//	 									startActivity(intent);
//	 									finish();
//	 									
//	 									ToastUtil.shortToast(getApplicationContext(), "转账成功");
	 							
//	 								}
//	 							}); 
//	 							ad.create().show();
	 							
//	 							PostRequstMoney();
	 							
	 							//设置的支付密码存储到本地;
	 							Log.e("mima", MD5Util.MD5Encode(payCode01));
	 							PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.PAYPASSWORD,MD5Util.MD5Encode(payCode01));
	 							
//	 							PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.PAYPASSWORD).equals("");
	 							Toast.makeText(getApplicationContext(), "设置成功!",Toast.LENGTH_SHORT).show();
	 							
	 							finish();
//	 							startActivity(new Intent(getApplicationContext(), MypassActivity.class));
	 							
	 						} else {
	 							ToastUtil.shortToast(ResetPayPasswordActivity.this,
	 									bean.getMessage());
	 						}

	 					}

	 				});

	 	}

		@Override
		public void onWeightClick(View v) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void initData() {
			// TODO Auto-generated method stub
			timer = new Timer();
			// edt_registephonenumber.getText().toString()
			resetCurrentTime();
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
		protected void onDestroy() {
			super.onDestroy();
			if (timer != null)
				timer.cancel();
			if (task != null)
				task.cancel();
		}

		@Override
		public void initView() {
			// TODO Auto-generated method stub

	    	// 标题
	 	tbv_show = (TitleBarView) findViewById(R.id.tbv_show);
	 	tbv_show.setTitleText("重置支付密码");
	 	
	 	//身份证号码验证：
	 	tv_checkID = (TextView) findViewById(R.id.tv_checkID);
	 	huangphone = (TextView) findViewById(R.id.huangphone);
	 	ll_checkID = (LinearLayout) findViewById(R.id.ll_checkID);
	 	 et_ID= (EditText) findViewById(R.id.et_ID);
	 	edt_registecode= (EditText) findViewById(R.id.edt_registecode);
	 	 
	 	//设置为可见：
//	 	tv_checkID.setVisibility(View.VISIBLE);
//	 	ll_checkID.setVisibility(View.VISIBLE);
	 	
		et_setupPayCode = (EditText) findViewById(R.id.edt_setupPayCode);
		et_confirmPayCode = (EditText) findViewById(R.id.et_confirmPayCode);
		btn_DONE = (Button) findViewById(R.id.btn_DONE);
		btn_getsmscode = (Button) findViewById(R.id.btn_getsmscode);
		
		btn_DONE.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ID_number = et_ID.getText().toString();
				payCode01 = et_setupPayCode.getText().toString();
				String payCode02 = et_confirmPayCode.getText().toString();
				String toastContent;
				
//				if ("".equals(ID_number)) {
//					toastContent = "请填写在身份证号码!";
//					Toast.makeText(getApplicationContext(), toastContent,Toast.LENGTH_SHORT).show();
//					} else 
						if ("".equals(payCode01)&&"".equals(payCode02)) {
					toastContent = "请输入您要设定的6位支付密码!";
					Toast.makeText(getApplicationContext(), toastContent,Toast.LENGTH_SHORT).show();
				} else if(!payCode01.equals(payCode02)){
					toastContent = "两次输入的支付密码不一致!";
					Toast.makeText(getApplicationContext(), toastContent,Toast.LENGTH_SHORT).show();
				}else if(payCode01.equals(payCode02)){
					postResetPayPassword();
				}
				
			}
		});
		
		btn_getsmscode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				String username = String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.MOBILE));
//				if (!StringUtil.isMobileNO(username) || (username.length() != 11)) { // 判断是否是正确的手机号码
//					// Log.e("bbb",use)
//					ToastUtil.shortToastByRec(ResetPayPasswordActivity.this,
//							R.string.mobileisnotvolid);
//					return;
//				} else
					if (currentTime == CommonConstants.CODETIME) {
//					PreferencesUtils.putString(getApplicationContext(),
//							PreferenceConstants.MOBILE, username);
					task = new TimerTask() {
						@Override
						public void run() {
							sendEmptyUiMessage(MsgConstants.MSG_01);
						}
					};
					timer.schedule(task, 0, 1000);
//					final String smsCode = edt_registecode.getText().toString()
//							.trim();
					Log.e("sms", "codddddd");
//					String id = DataTools.getDeviceId(getApplicationContext());
//					obj.put("deviceId", id);
//					Log.e("sms", ""+(UrlMap.getTwo(MCUrl.getSmscode,
//							PreferenceConstants.MOBILE, username,"deviceId",id)));
					AsyncHttpUtils.doSimGet(UrlMap.getUrl(MCUrl.getSmscode,
							 "userId",String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID))),
							new AsyncHttpResponseHandler() {

								@Override
								public void onSuccess(int arg0, Header[] arg1,
										byte[] arg2) {
									// TODO Auto-generated method stub
									SmsBean bean = new Gson().fromJson(new String(
											arg2), SmsBean.class);
									Log.e("sims",
											bean.getMessage() + bean.getErrCode());
									if (bean.getErrCode() == 0) {
										ToastUtil.shortToast(
												getApplicationContext(),
												bean.getMessage());
									}else {
										ToastUtil.shortToast(
												getApplicationContext(),
												bean.getMessage());
									}
								}

								@Override
								public void onFailure(int arg0, Header[] arg1,
										byte[] arg2, Throwable arg3) {
									Log.e("ccc", arg0 + "");
								}
							});
				}
			}
		});
		huangphone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(ResetPayPasswordActivity.this, RevisePhoneActivity.class));
			}
		});
		}
	     
}