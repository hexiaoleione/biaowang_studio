package com.hex.express.iwant.activities;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.LogisBean;
import com.hex.express.iwant.bean.SmsBean;
import com.hex.express.iwant.constance.CommonConstants;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.IDUtils;
import com.hex.express.iwant.utils.MD5Util;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.StringUtil;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * 
 * @author huyichuan
 *  修改手机号
 */
public class RevisePhoneActivity extends BaseActivity{

	@Bind(R.id.et_name)
	EditText idcard;  //身份证号
	@Bind(R.id.et_name3)
	EditText oldphone;//手机号
	@Bind(R.id.et_name4)
	EditText edtpassord;//  密码
	@Bind(R.id.et_name2)
	EditText newphone;// 新的手机号
	@Bind(R.id.edt_registecode)
	EditText edt_registecode;// 验证码
	@Bind(R.id.btn_getsmscode)
	Button btn_getsmscode;// 验证码按钮
	@Bind(R.id.btn_sumit)
	Button btn_sumit;// 验证码按钮
	@Bind(R.id.btnLeft)
	ImageView btnLeft;
	private Timer timer;
	private TimerTask task;
	private int currentTime;
	private String phone_number;
	private String code;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_revisephone);
		ButterKnife.bind(RevisePhoneActivity.this);
		getData();
		initView();
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
		btnLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		// TODO Auto-generated method stub
		btn_getsmscode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String username = newphone.getText().toString()
						.trim();
				if (!StringUtil.isMobileNO(username) || (username.length() != 11)) { // 判断是否是正确的手机号码
					// Log.e("bbb",use)
					ToastUtil.shortToastByRec(RevisePhoneActivity.this,
							R.string.mobileisnotvolid);
					return;
				} else if (currentTime == CommonConstants.GET_CODE_TIME) {
					PreferencesUtils.putString(getApplicationContext(),
							PreferenceConstants.MOBILE, username);
					task = new TimerTask() {
						@Override
						public void run() {
							sendEmptyUiMessage(MsgConstants.MSG_01);
						}
					};
					timer.schedule(task, 0, 1000);
					final String smsCode = edt_registecode.getText().toString()
							.trim();
					Log.e("sms", "codddddd");
					AsyncHttpUtils.doSimGet(UrlMap.getUrl(MCUrl.NewMobile,
							"mobile", username),
							new AsyncHttpResponseHandler() {

								@Override
								public void onSuccess(int arg0, Header[] arg1,
										byte[] arg2) {
									// TODO Auto-generated method stub
									SmsBean bean = new Gson().fromJson(new String(
											arg2), SmsBean.class);
									/*
									 * code = bean.getData().get(0).getCode()+"";
									 * Log.e("COde", new String(arg2));
									 */
									Log.e("sims",
											bean.getMessage() + bean.getErrCode());
									if (bean.getErrCode() == 0) {
										code = bean.getData().get(0).getCode() + "";
										
										Log.e("验证码是：", code+"--------------");
										
									}
									if (bean.getErrCode() == -1) {
										ToastUtil.shortToast(
												getApplicationContext(),
												bean.getMessage());
										currentTime=0;
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
		btn_sumit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 String idcards = idcard.getText().toString().trim();
				 String oldphones = oldphone.getText().toString();
				 String edtpassords = edtpassord.getText().toString().trim();
				 String newphones = newphone.getText().toString();
				 String edt_registecodes = edt_registecode.getText().toString().trim();
				 if (edt_registecodes.equals(code)) {
				if (StringUtil.isMobileNO(oldphones) || (oldphones.length() == 11)
					|| 	StringUtil.isMobileNO(newphones) || (newphones.length() == 11)) {
					if(!TextUtils.isEmpty(edt_registecodes) || !TextUtils.isEmpty(edtpassords)){
						if (IDUtils.IDCardValidate(idcards).equals("true")) {
							
							addPostResult();
						}else {
							ToastUtil.shortToast(getApplicationContext(), "请输入正确的身份证号");
						}
					}else {
						ToastUtil.shortToast(getApplicationContext(), "密码或验证码不能为空");
					}
				} else 
				{
					ToastUtil.shortToast(getApplicationContext(), "手机号输入不对");
				}
				}else {
					ToastUtil.shortToast(getApplicationContext(), "验证码输入错误");
				}
			}
		});
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}
	private void resetCurrentTime() {
		currentTime = CommonConstants.GET_CODE_TIME;
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
	private void addPostResult() {
		JSONObject obj = new JSONObject();
		String password=MD5Util.MD5Encode(edtpassord.getText().toString());
		try {
			obj.put("userId",
					String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
			obj.put("idCard", idcard.getText().toString().trim());//
			obj.put("mobile", oldphone.getText().toString());//
			obj.put("oldPassword", password);//
			obj.put("newMobile", newphone.getText().toString());//
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		dialog.show();
		Log.e("查看数据", UrlMap.getfive(MCUrl.Modift, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)), 
				"idCode", idcard.getText().toString(), "mobile",oldphone.getText().toString(),
				"oldPassword",password,"newMobile",newphone.getText().toString()));
		AsyncHttpUtils.doPut(getApplicationContext(), MCUrl.Modift,obj.toString(),null,new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("11111111111 wwww   ", new String(arg2));
						dialog.dismiss();
						
						BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
//						Log.e("oppop", bean.data.toString());
							if (bean.getErrCode()==0) {
								finish();
							}
							ToastUtil.shortToast(getApplicationContext(), bean.getMessage());
						

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						dialog.dismiss();
					}
				});

	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (timer != null)
			timer.cancel();
		if (task != null)
			task.cancel();
	}

}
