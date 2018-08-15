package com.hex.express.iwant.activities;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.bean.RegisterBean;
import com.hex.express.iwant.bean.SmsBean;
import com.hex.express.iwant.constance.CommonConstants;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.HttpRequestParams;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.DataTools;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.StringUtil;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 注册1/3界面
 * 
 * @author Eric
 * 
 */
public class RegisteInfoActivity extends BaseActivity {
	private Timer timer;
	private TimerTask task;
	private int currentTime;
	private TitleBarView tbv_show;
	private EditText edt_registephonenumber;
	private EditText edt_registecode;
	private EditText edt_introducer;
	private Button btn_getsmscode;
	private Button btn_nextstep;
	private String phone_number;
	private String code;
	int  codse;

	// private String smsCode;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registerinfo);
		iWantApplication.getInstance().addActivity(this);
		initView();
		initData();
		setOnClick();
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

	@Override
	public void onWeightClick(View v) {
		switch (v.getId()) {
		case R.id.btn_getsmscode:// 获取验证码
			String username = edt_registephonenumber.getText().toString()
					.trim();
			if (!StringUtil.isMobileNO(username) || (username.length() != 11)) { // 判断是否是正确的手机号码
				// Log.e("bbb",use)
				ToastUtil.shortToastByRec(RegisteInfoActivity.this,
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
				String id = DataTools.getDeviceId(getApplicationContext());
//				obj.put("deviceId", id);
				Log.e("sms", ""+(UrlMap.getTwo(MCUrl.smscodeSend,
						PreferenceConstants.MOBILE, username,"deviceId",id)));
				AsyncHttpUtils.doSimGet(UrlMap.getTwo(MCUrl.smscodeSend,
						PreferenceConstants.MOBILE, username,"deviceId",id),
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
			break;
		case R.id.btn_nextstep:// 下一步
			final String userName = edt_registephonenumber.getText().toString()
					.trim();
			final String smsCode = edt_registecode.getText().toString().trim();
			if (smsCode != null && !smsCode.equals("")) {
				if (userName.equals(edt_introducer.getText().toString().trim())) {
					ToastUtil.shortToast(RegisteInfoActivity.this, "推荐人不能是自己");
				} else {
					Log.e("cccd", UrlMap.getTwo(MCUrl.compare,
							"mobile", userName,"code",smsCode + ""));
					AsyncHttpUtils.doSimGet(UrlMap.getTwo(MCUrl.compare,
							"mobile", userName,"code",smsCode),
							new AsyncHttpResponseHandler() {

								@Override
								public void onSuccess(int arg0, Header[] arg1,
										byte[] arg2) {
									// TODO Auto-generated method stub
									SmsBean bean = new Gson().fromJson(new String(
											arg2), SmsBean.class);
									
									if (bean.getErrCode() == 0) {
										Intent intent = new Intent(RegisteInfoActivity.this,
												RegisterSetPwdActivity.class);
										intent.putExtra("code", smsCode);
										intent.putExtra("mobile", userName);
										intent.putExtra("recommendMobile", edt_introducer.getText()
												.toString());
										startActivity(intent);
										finish();
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
			} else {
				ToastUtil.shortToast(getApplication(), "请输入正确的验证码");
			}
			break;
		}
	}

	@Override
	public void initView() {
		// 标题
		tbv_show = (TitleBarView) findViewById(R.id.tbv_show);
		this.tbv_show.setTitleText(R.string.next1);
		tbv_show.getLeftBtn().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(RegisteInfoActivity.this, LoginActivity.class));
				
			}
		});
		// 手机号和验证码
		edt_registephonenumber = (EditText) findViewById(R.id.edt_registephonenumber);
		edt_registecode = (EditText) findViewById(R.id.edt_registecode);
		// 按钮
		btn_getsmscode = (Button) findViewById(R.id.btn_getsmscode);
		btn_nextstep = (Button) findViewById(R.id.btn_nextstep);
		// 设置edittext的监听
		// 推荐人手机号
		edt_introducer = (EditText) findViewById(R.id.edt_introducer);
	}

	@Override
	public void initData() {
		timer = new Timer();
		// edt_registephonenumber.getText().toString()
		resetCurrentTime();
	}

	@Override
	public void setOnClick() {
		btn_getsmscode.setOnClickListener(this);
		btn_nextstep.setOnClickListener(this);
	}

	@Override
	public void getData() {

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
