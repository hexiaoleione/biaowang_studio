package com.hex.express.iwant.activities;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.LogisPayActivity.MyReceiver;
import com.hex.express.iwant.alipay.AlipayUtils;
import com.hex.express.iwant.alipay.PayResult;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.DiscountBean;
import com.hex.express.iwant.bean.WechatBean;
import com.hex.express.iwant.constance.CollectionKey;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.MessageUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.DialogUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotPermittPayActivity extends BaseActivity{
	@Bind(R.id.logispay_moneys)//总钱数
	TextView logispay_moneys;
	@Bind(R.id.pay_exts)//关闭
	TextView  pay_ext;
	@Bind(R.id.logis_paybtn)//支付按钮
	Button  logis_paybtn;
	
	@Bind(R.id.pay_surplus)//支付按钮
	ImageView  pay_surplus;
	@Bind(R.id.pay_wenxin)//支付按钮
	ImageView  pay_wenxin;
	@Bind(R.id.pay_zhifubao)//支付按钮
	ImageView  pay_zhifubao;
	
	private String billCode;
	private String money;
	private String insureCostString;
	PayReq req;
	private String orderInfo;
	private String result;
	final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
	private MyReceiver reMyrecive;
	private Object needPayMoney;
	private DiscountBean bean;
	String way;
	int ways;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notpermit);//activity_logispay activity_logispaynew
		ButterKnife.bind(NotPermittPayActivity.this);
		billCode = getIntent().getStringExtra("billCode");
		req = new PayReq();
		msgApi.registerApp(CollectionKey.APP_ID);
		initData();
		
	}

	@OnClick({  R.id.pay_surplus, R.id.pay_zhifubao, R.id.pay_wenxin ,R.id.logis_paybtn})
	public void MyOnClick(View view) {
		Intent intent = new Intent();
		switch (view.getId()) {
		case R.id.pay_surplus://余额支付
			logispay_moneys.setText(getIntent().getStringExtra("playMoneyMin")+"元");
//			getaddsurplus();
//			pay_surplus.setBackgroundResource(R.drawable.underclick_03);
			pay_surplus.setBackgroundResource(R.drawable.underclick_03);
			pay_wenxin.setBackgroundResource(R.drawable.weixin_noclick_05);
			pay_zhifubao.setBackgroundResource(R.drawable.zhifubao_noclick_07);
			ways=1;
			break;
		case R.id.pay_zhifubao://支付宝支付
//			getAllpay();
			logispay_moneys.setText(getIntent().getStringExtra("playMoneyMax")+"元");
			pay_wenxin.setBackgroundResource(R.drawable.weixin_noclick_05);
			pay_zhifubao.setBackgroundResource(R.drawable.zhifubao_clik);
			pay_surplus.setBackgroundResource(R.drawable.noderclick);
			ways=2;
			break;
		case R.id.pay_wenxin://微信支付
//			getWxChat();
			logispay_moneys.setText(getIntent().getStringExtra("playMoneyMax")+"元");
			pay_wenxin.setBackgroundResource(R.drawable.weixin_clik);
			pay_zhifubao.setBackgroundResource(R.drawable.zhifubao_noclick_07);
			pay_surplus.setBackgroundResource(R.drawable.noderclick);
			ways=3;
			break;
		case R.id.logis_paybtn://支付按钮
			uoplay();
			break;
			
		default:
			break;
		}
		setResult(RESULT_OK, intent);

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
		reMyrecive = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("recharge");
		registerReceiver(reMyrecive, filter);
		getDiscount();
			pay_ext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	/**
	 * 支付
	 */
	public void uoplay(){// 1是余额，2是支付宝，3是微信支付
		if (ways==1) {
			money=getIntent().getStringExtra("playMoneyMin");
				getaddsurplus();
			
			
		}else if (ways==2) {
			money=getIntent().getStringExtra("playMoneyMax");
					getAllpay("A");
			
		}else if (ways==3) {
			money=getIntent().getStringExtra("playMoneyMax");
				getWxChat("1");
			
		}
		
		
	}
	/**
	 * 支付宝支付
	 */
	public void getAllpay(String ty) {
		// if (bean.data.get(0).discount != null) {
		// needPayMoney = Double.parseDouble(money)
		// * Double.parseDouble(bean.data.get(0).discount);
		// }
		if (billCode.equals("") || getIntent().getStringExtra("playMoneyMax").equals("")) {
			ToastUtil.shortToast(getApplicationContext(), "该订单有问题");
			return;
		}
		orderInfo = AlipayUtils.getOrderInfo("物流线上支付", null + getIntent().getStringExtra("playMoneyMax") + "元", getIntent().getStringExtra("playMoneyMax") + "",
				"http://www.efamax.com:8888/appservice-1.0-SNAPSHOT/jsp/alipay/receiveNotify.jsp", null, null,
				billCode);
		Log.e("pp", orderInfo);
		sendEmptyBackgroundMessage(MsgConstants.MSG_01);

	}

	/**
	 * 微信支付
	 */
	public void getWxChat(String type) {
		if (billCode.equals("") || getIntent().getStringExtra("playMoneyMax").equals("")) {
			ToastUtil.shortToast(getApplicationContext(), "该订单有问题");
			return;
		}
		MessageUtils.alertMessageCENTER(getApplicationContext(), "正在唤醒微信支付");
		if (!checkWeiXinVersion()) {
			ToastUtil.shortToast(getApplicationContext(), "微信版本不支持，请下载最新微信版本");
			return;
		}
		JSONObject obj = new JSONObject();
		try {
			obj.put("billCode", billCode);
			obj.put("matName", null);
			obj.put("matType", null);
			obj.put("insuranceFee", null);
			obj.put("insureMoney", null);
			obj.put("shipMoney", null);
			obj.put("userCouponId", null);
			obj.put("price", getIntent().getStringExtra("playMoneyMax"));
			obj.put("type", null);
			obj.put("weight", null);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.e("111111obj", obj.toString());
		RequestParams params = new RequestParams();
		dialog.show();
//		AsyncHttpUtils.doPut(getApplicationContext(), MCUrl.WPRE, obj.toString(), null,
//				new AsyncHttpResponseHandler() {
		AsyncHttpUtils.doGet(UrlMap.getTwo(MCUrl.WPRE, "billCode",billCode,
				"price",money), null, null, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						dialog.dismiss();
						if (arg2 == null)
							return;
					
						Log.e("111111111obj", new String(arg2));
						WechatBean bean = new Gson().fromJson(new String(arg2), WechatBean.class);
	            if (bean.getErrCode()==0) {
						req.appId = bean.data.get(0).appId;
						req.partnerId = bean.data.get(0).partnerId;
						req.prepayId = bean.data.get(0).prepayid;
						req.nonceStr = bean.data.get(0).nonceStr;
						req.packageValue = bean.data.get(0).package_;
						req.sign = bean.data.get(0).sign;
						req.timeStamp = bean.data.get(0).timestamp;
						req.extData = "app data";
						msgApi.registerApp(CollectionKey.APP_ID);
						msgApi.sendReq(req);
	        	}else {
					ToastUtil.shortToast(NotPermittPayActivity.this, bean.getMessage());
				}
					}
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						dialog.dismiss();
						Log.e("111111111obj", ""+arg0);
						Log.e("111111111obj", ""+arg1);
						Log.e("111111111obj", ""+arg2);
					}
				});
	}

	@Override
	public void handleBackgroundMessage(Message msg) {
		super.handleBackgroundMessage(msg);
		switch (msg.what) {
		case MsgConstants.MSG_01:
			PayTask task = new PayTask(this);
			result = task.pay(orderInfo);
			Log.e("mm", result);
			sendEmptyUiMessage(MsgConstants.MSG_01);
			break;

		default:
			break;
		}
	}

	@Override
	public void handleUiMessage(Message msg) {
		super.handleUiMessage(msg);
		switch (msg.what) {
		case MsgConstants.MSG_01:
			PayResult payResult = new PayResult(result);
			String resultStatus = payResult.getResultStatus();
			Log.e("su", resultStatus + payResult.getResult());
			if (TextUtils.equals(resultStatus, "9000")) {
				getQueryResult(billCode);
//				Toast.makeText(LogisPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
			} else {
				// 判断resultStatus 为非“9000”则代表可能支付失败
				// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
				if (TextUtils.equals(resultStatus, "8000")) {
					Toast.makeText(NotPermittPayActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

				} else {
					// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
					Toast.makeText(NotPermittPayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

				}
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 余额支付的接口
	 */
	public void getaddsurplus() {
		if (billCode.equals("") || getIntent().getStringExtra("playMoneyMax").equals("")) {
			ToastUtil.shortToast(getApplicationContext(), "该订单有问题");
			return;
		}
		JSONObject obj = new JSONObject();
		try {
			obj.put("userId",
			String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
			obj.put("billCode", billCode);
			obj.put("matName", null);
			obj.put("matType", null);
			obj.put("insuranceFee", null);
			obj.put("insureMoney", null);
			obj.put("shipMoney", null);
			obj.put("counterFee", getIntent().getStringExtra("playMoneyMax"));
			obj.put("weight", null);
			obj.put("userCouponId", 0);
			// if (cb_gift.isChecked()) {
			// obj.put("userCouponId", userCouponId);
			// Log.e("ic", userCouponId + "");
			// } else {
			// obj.put("userCouponId", 0);
			// Log.e("ic", "fff");
			// }

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e("obj", UrlMap.getThreeUrl(MCUrl.BALANCEPAYCOMPAY, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)), "billCode", billCode ,
				"counterFee", money));
		RequestParams params = new RequestParams();
		dialog.show();
//		AsyncHttpUtils.doPut(getApplicationContext(), MCUrl.BALANCEPAY, obj.toString(), null,
//		new AsyncHttpResponseHandler() {
		AsyncHttpUtils.doGet(UrlMap.getThreeUrl(MCUrl.BALANCEPAYCOMPAY, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)), "billCode", billCode ,
				"counterFee", money), null, null, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method
						// stub
						dialog.dismiss();
						Intent intent=new Intent();
						if (arg2 == null)
							return;
						Log.e("msg", new String(arg2));
						BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
						if (bean.getErrCode() == 0) {
							ToastUtil.shortToast(NotPermittPayActivity.this, bean.getMessage());
//							sendBroadcast(new Intent().setAction("freight").putExtra("flag", "owner"));//启动SendFreightActivity界面	
//							intent.setClass(LogisPayActivity.this, MylogisticakActivity.class);//个人
//							setResult(RESULT_OK, intent);
//							startActivity(intent);
							finish();
						}
						if (bean.getErrCode() == -2) {
							DialogUtils.createAlertDialogTwo(NotPermittPayActivity.this, "", bean.getMessage(), 0, "去充值",
									"更换支付方式", new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog, int which) {

											startActivity(new Intent(NotPermittPayActivity.this, RechargeActivity.class));
										}

									}).show();
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method
						// stub
						dialog.dismiss();
					}
				});
	}

	

	/**
	 * 接收微信支付成功返回的状态
	 * 
	 * @author
	 *
	 */
	public class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("recharge")) {
				getQueryResult(billCode);
			}

		}

	}

	// 获取支付宝支付折扣
	public void getDiscount() {
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.ALIPAYDISCOUNT, "uid", PreferenceConstants.UID), null, null, null,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("disconut", new String(arg2));
						bean = new Gson().fromJson(new String(arg2), DiscountBean.class);
					}
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub
					}
				});
	}

	// 获取支付宝和微信支付的结果
	public void getQueryResult(String billCode) {
		String billCodesString;
		  if (way.equals("2")) {
			  billCodesString=billCode+"B";
				}else {
			 billCodesString=billCode+"A";
				}
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.MONEYINLOGRESULT, "billCode", billCodesString), null, null, null,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("msdjkg", new String(arg2));
						BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
						if (bean.isSuccess()) {
							MessageUtils.alertMessageCENTER(getApplicationContext(), "支付成功");
//							Intent intent=new Intent();
//							intent.setClass(LogisPayActivity.this, MylogisticakActivity.class);//用户
//							startActivity(intent);
//							sendBroadcast(new Intent().setAction("freight").putExtra("flag", "owner"));//启动SendFreightActivity界面	
							finish();
						} else {
							MessageUtils.alertMessageCENTER(getApplicationContext(), bean.getMessage());
							finish();
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						MessageUtils.alertMessageCENTER(getApplicationContext(), "支付失败");

					}
				});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(reMyrecive);
	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub

	}
	/**
	 * 检查微信版本是否支持支付
	 * 
	 * @return
	 */
	private boolean checkWeiXinVersion() {
		boolean isPaySupported = msgApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
		return isPaySupported;
	}

}
