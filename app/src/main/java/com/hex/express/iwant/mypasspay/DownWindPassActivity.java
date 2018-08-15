package com.hex.express.iwant.mypasspay;

import org.apache.http.Header;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.DownEscoreDartActivity;
import com.hex.express.iwant.activities.MyDownwindActivity;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.DataTools;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 顺风镖件 镖师【确认取货码】
 * 
 * @author han
 *
 */
public class DownWindPassActivity extends Activity {

	// 从上一个页面接收到的参数

	private String dealPassword;// 交易码
	private Button btn_cancel;
	private Button btn_ok;
	private TextView tishi;

	// 镖件的信息
	private String recId;
	private String publishTime;
	private String personName;
	private String mobile;
	private String address;
	private String personNameTo;
	private String mobileTo;
	private String addressTo;
	private String matRemark;
	LoadingProgressDialog dialog;
	private double latitude;
	private double longitude;
	// 标识码
	private boolean isFinished = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = this.getIntent();
		recId = intent.getStringExtra("recId");
		publishTime = intent.getStringExtra("publishTime");
		personName = intent.getStringExtra("personName");
		mobile = intent.getStringExtra("mobile");
		address = intent.getStringExtra("address");
		personNameTo = intent.getStringExtra("personNameTo");
		mobileTo = intent.getStringExtra("mobileTo");
		addressTo = intent.getStringExtra("addressTo");
		matRemark = intent.getStringExtra("matRemark");
		latitude = intent.getDoubleExtra("arriveLatitude", 0.0);
		longitude = intent.getDoubleExtra("arriveLongitude", 0.0);
		if (latitude == 5e-324 || latitude == 4.9E-324 || longitude == 5e-324 || longitude == 4.9E-324) {
			latitude = 0;
			longitude = 0;
			ToastUtil.longToast(DownWindPassActivity.this, "未获取到定位信息，请开启定位！");
			Intent intent12 = new Intent();
			intent12.putExtra("type", "1");
			setResult(RESULT_OK, intent12);
			finish();
		}
		Log.e("recId", recId + "-----------");
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
		// Toast.makeText(MainActivity.this, pwdView.getdealPassword(),
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
		final DownPasswordView pwdView = new DownPasswordView(this);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏

