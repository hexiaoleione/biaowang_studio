package com.hex.express.iwant.mypasspay;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.MainActivity;
import com.hex.express.iwant.activities.ResetPayPasswordActivity;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.newmain.MainTab;
import com.hex.express.iwant.newsmain.NewMainActivity;
import com.hex.express.iwant.utils.MD5Util;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class MypassActivity extends Activity {

	// 接收到的转账参数
	private String getMoney;
	private String getPhone;
	private LoadingProgressDialog dialog;

	private String strPassword;
	private TextView tv_resetPayPassword;
	private LinearLayout linear_pass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = this.getIntent();
		getMoney = intent.getStringExtra("money");
		getPhone = intent.getStringExtra("phoneTo");
		
		
		/************* 第一种用法————开始 ***************/
		// setContentView(R.layout.activity_main);
		//
		// final PasswordView pwdView = (PasswordView)
		// findViewById(R.id.pwd_view);
		//
		// //添加密码输入完成的响应
		// pwdView.setOnFinishInput(new OnPasswordInputFinish() {
		// @Override
		// public void inputFinish() {
		// //输入完成后我们简单显示一下输入的密码
		// //也就是说——>实现你的交易逻辑什么的在这里写
		// Toast.makeText(MainActivity.this, pwdView.getStrPassword(),
		// Toast.LENGTH_SHORT).show();
		// }
		// });
		//
		// /**
		// * 可以用自定义控件中暴露出来的cancelImageView方法，重新提供相应
		// * 如果写了，会覆盖我们在自定义控件中提供的响应
		// * 可以看到这里toast显示 "Biu Biu Biu"而不是"Cancel"*/
		// pwdView.getCancelImageView().setOnClickListener(new
		// View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Toast.makeText(MainActivity.this, "Biu Biu Biu",
		// Toast.LENGTH_SHORT).show();
		// }
		// });
		/************ 第一种用法————结束 ******************/
		
		/************* 第二种用法————开始 *****************/
		 final PasswordView pwdView = new PasswordView(this);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏

//		final PasswordView pwdView = new PasswordView(this);
		setContentView(pwdView);
		
		//重置密码：
		tv_resetPayPassword = (TextView) pwdView.findViewById(R.id.tv_resetPassword);
		tv_resetPayPassword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			startActivity(new Intent(getApplicationContext(), ResetPayPasswordActivity.class));	
			}
		});
		
		pwdView.setOnFinishInput(new OnPasswordInputFinish() {
			@Override
			public void inputFinish() {
				strPassword = MD5Util.MD5Encode(pwdView.getStrPassword());//在监听事件里才能获取到值;
//				Toast.makeText(MypassActivity.this, strPassword, Toast.LENGTH_SHORT).show();
				// 申请转账;
				PostRequstMoney();
			}
		});
		 pwdView.getCancelImageView().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					finish();
					
				}
			});
		/************** 第二种用法————结束 ****************/
	}

	/**
	 * 转账:
	 */
	private void PostRequstMoney() {

		Log.e("---------------", PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.PAYPASSWORD));
//		Log.e("---------------", strPassword);
		
		if (!strPassword.equals(PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.PAYPASSWORD))) {
			ToastUtil.shortToast(getApplicationContext(), "支付密码不正确!");
		} else {
			JSONObject obj = new JSONObject();
			try {
				obj.put("mobileTo", getPhone);
				obj.put("tradeMoney", getMoney);
				obj.put("userId",
						Integer.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
				// obj.put("payPassword",strPassword);
				Log.e("json", "++++++" + obj.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			final Builder ads = new Builder(MypassActivity.this);
			ads.setTitle("转账中...");
			ads.create().setCancelable(false);
			ads.create().show();
			Log.e("json", "_____" + MCUrl.TRANFERMONEY);
			AsyncHttpUtils.doPostJson(MypassActivity.this, MCUrl.TRANFERMONEY, obj.toString(),
					new AsyncHttpResponseHandler() {

						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
							// dialog.dismiss();
							ads.create().dismiss();
							ads.setCancelable(true);
							finish();
						}

						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
							Log.e("json22", new String(arg2));
							// dialog.dismiss();
							ads.create().dismiss();
							BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
							if (bean.isSuccess()) {
								Builder ad = new Builder(MypassActivity.this);
								ad.setTitle("温馨提示");
								ad.setMessage("转账成功");
								ad.setPositiveButton("确认", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
//										Intent intent = new Intent(MypassActivity.this, MainActivity.class);
//										Intent intent = new Intent(MypassActivity.this, MainTab.class);
										Intent intent = new Intent(MypassActivity.this, NewMainActivity.class);	
										startActivity(intent);
										finish();
										ToastUtil.shortToast(getApplicationContext(), "转账成功");
									}
								});
								ad.create().show();

							} else {
								ads.create().dismiss();
								ToastUtil.shortToast(MypassActivity.this, bean.getMessage());
								finish();
							}

						}

					});

		}
	}
}
