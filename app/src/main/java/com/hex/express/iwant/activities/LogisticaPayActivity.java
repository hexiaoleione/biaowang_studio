package com.hex.express.iwant.activities;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.mypasspay.CompanyView;
import com.hex.express.iwant.mypasspay.OnPasswordInputFinish;
import com.hex.express.iwant.newmain.MainTab;
import com.hex.express.iwant.newsmain.NewMainActivity;
import com.hex.express.iwant.utils.MD5Util;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.R.integer;
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
import android.widget.TextView;

public class LogisticaPayActivity extends Activity {

	// 接收到的提现参数
	private String applyMoney, aliPayNickName, aliPayAccount;
	private String strPassword;
	private TextView tv_resetPayPassword;
	int recId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = this.getIntent();
		recId = intent.getIntExtra("recId", 0);
//		Log.e("money", applyMoney);
//		aliPayNickName = intent.getStringExtra("aliPayNickName");
//		aliPayAccount = intent.getStringExtra("aliPayAccount");
		/************* 第一种用法————开始 ***************/
		// setContentView(R.layout.activity_main);
		//
//		 final PasswordView pwdView = (PasswordView)
//		 findViewById(R.id.pwd_view);
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
//		 pwdView.getCancelImageView().setOnClickListener(new
//		 View.OnClickListener() {
//		 @Override
//		 public void onClick(View v) {
//		 Toast.makeText(MainActivity.this, "Biu Biu Biu",
//		 Toast.LENGTH_SHORT).show();
//		 }
//		 });
	
		/************ 第一种用法————结束 ******************/

		/************* 第二种用法————开始 *****************/
		final CompanyView pwdView = new CompanyView(this);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏

		// final PasswordView pwdView = new PasswordView(this);
		setContentView(pwdView);
		
		//重置密码：
		tv_resetPayPassword = (TextView) pwdView.findViewById(R.id.tv_resetPasswords);
		tv_resetPayPassword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//			startActivity(new Intent(getApplicationContext(), ResetPayPasswordActivity.class));	
			getMessages(recId);
			}
		});
		
		 pwdView.getCancelImageView().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					finish();
					
				}
			});
		 
		 
		pwdView.setOnFinishInput(new OnPasswordInputFinish() {
			@Override
			public void inputFinish() {
//				strPassword = MD5Util.MD5Encode(pwdView.getStrPassword());// 在监听事件里才能获取到值;
				strPassword = pwdView.getStrPassword();// 在监听事件里才能获取到值;
				// 打印出来密码
				// Toast.makeText(DepositPassActivity.this, strPassword,
				// Toast.LENGTH_SHORT).show();
				// 提现申请
				PostHttpRequst();
			}
		});
		/************** 第二种用法————结束 ****************/
	}

	/**
	 * 提现申请
	 */
	private void PostHttpRequst() {
//		Log.e("JSON1", strPassword.toString());
//		Log.e("JSON", PreferencesUtils.getString(getApplicationContext(),
//				PreferenceConstants.PAYPASSWORD));
//		if (!strPassword.equals(PreferencesUtils.getString(
//			getApplicationContext(), PreferenceConstants.PAYPASSWORD))) {
//			ToastUtil.shortToast(getApplicationContext(), "支付密码不正确!");
//		} else {
			JSONObject obj = new JSONObject();
			try {
				// 提现操作需要的参数:
				obj.put("recId", recId);
				obj.put("dealPassword", strPassword);
//				obj.put("aliPayAccount", aliPayAccount);
//				obj.put("userId", Integer.valueOf(PreferencesUtils.getInt(
//				getApplicationContext(), PreferenceConstants.UID)));
				Log.e("shuju", obj.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			RequestParams params = new RequestParams();
//			AsyncHttpUtils.doPostJson(LogisticaPayActivity.this, MCUrl.CHECK,
//					obj.toString(), new AsyncHttpResponseHandler() {
			AsyncHttpUtils.doGet(UrlMap.getTwo(MCUrl.CHECK, "recId", recId+"", "dealPassword", strPassword ), null, null, params,
					new AsyncHttpResponseHandler() {

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {

						}
						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							Log.e("json", new String(arg2));
							BaseBean bean = new Gson().fromJson(
									new String(arg2), BaseBean.class);
							if (bean.isSuccess()) {							
								Builder ad = new Builder(
										LogisticaPayActivity.this);
								ad.setTitle("温馨提示");
								ad.setMessage("您已完成");
								ad.setPositiveButton("确认",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
//												Intent intent = new Intent(
//														LogisticaPayActivity.this,
//														MainActivity.class);
//												Intent intent = new Intent(
//														LogisticaPayActivity.this,
//														MainTab.class);
												Intent intent = new Intent(LogisticaPayActivity.this, NewMainActivity.class);
												intent.putExtra("type", "1");
												startActivity(intent);
												finish();
											}
										});
								ad.create().show();
							} else {
								ToastUtil.shortToast(LogisticaPayActivity.this,
										bean.getMessage());
							}
						}

					});
//		}

	}
	public void getMessages(final int recid){
		RequestParams params = new RequestParams();
		Log.e("数据", UrlMap.getUrl(MCUrl.ARRIVE, "recId", recid+""));
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.ARRIVE, "recId", recid+""), null, null, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("11111111json", new String(arg2));
						BaseBean		beans = new Gson().fromJson(new String(arg2),
								BaseBean.class);
						if (beans.getErrCode()==0) {
							ToastUtil.shortToast(LogisticaPayActivity.this, beans.getMessage());
							// 输入支付密码:LogisticaPayActivity   CompanyDeliveryPayActivity
//							startActivity(new Intent(getApplicationContext(), LogisticaPayActivity.class)
//									.putExtra("recId",recid));
						}else {
							ToastUtil.shortToast(LogisticaPayActivity.this, beans.getMessage());
						}
						
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						ToastUtil.shortToast(LogisticaPayActivity.this, "网络请求加载失败");

					}
				});
	}
}