		// final PasswordView pwdView = new PasswordView(this);
		setContentView(pwdView);
		// tishi.setText(""+mobileTo);
		// 控件：取消和确定按钮：
		btn_cancel = (Button) pwdView.findViewById(R.id.btn_cancel);
		btn_ok = (Button) pwdView.findViewById(R.id.btn_ok);
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();

			}
		});

		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// finish();
				/**
				 * 输入取货验证码后请求
				 */
				// dialog.show();
				ToastUtil.longToast(DownWindPassActivity.this, "支付中...");
				AsyncHttpUtils
						.doSimGet(
								UrlMap.getfive(MCUrl.DRIVERTRUETASK, "recId", recId, "dealPassword", dealPassword,
										"checkDeviceId", DataTools.getDeviceId(DownWindPassActivity.this),
										"arriveLatitude", latitude + "", "arriveLongitude", longitude + ""),
								new AsyncHttpResponseHandler() {
									@Override
									public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
										// TODO Auto-generated method stub
										BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
										// dialog.dismiss();
										// Log.e("JSON1",
										// dealPassword.toString()+"============");
										// Log.e("bean________",
										// bean.getData().get(arg0).toString());
										if (bean.getErrCode() == 0) {// 如果取消订单成功
											Builder ad = new Builder(DownWindPassActivity.this);
											ad.setTitle("温馨提示");
											ad.setMessage("押镖成功");
											ad.setPositiveButton("确认", new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog, int which) {
													// Intent intent = new
													// Intent(
													// DownWindPassActivity.this,
													// MainActivity.class);
													// startActivity(intent);
													// finish();
													Intent intent = new Intent(getApplicationContext(),
															DownEscoreDartActivity.class);
													intent.putExtra("recId", recId);// int
																					// recId;//镖件主键
													intent.putExtra("publishTime", publishTime);// String
																								// publishTime;//发布时间
													intent.putExtra("personName", personName);// String
																								// personName;//发件人
													intent.putExtra("mobile", mobile);// String
																						// mobile;//发件人手机号
													intent.putExtra("address", address);// String
																						// address;//发件地址
													intent.putExtra("personNameTo", personNameTo);// String
																									// personNameTo;//收件人
													intent.putExtra("mobileTo", mobileTo);// String
																							// mobileTo;//收件人手机号
													intent.putExtra("addressTo", addressTo);// String
																							// addressTo;//收件地址
													intent.putExtra("matRemark", matRemark);// String
																							// matRemark;//物品备注
													intent.putExtra("isFinished", isFinished);// 标识码
													// startActivity(intent);
													intent.putExtra("type", "1");
													setResult(RESULT_OK, intent);
													finish();
												}
											});
											ad.create().show();
											finish();// 取消订单成功后即销毁此页面,并去启动上一个页面的Fragment
											// startActivity(new
											// Intent(getApplicationContext(),
											// MyDownwindActivity.class).putExtra("loadIndex",
											// "Escort"));

										} else {

											ToastUtil.shortToast(DownWindPassActivity.this, bean.getMessage());
										}
									}

									@Override
									public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
										// dialog.dismiss();
										ToastUtil.shortToast(DownWindPassActivity.this, "失败");
									}
								});
			}
		});

		// pwdView.getCancelImageView().setOnClickListener(new OnClickListener()
		// {
		//
		// @Override
		// public void onClick(View v) {
		//// finish();
		//// startActivity(new Intent(getApplicationContext(),
		// ResetPayPasswordActivity.class));
		//
		// }
		// });

		pwdView.setOnFinishInput(new OnPasswordInputFinish() {
			@Override
			public void inputFinish() {
				dealPassword = pwdView.getStrPassword();// 在监听事件里才能获取到值;
				// 打印出来密码
				// Toast.makeText(DepositPassActivity.this, dealPassword,
				// Toast.LENGTH_SHORT).show();
				// 提现申请
				if (latitude == 5e-324 || latitude == 4.9E-324 || longitude == 5e-324 || longitude == 4.9E-324) {
					// St
					latitude = 0.0;
					longitude = 0.0;
				}
				ToastUtil.longToast(DownWindPassActivity.this, "支付中...");
				AsyncHttpUtils
						.doSimGet(
								UrlMap.getfive(MCUrl.DRIVERTRUETASK, "recId", recId, "dealPassword", dealPassword,
										"checkDeviceId", DataTools.getDeviceId(DownWindPassActivity.this),
										"arriveLatitude", latitude + "", "arriveLongitude", longitude + ""),
								new AsyncHttpResponseHandler() {
									@Override
									public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
										// TODO Auto-generated method stub
										BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
										// dialog.dismiss();
										// Log.e("JSON1",
										// dealPassword.toString()+"============");
										// Log.e("bean________",
										// bean.getData().get(arg0).toString());
										if (bean.getErrCode() == 0) {// 如果取消订单成功
											// AlertDialog.Builder ad = new
											// Builder(
											// DownWindPassActivity.this);
											// ad.setTitle("温馨提示");
											// ad.setMessage("送单成功");
											// ad.setPositiveButton("确认",
											// new
											// DialogInterface.OnClickListener()
											// {
											// public void onClick(
											// DialogInterface dialog,
											// int which) {
											//// Intent intent = new Intent(
											//// DownWindPassActivity.this,
											//// MainActivity.class);
											//// startActivity(intent);
											//// finish();
											// Intent intent = new
											// Intent(getApplicationContext(),
											// DownEscoreDartActivity.class);
											//// intent.putExtra("recId",
											// recId);//int recId;//镖件主键
											//// intent.putExtra("publishTime",publishTime);//String
											// publishTime;//发布时间
											//// intent.putExtra("personName",personName);//String
											// personName;//发件人
											//// intent.putExtra("mobile",mobile);//String
											// mobile;//发件人手机号
											//// intent.putExtra("address",address);//String
											// address;//发件地址
											//// intent.putExtra("personNameTo",personNameTo);//String
											// personNameTo;//收件人
											//// intent.putExtra("mobileTo",mobileTo);//String
											// mobileTo;//收件人手机号
											//// intent.putExtra("addressTo",addressTo);//String
											// addressTo;//收件地址
											//// intent.putExtra("matRemark",matRemark);//String
											// matRemark;//物品备注
											//// intent.putExtra("isFinished",isFinished);//标识码
											//// startActivity(intent);
											//
											// }
											// });
											// ad.create().show();
											// finish();//取消订单成功后即销毁此页面,并去启动上一个页面的Fragment
											// startActivity(new
											// Intent(getApplicationContext(),
											// MyDownwindActivity.class).putExtra("loadIndex",
											// "Escort"));
											Intent intent = new Intent();
											intent.putExtra("type", "1");
											setResult(RESULT_OK, intent);
											finish();
										} else {
											Intent intent = new Intent();
											intent.putExtra("type", "1");
											setResult(RESULT_OK, intent);
											finish();
											ToastUtil.shortToast(DownWindPassActivity.this, bean.getMessage());
										}
									}

									@Override
									public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
										// dialog.dismiss();
										ToastUtil.shortToast(DownWindPassActivity.this, "失败");
									}
								});
			}
		});
	}
}
