package com.hex.express.iwant.activities;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.bean.SmsBean;
import com.hex.express.iwant.constance.CommonConstants;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.StringUtil;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
/**
 * 找回密码1/2
 * @author SCHT-40
 *
 */
public class FindPassWordActivity extends BaseActivity {
	
	private Timer timer;
	private TimerTask task;
	private int currentTime;
	private TitleBarView tbv_show;
	private EditText edt_registephonenumber;
	private EditText edt_registecode;
	private Button btn_getsmscode;
	private Button btn_nextstep;
	private String phone_number;
	private String code;
	LoadingProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_findpassword);
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
			break;

		/*case MsgConstants.MSG_02:
			etCode.setText("");
			currentTime = 0;
			break;*/
		}
	}

	@Override
	public void onWeightClick(View v) {
		switch (v.getId()) {
		case R.id.btn_getsmscode://获取验证码
			
			String username = edt_registephonenumber.getText().toString().trim();
			//String smsCode = edt_registecode.getText().toString().trim();
			/*//判断用户名验证码是否为空
			if (userName == null || smsCode == null || TextUtils.isEmpty(userName) || TextUtils.isEmpty(smsCode)) {
				ToastUtil.shortToastByRec(RegisteInfoActivity.this, R.string.mobileandsmscodenotnull);
				return;
			}*/
			if (!StringUtil.isMobileNO(username)||(username.length()!=11)){ //判断是否是正确的手机号码
				ToastUtil.shortToastByRec(FindPassWordActivity.this, R.string.mobileisnotvolid);
				return;
			}
			else if(currentTime == CommonConstants.GET_CODE_TIME) {
				task = new TimerTask() {

					@Override
					public void run() {
						sendEmptyUiMessage(MsgConstants.MSG_01);
					}
				};
				dialog.show();
				timer.schedule(task, 0, 1000);
				final String smsCode = edt_registecode.getText().toString()
						.trim();
				Log.e("sms", "codddddd");
				AsyncHttpUtils.doSimGet(UrlMap.getUrl(MCUrl.FIND_CODE,
						PreferenceConstants.MOBILE, username),
						new AsyncHttpResponseHandler() {

							@Override
							public void onSuccess(int arg0, Header[] arg1,
									byte[] arg2) {
								// TODO Auto-generated method stub
								SmsBean bean = new Gson().fromJson(
										new String(arg2), SmsBean.class);
								/*code = bean.getData().get(0).getCode()+"";
								Log.e("COde", new String(arg2));*/
								if(dialog!=null)
									dialog.dismiss();
								Log.e("sims",bean.getMessage()+bean.getErrCode());
								if(bean.getErrCode()==0){
									code = bean.getData().get(0).getCode()+"";
									ToastUtil.shortToast(getApplicationContext(), "验证码已发送，请注意查收");
								}
								if(bean.getErrCode()==-1){
									ToastUtil.shortToast(getApplicationContext(), bean.getMessage());
								}
							}
							@Override
							public void onFailure(int arg0, Header[] arg1,
									byte[] arg2, Throwable arg3) {

							}
						});
			}
			break;
		case R.id.btn_nextstep://下一步
			String userName = edt_registephonenumber.getText().toString().trim();
			String smsCode = edt_registecode.getText().toString().trim();
			if (code != null && !code.equals("") && code.equals(smsCode)) {
				Intent intent=new Intent(FindPassWordActivity.this,
						FindPassWordOkActivity.class);
				intent.putExtra("mobile", userName);
				startActivity(intent);
			}
			else{
				ToastUtil.shortToast(getApplication(), "请输入正确的验证码");
			}
			break;
		}
	}

	@Override
	public void initView() {
		//标题
		tbv_show = (TitleBarView) findViewById(R.id.tbv_show);
		this.tbv_show.setTitleText(R.string.findnext1);
		//手机号和验证码
		edt_registephonenumber = (EditText) findViewById(R.id.edt_registephonenumber);
		edt_registecode = (EditText) findViewById(R.id.edt_registecode);
		//按钮
		btn_getsmscode = (Button) findViewById(R.id.btn_getsmscode);
		btn_nextstep = (Button) findViewById(R.id.btn_nextstep);
		dialog=new LoadingProgressDialog(this);
		
	}

	@Override
	public void initData() {
		tbv_show.setTitleText("找回密码1/2");
		timer = new Timer();
		//edt_registephonenumber.getText().toString()
		resetCurrentTime();
	}

	@Override
	public void setOnClick() {
		btn_getsmscode.setOnClickListener(this);
		btn_nextstep.setOnClickListener(this);
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
}
