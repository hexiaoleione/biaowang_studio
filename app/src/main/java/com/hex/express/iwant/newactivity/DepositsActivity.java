package com.hex.express.iwant.newactivity;

import java.text.SimpleDateFormat;

import org.apache.http.Header;
import org.json.JSONObject;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.activities.BaseActivity;
import com.hex.express.iwant.activities.DepositActivity;
import com.hex.express.iwant.activities.HAdvertActivity;
import com.hex.express.iwant.activities.NewMyWalletActivity;
import com.hex.express.iwant.activities.RechargeActivity;
import com.hex.express.iwant.activities.RechargeActivity.Myrecive;
import com.hex.express.iwant.alipay.AlipayUtils;
import com.hex.express.iwant.alipay.PayResult;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.DeposBean;
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
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.content.BroadcastReceiver;
import android.content.Context;
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
import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * 
 * 交押金界面
*/

public class DepositsActivity  extends BaseActivity{
	
	@Bind(R.id.btnLeft)
	ImageView btnleft;//返回键
	@Bind(R.id.btnRight)
	TextView btnRight;
	@Bind(R.id.moss)
	TextView mos;
	
	@Bind(R.id.baoweixin)
	RelativeLayout baoweixin;
	@Bind(R.id.baozhifubao)
	RelativeLayout baozhifubao;
	@Bind(R.id.imgweixin)
	ImageView imgweixin;
	@Bind(R.id.imgzhifubao)
	ImageView imgzhifubao;
	@Bind(R.id.sumit)
	Button sumit;
	@Bind(R.id.yyshuo)
	TextView yyshuo;
	
	DeposBean		beanse;
	
	private  String type="1";
	private String orderInfo;
	private String result,billCode;
	
