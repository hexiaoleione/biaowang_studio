package com.hex.express.iwant.activities;

import java.math.BigDecimal;
import java.text.DecimalFormat;

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
import com.hex.express.iwant.bean.Checnum;
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

import android.R.integer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * 
 * 发物流支付界面
 */
public class LogisPayActivity extends BaseActivity {
	@Bind(R.id.logispay_moneys)//总钱数
	TextView logispay_moneys;
	@Bind(R.id.logispay_baos)//保险费
	TextView logispay_bao;
	@Bind(R.id.logispay_money)//快递费
	TextView logispay_money;
	@Bind(R.id.down_toggs)//选择现金卷开关
	ToggleButton  down_togg;
	@Bind(R.id.pay_exts)//关闭
	TextView  pay_ext;
	@Bind(R.id.logispay_fangshi)//支付方式
	TextView  logispay_fangshi;
	@Bind(R.id.logis_paybtn)//支付按钮
	Button  logis_paybtn;
	
	@Bind(R.id.pay_surplus)//支付按钮
	ImageView  pay_surplus;
	@Bind(R.id.pay_wenxin)//支付按钮
	ImageView  pay_wenxin;
	@Bind(R.id.pay_zhifubao)//支付按钮
	ImageView  pay_zhifubao;
	@Bind(R.id.wz_rela)
	RelativeLayout wz_rela;
	
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
		setContentView(R.layout.activity_logispaynew);//activity_logispay activity_logispaynew
		ButterKnife.bind(LogisPayActivity.this);
		billCode = getIntent().getStringExtra("billCode");
		req = new PayReq();
		msgApi.registerApp(CollectionKey.APP_ID);
		way=getIntent().getStringExtra("way");
		initData();
		
	}

	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub

	}
	@Override
	public void initData() {
		logispay_fangshi.setText("平台使用费");
		int i,j,h = 0; 
		reMyrecive = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("recharge");
		registerReceiver(reMyrecive, filter);
		getDiscount();
		logispay_money.setText(getIntent().getStringExtra("evaluationStatus"));
		logispay_moneys.setText(getIntent().getStringExtra("evaluationStatus"));
		//这里判断下是从哪里的支付接口 1是从顺风快递  2是从发物流接口
//		    String str=loff.data.get(0).transferMoney;
		   double d=Double.valueOf(getIntent().getStringExtra("evaluationStatus")).doubleValue();
		   double d1=Double.valueOf(getIntent().getStringExtra("evaluationScore")).doubleValue();
		   logispay_bao.setText("余额支付优惠"+(d1-d)+"元");
			down_togg.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
				return true;
				}
				});
