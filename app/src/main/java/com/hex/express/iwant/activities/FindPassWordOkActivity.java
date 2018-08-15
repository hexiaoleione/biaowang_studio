package com.hex.express.iwant.activities;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.RegisterBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.utils.DataTools;
import com.hex.express.iwant.utils.MD5Util;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.StringUtil;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
/**
 * 找回密码2/2
 * @author SCHT-40
 *
 */
public class FindPassWordOkActivity extends BaseActivity {
	private TitleBarView tbv_show;
	private EditText edt_pwdlength;
	private Button btn_nextstep;
	private String userName;
	private String smsCode;
	private String password;
	private String result;
	LoadingProgressDialog dialog;
	private String mobile;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registersetpwd);
		iWantApplication.getInstance().addActivity(this);
		getData();
		initView();
		initData();
		setOnClick();
	}

	@Override
	public void onWeightClick(View v) {
		switch (v.getId()) {
		case R.id.btn_nextstep://下一步
			String pwd = edt_pwdlength.getText().toString().trim();
			if (pwd == null || TextUtils.isEmpty(pwd)) {
				ToastUtil.shortToastByRec(getBaseContext(), R.string.pwdnotnull);
			} else if (TextUtils.isEmpty(pwd)) {
				ToastUtil.shortToast(getBaseContext(),"请输入密码");
			} else if (pwd.length() < 6 || pwd.length() >20) {
				ToastUtil.shortToastByRec(getBaseContext(), R.string.pwdshortandlong);
			} else {
				password=pwd;
				sendEmptyUiMessage(MsgConstants.MSG_01);
			}
			break;
		}
	}

	@Override
	public void initView() {
		//标题
		tbv_show = (TitleBarView) findViewById(R.id.tbv_show);
		//密码
		edt_pwdlength = (EditText) findViewById(R.id.edt_pwdlength);
		//按钮
		btn_nextstep = (Button) findViewById(R.id.btn_nextstep);
		dialog=new LoadingProgressDialog(this);
	}
	@Override
	public void handleUiMessage(Message msg) {
		super.handleUiMessage(msg);
		switch (msg.what) {
		case MsgConstants.MSG_01:
			JSONObject obj=new JSONObject();
			try {
				obj.put("mobile", mobile);
				obj.put("newPassword", MD5Util.MD5Encode(password));
				//obj.put("smsCode","1234");
				/*String id=DataTools.getDeviceId(getApplicationContext());
				obj.put("deviceId",id);*/
				Log.e("222", result+"---");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		//	LoadingProgressDialog.getInstance(this).show();
			dialog.show();
			AsyncHttpUtils.doPut(getApplicationContext(), MCUrl.RESET_CODE,obj.toString(),null,new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					Log.e("success", "success");
					Log.e("json",new String(arg2));
					RegisterBean bean=new Gson().fromJson(new String(arg2),RegisterBean.class);
					//LoadingProgressDialog.getInstance(RegisterSetPwdActivity.this).dismiss();
					if(dialog!=null)
					dialog.dismiss();
					ToastUtil.shortToast(getApplicationContext(), "密码修改成功");
					/*Log.e("userid",bean.getData().get(0).userId+"");*/
					/*PreferencesUtils.putInt(getApplicationContext(), PreferenceConstants.UID,bean.getData().get(0).userId);
					PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.MOBILE, bean.getData().get(0).mobile);
					PreferencesUtils.putBoolean(getApplicationContext(), PreferenceConstants.ISLOGIN,true);
					PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.PASSWORD, bean.getData().get(0).password);*/
					Intent intent = new Intent(FindPassWordOkActivity.this, LoginActivity.class);
					//跳转注册3/3
					startActivity(intent);
				}
				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
					Log.e("regis",arg0+"");
				}
			});
		default:
			break;
		}
	}
	@Override
	public void initData() {
		this.tbv_show.setTitleText(R.string.findnext2);
		this.btn_nextstep.setText(R.string.complete);
		mobile=getIntent().getStringExtra("mobile");
	}

	@Override
	public void setOnClick() {
		btn_nextstep.setOnClickListener(this);
	}

	@Override
	public void getData() {
		Intent intent = getIntent();
		userName = intent.getStringExtra("userName");
		smsCode = intent.getStringExtra("smsCode");
	}
}