	PayReq req;
	final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
	private SimpleDateFormat sDateFormat;
	private String trandNum;
	private Myrecive reMyrecive;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deposint_activity);//newwallet_activity newmywallet_activity
		iWantApplication.getInstance().addActivity(this);
		ButterKnife.bind(DepositsActivity.this);
		req = new PayReq();
		msgApi.registerApp(CollectionKey.APP_ID);
		getData();
		initView();
		initData();
		setOnClick();
		getrequs();
		
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
		
	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub
		btnleft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
				
			}
		});
		btnRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 String url="http://www.efamax.com/mobile/explain/marginExplain.html";
				    Intent intent=new Intent();
					intent.putExtra("url", url);
					intent.setClass(DepositsActivity.this, HAdvertActivity.class);//公司
					startActivity(intent);
			}
		});
		yyshuo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 String url="http://www.efamax.com/mobile/explain/marginExplain.html";
				    Intent intent=new Intent();
					intent.putExtra("url", url);
					intent.setClass(DepositsActivity.this, HAdvertActivity.class);//公司
					startActivity(intent);
			}
		});
		
		baoweixin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				type="1";
				
				imgweixin.setBackgroundResource(R.drawable.yaxuanzhong);
				imgzhifubao.setBackgroundResource(R.drawable.yaweixuanzhong);
			}
		});
		baozhifubao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				type="2";

				imgweixin.setBackgroundResource(R.drawable.yaweixuanzhong);
				imgzhifubao.setBackgroundResource(R.drawable.yaxuanzhong);
			}
		});
		sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
		final String date = sDateFormat.format(new java.util.Date());
		sumit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (type.equals("1")) {
					MessageUtils.alertMessageCENTER(getApplicationContext(), "正在唤醒微信支付");
					if (!checkWeiXinVersion()) {
						ToastUtil.shortToast(getApplicationContext(), "微信版本不支持，请下载最新微信版本");
						return;
					}
					if (!TextUtils.isEmpty(beanse.getData().get(0).money)) {
						
						JSONObject obj = new JSONObject();
						try {
							obj.put("userId", PreferencesUtils.getInt(mActivity, PreferenceConstants.UID));
							obj.put("rechargeMoney", beanse.getData().get(0).money);
//							obj.put("recommandMobile", null);
						} catch (Exception e) {
							// TODO: handle exception
						}
						dialog.show();
						//logistics/task//pay/wechat/pre
						AsyncHttpUtils.doPostJson(getApplicationContext(), MCUrl.WECHATRECHARGEPRE, obj.toString(),
								new AsyncHttpResponseHandler() {

									@Override
									public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
										dialog.dismiss();
										if (arg2 == null)
											return;
										WechatBean bean = new Gson().fromJson(new String(arg2), WechatBean.class);
										Log.e("lllllllll}}}}}}]]]", new String(arg2));
										req.appId = bean.data.get(0).appId;
										req.partnerId = bean.data.get(0).partnerId;
										req.prepayId = bean.data.get(0).prepayid;
										req.nonceStr = bean.data.get(0).nonceStr;
										req.packageValue = bean.data.get(0).package_;
										req.sign = bean.data.get(0).sign;
										req.timeStamp = bean.data.get(0).timestamp;
										req.extData = "app data";
										billCode = bean.data.get(0).billCode;
										msgApi.registerApp(CollectionKey.APP_ID);
										msgApi.sendReq(req);

									}

									@Override
									public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
										dialog.dismiss();
									}
								});
				} 
					
				}else if (type.equals("2")) {
					orderInfo = AlipayUtils.getOrderInfo("镖师充值", "镖王充值", // 推荐手机号码
							beanse.getData().get(0).money + "", // 充值金额
							"http://www.efamax.com:8888/appservice-1.0-SNAPSHOT/jsp/alipay/receiveNotify.jsp",
							null, null, PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)
									+ "CZ" + date);
					trandNum = PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID) + "CZ" + date;
					sendEmptyBackgroundMessage(MsgConstants.MSG_01);
				}
			}
		});
	}
	private void getrequs() {
		RequestParams params = new RequestParams();
	
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.driverMoney, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID))), null, null, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
					}
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						Log.e("json", "" + new String(arg2));
								beanse = new Gson().fromJson(
								new String(arg2), DeposBean.class); 
						if (beanse.getErrCode()==0) {
							// 镖师押金     0 默认      1    已充值    2  退款中   3  已退款
							mos.setText(beanse.getData().get(0).money);
						
						}
					}

				});
	}
	public class Myrecive extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("recharge")) {
				getQueryrechargeResult();
			}

		}

	}
	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
		reMyrecive = new Myrecive();
		IntentFilter filter = new IntentFilter();
		filter.addAction("recharge");
		registerReceiver(reMyrecive, filter);
		
	}
	// 获取微信充值的结果
		public void getQueryrechargeResult() {
			if (getIntent().getStringExtra("way")!=null && !getIntent().getStringExtra("way").equals("")) {
	            
	            	AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.MONEYINLOGRESULT, "billCode", billCode), null, null, null,
	        				new AsyncHttpResponseHandler() {
	        					@Override
	        					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
	        						Log.e("msdjkg", new String(arg2));
	        						BaseBean	 bean = new Gson().fromJson(new String(arg2), BaseBean.class);
	        						if (bean.getErrCode()==0) {
	        							MessageUtils.alertMessageCENTER(DepositsActivity.this, "充值成功！");
	        							finish();
									}else {
										MessageUtils.alertMessageCENTER(DepositsActivity.this, ""+bean.getMessage());
	        							finish();
									}
//	        						if (!billCode.equals("")) {
//	        							MessageUtils.alertMessageCENTER(RechargeActivity.this, "充值成功");
//	        							finish();
//	        						}
	        						
	        					}

	        					@Override
	        					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
	        						finish();
	        					}
	        				});
				}
			
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
				getQueryResult(trandNum);
				Toast.makeText(DepositsActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
				finish();
			} else {
				// 判断resultStatus 为非“9000”则代表可能支付失败
				// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
				if (TextUtils.equals(resultStatus, "8000")) {
					Toast.makeText(DepositsActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

				} else {
					// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
					Toast.makeText(DepositsActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
					
				}
			}
			break;

		default:
			break;
		}
	}
	// 获取支付宝和微信支付的结果
		public void getQueryResult(String billCode) {
			String billCodesString;
			if (getIntent().getStringExtra("way")!=null && !getIntent().getStringExtra("way").equals("")) {
	            if (getIntent().getStringExtra("way").equals("2")) {
	          	  billCodesString=billCode+"D";
	          	String useridStrings=String.valueOf(PreferencesUtils.getInt(getApplicationContext(),PreferenceConstants.UID));
	          	String billCodes=getIntent().getStringExtra("billCode")+"D"+useridStrings;
	          	String useridString=String.valueOf(PreferencesUtils.getInt(getApplicationContext(),PreferenceConstants.UID));
	          	AsyncHttpUtils.doGet(UrlMap.getTwo(MCUrl.AndUserId, "userId", String
						.valueOf(PreferencesUtils.getInt(getApplicationContext(),
								PreferenceConstants.UID)),"billCode", billCodes), null, null, null,
						new AsyncHttpResponseHandler() {
	    					@Override
	    					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
	    						Log.e("msdjkg", new String(arg2));
	    						BaseBean bean=new Gson().fromJson(new String(arg2), BaseBean.class);
	    						if (bean.isSuccess()) {
	    							MessageUtils.alertMessageCENTER(getApplicationContext(), "充值成功");						
	    						}else {
	    							MessageUtils.alertMessageCENTER(getApplicationContext(), bean.getMessage());
	    						}
	    					}
	    					@Override
	    					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
	    						MessageUtils.alertMessageCENTER(getApplicationContext(), "充值失败");
	    					}
	    				});
	            }else {
	            	billCodesString=billCode;
				}
	            }else {
	            	AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.MONEYINLOGRESULT, "billCode", billCode), null, null, null,
	        				new AsyncHttpResponseHandler() {
	        					@Override
	        					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
	        						Log.e("msdjkg", new String(arg2));
	        						BaseBean bean=new Gson().fromJson(new String(arg2), BaseBean.class);
	        						if (bean.isSuccess()) {
	        							MessageUtils.alertMessageCENTER(getApplicationContext(), "充值成功");						
	        						}else {
	        							MessageUtils.alertMessageCENTER(getApplicationContext(), bean.getMessage());
	        						}
	        					}
	        					@Override
	        					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
	        						MessageUtils.alertMessageCENTER(getApplicationContext(), "充值失败");
	        					}
	        				});
				}
			
		}
		@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			unregisterReceiver(reMyrecive);
		}

}