//	
		pay_ext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

	@OnClick({ R.id.lout_showdown, R.id.lout_show, R.id.pay_surplus, R.id.pay_zhifubao, R.id.pay_wenxin ,R.id.logis_paybtn})
	public void MyOnClick(View view) {
		Intent intent = new Intent();
		switch (view.getId()) {
		case R.id.lout_showdown:// 关闭activity
			finish();
			break;
		case R.id.lout_show:// 关闭activity
			finish();
			break;
		case R.id.pay_surplus://余额支付
			logispay_money.setText(getIntent().getStringExtra("evaluationStatus")+"元");
			logispay_moneys.setText(getIntent().getStringExtra("evaluationStatus")+"元");
//			getaddsurplus();
//			pay_surplus.setBackgroundResource(R.drawable.underclick_03);
			pay_surplus.setBackgroundResource(R.drawable.underclick_03);
			pay_wenxin.setBackgroundResource(R.drawable.weixin_noclick_05);
			pay_zhifubao.setBackgroundResource(R.drawable.zhifubao_noclick_07);
			ways=1;
			wz_rela.setVisibility(View.VISIBLE);
			break;
		case R.id.pay_zhifubao://支付宝支付
//			getAllpay();
			logispay_money.setText(getIntent().getStringExtra("evaluationScore")+"元");
			logispay_moneys.setText(getIntent().getStringExtra("evaluationScore")+"元");
			pay_wenxin.setBackgroundResource(R.drawable.weixin_noclick_05);
			pay_zhifubao.setBackgroundResource(R.drawable.zhifubao_clik);
			pay_surplus.setBackgroundResource(R.drawable.noderclick);
			ways=2;
			wz_rela.setVisibility(View.GONE);
			break;
		case R.id.pay_wenxin://微信支付
//			getWxChat();
			logispay_money.setText(getIntent().getStringExtra("evaluationScore")+"元");
			logispay_moneys.setText(getIntent().getStringExtra("evaluationScore")+"元");
			pay_wenxin.setBackgroundResource(R.drawable.weixin_clik);
			pay_zhifubao.setBackgroundResource(R.drawable.zhifubao_noclick_07);
			pay_surplus.setBackgroundResource(R.drawable.noderclick);
			ways=3;
			wz_rela.setVisibility(View.GONE);
			break;
		case R.id.logis_paybtn://支付按钮
			uoplay();
			break;
			
		default:
			break;
		}
		setResult(RESULT_OK, intent);

	}
	/**
	 * 支付
	 */
	public void uoplay(){// 1是余额，2是支付宝，3是微信支付
		if (ways==1) {
			money=getIntent().getStringExtra("evaluationStatus");
			if (way.equals("2")) {
			int 	transferMoney=getIntent().getIntExtra("transferMoney", 0);
			int 	takeCargoMoney=getIntent().getIntExtra("takeCargoMoney", 0);
			int 	sendCargoMoney=getIntent().getIntExtra("sendCargoMoney", 0);
			int 	cargoTotal=getIntent().getIntExtra("cargoTotal", 0);
			Log.e("1111 ", transferMoney+"");
				getaddsurp( transferMoney ,takeCargoMoney,sendCargoMoney,cargoTotal);
			}else {
				getaddsurplus();
			}
			
			
		}else if (ways==2) {
			money=getIntent().getStringExtra("evaluationScore");
            if (way.equals("2")) {
            	getAllpay("B");
				}else {
					getAllpay("A");
				}
			
		}else if (ways==3) {
			money=getIntent().getStringExtra("evaluationScore");
			if (way.equals("2")) {
				getWxChat("2");
				}else {
				getWxChat("1");
				}
			
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
		if (billCode.equals("") || money.equals("")) {
			ToastUtil.shortToast(getApplicationContext(), "该订单有问题");
			return;
		}
		orderInfo = AlipayUtils.getOrderInfo("物流线上支付", null + money + "元", money + "",
				"http://www.efamax.com:8888/appservice-1.0-SNAPSHOT/jsp/alipay/receiveNotify.jsp", null, null,
				billCode+ty);
		Log.e("pp", orderInfo);
		sendEmptyBackgroundMessage(MsgConstants.MSG_01);

	}

	/**
	 * 微信支付
	 */
	public void getWxChat(String type) {
		if (billCode.equals("") || money.equals("")) {
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
			obj.put("price", money);
			obj.put("type", type);
			obj.put("weight", null);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.e("111111obj", obj.toString());
		RequestParams params = new RequestParams();
		dialog.show();
//		AsyncHttpUtils.doPut(getApplicationContext(), MCUrl.WPRE, obj.toString(), null,
//				new AsyncHttpResponseHandler() {
		AsyncHttpUtils.doGet(UrlMap.getThreeUrl(MCUrl.WPRE, "billCode",billCode,
				"price",money,"type",type), null, null, params,
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
					ToastUtil.shortToast(LogisPayActivity.this, bean.getMessage());
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
					Toast.makeText(LogisPayActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

				} else {
					// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
					Toast.makeText(LogisPayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

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
		if (billCode.equals("") || money.equals("")) {
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
			obj.put("counterFee", money);
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
		Log.e("obj", UrlMap.getThreeUrl(MCUrl.BALANCEPAY, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)), "billCode", billCode ,
				"counterFee", money));
		RequestParams params = new RequestParams();
		dialog.show();
//		AsyncHttpUtils.doPut(getApplicationContext(), MCUrl.BALANCEPAY, obj.toString(), null,
//		new AsyncHttpResponseHandler() {
		AsyncHttpUtils.doGet(UrlMap.getThreeUrl(MCUrl.BALANCEPAY, "userId", String
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
							ToastUtil.shortToast(LogisPayActivity.this, bean.getMessage());
//							sendBroadcast(new Intent().setAction("freight").putExtra("flag", "owner"));//启动SendFreightActivity界面	
//							intent.setClass(LogisPayActivity.this, MylogisticakActivity.class);//个人
//							setResult(RESULT_OK, intent);
//							startActivity(intent);
							finish();
						}
						if (bean.getErrCode() == -2) {
							DialogUtils.createAlertDialogTwo(LogisPayActivity.this, "", bean.getMessage(), 0, "去充值",
									"更换支付方式", new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog, int which) {

											startActivity(new Intent(LogisPayActivity.this, RechargeActivity.class));
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
	* * 两个Double数相减 * 
	*  
	* @param v1 * 
	* @param v2 * 
	* @return Double 
	*/  
	public static Double sub(Double v1, Double v2) {  
	   BigDecimal b1 = new BigDecimal(v1.toString());  
	   BigDecimal b2 = new BigDecimal(v2.toString());  
	   return new Double(b1.subtract(b2).doubleValue());  
	}  
	/**
	 * 接收选择现金价参数
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("requestCode  ", "          "+requestCode);
		Log.e("resultCode  ", "          "+resultCode);
		Log.e("11111data  ", "          "+data);
		switch (requestCode) {
		case 1:
			if (requestCode == 1 && data != null) {
				Bundle bundle = data.getBundleExtra("bundle2");
				bundle.getString("strResult");
				Log.e("11111 strResult  ", "          "+bundle.getString("strResult"));
			  String name ="{"+ "info:"+bundle.getString("strResult")+"}";   
			  Checnum bean = new Gson().fromJson(new String(name), Checnum.class);
			  for(int i=0;i< bean.getInfo().size();i++){
				Log.e("11111   ", bean.getInfo().get(i).item)  ;
				Log.e("11111   ", bean.getInfo().get(i).maney)  ;
				
			  }
			 
//			JSONArray jsonArray;
//			try {
//				JSONObject jsonObject = new JSONObject(name);
//				Log.e("1111111jjsonObject1  ",""+jsonObject);
//				jsonArray = jsonObject.getJSONArray("info");
//				Log.e("1111111jjsonArray1  ",""+jsonArray);
//				for (int i = 0; i < jsonArray.length(); i++) {
//					JSONObject jo = jsonArray.getJSONObject(i);
//					Log.e("1111111jsssss1  ",jo.getString("item"));
//					}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			}
			
			break;
		}
		}
	/**
	 * 余额支付的接口   物流公司
	 */
	public void getaddsurp(int transferMoney,int takeCargoMoney,int sendCargoMoney,int cargoTotal) {
		if (billCode.equals("") || money.equals("")) {
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
			obj.put("counterFee", money);
			obj.put("weight", null);
			obj.put("userCouponId", 0);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e("111111111compay", UrlMap.getfour(MCUrl.BALANCEPAYCOMPAY, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)), "billCode", billCode ,
				"counterFee", money,"transferMoney",transferMoney+""));
		RequestParams params = new RequestParams();
		dialog.show();
//		AsyncHttpUtils.doPut(getApplicationContext(), MCUrl.BALANCEPAY, obj.toString(), null,
//		new AsyncHttpResponseHandler() {
//		AsyncHttpUtils.doGet(UrlMap.getfour(MCUrl.BALANCEPAYCOMPAY, "userId", String
//				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
//						PreferenceConstants.UID)), "billCode", billCode ,
//				"counterFee", money,"transferMoney",transferMoney+""), null, null, params,
//				new AsyncHttpResponseHandler() {
		AsyncHttpUtils.doGet(UrlMap.getseven(MCUrl.BALANCEPAYCOMPAY, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)), "billCode", billCode ,
				"counterFee", money,"transferMoney",transferMoney+"",
				"takeCargoMoney",takeCargoMoney+"","sendCargoMoney",sendCargoMoney+"","cargoTotal",cargoTotal+""), null, null, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method
						// stub
						Intent intent=new Intent();
						dialog.dismiss();
						if (arg2 == null)
							return;
						Log.e("compaymsg", new String(arg2));
						BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
						if (bean.getErrCode() == 0) {
							ToastUtil.shortToast(LogisPayActivity.this, bean.getMessage());
//							sendBroadcast(new Intent().setAction("freight").putExtra("flag", "owner"));//启动SendFreightActivity界面	
							intent.setClass(LogisPayActivity.this, LogistcaInforseActivity.class);//公司
							startActivity(intent);
							finish();
						}
						if (bean.getErrCode() == -2) {
							DialogUtils.createAlertDialogTwo(LogisPayActivity.this, "", bean.getMessage(), 0, "去充值",
									"更换支付方式", new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog, int which) {

											startActivity(new Intent(LogisPayActivity.this, RechargeActivity.class));
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
	 * 检查微信版本是否支持支付
	 * 
	 * @return
	 */
	private boolean checkWeiXinVersion() {
		boolean isPaySupported = msgApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
		return isPaySupported;
	}
}

